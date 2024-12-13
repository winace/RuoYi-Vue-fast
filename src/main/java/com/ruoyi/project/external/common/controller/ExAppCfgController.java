package com.ruoyi.project.external.common.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.R;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.framework.web.page.TableSupport;
import com.ruoyi.project.external.common.domain.ExAppCfg;
import com.ruoyi.project.external.common.service.IExAppCfgService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * 应用配置 控制器
 *
 * @author zhaowang
 * @since 2024-11-26 10:04:39
 */
@RestController
@RequestMapping("/external/common/appCfg")
public class ExAppCfgController extends BaseController {
    @Autowired
    private IExAppCfgService service;

    @ApiOperation("查询应用配置分页列表")
    @PreAuthorize("@ss.hasPermi('common:appCfg:list')")
    @GetMapping("/page")
    public TableDataInfo<ExAppCfg> page(ExAppCfg exAppCfg) {
        Page<ExAppCfg> pageDto = TableSupport.getPageDomain().toPage();
        IPage<ExAppCfg> page = service.queryPage(pageDto, exAppCfg);
        return getDataTable(page);
    }

    @ApiOperation("查询应用配置列表")
    @PreAuthorize("@ss.hasPermi('common:appCfg:list')")
    @GetMapping("/list")
    public R<List<ExAppCfg>> list() {
        List<ExAppCfg> list = service.list();
        return ok(list);
    }

    @ApiOperation("导出应用配置列表")
    @PreAuthorize("@ss.hasPermi('common:appCfg:export')")
    @Log(title = "应用配置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ExAppCfg exAppCfg) {
        Page<ExAppCfg> pageDto = TableSupport.getPageDomain().toPage();
        List<ExAppCfg> list = Lists.newArrayList();
        IPage<ExAppCfg> page = service.queryPage(pageDto, exAppCfg);
        list.addAll(page.getRecords());
        while (page.getTotal() > 0 && page.getCurrent() < page.getPages()) {
            pageDto.setCurrent(page.getCurrent() + 1);
            page = service.queryPage(pageDto, exAppCfg);
            list.addAll(page.getRecords());
        }
        ExcelUtil<ExAppCfg> util = new ExcelUtil<ExAppCfg>(ExAppCfg.class);
        util.exportExcel(response, list, "应用配置数据");
    }

    @ApiOperation("获取应用配置详细信息")
    @PreAuthorize("@ss.hasPermi('common:appCfg:query')")
    @GetMapping("/{appId}")
    public R<ExAppCfg> getInfo(@PathVariable("appId") String appId) {
        return R.ok(service.getById(appId));
    }

    @ApiOperation("保存应用配置")
    @PreAuthorize("@ss.hasPermi('common:appCfg:save')")
    @Log(title = "应用配置", businessType = BusinessType.SAVE)
    @PostMapping
    public R<Object> save(@Validated @RequestBody ExAppCfg appCfg) {
        return toR(service.saveAppCfg(appCfg));
    }

    @ApiOperation("删除应用配置")
    @PreAuthorize("@ss.hasPermi('common:appCfg:remove')")
    @Log(title = "应用配置", businessType = BusinessType.DELETE)
    @DeleteMapping("/{appIds}")
    public R<Object> remove(@PathVariable String[] appIds) {
        return toR(service.removeBatchByIds(Arrays.asList(appIds)));
    }
}
