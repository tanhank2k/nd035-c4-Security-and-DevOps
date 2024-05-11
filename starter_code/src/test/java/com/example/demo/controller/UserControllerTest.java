package com.example.demo.controller;

import com.example.demo.controllers.OrderController;
import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import com.example.demo.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.demo.controller.helper.UserHelper.buildNewUser;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {
    private UserController userController;
    private final UserRepository userRepo = mock(UserRepository.class);
    private final CartRepository cartRepository = mock(CartRepository.class);
    private final BCryptPasswordEncoder bCryptPasswordEncoder = mock(BCryptPasswordEncoder.class);
    public static String USER_NAME = "username";
    public static String PASSWORD = "password";
    public static String PASSWORD_LESS_7_CHAR = "pass";
    public static String PASSWORD_HASH = "password_hash";

    @Before
    public void setUp() {
        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository", userRepo);
        TestUtils.injectObjects(userController, "cartRepository", cartRepository);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", bCryptPasswordEncoder);
    }

    @Test
    public void createUser(){
        when(bCryptPasswordEncoder.encode(PASSWORD)).thenReturn(PASSWORD_HASH);
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername(USER_NAME);
        createUserRequest.setPassword(PASSWORD);
        createUserRequest.setConfirmPassword(PASSWORD);
        ResponseEntity<User> response = userController.createUser(createUserRequest);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        User itemResponse = response.getBody();
        assertNotNull(itemResponse);
        assertEquals(0L, itemResponse.getId());
        assertEquals(USER_NAME, itemResponse.getUsername());
        assertEquals(PASSWORD_HASH, itemResponse.getPassword());
    }

    @Test
    public void createUser_PasswordLessThan7Character(){
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername(USER_NAME);
        createUserRequest.setPassword(PASSWORD_LESS_7_CHAR);
        createUserRequest.setConfirmPassword(PASSWORD_LESS_7_CHAR);
        ResponseEntity<User> response = userController.createUser(createUserRequest);
        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    public void createUser_PasswordNotEqConfirmPass(){
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername(USER_NAME);
        createUserRequest.setPassword(PASSWORD);
        createUserRequest.setConfirmPassword(PASSWORD_LESS_7_CHAR);
        ResponseEntity<User> response = userController.createUser(createUserRequest);
        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    public void getUserById() {
        User user = buildNewUser(0L);
        when(userRepo.findById(any())).thenReturn(Optional.of(user));
        ResponseEntity<User> response = userController.findById(0L);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        User itemResponse = response.getBody();
        assertNotNull(itemResponse);
        assertEquals(0L, itemResponse.getId());
        assertEquals(USER_NAME, itemResponse.getUsername());
        assertEquals(PASSWORD, itemResponse.getPassword());
    }

    @Test
    public void findByUserName() {
        User user = buildNewUser(0L);
        when(userRepo.findByUsername(any())).thenReturn(user);
        ResponseEntity<User> response = userController.findByUserName(USER_NAME);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        User itemResponse = response.getBody();
        assertNotNull(itemResponse);
        assertEquals(0L, itemResponse.getId());
        assertEquals(USER_NAME, itemResponse.getUsername());
        assertEquals(PASSWORD, itemResponse.getPassword());
    }

    @Test
    public void findByUserName_NotFound() {
        when(userRepo.findByUsername(any())).thenReturn(null);
        ResponseEntity<User> response = userController.findByUserName(USER_NAME);
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }
}
