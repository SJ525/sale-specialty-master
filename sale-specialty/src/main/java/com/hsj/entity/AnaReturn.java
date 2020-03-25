package com.hsj.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/*
 *@ClassName AnaReturn
 *@Description 实体-采购与销售退货统计
 *@Author hsj
 *Date 2020/3/16 21:03
 */
@NoArgsConstructor
@Setter
@Getter
@ToString
public class AnaReturn {
    private Integer proId;          //特产id
    private String proName;         //特产名称
    private String proUnit;         //计价单位
    private Integer purQuaAll;      //采购总数量
    private Integer purRetNum;      //采购退货数量
    private Integer saleQuaAll;      //销售总数量
    private Integer saleRetNum;     //销售退货数量
    private String purRetRate;      //采购退货率
    private String saleRetRate;     //销售退货率
    @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date startDate;        //查询日期开始
    @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date endDate;          //查询日期结尾
}
