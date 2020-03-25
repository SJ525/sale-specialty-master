package com.hsj.service;

import com.hsj.dao.CusDao;
import com.hsj.entity.Customer;
import com.hsj.util.ChangToPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/*
 *@ClassName CusService
 *@Description 客户-服务类
 *@Author hsj
 *Date 2020/3/4 12:32
 */
@Service
@Transactional
public class CusService {
    @Autowired
    CusDao cusDao;

    /*根据客户id查询数据*/
    public Customer findByCusId(Integer id){
        return cusDao.findByCusId(id);
    }

    /*查询所有客户数据,返回类型是Page*/
    public Page<Customer> findAll(Pageable pageable){
        return cusDao.findAll(pageable);
    }

    /*查询所有客户数据,返回类型是List*/
    public List<Customer> findAllWithList(){
        return cusDao.findAll();
    }

    /*将List数据封装成Page数据*/
    public Page<Customer> packToPage(List<Customer> list,Pageable pageable){
        //调用util包里面的ChangToPage类
        ChangToPage<Customer> changToPage=new ChangToPage<>();
        return changToPage.getPage(list,pageable);
    }

    /*根据客户名称模糊查询数据*/
    public Page<Customer> findByCusNameLike(String name,Pageable pageable){
        List<Customer> list=cusDao.findByCusNameLike("%"+name+"%");
        return packToPage(list,pageable);
    }

    /*根据客户类型查询数据*/
    public Page<Customer> findByCusType(String name,Pageable pageable){
        List<Customer> list=cusDao.findByCusType(name);
        return packToPage(list,pageable);
    }


    /*根据客户类型和客户模糊名称查询数据*/
    public Page<Customer> findByBoth(String cusType,String cusName,Pageable pageable){
        List<Customer> list=cusDao.findByCusTypeAndCusNameLike(cusType,"%"+cusName+"%");
        return packToPage(list,pageable);
    }

    /*实现添加单个用户*/
    public Customer addCustomer(Customer customer){
        return cusDao.save(customer);
    }

    /*修改客户，并保存*/
    public void updateCustomer(Customer customer){
         cusDao.save(customer);
    }

    /*根据客户id,删除客户*/
    public void delCustomer(Integer id){
        cusDao.deleteById(id);
    }

    /*根据客户id修改关联性relation*/
    public void updateRelationByCusId(String relation,Integer id){cusDao.updateRelationByCusId(relation,id);}

    /*根据客户id获取关联性relation*/
    public String findRelationByCusId(Integer id){return cusDao.findRelationByCusId(id);}
}
