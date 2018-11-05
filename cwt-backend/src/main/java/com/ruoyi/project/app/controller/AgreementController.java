package com.ruoyi.project.app.controller;

import com.cwt.domain.dto.mySettings.AgreementDto;
import com.cwt.service.service.AgreementService;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.TableDataInfo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * 关于我们 信息操作处理
 *
 * @author ruoyi
 * @date 2018-08-27
 */
@Controller
@RequestMapping("/app/agreement")
public class AgreementController extends BaseController {
    private String prefix = "app/agreement";

    @Autowired
    private AgreementService agreementService;

    @RequiresPermissions("app:agreement:view")
    @GetMapping()
    public String agreement() {
        return prefix + "/agreement";
    }

    @RequiresPermissions("app:agreement:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(AgreementDto agreement) {
        return getDataTable(agreementService.selectAgreement());
    }

    /**
     * 修改
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") String id, ModelMap mmap) {
        AgreementDto agreement = agreementService.selectAgreement().get(0);
        mmap.put("agreement", agreement);
        return prefix + "/edit";
    }

    /**
     * 修改保存
     */
    @RequiresPermissions("app:agreement:edit")
    @Log(title = "关于我们", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(AgreementDto agreement) {
        agreement.setModifyTime(new Date());
        return toAjax(agreementService.updateAgreement(agreement));
    }

}
