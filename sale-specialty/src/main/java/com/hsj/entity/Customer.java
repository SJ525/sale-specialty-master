package com.hsj.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

/*
 *@ClassName customer
 *@Description 客户实体
 *@Author hsj
 *Date 2020/3/4 11:55
 */
@Entity
@Table(name = "customer")
@NoArgsConstructor
@Setter
@Getter
@ToString
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer cusId;         //客户id
    private String cusName;        //客户名称
    private String cusType;        //客户类型
    private String cusPhone;       //客户电话
    private String cusEmail;       //客户邮箱
    private String cusAddress;     //客户地址
    private String relation;        //有无关联其它模块，决定是否可删除
    @Transient
    private String msg;             //消息提示
}
