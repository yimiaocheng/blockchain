package com.ruoyi.project.app.controller.record;

import com.cwt.domain.dto.calculate.CalculatePowerListConditionDto;
import com.cwt.domain.dto.calculate.CalculatePowerListDto;
import com.cwt.service.service.WalletCalculatePowerRecordService;
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
@RequestMapping("/app/record/calculatePower")
public class WalletCalculatePowerRecordController extends BaseController {
    private String prefix = "app/walletCalculatePowerRecord";

    @Autowired
    private WalletCalculatePowerRecordService calculatePowerRecordService;

    /**
     * 查询所有算力变动记录
     * @param condition 查询条件
     */
    @RequiresPermissions("app:record:calculatePower:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(CalculatePowerListConditionDto condition) {
        startPage();
        List<CalculatePowerListDto> list = calculatePowerRecordService.listByCondition(condition);
        return getDataTable(list);
    }

    /**
     * 返回算力变动页面
     */
    @RequiresPermissions("app:record:calculatePower:view")
    @GetMapping("/index")
    public String walletCalculatePowerRecord(String userName, ModelMap model) {
        model.put("userName",userName);
        return prefix + "/walletCalculatePowerRecord";
    }
}
