package com.example.demo.controller.helper;

import com.example.demo.model.persistence.Item;

import java.math.BigDecimal;
import java.util.Objects;

public class ItemHelper {
    public static final String DEFAULT_NAME = "Name";
    public static final BigDecimal DEFAULT_PRICE = BigDecimal.valueOf(2.99);
    public static final String DEFAULT_DESC = "Description";

    public static Item buildNewItem(Long id, String name, BigDecimal price, String desc) {
        Item item = new Item();
        item.setId(id);
        item.setName(Objects.isNull(name) ? DEFAULT_NAME: name);
        item.setPrice(Objects.isNull(price) ? DEFAULT_PRICE:price);
        item.setDescription(Objects.isNull(desc) ? DEFAULT_DESC:desc);
        return item;
    }
}
