package com.hsj.dao;

import com.hsj.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

/*特产-JPA操作接口*/
public interface ProdDao extends JpaRepository<Product, Integer> {

    /*根据特产id查询数据*/
    public Product findByProId(Integer id);

    /*根据特产类型id进行查询数据*/
    public List<Product> findByCateProTypeId(Integer proTypeId);

    /*根据特产名称进行模糊查询数据*/
    public List<Product> findByProNameLike(String proName);

    /*根据特产类型id和特产名称查询*/
    public List<Product> findByCateProTypeIdAndProNameLike(Integer id, String name);

    /*根据特产id查询特产名称*/
    @Query(value = "select p.proName from Product p where p.proId=?1")
    public String findProNameByProId(Integer id);

    /*根据特产id查询特产图片*/
    @Query(value="select p.proImg from Product p where p.proId=?1")
    public String findProImgByProId(Integer id);

    /*根据特产id查询特产存量*/
    @Query(value="select p.proStock from Product p where p.proId=?1")
    public Integer findProStockByProId(Integer id);

    /*根据特产id查询特产类型id*/
    @Query(value="select p.cateProTypeId from Product p where p.proId=?1")
    public Integer findCateProTypeIdByProId(Integer id);

    /*根据特产id查询特产计价单位*/
    @Query(value = "select p.proUnit from Product p where p.proId=?1")
    public String findProUnitByProId(Integer id);

    /*根据特产id查询进货价格*/
    @Query(value = "select p.proInPrice from Product p where p.proId=?1")
    public BigDecimal findProInPriceByProId(Integer id);

    /*根据特产id查询出货价格*/
    @Query(value = "select p.proOutPrice from Product p where p.proId=?1")
    public BigDecimal findProOutPriceByProId(Integer id);

    /*根据proId查询buyId列表*/
    @Query(value = "SELECT b.buy_order_id FROM buy_product b JOIN product p ON b.buy_pro_id=p.pro_id WHERE p.pro_id=?",nativeQuery = true)
    public List<Integer> findBuyOrderIdByProId(Integer id);

    /*根据cate_pro_type_id查询数据*/
    @Query(value = "SELECT * FROM product WHERE cate_pro_type_id=?",nativeQuery = true)
    public List<Product> findProductsByCateProTypeId(Integer id);

    /*根据proId查询relation*/
    @Query(value = "SELECT p.relation FROM Product p WHERE p.proId=?1")
    public String findRelationByProId(Integer id);

    /*根据特产id修改关联性relation*/
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE product p SET p.relation=? WHERE p.pro_id=?",nativeQuery = true)
    public void updateRelationByProId(String relation, Integer id);

    /*查询库存pro_stock大于0的数据*/
    @Query(value = "SELECT * FROM product WHERE pro_stock>0",nativeQuery = true)
    public List<Product> findProductsByStock();

}
