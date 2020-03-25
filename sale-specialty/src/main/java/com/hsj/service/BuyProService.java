package com.hsj.service;

import com.hsj.dao.BuyProDao;
import com.hsj.entity.BuyProduct;
import com.hsj.util.ChangToPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/*
 *@ClassName BuyProService
 *@Description 采购特产管理-服务器类
 *@Author hsj
 *Date 2020/3/9 12:25
 */
@Service
@Transactional
public class BuyProService {
    @Autowired
    BuyProDao buyProDao;

    /*根据buyOrderId删除数据*/
    public void deleteByBuyOrderId(Integer id){
        buyProDao.deleteByBuyOrderId(id);
    }

    /*查询表buy_order的所有数据，以Page类型返回*/
    public Page<BuyProduct> findAllWithPage(Pageable pageable){
        return buyProDao.findAll(pageable);
    }

    /*查询表buy_order的所有数据，以List类型返回*/
    public List<BuyProduct> findAllWithList(){
        return buyProDao.findAll();
    }

    /*通过buyId查询数据，返回BuyProduct类型*/
    public BuyProduct findByBuyId(Integer id){
        return buyProDao.findByBuyId(id);
    }

    /*联合外键查询：通过表buy_product的buy_sup_id查询表supplier的sup_name*/
    public String findSupNameByBuySupId(Integer id){
        return buyProDao.findSupNameByBuySupId(id);
    }

    /*通过buyProId、buyOrderId和buySupId查询数据*/
    public Page<BuyProduct> findByBuyProIdAndBuyOrderIdAndBuySupId(Integer id1, Integer id2, Integer id3,Pageable pageable){
        List<BuyProduct> list = buyProDao.findByBuyProIdAndBuyOrderIdAndBuySupId(id1,id2,id3);
        return packToPage(list,pageable);
    }

    /*通过buyProId查询数据，返回Page数据类型*/
    public Page<BuyProduct> findByBuyProId(Integer id,Pageable pageable){
        List<BuyProduct> list = buyProDao.findByBuyProId(id);
        return packToPage(list,pageable);
    }


    /*通过buyOrderId查询数据*/
    public Page<BuyProduct> findByBuyOrderId(Integer id,Pageable pageable){
        List<BuyProduct> list = buyProDao.findByBuyOrderId(id);
        return packToPage(list,pageable);
    }
    public List<BuyProduct> findByBuyOrderId(Integer id){
        List<BuyProduct> list = buyProDao.findByBuyOrderId(id);
        return list;
    }

    /*通过buySupId查询数据*/
    public Page<BuyProduct> findByBuySupId(Integer id,Pageable pageable){
        List<BuyProduct> list = buyProDao.findByBuySupId(id);
        return packToPage(list,pageable);
    }

    /*通过buyProId和buyOrderId查询数据*/
    public Page<BuyProduct> findByBuyProIdAndBuyOrderId(Integer id1,Integer id2,Pageable pageable){
        List<BuyProduct> list = buyProDao.findByBuyProIdAndBuyOrderId(id1,id2);
        return packToPage(list,pageable);
    }

    /*通过buyProId和buySupId查询数据*/
    public Page<BuyProduct> findByBuyProIdAndBuySupId(Integer id1,Integer id2,Pageable pageable){
        List<BuyProduct> list = buyProDao.findByBuyProIdAndBuySupId(id1,id2);
        return packToPage(list,pageable);
    }

    /*通过buyOrderId和buySupId查询数据*/
    public Page<BuyProduct> findByBuyOrderIdAndBuySupId(Integer id1,Integer id2,Pageable pageable){
        List<BuyProduct> list = buyProDao.findByBuyOrderIdAndBuySupId(id1,id2);
        return packToPage(list,pageable);
    }

    /*区间查询：采购数量不为空，采购价格不为空*/
    public Page<BuyProduct> findByQuantityBetweenAndPriceBetween(Integer a, Integer b, BigDecimal i, BigDecimal j, Pageable pageable){
        List<BuyProduct> list = buyProDao.findByQuantityBetweenAndPriceBetween(a,b,i,j);
        return packToPage(list,pageable);
    }

    /*区间查询：采购数量不为空，采购价格为空*/
    public Page<BuyProduct>findByQuantityBetween(Integer a,Integer b, Pageable pageable){
        List<BuyProduct> list = buyProDao.findByQuantityBetween(a,b);
        return packToPage(list,pageable);
    }

    /*区间查询：采购数量为空，采购价格不为空*/
    public Page<BuyProduct> findByPriceBetween(BigDecimal i,BigDecimal j, Pageable pageable){
        List<BuyProduct> list = buyProDao.findByPriceBetween(i,j);
        return packToPage(list,pageable);
    }

    /*将List数据封装成Page数据*/
    public Page<BuyProduct> packToPage(List<BuyProduct> list, Pageable pageable){
        //调用util包里面的ChangToPage类
        ChangToPage<BuyProduct> changToPage=new ChangToPage<>();
        return changToPage.getPage(list,pageable);
    }

    /*为buy_product表添加数据*/
    public void addBuyProduct(BuyProduct buyProduct){
        buyProDao.save(buyProduct);
    }

    /*根据表buy_product的buy_pro_id查询SUM(b.quantity)*/
    public Integer findBuyQuantitySumAll(Integer id){return buyProDao.findBuyQuantitySumAll(id);}

    /*根据表buy_product的buy_pro_id和pur_order的type='退货订单',查询SUM(b.quantity)*/
    public Integer findBuyQuantitySumRet(Integer id){return buyProDao.findBuyQuantitySumRet(id);}

    /*根据表buy_product的buy_pro_id和pur_order的type='正常订单',查询SUM(b.quantity)*/
    public Integer findBuyQuantitySumNor(Integer id){return buyProDao.findBuyQuantitySumNor(id);}

    /*根据BuyOrderId,找出buy_product表中与根据BuyOrderId相关的数据*/
    public List<BuyProduct> findBuyProductsByBuyOrderId(Integer id){
        return buyProDao.findBuyProductsByBuyOrderId(id);
    }

    /*根据BuyOrderId,将buy_product表中的price汇总*/
    public BigDecimal findPriceSumByBuyOrderId(Integer id){
        return buyProDao.findPriceSumByBuyOrderId(id);
    }

    /*修改buy_product表中的数据*/
    public void modBuyProduct(BuyProduct buyProduct){
        buyProDao.save(buyProduct);
    }

    /*根据buyId,删除buy_product表中的数据*/
    public void delBuyProduct(Integer id){
        buyProDao.deleteById(id);
    }

    public List<Integer> findNormalTypeBuyProIds(){
        return buyProDao.findNormalTypeBuyProIds();
    }

    public List<BuyProduct> findBuyProductsByProId(Integer id){return buyProDao.findBuyProductsByProId(id);}

    /*通过buy_id找到buy_pro_id*/
    public Integer findBuyProIdByBuyId(Integer id){return buyProDao.findBuyProIdByBuyId(id);}

    /*通过buy_id找到relation*/
    public String findRelationByBuyId(Integer id){return buyProDao.findRelationByBuyId(id);}

    /*根据采购特产id修改关联性relation*/
    public void updateRelationByBuyProId(String relation,Integer id){buyProDao.updateRelationByBuyProId(relation,id);}

    public List<Integer> findAllProIds(){return buyProDao.findAllProIds();}

    public List<Integer> findBuyProIdsByBuyOrderId(Integer id){return buyProDao.findBuyProIdsByBuyOrderId(id);}
}
