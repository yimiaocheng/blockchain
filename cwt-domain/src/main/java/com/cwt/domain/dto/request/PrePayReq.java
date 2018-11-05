package com.cwt.domain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrePayReq implements Serializable{

    private String fromId;//用户id
    private String order_no;//订单id
    private String fee_ct;//订单费用ct
    private String fee_integral;//订单费用integral
    private String body;//商品描述
    private String notify_url;//支付回调地址
    private String state;//自定义参数
    private String sign;//签名
    private String timesiamp;//时间戳


}
