package com.example.demo.controller.helper;

import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;

import java.util.stream.Collectors;

public class OrderHelper {
    public static final String DEFAULT_USERNAME = "username";
    public static final String DEFAULT_PASSWORD = "password";

    public static UserOrder buildNewUserOrder(Long id, User user){
        UserOrder userOrder = new UserOrder();
        userOrder.setId(id);
        userOrder.setItems(user.getCart().getItems().stream().collect(Collectors.toList()));
        userOrder.setTotal(user.getCart().getTotal());
        userOrder.setUser(user);
        return userOrder;
    }
}
