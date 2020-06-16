package com.baizhi.lht.controller;


import com.baizhi.lht.entity.Admin;
import com.baizhi.lht.service.AdminService;
import com.baizhi.lht.util.ImageCodeUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;

@Controller
@RequestMapping("admin")
public class AdminController {
    //依赖注入
    @Resource
    private AdminService adminService;


    //生成验证码
    @RequestMapping("getImageCode")
    @ResponseBody
    public void getImageCode(HttpServletRequest request, HttpServletResponse response) {
        //获取验证码
        String code = ImageCodeUtil.getSecurityCode();
        //存储验证码
        request.getSession().setAttribute("code", code);
        //通过验证码生成图片
        BufferedImage image = ImageCodeUtil.createImage(code);
        //把验证码图片传递到网页
        try {
            ImageIO.write(image, "png", response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //管理员登录
    @RequestMapping("login")
    @ResponseBody
    public HashMap<String, Object> login(Admin admin, String enCode) {


        HashMap<String, Object> map = adminService.login(admin, enCode);

        return map;
    }

    //管理员退出
    @RequestMapping("logout")
    public String logout(HttpServletRequest request) {
        //获取session  并删除其中的用户对象
        request.getSession().removeAttribute("admin");
        //跳转到登录页面
        return "redirect:/login/login.jsp";
    }

}
