package com.example.demo.controller;

import com.example.demo.utils.TestUtils;
import com.example.demo.controllers.CartController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.demo.controller.helper.CartHelper.buildNewCart;
import static com.example.demo.controller.helper.ItemHelper.buildNewItem;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {
    public static final String USERNAME = "test";
    private CartController cartController;
    private final UserRepository userRepo = mock(UserRepository.class);
    private final CartRepository cartRepo = mock(CartRepository.class);
    private final ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setUp() {
        cartController = new CartController();
        TestUtils.injectObjects(cartController, "cartRepository", cartRepo);
        TestUtils.injectObjects(cartController, "userRepository", userRepo);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepository);
    }

    @Test
    public void addToCart() {
        User user = new User();
        user.setUsername(USERNAME);

        Item item = buildNewItem(0L, "Round Widget", new BigDecimal("2.99"), "A widget that is round");

        List<Item> itemList = new ArrayList<>();
        itemList.add(item);
        Cart cart = buildNewCart(0L, itemList,user);
        user.setCart(cart);

        when(userRepo.findByUsername(USERNAME)).thenReturn(user);
        when(itemRepository.findById(0L)).thenReturn(java.util.Optional.of(item));

        ModifyCartRequest request = new ModifyCartRequest();
        request.setItemId(0L);
        request.setQuantity(1);
        request.setUsername("test");

        ResponseEntity<Cart> response = cartController.addTocart(request);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        Cart retrievedCart = response.getBody();
        assertNotNull(retrievedCart);
        assertEquals(Optional.of(0L).get(), retrievedCart.getId());

        List<Item> items = retrievedCart.getItems();
        assertNotNull(items);
        Item retrievedItem = items.get(0);
        assertEquals(2, items.size());
        assertNotNull(retrievedItem);
        assertEquals(item, retrievedItem);
        assertEquals(new BigDecimal("5.98"), retrievedCart.getTotal());
        assertEquals(user, retrievedCart.getUser());
    }

    @Test
    public void testAddToCartNullUser() {
        User user = new User();
        user.setUsername(USERNAME);

        Item item = buildNewItem(0L, "Round Widget", new BigDecimal("2.99"), "A widget that is round");

        List<Item> itemList = new ArrayList<>();
        itemList.add(item);
        Cart cart = buildNewCart(0L, itemList,user);
        user.setCart(cart);

        when(userRepo.findByUsername(USERNAME)).thenReturn(null);
        when(itemRepository.findById(0L)).thenReturn(java.util.Optional.of(item));

        ModifyCartRequest request = new ModifyCartRequest();
        request.setItemId(0L);
        request.setQuantity(1);
        request.setUsername("test");

        ResponseEntity<Cart> response = cartController.addTocart(request);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void removeFromCart() {
        User user = new User();
        user.setUsername(USERNAME);

        Item item = buildNewItem(0L, "Round Widget", new BigDecimal("2.99"), "A widget that is round");

        List<Item> itemList = new ArrayList<>();
        itemList.add(item);
        Cart cart = buildNewCart(0L, itemList,user);
        user.setCart(cart);

        when(userRepo.findByUsername(USERNAME)).thenReturn(user);
        when(itemRepository.findById(0L)).thenReturn(java.util.Optional.of(item));

        ModifyCartRequest request = new ModifyCartRequest();
        request.setItemId(0L);
        request.setQuantity(1);
        request.setUsername("test");

        ResponseEntity<Cart> response = cartController.removeFromcart(request);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        Cart retrievedCart = response.getBody();
        assertNotNull(retrievedCart);
        assertEquals(Optional.of(0L).get(), retrievedCart.getId());
        List<Item> items = retrievedCart.getItems();
        assertNotNull(items);
        assertEquals(0, items.size());
        assertEquals(new BigDecimal("0.00"), retrievedCart.getTotal());
        assertEquals(user, retrievedCart.getUser());
    }
}
