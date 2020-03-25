package com.hsj.service;

import com.hsj.dao.SalPerDao;
import com.hsj.entity.Salesperson;
import com.hsj.util.ChangToPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/*
 *@ClassName SalPerService
 *@Description 销售员-服务器类
 *@Author hsj
 *Date 2020/3/5 13:23
 */
@Service
@Transactional
public class SalPerService {
    @Autowired
    SalPerDao salPerDao;

    /*1.根据销售员id查询数据*/
    public Salesperson findBySpId(Integer id){
        return salPerDao.findBySpId(id);
    }

    /*2.查询所有销售员数据,返回类型是Page*/
    public Page<Salesperson> findAll(Pageable pageable){
        return salPerDao.findAll(pageable);
    }

    /*3.查询所有销售员数据,返回类型是List*/
    public List<Salesperson> findAllWithList(){
        return salPerDao.findAll();
    }

    /*3.将List数据封装成Page数据*/
    public Page<Salesperson> packToPage(List<Salesperson> list, Pageable pageable){
        //调用util包里面的ChangToPage类
        ChangToPage<Salesperson> changToPage=new ChangToPage<>();
        return changToPage.getPage(list,pageable);
    }

    /*4.根据销售员姓名模糊查询数据*/
    public Page<Salesperson> findBySpNameLike(String name,Pageable pageable){
        List<Salesperson> list=salPerDao.findBySpNameLike("%"+name+"%");
        return packToPage(list,pageable);
    }

    /*5.根据销售员性别查询数据*/
    public Page<Salesperson> findBySpSex(String sex,Pageable pageable){
        List<Salesperson> list=salPerDao.findBySpSex(sex);
        return packToPage(list,pageable);
    }

    /*6.将4和5综合查询*/
    public Page<Salesperson> findByBoth(String name,String sex,Pageable pageable){
        List<Salesperson> list=salPerDao.findBySpNameLikeAndSpSex("%"+name+"%", sex);
        return packToPage(list,pageable);
    }

    /*7.实现添加单个销售员*/
    public Salesperson addSalesperson(Salesperson salesperson){
        return salPerDao.save(salesperson);
    }

    /*8.修改销售员，并保存*/
    public void updateSalesperson(Salesperson salesperson){
        salPerDao.save(salesperson);
    }

    /*9.根据销售员id,删除销售员*/
    public void delSalesperson(Integer id){
        salPerDao.deleteById(id);
    }

    /*根据销售员id修改关联性relation*/
    public void updateRelationBySpId(String relation,Integer id){salPerDao.updateRelationBySpId(relation,id);}

    /*根据销售员id获取关联性relation*/
    public String findRelationBySpId(Integer id){return salPerDao.findRelationBySpId(id);}
}
