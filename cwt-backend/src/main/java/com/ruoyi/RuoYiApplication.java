package com.ruoyi;

import com.cwt.persistent.mapper.UserMapper;
import com.cwt.service.service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * 启动程序
 * 
 * @author ruoyi
 */
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class},scanBasePackageClasses = {RuoYiApplication.class,UserService.class, UserMapper.class})
@MapperScan(value = {"com.ruoyi.project.*.*.mapper","com.cwt.persistent.mapper"})
public class RuoYiApplication
{
    public static void main(String[] args)
    {
        // System.setProperty("spring.devtools.restart.enabled", "false");
        SpringApplication.run(RuoYiApplication.class, args);
        System.out.println("===========启动成功=========");
    }
}