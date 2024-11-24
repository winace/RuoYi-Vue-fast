package com.ruoyi.project.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.R;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.project.system.domain.SysSimplePackage;
import com.ruoyi.project.system.domain.SysTenantPackage;
import com.ruoyi.project.system.service.ISysTenantPackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 租户套餐Controller
 *
 * @author zhaowang
 * @since 2024-11-22
 */
@RestController
@RequestMapping("/system/tenantpackage")
public class SysTenantPackageController extends BaseController {
    @Autowired
    private ISysTenantPackageService sysTenantPackageService;

    /**
     * 查询租户套餐列表
     */
    @PreAuthorize("@ss.hasPermi('system:tenantpackage:list')")
    @GetMapping("/list")
    public TableDataInfo<SysTenantPackage> list(SysTenantPackage sysTenantPackage) {
        IPage<SysTenantPackage> list = sysTenantPackageService.selectSysTenantPackageList(sysTenantPackage);
        return getDataTable(list);
    }

    /**
     * 查询租户套餐精简列表
     */
    @GetMapping("/get-simple-list")
    public R<List<SysSimplePackage>> getSimpleList() {
        List<SysSimplePackage> list = sysTenantPackageService.getSimpleList();
        return ok(list);
    }


    /**
     * 导出租户套餐列表
     */
    @PreAuthorize("@ss.hasPermi('system:tenantpackage:export')")
    @Log(title = "租户套餐", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysTenantPackage sysTenantPackage) {
        List<SysTenantPackage> list = sysTenantPackageService.selectSysTenantPackageExport(sysTenantPackage);
        ExcelUtil<SysTenantPackage> util = new ExcelUtil<SysTenantPackage>(SysTenantPackage.class);
        util.exportExcel(response, list, "租户套餐数据");
    }

    /**
     * 获取租户套餐详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:tenantpackage:query')")
    @GetMapping(value = "/{id}")
    public R<SysTenantPackage> getInfo(@PathVariable("id") Long id) {
        return ok(sysTenantPackageService.selectSysTenantPackageById(id));
    }

    /**
     * 新增租户套餐
     */
    @PreAuthorize("@ss.hasPermi('system:tenantpackage:add')")
    @Log(title = "租户套餐", businessType = BusinessType.INSERT)
    @PostMapping
    public R<Object> add(@RequestBody SysTenantPackage sysTenantPackage) {
        sysTenantPackageService.insertSysTenantPackage(sysTenantPackage);
        return ok(sysTenantPackage);
    }

    /**
     * 修改租户套餐
     */
    @PreAuthorize("@ss.hasPermi('system:tenantpackage:edit')")
    @Log(title = "租户套餐", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<Object> edit(@RequestBody SysTenantPackage sysTenantPackage) {
        int res = sysTenantPackageService.updateSysTenantPackage(sysTenantPackage);
        return toR(res);
    }

    /**
     * 删除租户套餐
     */
    @PreAuthorize("@ss.hasPermi('system:tenantpackage:remove')")
    @Log(title = "租户套餐", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Object> remove(@PathVariable Long[] ids) {
        int res = sysTenantPackageService.deleteSysTenantPackageByIds(ids);
        return toR(res);
    }
}
