package com.hsj.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/*
 *@ClassName Category
 *@Description 特产类型实体类
 *@Author hsj
 *Date 2020/2/26 12:19
 */
@Entity
@Table(name = "category")
@NoArgsConstructor
@Setter
@Getter
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer proTypeId;      //特产类型Id
    private String proTypeName;     //特产类型名称
    private String relation;        //有无关联其它模块，决定是否可删除
    @Transient
    private String msg;             //消息提示
}
