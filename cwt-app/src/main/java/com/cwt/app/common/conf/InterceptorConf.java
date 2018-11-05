package com.cwt.app.common.conf;

import com.cwt.app.common.interceptor.TokenInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * \* <p>Desciption:</p>
 * \* CreateTime: 2018/8/9 0:55
 * \* User: XianChaoWei
 * \* Version: V1.0
 * \
 */
@Configuration
public class InterceptorConf implements WebMvcConfigurer {

    @Bean
    public TokenInterceptor tokenInterceptor() {
        return new TokenInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tokenInterceptor())
                .addPathPatterns("/**")
                .addPathPatterns("/php/login/checkLogin")
                .excludePathPatterns("/swagger-resources/**")
                .excludePathPatterns("/v2/api-docs/**")
                .excludePathPatterns("/login/**")
                .excludePathPatterns("/version/**")
                .excludePathPatterns("/error")
                .excludePathPatterns("/php/a6shop/**");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("*");
    }

}
