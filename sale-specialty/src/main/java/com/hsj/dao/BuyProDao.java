package com.hsj.dao;

import com.hsj.entity.BuyProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/*采购特产特产-JPA操作接口*/
public interface BuyProDao extends JpaRepository<BuyProduct, Integer> {
    /*通过buyId查询数据，返回BuyProduct类型*/
    public BuyProduct findByBuyId(Integer id);

    /*根据buyOrderId删除数据*/
    @Modifying
    @Query("delete from BuyProduct b where b.buyOrderId=?1")
    public void deleteByBuyOrderId(Integer id);

    /*联合外键查询：通过表buy_product的buy_sup_id查询表supplier的sup_name*/
    @Query(value = "SELECT DISTINCT s.sup_name FROM buy_product b JOIN supplier s ON b.buy_sup_id=s.sup_id WHERE b.buy_sup_id=?",nativeQuery = true)
    public String findSupNameByBuySupId(Integer id);

    /*通过buyProId、buyOrderId和buySupId查询数据*/
    public List<BuyProduct> findByBuyProIdAndBuyOrderIdAndBuySupId(Integer id1, Integer id2, Integer id3);

    /*通过buyProId查询数据*/
    public List<BuyProduct> findByBuyProId(Integer id);

    /*通过buyOrderId查询数据*/
    public List<BuyProduct> findByBuyOrderId(Integer id);

    /*通过buySupId查询数据*/
    public List<BuyProduct> findByBuySupId(Integer id);

    /*通过buyProId和buyOrderId查询数据*/
    public List<BuyProduct> findByBuyProIdAndBuyOrderId(Integer id1, Integer id2);

    /*通过buyProId和buySupId查询数据*/
    public List<BuyProduct> findByBuyProIdAndBuySupId(Integer id1, Integer id2);

    /*通过buyOrderId和buySupId查询数据*/
    public List<BuyProduct> findByBuyOrderIdAndBuySupId(Integer id1, Integer id2);

    /*区间查询：采购数量不为空，采购价格不为空*/
    public List<BuyProduct> findByQuantityBetweenAndPriceBetween(Integer a, Integer b, BigDecimal i, BigDecimal j);

    /*区间查询：采购数量不为空，采购价格为空*/
    public List<BuyProduct>findByQuantityBetween(Integer a, Integer b);

    /*区间查询：采购数量为空，采购价格不为空*/
    public List<BuyProduct> findByPriceBetween(BigDecimal i, BigDecimal j);

    /*根据表buy_product的buy_pro_id查询SUM(b.quantity)*/
    @Query(value = "SELECT SUM(b.quantity) FROM buy_product b WHERE b.buy_pro_id=?",nativeQuery = true)
    public Integer findBuyQuantitySumAll(Integer id);

    /*根据表buy_product的buy_pro_id和pur_order的type='退货订单',查询SUM(b.quantity)*/
    @Query(value = "SELECT SUM(b.quantity) FROM buy_product b JOIN pur_order p ON b.buy_order_id=p.order_id WHERE b.buy_pro_id=? AND p.type='退货订单'",nativeQuery = true)
    public Integer findBuyQuantitySumRet(Integer id);

    /*根据表buy_product的buy_pro_id和pur_order的type='正常订单',查询SUM(b.quantity)*/
    @Query(value = "SELECT SUM(b.quantity) FROM buy_product b JOIN pur_order p ON b.buy_order_id=p.order_id WHERE b.buy_pro_id=? AND p.type='正常订单'",nativeQuery = true)
    public Integer findBuyQuantitySumNor(Integer id);

    /*根据buyOrderId,找出buy_product表中与buyOrderId相关的数据*/
    @Query(value = "SELECT b.* FROM buy_product b WHERE b.buy_order_id=?",nativeQuery = true)
    public List<BuyProduct> findBuyProductsByBuyOrderId(Integer id);

