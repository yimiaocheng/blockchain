package com.ruoyi.project.app.controller;

import com.cwt.domain.dto.balance.*;
import com.cwt.domain.dto.gameCoin.GameCoinDTO;
import com.cwt.domain.dto.integral.IntegralDTO;
import com.cwt.domain.dto.wallet.CenterAccountDto;
import com.cwt.service.service.*;
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
@RequestMapping("/app/gameAndIntegralRecord")
public class GameAndIntegralRecordController extends BaseController {
    private String prefix = "app/gameAndIntegralRecord";

    @Autowired
    private IntegralService integralService;
    @Autowired
    private GameCurrencyRecordService gameCurrencyRecordService;

    /**
     * 返回游戏代币记录页面
     */
    @RequiresPermissions("app:gameAndIntegralRecord:game:view")
    @GetMapping("/gamePage")
    public String gamePage(String userName, ModelMap model) {
        model.put("userMobile",userName);
        return prefix + "/game_page";
    }

    /**
     * 查询所有游戏代币记录信息
     * @param integralDTO 查询条件
     * @return 列表
     */
    @RequiresPermissions("app:gameAndIntegralRecord:game:list")
    @PostMapping("/gameList")
    @ResponseBody
    public TableDataInfo gameList(GameCoinDTO integralDTO) {
        startPage();
//        List<BalanceOrderOfAllListDto> list = balanceOrderService.listForAll(condition);
        return getDataTable(gameCurrencyRecordService.selectGameCoinListForBackend(integralDTO));
    }

    /**
     * 返回商城积分记录页面
     */
    @RequiresPermissions("app:gameAndIntegralRecord:integral:view")
    @GetMapping("/integralPage")
    public String integralPage(String userName, ModelMap model) {
        model.put("userMobile",userName);
        return prefix + "/integral_page";
    }

    /**
     * 查询所有商城积分记录信息
     * @param integralDTO 查询条件
     * @return 列表
     */
    @RequiresPermissions("app:gameAndIntegralRecord:integral:list")
    @PostMapping("/integralList")
    @ResponseBody
    public TableDataInfo integralList(IntegralDTO integralDTO) {
        startPage();
        return getDataTable(integralService.selectIntegralList(integralDTO));
    }

}
