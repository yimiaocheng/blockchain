package com.cwt.app.controller;

import com.cwt.app.common.dto.UserSessionDto;
import com.cwt.app.common.util.ResultUtils;
import com.cwt.app.common.util.SessionUtils;
import com.cwt.app.controller.api.BalancetApi;
import com.cwt.app.controller.api.WalletApi;
import com.cwt.domain.dto.ResultDto;
import com.cwt.domain.dto.grade.GradeDto;
import com.cwt.domain.dto.information.InformationDto;
import com.cwt.domain.dto.wallet.*;
import com.cwt.domain.entity.Wallet;
import com.cwt.persistent.mapper.WalletBalanceRecordMapper;
import com.cwt.persistent.mapper.WalletCalculatePowerRecordMapper;
import com.cwt.service.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;

@Api(WalletApi.WALLET_CONTROLLER_API)
@RestController
@RequestMapping("/wallet")
public class WalletController {

    @Autowired
    private WalletService walletService;
    @Autowired
    private InformationService informationService;
    @Autowired
    private GradeService gradeService;
    @Autowired
    private WalletBalanceRecordService walletBalanceRecordService;
    @Autowired
    private WalletCalculatePowerRecordService walletCalculatePowerRecordService;
    @Autowired
    private GameCurrencyRecordService gameCurrencyRecordService;


    /***
     * 余额转换算力功能
     * @param transBalance
     * @return 更新后的钱包对象
     */
    @ApiOperation(value = WalletApi.transformHashrate.METHOD_API_NAME
            , notes = WalletApi.transformHashrate.METHOD_API_NOTE)
    @RequestMapping(value = "/transformHashrate", method = RequestMethod.POST)
    public ResultDto transformHashrate(
            @ApiParam(WalletApi.transformHashrate.METHOD_API_TRANSBALANCE) @RequestParam(value = "transBalance") Double transBalance,
            @ApiParam(WalletApi.transformHashrate.METHOD_API_PASSWORD) @RequestParam("password") String password,
            HttpServletRequest request) {
        UserSessionDto session = SessionUtils.getSession(request.getSession());
        WalletDto walletDto = walletService.transform(transBalance, password, session.getUserId());
        return ResultUtils.buildSuccessDto(walletDto);
    }

    /***
     * 游戏币转换算力功能
     * @param transGameCoin
     * @return 更新后的钱包对象
     */
    @ApiOperation(value = WalletApi.gameCoinTransformHashrate.METHOD_API_NAME
            , notes = WalletApi.gameCoinTransformHashrate.METHOD_API_NOTE)
    @RequestMapping(value = "/gameCoinTransformHashrate", method = RequestMethod.POST)
    public ResultDto gameCoinTransformHashrate(
            @ApiParam(WalletApi.gameCoinTransformHashrate.METHOD_API_TRANSGAMECOIN) @RequestParam(value = "transGameCoin") BigDecimal transGameCoin,
            @ApiParam(WalletApi.gameCoinTransformHashrate.METHOD_API_PASSWORD) @RequestParam("password") String password,
            HttpServletRequest request) {
        UserSessionDto session = SessionUtils.getSession(request.getSession());
        WalletDto walletDto = walletService.gameCointransform(transGameCoin, password, session.getUserId());
        return ResultUtils.buildSuccessDto(walletDto);
    }

    /***
     * 节点转账功能(转出)
     * @param telephone 收款人账户
     * @param balance 交易余额
     * @param password 交易密码
     * @return
     */
    @ApiOperation(value = WalletApi.transferAccounts.METHOD_API_NAME,
            notes = WalletApi.transferAccounts.METHOD_API_NOTE)
    @RequestMapping(value = "/transferAccounts", method = RequestMethod.POST)
    public ResultDto transferAccounts(
            @ApiParam(WalletApi.transferAccounts.METHOD_API_TELEPHONE) @RequestParam("telephone") String telephone,
            @ApiParam(WalletApi.transferAccounts.METHOD_API_TRANSBALANCE) @RequestParam("balance") Double balance,
            @ApiParam(WalletApi.transferAccounts.METHOD_API_PASSWORD) @RequestParam("password") String password,
            HttpServletRequest request) {
        UserSessionDto session = SessionUtils.getSession(request.getSession());
        walletService.handleC2CTransfer(session.getUserId(), telephone, balance, password);
        return ResultUtils.buildSuccessDto(null);
    }

