package com.ruoyi.project.app.controller;

import com.cwt.domain.entity.Grade;
import com.cwt.service.service.GradeService;
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
 * 用户等级 信息操作处理
 * 
 * @author ruoyi
 * @date 2018-08-24
 */
@Controller
@RequestMapping("/app/grade")
public class GradeController extends BaseController
{
    private String prefix = "app/grade";
	
	@Autowired
	private GradeService gradeService;
	
	@RequiresPermissions("app:grade:view")
	@GetMapping()
	public String grade()
	{
	    return prefix + "/grade";
	}
	
	/**
	 * 查询用户等级列表
	 */
	@RequiresPermissions("app:grade:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(Grade grade)
	{
		startPage();
        List<Grade> list = gradeService.selectGradeList(grade);
		return getDataTable(list);
	}
	
	/**
	 * 新增用户等级
	 */
	@GetMapping("/add")
	public String add()
	{
	    return prefix + "/add";
	}
	
	/**
	 * 新增保存用户等级
	 */
	@RequiresPermissions("app:grade:add")
	@Log(title = "用户等级", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(Grade grade)
	{		
		return toAjax(gradeService.insertGrade(grade));
	}

	/**
	 * 修改用户等级
	 */
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") Integer id, ModelMap mmap)
	{
		Grade grade = gradeService.selectGradeById(id);
		mmap.put("grade", grade);
	    return prefix + "/edit";
	}
	
	/**
	 * 修改保存用户等级
	 */
	@RequiresPermissions("app:grade:edit")
	@Log(title = "用户等级", businessType = BusinessType.UPDATE)
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(Grade grade)
	{		
		return toAjax(gradeService.updateGrade(grade));
	}
	
	/**
	 * 删除用户等级
	 */
	@RequiresPermissions("app:grade:remove")
	@Log(title = "用户等级", businessType = BusinessType.DELETE)
	@PostMapping( "/remove")
	@ResponseBody
	public AjaxResult remove(String id)
	{		
		return toAjax(gradeService.deleteGradeById(id));
	}
	
}
