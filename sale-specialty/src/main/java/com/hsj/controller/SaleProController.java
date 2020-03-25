package com.hsj.controller;

import com.hsj.entity.Product;
import com.hsj.entity.SaleOrder;
import com.hsj.entity.SaleProduct;
import com.hsj.service.BuyProService;
import com.hsj.service.ProdService;
import com.hsj.service.SaleOrderService;
import com.hsj.service.SaleProService;
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
import java.util.List;

/*
 *@ClassName SaleProController
 *@Description 销售特产控制器
 *@Author hsj
 *Date 2020/3/13 16:50
 */
@Controller
@RequestMapping("/saleProduct")
public class SaleProController {
    @Autowired
    ProdService prodService;
    @Autowired
    BuyProService buyProService;
    @Autowired
    SaleOrderService saleOrderService;
    @Autowired
    SaleProService saleProService;

    /*查询表sale_product所有数据，跳转到sale/saleProduct视图*/
    @GetMapping("/list")
    public String findAllWithPage(HttpServletRequest request, Model model,
                                  @PageableDefault(size=7, sort={"saleId"}, direction= Sort.Direction.ASC) Pageable pageable){
        String URL=request.getServletPath()+"?";
        model.addAttribute("URL", URL);
        Page<SaleProduct> page=saleProService.findAllWithPage(pageable);
        codeBlockTwo(page,model);
        return "sale/saleProduct";
    }

    /*查询所有SaleProduct,返回类型是List*/
    @GetMapping("/getAllSaleProduct")
    @ResponseBody
    public List<SaleProduct> findAllWithList(){
        List<SaleProduct> list=saleProService.findAllWithList();
        for (SaleProduct sale:list){
            codeBlockOne(sale);
        }
        return list;
    }

    /*根据saleId查询数据*/
    @GetMapping("/findBySaleId")
    @ResponseBody
    public SaleProduct findBySaleId(Integer saleId){
        System.out.println(saleId);
        SaleProduct saleProduct=saleProService.findBySaleId(saleId);
        codeBlockOne(saleProduct);
        return saleProService.findBySaleId(saleId);
    }

    /*查询一：通过特产id、所属销售订单id进行查询数据，跳转到sale/saleProduct视图*/
    @GetMapping("/queryFirst")
    public String queryFirst(HttpServletRequest request, Model model,SaleProduct saleProduct,
                             @PageableDefault(size=7, sort={"saleId"}, direction= Sort.Direction.ASC) Pageable pageable){
        String saleProId=request.getParameter("saleProId");
        String saleOrderId=request.getParameter("saleOrderId");
        String path=request.getServletPath();
        String URL=path+"?saleProId="+saleProId+"&saleOrderId="+saleOrderId+"&";
        model.addAttribute("URL",URL);
        Integer sp=saleProduct.getSaleProId();
        Integer so=saleProduct.getSaleOrderId();
        if(sp==null&&so==null){
            findAllWithPage(request,model,pageable);//调用上一个方法，表示查询无效
        }
        if(sp!=null&&so!=null){
            Page<SaleProduct> page=saleProService.findBySaleProIdAndSaleOrderId(sp,so,pageable);
            codeBlockTwo(page, model);
        }
        if(sp!=null&&so==null){
            Page<SaleProduct> page=saleProService.findBySaleProId(sp,pageable);
            codeBlockTwo(page, model);
        }
        if(sp==null&&so!=null){
            Page<SaleProduct> page=saleProService.findBySaleOrderId(so,pageable);
            codeBlockTwo(page, model);
        }
        return "sale/saleProduct";
    }

