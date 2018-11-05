package com.ruoyi.project.app.controller;

import com.cwt.domain.dto.information.InformationForCtRulesDTO;
import com.cwt.domain.entity.Information;
import com.cwt.service.service.InformationService;
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
 * 自定义 信息操作处理
 * 
 * @author ruoyi
 * @date 2018-08-24
 */
@Controller
@RequestMapping("/app/information")
public class InformationController extends BaseController
{
    private String prefix = "app/information";
	
	@Autowired
	private InformationService informationService;
	
	@RequiresPermissions("app:information:view")
	@GetMapping()
	public String information()
	{
	    return prefix + "/information";
	}
	
	/**
	 * 查询自定义列表
	 */
	@RequiresPermissions("app:information:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(Information information)
	{
		startPage();
        List<Information> list = informationService.selectInformationList(information);
		return getDataTable(list);
	}

	/**
	 * 查询ct兑换规则比例--页面
	 */
	@GetMapping("/selectCtRulesListPage")
	public String selectCtRulesListPage()
	{
		return prefix + "/ct_rules";
	}

	/**
	 * 查询ct兑换规则比例
	 */
	@RequiresPermissions("app:information:list")
	@PostMapping("/selectCtRulesList")
	@ResponseBody
	public TableDataInfo selectCtRulesList(){
		return getDataTable(informationService.selectCtRulesList());
	}

	/**
	 * ct兑换规则比例修改--页面
	 */
	@GetMapping("/ctRulesEditPage")
	public String ctRulesEditPage(ModelMap mmap)
	{
		mmap.put("ctRules", informationService.selectCtRulesList().get(0));
		return prefix + "/ct_rules_edit";
	}

	/**
	 * ct兑换规则比例修改
	 */
	@RequiresPermissions("app:information:edit")
	@Log(title = "自定义", businessType = BusinessType.UPDATE)
	@PostMapping("/ctRulesEdit")
	@ResponseBody
	public AjaxResult ctRulesEdit(InformationForCtRulesDTO informationForCtRulesDTO){
		return toAjax(informationService.updateCtRules(informationForCtRulesDTO));
	}
	
	/**
	 * 新增自定义
	 */
	@GetMapping("/add")
	public String add()
	{
	    return prefix + "/add";
	}
	
	/**
	 * 新增保存自定义
	 */
	@RequiresPermissions("app:information:add")
	@Log(title = "自定义", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(Information information)
	{		
		return toAjax(informationService.insertInformation(information));
	}

	/**
	 * 修改自定义
	 */
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") Integer id, ModelMap mmap)
	{
		Information information = informationService.selectInformationById(id);
		mmap.put("information", information);
	    return prefix + "/edit";
	}
	
	/**
	 * 修改保存自定义
	 */
	@RequiresPermissions("app:information:edit")
	@Log(title = "自定义", businessType = BusinessType.UPDATE)
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(Information information)
	{
//		System.out.println("##################进来了#############");
		return toAjax(informationService.updateInformation(information));
	}
	
	/**
	 * 删除自定义
	 */
//	@RequiresPermissions("app:information:remove")
//	@Log(title = "自定义", businessType = BusinessType.DELETE)
//	@PostMapping( "/remove")
//	@ResponseBody
//	public AjaxResult remove(String ids)
//	{
//		return toAjax(informationService.deleteInformationByIds(ids));
//	}
	
}
