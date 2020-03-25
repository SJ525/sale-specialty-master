package com.hsj.service;

import com.hsj.dao.BuyProDao;
import com.hsj.dao.PurOrderDao;
import com.hsj.dao.SaleOrderDao;
import com.hsj.dao.SaleProDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

/*
 *@ClassName AnaProService
 *@Description 服务器类-支出与收入统计
 *@Author hsj
 *Date 2020/3/17 14:55
 */
@Service
public class AnaProService {
    @Autowired
    BuyProDao buyProDao;
    @Autowired
    SaleProDao saleProDao;
    @Autowired
    PurOrderDao purOrderDao;
    @Autowired
    SaleOrderDao saleOrderDao;

    /*查询表pur_order的最小日期*/
    public Date findPurOrderMinDate(){return purOrderDao.findPurOrderMinDate();}

    /*查询表sale_order的最大日期*/
    public Date findSaleOrderMaxDate(){return saleOrderDao.findSaleOrderMaxDate();}

    /*查询表buy_product和表pur_order，查询正常订单的采购支出*/
    public BigDecimal buyPriceSumNor(){return buyProDao.buyPriceSumNor();}

    /*查询表sale_product,根据特产id，查询正常订单的销售收入*/
    public BigDecimal salePriceSumNor(){return saleProDao.salePriceSumNor();}

    /*查询buy_product和表pur_order，根据特产id和查询日期区间，查询正常订单的采购支出*/
    public BigDecimal  buyPriceSumNorByDateBetween(Date start,Date end){
        return buyProDao.buyPriceSumNorByDateBetween(start,end);
    }

    /*查询表sale_product和表sale_order,根据特产id和查询日期区间，查询正常订单的销售收入*/
    public BigDecimal salePriceSumNorByDateBetween(Date start,Date end){
        return saleProDao.salePriceSumNorByDateBetween(start,end);
    }
}
