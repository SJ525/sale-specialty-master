package com.hsj.service;

import com.hsj.dao.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/*
 *@ClassName AnaNumService
 *@Description 服务器-采购量与销售量统计
 *@Author hsj
 *Date 2020/3/17 14:51
 */
@Service
public class AnaNumService{
    @Autowired
    ProdDao prodDao;
    @Autowired
    BuyProDao buyProDao;
    @Autowired
    SaleProDao saleProDao;
    @Autowired
    PurOrderDao purOrderDao;
    @Autowired
    SaleOrderDao saleOrderDao;

    /*查询表buy_product，获取表buy_product所有pro_id*/
    public List<Integer> findAllProIds(){return buyProDao.findAllProIds();}

    /*查询表product，根据特产id,查询特产名称*/
    public String findProNameByProId(Integer id){return prodDao.findProNameByProId(id);}

    /*查询表product，根据特产id,查询特产计价单位*/
    public String findProUnitByProId(Integer id){return prodDao.findProUnitByProId(id);}

    /*查询表buy_product，根据特产id，查询采购数量*/
    public Integer findBuyQuantitySumAll(Integer id){return buyProDao.findBuyQuantitySumAll(id);}

    /*查询表sale_product,根据特产id，查询销售数量*/
    public Integer findSaleQuantitySumAll(Integer id){return saleProDao.findSaleQuantitySumAll(id);}

    /*查询表buy_product，根据特产id，查询采购支出*/
    public BigDecimal findPriceSumByBuyProId(Integer id){return buyProDao.findPriceSumByBuyProId(id);}

    /*查询表sale_product,根据特产id，查询销售收入*/
    public BigDecimal findPriceSumBySaleProId(Integer id){return saleProDao.findPriceSumBySaleProId(id);}

    /*查询表pur_order的最小日期*/
    public Date findPurOrderMinDate(){return purOrderDao.findPurOrderMinDate();}

    /*查询表sale_order的最大日期*/
    public Date findSaleOrderMaxDate(){return saleOrderDao.findSaleOrderMaxDate();}

    /*查询表buy_product和表pur_order，根据特产id和查询日期区间，查询采购数量*/
    public Integer buyQuaSumByIdAndDateBetween(Integer id, Date start,Date end){
        return buyProDao.buyQuaSumByIdAndDateBetween(id, start,end);
    }

    /*查询表sale_product和表sale_order,根据特产id和查询日期区间，查询销售数量*/
    public Integer saleQuaSumByIdAndDateBetween(Integer id, Date start,Date end){
        return saleProDao.saleQuaSumByIdAndDateBetween(id, start,end);
    }

    /*查询buy_product和表pur_order，根据特产id和查询日期区间，查询采购支出*/
    public BigDecimal buyPriceSumByIdAndDateBetween(Integer id, Date start,Date end){
        return buyProDao.buyPriceSumByIdAndDateBetween(id, start,end);
    }

    /*查询表sale_product和表sale_order,根据特产id和查询日期区间，查询销售收入*/
    public BigDecimal salePriceSumByIdAndDateBetween(Integer id, Date start,Date end){
        return saleProDao.salePriceSumByIdAndDateBetween(id, start,end);
    }
}
