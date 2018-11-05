package com.cwt.service.service.impl;

import com.cwt.common.enums.UserResultEnums;
import com.cwt.common.exception.UserExeption;
import com.cwt.common.util.ExceptionPreconditionUtils;
import com.cwt.common.util.MD5Utils;
import com.cwt.domain.dto.user.UserBankDto;
import com.cwt.domain.entity.User;
import com.cwt.domain.entity.UserBank;
import com.cwt.persistent.mapper.UserBankMapper;
import com.cwt.persistent.mapper.UserMapper;
import com.cwt.service.service.UserBankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;

/**
 * \* <p>Desciption:</p>
 * \* CreateTime: 2018/7/21 10:17
 * \* User: XianChaoWei
 * \* Version: V1.0
 * \
 */
@Service("userBankService")
public class UserBankServiceImpl implements UserBankService {

    @Autowired
    private UserBankMapper userBankMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public UserBankDto getByUserid(String id) {
        //检测id不能为空
        ExceptionPreconditionUtils.checkStringArgumentsNotBlank(id, new UserExeption(UserResultEnums.NULL_ID));

        UserBankDto userBankDto = userBankMapper.selectByUserid(id);

        return userBankDto;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    public UserBankDto saveOrUpdateBankinfo(UserBank userBank) {
        //检测参数不能为空
        ExceptionPreconditionUtils.checkNotNull(userBank, new UserExeption(UserResultEnums.USERBANK_NULL));

        int countRow = 0;  //保存或修改影响条数
        int countU = 0;

        UserBankDto userBankDto = userBankMapper.selectByUserid(userBank.getUserId());  //查询之前是否有银行卡信息
        if(userBankDto == null){  //没有，新增一条
            userBank.setId(MD5Utils.MD5(UUID.randomUUID().toString()));
            userBank.setCreateTime(new Date());

            countRow = userBankMapper.insertSelective(userBank);
        }else{  //有信息，进行更新
            userBank.setId(userBankDto.getId());
            userBank.setModifyTime(new Date());
            //修改用户银行卡信息表
            countRow = userBankMapper.updateByPrimaryKeySelective(userBank);

            //将银行卡号放入用户表
            User user = new User();
            user.setId(userBank.getUserId());
            user.setPaymentMethodBankcard(userBank.getBankNumber());
            countU = userMapper.updateByPrimaryKeySelective(user);
        }

        if(countRow != 1 && countU != 1){
            throw new UserExeption(UserResultEnums.BANK_ERR);
        }

        //返回部分信息
        UserBankDto userBankDtoReturn = new UserBankDto();
        userBankDtoReturn.setBankLocation(userBank.getBankLocation());
        userBankDtoReturn.setBankName(userBank.getBankName());
        userBankDtoReturn.setBankNumber(userBank.getBankNumber());
        userBankDtoReturn.setBranchName(userBank.getBranchName());
        userBankDtoReturn.setUserName(userBank.getUserName());

        return userBankDtoReturn;
    }
}
