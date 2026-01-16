package com.bigbird.blog.service.impl;

import com.bigbird.blog.common.UserContext;
import com.bigbird.blog.entity.Comment;
import com.bigbird.blog.mapper.CommentMapper;
import com.bigbird.blog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Override
    public List<Comment> getArticleComments(Long articleId) {
        // 只返回已审核通过的评论（status=1）
        return commentMapper.selectByArticleId(articleId, 1);
    }

    @Override
    public Map<String, Object> getCommentPage(int page, int size, Long articleId, Integer status) {
        int offset = (page - 1) * size;
        List<Comment> records = commentMapper.selectPage(offset, size, articleId, status);
        long total = commentMapper.selectCount(articleId, status);

        Map<String, Object> result = new HashMap<>();
        result.put("records", records);
        result.put("total", total);
        result.put("current", page);
        result.put("size", size);
        result.put("pages", (total + size - 1) / size);
        return result;
    }

    @Override
    public Comment addComment(Long articleId, String content, Long parentId) {
        Long userId = UserContext.getCurrentUserId();
        if (userId == null) {
            throw new RuntimeException("请先登录");
        }

        Comment comment = new Comment();
        comment.setArticleId(articleId);
        comment.setUserId(userId);
        comment.setContent(content);
        comment.setParentId(parentId);
        //TODO 提供审核评论的方式
        comment.setStatus(1); // 待审核
        comment.setCreateTime(LocalDateTime.now());
        comment.setUpdateTime(LocalDateTime.now());

        commentMapper.insert(comment);
        return comment;
    }

    @Override
    public boolean auditComment(Long commentId, Integer status) {
        return commentMapper.updateStatus(commentId, status) > 0;
    }

    @Override
    public boolean removeById(Long id) {
        return commentMapper.deleteById(id) > 0;
    }

    @Override
    public Comment getById(Long id) {
        return commentMapper.selectById(id);
    }
}
