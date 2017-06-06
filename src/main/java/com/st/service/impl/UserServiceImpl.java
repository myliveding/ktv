package com.st.service.impl;

import com.st.javabean.pojo.wxtour.User;
import com.st.mapper.wxtour.UserMapper;
import com.st.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service("userService")
public class UserServiceImpl implements UserService {

	@Autowired
	private UserMapper userMapper;
	
    /**
     * @Title selectByUsername
     * @Description 根据用户名查询用户信息
     * @return List<User>
     */
    public List<User> selectByUsername(String username){
    	return userMapper.selectByUsername(username);
    }
}
