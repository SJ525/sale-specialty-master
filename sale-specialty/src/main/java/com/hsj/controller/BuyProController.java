package com.hsj.controller;

import com.hsj.entity.BuyProduct;
import com.hsj.entity.Product;
import com.hsj.entity.PurOrder;
import com.hsj.entity.PurOrderReturn;
import com.hsj.service.*;
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
import java.util.ArrayList;
import java.util.List;

/*
 *@ClassName BuyProController
 *@Description 采购特产控制器
 *@Author hsj
 *Date 2020/3/9 15:37
 */
@Controller
@RequestMapping("/buyProduct")
public class BuyProController {
    @Autowired
    ProdService prodService;
    @Autowired
    PurOrderService purOrderService;
    @Autowired
    BuyProService buyProService;
    @Autowired
    SaleProService saleProService;
    @Autowired
    PurOrdRetService purOrdRetService;

    /*查询表buy_product所有数据，跳转到purchase/buyProduct视图*/
    @GetMapping("/list")
    public String findAllWithPage(HttpServletRequest request, Model model,
                                  @PageableDefault(size=7, sort={"buyId"}, direction= Sort.Direction.ASC) Pageable pageable){
        String URL=request.getServletPath()+"?";
        model.addAttribute("URL", URL);
        Page<BuyProduct> page=buyProService.findAllWithPage(pageable);
        codeBlockTwo(page,model);
        return "purchase/buyProduct";
    }

    /*查询表buy_order的所有数据，以List类型返回*/
    @GetMapping("/getAllBuyProduct")
    @ResponseBody
    public List<Product> findAllWithList(){
        List<Integer> proIds=buyProService.findAllProIds();
        List<Product> products=new ArrayList<>();
        for (Integer proId:proIds){
            Product product=prodService.findByProId(proId);
            products.add(product);
        }
        return products;
    }

    /*通过buyId查询数据，返回BuyProduct类型*/
    @GetMapping("/getMsgById")
    @ResponseBody
    public BuyProduct findByBuyId(BuyProduct buyProduct){
        Integer buyId=buyProduct.getBuyId();
        BuyProduct buyProduct1=buyProService.findByBuyId(buyId);
        codeBlockOne(buyProduct1);
        return buyProduct1;
    }

    /*查询一：通过特产id、所属采购订单id和供应商id进行查询数据，跳转到purchase/buyProduct视图*/
    @GetMapping("/queryFirst")
    public String queryFirst(HttpServletRequest request, Model model,BuyProduct buyProduct,
                             @PageableDefault(size=7, sort={"buyId"}, direction= Sort.Direction.ASC) Pageable pageable){
        String buyProId=request.getParameter("buyProId");
        String buyOrderId=request.getParameter("buyOrderId");
        String buySupId=request.getParameter("buySupId");
        String path=request.getServletPath();
        String URL=path+"?buyProId="+buyProId+"&buyOrderId="+buyOrderId+"&buySupId="+buySupId+"&";
        model.addAttribute("URL",URL);
        Integer bpd=buyProduct.getBuyProId();
        Integer bod=buyProduct.getBuyOrderId();
        Integer bsd=buyProduct.getBuySupId();
        /*特产名称为空、所属采购订单为空、供应商为空*/
        if(bpd==null&&bod==null&&bsd==null){
            findAllWithPage(request,model,pageable);//调用上一个方法，表示查询无效
        }
        /*特产名称、所属采购订单、供应商都不为空*/
        if(bpd!=null&&bod!=null&&bsd!=null){
            Page<BuyProduct> page=buyProService.findByBuyProIdAndBuyOrderIdAndBuySupId(bpd,bod,bsd,pageable);
            codeBlockTwo(page,model);
        }
        /*特产名称不为空、所属采购订单为空、供应商为空*/
        if(bpd!=null&&bod==null&&bsd==null){
            Page<BuyProduct> page=buyProService.findByBuyProId(bpd,pageable);
            codeBlockTwo(page,model);
        }
        /*特产名称为空、所属采购订单不为空、供应商为空*/
        if(bpd==null&&bod!=null&&bsd==null){
            Page<BuyProduct> page=buyProService.findByBuyOrderId(bod,pageable);
            codeBlockTwo(page,model);
        }
        /*特产名称为空、所属采购订单为空、供应商不为空*/
        if(bpd==null&&bod==null&&bsd!=null){
            Page<BuyProduct> page=buyProService.findByBuySupId(bsd,pageable);
            codeBlockTwo(page,model);
        }
        /*特产名称不为空、所属采购订单不为空、供应商为空*/
        if(bpd!=null&&bod!=null&&bsd==null){
            Page<BuyProduct> page=buyProService.findByBuyProIdAndBuyOrderId(bpd,bod,pageable);
            codeBlockTwo(page,model);
        }
        /*特产名称不为空、所属采购订单为空、供应商不为空*/
        if(bpd!=null&&bod==null&&bsd!=null){
            Page<BuyProduct> page=buyProService.findByBuyProIdAndBuySupId(bpd,bsd,pageable);
            codeBlockTwo(page,model);
        }
        /*特产名称为空、所属采购订单不为空、供应商不为空*/
        if(bpd==null&&bod!=null&&bsd!=null){
            Page<BuyProduct> page=buyProService.findByBuyOrderIdAndBuySupId(bod,bsd,pageable);
            codeBlockTwo(page,model);
        }
        return "purchase/buyProduct";
    }