    /***
     * 根据用户Id获取钱包信息
     * @return WalletDto
     */
    @ApiOperation(value = WalletApi.selectByUserId.METHOD_API_NAME
            , notes = WalletApi.selectByUserId.METHOD_API_NOTE)
    @RequestMapping(value = "/selectByUserId", method = RequestMethod.GET)
    public ResultDto selectByUserId(
            @ApiParam(WalletApi.selectByUserId.METHOD_API_USERID) @RequestParam("userId") String userId) {
        WalletDto walletDto = walletService.getByUserId(userId);
        return ResultUtils.buildSuccessDto(walletDto);
    }

    /***
     * 查询余额转出信息
     * @return GetBillDto
     */
    @ApiOperation(value = WalletApi.selectTransferBill.METHOD_API_NAME
            , notes = WalletApi.selectTransferBill.METHOD_API_NOTE)
    @RequestMapping(value = "/selectTransferBill", method = RequestMethod.GET)
    public ResultDto selectTransferBill(
            @ApiParam(WalletApi.selectTransferBill.METHOD_API_NOTE_PAGENUM) @RequestParam("pageNum") Integer pageNum,
            @ApiParam(WalletApi.selectTransferBill.METHOD_API_NOTE_PAGESIZE) @RequestParam(value = "pageSize", required = false) Integer pageSize,
            HttpServletRequest request) {
        UserSessionDto session = SessionUtils.getSession(request.getSession());
        List<WalletBalanceTransferOutDto> transferBill = walletBalanceRecordService.getTransferBill(session.getUserId(), pageNum, pageSize);
        System.out.println(transferBill.toString());
        return ResultUtils.buildSuccessDto(transferBill);
    }

    /***
     * 查询余额转入信息
     * @return GetBillDto
     */
    @ApiOperation(value = WalletApi.selectCollectingBill.METHOD_API_NAME
            , notes = WalletApi.selectCollectingBill.METHOD_API_NOTE)
    @RequestMapping(value = "/selectCollectingBill", method = RequestMethod.GET)
    public ResultDto selectCollectingBill(
            @ApiParam(WalletApi.selectCollectingBill.METHOD_API_NOTE_PAGENUM) @RequestParam("pageNum") Integer pageNum,
            @ApiParam(WalletApi.selectCollectingBill.METHOD_API_NOTE_PAGESIZE) @RequestParam(value = "pageSize", required = false) Integer pageSize,
            HttpServletRequest request) {
        UserSessionDto session = SessionUtils.getSession(request.getSession());
        List<WalletBalanceTransferInDto> transferBill = walletBalanceRecordService.getCollectingBill(session.getUserId(), pageNum, pageSize);
        System.out.println(transferBill.toString());
        return ResultUtils.buildSuccessDto(transferBill);
    }

    /***
     * 查询余额兑换信息
     * @return GetBillDto
     */
    @ApiOperation(value = WalletApi.selectTransBill.METHOD_API_NAME
            , notes = WalletApi.selectTransBill.METHOD_API_NOTE)
    @RequestMapping(value = "/selectTransBill", method = RequestMethod.GET)
    public ResultDto selectTransBill(HttpServletRequest request,
           @ApiParam(WalletApi.selectTransBill.METHOD_API_NOTE_PAGENUM) @RequestParam("pageNum") Integer pageNum,
           @ApiParam(WalletApi.selectTransBill.METHOD_API_NOTE_PAGESIZE) @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        UserSessionDto session = SessionUtils.getSession(request.getSession());
        List<GetBillDto> transferBill = walletBalanceRecordService.getTransBill(session.getUserId(),pageNum,pageSize);
        System.out.println(transferBill.toString());
        return ResultUtils.buildSuccessDto(transferBill);
    }

    /***
     * 查询游戏币兑换信息
     * @return
     */
    @ApiOperation(value = WalletApi.selectGameCoinTransBill.METHOD_API_NAME
            , notes = WalletApi.selectGameCoinTransBill.METHOD_API_NOTE)
    @RequestMapping(value = "/selectGameCoinTransBill", method = RequestMethod.GET)
    public ResultDto selectGameCoinTransBill(HttpServletRequest request,
           @ApiParam(WalletApi.selectTransBill.METHOD_API_NOTE_PAGENUM) @RequestParam("pageNum") Integer pageNum,
           @ApiParam(WalletApi.selectTransBill.METHOD_API_NOTE_PAGESIZE) @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        UserSessionDto session = SessionUtils.getSession(request.getSession());
        List<GameCurrencyRecordDto> gameCoinTransBill = gameCurrencyRecordService.getGameCoinTransBill(session.getUserId(), pageNum, pageSize);
        return ResultUtils.buildSuccessDto(gameCoinTransBill);
    }

