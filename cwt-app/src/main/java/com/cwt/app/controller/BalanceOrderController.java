package com.cwt.app.controller;

import com.cwt.app.common.dto.UserSessionDto;
import com.cwt.app.common.util.ResultUtils;
import com.cwt.app.common.util.SessionUtils;
import com.cwt.app.controller.api.BalancetApi;
import com.cwt.common.constant.BaseConstants;
import com.cwt.common.enums.BalanceOrderResultEnums;
import com.cwt.common.exception.BalanceOrderException;
import com.cwt.common.util.ExceptionPreconditionUtils;
import com.cwt.domain.dto.ResultDto;
import com.cwt.domain.dto.balance.BalanceOrderInfoDto;
import com.cwt.domain.dto.balance.BalanceOrderUpdateDto;
import com.cwt.domain.dto.balance.GetBalanceOrderBillByConditionDto;
import com.cwt.domain.dto.balance.GetBalanceOrderByConditionDto;
import com.cwt.domain.dto.information.InformationForBalanceOrderDto;
import com.cwt.domain.entity.BalanceOrderBill;
import com.cwt.service.service.BalanceOrderBillService;
import com.cwt.service.service.BalanceOrderService;
import com.cwt.service.service.InformationService;
import com.cwt.service.service.WalletService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Api(BalancetApi.BALANCE_CONTROLLER_API)
@RestController
@RequestMapping("/balance")
public class BalanceOrderController {

    @Autowired
    private WalletService walletService;
    @Autowired
    private BalanceOrderService balanceOrderService;
    @Autowired
    private BalanceOrderBillService balanceOrderBillService;
    @Autowired
    private InformationService informationService;
    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * 创建买单的状态详情
     *
     * @param orderTotalNum 交易总额
     * @param limitNumMin   交易最少限制
     * @param limitNumMax   交易上限
     * @param payType       支付方式
     * @return
     */
    @ApiOperation(value = BalancetApi.addBuyOrderConfirm.METHOD_API_NAME
            , notes = BalancetApi.addBuyOrderConfirm.METHOD_API_NOTE)
    @RequestMapping(value = "/addBuyOrderConfirm", method = RequestMethod.POST)
    public ResultDto addBuyOrderConfirm(
            @ApiParam(BalancetApi.addBuyOrderConfirm.METHOD_API_ORDER_TOTAL_NUM) @RequestParam("orderTotalNum") BigDecimal orderTotalNum,
            @ApiParam(BalancetApi.addBuyOrderConfirm.METHOD_API_LIMIT_NUM_MIN) @RequestParam("limitNumMin") BigDecimal limitNumMin,
            @ApiParam(BalancetApi.addBuyOrderConfirm.METHOD_API_LIMIT_NUM_MAX) @RequestParam("limitNumMax") BigDecimal limitNumMax,
            @ApiParam(BalancetApi.addBuyOrderConfirm.METHOD_API_PAY_TYPE) @RequestParam("payType") Integer payType,
            @ApiParam(BalancetApi.addSellOrderConfirm.METHOD_API_PAY_BALANCECONVERTPERCENT) @RequestParam("balanceConvertPercent") BigDecimal balanceConvertPercent) {
        return ResultUtils.buildSuccessDto(balanceOrderService.headerBalanceOrderBuyConfirm(orderTotalNum, limitNumMin, limitNumMax, payType,balanceConvertPercent));
    }

