package com.bigbird.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bigbird.blog.entity.Article;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ArticleMapper extends BaseMapper<Article> {
}
