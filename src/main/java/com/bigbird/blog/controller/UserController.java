package com.bigbird.blog.controller;

import com.bigbird.blog.common.RequireAdmin;
import com.bigbird.blog.common.RequireLogin;
import com.bigbird.blog.common.Result;
import com.bigbird.blog.common.UserContext;
import com.bigbird.blog.entity.User;
import com.bigbird.blog.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
@CrossOrigin
@Tag(name = "用户管理", description = "用户注册、登录、信息管理等接口")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用户注册
     */
    @PostMapping("/register")
    @Operation(summary = "用户注册", description = "注册新用户，默认为普通用户角色")
    public Result<User> register(@RequestBody Map<String, String> params) {
        String username = params.get("username");
        String password = params.get("password");
        String nickname = params.get("nickname");

        if (username == null || username.isEmpty()) {
            return Result.error("用户名不能为空");
        }
        if (password == null || password.isEmpty()) {
            return Result.error("密码不能为空");
        }

        try {
            User user = userService.register(username, password, nickname);
            user.setPassword(null); // 不返回密码
            return Result.success(user);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 用户登录
     */
    @PostMapping("/login")
    @Operation(summary = "用户登录", description = "用户登录，返回 Token 和用户信息")
    public Result<Map<String, Object>> login(@RequestBody Map<String, String> params) {
        String username = params.get("username");
        String password = params.get("password");

        if (username == null || username.isEmpty()) {
            return Result.error("用户名不能为空");
        }
        if (password == null || password.isEmpty()) {
            return Result.error("密码不能为空");
        }

        try {
            String token = userService.login(username, password);
            User user = userService.getByUsername(username);
            user.setPassword(null);

            Map<String, Object> data = new HashMap<>();
            data.put("token", token);
            data.put("user", user);
            return Result.success(data);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取当前用户信息
     */
    @GetMapping("/info")
    @RequireLogin
    @Operation(summary = "获取当前用户信息", description = "需要登录，返回当前登录用户的信息")
    public Result<User> getUserInfo() {
        User user = UserContext.getCurrentUser();
        if (user != null) {
            user.setPassword(null);
        }
        return Result.success(user);
    }

    /**
     * 更新当前用户信息
     */
    @PutMapping("/info")
    @RequireLogin
    @Operation(summary = "更新当前用户信息", description = "需要登录，更新当前用户的昵称、邮箱等信息")
    public Result<Void> updateUserInfo(@RequestBody User user) {
        User currentUser = UserContext.getCurrentUser();
        user.setId(currentUser.getId());
        // 普通用户不能修改自己的角色和状态
        user.setRole(null);
        user.setStatus(null);
        userService.updateById(user);
        return Result.success();
    }

    /**
     * 分页查询用户列表（管理员）
     */
    @GetMapping("/list")
    @RequireAdmin
    @Operation(summary = "分页查询用户列表", description = "需要管理员权限")
    public Result<Map<String, Object>> getUserList(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "搜索关键词") @RequestParam(required = false) String keyword) {
        Map<String, Object> userPage = userService.getUserPage(page, size, keyword);
        return Result.success(userPage);
    }

    /**
     * 创建管理员用户（仅管理员可操作）
     */
    @PostMapping("/admin")
    @RequireAdmin
    @Operation(summary = "创建管理员用户", description = "需要管理员权限")
    public Result<User> createAdmin(@RequestBody Map<String, String> params) {
        String username = params.get("username");
        String password = params.get("password");
        String nickname = params.get("nickname");

        if (username == null || username.isEmpty()) {
            return Result.error("用户名不能为空");
        }
        if (password == null || password.isEmpty()) {
            return Result.error("密码不能为空");
        }

        try {
            User user = userService.createAdmin(username, password, nickname);
            user.setPassword(null);
            return Result.success(user);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 修改用户角色（管理员）
     */
    @PutMapping("/{id}/role")
    @RequireAdmin
    @Operation(summary = "修改用户角色", description = "需要管理员权限，role: ADMIN/USER")
    public Result<Void> updateUserRole(@Parameter(description = "用户ID") @PathVariable Long id, @RequestBody Map<String, String> params) {
        String role = params.get("role");
        if (role == null || role.isEmpty()) {
            return Result.error("角色不能为空");
        }
        try {
            userService.updateUserRole(id, role);
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 修改用户状态（管理员）
     */
    @PutMapping("/{id}/status")
    @RequireAdmin
    @Operation(summary = "修改用户状态", description = "需要管理员权限，status: 0-禁用，1-启用")
    public Result<Void> updateUserStatus(@Parameter(description = "用户ID") @PathVariable Long id, @RequestBody Map<String, Integer> params) {
        Integer status = params.get("status");
        if (status == null) {
            return Result.error("状态不能为空");
        }
        try {
            userService.updateUserStatus(id, status);
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 删除用户（管理员）
     */
    @DeleteMapping("/{id}")
    @RequireAdmin
    @Operation(summary = "删除用户", description = "需要管理员权限")
    public Result<Void> deleteUser(@Parameter(description = "用户ID") @PathVariable Long id) {
        userService.removeById(id);
        return Result.success();
    }
}
