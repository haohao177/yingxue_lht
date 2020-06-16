package com.baizhi.lht.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "yx_admin")
public class Admin implements Serializable {
    @Id
    private Integer id;
    @Column(name = "username")
    private String username;
    @Column(name = "username")
    private String password;


}