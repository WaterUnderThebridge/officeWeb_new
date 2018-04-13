package com.tlgc.mapper;

import com.tlgc.entity.News;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by TONY on 2018/4/4.
 */
public interface NewsMapper {
    public List<News> getNews(@Param("type")Integer type, @Param("LanguageType")Integer LanguageType);
}
