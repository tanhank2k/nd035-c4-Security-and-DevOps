package com.example.demo.controller.helper;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CartHelper {

    public static Cart buildNewCart(Long id, List<Item> itemList, User user){
        Cart cart = new Cart();
        cart.setId(id);
        cart.setItems(itemList);
        BigDecimal totalPrice = BigDecimal.valueOf(
                itemList.stream().mapToDouble(item -> item.getPrice().doubleValue()).sum()
        );
        cart.setTotal(totalPrice);
        cart.setUser(user);
        return cart;
    }
}
