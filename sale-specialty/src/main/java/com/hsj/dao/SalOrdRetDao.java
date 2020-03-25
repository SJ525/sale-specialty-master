package com.hsj.dao;

import com.hsj.entity.SaleOrderReturn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

/*销售退货订单-JPA接口*/
public interface SalOrdRetDao  extends JpaRepository<SaleOrderReturn, Integer> {
    /*根据returnId查询数据*/
    public SaleOrderReturn findByReturnId(Integer id);

    /*根据returnId查询orderId*/
    @Query(value = "SELECT p.orderId FROM SaleOrderReturn p WHERE p.returnId=?1")
    public Integer findOrderIdByReturnId(Integer id);

    /*根据returnId查询orderNumber*/
    @Query(value = "SELECT p.orderNumber FROM SaleOrderReturn p WHERE p.returnId=?1")
    public String findOrderNumberByReturnId(Integer id);

    /*采购退货订单号、开始退货日期区间、退货状态都不为空*/
    public List<SaleOrderReturn> findByOrderIdAndStartDateBetweenAndStatus(Integer id, Date start, Date end, String status);

    /*采购退货订单号不为空、开始退货日期区间为空、退货状态为空*/
    public List<SaleOrderReturn> findByOrderId(Integer id);

    /*采购退货订单号为空、开始退货日期区间不为空、退货状态为空*/
    public List<SaleOrderReturn> findByStartDateBetween(Date start, Date end);

    /*采购退货订单号为空、开始退货日期区间为空、退货状态不为空*/
    public List<SaleOrderReturn> findByStatus(String status);

    /*采购退货订单号不为空、开始退货日期区间不为空、退货状态为空*/
    public List<SaleOrderReturn> findByOrderIdAndStartDateBetween(Integer id, Date start, Date end);

    /*采购退货订单号不为空、开始退货日期区间为空、退货状态不为空*/
    public List<SaleOrderReturn> findByOrderIdAndStatus(Integer id, String status);

    /*采购退货订单号为空、开始退货日期区间不为空、退货状态不为空*/
    public List<SaleOrderReturn> findByStartDateBetweenAndStatus(Date start, Date end, String status);

    @Modifying
    @Query(value = "DELETE FROM sale_order_return  WHERE order_id=?",nativeQuery = true)
    public void delSaleOrderReturnByOrderId(Integer id);
}
