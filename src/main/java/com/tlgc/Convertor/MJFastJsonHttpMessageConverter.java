package com.tlgc.Convertor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONPObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;


import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageNotWritableException;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * @author 郑明亮
 * @time 2017年6月1日 上午11:51:57
 * @description <p>自定义转换器，拼接jsonp格式数据 </p>
 * @modifyBy
 * @modifyTime
 * @modifyDescription<p> </p>
 */
public class MJFastJsonHttpMessageConverter extends FastJsonHttpMessageConverter {
    public static final Charset UTF8 = Charset.forName("UTF-8");
    private Charset charset;
    private SerializerFeature[] features;

    public MJFastJsonHttpMessageConverter() {
        super();
        this.charset = UTF8;
        this.features = new SerializerFeature[0];
    }

    @Override
    protected void writeInternal(Object obj, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        // obj就是controller中注解为@ResponseBody的方法返回值对象
        if(obj instanceof JSONPObject){
            JSONPObject jsonpObject = (JSONPObject)obj;
            OutputStream out = outputMessage.getBody();
            String text = JSON.toJSONString(jsonpObject.getParameters(), this.features);
            String jsonpText = new StringBuilder(jsonpObject.getFunction()).append("(").append(text).append(")").toString();
            byte[] bytes = jsonpText.getBytes(this.charset);
            out.write(bytes);
        }else{
            super.writeInternal(obj, outputMessage);
        }
    }
}