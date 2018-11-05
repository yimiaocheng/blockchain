package com.cwt.app.controller;

import com.cwt.app.common.util.RedisUtils;
import com.cwt.app.common.util.ResultUtils;
import com.cwt.common.constant.BaseConstants;
import com.cwt.common.constant.UrlConstants;
import com.cwt.common.util.BeanPreconditionUtils;
import com.cwt.domain.dto.ResultDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Api("币币行情控制器")
@RequestMapping("/marker")
@RestController
public class CoinMarkerController {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private RedisUtils redisUtils;


    @ApiOperation(value = "币币行情获取接口", notes = "币币行情获取接口")
    @RequestMapping(value = "/getCoinMarker", method = RequestMethod.GET)
    public ResultDto getCoinMarker() {
        ResponseEntity<JSONObject> entityBTC = restTemplate.getForEntity(UrlConstants.BTC_PRICE_URL, JSONObject.class);
        ResponseEntity<JSONObject> entityETH = restTemplate.getForEntity(UrlConstants.ETH_PRICE_URL, JSONObject.class);
        if (200 == (entityBTC.getStatusCodeValue()) && 200 == (entityETH.getStatusCodeValue())) {
            Map<String, Double> coinMarkerMap = new HashMap<>();
            JSONObject jsonBTC = entityBTC.getBody();
            JSONObject jsonETH = entityETH.getBody();


            Double BTN_NAME = jsonBTC.getJSONObject("data").getDouble("amount");
            Double ETH_NAME = jsonETH.getJSONObject("data").getDouble("amount");
            Double BTN_OLD = 0.0;
            Double ETH_OLD = 0.0;

            Object oldBtc = redisUtils.getValue(BaseConstants.LAST_TIME_BTC_PRICE_KEY);
            Object oldEth = redisUtils.getValue(BaseConstants.LAST_TIME_ETH_PRICE_KEY);

            if(!BeanPreconditionUtils.checkNotNull(oldBtc)){
                try {
                    BTN_OLD = new Double(oldBtc.toString());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
            if(!BeanPreconditionUtils.checkNotNull(oldEth)){
                try {
                    ETH_OLD = new Double(oldEth.toString());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }

            coinMarkerMap.put(jsonBTC.getJSONObject("data").getString("base")+"_NEW",BTN_NAME);
            coinMarkerMap.put(jsonETH.getJSONObject("data").getString("base")+"_NEW",ETH_NAME);
            coinMarkerMap.put(jsonBTC.getJSONObject("data").getString("base")+"_OLD",BTN_OLD);
            coinMarkerMap.put(jsonETH.getJSONObject("data").getString("base")+"_OLD",ETH_OLD);

            return ResultUtils.buildSuccessDto(coinMarkerMap);

        } else {
            ResultDto resultDto = new ResultDto();
            resultDto.setCode("404");
            resultDto.setMsg("币币行情最新情况获取失败");
            return resultDto;
        }
    }


}
