package com.hsj.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/*
 *@ClassName PurOrder
 *@Description 采购订单实体类
 *@Author hsj
 *Date 2020/3/7 19:37
 */
@Entity
@Table(name = "pur_order")
@NoArgsConstructor
@Setter
@Getter
@ToString
public class PurOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer orderId;        //采购订单id
    private String orderNumber;     //采购订单编号
    private Integer purManId;       //采购员id
    @Transient
    private String purManName;      //采购员姓名
    @JsonFormat(pattern="yyyy-MM-dd HH:mm",timezone="GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm")
    private Date date;              //采购日期
    private Integer purWareId;      //所入仓库id
    @Transient
    private String wareName;       //仓库名称
    private String proMsg;          //采购特产信息
    private BigDecimal totalPrice;  //采购总价格
    private String payWay;          //付款方式
    private String type;            //订单类型（正常订单，退货订单）
    @Transient
    @JsonFormat(pattern="yyyy-MM-dd HH:mm",timezone="GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date startDate;              //查询开始日期
    @Transient
    @JsonFormat(pattern="yyyy-MM-dd HH:mm",timezone="GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date endDate;               //查询结束日期
    @Transient
    private String Msg;             //消息提示
}
