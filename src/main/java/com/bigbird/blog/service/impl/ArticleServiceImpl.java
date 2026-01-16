package com.bigbird.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bigbird.blog.entity.Article;
import com.bigbird.blog.mapper.ArticleMapper;
import com.bigbird.blog.service.ArticleService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Override
    public List<Article> getLatestArticles(int limit) {
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Article::getCreateTime)
               .last("LIMIT " + limit);
        return list(wrapper);
    }

    @Override
    public Page<Article> getArticlePage(int page, int size, String keyword) {
        Page<Article> articlePage = new Page<>(page, size);
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        
        if (StringUtils.hasText(keyword)) {
            wrapper.like(Article::getTitle, keyword)
                   .or()
                   .like(Article::getSummary, keyword);
        }
        
        wrapper.orderByDesc(Article::getCreateTime);
        return page(articlePage, wrapper);
    }

    @Override
    public Article getArticleDetail(Long id) {
        Article article = getById(id);
        if (article != null) {
            // 增加浏览量
            article.setViews(article.getViews() + 1);
            updateById(article);
        }
        return article;
    }
}
