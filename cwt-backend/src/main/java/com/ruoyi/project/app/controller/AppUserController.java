package com.ruoyi.project.app.controller;

import com.cwt.domain.dto.user.BackendUserDTO;
import com.cwt.service.service.UserService;
import com.cwt.service.service.WalletBalanceRecordService;
import com.cwt.service.service.WalletService;
import com.ruoyi.common.utils.StringUtils;
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

import java.math.BigDecimal;
import java.util.List;

/**
 * @author huangxl
 * Created on 2018/8/28
 */
@Controller
@RequestMapping("/app/appUser")
public class AppUserController extends BaseController
{
    private String prefix = "app/user";

    @Autowired
    private UserService userService;
    @Autowired
    private WalletService walletService;
    @Autowired
    private WalletBalanceRecordService walletBalanceRecordService;

    @RequiresPermissions("app:appUser:view")
    @GetMapping()
    public String user()
    {
        return prefix + "/user";
    }

    /**
     * 查询用户列表
     */
    @RequiresPermissions("app:appUser:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(BackendUserDTO user)
    {
        startPage();

        List<BackendUserDTO> list = userService.selectUserWalletList(user);

        return getDataTable(list);
    }

    @RequiresPermissions("app:appUser:flowBalance")
    @GetMapping("flowBalance/{id}")
    public String flowBalance(@PathVariable("id") String id, ModelMap mmap){
        mmap.put("id", id);
        return prefix + "/flowBalance";
    }

    @RequiresPermissions("app:appUser:calculationForce")
    @GetMapping("calculationForce/{id}")
    public String calculationForce(@PathVariable("id") String id, ModelMap mmap){
        mmap.put("id", id);
        return prefix + "/calculationForce";
    }

//    /**
//     * 新增用户
//     */
//    @GetMapping("/add")
//    public String add()
//    {
//        return prefix + "/add";
//    }
//
//    /**
//     * 新增保存用户
//     */
//    @RequiresPermissions("app:appUser:add")
//    @Log(title = "用户", businessType = BusinessType.INSERT)
//    @PostMapping("/add")
//    @ResponseBody
//    public AjaxResult addSave(User user)
//    {
//        return toAjax(userService.insertUser(user));
//    }

    /**
     * 修改用户
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") String id, ModelMap mmap)
    {
        BackendUserDTO user = userService.selectUserWalletById(id);
        mmap.put("appUser", user);
        return prefix + "/edit";
    }

    /**
     * 修改保存用户
     */
    @RequiresPermissions("app:appUser:edit")
    @Log(title = "用户", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(BackendUserDTO user)
    {
        if(user.getCpArithmeticType() != null && user.getCpAmount() != null){  //智能算力修改
            int countCp = walletService.updateCalculationForceForBackend(user);  //修改智能算力
            if(countCp != 1){
                return toAjax(0);
            }
        }

        if(user.getFbArithmeticType() != null && user.getFbAmount() != null){  //流动余额修改

            if(user.getFbArithmeticType() == 1){
                BigDecimal surplusCTAccount = walletBalanceRecordService.getSurplusCTAccount();  //剩余发行量

                if(user.getFbAmount().compareTo(surplusCTAccount) == 1){
                    return error(1, "增加资产太多，超过剩余发行量");
                }
            }

            int countFb = walletService.updateFlowBalanceForBackend(user);

            if(countFb != 1){
                return toAjax(0);
            }
        }

        if(StringUtils.isNotEmpty(user.getStatus())){  //流动用户状态
            return toAjax(userService.updateUserStatusForBackend(user));
        }else{
            return toAjax(1);
        }
    }

    /**
     * 删除用户
     */
//    @RequiresPermissions("app:appUser:remove")
//    @Log(title = "用户", businessType = BusinessType.DELETE)
//    @PostMapping( "/remove")
//    @ResponseBody
//    public AjaxResult remove(String ids)
//    {
//        return toAjax(userService.deleteUserByIds(ids));
//    }

}
