package com.bigbird.blog.service.impl;

import com.bigbird.blog.common.RoleConstants;
import com.bigbird.blog.entity.User;
import com.bigbird.blog.mapper.UserMapper;
import com.bigbird.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    // 简单的Token存储（生产环境建议使用Redis）
    private static final Map<String, Long> TOKEN_CACHE = new ConcurrentHashMap<>();

    @Override
    public User register(String username, String password, String nickname) {
        // 检查用户名是否已存在
        User existUser = userMapper.selectByUsername(username);
        if (existUser != null) {
            throw new RuntimeException("用户名已存在");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(encryptPassword(password));
        user.setNickname(nickname != null ? nickname : username);
        user.setRole(RoleConstants.USER);
        user.setStatus(1);
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());

        userMapper.insert(user);
        return user;
    }

    @Override
    public String login(String username, String password) {
        User user = userMapper.selectByUsername(username);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        if (!user.getPassword().equals(encryptPassword(password))) {
            throw new RuntimeException("密码错误");
        }
        if (user.getStatus() != 1) {
            throw new RuntimeException("账号已被禁用");
        }

        // 生成Token
        String token = UUID.randomUUID().toString().replace("-", "");
        TOKEN_CACHE.put(token, user.getId());
        return token;
    }

    @Override
    public User getById(Long id) {
        return userMapper.selectById(id);
    }

    @Override
    public User getByUsername(String username) {
        return userMapper.selectByUsername(username);
    }

    @Override
    public User getByToken(String token) {
        if (token == null || token.isEmpty()) {
            return null;
        }
        Long userId = TOKEN_CACHE.get(token);
        if (userId == null) {
            return null;
        }
        return userMapper.selectById(userId);
    }

    @Override
    public Map<String, Object> getUserPage(int page, int size, String keyword) {
        int offset = (page - 1) * size;
        List<User> records = userMapper.selectPage(offset, size, keyword);
        long total = userMapper.selectCount(keyword);

        // 隐藏密码
        records.forEach(user -> user.setPassword(null));

        Map<String, Object> result = new HashMap<>();
        result.put("records", records);
        result.put("total", total);
        result.put("current", page);
        result.put("size", size);
        result.put("pages", (total + size - 1) / size);
        return result;
    }

    @Override
    public boolean updateById(User user) {
        user.setUpdateTime(LocalDateTime.now());
        return userMapper.updateById(user) > 0;
    }

    @Override
    public boolean removeById(Long id) {
        return userMapper.deleteById(id) > 0;
    }

    @Override
    public User createAdmin(String username, String password, String nickname) {
        // 检查用户名是否已存在
        User existUser = userMapper.selectByUsername(username);
        if (existUser != null) {
            throw new RuntimeException("用户名已存在");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(encryptPassword(password));
        user.setNickname(nickname != null ? nickname : username);
        user.setRole(RoleConstants.ADMIN);
        user.setStatus(1);
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());

        userMapper.insert(user);
        return user;
    }

    @Override
    public boolean updateUserRole(Long userId, String role) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        user.setRole(role);
        user.setUpdateTime(LocalDateTime.now());
        return userMapper.updateById(user) > 0;
    }

    @Override
    public boolean updateUserStatus(Long userId, Integer status) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        user.setStatus(status);
        user.setUpdateTime(LocalDateTime.now());
        return userMapper.updateById(user) > 0;
    }

    /**
     * 密码加密（MD5）
     */
    private String encryptPassword(String password) {
        return DigestUtils.md5DigestAsHex(("bigbird_" + password).getBytes(StandardCharsets.UTF_8));
    }
}
