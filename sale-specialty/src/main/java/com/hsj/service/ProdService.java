package com.hsj.service;

import com.hsj.dao.CateDao;
import com.hsj.dao.ProdDao;
import com.hsj.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/*
 *@ClassName ProdService
 *@Description
 *@Author hsj
 *Date 2020/3/1 0:03
 */
@Service
@Transactional
public class ProdService {
    @Autowired
    ProdDao prodDao;
    @Autowired
    CateDao cateDao;

    /*查找表product的所有数据*/
    public List<Product> findAll(){
        return prodDao.findAll();
    }

    /*根据特产名称进行模糊查询*/
    public List<Product> findByProNameLike(String name){
        return prodDao.findByProNameLike("%"+name+"%");
    }

    /*实现添加特产类型，等同增加单个特产,添加特产时，字段pro_stock初始为0*/
    public Product addProduct(Product product){
        product.setProStock(0);
        return prodDao.save(product);
    }

    /*查询出两个表结合后的数据*/
    public List<Product> findAllBind(){
        List<Product> list=prodDao.findAll();
        for (Product product:list){
            Integer proTypeId=product.getCateProTypeId();
            String proTypeName=cateDao.findProTypeNameByProTypeId(proTypeId);
            product.setProTypeName(proTypeName);
        }
        return list;
    }

    /*根据特产类型id查询*/
    public List<Product> findByCateProTypeId(Integer id){
        return prodDao.findByCateProTypeId(id);
    }

    /*根据特产类型名称和特产名称模糊查询*/
    public List<Product> findByCateProTypeIdAndProNameLike(Integer id,String name){
        List<Product> list=prodDao.findByCateProTypeIdAndProNameLike(id,"%"+name+"%");
        return list;
    }


    /*根据特产id查询数据*/
    public Product findByProId(Integer id){
        return prodDao.findByProId(id);
    }

    /*根据特产id查询特产存量*/
    public Integer findProStockByProId(Integer id){
        return prodDao.findProStockByProId(id);
    }

    /*执行修改特产操作1*/
    public void modProductFirst(Product product){
        Integer proId=product.getProId();
        Integer proStock=findProStockByProId(proId);
        String proImg=product.getProImg();
        if (proImg==null){
            String img = prodDao.findProImgByProId(proId);
            product.setProImg(img);
        }
        product.setProStock(proStock);
        prodDao.save(product);
    }

    /*执行修改特产操作2*/
    public void modProductSecond(Product product){prodDao.save(product);}

    /*通过id删除特产类型*/
    public void delProduct(Integer proId){
        prodDao.deleteById(proId);
    }

    /*根据特产id查询特产类型id*/
    public Integer findCateProTypeIdByProId(Integer id){return prodDao.findCateProTypeIdByProId(id);}

    /*根据特产id查询特产计价单位*/
    public String findProUnitByProId(Integer id){
        String proUnit=prodDao.findProUnitByProId(id).substring(1);
        return proUnit;
    }

    /*根据特产id查询进货价格*/
    public BigDecimal findProInPriceByProId(Integer id){
        return prodDao.findProInPriceByProId(id);
    }

    /*根据特产id查询特产名称*/
    public String findProNameByProId(Integer id){
        return prodDao.findProNameByProId(id);
    }

    /*根据proId查询buyId列表*/
    public List<Integer> findBuyIdByProId(Integer id){
        return prodDao.findBuyOrderIdByProId(id);
    }

    /*根据特产id查询该特产出货价格*/
    public BigDecimal findProOutPriceByProId(Integer id){
        return prodDao.findProOutPriceByProId(id);
    }

    /*根据cate_pro_type_id查询数据*/
    public List<Product> findProductsByCateProTypeId(Integer id){return prodDao.findProductsByCateProTypeId(id);}

    /*根据proId查询relation*/
    public String findRelationByProId(Integer id){return prodDao.findRelationByProId(id);}

    /*根据特产id修改关联性relation*/
    public void updateRelationByProId(String relation,Integer id){
        prodDao.updateRelationByProId(relation,id);
    }

    /*查询库存pro_stock大于0的数据*/
    public List<Product> findProductsByStock(){return prodDao.findProductsByStock();}
}
