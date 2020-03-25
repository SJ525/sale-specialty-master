package com.hsj.service;

import com.hsj.dao.SalOrdRetDao;
import com.hsj.entity.SaleOrderReturn;
import com.hsj.util.ChangToPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/*
 *@ClassName SalOrdRetService
 *@Description 销售退货订单服务器
 *@Author hsj
 *Date 2020/3/13 16:45
 */
@Service
@Transactional
public class SalOrdRetService {
    @Autowired
    SalOrdRetDao salOrdRetDao;
    

    /*根据returnId查询数据*/
    public SaleOrderReturn findByReturnId(Integer id){
        return salOrdRetDao.findByReturnId(id);
    }

    /*查询表sale_order_return所有数据，以Page类型返回*/
    public Page<SaleOrderReturn> findAllWithPage(Pageable pageable){
        return salOrdRetDao.findAll(pageable);
    }

    /*查询表sale_order_return所有数据，以List类型返回*/
    public List<SaleOrderReturn> findAllWithList(){
        return salOrdRetDao.findAll();
    }

    /*销售退货订单号、开始退货日期区间、退货状态都不为空*/
    public Page<SaleOrderReturn> findByOrderIdAndStartDateBetweenAndStatus(Integer id, Date start,
                                                                          Date end, String status,Pageable pageable){
        List<SaleOrderReturn> list=salOrdRetDao.findByOrderIdAndStartDateBetweenAndStatus(id,start,end,status);
        return packToPage(list, pageable);
    }

    /*销售退货订单号不为空、开始退货日期区间为空、退货状态为空*/
    public Page<SaleOrderReturn> findByOrderId(Integer id,Pageable pageable){
        List<SaleOrderReturn> list=salOrdRetDao.findByOrderId(id);
        return packToPage(list, pageable);
    }

    /*销售退货订单号为空、开始退货日期区间不为空、退货状态为空*/
    public Page<SaleOrderReturn> findByStartDateBetween(Date start,Date end,Pageable pageable){
        List<SaleOrderReturn> list=salOrdRetDao.findByStartDateBetween(start,end);
        return packToPage(list, pageable);
    }

    /*销售退货订单号为空、开始退货日期区间为空、退货状态不为空*/
    public Page<SaleOrderReturn> findByStatus(String status,Pageable pageable){
        List<SaleOrderReturn> list=salOrdRetDao.findByStatus(status);
        return packToPage(list, pageable);
    }

    /*销售退货订单号不为空、开始退货日期区间不为空、退货状态为空*/
    public Page<SaleOrderReturn> findByOrderIdAndStartDateBetween(Integer id,Date start,Date end,Pageable pageable){
        List<SaleOrderReturn> list=salOrdRetDao.findByOrderIdAndStartDateBetween(id,start,end);
        return packToPage(list, pageable);
    }

    /*销售退货订单号不为空、开始退货日期区间为空、退货状态不为空*/
    public Page<SaleOrderReturn> findByOrderIdAndStatus(Integer id,String status,Pageable pageable){
        List<SaleOrderReturn> list=salOrdRetDao.findByOrderIdAndStatus(id,status);
        return packToPage(list, pageable);
    }

    /*销售退货订单号为空、开始退货日期区间不为空、退货状态不为空*/
    public Page<SaleOrderReturn> findByStartDateBetweenAndStatus(Date start,Date end,String status,Pageable pageable){
        List<SaleOrderReturn> list=salOrdRetDao.findByStartDateBetweenAndStatus(start,end,status);
        return packToPage(list, pageable);
    }

    /*将List数据封装成Page数据*/
    public Page<SaleOrderReturn> packToPage(List<SaleOrderReturn> list, Pageable pageable){
        //调用util包里面的ChangToPage类
        ChangToPage<SaleOrderReturn> changToPage=new ChangToPage<>();
        return changToPage.getPage(list,pageable);
    }

    /*给表sale_order_return新增一条数据*/
    public void addSaleOrderReturn(SaleOrderReturn saleOrderReturn){
        salOrdRetDao.save(saleOrderReturn);
    }

    /*修改单个SaleOrderReturn数据*/
    public void modSaleOrderReturn(SaleOrderReturn saleOrderReturn){
        salOrdRetDao.save(saleOrderReturn);
    }

    /*根据returnId查询orderId*/
    public Integer findOrderIdByReturnId(Integer id){return salOrdRetDao.findOrderIdByReturnId(id);}

    /*根据returnId查询orderNumber*/
    public String findOrderNumberByReturnId(Integer id){return salOrdRetDao.findOrderNumberByReturnId(id);}

    /*根据returnId删除数据*/
    public void delDataByReturnId(Integer id){
        salOrdRetDao.deleteById(id);
    }

    public void delSaleOrderReturnByOrderId(Integer id){
        salOrdRetDao.delSaleOrderReturnByOrderId(id);
    }
}
