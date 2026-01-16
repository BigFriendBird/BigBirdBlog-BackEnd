package com.bigbird.blog.mapper;

import com.bigbird.blog.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {

    User selectById(Long id);

    User selectByUsername(String username);

    List<User> selectPage(@Param("offset") int offset, @Param("size") int size, @Param("keyword") String keyword);

    long selectCount(@Param("keyword") String keyword);

    int insert(User user);

    int updateById(User user);

    int deleteById(Long id);

    List<User> selectAll();
}
