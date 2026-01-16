package com.bigbird.blog.service;

import com.bigbird.blog.entity.Comment;

import java.util.List;
import java.util.Map;

public interface CommentService {

    /**
     * 获取文章评论（已审核通过的）
     */
    List<Comment> getArticleComments(Long articleId);

    /**
     * 分页查询评论（管理员）
     */
    Map<String, Object> getCommentPage(int page, int size, Long articleId, Integer status);

    /**
     * 发表评论
     */
    Comment addComment(Long articleId, String content, Long parentId);

    /**
     * 审核评论（管理员）
     */
    boolean auditComment(Long commentId, Integer status);

    /**
     * 删除评论（管理员）
     */
    boolean removeById(Long id);

    /**
     * 获取评论详情
     */
    Comment getById(Long id);
}
