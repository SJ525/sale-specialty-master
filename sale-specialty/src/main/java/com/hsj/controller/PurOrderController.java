package com.hsj.controller;

import com.hsj.entity.Product;
import com.hsj.entity.PurOrder;
import com.hsj.service.*;
import com.hsj.util.GenNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/*
 *@ClassName PurOrderController
 *@Description 采购订单-控制器类
 *@Author hsj
 *Date 2020/3/8 11:47
 */
@Controller
@RequestMapping("/purOrder")
public class PurOrderController {
    @Autowired
    ProdService prodService;
    @Autowired
    PurOrderService purOrderService;
    @Autowired
    PurManService purManService;
    @Autowired
    BuyProService buyProService;
    @Autowired
    PurOrdRetService purOrdRetService;
    @Autowired
    WareService wareService;
    @Autowired
    SaleProService saleProService;

    /*获取pur_order、purchaser、warehouse三个表结合后的数据,数据类型是Page*/
    @GetMapping("/list")
    public String findAll(HttpServletRequest request, Model model,
                          @PageableDefault(size=7, sort={"orderId"}, direction= Sort.Direction.ASC) Pageable pageable){
        String URL=request.getServletPath()+"?";
        model.addAttribute("URL", URL);
        Page<PurOrder> page=purOrderService.findAllWithPage(pageable);
        repeatCodeOneTwo(page,model);
        model.addAttribute("datas", page);
        return "purchase/purOrder";
    }

    /*添加单个采购订单purOrder*/
    @PostMapping("/addPurOrder")
    @ResponseBody
    public PurOrder addPurOrder(PurOrder purOrder){
        repeatCodeOne(purOrder);
        purOrder.setOrderNumber("10"+GenNumber.getOrderNo());
        purOrder.setProMsg("暂无");
        purOrder.setTotalPrice(new BigDecimal("0.00"));
        purOrder.setType("正常订单");
        Integer purId=purOrder.getPurManId();
        Integer wareId=purOrder.getPurWareId();
        try {
            purOrderService.addPurOrder(purOrder);
            purOrder.setMsg("添加成功");
            purManService.updateRelationByPurId("已有关联，不可删除",purId);
            wareService.updateRelationByWareId("已有关联，不可删除",wareId);
        } catch (Exception e) {
            e.printStackTrace();
            purOrder.setMsg("添加失败");
        }
        return purOrder;
    }

    /*查询表pur_order所有数据，返回类型是List*/
    @GetMapping("/getAllPurOrder")
    @ResponseBody
    public List<PurOrder> findAllWithList(){
        List<PurOrder> list = purOrderService.findAllWithList();
        for(PurOrder purOrder:list){
            repeatCodeOne(purOrder);
        }
        return list;
    }

    /*查询所有正常的采购订单，返回类型是List*/
    @GetMapping("/getAllNormal")
    @ResponseBody
    public List<PurOrder> findAllNormal(){
        List<PurOrder> list = purOrderService.findAllNormal();
        for(PurOrder purOrder:list){
            repeatCodeOne(purOrder);
        }
        return list;
    }

    /*按订单编号、采购员、所入仓库进行联合查询*/
    @GetMapping("/queryFirst")
    public String queryFirst(HttpServletRequest request, Model model,PurOrder purOrder,
                             @PageableDefault(size=7, sort={"orderId"}, direction= Sort.Direction.ASC) Pageable pageable){
        String path=request.getServletPath();
        String orderId=request.getParameter("orderId");
        String purManId=request.getParameter("purManId");
        String purWareId=request.getParameter("purWareId");
        String URL=path+"?orderId="+orderId+"&purManId="+purManId+"&purWareId="+purWareId+"&";
        model.addAttribute("URL",URL);
        Integer ord=purOrder.getOrderId();
        Integer pmd=purOrder.getPurManId();
        Integer pwd=purOrder.getPurWareId();
        if (ord!=null){
            Page<PurOrder> page=purOrderService.findByOrderId(ord,pageable);
            repeatCodeOneTwo(page,model);
        }
        else{
            if (pmd==null&&pwd==null){
                findAll(request,model,pageable);
            }
            if (pmd!=null&&pwd==null){
                Page<PurOrder> page=purOrderService.findByPurManId(pmd,pageable);
                repeatCodeOneTwo(page,model);
            }
            if(pmd==null&&pwd!=null){
                Page<PurOrder> page=purOrderService.findByPurWareId(pwd,pageable);
                repeatCodeOneTwo(page,model);
            }
            if (pmd!=null&&pwd!=null){
                Page<PurOrder> page=purOrderService.findByPurManIdAndPurWareId(pmd,pwd,pageable);
                repeatCodeOneTwo(page,model);
            }
        }
        return "purchase/purOrder";
    }

