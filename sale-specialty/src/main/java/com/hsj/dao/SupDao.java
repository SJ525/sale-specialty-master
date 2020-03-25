package com.hsj.dao;

import com.hsj.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/*供应商-JPA操作接口*/
public interface SupDao extends JpaRepository<Supplier, Integer> {
    /*根据供应商id查询数据*/
    public Supplier findBySupId(Integer id);

    /*根据供应商模糊名称查询数据*/
    public List<Supplier> findBySupNameLike(String name);

    /*根据供应商类型查询数据*/
    public List<Supplier> findBySupType(String name);

    /*根据供应商类型和供应商模糊名称查询数据*/
    public List<Supplier> findBySupTypeAndSupNameLike(String supType, String supName);

    /*根据供应商id修改关联性relation*/
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE supplier p SET p.relation=? WHERE p.sup_id=?",nativeQuery = true)
    public void updateRelationBySupId(String relation, Integer id);

    /*根据供应商id获取关联性relation*/
    @Query(value = "SELECT p.relation FROM supplier p WHERE p.sup_id=?",nativeQuery = true)
    public String findRelationBySupId(Integer id);
}
