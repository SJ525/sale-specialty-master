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
 *@ClassName SaleOrder
 *@Description 销售订单实体
 *@Author hsj
 *Date 2020/3/13 11:45
 */
@Entity
@Table(name = "sale_order")
@NoArgsConstructor
@Setter
@Getter
@ToString
public class SaleOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer orderId;    //销售订单id
    private String orderNumber; //销售订单号
    private Integer saleSpId;   //销售员id
    @Transient
    private String spName;      //销售员姓名
    private Integer saleCusId;  //客户id
    @Transient
    private String cusName;     //客户姓名
    @JsonFormat(pattern="yyyy-MM-dd HH:mm",timezone="GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm")
    private Date date;      //销售时间
    private String proMsg;      //销售特产信息
    private BigDecimal totalPrice;//订单总价格
    private String payWay;      //客户支付方式
    private String type;        //订单类型
    @Transient
    @JsonFormat(pattern="yyyy-MM-dd HH:mm",timezone="GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date startDate;              //查询开始日期
    @Transient
    @JsonFormat(pattern="yyyy-MM-dd HH:mm",timezone="GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date endDate;               //查询结束日期
    @Transient
    private String msg;         //消息提示
}