    /*按客户支付方式、销售时间区间、订单类型进行联合查询*/
    @GetMapping("/querySecond")
    public String querySecond(HttpServletRequest request, Model model,PurOrder purOrder,
                             @PageableDefault(size=7, sort={"orderId"}, direction= Sort.Direction.ASC) Pageable pageable){
        String path=request.getServletPath();
        String payWay=request.getParameter("payWay");
        String startDate=request.getParameter("startDate");
        String endDate=request.getParameter("endDate");
        String type=request.getParameter("type");
        String URL=path+"?payWay="+payWay+"&startDate="+startDate+"&endDate="+endDate+"&type="+type+"&";
        model.addAttribute("URL",URL);
        String pw=purOrder.getPayWay();
        Date sd=purOrder.getStartDate();
        Date ed=purOrder.getEndDate();
        String tp=purOrder.getType();
        if(pw.equals("-请选择-")){
            pw=null;
        }
        if(sd==null||ed==null){
            sd=null;
            ed=null;
        }
        if(tp.equals("-请选择-")){
            tp=null;
        }
        if(pw==null&&sd==null&&tp==null){
            findAll(request,model,pageable);
        }
        if(pw!=null&&sd!=null&&tp!=null){
            Page<PurOrder> page=purOrderService.findByPayWayAndDateBetweenAndType(pw, sd, ed, tp, pageable);
            repeatCodeOneTwo(page,model);
        }
        if(pw!=null&&sd==null&&tp==null){
            Page<PurOrder> page=purOrderService.findByPayWay(pw,pageable);
            repeatCodeOneTwo(page,model);
        }
        if(pw==null&&sd!=null&&tp==null){
            Page<PurOrder> page=purOrderService.findByDateBetween(sd,ed,pageable);
            repeatCodeOneTwo(page,model);
        }
        if(pw==null&&sd==null&&tp!=null){
            Page<PurOrder> page=purOrderService.findByType(tp, pageable);
            repeatCodeOneTwo(page,model);
        }
        if(pw!=null&&sd!=null&&tp==null){
            Page<PurOrder> page=purOrderService.findByPayWayAndDateBetween(pw, sd, ed, pageable);
            repeatCodeOneTwo(page,model);
        }
        if(pw!=null&&sd==null&&tp!=null){
            Page<PurOrder> page=purOrderService.findByPayWayAndType(pw, tp, pageable);
            repeatCodeOneTwo(page,model);
        }
        if(pw==null&&sd!=null&&tp!=null){
            Page<PurOrder> page=purOrderService.findByDateBetweenAndType(sd, ed,tp,pageable);
            repeatCodeOneTwo(page,model);
        }
        return "purchase/purOrder";
    }

