package com.ipaylinks.jiaqi.serviceencryptor;

import com.ipaylinks.jiaqi.serviceencryptor.controller.MyFilter;
import com.ipaylinks.jiaqi.serviceencryptor.controller.MyHiddenHttpMethodFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.HiddenHttpMethodFilter;

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class,HibernateJpaAutoConfiguration.class})
public class ServiceEncryptorApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceEncryptorApplication.class, args);
    }


    @Bean
    public HiddenHttpMethodFilter hiddenHttpMethodFilter() {
        return new MyHiddenHttpMethodFilter();
    }



//    @Bean
//    public FilterRegistrationBean filterRegistrationBean(){
//        FilterRegistrationBean registration = new FilterRegistrationBean();
//        //添加过滤器
//        registration.setFilter(new MyFilter());
//        //设置过滤路径，/*所有路径
//        registration.addUrlPatterns("/*");
//        //添加默认参数
////        registration.addInitParameter("name", "alue");
//        //设置优先级
//        registration.setName("MyFilter");
//        //设置优先级
//        registration.setOrder(99);
//        return registration;
//    }
}
