package com.example.demo.controller.helper;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;

import java.math.BigDecimal;
import java.util.List;

public class UserHelper {
    public static final String DEFAULT_USERNAME = "username";
    public static final String DEFAULT_PASSWORD = "password";

    public static User buildNewUser(Long id){
        User user = new User();
        user.setUsername(DEFAULT_USERNAME);
        user.setPassword(DEFAULT_PASSWORD);
        user.setId(id);
        return user;
    }
}
