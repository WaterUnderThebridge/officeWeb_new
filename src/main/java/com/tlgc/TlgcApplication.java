package com.tlgc;


import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.tlgc.Convertor.MJFastJsonHttpMessageConverter;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.support.ErrorPageFilter;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication
@MapperScan(basePackages = {"com.tlgc.mapper"}) //MyBaitis持久化类
public class TlgcApplication extends SpringBootServletInitializer {
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(TlgcApplication.class);
	}
	public static void main(String[] args)

	{

		SpringApplication.run(TlgcApplication.class, args);
	}


	@Bean
	public HttpMessageConverters fastJsonHttpMessageConverters(){
		//创建FastJson信息转换对象
		FastJsonHttpMessageConverter fastJsonHttpMessageConverter = new MJFastJsonHttpMessageConverter();

		return new HttpMessageConverters(fastJsonHttpMessageConverter);
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
