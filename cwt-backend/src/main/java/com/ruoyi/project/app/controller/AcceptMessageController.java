package com.ruoyi.project.app.controller;

import com.cwt.domain.dto.mySettings.AcceptMessageDto;
import com.cwt.service.service.IAcceptMessageService;
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
 * 个人接受消息 信息操作处理
 * 
 * @author ruoyi
 * @date 2018-08-27
 */
@Controller
@RequestMapping("/app/acceptMessage")
public class AcceptMessageController extends BaseController
{
    private String prefix = "app/acceptMessage";
	
	@Autowired
	private IAcceptMessageService acceptMessageService;
	
	@RequiresPermissions("app:acceptMessage:view")
	@GetMapping()
	public String acceptMessage()
	{
	    return prefix + "/acceptMessage";
	}
	
	/**
	 * 查询个人接受消息列表
	 */
	@RequiresPermissions("app:acceptMessage:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(AcceptMessageDto acceptMessage)
	{
		startPage();
        List<AcceptMessageDto> list = acceptMessageService.selectAcceptMessageList(acceptMessage);
		return getDataTable(list);
	}
	
//	/**
//	 * 新增个人接受消息
//	 */
//	@GetMapping("/add")
//	public String add()
//	{
//
//		return prefix + "/add";
//	}
	/**
	 * 新增个人接受消息
	 */
	@GetMapping("/sendMsg")
	public String sendMsg(@RequestParam String userId, @RequestParam String userName, ModelMap mmap)
	{
		mmap.put("userId", userId);
		mmap.put("userName", userName);
		return prefix + "/add";
	}
	
	/**
	 * 新增保存个人接受消息
	 */
	@RequiresPermissions("app:acceptMessage:add")
	@Log(title = "个人接受消息", businessType = BusinessType.INSERT)
	@PostMapping("/addSave")
	@ResponseBody
	public AjaxResult addSave(AcceptMessageDto acceptMessage)
	{
		return toAjax(acceptMessageService.insertAcceptMessage(acceptMessage));
	}

	/**
	 * 修改个人接受消息
	 */
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") String id, ModelMap mmap)
	{
		AcceptMessageDto acceptMessage = acceptMessageService.selectAcceptMessageById(id);
		mmap.put("acceptMessage", acceptMessage);
	    return prefix + "/edit";
	}
	
	/**
	 * 修改保存个人接受消息
	 */
	@RequiresPermissions("app:acceptMessage:edit")
	@Log(title = "个人接受消息", businessType = BusinessType.UPDATE)
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(AcceptMessageDto acceptMessage)
	{		
		return toAjax(acceptMessageService.updateAcceptMessage(acceptMessage));
	}
	
	/**
	 * 删除个人接受消息
	 */
	@RequiresPermissions("app:acceptMessage:remove")
	@Log(title = "个人接受消息", businessType = BusinessType.DELETE)
	@PostMapping( "/remove")
	@ResponseBody
	public AjaxResult remove(String ids)
	{		
		return toAjax(acceptMessageService.deleteAcceptMessageByIds(ids));
	}
	
}
