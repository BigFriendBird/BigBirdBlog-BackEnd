package com.bigbird.blog.mapper;

import com.bigbird.blog.entity.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CommentMapper {

    Comment selectById(Long id);

    List<Comment> selectByArticleId(@Param("articleId") Long articleId, @Param("status") Integer status);

    List<Comment> selectPage(@Param("offset") int offset, @Param("size") int size,
                             @Param("articleId") Long articleId, @Param("status") Integer status);

    long selectCount(@Param("articleId") Long articleId, @Param("status") Integer status);

    int insert(Comment comment);

    int updateById(Comment comment);

    int updateStatus(@Param("id") Long id, @Param("status") Integer status);

    int deleteById(Long id);

    List<Comment> selectAll();
}
