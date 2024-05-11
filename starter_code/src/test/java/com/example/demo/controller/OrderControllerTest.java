package com.example.demo.controller;

import com.example.demo.controllers.CartController;
import com.example.demo.controllers.OrderController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.demo.controller.helper.CartHelper.buildNewCart;
import static com.example.demo.controller.helper.ItemHelper.buildNewItem;
import static com.example.demo.controller.helper.OrderHelper.buildNewUserOrder;
import static com.example.demo.controller.helper.UserHelper.buildNewUser;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {
    public static final String USERNAME = "test";
    private OrderController orderController;
    private final UserRepository userRepo = mock(UserRepository.class);
    private final OrderRepository orderRepo = mock(OrderRepository.class);

    @Before
    public void setUp() {
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "userRepository", userRepo);
        TestUtils.injectObjects(orderController, "orderRepository", orderRepo);
    }

    @Test
    public void submitOrder(){
        Item item = buildNewItem(0L, "Round Widget", new BigDecimal("2.99"), "A widget that is round");

        List<Item> itemList = new ArrayList<>();
        itemList.add(item);
        User user = buildNewUser(0L);
        Cart cart = buildNewCart(0L, itemList,user);
        user.setCart(cart);

        UserOrder userOrder = buildNewUserOrder(0L,user);
        when(userRepo.findByUsername(any())).thenReturn(user);
        when(orderRepo.save(any())).thenReturn(userOrder);

        ResponseEntity<UserOrder> response = orderController.submit(user.getUsername());

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        UserOrder itemResponse = response.getBody();
        assertNotNull(itemResponse);
        assertEquals(Optional.of(0L).get(), itemResponse.getId());
        assertEquals(new BigDecimal("2.99"), itemResponse.getTotal());
    }

    @Test
    public void submitOrder_NotFound(){
        when(userRepo.findByUsername(any())).thenReturn(null);
        ResponseEntity<UserOrder> response = orderController.submit("test");
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }


    @Test
    public void getOrdersForUser(){
        Item item = buildNewItem(0L, "Round Widget", new BigDecimal("2.99"), "A widget that is round");
        List<Item> itemList = new ArrayList<>();
        itemList.add(item);
        User user = buildNewUser(0L);
        Cart cart = buildNewCart(0L, itemList,user);
        user.setCart(cart);
        UserOrder userOrder = buildNewUserOrder(0L,user);
        List<UserOrder> userOrderList = new ArrayList<>();
        userOrderList.add(userOrder);
        when(userRepo.findByUsername(any())).thenReturn(user);
        when(orderRepo.findByUser(any())).thenReturn(userOrderList);

        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser(user.getUsername());

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        List<UserOrder> itemResponse = response.getBody();
        assertNotNull(itemResponse);

        assertEquals(1, itemResponse.size());
        assertEquals(Optional.of(0L).get(), itemResponse.get(0).getId());
        assertEquals(new BigDecimal("2.99"), itemResponse.get(0).getTotal());
    }

    @Test
    public void getOrdersForUser_NotFound(){
        when(userRepo.findByUsername(any())).thenReturn(null);
        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("test");
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }
}
