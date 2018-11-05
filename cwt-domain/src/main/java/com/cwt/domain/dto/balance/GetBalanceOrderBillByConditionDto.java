package com.cwt.domain.dto.balance;

import com.cwt.domain.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * GetBalanceOrderByConditionDto 数据传输类
 * 购买/出售余额列表数据
 * @date 2018-08-22 17:28:41
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetBalanceOrderBillByConditionDto extends BaseDto {
	private String id;//记录id
	private String orderId;//发布订单id
	private String headImg;//头像
	private String nickName;//昵称
	private String userName;//手机号
	private String optUserName;//操作用户昵称
	private String optNickName;//操作用户账号
	private String optHeadImg;
	private BigDecimal limitNumMin;//最小限额
	private BigDecimal limitNumMax;//最大限额
	private String orderUserId;//发布用户id
	private Integer orderUserStatus;//发布用户状态
	private String optUserId;//交易用户id
	private Integer optUserStatus;//操作用户的状态
	private Integer billStatus;//订单状态
	private BigDecimal moneyAmount;//金额数量
	private BigDecimal amount;//余额数量
	private BigDecimal amountReal;//余额数量
	private BigDecimal balanceConvertPercent;//金额：余额 比率
	private Integer payType;//支付方式
	private Integer orderType;//订单类型
	private java.util.Date createTime;//创建时间
	private java.util.Date modifyTime;//修改时间
}