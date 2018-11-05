package com.ruoyi.project.app.controller;

import com.cwt.domain.dto.mySettings.ComplaintProposalDto;
import com.cwt.service.service.IComplaintProposalService;
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
 * 投诉建议 信息操作处理
 * 
 * @author ruoyi
 * @date 2018-08-27
 */
@Controller
@RequestMapping("/app/complaintProposal")
public class ComplaintProposalController extends BaseController
{
    private String prefix = "app/complaintProposal";
	
	@Autowired
	private IComplaintProposalService complaintProposalService;
	
	@RequiresPermissions("app:complaintProposal:view")
	@GetMapping()
	public String complaintProposal()
	{
	    return prefix + "/complaintProposal";
	}
	
	/**
	 * 查询投诉建议列表
	 */
	@RequiresPermissions("app:complaintProposal:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(ComplaintProposalDto complaintProposal)
	{
		startPage();
        List<ComplaintProposalDto> list = complaintProposalService.selectComplaintProposalList(complaintProposal);
		return getDataTable(list);
	}
	
	/**
	 * 新增投诉建议
	 */
	@GetMapping("/add")
	public String add()
	{
	    return prefix + "/add";
	}
	
	/**
	 * 新增保存投诉建议
	 */
	@RequiresPermissions("app:complaintProposal:add")
	@Log(title = "投诉建议", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(ComplaintProposalDto complaintProposal)
	{		
		return toAjax(complaintProposalService.insertComplaintProposal(complaintProposal));
	}

	/**
	 * 修改投诉建议
	 */
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") String id, ModelMap mmap)
	{
		ComplaintProposalDto complaintProposal = complaintProposalService.selectComplaintProposalById(id);
		mmap.put("complaintProposal", complaintProposal);
	    return prefix + "/edit";
	}
	
	/**
	 * 修改保存投诉建议
	 */
	@RequiresPermissions("app:complaintProposal:edit")
	@Log(title = "投诉建议", businessType = BusinessType.UPDATE)
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(ComplaintProposalDto complaintProposal)
	{		
		return toAjax(complaintProposalService.updateComplaintProposal(complaintProposal));
	}

	/**
	 * 显示投诉详情
	 * @param content
	 * @param mmap
	 * @return
	 */
	@RequiresPermissions("app:appUser:showDetail")
	@GetMapping("showDetail/{content}")
	public String flowBalance(@PathVariable("content") String content, ModelMap mmap){
		mmap.put("content", content);
		return prefix + "/showDetail";
	}
	
	/**
	 * 批量已读投诉建议
	 */
	@RequiresPermissions("app:complaintProposal:edit")
	@Log(title = "投诉建议", businessType = BusinessType.DELETE)
	@PostMapping( "/readByIds")
	@ResponseBody
	public AjaxResult readByIds(String ids)
	{
		return toAjax(complaintProposalService.readComplaintProposalByIds(ids));
	}

	/**
	 * 忽略投诉建议
	 */
	@RequiresPermissions("app:complaintProposal:edit")
	@Log(title = "投诉建议", businessType = BusinessType.DELETE)
	@PostMapping( "/ignoreByIds")
	@ResponseBody
	public AjaxResult ignoreByIds(String ids)
	{
		return toAjax(complaintProposalService.ignoreComplaintProposalByIds(ids));
	}

}
