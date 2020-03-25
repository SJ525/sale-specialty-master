package com.hsj.service;

import com.hsj.dao.PurOrdRetDao;
import com.hsj.dao.PurOrderDao;
import com.hsj.entity.PurOrder;
import com.hsj.util.ChangToPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/*
 *@ClassName PurOrderService
 *@Description 采购订单-服务器类
 *@Author hsj
 *Date 2020/3/8 10:57
 */
@Service
@Transactional
public class PurOrderService {
    @Autowired
    PurOrdRetDao purOrdRetDao;
    @Autowired
    PurOrderDao purOrderDao;

    /*查询表pur_order所有数据，返回类型是Page*/
    public Page<PurOrder> findAllWithPage(Pageable pageable){
        return purOrderDao.findAll(pageable);
    }

    /*查询表pur_order所有数据，返回类型是List*/
    public List<PurOrder> findAllWithList(){return purOrderDao.findAll();}

    /*查询所有正常的采购订单，返回类型是List*/
    public List<PurOrder> findAllNormal(){return purOrderDao.findAllNormal();}

    /*外键查询:通过表pur_order的pur_man_id查询表purchaser的pur_name*/
    public String findPurchaserPurName(Integer id){
        return purOrderDao.findPurchaserPurName(id);
    }

    /*外键查询:通过表pur_order的pur_ware_id查询表warehouse的ware_name*/
    public String findWarehouseWareName(Integer id){
        return purOrderDao.findWarehouseWareName(id);
    }

    /*添加单个采购订单purOrder*/
    public void addPurOrder(PurOrder purOrder){purOrderDao.save(purOrder);}

    /*当订单编号不为空*/
    public Page<PurOrder> findByOrderId(Integer id,Pageable pageable){
        List<PurOrder> list=purOrderDao.findByOrderId(id);
        return packToPage(list,pageable);
    }

    /*当订单编号为空、采购员不为空、所入仓库为空*/
    public Page<PurOrder> findByPurManId(Integer id,Pageable pageable){
        List<PurOrder> list=purOrderDao.findByPurManId(id);
        return packToPage(list,pageable);
    }

    /*当订单编号为空、采购员为空、所入仓库不为空*/
    public Page<PurOrder> findByPurWareId(Integer id,Pageable pageable){
        List<PurOrder> list=purOrderDao.findByPurWareId(id);
        return packToPage(list,pageable);
    }

    /*当订单编号为空、采购员不为空、所入仓库不为空*/
    public Page<PurOrder> findByPurManIdAndPurWareId(Integer id1,Integer id2,Pageable pageable){
        List<PurOrder> list=purOrderDao.findByPurManIdAndPurWareId(id1,id2);
        return packToPage(list,pageable);
    }

    /*付款方式、采购日期、订单类型都不为空*/
    public Page<PurOrder> findByPayWayAndDateBetweenAndType(String payWay, Date startDate, Date endDate, String Type,Pageable pageable){
        List<PurOrder> list=purOrderDao.findByPayWayAndDateBetweenAndType(payWay,startDate,endDate,Type);
        return packToPage(list,pageable);
    }

    /*付款方式为空、采购日期不为空，订单类型为空*/
    public Page<PurOrder> findByDateBetween(Date startDate, Date endDate,Pageable pageable){
        List<PurOrder> list=purOrderDao.findByDateBetween(startDate,endDate);
        return packToPage(list,pageable);
    }

    /*付款方式不为空、采购日期为空，订单类型为空*/
    public Page<PurOrder> findByPayWay(String payWay,Pageable pageable){
        List<PurOrder> list=purOrderDao.findByPayWay(payWay);
        return packToPage(list,pageable);
    }

    /*付款方式为空、采购日期为空，订单类型不为空*/
    public Page<PurOrder> findByType(String Type,Pageable pageable){
        List<PurOrder> list=purOrderDao.findByType(Type);
        return packToPage(list,pageable);
    }

    /*付款方式不为空、采购日期不为空，订单类型为空*/
    public Page<PurOrder> findByPayWayAndDateBetween(String payWay, Date startDate, Date endDate,Pageable pageable){
        List<PurOrder> list=purOrderDao.findByPayWayAndDateBetween(payWay,startDate,endDate);
        return packToPage(list,pageable);
    }

    /*付款方式不为空、采购日期为空，订单类型不为空*/
    public Page<PurOrder> findByPayWayAndType(String payWay,String Type,Pageable pageable){
        List<PurOrder> list=purOrderDao.findByPayWayAndType(payWay,Type);
        return packToPage(list,pageable);
    }

    /*付款方式为空、采购日期不为空，订单类型不为空*/
    public Page<PurOrder> findByDateBetweenAndType(Date startDate, Date endDate, String Type,Pageable pageable){
        List<PurOrder> list=purOrderDao.findByDateBetweenAndType(startDate,endDate,Type);
        return packToPage(list,pageable);
    }

    /*将List数据封装成Page数据*/
    public Page<PurOrder> packToPage(List<PurOrder> list, Pageable pageable){
        //调用util包里面的ChangToPage类
        ChangToPage<PurOrder> changToPage=new ChangToPage<>();
        return changToPage.getPage(list,pageable);
    }

    /*根据orderId查询数据*/
   public PurOrder findByOrderId(Integer id){
       List<PurOrder> list=purOrderDao.findByOrderId(id);
       PurOrder purOrder = null;
       for (PurOrder pur:list){
           purOrder=pur;
       }
        return purOrder;
    }

    /*修改单个PurOrder数据*/
    public void modPurOrder(PurOrder purOrder){
        purOrderDao.save(purOrder);
    }

    /*根据orderId删除数据*/
    public void deleteByOrderId(Integer id){
        purOrderDao.deleteById(id);
    }

    /*通过order_id查询order_number*/
    public String findOrderNumberByOrderId(Integer id){
        return purOrderDao.findOrderNumberByOrderId(id);
    }

    /*根据order_id查询type*/
    public String findOrderTypeByOrderId(Integer id){return purOrderDao.findOrderTypeByOrderId(id);}

    /*根据order_id修改type*/
    public void updateTypeByOrderId(String type,Integer id){
        purOrderDao.updateTypeByOrderId(type,id);
    }

    /*根据order_id查询采购日期date*/
    public Date findDateByOrderId(Integer id){return purOrderDao.findDateByOrderId(id);}

    public List<Integer> findProIdsByOrderId(Integer id){
        return purOrderDao.findProIdsByOrderId(id);
    }

    /*根pur_man_id查询数据*/
    public List<PurOrder> findPurOrdersByPurManId(Integer id){
        return purOrderDao.findPurOrdersByPurManId(id);
    }

    /*根据order_id查询purManId*/
    public Integer findPurManIdByOrderId(Integer id){return purOrderDao.findPurManIdByOrderId(id);}

    /*根pur_ware_id查询数据*/
    public List<PurOrder> findPurOrdersByPurWareId(Integer id){return purOrderDao.findPurOrdersByPurWareId(id);}

    /*根据order_id查询purWareId*/
    public Integer findPurWareIdByOrderId(Integer id){return purOrderDao.findPurWareIdByOrderId(id);}
}
