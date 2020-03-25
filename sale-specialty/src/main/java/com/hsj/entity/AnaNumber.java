package com.hsj.entity;

/*
 *@ClassName AnaNumber
 *@Description 实体-采购量与销售量统计
 *@Author hsj
 *Date 2020/3/16 20:30
 */

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

@NoArgsConstructor
@Setter
@Getter
@ToString
public class AnaNumber {
    private Integer proId;          //特产id
    private String proName;         //特产名称
    private Integer purQuantity;    //采购数量
    private Integer saleQuantity;   //销售数量
    private String proUnit;         //计价单位
    private BigDecimal pay;         //采购支出
    private BigDecimal income;      //销售收入
    private BigDecimal profit;      //利润盈亏（收入-支出)
    @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date startDate;        //查询时间开始
    @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date endDate;          //查询时间结尾
}
