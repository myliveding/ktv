package com.st.ktv.service.impl;

import com.st.ktv.entity.User;
import com.st.ktv.mapper.UserMapper;
import com.st.ktv.service.UserService;
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
