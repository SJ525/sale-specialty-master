package com.hsj.service;

import com.hsj.dao.SaleProDao;
import com.hsj.entity.SaleProduct;
import com.hsj.util.ChangToPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/*
 *@ClassName SaleProService
 *@Description 销售特产服务器
 *@Author hsj
 *Date 2020/3/13 16:44
 */
@Service
@Transactional
public class SaleProService {
    @Autowired
    SaleProDao saleProDao;

    /*查询所有SaleProduct,返回类型是Page*/
    public Page<SaleProduct> findAllWithPage(Pageable pageable){
        return saleProDao.findAll(pageable);
    }

    /*查询所有SaleProduct,返回类型是List*/
    public List<SaleProduct> findAllWithList(){return saleProDao.findAll();}

    /*特产名称、所属销售订单都不为空*/
    public Page<SaleProduct> findBySaleProIdAndSaleOrderId(Integer id1, Integer id2,Pageable pageable){
        List<SaleProduct> list =saleProDao.findBySaleProIdAndSaleOrderId(id1,id2);
        return packToPage(list, pageable);
    }

    /*特产名称不为空、所属销售订单为空*/
    public Page<SaleProduct> findBySaleProId(Integer id,Pageable pageable){
        List<SaleProduct> list =saleProDao.findBySaleProId(id);
        return packToPage(list, pageable);
    }

    /*特产名称为空、所属销售订单不为空*/
    public Page<SaleProduct> findBySaleOrderId(Integer id,Pageable pageable){
        List<SaleProduct> list =saleProDao.findBySaleOrderId(id);
        return packToPage(list, pageable);
    }

    /*销售数量区间、销售价格区间都不为空*/
    public Page<SaleProduct> findByQuantityBetweenAndPriceBetween(Integer sq, Integer eq, BigDecimal sp, BigDecimal ep,Pageable pageable){
        List<SaleProduct> list =saleProDao.findByQuantityBetweenAndPriceBetween(sq,eq,sp,ep);
        return packToPage(list, pageable);
    }

    /*销售数量区间不为空、销售价格区间为空*/
    public Page<SaleProduct>  findByQuantityBetween(Integer sq,Integer eq,Pageable pageable){
        List<SaleProduct> list =saleProDao.findByQuantityBetween(sq,eq);
        return packToPage(list, pageable);
    }

    /*销售数量区间不为空、销售价格区间为空*/
    public Page<SaleProduct> findByPriceBetween(BigDecimal sp,BigDecimal ep,Pageable pageable){
        List<SaleProduct> list =saleProDao.findByPriceBetween(sp,ep);
        return packToPage(list, pageable);
    }

    /*将List数据封装成Page数据*/
    public Page<SaleProduct> packToPage(List<SaleProduct> list, Pageable pageable){
        //调用util包里面的ChangToPage类
        ChangToPage<SaleProduct> changToPage=new ChangToPage<>();
        return changToPage.getPage(list,pageable);
    }

    /*为sale_product表添加数据*/
    public void addSaleProduct(SaleProduct saleProduct){
        saleProDao.save(saleProduct);
    }

    /*根据表sale_product的sale_pro_id和sale_order的type='正常订单',查询SUM(b.quantity)*/
    public Integer findSaleQuantitySumAll(Integer id){return saleProDao.findSaleQuantitySumAll(id);}

    /*根据表sale_product的sale_pro_id和sale_order的type='正常订单',查询SUM(b.quantity)*/
    public Integer findSaleQuantitySumRet(Integer id){return saleProDao.findSaleQuantitySumRet(id);}

    /*根据表sale_product的sale_pro_id和sale_order的type='正常订单',查询SUM(b.quantity)*/
    public Integer findSaleQuantitySumNor(Integer id){return saleProDao.findSaleQuantitySumNor(id);}

    /*根据saleOrderId,找出sale_product表中与saleOrderId相关的数据*/
    public List<SaleProduct> findSaleProductsBySaleOrderId(Integer id){
        return saleProDao.findSaleProductsBySaleOrderId(id);
    }

    /*根据SaleOrderId,将sale_product表中的price汇总*/
    public BigDecimal findPriceSumBySaleOrderId(Integer id){
        return saleProDao.findPriceSumBySaleOrderId(id);
    }

    /*根据saleId查询数据*/
    public SaleProduct findBySaleId(Integer id){return saleProDao.findBySaleId(id);}

    /*修改SaleProduct数据*/
    public void modSaleProduct(SaleProduct saleProduct){saleProDao.save(saleProduct);}

    /*根据saleOrderId删除数据*/
    public void delBySaleOrderId(Integer id){
        saleProDao.deleteBySaleOrderId(id);
    }

    /*根据saleOrderId删除数据*/
    public void delBySaleId(Integer id){
        saleProDao.deleteById(id);
    }

    /*根据saleProId查找数据*/
    public List<SaleProduct> findSaleProductsNorBySaleProId(Integer id){return saleProDao.findSaleProductsNorBySaleProId(id);}
    public List<SaleProduct> findSaleProductsBySaleProId(Integer id){return saleProDao.findSaleProductsBySaleProId(id);}

    /*根据sale_id查询sale_pro_id*/
    public Integer findSaleProIdBySaleId(Integer id){return saleProDao.findSaleProIdBySaleId(id);}

    /*findBuyProductsByProId*/
    public List<SaleProduct> findSaleProductsByProId(Integer id){return saleProDao.findSaleProductsByProId(id);}

    /*根据saleOrderId删除数据*/
    public void deleteBySaleProId(Integer id){saleProDao.deleteBySaleProId(id);}

    public List<Integer> findSaleProIdsBySaleOrderId(Integer id){return saleProDao.findSaleProIdsBySaleOrderId(id);}
}
