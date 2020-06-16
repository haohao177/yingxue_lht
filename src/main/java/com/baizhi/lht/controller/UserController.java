package com.baizhi.lht.controller;

import com.alibaba.fastjson.JSON;
import com.baizhi.lht.entity.User;
import com.baizhi.lht.service.UserService;
import com.baizhi.lht.util.AliyunSendPhoneUtil;
import io.goeasy.GoEasy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@Controller
@RequestMapping("user")
public class UserController {

    //依赖注入
    @Resource
    private UserService userService;


    //分页展示
    @RequestMapping("showAllPage")
    @ResponseBody
    public HashMap<String, Object> showAllPage(Integer page, Integer rows) {
        //  page 是当前页  rows 是展示条数
        HashMap<String, Object> map = userService.showAllPage(page, rows);

        return map;
    }


    //更新操作
    @RequestMapping("edit")
    @ResponseBody
    public String edit(User user, String oper) {
        String uid = null;
        //添加
        if (oper.equals("add")) {
            uid = userService.add(user);

            //修改
        } else if (oper.equals("edit")) {
            userService.update(user);
            //删除
        } else if (oper.equals("del")) {

        }
        return uid;
    }

    //上传文件
    @RequestMapping("uploadUserCover")
    @ResponseBody
    public void uploadUserCover(MultipartFile headImg, String id, HttpServletRequest request) {

        userService.uploadUserCoverAliyun(headImg, id, request);
    }

    @RequestMapping("getPhoneCode")
    @ResponseBody
    public HashMap<String, Object> getPhoneCode(String phone, HttpServletRequest request) {
        //生成随机数
        String random = AliyunSendPhoneUtil.getRandom(6);

        //存储随机数
        request.getSession().setAttribute("phoneCode", random);

        //发送短信 并获取发送状态
        String s = AliyunSendPhoneUtil.sendPhoneMessage(phone, random);

        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("message", s);
        return map;
    }

    /*
     * 用户数据导出
     *
     * */
    @ResponseBody
    @RequestMapping("downloadUser")
    public HashMap<String, Object> downloadUser() {
        HashMap<String, Object> map = userService.downloadUser();
        return map;
    }

    /*
     * 用户数据注册月份
     *
     * */
    @ResponseBody
    @RequestMapping("dataMonth")
    public HashMap<String, Object> dataMonth() {
        //获取数据
        HashMap<String, Object> map = userService.querydataMonth();
        //转换成json格式的字符串
        String content = JSON.toJSONString(map);
        //设置Goeasy 参数 指定的服务地址  自己的appkey
        GoEasy goEasy = new GoEasy("http://rest-hangzhou.goeasy.io", "BC-1d32917956554f3698de28365aa54183");
        //发布消息  参数  通道名 ，发送的数据/内容
        goEasy.publish("yingxue", content);


        return map;
    }

}
