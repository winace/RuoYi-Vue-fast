package com.ruoyi.project.external.common.controller;

import com.ruoyi.common.utils.bean.Builder;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.R;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.framework.web.page.TableSupport;
import com.ruoyi.project.external.common.domain.ExModuleFunc;
import com.ruoyi.project.external.common.domain.ExUser;
import com.ruoyi.project.external.common.service.IExModuleFuncService;
import com.ruoyi.project.external.common.service.IExUserService;
import com.ruoyi.project.external.common.vo.ExUserGrantVo;
import com.ruoyi.project.external.common.vo.ExUserVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * 应用用户 控制器
 *
 * @author zhaowang
 * @since 2024-11-26 22:48:47
 */
@RestController
@RequestMapping("/external/common/exUser")
public class ExUserController extends BaseController {
    @Autowired
    private IExUserService service;
    @Autowired
    private IExModuleFuncService moduleFuncService;

    @ApiOperation("查询应用用户列表")
    @PreAuthorize("@ss.hasPermi('common:exUser:list')")
    @GetMapping("/list")
    public TableDataInfo<ExUserVo> list(ExUser user) {
        Page<ExUser> pageDto = TableSupport.getPageDomain().toPage();
        IPage<ExUserVo> page = service.queryPage(pageDto, user);
        return getDataTable(page);
    }

    @ApiOperation("导出应用用户列表")
    @PreAuthorize("@ss.hasPermi('common:exUser:export')")
    @Log(title = "应用用户", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ExUser user) {
        Page<ExUser> pageDto = TableSupport.getPageDomain().toPage();
        List<ExUserVo> list = Lists.newArrayList();
        IPage<ExUserVo> page = service.queryPage(pageDto, user);
        list.addAll(page.getRecords());
        while (page.getTotal() > 0 && page.getCurrent() < page.getPages()) {
            pageDto.setCurrent(page.getCurrent() + 1);
            page = service.queryPage(pageDto, user);
            list.addAll(page.getRecords());
        }
        ExcelUtil<ExUserVo> util = new ExcelUtil<>(ExUserVo.class);
        util.exportExcel(response, list, "应用用户数据");
    }

    @ApiOperation("获取应用用户详细信息")
    @PreAuthorize("@ss.hasPermi('common:exUser:query')")
    @GetMapping("/{openid}")
    public R<ExUser> getInfo(@PathVariable("openid") String openid) {
        return R.ok(service.getById(openid));
    }

    @ApiOperation("保存应用用户")
    @PreAuthorize("@ss.hasPermi('common:exUser:save')")
    @Log(title = "应用用户", businessType = BusinessType.UPDATE)
    @PostMapping
    public R<Object> save(@Validated @RequestBody ExUser user) {
        return toR(service.saveUser(user));
    }

    @ApiOperation("获取应用用户授权信息")
    @PreAuthorize("@ss.hasPermi('common:exUser:grant')")
    @GetMapping("/grant/{openid}")
    public R<ExUserGrantVo> getGrantInfo(@PathVariable("openid") String openid) {
        ExUser user = service.getById(openid);
        ExModuleFunc moduleFunc = Builder.of(ExModuleFunc::new).with(ExModuleFunc::setAppId, user.getAppId()).build();
        List<ExModuleFunc> treeList = moduleFuncService.queryTreeList(moduleFunc);
        ExUserGrantVo vo = new ExUserGrantVo(user, treeList);
        return R.ok(vo);
    }

    @ApiOperation("应用用户授权")
    @PreAuthorize("@ss.hasPermi('common:exUser:grant')")
    @Log(title = "应用用户授权", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<Object> grant(@RequestBody ExUser user) {
        return toR(service.saveUser(user));
    }

    @ApiOperation("删除应用用户")
    @PreAuthorize("@ss.hasPermi('common:exUser:remove')")
    @Log(title = "应用用户", businessType = BusinessType.DELETE)
    @DeleteMapping("/{openids}")
    public R<Object> remove(@PathVariable String[] openids) {
        return toR(service.removeBatchByIds(Arrays.asList(openids)));
    }
}
