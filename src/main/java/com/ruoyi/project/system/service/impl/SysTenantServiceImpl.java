package com.ruoyi.project.system.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.msg.EmailUtil;
import com.ruoyi.common.utils.msg.SmsUtil;
import com.ruoyi.common.utils.redis.ConfigUtil;
import com.ruoyi.framework.mybatis.util.TenantUtils;
import com.ruoyi.framework.redis.RedisCache;
import com.ruoyi.framework.security.LoginUser;
import com.ruoyi.framework.web.page.TableSupport;
import com.ruoyi.project.system.domain.*;
import com.ruoyi.project.system.mapper.*;
import com.ruoyi.project.system.service.ISysTenantService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 租户管理Service业务层处理
 *
 * @author zhaowang
 * @since 2024-11-22
 */
@Slf4j
@Service
public class SysTenantServiceImpl implements ISysTenantService {
    @Autowired
    private SysTenantMapper sysTenantMapper;

    @Autowired
    private SysTenantPackageMapper sysTenantPackageMapper;

    @Autowired
    private SysDeptMapper deptMapper;

    @Autowired
    private SysPostMapper sysPostMapper;

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private SysRoleMenuMapper sysRoleMenuMapper;

    @Autowired
    private SysRoleDeptMapper sysRoleDeptMapper;

    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    private SysUserRoleMapper userRoleMapper;

    @Autowired
    private SysUserPostMapper userPostMapper;

    @Autowired
    private EmailUtil emailUtil;

    @Autowired
    private SmsUtil smsUtil;

    @Autowired
    private RedisCache redisCache;


    /**
     * 查询租户管理
     *
     * @param id 租户管理主键
     * @return 租户管理
     */
    @Override
    public SysTenant selectSysTenantById(Long id) {
        return sysTenantMapper.selectById(id);
    }

    /**
     * 查询租户管理列表-分页
     *
     * @param sysTenant 租户管理
     * @return 租户管理
     */
    @Override
    public IPage<SysTenant> selectSysTenantPage(SysTenant sysTenant) {
        Page<SysTenant> pageDto = TableSupport.getPageDomain().toPage();
        return sysTenantMapper.selectSysTenantList(pageDto, sysTenant);
    }

    /**
     * 查询租户管理列表
     *
     * @param sysTenant 租户管理
     * @return 租户管理
     */
    @Override
    public List<SysTenant> selectSysTenantList(SysTenant sysTenant) {
        return sysTenantMapper.selectSysTenantList(sysTenant);
    }

