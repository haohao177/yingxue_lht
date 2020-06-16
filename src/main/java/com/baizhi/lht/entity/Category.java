package com.baizhi.lht.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "yx_category")
public class Category implements Serializable {
    @Id
    private String id;
    @Column(name = "cate_name")
    private String cateName;

    private Integer levels;
    @Column(name = "parent_id")
    private String parentId;

    private List<Category> categoryList;


}