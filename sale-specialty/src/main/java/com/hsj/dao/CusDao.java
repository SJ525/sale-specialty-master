package com.hsj.dao;

import com.hsj.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/*客户-JPA操作接口*/
public interface CusDao extends JpaRepository<Customer, Integer> {
    /*根据客户id查询数据*/
    public Customer findByCusId(Integer id);

    /*根据客户模糊名称查询数据*/
    public List<Customer> findByCusNameLike(String name);

    /*根据客户类型查询数据*/
    public List<Customer> findByCusType(String name);

    /*根据客户类型和客户模糊名称查询数据*/
    public List<Customer> findByCusTypeAndCusNameLike(String cusType, String cusName);

    /*根据客户id查询客户名称*/
    @Query(value = "SELECT c.cusName FROM Customer c WHERE c.cusId=?1")
    public String findCusNameByCusId(Integer id);

    /*根据客户id修改关联性relation*/
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE customer p SET p.relation=? WHERE p.cus_id=?",nativeQuery = true)
    public void updateRelationByCusId(String relation, Integer id);

    /*根据客户id获取关联性relation*/
    @Query(value = "SELECT p.relation FROM customer p WHERE p.cus_id=?",nativeQuery = true)
    public String findRelationByCusId(Integer id);
}
