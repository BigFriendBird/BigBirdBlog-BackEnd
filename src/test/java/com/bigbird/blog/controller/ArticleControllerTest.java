package com.bigbird.blog.controller;

import com.bigbird.blog.entity.Article;
import com.bigbird.blog.service.ArticleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ArticleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ArticleService articleService;

    @Test
    void testGetLatestArticles() throws Exception {
        mockMvc.perform(get("/articles/latest")
                        .param("limit", "3"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    void testGetArticlePage() throws Exception {
        mockMvc.perform(get("/articles")
                        .param("page", "1")
                        .param("size", "10"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").exists());
    }

    @Test
    void testGetArticlePageWithKeyword() throws Exception {
        mockMvc.perform(get("/articles")
                        .param("page", "1")
                        .param("size", "10")
                        .param("keyword", "测试"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }

    @Test
    void testCreateArticle() throws Exception {
        Article article = new Article();
        article.setTitle("Controller测试创建文章");
        article.setSummary("通过API创建的文章摘要");
        article.setContent("通过API创建的文章内容");
        article.setAuthor("BigBird");
        article.setTags("API测试");

        mockMvc.perform(post("/articles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(article)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.id").exists())
                .andExpect(jsonPath("$.data.title").value("Controller测试创建文章"));
    }

    @Test
    void testGetArticleDetail() throws Exception {
        // 先创建一篇文章
        Article article = createTestArticle();
        articleService.save(article);

        mockMvc.perform(get("/articles/{id}", article.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.id").value(article.getId()));
    }

    @Test
    void testGetArticleDetailNotFound() throws Exception {
        mockMvc.perform(get("/articles/{id}", 999999L))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.message").value("文章不存在"));
    }

    @Test
    void testUpdateArticle() throws Exception {
        // 先创建一篇文章
        Article article = createTestArticle();
        articleService.save(article);

        // 更新文章内容
        article.setTitle("Controller更新后的标题");
        article.setContent("Controller更新后的内容");

        mockMvc.perform(put("/articles/{id}", article.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(article)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));

        // 验证更新结果
        Article updated = articleService.getById(article.getId());
        System.out.println("更新后的标题: " + updated.getTitle());
    }

    @Test
    void testDeleteArticle() throws Exception {
        // 先创建一篇文章
        Article article = createTestArticle();
        articleService.save(article);

        mockMvc.perform(delete("/articles/{id}", article.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));

        // 验证删除结果
        Article deleted = articleService.getById(article.getId());
        System.out.println("删除后查询结果: " + deleted);
    }

    @Test
    void testFullCRUDFlow() throws Exception {
        // 1. 创建文章
        Article article = new Article();
        article.setTitle("CRUD流程测试文章");
        article.setSummary("完整CRUD流程测试");
        article.setContent("测试创建、读取、更新、删除完整流程");
        article.setAuthor("BigBird");
        article.setTags("CRUD,测试");

        MvcResult createResult = mockMvc.perform(post("/articles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(article)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andReturn();

        String createResponse = createResult.getResponse().getContentAsString();
        System.out.println("1. 创建文章响应: " + createResponse);

        // 从响应中提取ID
        Long articleId = objectMapper.readTree(createResponse).path("data").path("id").asLong();

        // 2. 读取文章
        mockMvc.perform(get("/articles/{id}", articleId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.title").value("CRUD流程测试文章"));
        System.out.println("2. 读取文章成功");

        // 3. 更新文章
        article.setId(articleId);
        article.setTitle("CRUD流程测试文章-已更新");
        mockMvc.perform(put("/articles/{id}", articleId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(article)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
        System.out.println("3. 更新文章成功");

        // 4. 删除文章
        mockMvc.perform(delete("/articles/{id}", articleId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
        System.out.println("4. 删除文章成功");

        // 5. 验证删除
        mockMvc.perform(get("/articles/{id}", articleId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500));
        System.out.println("5. 验证删除成功 - 文章已不存在");
    }

    private Article createTestArticle() {
        Article article = new Article();
        article.setTitle("Controller测试文章 - " + System.currentTimeMillis());
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