    /*查询二：通过采购数量区间，采购价格区间进行查询数据，跳转到purchase/buyProduct视图*/
    @GetMapping("/querySecond")
    public String querySecond(HttpServletRequest request, Model model,BuyProduct buyProduct,
                              @PageableDefault(size=7, sort={"buyId"}, direction= Sort.Direction.ASC) Pageable pageable){
        String startQuantity=request.getParameter("startQuantity");
        String endQuantity=request.getParameter("endQuantity");
        String startPrice=request.getParameter("startPrice");
        String endPrice=request.getParameter("endPrice");
        String path=request.getServletPath();
        String URL=path+"?startQuantity="+startQuantity+"&endQuantity="+endQuantity+"&startPrice="+startPrice+"&endPrice="+endPrice+"&";
        model.addAttribute("URL",URL);
        Integer sq=buyProduct.getStartQuantity();
        Integer eq=buyProduct.getEndQuantity();
        BigDecimal sp=buyProduct.getStartPrice();
        BigDecimal ep=buyProduct.getEndPrice();
        if(sq==null||eq==null){
            sq=null;
            eq=null;
        }
        if(sp==null||ep==null){
            sp=null;
            ep=null;
        }
        /*采购数量区间为空，采购价格区间为空*/
        if(sq==null&&sp==null){
            findAllWithPage(request,model,pageable);//表示查询无效
        }
        /*采购数量区间不为空，采购价格区间不为空*/
        if(sq!=null&&sp!=null){
            Page<BuyProduct> page=buyProService.findByQuantityBetweenAndPriceBetween(sq,eq,sp,ep,pageable);
            codeBlockTwo(page,model);
        }
        /*采购数量区间不为空，采购价格区间为空*/
        if(sq!=null&&sp==null){
            Page<BuyProduct> page=buyProService.findByQuantityBetween(sq,eq,pageable);
            codeBlockTwo(page,model);
        }
        /*采购数量区间为空，采购价格区间不为空*/
        if(sq==null&&sp!=null){
            Page<BuyProduct> page=buyProService.findByPriceBetween(sp,ep,pageable);
            codeBlockTwo(page,model);
        }
        return "purchase/buyProduct";
    }

    /*新增采购特产，同时更新buy_product、product和pur_order两个个表*/
    @PostMapping("/addBuyProduct")
    @ResponseBody
    public BuyProduct addBuyProduct(BuyProduct buyProduct){
        Integer proId=buyProduct.getBuyProId();
        Integer quantity=buyProduct.getQuantity();
        BigDecimal proInPrice=prodService.findProInPriceByProId(proId);
        BigDecimal price=proInPrice.multiply(BigDecimal.valueOf(quantity));
        buyProduct.setPrice(price);
        if(saleProService.findSaleProductsBySaleProId(proId).size()!=0){
            buyProduct.setRelation("已有关联，不可删除");
        }
        else {
            buyProduct.setRelation("暂无关联，可以删除");
        }
        try {
            buyProService.addBuyProduct(buyProduct);
            buyProduct.setMsg("添加成功");
            prodService.updateRelationByProId("已有关联，不可删除",proId);
        } catch (Exception e) {
            e.printStackTrace();
            buyProduct.setMsg("添加失败");
        }
        /*更新数据表product*/
        updateTableProduct(buyProduct.getBuyProId());
        /*更新数据表pur_order*/
        updateTablePurOrder(buyProduct.getBuyOrderId());
        return buyProduct;
    }

