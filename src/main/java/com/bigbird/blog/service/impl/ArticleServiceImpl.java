package com.bigbird.blog.service.impl;

import com.bigbird.blog.entity.Article;
import com.bigbird.blog.mapper.ArticleMapper;
import com.bigbird.blog.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;

    @Override
    public List<Article> getLatestArticles(int limit) {
        return articleMapper.selectLatestArticles(limit);
    }

    @Override
    public Map<String, Object> getArticlePage(int page, int size, String keyword) {
        int offset = (page - 1) * size;
        List<Article> records = articleMapper.selectPage(offset, size, keyword);
        long total = articleMapper.selectCount(keyword);

        Map<String, Object> result = new HashMap<>();
        result.put("records", records);
        result.put("total", total);
        result.put("current", page);
        result.put("size", size);
        result.put("pages", (total + size - 1) / size);
        return result;
    }

    @Override
    public Article getArticleDetail(Long id) {
        Article article = articleMapper.selectById(id);
        if (article != null) {
            // 增加浏览量
            article.setViews(article.getViews() + 1);
            article.setUpdateTime(LocalDateTime.now());
            articleMapper.updateById(article);
        }
        return article;
    }

    @Override
    public Article getById(Long id) {
        return articleMapper.selectById(id);
    }

    @Override
    public boolean save(Article article) {
        article.setCreateTime(LocalDateTime.now());
        article.setUpdateTime(LocalDateTime.now());
        if (article.getViews() == null) {
            article.setViews(0);
        }
        return articleMapper.insert(article) > 0;
    }

    @Override
    public boolean updateById(Article article) {
        article.setUpdateTime(LocalDateTime.now());
        return articleMapper.updateById(article) > 0;
    }

    @Override
    public boolean removeById(Long id) {
        return articleMapper.deleteById(id) > 0;
    }

    @Override
    public List<Article> list() {
        return articleMapper.selectAll();
    }
}
