package com.baizhi.lht.service;


import com.baizhi.lht.entity.Category;

import java.util.HashMap;

public interface CategoryService {
    HashMap<String, Object> showOneCategory(Integer page, Integer rows);

    HashMap<String, Object> showTwoCategory(Integer page, Integer rows, String prentId);

    HashMap<String, Object> add(Category category);

    HashMap<String, Object> update(Category category);

    HashMap<String, Object> delete(Category category);

    HashMap<String, Object> queryAllCategory();
}