    /*查询二：通过销售数量区间，销售价格区间进行查询数据，跳转到salechase/saleProduct视图*/
    @GetMapping("/querySecond")
    public String querySecond(HttpServletRequest request, Model model,SaleProduct saleProduct,
                              @PageableDefault(size=7, sort={"saleId"}, direction= Sort.Direction.ASC) Pageable pageable){
        String startQuantity=request.getParameter("startQuantity");
        String endQuantity=request.getParameter("endQuantity");
        String startPrice=request.getParameter("startPrice");
        String endPrice=request.getParameter("endPrice");
        String path=request.getServletPath();
        String URL=path+"?startQuantity="+startQuantity+"&endQuantity="+endQuantity+"&startPrice="+startPrice+"&endPrice="+endPrice+"&";
        model.addAttribute("URL",URL);
        Integer sq=saleProduct.getStartQuantity();
        Integer eq=saleProduct.getEndQuantity();
        BigDecimal sp=saleProduct.getStartPrice();
        BigDecimal ep=saleProduct.getEndPrice();
        if(sq==null||eq==null){
            sq=null;
            eq=null;
        }
        if(sp==null||ep==null){
            sp=null;
            ep=null;
        }
        /*销售数量区间为空，销售价格区间为空*/
        if(sq==null&&sp==null){
            findAllWithPage(request,model,pageable);//表示查询无效
        }
        /*销售数量区间不为空，销售价格区间不为空*/
        if(sq!=null&&sp!=null){
            Page<SaleProduct> page=saleProService.findByQuantityBetweenAndPriceBetween(sq,eq,sp,ep,pageable);
            codeBlockTwo(page,model);
        }
        /*销售数量区间不为空，销售价格区间为空*/
        if(sq!=null&&sp==null){
            Page<SaleProduct> page=saleProService.findByQuantityBetween(sq,eq,pageable);
            codeBlockTwo(page,model);
        }
        /*销售数量区间为空，销售价格区间不为空*/
        if(sq==null&&sp!=null){
            Page<SaleProduct> page=saleProService.findByPriceBetween(sp,ep,pageable);
            codeBlockTwo(page,model);
        }
        return "sale/saleProduct";
    }

    /*新增销售特产，同时更新product和sale_order两个个表*/
    @PostMapping("/addSaleProduct")
    @ResponseBody
    public SaleProduct addSaleProduct(SaleProduct saleProduct){
        Integer proId=saleProduct.getSaleProId();
        int quantity=saleProduct.getQuantity();
        int proStock=prodService.findProStockByProId(proId);
        if (quantity>proStock){
            saleProduct.setMsg("添加失败:该特产库存数目不足，请联系采购部门采购");
            return saleProduct;
        }
        BigDecimal proOutPrice=prodService.findProOutPriceByProId(proId);
        BigDecimal price=proOutPrice.multiply(BigDecimal.valueOf(quantity));
        saleProduct.setPrice(price);
        Integer buyProId=saleProduct.getSaleProId();
        try {
            saleProService.addSaleProduct(saleProduct);
            saleProduct.setMsg("添加成功");
            buyProService.updateRelationByBuyProId("已有关联，不可删除", buyProId);
        } catch (Exception e) {
            e.printStackTrace();
            saleProduct.setMsg("添加失败");
        }
        /*更新数据表product,字段pro_stock*/
        updateTableProduct(proId);
        /*更新数据表sale_order,字段pro_msg和字段total_price*/
        updateTableSaleOrder(saleProduct.getSaleOrderId());
        return saleProduct;
    }

