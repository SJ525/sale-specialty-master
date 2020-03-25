package com.hsj.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

/*
 *@ClassName Warehouse
 *@Description 仓库实体类
 *@Author hsj
 *Date 2020/3/4 12:15
 */
@Entity
@Table(name = "warehouse")
@NoArgsConstructor
@Setter
@Getter
@ToString
public class Warehouse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer wareId;         //仓库id
    private String wareName;        //仓库名称
    private double wareSize;        //仓库大小（平方米）
    private String wareAddress;     //仓库地址
    private Integer wareSmId;       //仓库管理员id
    private String relation;        //有无关联其它模块，决定是否可删除
    @Transient
    private String wareSmName;      //仓库管理员姓名
    @Transient
    private double startSize;       //仓库大小查询起始
    @Transient
    private double endSize;         //仓库大小查询结尾
    @Transient
    private String msg;             //消息提示
}
