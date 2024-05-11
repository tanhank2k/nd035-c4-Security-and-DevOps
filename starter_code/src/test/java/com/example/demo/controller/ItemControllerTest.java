package com.example.demo.controller;

import com.example.demo.controllers.CartController;
import com.example.demo.controllers.ItemController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
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
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {
    private ItemController itemController;
    private final ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setUp() {
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepository);
    }

    @Test
    public void getAllItems() {
        Item item1 = buildNewItem(0L, "Round Widget", new BigDecimal("2.99"), "A widget that is round");
        Item item2 = buildNewItem(1L, "Round Widget", new BigDecimal("2.99"), "A widget that is round");
        List<Item> itemList = new ArrayList<>();
        itemList.add(item1);
        itemList.add(item2);

        when(itemRepository.findAll()).thenReturn(itemList);

        ResponseEntity<List<Item>> response = itemController.getItems();

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        List<Item> itemListResponse = response.getBody();
        assertNotNull(itemListResponse);
        assertEquals(2, itemListResponse.size());

        assertEquals(item1, itemListResponse.get(0));
        assertEquals(item2, itemListResponse.get(1));
    }

    @Test
    public void getItemById() {
        Item item1 = buildNewItem(0L, "Round Widget", new BigDecimal("2.99"), "A widget that is round");

        when(itemRepository.findById(any())).thenReturn(Optional.of(item1));

        ResponseEntity<Item> response = itemController.getItemById(0L);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        Item itemResponse = response.getBody();
        assertNotNull(itemResponse);
        assertEquals(Optional.of(0L).get(), itemResponse.getId());
        assertEquals("Round Widget", itemResponse.getName());
        assertEquals(new BigDecimal("2.99"), itemResponse.getPrice());
        assertEquals("A widget that is round", itemResponse.getDescription());
    }

    @Test
    public void getItemById_NotFound() {
        when(itemRepository.findById(any())).thenReturn(Optional.empty());
        ResponseEntity<Item> response = itemController.getItemById(1L);
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void getItemsByName() {
        Item item1 = buildNewItem(0L, "Round Widget", new BigDecimal("2.99"), "A widget that is round");
        Item item2 = buildNewItem(1L, "Round Widget", new BigDecimal("2.99"), "A widget that is round");
        List<Item> itemList = new ArrayList<>();
        itemList.add(item1);
        itemList.add(item2);
        when(itemRepository.findByName(any())).thenReturn(itemList);

        ResponseEntity<List<Item>> response = itemController.getItemsByName("Round Widget");

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        List<Item> itemListResponse = response.getBody();
        assertNotNull(itemListResponse);
        assertEquals(2, itemListResponse.size());

        assertEquals(item1, itemListResponse.get(0));
        assertEquals(item2, itemListResponse.get(1));
    }

    @Test
    public void getItemsByName_NotFound() {
        when(itemRepository.findByName(any())).thenReturn(null);
        ResponseEntity<List<Item>> response = itemController.getItemsByName("Name");
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

}