    /*修改SaleProduct数据*/
    @PostMapping("/modSaleProduct")
    @ResponseBody
    public SaleProduct modSaleProduct(SaleProduct saleProduct){
        Integer orderId=saleProduct.getSaleOrderId();
        String type=saleOrderService.findOrderTypeByOrderId(orderId);
        if (type.equals("退货订单")){
            saleProduct.setMsg("修改失败：该采购特产已经退货，不可以修改属性");
            return saleProduct;
        }
        Integer proId=saleProduct.getSaleProId();
        Integer quantity=saleProduct.getQuantity();
        BigDecimal proOutPrice=prodService.findProOutPriceByProId(proId);
        BigDecimal price=proOutPrice.multiply(BigDecimal.valueOf(quantity));
        saleProduct.setPrice(price);
        Integer saleId=saleProduct.getSaleId();
        Integer saleProId=saleProService.findSaleProIdBySaleId(saleId);
        try {
            saleProService.modSaleProduct(saleProduct);
            saleProduct.setMsg("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            saleProduct.setMsg("修改失败");
        }
        codeBlockThree(saleProId);
        /*更新数据表product,字段pro_stock*/
        updateTableProduct(proId);
        /*更新数据表sale_order,字段pro_msg和字段total_price*/
        updateTableSaleOrder(saleProduct.getSaleOrderId());
        return saleProduct;
    }

    /*根据buyId,删除buy_product表中的数据*/
    @PostMapping("/delSaleProduct")
    @ResponseBody
    public SaleProduct delSaleProduct(Integer saleId){
        SaleProduct saleProduct=saleProService.findBySaleId(saleId);
        Integer saleProId=saleProduct.getSaleProId();
        Integer saleOrderId=saleProduct.getSaleProId();
        try {
            saleProService.delBySaleId(saleId);
            saleProduct.setMsg("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            saleProduct.setMsg("删除失败");
        }
        codeBlockThree(saleOrderId);
        /*更新数据表product*/
        updateTableProduct(saleProId);
        /*更新数据表pur_order*/
        updateTableSaleOrder(saleOrderId);
        return saleProduct;
    }

    /*代码块封装1：为SaleProduct JavaBean的proName、orderNumber、proUnit属性赋值*/
    public void codeBlockOne(SaleProduct saleProduct){
        Integer saleProId=saleProduct.getSaleProId();
        Integer saleOrderId=saleProduct.getSaleOrderId();
        String proUnit=prodService.findProUnitByProId(saleProId);
        String proName=prodService.findProNameByProId(saleProId);
        String orderNumber=saleOrderService.findOrderNumberByOrderId(saleOrderId);
        saleProduct.setProUnit(proUnit);
        saleProduct.setProName(proName);
        saleProduct.setOrderNumber(orderNumber);
    }

    /*代码封装块2：将Page数据的content取出，使用for循环，调用代码块封装1*/
    public void codeBlockTwo(Page<SaleProduct> page, Model model){
        List<SaleProduct> list=page.getContent();
        for (SaleProduct saleProduct:list){
            codeBlockOne(saleProduct);
        }
        model.addAttribute("datas",page);
    }

    public void codeBlockThree(Integer saleProId){
        Integer size=saleProService.findSaleProductsNorBySaleProId(saleProId).size();
        if (size==0){
            buyProService.updateRelationByBuyProId("暂无关联，可以删除",saleProId);
        }
        else{
            buyProService.updateRelationByBuyProId("已有关联，不可删除",saleProId);
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

    /*更新数据表sale_order,字段pro_msg和字段total_price*/
    public void updateTableSaleOrder(Integer saleOrderId){
        System.out.println("saleOrderId="+saleOrderId);
        List<SaleProduct> list=saleProService.findSaleProductsBySaleOrderId(saleOrderId);
        StringBuffer proMsg=new StringBuffer();
        int i=1;
        /*更新字段pro_msg的值*/
        for (SaleProduct sp:list){
            codeBlockOne(sp);
            proMsg.append(i+"、"+sp.toString()+"\r\n");
            i++;
        }
        SaleOrder saleOrder=saleOrderService.findByOrderId(saleOrderId);
        if(proMsg.length()>0){
            saleOrder.setProMsg(new String(proMsg));
            /*更新字段total_price的值*/
            BigDecimal totalPrice=saleProService.findPriceSumBySaleOrderId(saleOrderId);
            saleOrder.setTotalPrice(totalPrice);
        }
        try {
            saleOrderService.modSaleOrder(saleOrder);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
