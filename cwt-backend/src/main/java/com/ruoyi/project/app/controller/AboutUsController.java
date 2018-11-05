package com.ruoyi.project.app.controller;

import com.cwt.domain.dto.mySettings.AboutUsDto;
import com.cwt.service.service.IAboutUsService;
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

import java.util.List;

/**
 * 关于我们 信息操作处理
 *
 * @author ruoyi
 * @date 2018-08-27
 */
@Controller
@RequestMapping("/app/aboutUs")
public class AboutUsController extends BaseController {
    private String prefix = "app/aboutUs";

    @Autowired
    private IAboutUsService aboutUsService;

    @RequiresPermissions("app:aboutUs:view")
    @GetMapping()
    public String aboutUs() {
        return prefix + "/aboutUs";
    }

    /**
     * 查询关于我们列表
     */
    @RequiresPermissions("app:aboutUs:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(AboutUsDto aboutUs) {
        startPage();
        List<AboutUsDto> list = aboutUsService.selectAboutUsList(aboutUs);
        return getDataTable(list);
    }

    /**
     * 新增关于我们
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存关于我们
     */
    @RequiresPermissions("app:aboutUs:add")
    @Log(title = "关于我们", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(AboutUsDto aboutUs) {
        System.out.println("########################################新增保存关于我们"+ aboutUs.getTextContent());
        return toAjax(aboutUsService.insertAboutUs(aboutUs));
    }

    /**
     * 修改关于我们
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") String id, ModelMap mmap) {
        AboutUsDto aboutUs = aboutUsService.selectAboutUsById(id);
        mmap.put("aboutUs", aboutUs);
        return prefix + "/edit";
    }

    /**
     * 修改保存关于我们
     */
    @RequiresPermissions("app:aboutUs:edit")
    @Log(title = "关于我们", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(AboutUsDto aboutUs) {
        return toAjax(aboutUsService.updateAboutUs(aboutUs));
    }

    /**
     * 删除关于我们
     */
    @RequiresPermissions("app:aboutUs:remove")
    @Log(title = "关于我们", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(aboutUsService.deleteAboutUsByIds(ids));
    }

}
