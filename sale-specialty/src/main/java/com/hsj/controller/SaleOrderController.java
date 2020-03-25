package com.hsj.controller;

import com.hsj.entity.Product;
import com.hsj.entity.SaleOrder;
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
 *@ClassName SaleOrderController
 *@Description 销售订单控制器
 *@Author hsj
 *Date 2020/3/13 16:48
 */
@Controller
@RequestMapping("/saleOrder")
public class SaleOrderController {
    @Autowired
    SaleOrderService saleOrderService;
    @Autowired
    SaleProService saleProService;
    @Autowired
    BuyProService buyProService;
    @Autowired
    ProdService prodService;
    @Autowired
    SalOrdRetService salOrdRetService;
    @Autowired
    SalPerService salPerService;
    @Autowired
    CusService cusService;


    /*获取sale_order、salechaser、warehouse三个表结合后的数据,数据类型是Page*/
    @GetMapping("/list")
    public String findAllWithPage(HttpServletRequest request, Model model,
                          @PageableDefault(size=7, sort={"orderId"}, direction= Sort.Direction.ASC) Pageable pageable){
        String URL=request.getServletPath()+"?";
        model.addAttribute("URL", URL);
        Page<SaleOrder> page=saleOrderService.findAllWithPage(pageable);
        repeatCodeTwo(page,model);
        return "sale/saleOrder";
    }

    /*查询表sale_order所有数据，返回类型是List*/
    @GetMapping("/getAllSaleOrder")
    @ResponseBody
    public List<SaleOrder> findAllWithList(){
        List<SaleOrder> list = saleOrderService.findAllWithList();
        for(SaleOrder saleOrder:list){
            repeatCodeOne(saleOrder);
        }
        return list;
    }

    /*查询所有正常的销售订单，返回类型是List*/
    @GetMapping("/getAllNormal")
    @ResponseBody
    public List<SaleOrder> findAllNormal(){
        List<SaleOrder> list = saleOrderService.findAllNormal();
        for(SaleOrder saleOrder:list){
            repeatCodeOne(saleOrder);
        }
        return list;
    }

    /*按订单编号、销售员、客户进行联合查询*/
    @GetMapping("/queryFirst")
    public String queryFirst(HttpServletRequest request, Model model,SaleOrder saleOrder,
                             @PageableDefault(size=7, sort={"orderId"}, direction= Sort.Direction.ASC) Pageable pageable){
        String path=request.getServletPath();
        String orderId=request.getParameter("orderId");
        String saleSpId=request.getParameter("saleSpId");
        String saleCusId=request.getParameter("saleCusId");
        String URL=path+"?orderId="+orderId+"&saleSpId="+saleSpId+"&saleCusId="+saleCusId+"&";
        model.addAttribute("URL",URL);
        Integer ord=saleOrder.getOrderId();
        Integer ssd=saleOrder.getSaleSpId();
        Integer scd=saleOrder.getSaleCusId();
        if (ord!=null){
            Page<SaleOrder> page=saleOrderService.findByOrderId(ord,pageable);
            repeatCodeTwo(page,model);
        }
        else{
            if (ssd==null&&scd==null){
                findAllWithPage(request,model,pageable);
            }
            if (ssd!=null&&scd==null){
                Page<SaleOrder> page=saleOrderService.findBySaleSpId(ssd,pageable);
                repeatCodeTwo(page,model);
            }
            if(ssd==null&&scd!=null){
                Page<SaleOrder> page=saleOrderService.findBySaleCusId(scd,pageable);
                repeatCodeTwo(page,model);
            }
            if (ssd!=null&&scd!=null){
                Page<SaleOrder> page=saleOrderService.findBySaleSpIdAndSaleCusId(ssd,scd,pageable);
                repeatCodeTwo(page,model);
            }
        }
        return "sale/saleOrder";
    }

