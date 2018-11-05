package com.ruoyi.project.app.controller.order;

import com.cwt.domain.dto.balance.*;
import com.cwt.domain.dto.wallet.CenterAccountDto;
import com.cwt.service.service.BalanceOrderBillService;
import com.cwt.service.service.BalanceOrderService;
import com.cwt.service.service.WalletBalanceRecordService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.page.TableDataInfo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author huangxl
 *
 */
@Controller
@RequestMapping("/app/balanceOrder")
public class BalanceOrderController extends BaseController {
    private String prefix = "app/balanceOrder";

    @Autowired
    private BalanceOrderService balanceOrderService;
    @Autowired
    private BalanceOrderBillService balanceOrderBillService;
    @Autowired
    private WalletBalanceRecordService walletBalanceRecordService;

    /**
     * 查询所有余额交易订单信息
     * @param condition 查询条件
     * @return 列表
     */
    @RequiresPermissions("app:order:balance:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(BalanceOrderOfAllConditionDto condition) {
        startPage();
        List<BalanceOrderOfAllListDto> list = balanceOrderService.listForAll(condition);
        return getDataTable(list);
    }

    /**
     * 查询所有余额交易明细信息
     * @param condition 查询条件
     * @return 列表
     */
    @RequiresPermissions("app:order:balance:list")
    @PostMapping("/billList")
    @ResponseBody
    public TableDataInfo list(BalanceOrderBillOfAllConditionDto condition) {
        startPage();
        List<BalanceOrderBillOfAllListDto> list = balanceOrderBillService.listForAll(condition);
        return getDataTable(list);
    }

    /**
     * 返回余额交易订单页面
     */
    @RequiresPermissions("app:order:balance:view")
    @GetMapping("/orderIndex")
    public String orderIndex() {
        return prefix + "/orderIndex";
    }

    /**
     * 返回余额交易订单明细页面
     * @param id 总订单id
     * @param billId  订单明细id
     * @return
     */
    @RequiresPermissions("app:order:balance:view")
    @GetMapping("/billIndex")
    public String billIndex(String id,String billId, ModelMap model) {
        model.put("orderId",id);
        model.put("billId",billId);
        return prefix + "/billIndex";
    }


    /**
     * 返回中央账户收支流水页面
     */
    @RequiresPermissions("app:order:balance:view")
    @GetMapping("/centerAccountIndex")
    public String centerAccountIndex() {
        return prefix + "/centerAccountIndex";
    }

    /**
     * 查询所有中央账户收支流水信息
     * @return 列表
     */
    @RequiresPermissions("app:order:balance:list")
    @PostMapping("/centerAccountList")
    @ResponseBody
    public TableDataInfo centerAccountList() {

        List<CenterAccountDto> list = walletBalanceRecordService.centerAccountCount();

        return getDataTable(list);
    }

    /**
     * 返回中央账户收支流水详细页面
     */
    @RequiresPermissions("app:order:balance:view")
    @GetMapping("/centerAccountDetail")
    public String centerAccountDetail() {
        return prefix + "/centerAccountDetail";
    }

    /**
     * 查询所有中央账户收支流水信息
     * @return 列表
     */
    @RequiresPermissions("app:order:balance:list")
    @PostMapping("/centerAccountDetailList")
    @ResponseBody
    public TableDataInfo centerAccountDetailList(CenterAccountConditionDto centerAccountConditionDto) {
        startPage();
        List<CenterAccountDto> list = walletBalanceRecordService.centerAccountDetail(centerAccountConditionDto);

        return getDataTable(list);
    }


}