    /*根据orderId查询数据*/
    @GetMapping("/findByOrderId")
    @ResponseBody
    public PurOrder findByOrderId(HttpServletRequest request){
        String strId=request.getParameter("orderId");
        Integer orderId= null;
        try {
            orderId = Integer.parseInt(strId);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        PurOrder pur=purOrderService.findByOrderId(orderId);
        repeatCodeOne(pur);
        return pur;
    }

    /*修改单个PurOrder数据*/
    @PostMapping("/modPurOrder")
    @ResponseBody
    public PurOrder modPurOrder(PurOrder purOrder){
        Integer orderId=purOrder.getOrderId();
        String type=purOrderService.findOrderTypeByOrderId(orderId);
        purOrder.setType(type);
        Integer purManId=purOrder.getPurManId();
        Integer purWareId=purOrder.getPurWareId();
        try {
            purOrderService.modPurOrder(purOrder);
            purOrder.setMsg("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            purOrder.setMsg("修改失败");
        }
        repeatCodeThree(purManId,purWareId);
        return purOrder;
    }

    /*根据orderId删除数据*/
    @PostMapping("/delByOrderId")
    @ResponseBody
    public PurOrder delByOrderId(PurOrder purOrder){
        Integer orderId=purOrder.getOrderId();
        Integer purManId=purOrderService.findPurManIdByOrderId(orderId);
        Integer purWareId=purOrderService.findPurWareIdByOrderId(orderId);
        List<Integer> proIds=buyProService.findBuyProIdsByBuyOrderId(orderId);
        try {
            for (Integer proId:proIds){
                saleProService.deleteBySaleProId(proId);
            }
            //删除order_id的外键绑定表buy_product的相关数据
            buyProService.deleteByBuyOrderId(orderId);
            //删除主表pur_order表的数据
            purOrderService.deleteByOrderId(orderId);
            //删除对应的退货订单表
            purOrdRetService.delPurOrderReturnByOrderId(orderId);
            purOrder.setMsg("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            purOrder.setMsg("删除失败");
        }
        repeatCodeThree(purManId,purWareId);
        /*更新数据表product,字段pro_stock*/
        for (Integer proId:proIds){
            updateTableProduct(proId);
        }
        return purOrder;
    }

    /*对相同的代码块，进行封装使用，使得purManName和wareName显示出来*/
    public void repeatCodeOne(PurOrder purOrder){
        Integer purManId=purOrder.getPurManId();
        Integer purWareId=purOrder.getPurWareId();
        String purManName=purOrderService.findPurchaserPurName(purManId);
        String wareName=purOrderService.findWarehouseWareName(purWareId);
        purOrder.setPurManName(purManName);
        purOrder.setWareName(wareName);
        if (purOrder.getProMsg()==null){
            purOrder.setProMsg("暂无");
        }
        if (purOrder.getTotalPrice()==null){
            purOrder.setTotalPrice(new BigDecimal("0.00"));
        }
    }

    /*对相同的代码块，进行封装使用,遍历Page对象，使得purManName和wareName显示出来*/
    public void repeatCodeOneTwo(Page<PurOrder> page,Model model){
        List<PurOrder> list=page.getContent();
        for (PurOrder pur:list){
            repeatCodeOne(pur);
        }
        model.addAttribute("datas",page);
    }

    public void repeatCodeThree(Integer purManId,Integer purWareId){
        Integer size1=purOrderService.findPurOrdersByPurManId(purManId).size();
        if (size1==0){
            purManService.updateRelationByPurId("暂无关联，可以删除",purManId);
        }
        else{
            purManService.updateRelationByPurId("已有关联，不可删除",purManId);
        }
        Integer size2=purOrderService.findPurOrdersByPurWareId(purWareId).size();
        if (size2==0){
            wareService.updateRelationByWareId("暂无关联，可以删除",purWareId);
        }
        else{
            wareService.updateRelationByWareId("已有关联，不可删除",purWareId);
        }
    }
    /*更新数据表product,字段pro_stock和字段relation*/
    public void updateTableProduct(Integer proId){
        int buyQuantity=0,saleQuantity=0;
        if(buyProService.findBuyProductsByProId(proId).size()!=0){
            buyQuantity=buyProService.findBuyQuantitySumNor(proId);
        }
        if(saleProService.findSaleProductsNorBySaleProId(proId).size()!=0){
            saleQuantity=saleProService.findSaleQuantitySumNor(proId);
        }
        int quantity=buyQuantity-saleQuantity;
        Product product=prodService.findByProId(proId);
        product.setProStock(quantity);
        try {
            prodService.modProductSecond(product);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Integer size=buyProService.findBuyProductsByProId(proId).size();
        if (size==0){
            prodService.updateRelationByProId("暂无关联，可以删除",proId);
        }
        else{
            prodService.updateRelationByProId("已有关联，不可删除",proId);
        }
    }
}
