package com.hsj.dao;

import com.hsj.entity.PurOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

/*采购订单-JPA接口*/
public interface PurOrderDao extends JpaRepository<PurOrder, Integer> {
   /*查询所有正常的采购订单*/
    @Query(value = "SELECT * FROM pur_order WHERE type='正常订单'",nativeQuery = true)
    public List<PurOrder> findAllNormal();

    /*外键查询:通过表pur_order的pur_man_id查询表purchaser的pur_name*/
    @Query(value = "SELECT DISTINCT b.pur_name FROM pur_order a JOIN purchaser b ON a.pur_man_id=b.pur_id WHERE a.pur_man_id=?",nativeQuery = true)
    public String findPurchaserPurName(Integer id);

    /*外键查询:通过表pur_order的pur_ware_id查询表warehouse的ware_name*/
    @Query(value = "SELECT DISTINCT w.ware_name FROM pur_order p JOIN warehouse w ON p.pur_ware_id=w.ware_id WHERE p.pur_ware_id=?",nativeQuery = true)
    public String findWarehouseWareName(Integer id);

    /*当订单编号不为空、采购员为空(不为空)、所入仓库为空(不为空)*/
    public List<PurOrder> findByOrderId(Integer id);

    /*当订单编号为空、采购员不为空、所入仓库为空*/
    public List<PurOrder> findByPurManId(Integer id);

    /*当订单编号为空、采购员为空、所入仓库不为空*/
    public List<PurOrder> findByPurWareId(Integer id);

    /*当订单编号为空、采购员不为空、所入仓库不为空*/
    public List<PurOrder> findByPurManIdAndPurWareId(Integer id1, Integer id2);

    /*付款方式、采购日期、订单类型都不为空*/
    public List<PurOrder> findByPayWayAndDateBetweenAndType(String payWay, Date startDate, Date endDate, String Type);

    /*付款方式为空、采购日期不为空，订单类型为空*/
    public List<PurOrder> findByDateBetween(Date startDate, Date endDate);

    /*付款方式不为空、采购日期为空，订单类型为空*/
    public List<PurOrder> findByPayWay(String payWay);

    /*付款方式为空、采购日期为空，订单类型不为空*/
    public List<PurOrder> findByType(String Type);

    /*付款方式不为空、采购日期不为空，订单类型为空*/
    public List<PurOrder> findByPayWayAndDateBetween(String payWay, Date startDate, Date endDate);

    /*付款方式不为空、采购日期为空，订单类型不为空*/
    public List<PurOrder> findByPayWayAndType(String payWay, String Type);

    /*付款方式为空、采购日期不为空，订单类型不为空*/
    public List<PurOrder> findByDateBetweenAndType(Date startDate, Date endDate, String Type);

    /*通过order_id查询order_number*/
    @Query(value = "SELECT p.orderNumber FROM PurOrder p WHERE p.orderId=?1")
    public String findOrderNumberByOrderId(Integer id);

    /*根据order_id查询type*/
    @Query(value = "SELECT p.type FROM PurOrder p WHERE p.orderId=?1")
    public String findOrderTypeByOrderId(Integer id);

    /*根据order_id修改type*/
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE pur_order p SET p.type=? WHERE p.order_id=?",nativeQuery = true)
    public void updateTypeByOrderId(String type, Integer id);

    /*根据order_id查询采购日期date*/
    @Query(value = "SELECT p.date FROM PurOrder p WHERE p.orderId=?1")
    public Date findDateByOrderId(Integer id);

    @Query(value = "SELECT a.pro_id FROM product a JOIN buy_product b ON a.pro_id=b.buy_pro_id JOIN pur_order c ON b.buy_order_id=c.order_id WHERE c.order_id=?",nativeQuery = true)
    public List<Integer> findProIdsByOrderId(Integer id);

    /*根pur_man_id查询数据*/
    @Query(value = "SELECT * FROM pur_order WHERE pur_man_id=?",nativeQuery = true)
    public List<PurOrder> findPurOrdersByPurManId(Integer id);

   /*根据order_id查询purManId*/
   @Query(value = "SELECT p.purManId FROM PurOrder p WHERE p.orderId=?1")
   public Integer findPurManIdByOrderId(Integer id);

   /*根pur_ware_id查询数据*/
   @Query(value = "SELECT * FROM pur_order WHERE pur_ware_id=?",nativeQuery = true)
   public List<PurOrder> findPurOrdersByPurWareId(Integer id);

   /*根据order_id查询purWareId*/
   @Query(value = "SELECT p.purWareId FROM PurOrder p WHERE p.orderId=?1")
   public Integer findPurWareIdByOrderId(Integer id);

   /*查询表pur_order的最小日期*/
   @Query(value="SELECT MIN(date) FROM pur_order",nativeQuery=true)
   public Date findPurOrderMinDate();

}
