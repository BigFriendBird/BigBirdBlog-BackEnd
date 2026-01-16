package com.bigbird.blog.service;

import com.bigbird.blog.entity.Article;

import java.util.List;
import java.util.Map;

public interface ArticleService {

    /**
     * 获取最新文章
     */
    List<Article> getLatestArticles(int limit);

    /**
     * 分页查询文章
     */
    Map<String, Object> getArticlePage(int page, int size, String keyword);

    /**
     * 获取文章详情（增加浏览量）
     */
    Article getArticleDetail(Long id);

    /**
     * 根据ID获取文章
     */
    Article getById(Long id);

    /**
     * 保存文章
     */
    boolean save(Article article);

    /**
     * 更新文章
     */
    boolean updateById(Article article);

    /**
     * 删除文章
     */
    boolean removeById(Long id);

    /**
     * 获取所有文章
     */
    List<Article> list();
}
