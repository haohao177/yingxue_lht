package com.baizhi.lht.repository;

import com.baizhi.lht.entity.Video;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface VideoRepository extends ElasticsearchRepository<Video, String> {
}
