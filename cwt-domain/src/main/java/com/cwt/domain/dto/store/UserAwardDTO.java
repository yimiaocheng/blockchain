package com.cwt.domain.dto.store;

import com.cwt.domain.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAwardDTO extends BaseDto {

    private String telephone;
    private String award_type;
    private String award_orderNo;
    private String award_status;
    private Date beginTime;//开始时间
    private Date endTime;//结束时间

}
