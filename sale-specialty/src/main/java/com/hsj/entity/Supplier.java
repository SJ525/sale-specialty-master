package com.hsj.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

/*
 *@ClassName Supplier
 *@Description 供应商实体类
 *@Author hsj
 *Date 2020/3/4 12:02
 */
@Entity
@Table(name = "supplier")
@NoArgsConstructor
@Setter
@Getter
@ToString
public class Supplier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer supId;          //供应商id
    private String supName;         //供应商名称
    private String supType;         //供应商类型
    private String supPhone;        //供应商电话
    private String supEmail;        //供应商邮箱
    private String supAddress;      //供应商地址
    private String relation;        //有无关联其它模块，决定是否可删除
    @Transient
    private String msg;             //消息提示
}
