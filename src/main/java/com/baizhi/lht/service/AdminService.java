package com.baizhi.lht.service;


import com.baizhi.lht.entity.Admin;

import java.util.HashMap;

public interface AdminService {
    HashMap<String, Object> login(Admin admin, String enCode);
}
