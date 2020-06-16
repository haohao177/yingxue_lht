package com.baizhi.lht.controller;

import com.baizhi.lht.entity.Video;
import com.baizhi.lht.service.VideoService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("video")
public class VideoController {

    //依赖注入
    @Resource
    private VideoService videoService;


    //分页查询
    @RequestMapping("showVideoPage")
    @ResponseBody
    public HashMap<String, Object> showVideoPage(Integer page, Integer rows) {
        HashMap<String, Object> map = videoService.showVideoPage(page, rows);
        return map;
    }

    //更新状态
    @RequestMapping("edit")
    @ResponseBody
    public Object edit(Video video, String oper) {
        if (oper.equals("add")) {
            String id = videoService.add(video);
            return id;
        } else if (oper.equals("edit")) {
            videoService.update(video);
        } else if (oper.equals("del")) {
            HashMap<String, Object> map = videoService.delete(video);
            return map;
        }
        return null;
    }

    @ResponseBody
    @RequestMapping("uploadVideo")
    public void uploadVideo(MultipartFile path, String id, HttpServletRequest request) {
        videoService.uploadVideo(path, id, request);
    }


    @ResponseBody
    @RequestMapping("querySearch")
    public List<Video> querySearch(String content) {
        List<Video> videos = videoService.querySearch(content);
        return videos;
    }
}
