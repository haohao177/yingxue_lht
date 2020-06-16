package com.baizhi.lht.app;

import com.baizhi.lht.common.CommonResult;
import com.baizhi.lht.service.CategoryService;
import com.baizhi.lht.service.VideoService;
import com.baizhi.lht.util.AliyunSendPhoneUtil;
import com.baizhi.lht.vo.VideoVo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("app")
public class AppInterface {
    @Resource
    HttpSession session;

    //发送验证码
    /*
     * pgone s手机号
     *
     * */
    @RequestMapping("getPhoneCode")
    @ResponseBody
    public HashMap<String, Object> getPhoneCode(String phone) {


        HashMap<String, Object> map = new HashMap<>();
        String message = null;
        try {
            ///获取随机数
            String random = AliyunSendPhoneUtil.getRandom(6);
            //存储随机数
            session.setAttribute("phone", random);

            //调用工具类发送短信
            message = AliyunSendPhoneUtil.sendPhoneMessage(phone, random);
            map.put("status", "100");
            map.put("message", "验证码" + message);
            map.put("data", phone);
            System.out.println("到这里");
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", "104");
            map.put("message", message);
            map.put("data", null);
        }
        return map;
    }

    //登录验证
    /*
     * phone 手机号
     * pgoneCode 手机验证码
     *
     * */
    @RequestMapping("login")
    @ResponseBody
    public HashMap<String, Object> login(String phone, String phoneCode) {

        return new HashMap<>();
    }

    @Resource
    VideoService videoService;

    //展示首页数据
    @RequestMapping("queryByReleaseTime")
    @ResponseBody
    public CommonResult queryByReleaseTime() {

        try {
            List<VideoVo> videoVoList = videoService.queryByReleaseTime();
            return new CommonResult().success(videoVoList);
        } catch (Exception e) {
            return new CommonResult().failed();
        }
    }


    //搜索功能
    /*
     * content  搜索内容
     * */
    @ResponseBody
    @RequestMapping("queryByLikeVideoName")
    public HashMap<String, Object> queryByLikeVideoName(String content) {


        HashMap<String, Object> map = new HashMap<>();


        try {
            List<VideoVo> list = videoService.queryByLikeVideoName(content);
            if (list.size() == 0) {
                map.put("data", list);
                map.put("message", "无匹配内容");
                map.put("status", "100");

            } else {

                map.put("data", list);
                map.put("message", "查询成功");
                map.put("status", "100");
            }

        } catch (Exception e) {
            e.printStackTrace();
            map.put("data", new ArrayList<>());
            map.put("message", "查询失败");
            map.put("status", "104");
        }

        return map;
    }

    /*
     * 浏览视频信息
     * videoId  视频id
     *cateId 类别id
     *userId 用户id
     * */

    public HashMap<String, Object> queryByVideoDetail(String videoId, String cateId, String userId) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        VideoVo videoVo = videoService.queryByVideoDetail(videoId, cateId, userId);
        return map;
    }


    @Resource
    private CategoryService categoryService;

    /*
     * 查询所有类别
     *
     * */
    @RequestMapping("queryAllCategory")
    @ResponseBody
    public HashMap<String, Object> queryAllCategory() {
        HashMap<String, Object> map = categoryService.queryAllCategory();
        return map;

    }

    /*
     * 查询二级类别下的视频
     * cateId  二级类别id
     *
     * */

    @RequestMapping("queryCateVideoList")
    @ResponseBody
    public HashMap<String, Object> queryCateVideoList(String cateId) {
        HashMap<String, Object> map = videoService.queryCateVideoList(cateId);
        return map;
    }

}
