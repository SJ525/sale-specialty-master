package com.hsj.dao;

import com.hsj.entity.SaleProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/*销售特产-JPA接口*/
public interface SaleProDao extends JpaRepository<SaleProduct, Integer>{

    /*特产名称、所属销售订单都不为空*/
    public List<SaleProduct> findBySaleProIdAndSaleOrderId(Integer id1, Integer id2);

    /*特产名称不为空、所属销售订单为空*/
    public List<SaleProduct> findBySaleProId(Integer id);

    /*特产名称为空、所属销售订单不为空*/
    public List<SaleProduct> findBySaleOrderId(Integer id);

    /*销售数量区间、销售价格区间都不为空*/
    public List<SaleProduct> findByQuantityBetweenAndPriceBetween(Integer sq, Integer eq, BigDecimal sp, BigDecimal ep);

    /*销售数量区间不为空、销售价格区间为空*/
    public List<SaleProduct>  findByQuantityBetween(Integer sq, Integer eq);

    /*销售数量区间不为空、销售价格区间为空*/
    public List<SaleProduct> findByPriceBetween(BigDecimal sp, BigDecimal ep);

    /*根据表sale_product的sale_pro_id和sale_order的type='正常订单',查询SUM(b.quantity)*/
    @Query(value = "SELECT SUM(b.quantity) FROM sale_product b WHERE b.sale_pro_id=?",nativeQuery = true)
    public Integer findSaleQuantitySumAll(Integer id);

    /*根据表sale_product的sale_pro_id和sale_order的type='正常订单',查询SUM(b.quantity)*/
    @Query(value = "SELECT SUM(b.quantity) FROM sale_product b JOIN sale_order s ON b.sale_order_id=s.order_id WHERE b.sale_pro_id=? AND s.type='退货订单'",nativeQuery = true)
    public Integer findSaleQuantitySumRet(Integer id);

    /*根据表sale_product的sale_pro_id和sale_order的type='正常订单',查询SUM(b.quantity)*/
    @Query(value = "SELECT SUM(b.quantity) FROM sale_product b JOIN sale_order s ON b.sale_order_id=s.order_id WHERE b.sale_pro_id=? AND s.type='正常订单'",nativeQuery = true)
    public Integer findSaleQuantitySumNor(Integer id);

    /*根据saleOrderId,找出sale_product表中与saleOrderId相关的数据*/
    @Query(value = "SELECT b.* FROM sale_product b WHERE b.sale_order_id=?",nativeQuery = true)
    public List<SaleProduct> findSaleProductsBySaleOrderId(Integer id);

    /*根据SaleOrderId,将sale_product表中的price汇总*/
    @Query(value = "SELECT SUM(b.price) FROM sale_product b WHERE b.sale_order_id=?",nativeQuery = true)
    public BigDecimal findPriceSumBySaleOrderId(Integer id);

    /*根据saleId查询数据*/
    public  SaleProduct findBySaleId(Integer id);

    /*根据saleOrderId删除数据*/
    @Modifying
    @Query("delete from SaleProduct b where b.saleOrderId=?1")
    public void deleteBySaleOrderId(Integer id);

    @Query(value = "SELECT DISTINCT b.sale_pro_id FROM sale_product b JOIN sale_order p ON b.sale_order_id=p.order_id WHERE p.type='正常订单'",nativeQuery = true)
    public List<Integer> findNormalTypeSaleProIds();

    /*根据saleProId查找数据*/
    @Query(value = "SELECT * FROM sale_product s join sale_order p on s.sale_order_id=p.order_id WHERE s.sale_pro_id=? AND p.type='正常订单'",nativeQuery = true)
    public List<SaleProduct> findSaleProductsNorBySaleProId(Integer id);

    @Query(value = "SELECT * FROM sale_product s WHERE s.sale_pro_id=?",nativeQuery = true)
    public List<SaleProduct> findSaleProductsBySaleProId(Integer id);

    /*根据sale_id查询sale_pro_id*/
    @Query(value = "SELECT p.saleProId FROM SaleProduct p WHERE p.saleId=?1")
    public Integer findSaleProIdBySaleId(Integer id);

    /*findBuyProductsByProId*/
    @Query(value = "SELECT b.* FROM sale_product b WHERE b.sale_pro_id=?",nativeQuery = true)
    public List<SaleProduct> findSaleProductsByProId(Integer id);

    /*根据saleOrderId删除数据*/
    @Modifying
    @Query("delete from SaleProduct b where b.saleProId=?1")
    public void deleteBySaleProId(Integer id);

    @Query(value = "SELECT DISTINCT b.sale_pro_id FROM sale_product b where b.sale_order_id=?",nativeQuery = true)
    public List<Integer> findSaleProIdsBySaleOrderId(Integer id);

    /*查询表sale_product,根据特产id，查询销售支出*/
    @Query(value="SELECT SUM(s.price) FROM sale_product s WHERE s.sale_pro_id=?",nativeQuery=true)
    public BigDecimal findPriceSumBySaleProId(Integer id);

    /*查询表sale_product和表sale_order,根据特产id和查询日期区间，查询销售数量*/
    @Query(value="SELECT SUM(b.quantity) FROM sale_product b JOIN sale_order p ON b.sale_order_id=p.order_id WHERE b.sale_pro_id=? AND p.date between ? AND ?",nativeQuery=true)
    public Integer saleQuaSumByIdAndDateBetween(Integer id, Date start, Date end);

    /*查询表sale_product和表sale_order,根据特产id和查询日期区间，查询销售收入*/
    @Query(value="SELECT SUM(b.price) FROM sale_product b JOIN sale_order p ON b.sale_order_id=p.order_id WHERE b.sale_pro_id=? AND p.date between ? AND ?",nativeQuery=true)
    public BigDecimal salePriceSumByIdAndDateBetween(Integer id, Date start, Date end);

    /*查询表sale_product和表sale_order,根据特产id和时间区间,查询销售退货数量*/
    @Query(value="SELECT SUM(b.quantity) FROM sale_product b JOIN sale_order s ON b.sale_order_id=s.order_id WHERE b.sale_pro_id=? AND s.type='退货订单' AND s.date between ? AND ?",nativeQuery=true)
    public Integer saleQuaSumRetByProIdAndDateBetween(Integer id, Date start, Date end);

    /*查询表sale_product,查询正常订单的销售收入*/
    @Query(value="SELECT SUM(s.price) FROM sale_product s JOIN sale_order p ON s.sale_order_id=p.order_id WHERE p.type='正常订单'",nativeQuery=true)
    public BigDecimal salePriceSumNor();

    /*查询表sale_product和表sale_order,根据查询日期区间，查询正常订单的销售收入*/
    @Query(value="SELECT SUM(s.price) FROM sale_product s JOIN sale_order p ON s.sale_order_id=p.order_id WHERE p.type='正常订单' AND p.date between ? AND ?",nativeQuery=true)
    public BigDecimal salePriceSumNorByDateBetween(Date start, Date end);
}
