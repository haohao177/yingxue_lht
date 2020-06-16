package com.baizhi.lht.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Month implements Serializable {

    private String month;
    private String sex;
    private List<City> citys;
    private Integer count;
}
