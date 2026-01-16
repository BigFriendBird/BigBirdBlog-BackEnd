package com.bigbird.blog.controller;

import com.bigbird.blog.common.RequireAdmin;
import com.bigbird.blog.common.Result;
import com.bigbird.blog.entity.Article;
import com.bigbird.blog.service.ArticleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/articles")
@CrossOrigin
@Tag(name = "文章管理", description = "文章的增删改查接口")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    /**
     * 获取最新文章
     */
    @GetMapping("/latest")
    @Operation(summary = "获取最新文章", description = "获取最新发布的文章列表")
    public Result<List<Article>> getLatestArticles(
            @Parameter(description = "获取数量") @RequestParam(defaultValue = "3") int limit) {
        List<Article> articles = articleService.getLatestArticles(limit);
        return Result.success(articles);
    }

    /**
     * 分页查询文章
     */
    @GetMapping
    @Operation(summary = "分页查询文章", description = "支持关键词搜索")
    public Result<Map<String, Object>> getArticlePage(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "搜索关键词") @RequestParam(required = false) String keyword) {
        Map<String, Object> articlePage = articleService.getArticlePage(page, size, keyword);
        return Result.success(articlePage);
    }

    /**
     * 获取文章详情
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取文章详情", description = "获取文章详情，同时增加浏览量")
    public Result<Article> getArticleDetail(@Parameter(description = "文章ID") @PathVariable Long id) {
        Article article = articleService.getArticleDetail(id);
        if (article == null) {
            return Result.error("文章不存在");
        }
        return Result.success(article);
    }

    /**
     * 创建文章（管理员）
     */
    @PostMapping("/createArticle")
    @RequireAdmin
    @Operation(summary = "创建文章", description = "需要管理员权限")
    public Result<Article> createArticle(@RequestBody Article article) {
        article.setViews(0);
        articleService.save(article);
        return Result.success(article);
    }

    /**
     * 更新文章（管理员）
     */
    @PutMapping("/{id}")
    @RequireAdmin
    @Operation(summary = "更新文章", description = "需要管理员权限")
    public Result<Void> updateArticle(@Parameter(description = "文章ID") @PathVariable Long id, @RequestBody Article article) {
        article.setId(id);
        articleService.updateById(article);
        return Result.success();
    }

    /**
     * 删除文章（管理员）
     */
    @DeleteMapping("/{id}")
    @RequireAdmin
    @Operation(summary = "删除文章", description = "需要管理员权限")
    public Result<Void> deleteArticle(@Parameter(description = "文章ID") @PathVariable Long id) {
        articleService.removeById(id);
        return Result.success();
    }
}
