package com.hsj.service;

import com.hsj.dao.PurManDao;
import com.hsj.entity.Purchaser;
import com.hsj.util.ChangToPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/*
 *@ClassName PurManService
 *@Description 采购员-服务器类
 *@Author hsj
 *Date 2020/3/5 12:22
 */
@Service
@Transactional
public class PurManService {
    @Autowired
    PurManDao purManDao;

    /*1.根据采购员id查询数据*/
    public Purchaser findByPurId(Integer id){
        return purManDao.findByPurId(id);
    }

    /*2.查询所有采购员数据，返回类型是Page*/
    public Page<Purchaser> findAllWithPage(Pageable pageable){
        return purManDao.findAll(pageable);
    }

    /*3.查询所有采购员数据，返回类型是List*/
    public List<Purchaser> findAllWithList(){
        return purManDao.findAll();
    }

    /*4.将List数据封装成Page数据*/
    public Page<Purchaser> packToPage(List<Purchaser> list, Pageable pageable){
        //调用util包里面的ChangToPage类
        ChangToPage<Purchaser> changToPage=new ChangToPage<>();
        return changToPage.getPage(list,pageable);
    }

    /*5.根据采购员姓名模糊查询数据*/
    public Page<Purchaser> findByPurNameLike(String name,Pageable pageable){
        List<Purchaser> list=purManDao.findByPurNameLike("%"+name+"%");
        return packToPage(list,pageable);
    }

    /*6.根据采购员性别查询数据*/
    public Page<Purchaser> findByPurSex(String sex,Pageable pageable){
        List<Purchaser> list=purManDao.findByPurSex(sex);
        return packToPage(list,pageable);
    }

    /*7.将4和5综合查询*/
    public Page<Purchaser> findByBoth(String name,String sex,Pageable pageable){
        List<Purchaser> list=purManDao.findByPurNameLikeAndPurSex("%"+name+"%", sex);
        return packToPage(list,pageable);
    }

    /*8.实现添加单个采购员*/
    public void addPurchaser(Purchaser purchaser){
        purManDao.save(purchaser);
    }

    /*9.修改采购员，并保存*/
    public void updatePurchaser(Purchaser purchaser){
        purManDao.save(purchaser);
    }

    /*10.根据采购员id,删除采购员*/
    public void delPurchaser(Integer id){
        purManDao.deleteById(id);
    }

    /*根据采购员id修改关联性relation*/
    public void updateRelationByPurId(String relation,Integer id){
        purManDao.updateRelationByPurId(relation,id);
    }

    /*根据采购员id获取关联性relation*/
    public String findRelationByPurId(Integer id){
        return purManDao.findRelationByPurId(id);
    }
}
