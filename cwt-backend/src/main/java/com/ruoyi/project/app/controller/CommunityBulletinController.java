package com.ruoyi.project.app.controller;

import com.cwt.domain.dto.mySettings.CommunityBulletinDto;
import com.cwt.service.service.ICommunityBulletinService;
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
 * 社区公告 信息操作处理
 * 
 * @author ruoyi
 * @date 2018-08-27
 */
@Controller
@RequestMapping("/app/communityBulletin")
public class CommunityBulletinController extends BaseController
{
    private String prefix = "app/communityBulletin";
	
	@Autowired
	private ICommunityBulletinService communityBulletinService;
	
	@RequiresPermissions("app:communityBulletin:view")
	@GetMapping()
	public String communityBulletin()
	{
	    return prefix + "/communityBulletin";
	}
	
	/**
	 * 查询社区公告列表
	 */
	@RequiresPermissions("app:communityBulletin:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(CommunityBulletinDto communityBulletin)
	{
		startPage();
        List<CommunityBulletinDto> list = communityBulletinService.selectCommunityBulletinList(communityBulletin);
		return getDataTable(list);
	}
	
	/**
	 * 新增社区公告
	 */
	@GetMapping("/add")
	public String add()
	{
	    return prefix + "/add";
	}
	
	/**
	 * 新增保存社区公告
	 */
	@RequiresPermissions("app:communityBulletin:add")
	@Log(title = "社区公告", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(CommunityBulletinDto communityBulletin)
	{		
		return toAjax(communityBulletinService.insertCommunityBulletin(communityBulletin));
	}

	/**
	 * 修改社区公告
	 */
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") String id, ModelMap mmap)
	{
		CommunityBulletinDto communityBulletin = communityBulletinService.selectCommunityBulletinById(id);
		mmap.put("communityBulletin", communityBulletin);
	    return prefix + "/edit";
	}
	
	/**
	 * 修改保存社区公告
	 */
	@RequiresPermissions("app:communityBulletin:edit")
	@Log(title = "社区公告", businessType = BusinessType.UPDATE)
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(CommunityBulletinDto communityBulletin)
	{		
		return toAjax(communityBulletinService.updateCommunityBulletin(communityBulletin));
	}
	
	/**
	 * 删除社区公告
	 */
	@RequiresPermissions("app:communityBulletin:remove")
	@Log(title = "社区公告", businessType = BusinessType.DELETE)
	@PostMapping( "/remove")
	@ResponseBody
	public AjaxResult remove(String ids)
	{
		return toAjax(communityBulletinService.deleteCommunityBulletinByIds(ids));
	}

}