    /*修改buy_product表中的数据*/
    @PostMapping("/modBuyProduct")
    @ResponseBody
    public BuyProduct modBuyProduct(BuyProduct buyProduct){
        Integer orderId=buyProduct.getBuyOrderId();
        String type=purOrderService.findOrderTypeByOrderId(orderId);
        if (type.equals("退货订单")){
            buyProduct.setMsg("修改失败：该采购特产已经退货，不可以修改属性");
            return buyProduct;
        }
        Integer buyId=buyProduct.getBuyId();
        Integer proId=buyProduct.getBuyProId();
        Integer quantity=buyProduct.getQuantity();
        String relation=buyProService.findRelationByBuyId(buyId);
        BigDecimal proInPrice=prodService.findProInPriceByProId(proId);
        BigDecimal price=proInPrice.multiply(BigDecimal.valueOf(quantity));
        buyProduct.setPrice(price);
        buyProduct.setRelation(relation);
        try {
            buyProService.modBuyProduct(buyProduct);
            buyProduct.setMsg("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            buyProduct.setMsg("修改失败");
        }
        Integer size=buyProService.findBuyProductsByProId(proId).size();
        if (size==0){
            prodService.updateRelationByProId("暂无关联，可以删除",proId);
        }
        else{
            prodService.updateRelationByProId("已有关联，不可删除",proId);
        }
        /*更新数据表product*/
        updateTableProduct(buyProduct.getBuyProId());
        /*更新数据表pur_order*/
        updateTablePurOrder(buyProduct.getBuyOrderId());
        return buyProduct;
    }

    /*根据buyId,删除buy_product表中的数据*/
    @PostMapping("/delBuyProduct")
    @ResponseBody
    public BuyProduct delBuyProduct(Integer buyId){
        BuyProduct buyProduct=buyProService.findByBuyId(buyId);
        Integer proId=buyProduct.getBuyProId();
        Integer orderId=buyProduct.getBuyOrderId();
        try {
            buyProService.delBuyProduct(buyId);
            buyProduct.setMsg("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            buyProduct.setMsg("删除失败");
        }
        Integer size=buyProService.findBuyProductsByProId(proId).size();
        if (size==0){
            prodService.updateRelationByProId("暂无关联，可以删除",proId);
        }
        else{
            prodService.updateRelationByProId("已有关联，不可删除",proId);
        }
        /*更新数据表product*/
        updateTableProduct(proId);
        /*更新数据表pur_order*/
        updateTablePurOrder(orderId);
        /*更新表pur_order_return*/
        updateTablePurOrderReturn(orderId);
        return buyProduct;
    }

    /*代码块封装1：为BuyProduct JavaBean的proName、orderNumber、proUnit和supName属性赋值*/
    public void codeBlockOne(BuyProduct buyProduct){
        Integer buyProId=buyProduct.getBuyProId();
        Integer buyOrderId=buyProduct.getBuyOrderId();
        Integer buySupId=buyProduct.getBuySupId();
        String proUnit=prodService.findProUnitByProId(buyProId);
        String proName=prodService.findProNameByProId(buyProId);
        String orderNumber=purOrderService.findOrderNumberByOrderId(buyOrderId);
        String supName=buyProService.findSupNameByBuySupId(buySupId);
        buyProduct.setProUnit(proUnit);
        buyProduct.setProName(proName);
        buyProduct.setOrderNumber(orderNumber);
        buyProduct.setSupName(supName);
    }

    /*代码封装块2：将Page数据的content取出，使用for循环，调用代码块封装1*/
    public void codeBlockTwo(Page<BuyProduct> page,Model model){
        List<BuyProduct> list=page.getContent();
        for (BuyProduct buyProduct:list){
            codeBlockOne(buyProduct);
        }
        model.addAttribute("datas",page);
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

    /*更新数据表pur_order,字段pro_msg和字段total_price*/
    public void updateTablePurOrder(Integer buyOrderId){
        List<BuyProduct> list=buyProService.findBuyProductsByBuyOrderId(buyOrderId);
        /*更新字段pro_msg的值*/
        for (BuyProduct bp1:list){
            codeBlockOne(bp1);
        }
        StringBuffer proMsg=new StringBuffer();
        int i=1;
        for (BuyProduct bp2:list){
            proMsg.append(i+"、"+bp2.toString()+"\r\n");
            i++;
        }
        PurOrder purOrder=purOrderService.findByOrderId(buyOrderId);
        if(proMsg.length()>0){
            purOrder.setProMsg(new String(proMsg));
            /*更新字段total_price的值*/
            BigDecimal totalPrice=buyProService.findPriceSumByBuyOrderId(buyOrderId);
            purOrder.setTotalPrice(totalPrice);
        }
        try {
            purOrderService.modPurOrder(purOrder);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*更新表pur_order_return*/
    public void updateTablePurOrderReturn(Integer buyOrderId){
        String msg=purOrderService.findByOrderId(buyOrderId).getProMsg();
        if (purOrdRetService.findByOrderId(buyOrderId).size()!=0){
            PurOrderReturn purOrderReturn=purOrdRetService.findByOrderId(buyOrderId).get(0);
            purOrderReturn.setMsg(msg);
            purOrdRetService.update(purOrderReturn);
        }
    }
}
