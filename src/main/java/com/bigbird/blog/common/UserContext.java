package com.bigbird.blog.common;

import com.bigbird.blog.entity.User;

/**
 * 用户上下文持有器（线程安全）
 */
public class UserContext {

    private static final ThreadLocal<User> USER_HOLDER = new ThreadLocal<>();

    public static void setCurrentUser(User user) {
        USER_HOLDER.set(user);
    }

    public static User getCurrentUser() {
        return USER_HOLDER.get();
    }

    public static Long getCurrentUserId() {
        User user = USER_HOLDER.get();
        return user != null ? user.getId() : null;
    }

    public static boolean isAdmin() {
        User user = USER_HOLDER.get();
        return user != null && RoleConstants.ADMIN.equals(user.getRole());
    }

    public static void clear() {
        USER_HOLDER.remove();
    }
}
