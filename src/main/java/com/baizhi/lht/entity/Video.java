package com.baizhi.lht.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "yingx", type = "video")
@Table(name = "yx_video")
public class Video implements Serializable {
    @Id
    @org.springframework.data.annotation.Id
    private String id;

    //视频名
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
    private String title;

    //视频简介
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
    private String brief;

    @Field(type = FieldType.Keyword)
    private String path;

    @Field(type = FieldType.Keyword)
    private String cover;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "publish_date")
    @Field(type = FieldType.Date)
    private Date publishDate;

    @Column(name = "category_id")
    @Field(type = FieldType.Keyword)
    private String categoryId;

    @Column(name = "group_id")
    @Field(type = FieldType.Keyword)
    private String groupId;

    @Field(type = FieldType.Keyword)
    @Column(name = "user_id")
    private String userId;

}