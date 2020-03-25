package com.hsj.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

/*
 *@ClassName StoreMan
 *@Description 仓库管理员-实体类
 *@Author hsj
 *Date 2020/3/5 11:47
 */
@Entity
@Table(name = "store_man")
@NoArgsConstructor
@Setter
@Getter
@ToString
public class StoreMan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer smId;          //仓库管理员id
    private String smName;         //仓库管理员姓名
    private String smSex;          //仓库管理员性别
    private Integer smAge;         //仓库管理员年龄
    private String smPhone;        //仓库管理员电话
    private String smAddress;      //仓库管理员地址
    private String relation;        //有无关联其它模块，决定是否可删除
    @Transient
    private String msg;             //消息提示
}
