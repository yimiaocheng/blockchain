package com.cwt.common.util;


import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author huangxl
 * 用于余额交易运算
 */
public class BalanceComputeUtils {
    /**
     * 返还的数额，例如余额交易的撤单、余额交易剩余数量<最小限购数等，需要将余额返还给挂单者
     * 公式：返还数额 = 数量 / (1 - 手续费 - 补贴)
     * @param number  计算的数额
     * @param total 挂单的总额 ,预留参数
     * @param serviceCharge 平台手续费
     * @param subsidyCharge 补贴
     * @return 返还的数额
     */
    public static BigDecimal freeBack(BigDecimal number,BigDecimal total,BigDecimal serviceCharge,BigDecimal subsidyCharge){
        BigDecimal deduction = BigDecimal.valueOf(1).subtract(serviceCharge).subtract(subsidyCharge);
        return number.divide(deduction,4, RoundingMode.HALF_UP);
    }

    /**
     * 购买实际获得的收益
     * 公式：实际收入 = 购买数量 * (1 - 手续费) / (1 - 手续费 - 补贴)
     * @param number 购买数量
     * @param total 挂单总额 ，预留参数
     * @param serviceCharge 平台手续费
     * @param subsidyCharge 补贴
     * @return 实际获得的数额
     */
    public static BigDecimal actualIncome(BigDecimal number,BigDecimal total,BigDecimal serviceCharge,BigDecimal subsidyCharge){
        BigDecimal var1 = BigDecimal.valueOf(1).subtract(serviceCharge);
        BigDecimal var2 = BigDecimal.valueOf(1).subtract(serviceCharge).subtract(subsidyCharge);
        return number.multiply(var1).divide(var2,4, RoundingMode.HALF_UP);
    }

    /**
     * 余额交易实际出售的交易总额（卖单）
     * 公式：实际交易总额 = 创建交易总额 * （1 - 平台手续费比例 - 补贴比例 ）
     * @param number          创建卖单的交易总额
     * @param serviceCharge  平台手续费比例
     * @param subsidyCharge  补贴比例
     * @return  返回实际交易总额
     */
    public  static BigDecimal sellOrderNum(BigDecimal number,BigDecimal serviceCharge,BigDecimal subsidyCharge){
        BigDecimal deduction = BigDecimal.valueOf(1).subtract(serviceCharge).subtract(subsidyCharge);
        return number.multiply(deduction);
    }

    /**
     * 余额交易实际获得的流动余额（买单）
     * 公式：实际交易总额 = 创建交易总额 * 补贴比例 / (1 - 平台手续费- 补贴比例)  +  创建交易总额
     * @param number          创建买单的交易总额
     * @param serviceCharge  平台手续费比例
     * @param subsidyCharge  补贴比例
     * @return  实际获得的流动余额
     */
    public  static BigDecimal buyFlowBalance(BigDecimal number,BigDecimal serviceCharge,BigDecimal subsidyCharge){
        BigDecimal deduction = BigDecimal.valueOf(1).subtract(serviceCharge).subtract(subsidyCharge);
        return number.multiply(subsidyCharge).divide(deduction,4, RoundingMode.HALF_UP).add(number);
    }


    /***
     * 余额交易-手续费-
     * 公式： 手续费比率 * 交易总额 / (1-手续费比率-补贴比率)
     * @param number
     * @param serviceCharge
     * @param subsidy
     * @return
     */
    public static BigDecimal realServiceCharge(BigDecimal number,BigDecimal serviceCharge,BigDecimal subsidy){
        BigDecimal deduction = serviceCharge.multiply(number.divide(BigDecimal.valueOf(1).subtract(serviceCharge).subtract(subsidy),4, RoundingMode.HALF_UP));
        return deduction;
    }

    /***
     * 余额交易-补贴-
     * 公式： 补贴比率 * 交易总额 / (1-手续费比率-补贴比率)
     * @param number
     * @param serviceCharge
     * @param subsidy
     * @return
     */
    public static BigDecimal realSubsidy(BigDecimal number,BigDecimal serviceCharge,BigDecimal subsidy){
        BigDecimal deduction = subsidy.multiply(number.divide(BigDecimal.valueOf(1).subtract(serviceCharge).subtract(subsidy),4, RoundingMode.HALF_UP));
        return deduction;
    }


}
