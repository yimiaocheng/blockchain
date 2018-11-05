package com.cwt.domain.dto.wallet;

import com.cwt.domain.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

//转账记录Dto
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetBillDto extends BaseDto {
    private String userName;
    private BigDecimal amount;
    private Date createTime;
}
