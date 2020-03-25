package com.hsj.entity;

import lombok.*;

import javax.persistence.*;

/*
 *@ClassName Purchaser
 *@Description 采购员-实体类
 *@Author hsj
 *Date 2020/3/5 11:41
 */
@Entity
@Table(name = "purchaser")
@NoArgsConstructor
@Setter
@Getter
@ToString
public class Purchaser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer purId;          //采购员id
    private String purName;         //采购员姓名
    private String purSex;          //采购员性别
    private Integer purAge;         //采购员年龄
    private String purPhone;        //采购员电话
    private String purAddress;      //采购员地址
    private String relation;        //有无关联其它模块，决定是否可删除
    @Transient
    private String msg;             //消息提示

    public Purchaser(String purName, String purSex, Integer purAge, String purPhone, String purAddress, String relation) {
        this.purName = purName;
        this.purSex = purSex;
        this.purAge = purAge;
        this.purPhone = purPhone;
        this.purAddress = purAddress;
        this.relation = relation;
    }
}
