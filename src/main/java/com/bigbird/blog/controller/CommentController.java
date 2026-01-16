package com.bigbird.blog.controller;

import com.bigbird.blog.common.RequireAdmin;
import com.bigbird.blog.common.RequireLogin;
import com.bigbird.blog.common.Result;
import com.bigbird.blog.entity.Comment;
import com.bigbird.blog.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/comments")
@CrossOrigin
@Tag(name = "评论管理", description = "评论的发表、查看、审核等接口")
public class CommentController {

    @Autowired
    private CommentService commentService;

    /**
     * 获取文章评论（公开，只返回已审核的）
     */
    @GetMapping("/article/{articleId}")
    @Operation(summary = "获取文章评论", description = "公开接口，获取指定文章的已审核评论")
    public Result<List<Comment>> getArticleComments(@Parameter(description = "文章ID") @PathVariable Long articleId) {
        List<Comment> comments = commentService.getArticleComments(articleId);
        return Result.success(comments);
    }

    /**
     * 发表评论（需要登录）
     */
    @PostMapping
    @RequireLogin
    @Operation(summary = "发表评论", description = "需要登录，发表评论后需要管理员审核")
    public Result<Comment> addComment(@RequestBody Map<String, Object> params) {
        Long articleId = Long.valueOf(params.get("articleId").toString());
        String content = (String) params.get("content");
        Long parentId = params.get("parentId") != null ? Long.valueOf(params.get("parentId").toString()) : null;

        if (content == null || content.trim().isEmpty()) {
            return Result.error("评论内容不能为空");
        }

        try {
            Comment comment = commentService.addComment(articleId, content, parentId);
            return Result.success(comment);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 分页查询评论列表（管理员）
     */
    @GetMapping("/admin/list")
    @RequireAdmin
    @Operation(summary = "分页查询评论列表", description = "需要管理员权限")
    public Result<Map<String, Object>> getCommentList(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "文章ID") @RequestParam(required = false) Long articleId,
            @Parameter(description = "状态：0-待审核，1-已通过，2-已拒绝") @RequestParam(required = false) Integer status) {
        Map<String, Object> commentPage = commentService.getCommentPage(page, size, articleId, status);
        return Result.success(commentPage);
    }

    /**
     * 审核评论（管理员）
     * status: 1-通过，2-拒绝
     */
    @PutMapping("/{id}/audit")
    @RequireAdmin
    @Operation(summary = "审核评论", description = "需要管理员权限，status: 1-通过，2-拒绝")
    public Result<Void> auditComment(@Parameter(description = "评论ID") @PathVariable Long id, @RequestBody Map<String, Integer> params) {
        Integer status = params.get("status");
        if (status == null || (status != 1 && status != 2)) {
            return Result.error("状态值无效，1-通过，2-拒绝");
        }
        commentService.auditComment(id, status);
        return Result.success();
    }

    /**
     * 删除评论（管理员）
     */
    @DeleteMapping("/{id}")
    @RequireAdmin
    @Operation(summary = "删除评论", description = "需要管理员权限")
    public Result<Void> deleteComment(@Parameter(description = "评论ID") @PathVariable Long id) {
        commentService.removeById(id);
        return Result.success();
    }
}
