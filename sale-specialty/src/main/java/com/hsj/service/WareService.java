package com.hsj.service;

import com.hsj.dao.WareDao;
import com.hsj.entity.Warehouse;
import com.hsj.util.ChangToPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/*
 *@ClassName WareService
 *@Description 仓库-服务器类
 *@Author hsj
 *Date 2020/3/4 21:13
 */
@Service
@Transactional
public class WareService {
    @Autowired
    WareDao wareDao;

    /*1.根据wareId查询数据*/
    public Warehouse findByWareId(Integer id){return wareDao.findByWareId(id);}

    /*2.查询所有仓库数据,返回类型是Page*/
    public Page<Warehouse> findAllWithPage(Pageable pageable){
        return wareDao.findAll(pageable);
    }

    /*3.查询所有仓库数据,返回类型是List*/
    public List<Warehouse> findAllWithList(){
        return wareDao.findAll();
    }

    /*4.将List数据封装成Page数据*/
    public Page<Warehouse> packToPage(List<Warehouse> list, Pageable pageable){
        //调用util包里面的ChangToPage类
        ChangToPage<Warehouse> changToPage=new ChangToPage<>();
        return changToPage.getPage(list,pageable);
    }

    /*5.根据wareNameLike查询数据*/
    public Page<Warehouse> findByWareNameLike(String name, Pageable pageable){
        List<Warehouse> list=wareDao.findByWareNameLike("%"+name+"%");
        return packToPage(list,pageable);
    }

    /*6.根据wareSizeBetween查询*/
    public  Page<Warehouse> findByWareSizeBetween(double start,double end,Pageable pageable){
        List<Warehouse> list=wareDao.findByWareSizeBetween(start,end);
        return packToPage(list,pageable);
    }

    /*7.根据wareNameLike和wareSizeBetween查询*/
    public Page<Warehouse> findByWareNameLikeAndWareSizeBetween(String name,double start,double end,Pageable pageable){
        List<Warehouse> list=wareDao.findByWareNameLikeAndWareSizeBetween("%"+name+"%",start,end);
        return packToPage(list,pageable);
    }

    /*8.实现添加单个仓库*/
    public void addWarehouse(Warehouse warehouse){
        wareDao.save(warehouse);
    }

    /*9.实现修改仓库，并保存*/
    public void updateWarehouse(Warehouse warehouse){
        wareDao.save(warehouse);
    }

    /*10.根据仓库id,删除仓库*/
    public void delWarehouse(Integer id){
        wareDao.deleteById(id);
    }

    /*11.联合外键查询：通过warehouse表的cate_sm_id查询stroe_man表的sm_name*/
    public String findSmNameByWareSmId(Integer id){
        return wareDao.findSmNameByWareSmId(id);
    }

    /*12.通过仓库的WareSmId和WareSizeBetween查询*/
    public Page<Warehouse> findByWareSmIdAndWareSizeBetween(Integer id,double start,double end,Pageable pageable){
        List<Warehouse> list =wareDao.findByWareSmIdAndWareSizeBetween(id,start,end);
        return packToPage(list,pageable);
    }

    /*13.三种查询方式都结合*/
    public Page<Warehouse> findByThreeWays(String name,Integer id,double start,double end,Pageable pageable){
        List<Warehouse> list =wareDao.findByWareNameLikeAndWareSmIdAndWareSizeBetween("%"+name+"%",id,start,end);
        return packToPage(list,pageable);
    }

    /*根据仓库id修改关联性relation*/
    public void updateRelationByWareId(String relation,Integer id){wareDao.updateRelationByWareId(relation,id);}

    /*根据仓库id获取关联性relation*/
    public String findRelationByWareId(Integer id){return wareDao.findRelationByWareId(id);}

    /*根据ware_sm_id查询数据*/
    public List<Warehouse> findWarehousesByWareSmId(Integer id){return wareDao.findWarehousesByWareSmId(id);}
}
