package com.tlgc;


import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.tlgc.Convertor.MJFastJsonHttpMessageConverter;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

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

}
