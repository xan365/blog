package com.bamboo.blog.service;

import com.bamboo.blog.entity.User;

public interface UserService {

    User checkUser(String username, String password);
}
