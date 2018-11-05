package com.cwt.domain.dto.store;

import com.cwt.domain.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRefundListPO extends BaseDto {

    private String userId;
    private String businessId;
    private String orderId;
    private String fee_ct;
    private String fee_integral;
    private String refund_status;
    private String remark;
    private Date modify_time;

}
