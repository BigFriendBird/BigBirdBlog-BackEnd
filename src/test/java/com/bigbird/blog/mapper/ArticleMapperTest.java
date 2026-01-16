package com.bigbird.blog.mapper;

import com.bigbird.blog.entity.Article;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ArticleMapperTest {

    @Autowired
    private ArticleMapper articleMapper;

    @Test
    void testInsertArticle() {
        Article article = new Article();
        article.setTitle("测试文章标题");
        article.setSummary("这是测试文章的摘要");
        article.setContent("这是测试文章的详细内容，包含很多文字...");
        article.setAuthor("BigBird");
        article.setTags("测试,Java,Spring");
        article.setViews(0);
        article.setCreateTime(LocalDateTime.now());
        article.setUpdateTime(LocalDateTime.now());

        int rows = articleMapper.insert(article);

        assertEquals(1, rows);
        assertNotNull(article.getId());
        System.out.println("新插入的文章ID: " + article.getId());
    }

    @Test
    void testSelectById() {
        // 先插入一条测试数据
        Article article = createTestArticle();
        articleMapper.insert(article);

        // 查询
        Article found = articleMapper.selectById(article.getId());

        assertNotNull(found);
        assertEquals(article.getTitle(), found.getTitle());
        assertEquals(article.getAuthor(), found.getAuthor());
        System.out.println("查询到的文章: " + found);
    }

    @Test
    void testSelectLatestArticles() {
        // 插入几条测试数据
        for (int i = 1; i <= 3; i++) {
            Article article = new Article();
            article.setTitle("最新文章测试 " + i);
            article.setSummary("摘要 " + i);
            article.setContent("内容 " + i);
            article.setAuthor("BigBird");
            article.setTags("测试");
            article.setViews(0);
            article.setCreateTime(LocalDateTime.now());
            article.setUpdateTime(LocalDateTime.now());
            articleMapper.insert(article);
        }

        // 查询最新3篇文章
        List<Article> articles = articleMapper.selectLatestArticles(3);

        assertNotNull(articles);
        assertTrue(articles.size() <= 3);
        System.out.println("最新文章数量: " + articles.size());
        articles.forEach(a -> System.out.println(" - " + a.getTitle()));
    }

    @Test
    void testSelectPage() {
        // 分页查询第一页，每页5条
        List<Article> articles = articleMapper.selectPage(0, 5, null);

        assertNotNull(articles);
        System.out.println("分页查询结果数量: " + articles.size());
        articles.forEach(a -> System.out.println(" - " + a.getTitle()));
    }

    @Test
    void testSelectPageWithKeyword() {
        // 先插入一条带关键字的文章
        Article article = new Article();
        article.setTitle("Spring Boot 入门教程");
        article.setSummary("学习 Spring Boot 开发");
        article.setContent("详细的 Spring Boot 教程内容...");
        article.setAuthor("BigBird");
        article.setTags("Spring,教程");
        article.setViews(0);
        article.setCreateTime(LocalDateTime.now());
        article.setUpdateTime(LocalDateTime.now());
        articleMapper.insert(article);

        // 按关键字搜索
        List<Article> articles = articleMapper.selectPage(0, 10, "Spring");

        assertNotNull(articles);
        assertTrue(articles.size() > 0);
        System.out.println("搜索 'Spring' 结果数量: " + articles.size());
    }

    @Test
    void testSelectCount() {
        long count = articleMapper.selectCount(null);
        System.out.println("文章总数: " + count);
        assertTrue(count >= 0);
    }

    @Test
    void testUpdateById() {
        // 先插入一条测试数据
        Article article = createTestArticle();
        articleMapper.insert(article);

        // 更新
        article.setTitle("更新后的标题");
        article.setContent("更新后的内容");
        article.setUpdateTime(LocalDateTime.now());
        int rows = articleMapper.updateById(article);

        assertEquals(1, rows);

        // 验证更新结果
        Article updated = articleMapper.selectById(article.getId());
        assertEquals("更新后的标题", updated.getTitle());
        System.out.println("文章更新成功: " + updated.getTitle());
    }

    @Test
    void testDeleteById() {
        // 先插入一条测试数据
        Article article = createTestArticle();
        articleMapper.insert(article);

        // 软删除
        int rows = articleMapper.deleteById(article.getId());
        assertEquals(1, rows);

        // 验证删除后查询不到
        Article deleted = articleMapper.selectById(article.getId());
        assertNull(deleted);
        System.out.println("文章软删除成功");
    }

    @Test
    void testSelectAll() {
        List<Article> articles = articleMapper.selectAll();
        assertNotNull(articles);
        System.out.println("所有文章数量: " + articles.size());
    }

    private Article createTestArticle() {
        Article article = new Article();
        article.setTitle("测试文章 - " + System.currentTimeMillis());
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
