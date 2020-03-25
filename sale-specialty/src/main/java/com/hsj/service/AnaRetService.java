package com.hsj.service;

import com.hsj.dao.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/*
 *@ClassName AnaRetService
 *@Description 服务器类-采购与销售退货统计
 *@Author hsj
 *Date 2020/3/17 14:55
 */
@Service
public class AnaRetService {
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

    /*查询表pur_order的最小日期*/
    public Date findPurOrderMinDate(){return purOrderDao.findPurOrderMinDate();}

    /*查询表sale_order的最大日期*/
    public Date findSaleOrderMaxDate(){return saleOrderDao.findSaleOrderMaxDate();}

    /*查询表buy_product,根据特产id查询采购总数量*/
    public Integer findBuyQuantitySumAll(Integer id){return buyProDao.findBuyQuantitySumAll(id);}

    /*查询表buy_product和表pur_order,根据特产id查询采购退货数量*/
    public Integer findBuyQuantitySumRet(Integer id){return buyProDao.findBuyQuantitySumRet(id);}

    /*查询表sale_product,根据特产id，查询销售总数量*/
    public Integer findSaleQuantitySumAll(Integer id){return saleProDao.findSaleQuantitySumAll(id);}

    /*查询表sale_product和表sale_order,根据特产id查询销售退货数量*/
    public Integer findSaleQuantitySumRet(Integer id){return saleProDao.findSaleQuantitySumRet(id);}

    /*查询表buy_product和表pur_order，根据特产id和查询日期区间，查询采购数量*/
    public Integer buyQuaSumByIdAndDateBetween(Integer id, Date start,Date end){
        return buyProDao.buyQuaSumByIdAndDateBetween(id, start,end);
    }

    /*查询表buy_product和表pur_order,根据特产id和时间区间，查询采购退货数量*/
    public Integer purQuaSumRetByProIdAndDateBetween(Integer id,Date start,Date end){
        return buyProDao.purQuaSumRetByProIdAndDateBetween(id,start,end);
    }

    /*查询表sale_product和表sale_order,根据特产id和查询日期区间，查询销售数量*/
    public Integer saleQuaSumByIdAndDateBetween(Integer id, Date start,Date end) {
        return saleProDao.saleQuaSumByIdAndDateBetween(id, start, end);
    }

    /*查询表sale_product和表sale_order,根据特产id和时间区间,查询销售退货数量*/
    public Integer saleQuaSumRetByProIdAndDateBetween(Integer id,Date start,Date end){
        return saleProDao.saleQuaSumRetByProIdAndDateBetween(id,start,end);
    }
}
