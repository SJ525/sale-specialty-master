package com.hsj.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

/*
 *@ClassName SaleOrderReturn
 *@Description
 *@Author hsj
 *Date 2020/3/13 12:30
 */
@Entity
@Table(name = "sale_order_return")
@NoArgsConstructor
@Setter
@Getter
@ToString
public class SaleOrderReturn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer returnId;           //销售退货订单id
    private Integer orderId;            //销售订单id
    private String orderNumber;         //销售订单编号
    @JsonFormat(pattern="yyyy-MM-dd HH:mm",timezone="GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm")
    private Date startDate;             //退货开始日期
    @JsonFormat(pattern="yyyy-MM-dd HH:mm",timezone="GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm")
    private Date endDate;               //退货完成日期
    private Integer days;               //退货所用时间
    private String status;              //退货状态
    @Transient
    @JsonFormat(pattern="yyyy-MM-dd HH:mm",timezone="GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date queryStart;            //退货开始日期查询起始
    @Transient
    @JsonFormat(pattern="yyyy-MM-dd HH:mm",timezone="GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date queryEnd;              //退货开始日期查询结尾
    @Transient
    private String changeType;         //转换订单类型
    @Transient
    private String msg;                 //消息提示
}
