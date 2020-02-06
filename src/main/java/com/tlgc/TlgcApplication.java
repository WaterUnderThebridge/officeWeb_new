package com.tlgc;




import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;


import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;

import org.springframework.boot.web.servlet.support.ErrorPageFilter;
import org.springframework.context.annotation.Bean;




@EnableAutoConfiguration(exclude={
		JpaRepositoriesAutoConfiguration.class//禁止springboot自动加载持久化bean,解决多数据源报错
})
@SpringBootApplication
public class TlgcApplication  {

	public static void main(String[] args)

	{
		SpringApplication.run(TlgcApplication.class, args);
	}




	//启动类中加如下代码，成功解决问题
	@Bean
	public ErrorPageFilter errorPageFilter() {
		return new ErrorPageFilter();
	}
	@Bean
	public FilterRegistrationBean disableSpringBootErrorFilter(ErrorPageFilter filter) {
		FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
		filterRegistrationBean.setFilter(filter);
		filterRegistrationBean.setEnabled(false);
		return filterRegistrationBean;
	}
//	//跨域
//	@Bean
//	public WebMvcConfigurer webMvcConfigurer() {
//		return new WebMvcConfigurerAdapter() {
//			@Override
//			public void addCorsMappings(CorsRegistry registry) {
//				registry.addMapping("/**").allowedOrigins("*");
//				registry.addMapping("/**").allowedHeaders("*");
//				registry.addMapping("/**").allowedMethods("*");
//			}
//		};
//	}
}
