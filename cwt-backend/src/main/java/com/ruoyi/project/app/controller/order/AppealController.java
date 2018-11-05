package com.ruoyi.project.app.controller.order;


import com.cwt.domain.dto.appeal.AppealListConditionDto;
import com.cwt.domain.dto.appeal.AppealListDto;
import com.cwt.domain.dto.balance.BalanceOrderOfAllConditionDto;
import com.cwt.domain.dto.balance.BalanceOrderOfAllListDto;
import com.cwt.service.service.AppealService;
import com.cwt.service.service.BalanceOrderBillService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.TableDataInfo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/app/appeal")
public class AppealController extends BaseController {
    private String prefix = "app/balanceOrder";
    @Autowired
    private AppealService appealService;
    @Autowired
    private BalanceOrderBillService balanceOrderBillService;

    /**
     * 返回余额交易订单明细页面
     */
    @RequiresPermissions("app:appeal:view")
    @GetMapping("/index")
    public String billIndex() {
        return prefix + "/appealIndex";
    }

    /**
     * 查询所有余额交易订单信息
     * @param condition 查询条件
     * @return 列表
     */
    @RequiresPermissions("app:appeal:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(AppealListConditionDto condition) {
        startPage();
        List<AppealListDto> list = appealService.listAppealByCondition(condition);
        return getDataTable(list);
    }

    /***
     * 同意申诉
     * @param billId
     * @param appealId
     * @return
     */
    @PostMapping(value = "/agress")
    @RequiresPermissions("app:appeal:agress")
    @ResponseBody
    public AjaxResult headerAgreed2Appeal(@RequestParam("appealId") String appealId){
        balanceOrderBillService.headerAgreed2Appeal(appealId);
        return success();
    }

    /***
     * 驳回申诉
     * @param billId
     * @param appealId
     * @return
     */
    @PostMapping(value = "/disagress")
    @RequiresPermissions("app:appeal:disagress")
    @ResponseBody
    public AjaxResult headerReject2Appeal(@RequestParam("appealId") String appealId){
        balanceOrderBillService.headerReject2Appeal(appealId);
        return success();
    }

    /***
     * 撤销申述
     * @param billId
     * @param appealId
     * @return
     */
    @PostMapping(value = "/discard")
    @RequiresPermissions("app:appeal:discard")
    @ResponseBody
    public AjaxResult headerRepeal2Appeal(@RequestParam("appealId") String appealId){
        balanceOrderBillService.headerRepeal2Appeal(appealId);
        return success();
    }

}
