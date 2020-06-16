package com.baizhi.lht.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "yx_user")
public class User implements Serializable {
    @Id
    @Excel(name = "用户ID")
    private String id;
    @Excel(name = "用户名")
    private String username;
    @Excel(name = "手机号")
    private String hone;
    @Excel(name = "用户头像")
    @Column(name = "head_img")
    private String headImg;
    @Excel(name = "用户简介")
    private String breif;
    @Excel(name = "用户微信")
    private String wechat;
    @Excel(name = "用户状态")
    private String status;
    @Excel(name = "注册时间", format = "yyyy年MM月dd日")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "create_date")
    private Date createDate;
    @Excel(name = "性别")
    private String sex;
    @Excel(name = "地址")
    private String city;


}