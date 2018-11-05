package com.cwt.service.service.impl;

import com.cwt.common.enums.UserResultEnums;
import com.cwt.common.exception.UserExeption;
import com.cwt.common.util.BeanUtils;
import com.cwt.domain.dto.user.SmsgLimitDto;
import com.cwt.domain.entity.SmsgLimit;
import com.cwt.persistent.mapper.SmsgLimitMapper;
import com.cwt.service.service.SmsgLimitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;

@Service("smsgLimitService")
public class SmsgLimitServiceImpl implements SmsgLimitService {
    @Autowired
    private SmsgLimitMapper smsgLimitMapper;

    @Override
    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.READ_COMMITTED)
    public SmsgLimitDto selectCountByMobilePhone(String mobilephone) {

        SmsgLimitDto smsgLimitDto = smsgLimitMapper.selectCountByMobilePhone(mobilephone);  //查询今日验证码条数

        if(smsgLimitDto == null){  //如果没记录（首次获取验证码），插入一条数据
            SmsgLimit smsgLimit = new SmsgLimit();
            smsgLimit.setId(UUID.randomUUID().toString());
            smsgLimit.setMobilephone(mobilephone);
            smsgLimit.setSmsgCount(0);
            smsgLimit.setSmsgDate(new Date());

            int countRow = smsgLimitMapper.insert(smsgLimit);  //插入今日验证码记录

            if(countRow == 1){
                smsgLimitDto = new SmsgLimitDto();
                BeanUtils.copySamePropertyValue(smsgLimit, smsgLimitDto);  //返回新增记录
            }else{
                throw new UserExeption(UserResultEnums.SMSGCODE_ERR);  //验证码获取失败
            }
        }

        return smsgLimitDto;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.READ_COMMITTED)
    public void addSmsgCount(String mobilephone) {
        smsgLimitMapper.addSmsgCount(mobilephone);

    }

}
