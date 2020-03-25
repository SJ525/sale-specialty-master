package com.hsj.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigDecimal;

/*
 *@ClassName Product
 *@Description 特产信息
 *@Author hsj
 *Date 2020/2/26 12:19
 */
@Entity
@Table(name = "product")
@NoArgsConstructor
@Setter
@Getter
@ToString
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer proId;          //特产id
    private String proName;         //特产名称
    private BigDecimal proInPrice;  //特产进货价格
    private BigDecimal proOutPrice; //特产出货价格
    private String proUnit;         //计价单位
    private Integer cateProTypeId;      //特产类型id
    @Transient
    private String proTypeName;     //特产类型名称
    private String proImg;          //特产图片
    private Integer proStock;       //特产库存量
    private String relation;        //有无关联其它模块，决定是否可删除
    @Transient
    private String msg;             //消息提示
}
