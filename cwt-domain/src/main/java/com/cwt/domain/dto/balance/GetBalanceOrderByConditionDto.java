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
public class GetBalanceOrderByConditionDto extends BaseDto {
	private String id;//记录id
	private String userId;//发布者id
	private String userName;//发布者账号
	private BigDecimal orderNum;//订单剩余
	private BigDecimal limitNumMin;//最小限额
	private BigDecimal limitNumMax;//最大限额
	private Integer payType;//支付方式
	private Integer orderType;//订单类型
	private java.util.Date createTime;//创建时间
	private java.util.Date modifyTime;//修改时间
	private BigDecimal flowBalance;//用户余额
	private String headImg;//头像
	private String nickName;//昵称
	private BigDecimal balanceConvertPercent;//余额和现金的比例  余额：现金
	private BigDecimal serverCharge;//平台抽取的手续费  公式：订单总数 * 手续费比例
	private BigDecimal subsidy;//卖家补贴给买家的比例
	private BigDecimal orderTotalNum;//购买总数
	private Integer orderStatus;//订单状态
	private int transationTotal;//交易总数
	private int appealTotal;//申诉总数
	private int appealSuccess;//胜诉总数
	private int appealFail;//败诉总数

}