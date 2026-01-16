package com.bigbird.blog.mapper;

import com.bigbird.blog.entity.Article;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ArticleMapper {

    List<Article> selectLatestArticles(int limit);

    List<Article> selectPage(@Param("offset") int offset, @Param("size") int size, @Param("keyword") String keyword);

    long selectCount(@Param("keyword") String keyword);

    Article selectById(Long id);

    int insert(Article article);

    int updateById(Article article);

    int deleteById(Long id);

    List<Article> selectAll();
}