    /**
     * 创建卖单的状态详情
     *
     * @param orderTotalNum 交易总额
     * @param limitNumMin   交易最少限制
     * @param limitNumMax   交易上限
     * @param payType       支付方式
     * @return
     */
    @ApiOperation(value = BalancetApi.addSellOrderConfirm.METHOD_API_NAME
            , notes = BalancetApi.addSellOrderConfirm.METHOD_API_NOTE)
    @RequestMapping(value = "/addSellOrderConfirm", method = RequestMethod.POST)
    public ResultDto addSellOrderConfirm(
            @ApiParam(BalancetApi.addSellOrderConfirm.METHOD_API_ORDER_TOTAL_NUM) @RequestParam("orderTotalNum") BigDecimal orderTotalNum,
            @ApiParam(BalancetApi.addSellOrderConfirm.METHOD_API_LIMIT_NUM_MIN) @RequestParam("limitNumMin") BigDecimal limitNumMin,
            @ApiParam(BalancetApi.addSellOrderConfirm.METHOD_API_LIMIT_NUM_MAX) @RequestParam("limitNumMax") BigDecimal limitNumMax,
            @ApiParam(BalancetApi.addSellOrderConfirm.METHOD_API_PAY_TYPE) @RequestParam("payType") Integer payType,
            @ApiParam(BalancetApi.addSellOrderConfirm.METHOD_API_PAY_BALANCECONVERTPERCENT) @RequestParam("balanceConvertPercent") BigDecimal balanceConvertPercent,
            HttpServletRequest request) {
        String userId = SessionUtils.getSession(request.getSession()).getUserId();
        BigDecimal flowBalance = walletService.getByUserId(userId).getFlowBalance();//获取用户的钱包流动余额
        //判断用户的流动余额是否小于交易总额
        if (flowBalance.compareTo(orderTotalNum) < 0) {
            throw new BalanceOrderException(BalanceOrderResultEnums.FLOW_BALANCE_INSUFFICIENT);
        }

        return ResultUtils.buildSuccessDto(balanceOrderService.headerBalanceOrderSellConfirm(orderTotalNum, limitNumMin, limitNumMax, payType,balanceConvertPercent));
    }

    /**
     * 创建余额交易买单订单
     *
     * @param balanceOrderInfoDto 买单信息
     * @return
     */
    @ApiOperation(value = BalancetApi.addBuyOrder.METHOD_API_NAME
            , notes = BalancetApi.addBuyOrder.METHOD_API_NOTE)
    @RequestMapping(value = "/addBuyOrder", method = RequestMethod.POST)
    public ResultDto addBuyOrder(
            @ApiParam(BalancetApi.addBuyOrder.METHOD_API_BALANCE_ORDER) BalanceOrderInfoDto balanceOrderInfoDto,
            HttpServletRequest request) {
        String userId = SessionUtils.getSession(request.getSession()).getUserId();
//        String userId = "cca13e40-5332-42d2-88d2-38474ec81c50";
        boolean success = balanceOrderService.insertBalanceOrderBuy(userId, balanceOrderInfoDto);
        if (success) {
            return ResultUtils.buildSuccessDto(success);
        } else {
            ResultDto resultDto = new ResultDto();
            resultDto.setMsg(BalanceOrderResultEnums.ADD_ERROR.getMsg());
            resultDto.setCode(BalanceOrderResultEnums.ADD_ERROR.getCode());
            resultDto.setData(success);
            return resultDto;
        }
    }


    /**
     * 创建余额交易卖单订单
     *
     * @param balanceOrderInfoDto 买单信息
     * @return
     */
    @ApiOperation(value = BalancetApi.addSellOrder.METHOD_API_NAME
            , notes = BalancetApi.addSellOrder.METHOD_API_NOTE)
    @RequestMapping(value = "/addSellOrder", method = RequestMethod.POST)
    public ResultDto addSellOrder(
            @ApiParam(BalancetApi.addSellOrder.METHOD_API_BALANCE_ORDER) BalanceOrderInfoDto balanceOrderInfoDto,
            HttpServletRequest request) {
        String userId = SessionUtils.getSession(request.getSession()).getUserId();
        String telephone = SessionUtils.getSession(request.getSession()).getTelephone();
//        String userId = "cca13e40-5332-42d2-88d2-38474ec81c50";
//        String telephone = "18888888888";
        BigDecimal flowBalance = walletService.getByUserId(userId).getFlowBalance();//获取用户的钱包流动余额
        //判断用户的流动余额是否小于交易总额
        if (flowBalance.compareTo(balanceOrderInfoDto.getOrderTotalNum()) < 0) {
            throw new BalanceOrderException(BalanceOrderResultEnums.FLOW_BALANCE_INSUFFICIENT);
        }
        boolean success = balanceOrderService.insertBalanceOrderSell(userId, telephone, balanceOrderInfoDto);
        if (success) {
            return ResultUtils.buildSuccessDto(success);
        } else {
            ResultDto resultDto = new ResultDto();
            resultDto.setMsg(BalanceOrderResultEnums.ADD_ERROR.getMsg());
            resultDto.setCode(BalanceOrderResultEnums.ADD_ERROR.getCode());
            resultDto.setData(success);
            return resultDto;
        }
    }

