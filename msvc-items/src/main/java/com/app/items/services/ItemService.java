package com.app.items.services;

import com.app.items.models.Item;

import java.util.List;
import java.util.Optional;

public interface ItemService {

    List<Item> getItems();

    Optional<Item> getItem(Long id);
}
