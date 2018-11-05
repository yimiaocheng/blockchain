package com.ruoyi.project.app.controller.store;

import com.cwt.domain.dto.store.*;
import com.cwt.service.service.PayService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.page.TableDataInfo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/app/record/store")
public class StoreController extends BaseController {

    @Autowired
    private PayService payService;

    private String prefix = "app/store";

    /**
     * 返回支付信息记录页面
     */
    @RequiresPermissions("app:record:store:view")
    @GetMapping("/index")
    public String pay() {
        return prefix + "/userPayRecord";
    }

    /**
     * 查询所有商城支付信息记录信息
     * @param userPayInfoDTO 查询条件
     * @return 列表
     */
    @RequiresPermissions("app:record:store:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo userPayInfoList(UserPayInfoDTO userPayInfoDTO) {
        startPage();
        List<UserPayInfoPO> list = payService.selectUserPayInfoList(userPayInfoDTO);
        return getDataTable(list);
    }


    /**
     * 返回奖金发放信息页面
     */
    @RequiresPermissions("app:record:store:view")
    @GetMapping("/userAward")
    public String userAward() {
        return prefix + "/userAwardRecord";
    }

    /**
     * 查询奖金发放信息记录信息
     * @param userAwardDTO 查询条件
     * @return 列表
     */
    @RequiresPermissions("app:record:store:list")
    @PostMapping("/userAwardlist")
    @ResponseBody
    public TableDataInfo userAwardlist(UserAwardDTO userAwardDTO) {
        startPage();
        List<UserAwardListPO> list = payService.selectUserAwardList(userAwardDTO);
        return getDataTable(list);
    }


    /**
     * 返回确认收款记录信息页面
     */
    @RequiresPermissions("app:record:store:view")
    @GetMapping("/userReceive")
    public String userReceive() {
        return prefix + "/userReceiveRecord";
    }

    /**
     * 查询确认收款记录信息
     * @param userReceiveDTO 查询条件
     * @return 列表
     */
    @RequiresPermissions("app:record:store:list")
    @PostMapping("/userReceiveList")
    @ResponseBody
    public TableDataInfo userReceiveList(UserReceiveDTO userReceiveDTO) {
        startPage();
        List<UserReceiveListPO> list = payService.selectUserReceiveList(userReceiveDTO);
        return getDataTable(list);
    }



    /**
     * 返回用户退款记录信息页面
     */
    @RequiresPermissions("app:record:store:view")
    @GetMapping("/userRefund")
    public String userRefund() {
        return prefix + "/userRefundRecord";
    }



    /**
     * 查询用户退款记录信息
     * @param userRefundDTO 查询条件
     * @return 列表
     */
    @RequiresPermissions("app:record:store:list")
    @PostMapping("/userRefundList")
    @ResponseBody
    public TableDataInfo userRefundList(UserRefundDTO userRefundDTO) {
        startPage();
        List<UserRefundListPO> list = payService.selectUserRefundList(userRefundDTO);
        return getDataTable(list);
    }





}
