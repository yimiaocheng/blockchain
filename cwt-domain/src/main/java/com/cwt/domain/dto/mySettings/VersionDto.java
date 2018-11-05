package com.cwt.domain.dto.mySettings;

import com.cwt.domain.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VersionDto extends BaseDto {
    private Integer id;
    private String version;
    private String apkUrl;
    private String iosId;
    private String remark;
    private Integer compel;
    private String systemType;
    private Date createTime;
}
