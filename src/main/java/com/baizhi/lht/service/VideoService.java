package com.baizhi.lht.service;

import com.baizhi.lht.entity.Video;
import com.baizhi.lht.vo.VideoVo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

public interface VideoService {
    HashMap<String, Object> showVideoPage(Integer page, Integer rows);

    String add(Video video);

    void update(Video video);

    HashMap<String, Object> delete(Video video);

    void uploadVideo(MultipartFile headImg, String id, HttpServletRequest request);

    List<VideoVo> queryByReleaseTime();

    List<Video> quryby();

    List<VideoVo> queryByLikeVideoName(String content);

    VideoVo queryByVideoDetail(String videoId, String cateId, String userId);

    HashMap<String, Object> queryCateVideoList(String cateId);

    List<Video> querySearch(String content);
}
