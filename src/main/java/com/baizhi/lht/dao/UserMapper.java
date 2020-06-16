package com.baizhi.lht.dao;


import com.baizhi.lht.entity.Month;
import com.baizhi.lht.entity.User;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface UserMapper extends Mapper<User> {
    User queryByUsername(String username);

    List<Month> queryDataMonth(String sex);
}