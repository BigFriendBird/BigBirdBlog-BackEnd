package com.bigbird.blog.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bigbird.blog.common.Result;
import com.bigbird.blog.entity.Article;
import com.bigbird.blog.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/articles")
@CrossOrigin
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    /**
     * 获取最新文章
     */
    @GetMapping("/latest")
    public Result<List<Article>> getLatestArticles(@RequestParam(defaultValue = "3") int limit) {
        List<Article> articles = articleService.getLatestArticles(limit);
        return Result.success(articles);
    }

    /**
     * 分页查询文章
     */
    @GetMapping
    public Result<Page<Article>> getArticlePage(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword) {
        Page<Article> articlePage = articleService.getArticlePage(page, size, keyword);
        return Result.success(articlePage);
    }

    /**
     * 获取文章详情
     */
    @GetMapping("/{id}")
    public Result<Article> getArticleDetail(@PathVariable Long id) {
        Article article = articleService.getArticleDetail(id);
        if (article == null) {
            return Result.error("文章不存在");
        }
        return Result.success(article);
    }

    /**
     * 创建文章
     */
    @PostMapping
    public Result<Article> createArticle(@RequestBody Article article) {
        article.setViews(0);
        articleService.save(article);
        return Result.success(article);
    }

    /**
     * 更新文章
     */
    @PutMapping("/{id}")
    public Result<Void> updateArticle(@PathVariable Long id, @RequestBody Article article) {
        article.setId(id);
        articleService.updateById(article);
        return Result.success();
    }

    /**
     * 删除文章
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteArticle(@PathVariable Long id) {
        articleService.removeById(id);
        return Result.success();
    }
}
