package com.ruoyi.project.external.common.controller;

import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.R;
import com.ruoyi.project.external.common.domain.ExModuleFunc;
import com.ruoyi.project.external.common.service.IExModuleFuncService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * 应用功能 控制器
 *
 * @author zhaowang
 * @since 2024-11-26 10:04:40
 */
@RestController
@RequestMapping("/external/common/moduleFunc")
public class ExModuleFuncController extends BaseController {
    @Autowired
    private IExModuleFuncService service;

    @ApiOperation("查询应用功能列表")
    @PreAuthorize("@ss.hasPermi('common:moduleFunc:list')")
    @GetMapping("/list")
    public R<List<ExModuleFunc>> list(ExModuleFunc moduleFunc) {
        List<ExModuleFunc> list = service.queryList(moduleFunc);
        return R.ok(list);
    }

    @ApiOperation("查询应用功能树")
    @PreAuthorize("@ss.hasPermi('common:moduleFunc:list')")
    @GetMapping("/tree")
    public R<List<ExModuleFunc>> tree(ExModuleFunc moduleFunc) {
        List<ExModuleFunc> tree = service.queryTreeList(moduleFunc);
        return R.ok(tree);
    }

    @ApiOperation("导出应用功能列表")
    @PreAuthorize("@ss.hasPermi('common:moduleFunc:export')")
    @Log(title = "应用功能", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ExModuleFunc moduleFunc) {
        List<ExModuleFunc> list = service.queryList(moduleFunc);
        ExcelUtil<ExModuleFunc> util = new ExcelUtil<ExModuleFunc>(ExModuleFunc.class);
        util.exportExcel(response, list, "应用功能数据");
    }

    @ApiOperation("获取应用功能详细信息")
    @PreAuthorize("@ss.hasPermi('common:moduleFunc:query')")
    @GetMapping("/{id}")
    public R<ExModuleFunc> getInfo(@PathVariable("id") Long id) {
        return R.ok(service.getById(id));
    }

    @ApiOperation("新增应用功能")
    @PreAuthorize("@ss.hasPermi('common:moduleFunc:add')")
    @Log(title = "应用功能", businessType = BusinessType.INSERT)
    @PostMapping
    public R<Object> add(@Validated @RequestBody ExModuleFunc moduleFunc) {
        return toR(service.saveModuleFunc(moduleFunc));
    }

    @ApiOperation("修改应用功能")
    @PreAuthorize("@ss.hasPermi('common:moduleFunc:edit')")
    @Log(title = "应用功能", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<Object> edit(@Validated @RequestBody ExModuleFunc moduleFunc) {
        return toR(service.updateModuleFunc(moduleFunc));
    }

    @ApiOperation("删除应用功能")
    @PreAuthorize("@ss.hasPermi('common:moduleFunc:remove')")
    @Log(title = "应用功能", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Object> remove(@PathVariable Long[] ids) {
        return toR(service.removeBatchByIds(Arrays.asList(ids)));
    }
}
