package com.cwt.domain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAwardPO implements Serializable{

    private String userId;
    private String award_ct;
    private String award_integral;
    private String award_remark;
}


