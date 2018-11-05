package com.cwt.domain.dto.store;

import com.cwt.domain.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserReceiveListPO extends BaseDto {

    private String telephone;
    private String orderNo;
    private String status;
    private String fee_ct;
    private String fee_integral;
    private Date modify_time;

}
