package com.hsj.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

/*
 *@ClassName Salesperson
 *@Description 销售员-实体类
 *@Author hsj
 *Date 2020/3/5 11:54
 */
@Entity
@Table(name = "salesperson")
@NoArgsConstructor
@Setter
@Getter
@ToString
public class Salesperson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer spId;          //销售员id
    private String spName;         //销售员姓名
    private String spSex;          //销售员性别
    private Integer spAge;         //销售员年龄
    private String spPhone;        //销售员电话
    private String spAddress;      //销售员地址
    private String relation;        //有无关联其它模块，决定是否可删除
    @Transient
    private String msg;             //消息提示
}
