package com.hsj.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

/*
 *@ClassName BuyProduct
 *@Description 特产采购实体
 *@Author hsj
 *Date 2020/3/7 19:37
 */
@Entity
@Table(name = "buy_product")
@NoArgsConstructor
@Setter
@Getter
public class BuyProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer buyId;          //采购特产id
    private Integer buyProId;       //特产id
    @Transient
    private String proName;         //特产名称
    private Integer buyOrderId;     //采购订单id
    @Transient
    private String orderNumber;     //采购订单编号
    private Integer buySupId;       //供应商id
    @Transient
    private String supName;         //供应商名称
    private Integer quantity;       //采购数量
    @Transient
    private String proUnit;         //计价单位
    private BigDecimal price;       //采购价格
    private String relation;        //有无关联其它模块，决定是否可删除
    @Transient
    private Integer startQuantity;  //采购数量起始查询
    @Transient
    private Integer endQuantity;    //采购数量结尾查询
    @Transient
    private BigDecimal startPrice;  //采购价格起始查询
    @Transient
    private BigDecimal endPrice;    //采购价格结尾查询
    @Transient
    private String msg;             //消息提示

    @Override
    public String toString() {
        return "[" +
                "特产名:" + proName +
                ", 供应商:" + supName +
                ", 采购数目:" + quantity +proUnit+
                ", 采购价格:￥" + price +
                ']';
    }
}