    /***
     * 查询推荐奖记录信息
     * @return GetBillDto
     */
    @ApiOperation(value = WalletApi.selectRecommendAwardBill.METHOD_API_NAME
            , notes = WalletApi.selectRecommendAwardBill.METHOD_API_NOTE)
    @RequestMapping(value = "/selectRecommendAwardBill", method = RequestMethod.GET)
    public ResultDto selectRecommendAwardBill(HttpServletRequest request) {
        UserSessionDto session = SessionUtils.getSession(request.getSession());
        List<GetBillDto> transferBill = walletCalculatePowerRecordService.getRecommendAwardBill(session.getUserId());
        System.out.println(transferBill.toString());
        return ResultUtils.buildSuccessDto(transferBill);
    }

    /***
     * 查询智能释放记录信息
     * @return GetBillDto
     */
    @ApiOperation(value = WalletApi.selectAutoUpdateBalanceBill.METHOD_API_NAME
            , notes = WalletApi.selectAutoUpdateBalanceBill.METHOD_API_NOTE)
    @RequestMapping(value = "/selectAutoUpdateBalanceBill", method = RequestMethod.GET)
    public ResultDto selectAutoUpdateBalanceBill(HttpServletRequest request) {
        UserSessionDto session = SessionUtils.getSession(request.getSession());
        List<GetBillDto> transferBill = walletBalanceRecordService.getAutoUpdateBalanceBill(session.getUserId());
        return ResultUtils.buildSuccessDto(transferBill);
    }

    /***
     * 根据钱包Id获取钱包信息
     * @return WalletDto
     */
    @ApiOperation(value = WalletApi.selectByWalletId.METHOD_API_NAME
            , notes = WalletApi.selectByWalletId.METHOD_API_NOTE)
    @RequestMapping(value = "/selectByWalletId", method = RequestMethod.GET)
    public ResultDto selectByWalletId(
            @ApiParam(WalletApi.selectByWalletId.METHOD_API_WALLETID) @RequestParam("walletId") String walletId) {
        WalletDto walletDto = walletService.getByWalletId(walletId);
        return ResultUtils.buildSuccessDto(walletDto);
    }

    /***
     * 根据用户Id新建/保存钱包
     * @return WalletDto
     */
    @ApiOperation(value = WalletApi.saveByUserId.METHOD_API_NAME
            , notes = WalletApi.saveByUserId.METHOD_API_NOTE)
    @RequestMapping(value = "/saveByUserId", method = RequestMethod.POST)
    public ResultDto saveByUserId(
            @ApiParam(WalletApi.saveByUserId.METHOD_API_USERID) @RequestParam("userId") String userId) {
        WalletDto walletDto = walletService.saveByUserId(userId);
        return ResultUtils.buildSuccessDto(walletDto);
    }

    /***
     * 根据数据名获取数据
     * @return WalletDto
     */
    @ApiOperation(value = WalletApi.selectByDateName.METHOD_API_NAME
            , notes = WalletApi.selectByDateName.METHOD_API_NOTE)
    @RequestMapping(value = "/selectByDateName", method = RequestMethod.GET)
    public ResultDto selectByDateName(
            @ApiParam(WalletApi.selectByDateName.METHOD_API_DATENAME) @RequestParam("dataName") String dataName) {
        InformationDto informationDto = informationService.getByDateName(dataName);
        return ResultUtils.buildSuccessDto(informationDto);
    }

    /***
     * 根据用户Id获取等级信息
     * @return WalletDto
     */
    @ApiOperation(value = WalletApi.selectGradeByUserId.METHOD_API_NAME
            , notes = WalletApi.selectGradeByUserId.METHOD_API_NOTE)
    @RequestMapping(value = "/selectGradeByUserId", method = RequestMethod.GET)
    public ResultDto selectGradeByUserId(
            @ApiParam(WalletApi.saveByUserId.METHOD_API_USERID) @RequestParam("userId") String userId) {
        GradeDto gradeDto = gradeService.getByUserId(userId);
        return ResultUtils.buildSuccessDto(gradeDto);
    }

