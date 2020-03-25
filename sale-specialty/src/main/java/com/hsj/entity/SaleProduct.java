package com.hsj.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

/*
 *@ClassName SaleProduct
 *@Description 销售特产实体
 *@Author hsj
 *Date 2020/3/13 12:17
 */
@Entity
@Table(name = "sale_product")
@NoArgsConstructor
@Setter
@Getter
public class SaleProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer saleId;         //销售特产id
    private Integer saleOrderId;    //销售订单id
    @Transient
    private String orderNumber;     //销售订单号
    private Integer saleProId;      //特产id
    @Transient
    private String proName;         //特产名称
    private Integer quantity;       //销售数量
    @Transient
    private String proUnit;         //计价单位
    private BigDecimal price;       //销售价格
    @Transient
    private Integer startQuantity;  //销售数量区间起始
    @Transient
    private Integer endQuantity;    //销售数量区间结尾
    @Transient
    private BigDecimal startPrice;  //销售价格区间起始
    @Transient
    private BigDecimal endPrice;    //销售价格区间结尾
    @Transient
    private String msg;             //消息提示

    @Override
    public String toString() {
        return "[" +
                "特产名:" + proName +
                ", 销售数目:" + quantity +proUnit+
                ", 销售价格:￥" + price +
                ']';
    }
}
