package com.baizhi.lht.po;

import com.baizhi.lht.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryPo {

    private String c1id;
    private String c1cateName;
    private Integer c1levels;
    private String c1parentId;
    private List<Category> categoryList;
}
