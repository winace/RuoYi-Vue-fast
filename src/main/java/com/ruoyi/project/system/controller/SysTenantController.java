package com.ruoyi.project.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.R;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.project.system.domain.SysTenant;
import com.ruoyi.project.system.service.ISysTenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 租户管理Controller
 *
 * @author zhaowang
 * @since 2024-11-22
 */
@RestController
@RequestMapping("/system/tenant")
public class SysTenantController extends BaseController {
    @Autowired
    private ISysTenantService sysTenantService;

    /**
     * 查询租户管理列表
     */
    @PreAuthorize("@ss.hasPermi('system:tenant:list')")
    @GetMapping("/list")
    public TableDataInfo<SysTenant> list(SysTenant sysTenant) {
        IPage<SysTenant> list = sysTenantService.selectSysTenantPage(sysTenant);
        return getDataTable(list);
    }

    /**
     * 导出租户管理列表
     */
    @PreAuthorize("@ss.hasPermi('system:tenant:export')")
    @Log(title = "租户管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysTenant sysTenant) {
        List<SysTenant> list = sysTenantService.selectSysTenantList(sysTenant);
        ExcelUtil<SysTenant> util = new ExcelUtil<SysTenant>(SysTenant.class);
        util.exportExcel(response, list, "租户管理数据");
    }

    /**
     * 获取租户管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:tenant:query')")
    @GetMapping(value = "/{id}")
    public R<SysTenant> getInfo(@PathVariable("id") Long id) {
        return ok(sysTenantService.selectSysTenantById(id));
    }

    /**
     * 新增租户管理
     */
    @PreAuthorize("@ss.hasPermi('system:tenant:add')")
    @Log(title = "租户管理", businessType = BusinessType.INSERT)
    @PostMapping
    public R<Object> add(@RequestBody SysTenant sysTenant) {
        boolean b = sysTenantService.insertSysTenant(sysTenant);
        return ok(sysTenant);
    }

    /**
     * 修改租户管理
     */
    @PreAuthorize("@ss.hasPermi('system:tenant:edit')")
    @Log(title = "租户管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<Object> edit(@RequestBody SysTenant sysTenant) {
        return toR(sysTenantService.updateSysTenant(sysTenant));
    }


    /**
     * 重置租户管理员密码
     */
    @PreAuthorize("@ss.hasPermi('system:tenant:resetPwd')")
    @Log(title = "重置租户管理员密码", businessType = BusinessType.UPDATE)
    @PostMapping("/resetTenantUserPwd/{id}")
    public R<Object> resetTenantUserPwd(@PathVariable String id) {
        return toR(sysTenantService.resetTenantUserPwd(id));
    }

    /**
     * 删除租户管理
     */
    @PreAuthorize("@ss.hasPermi('system:tenant:remove')")
    @Log(title = "租户管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Object> remove(@PathVariable Long[] ids) {
        return toR(sysTenantService.deleteSysTenantByIds(ids));
    }
}
