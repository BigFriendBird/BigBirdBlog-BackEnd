package com.bigbird.blog.service;

import com.bigbird.blog.entity.Article;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ArticleServiceTest {

    @Autowired
    private ArticleService articleService;

    @Test
    void testSaveArticle() {
        Article article = new Article();
        article.setTitle("Service层测试文章");
        article.setSummary("通过Service层创建的文章");
        article.setContent("这是通过Service层测试创建的文章内容");
        article.setAuthor("BigBird");
        article.setTags("测试,Service");
        article.setViews(0);
        article.setCreateTime(LocalDateTime.now());
        article.setUpdateTime(LocalDateTime.now());

        boolean result = articleService.save(article);

        assertTrue(result);
        assertNotNull(article.getId());
        System.out.println("Service层保存文章成功，ID: " + article.getId());
    }

    @Test
    void testGetById() {
        // 先创建一篇文章
        Article article = createTestArticle();
        articleService.save(article);

        // 通过ID获取
        Article found = articleService.getById(article.getId());

        assertNotNull(found);
        assertEquals(article.getTitle(), found.getTitle());
        System.out.println("获取文章: " + found.getTitle());
    }

    @Test
    void testGetLatestArticles() {
        List<Article> articles = articleService.getLatestArticles(5);

        assertNotNull(articles);
        assertTrue(articles.size() <= 5);
        System.out.println("获取最新文章数量: " + articles.size());
        articles.forEach(a -> System.out.println(" - " + a.getTitle() + " (" + a.getCreateTime() + ")"));
    }

    @Test
    void testGetArticlePage() {
        Map<String, Object> result = articleService.getArticlePage(1, 10, null);

        assertNotNull(result);
        assertTrue(result.containsKey("list") || result.containsKey("records"));
        assertTrue(result.containsKey("total"));
        System.out.println("分页查询结果: " + result);
    }

    @Test
    void testGetArticlePageWithKeyword() {
        // 先插入一篇包含关键字的文章
        Article article = new Article();
        article.setTitle("MyBatis 使用教程");
        article.setSummary("学习 MyBatis 框架");
        article.setContent("MyBatis 是优秀的持久层框架...");
        article.setAuthor("BigBird");
        article.setTags("MyBatis,教程");
        article.setViews(0);
        article.setCreateTime(LocalDateTime.now());
        article.setUpdateTime(LocalDateTime.now());
        articleService.save(article);

        // 按关键字查询
        Map<String, Object> result = articleService.getArticlePage(1, 10, "MyBatis");

        assertNotNull(result);
        System.out.println("按关键字'MyBatis'查询结果: " + result);
    }

    @Test
    void testGetArticleDetail() {
        // 先创建一篇文章
        Article article = createTestArticle();
        article.setViews(10);
        articleService.save(article);

        int originalViews = article.getViews();

        // 获取文章详情（应该会增加浏览量）
        Article detail = articleService.getArticleDetail(article.getId());

        assertNotNull(detail);
        // 检查浏览量是否增加
        System.out.println("原浏览量: " + originalViews + ", 当前浏览量: " + detail.getViews());
    }

    @Test
    void testUpdateById() {
        // 先创建一篇文章
        Article article = createTestArticle();
        articleService.save(article);

        // 更新文章
        article.setTitle("更新后的Service测试标题");
        article.setContent("更新后的内容");
        article.setUpdateTime(LocalDateTime.now());
        boolean result = articleService.updateById(article);

        assertTrue(result);

        // 验证更新
        Article updated = articleService.getById(article.getId());
        assertEquals("更新后的Service测试标题", updated.getTitle());
        System.out.println("文章更新成功: " + updated.getTitle());
    }

    @Test
    void testRemoveById() {
        // 先创建一篇文章
        Article article = createTestArticle();
        articleService.save(article);
        Long id = article.getId();

        // 删除文章
        boolean result = articleService.removeById(id);

        assertTrue(result);

        // 验证删除
        Article deleted = articleService.getById(id);
        assertNull(deleted);
        System.out.println("文章删除成功");
    }

    @Test
    void testList() {
        List<Article> articles = articleService.list();

        assertNotNull(articles);
        System.out.println("所有文章数量: " + articles.size());
    }

    private Article createTestArticle() {
        Article article = new Article();
        article.setTitle("Service测试文章 - " + System.currentTimeMillis());
        article.setSummary("测试摘要");
        article.setContent("测试内容");
        article.setAuthor("BigBird");
        article.setTags("测试");
        article.setViews(0);
        article.setCreateTime(LocalDateTime.now());
        article.setUpdateTime(LocalDateTime.now());
        return article;
    }
}
