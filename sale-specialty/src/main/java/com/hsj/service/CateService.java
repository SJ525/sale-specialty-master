package com.hsj.service;

import com.hsj.dao.CateDao;
import com.hsj.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/*
 *@ClassName CateService
 *@Description 特产类型管理服务器
 *@Author hsj
 *Date 2020/2/27 20:26
 */
@Service
@Transactional
public class CateService{
    @Autowired
    CateDao cateDao;

    //实现根据特产类型名称进行模糊查找数据
    public List<Category> findByProTypeNameLike(String name){
        return cateDao.findByProTypeNameLike("%"+name+"%");
    }

    //实现根据特产类型名称进行查找数据
    public Category findByProTypeName(String name){
        return cateDao.findByProTypeName(name);
    }

    //实现将所有数据进行分页
    public Page<Category> findAll(Pageable pageable){
        return cateDao.findAll(pageable);
    }

    //实现添加特产类型，等同增加单个类型
    public void addProType(Category category){
        cateDao.save(category);
    }

    //通过id获取修改的特产类型信息
    public Category findByProTypeId(Integer proTypeId){
        return cateDao.findByProTypeId(proTypeId);
    }

    // 执行修改特产类型操作
    public void modProType(Category category){
         cateDao.save(category);
    }

    //通过id删除特产类型
    public void delProType(Integer proTypeId){
        cateDao.deleteById(proTypeId);
    }

    //查找所有特产类型,数据类型是List
    public List<Category> findAll(){
        return cateDao.findAll();
    }

    /*根据特产类型名称查询特产类型id*/
    public Integer findProTypeIdByProTypeName(String name){
        return cateDao.findProTypeIdByProTypeName(name);
    }

    /*根据特产类型id查询特产类型名称*/
    public String findProTypeNameByProTypeId(Integer id){
        return cateDao.findProTypeNameByProTypeId(id);
    }

    /*根据特产类型id修改关联性relation*/
    public void updateRelationByProTypeId(String relation,Integer id){cateDao.updateRelationByProTypeId(relation,id);}

    /*根据特产类型获取关联性relation*/
    public String findRelationByProTypeId(Integer id){
        return cateDao.findRelationByProTypeId(id);
    }
}
