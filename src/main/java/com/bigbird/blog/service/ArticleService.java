package com.bigbird.blog.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bigbird.blog.entity.Article;

import java.util.List;

public interface ArticleService extends IService<Article> {

    /**
     * 获取最新文章
     */
    List<Article> getLatestArticles(int limit);

    /**
     * 分页查询文章
     */
    Page<Article> getArticlePage(int page, int size, String keyword);

    /**
     * 获取文章详情（增加浏览量）
     */
    Article getArticleDetail(Long id);
}
