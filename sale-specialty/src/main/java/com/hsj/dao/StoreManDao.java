package com.hsj.dao;

import com.hsj.entity.StoreMan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/*仓库管理员-JPA操作接口*/
public interface StoreManDao extends JpaRepository<StoreMan, Integer> {
    /*1.根据仓库管理员id查询数据*/
    public StoreMan findBySmId(Integer id);

    /*2.根据仓库管理员名称模糊查询数据*/
    public List<StoreMan> findBySmNameLike(String name);

    /*3.根据仓库管理员性别查询数据*/
    public List<StoreMan> findBySmSex(String sex);

    /*4.将2和3两种查询综合*/
    public List<StoreMan> findBySmNameLikeAndSmSex(String name, String sex);

    /*5.根据仓库管理员名称查询仓库管理员id*/
    @Query(value = "SELECT s.sm_id FROM store_man s WHERE s.sm_name=?",nativeQuery = true)
    public Integer findSmIdBySmName(String name);

    /*6.根据仓库管理员id查询仓库管理员名称*/
    @Query(value = "SELECT s.sm_name FROM store_man s WHERE s.sm_id=?",nativeQuery = true)
    public String findSmNameBySmId(Integer id);

    /*根据仓库管理员id修改关联性relation*/
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE store_man p SET p.relation=? WHERE p.sm_id=?",nativeQuery = true)
    public void updateRelationBySmId(String relation, Integer id);

    /*根据仓库管理员id获取关联性relation*/
    @Query(value = "SELECT p.relation FROM store_man p WHERE p.sm_id=?",nativeQuery = true)
    public String findRelationBySmId(Integer id);
}
