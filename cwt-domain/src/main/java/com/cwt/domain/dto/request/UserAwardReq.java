package com.cwt.domain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAwardReq implements Serializable{

    private String award_type;//奖金类型 1.销售提成2.差额利润3.服务补贴4.加权分红
    private String award_orderNo;
    private List<UserAwardPO> award;
}
