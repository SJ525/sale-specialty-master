package com.hsj.service;

import com.hsj.dao.StoreManDao;
import com.hsj.entity.StoreMan;
import com.hsj.util.ChangToPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/*
 *@ClassName StoreManService
 *@Description 仓库管理员-服务器类
 *@Author hsj
 *Date 2020/3/5 13:18
 */
@Service
@Transactional
public class StoreManService {
    @Autowired
    StoreManDao storeManDao;

    /*1.根据仓库管理员id查询数据*/
    public StoreMan findBySmId(Integer id){
        return storeManDao.findBySmId(id);
    }

    /*2.查询所有仓库管理员数据,以Page类型返回*/
    public Page<StoreMan> findAllPage(Pageable pageable){
        return storeManDao.findAll(pageable);
    }

    /*4.查询所有仓库管理员数据,以List类型返回*/
    public List<StoreMan> findAllList(){
        return storeManDao.findAll();
    }

    /*5.将List数据封装成Page数据*/
    public Page<StoreMan> packToPage(List<StoreMan> list, Pageable pageable){
        //调用util包里面的ChangToPage类
        ChangToPage<StoreMan> changToPage=new ChangToPage<>();
        return changToPage.getPage(list,pageable);
    }

    /*6.根据仓库管理员姓名模糊查询数据*/
    public Page<StoreMan> findBySmNameLike(String name,Pageable pageable){
        List<StoreMan> list=storeManDao.findBySmNameLike("%"+name+"%");
        return packToPage(list,pageable);
    }

    /*7.根据仓库管理员性别查询数据*/
    public Page<StoreMan> findBySmSex(String sex,Pageable pageable){
        List<StoreMan> list=storeManDao.findBySmSex(sex);
        return packToPage(list,pageable);
    }

    /*8.将4和5综合查询*/
    public Page<StoreMan> findByBoth(String name,String sex,Pageable pageable){
        List<StoreMan> list=storeManDao.findBySmNameLikeAndSmSex("%"+name+"%", sex);
        return packToPage(list,pageable);
    }

    /*9.实现添加单个仓库管理员*/
    public void addStoreMan(StoreMan storeMan){storeManDao.save(storeMan);}

    /*10.修改仓库管理员，并保存*/
    public void updateStoreMan(StoreMan storeMan){
        storeManDao.save(storeMan);
    }

    /*11.根据仓库管理员id,删除仓库管理员*/
    public void delStoreMan(Integer id){
        storeManDao.deleteById(id);
    }

    /*12.根据仓库管理员名称查询仓库管理员id*/
    public Integer findSmIdBySmName(String name){
        return storeManDao.findSmIdBySmName(name);
    }

    /*13.根据仓库管理员id查询仓库管理员名称*/
    public String findSmNameBySmId(Integer id){
        return storeManDao.findSmNameBySmId(id);
    }

    /*根据仓库管理员id修改关联性relation*/
    public void updateRelationBySmId(String relation,Integer id){storeManDao.updateRelationBySmId(relation,id);}

    /*根据仓库管理员id获取关联性relation*/
    public String findRelationBySmId(Integer id){return storeManDao.findRelationBySmId(id);}
}