    /***
     * 根据筛选条件查询 *发布* 订单
     * @param orderType 买/卖单
     * @param payType 支付方式
     * @param orderNum 交易数量
     * @param pageNum 起始行号
     * @param pageSize 总行数
     * @return
     */
    @ApiOperation(value = BalancetApi.selectByCondition.METHOD_API_NAME
            , notes = BalancetApi.selectByCondition.METHOD_API_NOTE)
    @RequestMapping(value = "/selectByCondition", method = RequestMethod.POST)
    public ResultDto selectByCondition(HttpServletRequest request,
           @ApiParam(BalancetApi.selectByCondition.METHOD_API_ORDERTYPE) @RequestParam("orderType") Integer orderType,
           @ApiParam(BalancetApi.selectByCondition.METHOD_API_PAYTYPE) @RequestParam(value = "payType", required = false) Integer payType,
           @ApiParam(BalancetApi.selectByCondition.METHOD_API_ORDERNUM) @RequestParam(value = "orderNum", required = false) BigDecimal orderNum,
           @ApiParam(BalancetApi.selectByCondition.METHOD_API_PAGENUM) @RequestParam(value = "pageNum", required = false) Integer pageNum,
           @ApiParam(BalancetApi.selectByCondition.METHOD_API_PAGESIZE) @RequestParam(value = "pageSize", required = false) Integer pageSize,
            @ApiParam(BalancetApi.selectByCondition.METHOD_API_ORDERBYTYPE) @RequestParam(value = "orderByType", required = false) String orderByType) {
        UserSessionDto session = SessionUtils.getSession(request.getSession());
        return ResultUtils.buildSuccessDto(balanceOrderService.listByCondition(session.getUserId(), orderType, payType, orderNum, pageNum, pageSize,orderByType));
    }

    /***
     * 根据筛选条件查询 *交易* 订单
     * @param request
     * @param orderType
     * @param billStatus
     * @return
     */
    @ApiOperation(value = BalancetApi.listByOrderTypeAndBillStatus.METHOD_API_NAME
            , notes = BalancetApi.listByOrderTypeAndBillStatus.METHOD_API_NOTE)
    @RequestMapping(value = "/listByOrderTypeAndBillStatus", method = RequestMethod.POST)
    public ResultDto listByOrderTypeAndBillStatus(HttpServletRequest request,
          @ApiParam(BalancetApi.listByOrderTypeAndBillStatus.METHOD_API_ORDERTYPE) @RequestParam(value = "orderType", required = false) Integer orderType,
          @ApiParam(BalancetApi.listByOrderTypeAndBillStatus.METHOD_API_BILLSTATUS) @RequestParam("billStatus") Integer billStatus,
          @ApiParam(BalancetApi.listByOrderTypeAndBillStatus.METHOD_API_PAGENUM) @RequestParam(value = "pageNum", required = false) Integer pageNum,
          @ApiParam(BalancetApi.listByOrderTypeAndBillStatus.METHOD_API_PAGESIZE) @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        UserSessionDto session = SessionUtils.getSession(request.getSession());
        List<GetBalanceOrderBillByConditionDto> list = balanceOrderBillService.listByOrderTypeAndBillStatus(session.getUserId(), orderType, billStatus, pageNum, pageSize);
        return ResultUtils.buildSuccessDto(list);
    }

    /***
     * 下单确认功能
     * @param id
     * @param orderUserId
     * @param moneyAmount
     * @param amount
     * @return
     */
    @ApiOperation(value = BalancetApi.headerOrderConfirmation.METHOD_API_NAME
            , notes = BalancetApi.headerOrderConfirmation.METHOD_API_NOTE)
    @RequestMapping(value = "/headerOrderConfirmation", method = RequestMethod.POST)
    public ResultDto headerOrderConfirmation(HttpServletRequest request,
             @ApiParam(BalancetApi.headerOrderConfirmation.METHOD_AIP_ID) @RequestParam("id") String id,
             @ApiParam(BalancetApi.headerOrderConfirmation.METHOD_AIP_ORDERUSERID) @RequestParam("orderUserId") String orderUserId,
             @ApiParam(BalancetApi.headerOrderConfirmation.METHOD_AIP_MONEYAMOUNT) @RequestParam("moneyAmount") BigDecimal moneyAmount,
             @ApiParam(BalancetApi.headerOrderConfirmation.METHOD_AIP_AMOUNT) @RequestParam("amount") BigDecimal amount) {
        UserSessionDto session = SessionUtils.getSession(request.getSession());
        BalanceOrderBill bill = balanceOrderBillService.headerOrderConfirmation(id, orderUserId, session.getUserId(), moneyAmount, amount);
        ResultDto d = new ResultDto();
        d.setMsg("下单成功！");
        d.setCode("200");
        addNewOrderIdToRedis(bill.getId());//将订单加到缓存
        return d;
    }

