package com.cwt.app.listener;

import com.cwt.common.constant.BaseConstants;
import com.cwt.service.service.BalanceOrderBillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisReceiver {
    private static final Logger LOG = LoggerFactory.getLogger(RedisReceiver.class);
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private BalanceOrderBillService balanceOrderBillService;
    /**
     * redis key过期之后，会通过反射来调用此方法
     * @param message
     */
    public void receiveMessage(String message) {
        LOG.info("=======receiveMessage  redis invalid key is ======"+message);
        //判断是否有 订单key的前缀，如果有就进行放弃处理
        if(message.startsWith(BaseConstants.REDIS_KEY_ORDER_NEW_PRE)){
            //获取订单
            String orderId = message.substring(BaseConstants.REDIS_KEY_ORDER_NEW_PRE.length());
            LOG.debug("orderId:"+orderId);
            try {
                balanceOrderBillService.headerAutoFail(orderId);
                removeNewOrderIdFromRedis(orderId);
            }catch (Exception e){
                e.printStackTrace();
                LOG.error("redis receiveMessage handle fail======"+e.getMessage());
            }
        }
    }

    /**
     * 移除对应id
     * @param orderId
     */
    private void removeNewOrderIdFromRedis(String orderId){
        System.out.println("leftpop  key="+BaseConstants.REDIS_KEY_ORDER_NEW_LIST+",value="+orderId);
        redisTemplate.opsForList().remove(BaseConstants.REDIS_KEY_ORDER_NEW_LIST,0,orderId);
    }
}
