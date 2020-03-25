package com.hsj.service;

import com.hsj.dao.CusDao;
import com.hsj.dao.SalPerDao;
import com.hsj.dao.SaleOrderDao;
import com.hsj.entity.SaleOrder;
import com.hsj.util.ChangToPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/*
 *@ClassName SaleOrderService
 *@Description 销售订单服务器
 *@Author hsj
 *Date 2020/3/13 16:41
 */
@Service
@Transactional
public class SaleOrderService {
    @Autowired
    SalPerDao salPerDao;
    @Autowired
    CusDao cusDao;
    @Autowired
    SaleOrderDao saleOrderDao;

    /*根据销售员id查询销售员姓名*/
    public String findSpNameBySpId(Integer id){
        return salPerDao.findSpNameBySpId(id);
    }

    /*根据客户id查询客户名称*/
    public String findCusNameByCusId(Integer id){
        return cusDao.findCusNameByCusId(id);
    }

    /*查询所有销售订单，返回类型是Page*/
    public Page<SaleOrder> findAllWithPage(Pageable pageable){
        return saleOrderDao.findAll(pageable);
    }

    /*查询所有销售订单，返回类型是List*/
    public List<SaleOrder> findAllWithList(){
        return saleOrderDao.findAll();
    }

    /*查询所有正常的销售订单，返回类型是List*/
    public List<SaleOrder> findAllNormal(){return saleOrderDao.findAllNormal();}

    /*当订单编号不为空*/
    public Page<SaleOrder> findByOrderId(Integer id,Pageable pageable){
        List<SaleOrder> list=saleOrderDao.findByOrderId(id);
        return packToPage(list,pageable);
    }

    /*当订单编号为空、销售员不为空、客户为空*/
    public Page<SaleOrder> findBySaleSpId(Integer id,Pageable pageable){
        List<SaleOrder> list=saleOrderDao.findBySaleSpId(id);
        return packToPage(list,pageable);
    }

    /*当订单编号为空、销售员为空、客户不为空*/
    public Page<SaleOrder> findBySaleCusId(Integer id,Pageable pageable){
        List<SaleOrder> list=saleOrderDao.findBySaleCusId(id);
        return packToPage(list,pageable);
    }

    /*当订单编号为空、销售员不为空、客户不为空*/
    public Page<SaleOrder> findBySaleSpIdAndSaleCusId(Integer id1,Integer id2,Pageable pageable){
        List<SaleOrder> list=saleOrderDao.findBySaleSpIdAndSaleCusId(id1,id2);
        return packToPage(list,pageable);
    }

    /*付款方式、销售时间、订单类型都不为空*/
    public Page<SaleOrder> findByPayWayAndDateBetweenAndType(String payWay, Date startDate, Date endDate, String Type, Pageable pageable){
        List<SaleOrder> list=saleOrderDao.findByPayWayAndDateBetweenAndType(payWay,startDate,endDate,Type);
        return packToPage(list,pageable);
    }

    /*付款方式为空、销售时间不为空，订单类型为空*/
    public Page<SaleOrder> findByDateBetween(Date startDate, Date endDate,Pageable pageable){
        List<SaleOrder> list=saleOrderDao.findByDateBetween(startDate,endDate);
        return packToPage(list,pageable);
    }

    /*付款方式不为空、销售时间为空，订单类型为空*/
    public Page<SaleOrder> findByPayWay(String payWay,Pageable pageable){
        List<SaleOrder> list=saleOrderDao.findByPayWay(payWay);
        return packToPage(list,pageable);
    }

    /*付款方式为空、销售时间为空，订单类型不为空*/
    public Page<SaleOrder> findByType(String Type,Pageable pageable){
        List<SaleOrder> list=saleOrderDao.findByType(Type);
        return packToPage(list,pageable);
    }

    /*付款方式不为空、销售时间不为空，订单类型为空*/
    public Page<SaleOrder> findByPayWayAndDateBetween(String payWay, Date startDate, Date endDate,Pageable pageable){
        List<SaleOrder> list=saleOrderDao.findByPayWayAndDateBetween(payWay,startDate,endDate);
        return packToPage(list,pageable);
    }

    /*付款方式不为空、销售时间为空，订单类型不为空*/
    public Page<SaleOrder> findByPayWayAndType(String payWay,String Type,Pageable pageable){
        List<SaleOrder> list=saleOrderDao.findByPayWayAndType(payWay,Type);
        return packToPage(list,pageable);
    }

    /*付款方式为空、销售时间不为空，订单类型不为空*/
    public Page<SaleOrder> findByDateBetweenAndType(Date startDate, Date endDate, String Type,Pageable pageable){
        List<SaleOrder> list=saleOrderDao.findByDateBetweenAndType(startDate,endDate,Type);
        return packToPage(list,pageable);
    }

    /*将List数据封装成Page数据*/
    public Page<SaleOrder> packToPage(List<SaleOrder> list, Pageable pageable){
        //调用util包里面的ChangToPage类
        ChangToPage<SaleOrder> changToPage=new ChangToPage<>();
        return changToPage.getPage(list,pageable);
    }

    /*添加单个销售订单saleOrder*/
    public void addSaleOrder(SaleOrder saleOrder){saleOrderDao.save(saleOrder);}

    /*根据orderId查询数据*/
    public SaleOrder findByOrderId(Integer id){
        List<SaleOrder> list=saleOrderDao.findByOrderId(id);
        SaleOrder saleOrder = null;
        for (SaleOrder sale:list){
            saleOrder=sale;
        }
        return saleOrder;
    }

    /*修改单个SaleOrder数据*/
    public void modSaleOrder(SaleOrder saleOrder){
        saleOrderDao.save(saleOrder);
    }

    /*根据order_id查询type*/
    public String findOrderTypeByOrderId(Integer id){return saleOrderDao.findOrderTypeByOrderId(id);}

    /*根据orderId查询orderNumber*/
    public String findOrderNumberByOrderId(Integer id){
        return saleOrderDao.findOrderNumberByOrderId(id);
    }

    /*根据orderId删除单个SaleOrder*/
    public void delByOrderId(Integer id){saleOrderDao.deleteById(id);}

    /*根据order_id查询采购日期date*/
    public Date findDateByOrderId(Integer id){return saleOrderDao.findDateByOrderId(id);}

    /*根据order_id修改type*/
    public void updateTypeByOrderId(String type,Integer id){
        saleOrderDao.updateTypeByOrderId(type,id);
    }

    public List<Integer> findProIdsByOrderId(Integer id){
        return saleOrderDao.findProIdsByOrderId(id);
    }

    /*根据orderId删除数据*/
    public void deleteByOrderId(Integer id){
        saleOrderDao.deleteById(id);
    }

    /*根sale_sp_id查询数据*/
    public List<SaleOrder> findSalesOrdersBySaleSpId(Integer id){return saleOrderDao.findSalesOrdersBySaleSpId(id);}

    /*根据order_id查询saleSpId*/
    public Integer findSaleSpIdByOrderId(Integer id){return saleOrderDao.findSaleSpIdByOrderId(id);}

    /*根据sale_cus_id查询数据*/
    public List<SaleOrder> findSalesOrdersBySaleCusId(Integer id){return saleOrderDao.findSalesOrdersBySaleCusId(id);}

    /*根据order_id查询saleCusId*/
    public Integer findSaleCusIdByOrderId(Integer id){return saleOrderDao.findSaleCusIdByOrderId(id);}
}