    /***
     * 放弃买单
     * @param id
     * @return
     */
    @ApiOperation(value = BalancetApi.headerAbandonBuy.METHOD_API_NAME
            , notes = BalancetApi.headerAbandonBuy.METHOD_API_NOTE)
    @RequestMapping(value = "/headerAbandonBuy", method = RequestMethod.POST)
    public ResultDto headerAbandonBuy(
            @ApiParam(BalancetApi.headerAbandonBuy.METHOD_AIP_ID) @RequestParam("id") String id) {
        balanceOrderBillService.headerAbandonBuy(id);
        removeNewOrderIdFromRedis(id);//从redis移除新建订单id的key
        return ResultUtils.buildSuccessDto(null);
    }

    /***
     * 卖家点击下一步，将用户状态和订单状态设置为进行中
     * 已不需要该功能
     * @param id
     * @return
     */
    @Deprecated
    @ApiOperation(value = BalancetApi.updateOrderBillStatus.METHOD_API_NAME
            , notes = BalancetApi.updateOrderBillStatus.METHOD_API_NOTE)
    @RequestMapping(value = "/updateOrderBillStatus", method = RequestMethod.POST)
    public ResultDto updateOrderBillStatus(HttpServletRequest request,
                                           @ApiParam(BalancetApi.updateOrderBillStatus.METHOD_AIP_ID) @RequestParam("id") String id) {
        UserSessionDto session = SessionUtils.getSession(request.getSession());
        balanceOrderBillService.updateOrderBillStatus(id, session.getUserId());
        removeNewOrderIdFromRedis(id);//从redis移除新建订单id的key
        return ResultUtils.buildSuccessDto(null);
    }

    /***
     * 确认付款
     * @param id
     * @param password
     * @return
     */
    @ApiOperation(value = BalancetApi.headerBuyAffirm.METHOD_API_NAME
            , notes = BalancetApi.headerBuyAffirm.METHOD_API_NOTE)
    @RequestMapping(value = "/headerBuyAffirm", method = RequestMethod.POST)
    public ResultDto headerBuyAffirm(HttpServletRequest request,
             @ApiParam(BalancetApi.headerBuyAffirm.METHOD_AIP_ID) @RequestParam("id") String id,
             @ApiParam(BalancetApi.headerBuyAffirm.METHOD_AIP_PASSWORD) @RequestParam("password") String password) {
        UserSessionDto session = SessionUtils.getSession(request.getSession());
        balanceOrderBillService.headerBuyAffirm(id, password, session.getUserId());
        removeNewOrderIdFromRedis(id);//从redis移除新建订单id的key
        return ResultUtils.buildSuccessDto(null);
    }

    /***
     * 确认收款
     * @param id
     * @param password
     * @return
     */
    @ApiOperation(value = BalancetApi.headerSellAffirm.METHOD_API_NAME
            , notes = BalancetApi.headerSellAffirm.METHOD_API_NOTE)
    @RequestMapping(value = "/headerSellAffirm", method = RequestMethod.POST)
    public ResultDto headerSellAffirm(HttpServletRequest request,
            @ApiParam(BalancetApi.headerSellAffirm.METHOD_AIP_ID) @RequestParam("id") String id,
            @ApiParam(BalancetApi.headerSellAffirm.METHOD_AIP_PASSWORD) @RequestParam("password") String password) {
        UserSessionDto session = SessionUtils.getSession(request.getSession());
        balanceOrderBillService.headerSellAffirm(id,password,session.getUserId());
        removeNewOrderIdFromRedis(id);//从redis移除新建订单id的key
        return ResultUtils.buildSuccessDto(null);
    }