    /*根据BuyOrderId,将buy_product表中的price汇总*/
    @Query(value = "SELECT SUM(b.price) FROM buy_product b WHERE b.buy_order_id=?",nativeQuery = true)
    public BigDecimal findPriceSumByBuyOrderId(Integer id);

    /*findNormalTypeBuyProIds*/
    @Query(value = "SELECT DISTINCT b.buy_pro_id FROM buy_product b JOIN pur_order p ON b.buy_order_id=p.order_id WHERE p.type='正常订单'",nativeQuery = true)
    public List<Integer> findNormalTypeBuyProIds();

    /*通过buy_id找到buy_pro_id*/
    @Query(value = "SELECT b.buyProId FROM BuyProduct b WHERE b.buyId=?1")
    public Integer findBuyProIdByBuyId(Integer id);

    /*根据采购特产id修改关联性relation*/
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE buy_product p SET p.relation=? WHERE p.buy_pro_id=?",nativeQuery = true)
    public void updateRelationByBuyProId(String relation, Integer id);

    /*通过buy_id找到relation*/
    @Query(value = "SELECT b.relation FROM BuyProduct b WHERE b.buyId=?1")
    public String findRelationByBuyId(Integer id);

    /*findBuyProductsByProId*/
    @Query(value = "SELECT b.* FROM buy_product b WHERE b.buy_pro_id=?",nativeQuery = true)
    public List<BuyProduct> findBuyProductsByProId(Integer id);

    @Query(value = "SELECT DISTINCT buy_pro_id FROM buy_product",nativeQuery = true)
    public List<Integer> findAllProIds();

    @Query(value = "SELECT DISTINCT b.buy_pro_id FROM buy_product b where b.buy_order_id=?",nativeQuery = true)
    public List<Integer> findBuyProIdsByBuyOrderId(Integer id);

    /*查询表buy_product，根据特产id，查询采购支出*/
    @Query(value="SELECT SUM(b.price) FROM buy_product b WHERE b.buy_pro_id=?",nativeQuery=true)
    public BigDecimal findPriceSumByBuyProId(Integer id);

    /*查询表buy_product和表pur_order，根据特产id和查询日期区间，查询采购数量*/
    @Query(value="SELECT SUM(b.quantity) FROM buy_product b JOIN pur_order p ON b.buy_order_id=p.order_id WHERE b.buy_pro_id=? AND p.date between ? AND ?",nativeQuery=true)
    public Integer buyQuaSumByIdAndDateBetween(Integer id, Date start, Date end);

    /*查询buy_product和表pur_order，根据特产id和查询日期区间，查询采购支出*/
    @Query(value="SELECT SUM(b.price) FROM buy_product b JOIN pur_order p ON b.buy_order_id=p.order_id WHERE b.buy_pro_id=? AND p.date between ? AND ?",nativeQuery=true)
    public BigDecimal  buyPriceSumByIdAndDateBetween(Integer id, Date start, Date end);

    /*查询表buy_product和表pur_order,根据特产id和时间区间查询,采购退货数量*/
    @Query(value="SELECT SUM(b.quantity) FROM buy_product b JOIN pur_order p ON b.buy_order_id=p.order_id WHERE b.buy_pro_id=? AND p.type='退货订单' AND p.date between ? AND ?",nativeQuery=true)
    public Integer purQuaSumRetByProIdAndDateBetween(Integer id, Date start, Date end);

    /*查询表buy_product和表pur_order，查询正常订单的采购支出*/
    @Query(value="SELECT SUM(b.price) FROM buy_product b JOIN pur_order p ON b.buy_order_id=p.order_id WHERE p.type='正常订单'",nativeQuery=true)
    public BigDecimal buyPriceSumNor();

    /*查询buy_product和表pur_order，根据查询日期区间，查询正常订单的采购支出*/
    @Query(value="SELECT SUM(b.price) FROM buy_product b JOIN pur_order p ON b.buy_order_id=p.order_id WHERE p.type='正常订单' AND p.date between ? AND ?",nativeQuery=true)
    public BigDecimal  buyPriceSumNorByDateBetween(Date start, Date end);
}
