package com.st.service;

import java.util.List;

import com.st.javabean.pojo.wxtour.User;

public interface UserService {
    /**
     * @Title selectByUsername
     * @Description 根据用户名查询用户信息
     * @return List<User>
     */
    List<User> selectByUsername(String username);
}