    /***
     * 申诉
     * @param billId
     * @param appealFileName
     * @return
     */
    @ApiOperation(value = BalancetApi.headerAppeal.METHOD_API_NAME
            , notes = BalancetApi.headerAppeal.METHOD_API_NOTE)
    @RequestMapping(value = "/headerAppeal", method = RequestMethod.POST)
    public ResultDto headerAppeal(HttpServletRequest request,
          @ApiParam(BalancetApi.headerAppeal.METHOD_AIP_BILLID) @RequestParam("billId") String billId,
          @ApiParam(BalancetApi.headerAppeal.METHOD_AIP_APPEALFILENAME) @RequestParam("appealFileName") String appealFileName,
          @ApiParam(BalancetApi.headerAppeal.METHOD_AIP_APPEALTEXT) @RequestParam("appealText") String appealText) {
        UserSessionDto session = SessionUtils.getSession(request.getSession());
        balanceOrderBillService.headerAppeal(billId, session.getUserId(), appealFileName, appealText);
        return ResultUtils.buildSuccessDto(null);
    }

    /***
     * 同意申诉
     * @param appealId
     * @return
     */
    @ApiOperation(value = BalancetApi.headerAgreed2Appeal.METHOD_API_NAME
            , notes = BalancetApi.headerAgreed2Appeal.METHOD_API_NOTE)
    @RequestMapping(value = "/headerAgreed2Appeal", method = RequestMethod.POST)
    public ResultDto headerAgreed2Appeal(
            @ApiParam(BalancetApi.headerAgreed2Appeal.METHOD_AIP_APPEALID) @RequestParam("appealId") String appealId) {
        balanceOrderBillService.headerAgreed2Appeal(appealId);
        return ResultUtils.buildSuccessDto(null);
    }

    /***
     * 驳回申诉
     * @param appealId
     * @return
     */
    @ApiOperation(value = BalancetApi.headerReject2Appeal.METHOD_API_NAME
            , notes = BalancetApi.headerReject2Appeal.METHOD_API_NOTE)
    @RequestMapping(value = "/headerReject2Appeal", method = RequestMethod.POST)
    public ResultDto headerReject2Appeal(
            @ApiParam(BalancetApi.headerReject2Appeal.METHOD_AIP_APPEALID) @RequestParam("appealId") String appealId) {
        balanceOrderBillService.headerReject2Appeal(appealId);
        return ResultUtils.buildSuccessDto(null);
    }

    /***
     * 撤销申述
     * @param appealId
     * @return
     */
    @ApiOperation(value = BalancetApi.headerRepeal2Appeal.METHOD_API_NAME
            , notes = BalancetApi.headerRepeal2Appeal.METHOD_API_NOTE)
    @RequestMapping(value = "/headerRepeal2Appeal", method = RequestMethod.POST)
    public ResultDto headerRepeal2Appeal(
            @ApiParam(BalancetApi.headerRepeal2Appeal.METHOD_AIP_APPEALID) @RequestParam("appealId") String appealId) {
        balanceOrderBillService.headerRepeal2Appeal(appealId);
        return ResultUtils.buildSuccessDto(null);
    }

    /***
     * 强制撤销总单
     * @param orderId
     */
    @ApiOperation(value = BalancetApi.handleCancelOrder.METHOD_API_NAME
            , notes = BalancetApi.handleCancelOrder.METHOD_API_NOTE)
    @RequestMapping(value = "/handleCancelOrder", method = RequestMethod.POST)
    public ResultDto handleCancelOrder(HttpServletRequest request,
            @ApiParam(BalancetApi.handleCancelOrder.METHOD_AIP_ORDERID) @RequestParam("orderId") String orderId) {
        UserSessionDto session = SessionUtils.getSession(request.getSession());
        balanceOrderService.cancelOrder(orderId,session.getUserId());
        return ResultUtils.buildSuccessDto(null);
    }

    /***
     * 2018.8.29 09:07
     * lzf
     *
     *
     *
     *
     */

    /**
     * USERNAME:YEHAO
     */
    @ApiOperation(value = BalancetApi.selectBalanceOrderUpdateById.METHOD_API_NAME
            , notes = BalancetApi.selectBalanceOrderUpdateById.METHOD_API_NOTE)
    @RequestMapping(value = "/selectBalanceOrderUpdateById", method = RequestMethod.GET)
    public ResultDto selectBalanceOrderUpdateById(@ApiParam(BalancetApi.selectBalanceOrderUpdateById.METHOD_AIP_ID) @RequestParam("id") String id) {
        return ResultUtils.buildSuccessDto(balanceOrderService.selectBalanceOrderUpdateById(id));
    }

