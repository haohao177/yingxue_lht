package com.baizhi.lht.service;


import com.baizhi.lht.entity.User;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

public interface UserService {
    HashMap<String, Object> showAllPage(Integer page, Integer rows);

    String add(User user);

    void uploadUserCover(MultipartFile headIamge, String id, HttpServletRequest request);

    void uploadUserCoverAliyun(MultipartFile headIamge, String id, HttpServletRequest request);

    void update(User user);

    HashMap<String, Object> downloadUser();

    HashMap<String, Object> querydataMonth();
}
