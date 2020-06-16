package com.baizhi.lht.dao;


import com.baizhi.lht.entity.Video;
import com.baizhi.lht.vo.VideoVo;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface VideoMapper extends Mapper<Video> {
    List<VideoVo> queryByReleaseTimes();

    List<Video> quryby();

    List<VideoVo> queryByLikeVideoName(String content);

    List<VideoVo> queryCateVideoList(String cateId);
}
