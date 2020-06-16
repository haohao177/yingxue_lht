package com.baizhi.lht.serviceImpl;

import com.baizhi.lht.dao.AdminMapper;
import com.baizhi.lht.entity.Admin;
import com.baizhi.lht.service.AdminService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.HashMap;

@Service
@Transactional
public class AdminServiceImpl implements AdminService {

    //依赖注入
    @Resource
    private AdminMapper adminMapper;

    @Resource
    private HttpSession session;


    @Override
    public HashMap<String, Object> login(Admin admin, String enCode) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        //取出验证码
        String code = (String) session.getAttribute("code");
        //比对验证码
        if (code.equalsIgnoreCase(enCode)) {
            //验证码正确
            //查询用户

            Admin admin1 = adminMapper.selectOne(admin);


            // 查询用户是否存在
            if (admin1 != null) {
                //存在
                //验证密码是否正确
                if (admin.getPassword().equals(admin1.getPassword())) {
                    //正确 登录
                    session.setAttribute("admin", admin1);
                    map.put("status", "100");
                    map.put("message", "登录成功");

                } else {
                    //错误 提示错误情况
                    map.put("status", "101");
                    map.put("message", "密码错误");
                }


            } else {
                //不存在
                map.put("status", "101");
                map.put("message", "用户不存在");
            }
        } else {
            //验证码错误
            map.put("status", "101");
            map.put("message", "验证码错误");
        }
        return map;
    }
}
