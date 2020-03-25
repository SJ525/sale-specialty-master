package com.hsj.service;

import com.hsj.dao.PurOrdRetDao;
import com.hsj.entity.PurOrderReturn;
import com.hsj.util.ChangToPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/*
 *@ClassName PurOrdRetService
 *@Description 采购退货订单服务器类
 *@Author hsj
 *Date 2020/3/11 10:59
 */
@Service
@Transactional
public class PurOrdRetService {
    @Autowired
    PurOrdRetDao purOrdRetDao;

    /*根据returnId查询数据*/
    public PurOrderReturn findByReturnId(Integer id){
        return purOrdRetDao.findByReturnId(id);
    }

    /*查询表pur_order_return所有数据，以Page类型返回*/
    public Page<PurOrderReturn> findAllWithPage(Pageable pageable){
        return purOrdRetDao.findAll(pageable);
    }

    /*查询表pur_order_return所有数据，以List类型返回*/
    public List<PurOrderReturn> findAllWithList(){
        return purOrdRetDao.findAll();
    }

    /*采购退货订单号、开始退货日期区间、退货状态都不为空*/
    public Page<PurOrderReturn> findByOrderIdAndStartDateBetweenAndStatus(Integer id, Date start,
                                                                          Date end, String status,Pageable pageable){
        List<PurOrderReturn> list=purOrdRetDao.findByOrderIdAndStartDateBetweenAndStatus(id,start,end,status);
        return packToPage(list, pageable);
    }

    /*采购退货订单号不为空、开始退货日期区间为空、退货状态为空*/
    public Page<PurOrderReturn> findByOrderId(Integer id,Pageable pageable){
        List<PurOrderReturn> list=purOrdRetDao.findByOrderId(id);
        return packToPage(list, pageable);
    }
    public List<PurOrderReturn> findByOrderId(Integer id){
        return purOrdRetDao.findByOrderId(id);
    }
    /*采购退货订单号为空、开始退货日期区间不为空、退货状态为空*/
    public Page<PurOrderReturn> findByStartDateBetween(Date start,Date end,Pageable pageable){
        List<PurOrderReturn> list=purOrdRetDao.findByStartDateBetween(start,end);
        return packToPage(list, pageable);
    }

    /*采购退货订单号为空、开始退货日期区间为空、退货状态不为空*/
    public Page<PurOrderReturn> findByStatus(String status,Pageable pageable){
        List<PurOrderReturn> list=purOrdRetDao.findByStatus(status);
        return packToPage(list, pageable);
    }

    /*采购退货订单号不为空、开始退货日期区间不为空、退货状态为空*/
    public Page<PurOrderReturn> findByOrderIdAndStartDateBetween(Integer id,Date start,Date end,Pageable pageable){
        List<PurOrderReturn> list=purOrdRetDao.findByOrderIdAndStartDateBetween(id,start,end);
        return packToPage(list, pageable);
    }

    /*采购退货订单号不为空、开始退货日期区间为空、退货状态不为空*/
    public Page<PurOrderReturn> findByOrderIdAndStatus(Integer id,String status,Pageable pageable){
        List<PurOrderReturn> list=purOrdRetDao.findByOrderIdAndStatus(id,status);
        return packToPage(list, pageable);
    }

    /*采购退货订单号为空、开始退货日期区间不为空、退货状态不为空*/
    public Page<PurOrderReturn> findByStartDateBetweenAndStatus(Date start,Date end,String status,Pageable pageable){
        List<PurOrderReturn> list=purOrdRetDao.findByStartDateBetweenAndStatus(start,end,status);
        return packToPage(list, pageable);
    }

    /*将List数据封装成Page数据*/
    public Page<PurOrderReturn> packToPage(List<PurOrderReturn> list, Pageable pageable){
        //调用util包里面的ChangToPage类
        ChangToPage<PurOrderReturn> changToPage=new ChangToPage<>();
        return changToPage.getPage(list,pageable);
    }

    /*给表pur_order_return新增一条数据*/
    public void addPurOrderReturn(PurOrderReturn purOrderReturn){
        purOrdRetDao.save(purOrderReturn);
    }

    /*修改单个PurOrderReturn数据*/
    public void modPurOrderReturn(PurOrderReturn purOrderReturn){
        purOrdRetDao.save(purOrderReturn);
    }

    /*根据returnId查询orderId*/
    public Integer findOrderIdByReturnId(Integer id){return purOrdRetDao.findOrderIdByReturnId(id);}

    /*根据returnId查询orderNumber*/
    public String findOrderNumberByReturnId(Integer id){return purOrdRetDao.findOrderNumberByReturnId(id);}

    /*根据returnId删除数据*/
    public void delDataByReturnId(Integer id){
        purOrdRetDao.deleteById(id);
    }

    public void delPurOrderReturnByOrderId(Integer id){
        purOrdRetDao.delPurOrderReturnByOrderId(id);
    }

    public void update(PurOrderReturn purOrderReturn){purOrdRetDao.save(purOrderReturn);}
}
