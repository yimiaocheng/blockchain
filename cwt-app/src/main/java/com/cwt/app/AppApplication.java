package com.cwt.app;

import com.cwt.app.common.handle.HttpServletRequestReplacedFilter;
import com.cwt.persistent.mapper.UserMapper;
import com.cwt.service.service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import tk.mybatis.spring.mapper.MapperScannerConfigurer;

/**
 * 更改项目架构
 * @author huangxl
 * Created on 2018/8/27
 */
@SpringBootApplication(scanBasePackageClasses = {AppApplication.class,UserMapper.class, UserService.class})
@EnableAsync //允许异步调用
public class AppApplication {
    public static void main(String[] args) {
        SpringApplication.run(AppApplication.class);
    }

    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer(){
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        mapperScannerConfigurer.setBasePackage("com.cwt.persistent.mapper");
        return mapperScannerConfigurer;
    }

    @Bean
    public FilterRegistrationBean httpServletRequestReplacedRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new HttpServletRequestReplacedFilter());
        registration.addUrlPatterns("/*");
        registration.addInitParameter("paramName", "paramValue");
        registration.setName("httpServletRequestReplacedFilter");
        registration.setOrder(1);
        return registration;
    }

}