    /**
     * 新增租户管理
     *
     * @param sysTenant 租户管理
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insertSysTenant(SysTenant sysTenant) {
        if (StringUtils.isEmpty(sysTenant.getUserName())) {
            throw new ServiceException("管理员账号为空,请重新设置!");
        }
        //先判断租户管理员设置的账号是否存在
        SysUser usercount = userMapper.checkUserNameUnique(sysTenant.getUserName());
        if (!(usercount == null)) {
            throw new ServiceException("用户名已存在,请重新设置!");
        }
        //创建租户
        sysTenantMapper.insert(sysTenant);
        //租户创建完成后 开始创建相关基础数据
        TenantUtils.execute(sysTenant.getId(), () -> {
            //创建默认部门--部门默认名称以租户名称
            Long deptId = createDept(sysTenant);
            //创建默认岗位--岗位默认为董事长
            Long postId = createPost(sysTenant.getUserName());
            //创建默认角色--角色默认为租户名称+管理员
            Long roleId = createRole(sysTenant);
            //创建默认账号
            createUser(sysTenant, deptId, postId, roleId);
        });
        return true;
    }

    private void createUser(SysTenant sysTenant, Long deptId, Long postId, Long roleId) {
        SysUser user = new SysUser();
        user.setDeptId(deptId).setUserName(sysTenant.getUserName()).setNickName(sysTenant.getTenantName())
                //用户类型 00 表示各管理员账号，不允许租户修改删除 其他账号为10
                .setUserType("00")
                .setEmail(sysTenant.getUserEmail()).setPhonenumber(sysTenant.getUserPhone()).setRemark("租户管理员");
        //默认密码 采用随机生成8位数字
        String randomPassword = RandomStringUtils.randomNumeric(8);

        String password = SecurityUtils.encryptPassword(randomPassword);
        user.setPassword(password);
        userMapper.insert(user);
        userPostMapper.insert(new SysUserPost().setUserId(user.getUserId()).setPostId(postId));
        userRoleMapper.insert(new SysUserRole().setRoleId(roleId).setUserId(user.getUserId()));
        String configValue = ConfigUtil.getCacheKey("sys.message.type");
        if (!Boolean.parseBoolean(configValue)) {
            //调用邮件通知
            emailUtil.sendSimpleMail("租户管理员账号注册成功", "请牢记登录密码:" + randomPassword, sysTenant.getUserEmail());
        } else {
            try {
                //短信
                smsUtil.send(sysTenant.getUserPhone(), randomPassword);
            } catch (Exception e) {
                log.info("短信调用失败:{}", e.getMessage());
                //调用邮件通知
                emailUtil.sendSimpleMail("租户管理员账号注册成功", "请牢记登录密码:" + randomPassword, sysTenant.getUserEmail());
            }
        }
    }

    private Long createRole(SysTenant sysTenant) {
        // 创建角色
        SysRole role = new SysRole();
        role.setRoleName(sysTenant.getTenantName() + "管理员").setRoleKey("admin")
                .setRoleSort(1).setDataScope("1").setMenuCheckStrictly(true).setDeptCheckStrictly(true);
        role.setCreateBy(sysTenant.getUserName());
        role.setRemark("租户管理员");
        role.setAdminRole(true);
        sysRoleMapper.insertRole(role);
        //根据租户套餐ids查出套餐编码塞入角色-菜单表
        createRoleMenu(sysTenant, role);
        return role.getRoleId();
    }

    //目前为单套餐,跟租户绑定,解耦防止套餐变动影响多个租户
    private void createRoleMenu(SysTenant sysTenant, SysRole role) {
        SysTenantPackage sysTenantPackage = sysTenantPackageMapper.selectById(sysTenant.getTenantPackage());
        List<String> subMenus = Arrays.asList(sysTenantPackage.getMenuIds().split(","));

        List<SysRoleMenu> roleMenuList = subMenus.stream().map(menuid -> {
            SysRoleMenu entity = new SysRoleMenu();
            entity.setRoleId(role.getRoleId());
            entity.setMenuId(Convert.toLong(menuid));
            return entity;
        }).collect(Collectors.toList());
        sysRoleMenuMapper.batchRoleMenu(roleMenuList);
    }

    private Long createPost(String username) {
        SysPost post = new SysPost();
        post.setPostCode("ceo").setPostName("董事长").setPostSort(1);
        post.setCreateBy(username);
        sysPostMapper.insert(post);
        return post.getPostId();
    }

    private Long createDept(SysTenant sysTenant) {
        // 创建部门
        SysDept dept = new SysDept();
        dept.setParentId(0L).setAncestors("0").setDeptName(sysTenant.getTenantName()).setOrderNum(0)
                .setLeader(sysTenant.getTenantName() + "管理员").setPhone(sysTenant.getUserPhone()).setEmail(sysTenant.getUserEmail());
        deptMapper.insert(dept);
        return dept.getDeptId();
    }


    /**
     * 修改租户管理
     *
     * @param sysTenant 租户管理
     * @return 结果
     */
    @Override
    public int updateSysTenant(SysTenant sysTenant) {
        //判断最新的租户套餐是否改变 重新授权 租户二级管理员账号需重新分配三级账号权限
        SysTenant tSysTenant = sysTenantMapper.selectById(sysTenant.getId());
        if (sysTenant.getTenantPackage() != null && !sysTenant.getTenantPackage().equals(tSysTenant.getTenantPackage())) {
            //正常逻辑下每个租户只有一个二级管理员账号
            SysRole tRole = sysRoleMapper.queryAdminRole(sysTenant.getId());
            if (tRole != null) {
                //删除原租户下所有的角色-菜单信息
                sysRoleMenuMapper.deleteRoleMenuByTenantId(sysTenant.getId());
                //新增默认角色-菜单信息
                TenantUtils.execute(sysTenant.getId(), () -> createRoleMenu(sysTenant, tRole));
                // TODO  此处需要优化,如果当前同时在线人数较多,会出现卡顿
                //原登录租户账号退出重登 租户二级管理员账号需重新分配三级账号权限
                Collection<String> keys = redisCache.keys(CacheConstants.LOGIN_TOKEN_KEY + "*");
                for (String key : keys) {
                    LoginUser onlineUser = redisCache.getCacheObject(key);
                    if (onlineUser.getUser().getTenantId() != null && onlineUser.getUser().getTenantId().equals(sysTenant.getId())) {
                        redisCache.deleteObject(key);
                    }
                }
            }
        }
        return sysTenantMapper.updateById(sysTenant);
    }

    /**
     * 批量删除租户管理
     *
     * @param ids 需要删除的租户管理主键
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteSysTenantByIds(Long[] ids) {
        //优化删除逻辑
        //1.先删租户
        int tenantRes = sysTenantMapper.deleteSysTenantByIds(ids);
        if (tenantRes > 0) {
            //下面才会进行子模块数据的删除
            //部门模块
            deptMapper.deleteDeptByTenantId(ids);
            //职位模块
            sysPostMapper.deletePostByTenantId(ids);
            //权限
            sysRoleMapper.deleteRoleByTenantId(ids);
            sysRoleMenuMapper.deleteRoleMenuByTenantIds(ids);
            sysRoleDeptMapper.deleteRoleDeptByTenantId(ids);
            //账号
            userMapper.deleteUserByTenantId(ids);
            userRoleMapper.deleteUserRoleByTenantId(ids);
            userPostMapper.deleteUserPostByTenantId(ids);
            return 1;
        } else {
            throw new ServiceException("当前租户已被删除不存在！");
        }
    }

    /**
     * 删除租户管理信息
     *
     * @param id 租户管理主键
     * @return 结果
     */
    @Override
    public int deleteSysTenantById(Long id) {
        return sysTenantMapper.deleteById(id);
    }
}
