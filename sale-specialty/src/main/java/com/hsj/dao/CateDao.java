package com.hsj.dao;

import com.hsj.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/*特产分类-JPA操作接口*/
public interface CateDao extends JpaRepository<Category, Integer> {

    /*1.根据特产类型名称进行模糊查询数据*/
    public List<Category> findByProTypeNameLike(String proTypeName);

    /*2.根据特产类型名称进行查询数据*/
    public Category findByProTypeName(String proTypeName);

    /*3.根据特产类型id查询数据*/
    public Category findByProTypeId(Integer proTypeId);

    /*4.根据特产类型名称查询特产类型id*/
    @Query(value="select c.proTypeId from Category c where c.proTypeName=?1")
    public Integer findProTypeIdByProTypeName(String name);

    /*5.根据特产类型id查询特产类型名称*/
    @Query(value="select c.proTypeName from Category c where c.proTypeId=?1")
    public String findProTypeNameByProTypeId(Integer id);

    /*根据特产类型id修改关联性relation*/
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE category c SET c.relation=? WHERE c.pro_type_id=?",nativeQuery = true)
    public void updateRelationByProTypeId(String relation, Integer id);

    /*根据特产类型id获取关联性relation*/
    @Query(value = "SELECT c.relation FROM Category c WHERE c.proTypeId=?1")
    public String findRelationByProTypeId(Integer id);
}
