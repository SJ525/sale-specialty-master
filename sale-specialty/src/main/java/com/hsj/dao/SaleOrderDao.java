package com.hsj.dao;


import com.hsj.entity.SaleOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

/*销售订单-JPA接口*/
public interface SaleOrderDao extends JpaRepository<SaleOrder, Integer> {
    /*查询所有正常的销售订单*/
    @Query(value = "SELECT * FROM sale_order WHERE type='正常订单'",nativeQuery = true)
    public List<SaleOrder> findAllNormal();
    
    /*当订单编号不为空、销售员为空(不为空)、客户为空(不为空)*/
    public List<SaleOrder> findByOrderId(Integer id);

    /*当订单编号为空、销售员不为空、客户为空*/
    public List<SaleOrder> findBySaleSpId(Integer id);

    /*当订单编号为空、销售员为空、客户不为空*/
    public List<SaleOrder> findBySaleCusId(Integer id);

    /*当订单编号为空、销售员不为空、客户不为空*/
    public List<SaleOrder> findBySaleSpIdAndSaleCusId(Integer id1, Integer id2);

    /*客户支付方式、销售时间、订单类型都不为空*/
    public List<SaleOrder> findByPayWayAndDateBetweenAndType(String payWay, Date startDate, Date endDate, String Type);

    /*客户支付方式为空、销售时间不为空，订单类型为空*/
    public List<SaleOrder> findByDateBetween(Date startDate, Date endDate);

    /*客户支付方式不为空、销售时间为空，订单类型为空*/
    public List<SaleOrder> findByPayWay(String payWay);

    /*客户支付方式为空、销售时间为空，订单类型不为空*/
    public List<SaleOrder> findByType(String Type);

    /*客户支付方式不为空、销售时间不为空，订单类型为空*/
    public List<SaleOrder> findByPayWayAndDateBetween(String payWay, Date startDate, Date endDate);

    /*客户支付方式不为空、销售时间为空，订单类型不为空*/
    public List<SaleOrder> findByPayWayAndType(String payWay, String Type);

    /*客户支付方式为空、销售时间不为空，订单类型不为空*/
    public List<SaleOrder> findByDateBetweenAndType(Date startDate, Date endDate, String Type);

    /*根据order_id查询type*/
    @Query(value = "SELECT s.type FROM SaleOrder s WHERE s.orderId=?1")
    public String findOrderTypeByOrderId(Integer id);

    /*根据order_id查询order_number*/
    @Query(value = "SELECT s.orderNumber FROM SaleOrder s WHERE  s.orderId=?1")
    public String findOrderNumberByOrderId(Integer id);

    /*根据order_id查询采购日期date*/
    @Query(value = "SELECT p.date FROM SaleOrder p WHERE p.orderId=?1")
    public Date findDateByOrderId(Integer id);

    /*根据order_id修改type*/
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE sale_order p SET p.type=? WHERE p.order_id=?",nativeQuery = true)
    public void updateTypeByOrderId(String type, Integer id);

    @Query(value = "SELECT a.pro_id FROM product a JOIN sale_product b ON a.pro_id=b.sale_pro_id JOIN sale_order c ON b.sale_order_id=c.order_id WHERE c.order_id=?",nativeQuery = true)
    public List<Integer> findProIdsByOrderId(Integer id);

    /*根sale_sp_id查询数据*/
    @Query(value = "SELECT * FROM sale_order WHERE sale_sp_id=?",nativeQuery = true)
    public List<SaleOrder> findSalesOrdersBySaleSpId(Integer id);

    /*根据order_id查询saleSpId*/
    @Query(value = "SELECT p.saleSpId FROM SaleOrder p WHERE p.orderId=?1")
    public Integer findSaleSpIdByOrderId(Integer id);

    /*根据sale_cus_id查询数据*/
    @Query(value = "SELECT * FROM sale_order WHERE sale_cus_id=?",nativeQuery = true)
    public List<SaleOrder> findSalesOrdersBySaleCusId(Integer id);

    /*根据order_id查询saleCusId*/
    @Query(value = "SELECT p.saleCusId FROM SaleOrder p WHERE p.orderId=?1")
    public Integer findSaleCusIdByOrderId(Integer id);

    /*查询表sale_order的最大日期*/
    @Query(value="SELECT MAX(date) FROM sale_order",nativeQuery=true)
    public Date findSaleOrderMaxDate();
}