    /***
     * 更新钱包信息
     * @return WalletDto
     */
    @ApiOperation(value = WalletApi.updateWallet.METHOD_API_NAME
            , notes = WalletApi.updateWallet.METHOD_API_NOTE)
    @RequestMapping(value = "/updateWallet", method = RequestMethod.POST)
    public ResultDto updateWallet(
            @ApiParam(WalletApi.updateWallet.METHOD_API_WALLET) @RequestParam("wallet") Wallet wallet) {
        WalletDto walletDto = walletService.updateWallet(wallet);
        return ResultUtils.buildSuccessDto(walletDto);
    }

    /***
     * 查询当前账号所有余额更改记录
     * @return CalculatePowerRecordDto
     */
    @ApiOperation(value = WalletApi.listAllBalance.METHOD_API_NAME
            , notes = WalletApi.listAllBalance.METHOD_API_NOTE)
    @RequestMapping(value = "/listAllBalance", method = RequestMethod.GET)
    public ResultDto listAllBalance(HttpServletRequest request,
            @ApiParam(WalletApi.listAllBalance.METHOD_API_PAGENUM) @RequestParam(value = "pageNum", required = false) Integer pageNum,
            @ApiParam(WalletApi.listAllBalance.METHOD_API_PAGESIZE) @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        UserSessionDto session = SessionUtils.getSession(request.getSession());
        List<BalanceRecordListAllDto> balanceRecordListAllDtos = walletBalanceRecordService.listAll(session.getUserId(),pageNum,pageSize);
        return ResultUtils.buildSuccessDto(balanceRecordListAllDtos);
    }

    /***
     * 查询当前账号所有算力更改记录
     * @return CalculatePowerRecordDto
     */
    @ApiOperation(value = WalletApi.listAllForce.METHOD_API_NAME
            , notes = WalletApi.listAllForce.METHOD_API_NOTE)
    @RequestMapping(value = "/listAllForce", method = RequestMethod.GET)
    public ResultDto listAllForce(HttpServletRequest request,
           @ApiParam(WalletApi.listAllForce.METHOD_API_PAGENUM) @RequestParam(value = "pageNum", required = false) Integer pageNum,
           @ApiParam(WalletApi.listAllForce.METHOD_API_PAGESIZE) @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        UserSessionDto session = SessionUtils.getSession(request.getSession());
        List<CalculatePowerRecordDto> calculatePowerRecordDtos = walletCalculatePowerRecordService.listAll(session.getUserId(),pageNum,pageSize);
        return ResultUtils.buildSuccessDto(calculatePowerRecordDtos);
    }

    /***
     * 获取每天的余额释放
     */
    @ApiOperation(value = WalletApi.headerUpdateBalance.METHOD_API_NAME
            , notes = WalletApi.headerUpdateBalance.METHOD_API_NOTE)
    @RequestMapping(value = "/headerUpdateBalance", method = RequestMethod.POST)
    public ResultDto headerUpdateBalance(HttpServletRequest request){
        UserSessionDto session = SessionUtils.getSession(request.getSession());
        walletService.headerUpdateBalance(session.getUserId());
        return ResultUtils.buildSuccessDto(null);
    }

    /***
     * 查询每天的智能释放的值
     */
    @ApiOperation(value = WalletApi.selectUpdateBalance.METHOD_API_NAME
            , notes = WalletApi.selectUpdateBalance.METHOD_API_NOTE)
    @RequestMapping(value = "/selectUpdateBalance", method = RequestMethod.GET)
    public ResultDto selectUpdateBalance(HttpServletRequest request){
        UserSessionDto session = SessionUtils.getSession(request.getSession());
        return ResultUtils.buildSuccessDto(walletService.selectUpdateBalance(session.getUserId()));
    }


    /**
     * ##################huangxl#############
     */

    /***
     * 扫码转出接口
     * @param telephone 收款人账户
     * @param balance 交易余额
     * @param password 交易密码
     * @return
     */
    @ApiOperation(value = "扫码转出",
            notes = "扫码转出")
    @RequestMapping(value = "/scanTransfer", method = RequestMethod.POST)
    public ResultDto scanTransfer(
            @ApiParam(WalletApi.transferAccounts.METHOD_API_TELEPHONE) @RequestParam("telephone") String telephone,
            @ApiParam(WalletApi.transferAccounts.METHOD_API_TRANSBALANCE) @RequestParam("balance") Double balance,
            @ApiParam(WalletApi.transferAccounts.METHOD_API_PASSWORD) @RequestParam("password") String password,
            HttpServletRequest request) {
        UserSessionDto session = SessionUtils.getSession(request.getSession());
        walletService.handleScanQRCodeTransfer(session.getUserId(), telephone, balance, password);
        return ResultUtils.buildSuccessDto(null);
    }
}
