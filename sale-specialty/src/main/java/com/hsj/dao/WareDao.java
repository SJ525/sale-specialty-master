package com.hsj.dao;

import com.hsj.entity.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/*仓库-JPA操作接口*/
public interface WareDao extends JpaRepository<Warehouse, Integer> {
    /*1.根据仓库id查询数据*/
    public Warehouse findByWareId(Integer id);

    /*2.根据仓库名称模糊名称查询数据*/
    public List<Warehouse> findByWareNameLike(String name);

    /*3根据仓库大小进行区间查询*/
    public List<Warehouse> findByWareSizeBetween(double start, double end);

    /*4.将2和3两种查询方式综合*/
    public List<Warehouse> findByWareNameLikeAndWareSizeBetween(String name, double start, double end);

    /*5.联合外键查询：通过warehouse表的cate_sm_id查询stroe_man表的sm_name*/
    @Query(value = "SELECT DISTINCT s.sm_name FROM warehouse w JOIN store_man s ON w.ware_sm_id=s.sm_id WHERE w.ware_sm_id=?",nativeQuery = true)
    public String findSmNameByWareSmId(Integer id);

    /*6.将3和6两种查询方式结合*/
    public List<Warehouse> findByWareSmIdAndWareSizeBetween(Integer id, double start, double end);

    /*7.将2，3和6两种查询方式结合*/
    public List<Warehouse> findByWareNameLikeAndWareSmIdAndWareSizeBetween(String name, Integer id, double start, double end);

    /*根据仓库id修改关联性relation*/
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE warehouse p SET p.relation=? WHERE p.ware_id=?",nativeQuery = true)
    public void updateRelationByWareId(String relation, Integer id);

    /*根据仓库id获取关联性relation*/
    @Query(value = "SELECT p.relation FROM warehouse p WHERE p.ware_id=?",nativeQuery = true)
    public String findRelationByWareId(Integer id);

    /*根据ware_sm_id查询数据*/
    @Query(value = "SELECT * FROM warehouse WHERE ware_sm_id=?",nativeQuery = true)
    public List<Warehouse> findWarehousesByWareSmId(Integer id);
}
