package com.example.shop;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.util.LinkedHashMap;
import java.util.Map;

/** Per-session shopping cart: product id -> quantity. */
@Component
@SessionScope
public class Cart {

    private final Map<Long, Integer> items = new LinkedHashMap<>();

    public void add(Long productId) {
        items.merge(productId, 1, Integer::sum);
    }

    public void remove(Long productId) {
        items.remove(productId);
    }

    public void clear() {
        items.clear();
    }

    public Map<Long, Integer> getItems() {
        return items;
    }

    public int getCount() {
        return items.values().stream().mapToInt(Integer::intValue).sum();
    }
}
