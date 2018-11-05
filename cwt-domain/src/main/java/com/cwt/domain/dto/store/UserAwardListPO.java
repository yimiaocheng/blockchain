package com.cwt.domain.dto.store;

import com.cwt.domain.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAwardListPO extends BaseDto {

    private String telephone;
    private String award_ct;
    private String award_integral;
    private String award_type;
    private String award_remark;
    private String award_orderNo;
    private String award_status;
    private Date modify_time;


}
