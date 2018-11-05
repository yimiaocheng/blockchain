package com.cwt.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "app_version")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Version extends BaseModel {
    @Id
    @Column(name = "id")
    private Integer id;
    @Column(name = "version")
    private String version;
    @Column(name = "apk_url")
    private String apkUrl;
    @Column(name = "ios_id")
    private String iosId;
    @Column(name = "remark")
    private String remark;
    @Column(name = "compel")
    private Integer compel;
    @Column(name = "system_type")
    private String systemType;
    @Column(name = "create_time")
    private Date createTime;
}
