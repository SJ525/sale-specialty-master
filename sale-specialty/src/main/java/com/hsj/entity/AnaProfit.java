package com.hsj.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/*
 *@ClassName AnaProfit
 *@Description 实体类-支出与收入统计
 *@Author hsj
 *Date 2020/3/16 21:13
 */
@NoArgsConstructor
@Setter
@Getter
@ToString
public class AnaProfit {
    private BigDecimal totalPay;        //采购总支出
    private BigDecimal totalInCome;     //销售总收入
    private BigDecimal profit;          //利润盈亏（销售收入-采购支出）
    @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date startDate;            //查询日期开始
    @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date endDate;              //查询日期结尾
}
