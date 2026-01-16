package com.bigbird.blog.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Comment {

    private Long id;

    /**
     * 文章ID
     */
    private Long articleId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 父评论ID（用于回复功能）
     */
    private Long parentId;

    /**
     * 状态：0-待审核，1-已通过，2-已拒绝
     */
    private Integer status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Integer deleted;

    // 非数据库字段，用于返回前端
    /**
     * 评论用户昵称
     */
    private transient String nickname;

    /**
     * 评论用户头像
     */
    private transient String avatar;
}
