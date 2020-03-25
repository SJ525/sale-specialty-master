package com.hsj.dao;

import com.hsj.entity.Purchaser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/*采购员-JPA操作接口*/
public interface PurManDao extends JpaRepository<Purchaser, Integer> {
    /*1.根据采购员id查询数据*/
    public Purchaser findByPurId(Integer id);

    /*2.根据采购员名称模糊查询数据*/
    public List<Purchaser> findByPurNameLike(String name);

    /*3.根据采购员性别查询数据*/
    public List<Purchaser> findByPurSex(String sex);

    /*4.将2和3两种查询综合*/
    public List<Purchaser> findByPurNameLikeAndPurSex(String name, String sex);

    /*根据采购员id修改关联性relation*/
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE purchaser p SET p.relation=? WHERE p.pur_id=?",nativeQuery = true)
    public void updateRelationByPurId(String relation, Integer id);

    /*根据采购员id获取关联性relation*/
    @Query(value = "SELECT p.relation FROM Purchaser p WHERE p.purId=?1")
    public String findRelationByPurId(Integer id);

}
