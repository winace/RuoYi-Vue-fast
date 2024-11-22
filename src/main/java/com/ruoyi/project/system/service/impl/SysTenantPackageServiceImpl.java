package com.ruoyi.project.system.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.redis.RedisCache;
import com.ruoyi.framework.security.LoginUser;
import com.ruoyi.framework.web.page.TableSupport;
import com.ruoyi.project.system.domain.*;
import com.ruoyi.project.system.mapper.SysRoleMapper;
import com.ruoyi.project.system.mapper.SysRoleMenuMapper;
import com.ruoyi.project.system.mapper.SysTenantPackageMapper;
import com.ruoyi.project.system.service.ISysTenantPackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * 租户套餐Service业务层处理
 *
 * @author zhaowang
 * @since 2024-11-22
 */
@Service
public class SysTenantPackageServiceImpl implements ISysTenantPackageService {
    @Autowired
    private SysTenantPackageMapper sysTenantPackageMapper;

    @Autowired
    private SysRoleMenuMapper sysRoleMenuMapper;

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private RedisCache redisCache;

    @Override
    public SysTenantPackage selectSysTenantPackageById(Long id) {
        return sysTenantPackageMapper.selectById(id);
    }

    @Override
    public List<SysSimplePackage> getSimpleList() {
        return sysTenantPackageMapper.getSimpleList();
    }

    @Override
    public IPage<SysTenantPackage> selectSysTenantPackageList(SysTenantPackage sysTenantPackage) {
        Page<SysTenantPackage> pageDto = TableSupport.getPageDomain().toPage();
        return sysTenantPackageMapper.selectSysTenantPackageList(pageDto, sysTenantPackage);
    }

    @Override
    public int insertSysTenantPackage(SysTenantPackage sysTenantPackage) {
        return sysTenantPackageMapper.insert(sysTenantPackage);
    }

    /**
     * 修改租户套餐
     *
     * @param sysTenantPackage 租户套餐
     * @return 结果
     */
    @Override
    @Transactional
    public int updateSysTenantPackage(SysTenantPackage sysTenantPackage) {
        //判断是否有 租户使用此套餐
        if (sysTenantPackage.getStatus() == 1) {
            Integer activeTenants = sysTenantPackageMapper.getActiveTenantByPackage(sysTenantPackage.getId());
            if (activeTenants > 0) {
                //目前有正常的租户在使用此套餐，不允许关闭套餐
                throw new ServiceException("租户套餐已经被使用，无法被停用！");
            }
        }
        //租户套餐修改逻辑优化,踢掉当前所有登陆此套餐的用户
        //先查询租户套餐有没有被改变
        SysTenantPackage old = sysTenantPackageMapper.selectById(sysTenantPackage.getId());
        Map<String, String[]> packageIsChange = StringUtils.compareStringArray(old.getMenuIds().split(","), sysTenantPackage.getMenuIds().split(","));
        boolean excludeUser = packageIsChange.get("deleteArr") != null && packageIsChange.get("deleteArr").length > 0;
        boolean addUserMenu = packageIsChange.get("addArr") != null && packageIsChange.get("addArr").length > 0;
        if (excludeUser || addUserMenu) {
            //查询当前套餐跟哪些租户做了绑定
            List<SysTenant> tenants = sysTenantPackageMapper.getTenantByPackage(sysTenantPackage.getId());
            tenants.forEach(tenant -> {
                //先删除被去掉的菜单
                if (excludeUser) {
                    sysRoleMenuMapper.deleteRoleMenuByTenantIdAndPackage(tenant.getId(), StringUtils.stringToLong(packageIsChange.get("deleteArr")));
                }
                //只给租户管理员添加新增的菜单
                if (addUserMenu) {
                    //查询当前租户的管理员角色id
                    SysRole role = sysRoleMapper.selectRoleByTenant(tenant.getId());
                    List<SysRoleMenu> roleMenuList = Arrays.stream(packageIsChange.get("addArr")).map(menuId -> {
                        SysRoleMenu entity = new SysRoleMenu();
                        entity.setRoleId(role.getRoleId());
                        entity.setMenuId(Convert.toLong(menuId));
                        entity.setTenantId(tenant.getId());
                        return entity;
                    }).collect(Collectors.toList());
                    sysRoleMenuMapper.batchRoleMenuByPackage(roleMenuList);
                }
                //踢掉当前租户下的用户
                //只有原始套餐有菜单被删除的时候才会涉及到用户权限问题
                if (excludeUser) {
                    //踢掉当前用户,刷新已有权限
                    // TODO  此处需要优化,如果当前同时在线人数较多,会出现卡顿
                    Collection<String> keys = redisCache.keys(CacheConstants.LOGIN_TOKEN_KEY + "*");
                    for (String key : keys) {
                        LoginUser onlineUser = redisCache.getCacheObject(key);
                        if (onlineUser.getUser().getTenantId() != null && onlineUser.getUser().getTenantId().equals(tenant.getId())) {
                            redisCache.deleteObject(key);
                        }
                    }
                }
            });
        }
        return sysTenantPackageMapper.updateById(sysTenantPackage);
    }

    /**
     * 批量删除租户套餐
     *
     * @param ids 需要删除的租户套餐主键
     * @return 结果
     */
    @Override
    public int deleteSysTenantPackageByIds(Long[] ids) {
        //判断是否有 租户使用此套餐
        Integer activeTenants = sysTenantPackageMapper.getActiveTenantByPackage(ids[0]);
        if (activeTenants > 0) {
            //目前有正常的租户在使用此套餐，不允许关闭套餐
            throw new ServiceException("租户套餐已经被使用，无法被删除！");
        }
        return sysTenantPackageMapper.deleteSysTenantPackageByIds(ids);
    }

    /**
     * 查询导出
     *
     * @return 租户套餐
     */
    @Override
    public List<SysTenantPackage> selectSysTenantPackageExport(SysTenantPackage sysTenantPackage) {
        return sysTenantPackageMapper.selectSysTenantPackageExport(sysTenantPackage);
    }
}
