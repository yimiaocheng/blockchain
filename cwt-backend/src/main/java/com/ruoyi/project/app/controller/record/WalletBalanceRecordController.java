package com.ruoyi.project.app.controller.record;

import com.cwt.domain.dto.balance.BalanceRecordListConditionDto;
import com.cwt.domain.dto.balance.BalanceRecordListDto;
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

@Controller
@RequestMapping("/app/record/balance")
public class WalletBalanceRecordController extends BaseController {

    private String prefix = "app/walletBalanceRecord";

    @Autowired
    private WalletBalanceRecordService walletBalanceRecordService;

    /**
     * 查询所有的余额变动记录
     * @param condition 查询条件
     */
    @RequiresPermissions("app:record:balance:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(BalanceRecordListConditionDto condition) {
        startPage();
        List<BalanceRecordListDto> list = walletBalanceRecordService.listByCondition(condition);
        return getDataTable(list);
    }

    /**
     * 返回余额变动记录页面
     */
    @RequiresPermissions("app:record:balance:view")
    @GetMapping("/index")
    public String walletBalanceRecord(String userName, ModelMap model) {
        model.put("userName",userName);
        return prefix + "/walletBalanceRecord";
    }

    /**
     * 返回余额转入转出记录页面
     */
    @RequiresPermissions("app:record:balance:view")
    @GetMapping("/transferIndex")
    public String walletBalanceTransferRecord() {
        return prefix + "/walletBalanceTransferRecord";
    }
}
