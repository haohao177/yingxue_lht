package com.baizhi.lht.dao;


import com.baizhi.lht.entity.Category;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface CategoryMapper extends Mapper<Category> {
    List<Category> queryAllCategory();
}