    /**
     * 修改买单的状态详情
     *
     * @param balanceOrderUpdateDto
     * @return
     */
    @ApiOperation(value = BalancetApi.headerBalanceOrderBuyUpdateConfirm.METHOD_API_NAME
            , notes = BalancetApi.headerBalanceOrderBuyUpdateConfirm.METHOD_API_NOTE)
    @RequestMapping(value = "/headerBalanceOrderBuyUpdateConfirm", method = RequestMethod.POST)
    public ResultDto headerBalanceOrderBuyUpdateConfirm(BalanceOrderUpdateDto balanceOrderUpdateDto) {
        return ResultUtils.buildSuccessDto(balanceOrderService.headerBalanceOrderBuyUpdateConfirm(balanceOrderUpdateDto));
    }

    /**
     * 修改卖单的状态详情
     *
     * @param balanceOrderUpdateDto
     * @return
     */
    @ApiOperation(value = BalancetApi.headerBalanceOrderSellUpdateConfirm.METHOD_API_NAME
            , notes = BalancetApi.headerBalanceOrderSellUpdateConfirm.METHOD_API_NOTE)
    @RequestMapping(value = "/headerBalanceOrderSellUpdateConfirm", method = RequestMethod.POST)
    public ResultDto headerBalanceOrderSellUpdateConfirm(BalanceOrderUpdateDto balanceOrderUpdateDto) {
        return ResultUtils.buildSuccessDto(balanceOrderService.headerBalanceOrderSellUpdateConfirm(balanceOrderUpdateDto));
    }

    /**
     * 修改余额交易买单订单
     *
     * @param balanceOrderInfoDto 订单信息
     * @return
     */
    @ApiOperation(value = BalancetApi.updateBuyOrder.METHOD_API_NAME
            , notes = BalancetApi.updateBuyOrder.METHOD_API_NOTE)
    @RequestMapping(value = "/updateBuyOrder", method = RequestMethod.POST)
    public ResultDto updateBuyOrder(
            BalanceOrderInfoDto balanceOrderInfoDto,
            HttpServletRequest request) {
        boolean success = balanceOrderService.updateBalanceOrderBuy(balanceOrderInfoDto);
        if (success) {
            return ResultUtils.buildSuccessDto(success);
        } else {
            ResultDto resultDto = new ResultDto();
            resultDto.setMsg(BalanceOrderResultEnums.UPDATE_ERROR.getMsg());
            resultDto.setCode(BalanceOrderResultEnums.UPDATE_ERROR.getCode());
            resultDto.setData(success);
            return resultDto;
        }
    }


    /**
     * 修改余额交易卖单订单
     *
     * @param balanceOrderInfoDto 订单信息
     * @return
     */
    @ApiOperation(value = BalancetApi.updateSellOrder.METHOD_API_NAME
            , notes = BalancetApi.updateSellOrder.METHOD_API_NOTE)
    @RequestMapping(value = "/updateSellOrder", method = RequestMethod.POST)
    public ResultDto updateSellOrder(
            BalanceOrderInfoDto balanceOrderInfoDto,
            HttpServletRequest request) {
        String userId = SessionUtils.getSession(request.getSession()).getUserId();
        String telephone = SessionUtils.getSession(request.getSession()).getTelephone();
//        String userId = "cca13e40-5332-42d2-88d2-38474ec81c50";
//        String telephone = "18888888888";
        boolean success = balanceOrderService.updateBalanceOrderSell(userId, telephone, balanceOrderInfoDto);
        if (success) {
            return ResultUtils.buildSuccessDto(success);
        } else {
            ResultDto resultDto = new ResultDto();
            resultDto.setMsg(BalanceOrderResultEnums.UPDATE_ERROR.getMsg());
            resultDto.setCode(BalanceOrderResultEnums.UPDATE_ERROR.getCode());
            resultDto.setData(success);
            return resultDto;
        }
    }

