package com.bigbird.blog.service;

import com.bigbird.blog.entity.User;

import java.util.Map;

public interface UserService {

    /**
     * 用户注册
     */
    User register(String username, String password, String nickname);

    /**
     * 用户登录
     */
    String login(String username, String password);

    /**
     * 根据ID获取用户
     */
    User getById(Long id);

    /**
     * 根据用户名获取用户
     */
    User getByUsername(String username);

    /**
     * 根据Token获取用户
     */
    User getByToken(String token);

    /**
     * 分页查询用户（管理员）
     */
    Map<String, Object> getUserPage(int page, int size, String keyword);

    /**
     * 更新用户信息
     */
    boolean updateById(User user);

    /**
     * 删除用户（管理员）
     */
    boolean removeById(Long id);

    /**
     * 创建管理员用户
     */
    User createAdmin(String username, String password, String nickname);

    /**
     * 修改用户角色（管理员）
     */
    boolean updateUserRole(Long userId, String role);

    /**
     * 修改用户状态（管理员）
     */
    boolean updateUserStatus(Long userId, Integer status);
}
