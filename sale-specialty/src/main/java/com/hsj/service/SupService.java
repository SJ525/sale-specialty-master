package com.hsj.service;

import com.hsj.dao.SupDao;
import com.hsj.entity.Supplier;
import com.hsj.util.ChangToPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/*
 *@ClassName SupService
 *@Description 供应商-服务器类
 *@Author hsj
 *Date 2020/3/4 19:25
 */
@Service
@Transactional
public class SupService {
    @Autowired
    SupDao supDao;

    /*根据供应商id查询数据*/
    public Supplier findBySupId(Integer id){
        return supDao.findBySupId(id);
    }

    /*查询所有供应商数据,数据类型是Page*/
    public Page<Supplier> findAll(Pageable pageable){
        return supDao.findAll(pageable);
    }

    /*查询表supplier所有数据，返回类型是List*/
    public List<Supplier> findAllWithList(){
        return supDao.findAll();
    }

    /*将List数据封装成Page数据*/
    public Page<Supplier> packToPage(List<Supplier> list, Pageable pageable){
        //调用util包里面的ChangToPage类
        ChangToPage<Supplier> changToPage=new ChangToPage<>();
        return changToPage.getPage(list,pageable);
    }

    /*根据供应商名称模糊查询数据*/
    public Page<Supplier> findBySupNameLike(String name,Pageable pageable){
        List<Supplier> list=supDao.findBySupNameLike("%"+name+"%");
        return packToPage(list,pageable);
    }

    /*根据供应商类型查询数据*/
    public Page<Supplier> findBySupType(String name,Pageable pageable){
        List<Supplier> list=supDao.findBySupType(name);
        return packToPage(list,pageable);
    }

    /*根据供应商类型和供应商模糊名称查询数据*/
    public Page<Supplier> findByBoth(String cusType,String cusName,Pageable pageable){
        List<Supplier> list=supDao.findBySupTypeAndSupNameLike(cusType,"%"+cusName+"%");
        return packToPage(list,pageable);
    }

    /*实现添加单个供应商*/
    public Supplier addSupplier(Supplier Supplier){
        return supDao.save(Supplier);
    }

    /*修改供应商，并保存*/
    public void updateSupplier(Supplier Supplier){
        supDao.save(Supplier);
    }

    /*根据供应商id,删除供应商*/
    public void delSupplier(Integer id){
        supDao.deleteById(id);
    }

    /*根据供应商id修改关联性relation*/
    public void updateRelationBySupId(String relation,Integer id){supDao.updateRelationBySupId(relation,id);}

    /*根据供应商id获取关联性relation*/
    public String findRelationBySupId(Integer id){return supDao.findRelationBySupId(id);}
}
