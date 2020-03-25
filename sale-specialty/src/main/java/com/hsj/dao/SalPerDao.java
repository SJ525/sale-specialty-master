package com.hsj.dao;

import com.hsj.entity.Salesperson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/*销售员-JPA操作接口*/
public interface SalPerDao extends JpaRepository<Salesperson, Integer> {
    /*1.根据销售员id查询数据*/
    public Salesperson findBySpId(Integer id);

    /*2.根据销售员名称模糊查询数据*/
    public List<Salesperson> findBySpNameLike(String name);

    /*3.根据销售员性别查询数据*/
    public List<Salesperson> findBySpSex(String sex);

    /*4.将2和3两种查询综合*/
    public List<Salesperson> findBySpNameLikeAndSpSex(String name, String sex);

    /*5.根据销售员id查询销售员姓名*/
    @Query(value = "SELECT s.spName FROM Salesperson s WHERE s.spId=?1")
    public String findSpNameBySpId(Integer id);

    /*根据销售员id修改关联性relation*/
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE salesperson p SET p.relation=? WHERE p.sp_id=?",nativeQuery = true)
    public void updateRelationBySpId(String relation, Integer id);

    /*根据销售员id获取关联性relation*/
    @Query(value = "SELECT p.relation FROM salesperson p WHERE p.sp_id=?",nativeQuery = true)
    public String findRelationBySpId(Integer id);
}
