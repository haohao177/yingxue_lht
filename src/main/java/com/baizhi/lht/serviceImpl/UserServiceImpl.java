package com.baizhi.lht.serviceImpl;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.baizhi.lht.annotation.AddCache;
import com.baizhi.lht.annotation.AddLog;
import com.baizhi.lht.annotation.DelCache;
import com.baizhi.lht.dao.UserMapper;
import com.baizhi.lht.entity.AdminExample;
import com.baizhi.lht.entity.Month;
import com.baizhi.lht.entity.User;
import com.baizhi.lht.entity.UserExample;
import com.baizhi.lht.service.UserService;
import org.apache.ibatis.session.RowBounds;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    //依赖注入
    @Resource
    private UserMapper userMapper;

    //查询
    @AddCache
    @Override
    public HashMap<String, Object> showAllPage(Integer page, Integer rows) {
        HashMap<String, Object> map = new HashMap<String, Object>();

        //存储当前页
        map.put("page", page);
        //获取总条数

        int i = userMapper.selectCountByExample(new UserExample());
        //存储总条数
        map.put("records", i);

        //获取总页数
        Integer total = i % rows == 0 ? i / rows : i / rows + 1;
        //存储总页数
        map.put("total", total);

        //查询展示数据
        AdminExample adminExample = new AdminExample();
        RowBounds rowBounds = new RowBounds((page - 1) * rows, rows);
        //存储查询的展示数据
        List<User> users = userMapper.selectByExampleAndRowBounds(adminExample, rowBounds);
        map.put("rows", users);
        return map;
    }


    //添加操作
    @DelCache
    @AddLog(description = "添加用户")
    @Override
    public String add(User user) {
        //插入数据
        user.setId(UUID.randomUUID().toString());
        user.setStatus("1");
        user.setCreateDate(new Date());
        userMapper.insertSelective(user);
        return user.getId();
    }

    //修改路径
    @DelCache
    @AddLog(description = "修改头像")
    @Override
    public void uploadUserCover(MultipartFile headIamge, String id, HttpServletRequest request) {
        //获取上传路径文件路径 通过相对路径获得绝对路径
        String realPath = request.getSession().getServletContext().getRealPath("/upload/photo");
        //判断文件是否存在
        File file = new File(realPath);
        if (!file.exists()) {
            file.mkdirs(); //创建文件夹
        }
        //获取文件名
        String filename = headIamge.getOriginalFilename();
        //创建一个新文件名
        String newFileName = new Date().getTime() + "-" + filename;

        //文件上传
        try {
            headIamge.transferTo(new File(realPath, newFileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        UserExample userExample = new UserExample();
        userExample.createCriteria().andIdEqualTo(id);
        User user = new User();
        user.setHeadImg(newFileName);
        //修改表中文件名
        System.out.println("修改");
        System.out.println(id);
        userMapper.updateByExampleSelective(user, userExample);
    }

    //修改头像
    @DelCache
    @AddLog(description = "修改头像")
    @Override
    public void uploadUserCoverAliyun(MultipartFile headIamge, String id, HttpServletRequest request) {
        // 1.获取文件名
        String filename = headIamge.getOriginalFilename();
        //创建一个新文件名
        String newFileName = new Date().getTime() + "-" + filename;
        System.out.println(newFileName);
        //2把文件转化为bute数组
        byte[] bytes = null;
        try {
            bytes = headIamge.getBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String buckeName = "yingxue-lht";
        String ObjectNmae = "headImage/" + newFileName;
        //3.上传到阿里云
        // Endpoint以杭州为例，其它Region请按实际情况填写。
        String endpoint = "http://oss-cn-beijing.aliyuncs.com";
        // 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建RAM账号。
        String accessKeyId = "LTAI4GAMZo4HjW8m8W5Qv83E";
        String accessKeySecret = "arUd0arlY1Qi9QWtpBKEmmBYwWDp9j";

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        // 上传Byte数组。
        byte[] content = "Hello OSS".getBytes();
        ossClient.putObject(buckeName, ObjectNmae, new ByteArrayInputStream(bytes));

        // 关闭OSSClient。
        ossClient.shutdown();


        UserExample userExample = new UserExample();
        userExample.createCriteria().andIdEqualTo(id);
        User user = new User();
        user.setHeadImg(newFileName);
        //修改表中文件名
        userMapper.updateByExampleSelective(user, userExample);
    }

    //修改
    @DelCache
    @AddLog(description = "修改用户信息")
    @Override
    public void update(User user) {
        System.out.println(user);
        if (user.getHeadImg().equals("")) {
            User user1 = userMapper.selectOne(user);
            System.out.println(user1);
            user.setHeadImg(user1.getHeadImg());
            userMapper.updateByPrimaryKeySelective(user);
        }
        userMapper.updateByPrimaryKeySelective(user);


    }

    //导出
    @AddCache
    @Override
    public HashMap<String, Object> downloadUser() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        try {
            //查询所有用户数据
            UserExample example = new UserExample();
            List<User> users = userMapper.selectByExample(example);
            System.out.println(users);

            Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams("用户数据表", "用户"), User.class, users);
            workbook.write(new FileOutputStream(new File("G:/users.xls")));
            workbook.close();
            map.put("status", "100");
            map.put("message", "导出成功");
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", "104");
            map.put("message", "导出失败");
        }
        return map;
    }

    //注册量
    @AddCache
    @Override
    public HashMap<String, Object> querydataMonth() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        List<Month> m = userMapper.queryDataMonth("男");
        List<Month> w = userMapper.queryDataMonth("女");
        List<Month> men = new ArrayList<>();
        List<Month> woman = new ArrayList<>();

        for (int i = 1; i <= 6; i++) {
            Month month = new Month();
            month.setMonth(i + "月");
            month.setCount(0);
            men.add(month);

        }
        for (int i = 1; i <= 6; i++) {
            Month month = new Month();
            month.setMonth(i + "月");
            month.setCount(0);

            woman.add(month);
        }
        for (Month m1 : m) {
            for (int i = 0; i < 6; i++) {
                if (m1.getMonth().equals(men.get(i).getMonth())) {
                    Month month1 = men.get(i);
                    month1.setCount(m1.getCount());
                    men.set(i, month1);
                }
            }
        }

        for (Month m2 : w) {
            for (int i = 0; i < 6; i++) {
                if (m2.getMonth().equals(woman.get(i).getMonth())) {
                    Month month2 = woman.get(i);
                    month2.setCount(m2.getCount());
                    woman.set(i, month2);
                }
            }
        }

        map.put("men", men);
        map.put("woman", woman);

        return map;
    }
}