    /*按客户支付方式、销售时间区间、订单类型进行联合查询*/
    @GetMapping("/querySecond")
    public String querySecond(HttpServletRequest request, Model model,SaleOrder saleOrder,
                              @PageableDefault(size=7, sort={"orderId"}, direction= Sort.Direction.ASC) Pageable pageable){
        String path=request.getServletPath();
        String payWay=request.getParameter("payWay");
        String startDate=request.getParameter("startDate");
        String endDate=request.getParameter("endDate");
        String type=request.getParameter("type");
        String URL=path+"?payWay="+payWay+"&startDate="+startDate+"&endDate="+endDate+"&type="+type+"&";
        model.addAttribute("URL",URL);
        String pw=saleOrder.getPayWay();
        Date sd=saleOrder.getStartDate();
        Date ed=saleOrder.getEndDate();
        String tp=saleOrder.getType();
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
            findAllWithPage(request,model,pageable);
        }
        if(pw!=null&&sd!=null&&tp!=null){
            Page<SaleOrder> page=saleOrderService.findByPayWayAndDateBetweenAndType(pw, sd, ed, tp, pageable);
            repeatCodeTwo(page,model);
        }
        if(pw!=null&&sd==null&&tp==null){
            Page<SaleOrder> page=saleOrderService.findByPayWay(pw,pageable);
            repeatCodeTwo(page,model);
        }
        if(pw==null&&sd!=null&&tp==null){
            Page<SaleOrder> page=saleOrderService.findByDateBetween(sd,ed,pageable);
            repeatCodeTwo(page,model);
        }
        if(pw==null&&sd==null&&tp!=null){
            Page<SaleOrder> page=saleOrderService.findByType(tp, pageable);
            repeatCodeTwo(page,model);
        }
        if(pw!=null&&sd!=null&&tp==null){
            Page<SaleOrder> page=saleOrderService.findByPayWayAndDateBetween(pw, sd, ed, pageable);
            repeatCodeTwo(page,model);
        }
        if(pw!=null&&sd==null&&tp!=null){
            Page<SaleOrder> page=saleOrderService.findByPayWayAndType(pw, tp, pageable);
            repeatCodeTwo(page,model);
        }
        if(pw==null&&sd!=null&&tp!=null){
            Page<SaleOrder> page=saleOrderService.findByDateBetweenAndType(sd, ed,tp,pageable);
            repeatCodeTwo(page,model);
        }
        return "sale/saleOrder";
    }

    /*添加单个销售订单saleOrder*/
    @PostMapping("/addSaleOrder")
    @ResponseBody
    public SaleOrder addSaleOrder(SaleOrder saleOrder){
        repeatCodeOne(saleOrder);
        saleOrder.setOrderNumber("20"+GenNumber.getOrderNo());
        saleOrder.setProMsg("暂无");
        saleOrder.setTotalPrice(new BigDecimal("0.00"));
        saleOrder.setType("正常订单");
        Integer spId=saleOrder.getSaleSpId();
        Integer cusId=saleOrder.getSaleCusId();
        try {
            saleOrderService.addSaleOrder(saleOrder);
            saleOrder.setMsg("添加成功");
            salPerService.updateRelationBySpId("已有关联，不可删除",spId);
            cusService.updateRelationByCusId("已有关联，不可删除",cusId);
        } catch (Exception e) {
            e.printStackTrace();
            saleOrder.setMsg("添加失败");
        }
        return saleOrder;
    }

    /*根据orderId查询数据*/
    @GetMapping("/findByOrderId")
    @ResponseBody
    public SaleOrder findByOrderId(HttpServletRequest request){
        String strId=request.getParameter("orderId");
        Integer orderId= null;
        try {
            orderId = Integer.parseInt(strId);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        SaleOrder sale=saleOrderService.findByOrderId(orderId);
        repeatCodeOne(sale);
        return sale;
    }

    /*修改单个SaleOrder数据*/
    @PostMapping("/modSaleOrder")
    @ResponseBody
    public SaleOrder modSaleOrder(SaleOrder saleOrder){
        Integer orderId=saleOrder.getOrderId();
        String type=saleOrderService.findOrderTypeByOrderId(orderId);
        saleOrder.setType(type);
        Integer saleSpId=saleOrderService.findSaleSpIdByOrderId(orderId);
        Integer saleCusId=saleOrderService.findSaleCusIdByOrderId(orderId);
        try {
            saleOrderService.modSaleOrder(saleOrder);
            saleOrder.setMsg("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            saleOrder.setMsg("修改失败");
        }
        repeatCodeThree(saleSpId,saleCusId);
        return saleOrder;
    }

    /*根据orderId删除数据*/
    @PostMapping("/delByOrderId")
    @ResponseBody
    public SaleOrder delByOrderId(SaleOrder saleOrder){
        Integer orderId=saleOrder.getOrderId();
        Integer saleSpId=saleOrderService.findSaleSpIdByOrderId(orderId);
        Integer saleCusId=saleOrderService.findSaleCusIdByOrderId(orderId);
        List<Integer> proIds=saleProService.findSaleProIdsBySaleOrderId(orderId);
        try {
            //删除order_id的外键绑定表sale_product的相关数据
            saleProService.delBySaleOrderId(orderId);
            //删除主表sale_order表的数据
            saleOrderService.deleteByOrderId(orderId);
            //再删除对应的退货订单表
            salOrdRetService.delSaleOrderReturnByOrderId(orderId);
            saleOrder.setMsg("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            saleOrder.setMsg("删除失败");
        }
        repeatCodeThree(saleSpId,saleCusId);
        /*更新数据表product,字段pro_stock*/
        for (Integer proId:proIds){
            updateTableProduct(proId);
        }
        return saleOrder;
    }
    
    /*对相同的代码块，进行封装使用，使得spName和cusName显示出来*/
    public void repeatCodeOne(SaleOrder saleOrder){
        Integer  saleSpId=saleOrder.getSaleSpId();
        Integer saleCusId=saleOrder.getSaleCusId();
        String spName=saleOrderService.findSpNameBySpId(saleSpId);
        String cusName=saleOrderService.findCusNameByCusId(saleCusId);
        saleOrder.setSpName(spName);
        saleOrder.setCusName(cusName);
        if (saleOrder.getProMsg()==null){
            saleOrder.setProMsg("暂无");
        }
        if (saleOrder.getTotalPrice()==null){
            saleOrder.setTotalPrice(new BigDecimal("0.00"));
        }
    }

    /*对相同的代码块，进行封装使用,遍历Page对象，使得saleManName和wareName显示出来*/
    public void repeatCodeTwo(Page<SaleOrder> page, Model model){
        List<SaleOrder> list=page.getContent();
        for (SaleOrder sale:list){
            repeatCodeOne(sale);
        }
        model.addAttribute("datas",page);
    }

    public void repeatCodeThree(Integer saleSpId,Integer saleCusId){
        Integer size1=saleOrderService.findSalesOrdersBySaleSpId(saleSpId).size();
        if (size1==0){
            salPerService.updateRelationBySpId("暂无关联，可以删除",saleSpId);
        }
        else{
            salPerService.updateRelationBySpId("已有关联，不可删除",saleSpId);
        }
        Integer size2=saleOrderService.findSalesOrdersBySaleCusId(saleCusId).size();
        if (size2==0){
            cusService.updateRelationByCusId("暂无关联，可以删除",saleCusId);
        }
        else{
            cusService.updateRelationByCusId("已有关联，不可删除",saleCusId);
        }
    }

    /*更新数据表product,字段pro_stock*/
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
    }
}
