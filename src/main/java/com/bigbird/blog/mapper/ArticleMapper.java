package com.bigbird.blog.mapper;

import com.bigbird.blog.entity.Article;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ArticleMapper {

    @Select("SELECT * FROM article WHERE deleted = 0 ORDER BY create_time DESC LIMIT #{limit}")
    List<Article> selectLatestArticles(int limit);

    @Select("<script>" +
            "SELECT * FROM article WHERE deleted = 0 " +
            "<if test='keyword != null and keyword != \"\"'>" +
            "AND (title LIKE CONCAT('%', #{keyword}, '%') OR summary LIKE CONCAT('%', #{keyword}, '%')) " +
            "</if>" +
            "ORDER BY create_time DESC LIMIT #{offset}, #{size}" +
            "</script>")
    List<Article> selectPage(@Param("offset") int offset, @Param("size") int size, @Param("keyword") String keyword);

    @Select("<script>" +
            "SELECT COUNT(*) FROM article WHERE deleted = 0 " +
            "<if test='keyword != null and keyword != \"\"'>" +
            "AND (title LIKE CONCAT('%', #{keyword}, '%') OR summary LIKE CONCAT('%', #{keyword}, '%')) " +
            "</if>" +
            "</script>")
    long selectCount(@Param("keyword") String keyword);

    @Select("SELECT * FROM article WHERE id = #{id} AND deleted = 0")
    Article selectById(Long id);

    @Insert("INSERT INTO article (title, summary, content, author, tags, views, create_time, update_time, deleted) " +
            "VALUES (#{title}, #{summary}, #{content}, #{author}, #{tags}, #{views}, #{createTime}, #{updateTime}, 0)")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Article article);

    @Update("UPDATE article SET title = #{title}, summary = #{summary}, content = #{content}, " +
            "author = #{author}, tags = #{tags}, views = #{views}, update_time = #{updateTime} WHERE id = #{id}")
    int updateById(Article article);

    @Update("UPDATE article SET deleted = 1 WHERE id = #{id}")
    int deleteById(Long id);

    @Select("SELECT * FROM article WHERE deleted = 0")
    List<Article> selectAll();
}
