package com.cwt.domain.dto.appeal;

import lombok.Data;

import java.util.Date;

/**
 * 后台申诉列表
 */
@Data
public class AppealListDto {
    private String id;//申诉id
    private String userName;//申诉人手机号
    private String balanceBillId;//订单明细ID
    private String appealFile;//文件路径
    private String appealExplain;//申诉描述
    private Integer status;//状态 新建(0)，同意(1)，驳回(2)，作废(3)
    private Date creatDatetime;//创建时间
}