    /**
     * 已发布分页查询接口
     *
     * @param pageNum  第几页
     * @param pageSize 一页几条
     * @return
     */
    @ApiOperation(value = BalancetApi.listByReleaseRecords.METHOD_API_NAME
            , notes = BalancetApi.listByReleaseRecords.METHOD_API_NOTE)
    @RequestMapping(value = "/listByReleaseRecords", method = RequestMethod.GET)
    public ResultDto listByReleaseRecords(
            @ApiParam(BalancetApi.listByReleaseRecords.METHOD_API_NOTE_PAGENUM) @RequestParam("pageNum") Integer pageNum,
            @ApiParam(BalancetApi.listByReleaseRecords.METHOD_API_NOTE_PAGESIZE) @RequestParam(value = "pageSize", required = false) Integer pageSize,
            HttpServletRequest request) {
        String userId = SessionUtils.getSession(request.getSession()).getUserId();
        return ResultUtils.buildSuccessDto(balanceOrderService.listByReleaseRecords(userId, pageNum, pageSize));
    }

    /**
     * 买单撤单
     *
     * @param id 订单ID
     * @return
     */
    @ApiOperation(value = BalancetApi.updateBalanceBuyOrderInvalid.METHOD_API_NAME
            , notes = BalancetApi.updateBalanceBuyOrderInvalid.METHOD_API_NOTE)
    @RequestMapping(value = "/updateBalanceBuyOrderInvalid", method = RequestMethod.GET)
    public ResultDto updateBalanceBuyOrderInvalid(@ApiParam(BalancetApi.updateBalanceBuyOrderInvalid.METHOD_API_ID) @RequestParam("id") String id) {
        ExceptionPreconditionUtils.checkStringArgumentsNotBlank(id, new BalanceOrderException(BalanceOrderResultEnums.NO_ORDER));
        return ResultUtils.buildSuccessDto(balanceOrderService.updateBalanceBuyOrderInvalid(id));
    }

    /**
     * 卖单撤单
     *
     * @param id 订单ID
     * @return
     */
    @ApiOperation(value = BalancetApi.updateBalanceSellOrderInvalid.METHOD_API_NAME
            , notes = BalancetApi.updateBalanceSellOrderInvalid.METHOD_API_NOTE)
    @RequestMapping(value = "/updateBalanceSellOrderInvalid", method = RequestMethod.GET)
    public ResultDto updateBalanceSellOrderInvalid(@ApiParam(BalancetApi.updateBalanceSellOrderInvalid.METHOD_API_ID) @RequestParam("id") String id, HttpServletRequest request) {
        ExceptionPreconditionUtils.checkStringArgumentsNotBlank(id, new BalanceOrderException(BalanceOrderResultEnums.NO_ORDER));
        String userId = SessionUtils.getSession(request.getSession()).getUserId();
        String telephone = SessionUtils.getSession(request.getSession()).getTelephone();
        return ResultUtils.buildSuccessDto(balanceOrderService.updateBalanceSellOrderInvalid(id, userId, telephone));
    }

    /**
     * **********************
     *       huangxl
     * **********************
     */
    @ApiOperation(value = "获取余额交易的手续费和补贴费用"
            , notes = "获取余额交易的手续费和补贴费用")
    @GetMapping("/getCharge")
    public ResultDto selectBalanceOrderCharge(){
        InformationForBalanceOrderDto information = informationService.selectBalanceOrderMsg();
        return ResultUtils.buildSuccessDto(information);
    }

    /**
     * 下单后将订单id放到redis，并设置15分钟失效
     *
     * @param orderId
     */
    private void addNewOrderIdToRedis(String orderId) {
        //放到redis缓存，并设置失效时间为15分钟
        String key = BaseConstants.REDIS_KEY_ORDER_NEW_PRE + orderId;
        redisTemplate.opsForValue().set(key, System.currentTimeMillis(), BaseConstants.ORDER_NEW_EXPIRE_MILLISECONDS, TimeUnit.MILLISECONDS);
        redisTemplate.opsForList().rightPush(BaseConstants.REDIS_KEY_ORDER_NEW_LIST, orderId);
    }

    /**
     * 从redis移除订单id
     *
     * @param orderId
     */
    private void removeNewOrderIdFromRedis(String orderId) {
        String key = BaseConstants.REDIS_KEY_ORDER_NEW_PRE + orderId;
        redisTemplate.delete(key);
        redisTemplate.opsForList().remove(BaseConstants.REDIS_KEY_ORDER_NEW_LIST, 0, orderId);
    }
}
