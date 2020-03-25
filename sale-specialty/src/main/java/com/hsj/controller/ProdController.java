package com.hsj.controller;

import com.hsj.entity.*;
import com.hsj.service.*;
import com.hsj.util.ChangToPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.List;

/*
 *@ClassName ProdController
 *@Description 特产信息管理控制器
 *@Author hsj
 *Date 2020/2/27 20:21
 */
@Controller
@RequestMapping("/product")
public class ProdController {
    @Autowired
    CateService cateService;
    @Autowired
    ProdService prodService;
    @Autowired
    BuyProService buyProService;
    @Autowired
    UploadService uploadService;
    @Autowired
    SaleProService saleProService;
    @Autowired
    PurOrderService purOrderService;
    @Autowired
    SaleOrderService saleOrderService;

    /*跳转到product/admProduct页面。页面返回所有特产*/
    @GetMapping("/list")
    public String getAllProductPage(HttpServletRequest request,Model model,@PageableDefault(size=7, sort={"proId"}, direction= Sort.Direction.ASC) Pageable pageable){
        String proTypeName=request.getParameter("proTypeName"),proName=request.getParameter("proName");
        String URL=request.getServletPath()+"?";
        Page<Product> page=getPageData(pageable);
        model.addAttribute("proTypeName", proTypeName);
        model.addAttribute("proName", proName);
        model.addAttribute("URL", URL);
        model.addAttribute("datas",page);
        return "product/admProduct";
    }

    /*获取表product所有数据，返回类型是List*/
    @GetMapping("/getAllProduct")
    @ResponseBody
    public List<Product> getAllProductList(){
        return prodService.findAll();
    }
    //实现表product和表Category外键字段proId的映射，给Product对象的proType属性赋值
    //并且将数据封装成Page<T>类型
    public Page<Product> getPageData(Pageable pageable){
        List<Product> proList=prodService.findAllBind();
        for (Product product:proList){
            Integer proStock=product.getProStock();
            if (proStock==null){
                product.setProStock(0);
            }
        }
        ChangToPage<Product> changToPage=new ChangToPage<>();
        return changToPage.getPage(proList,pageable);
    }

    //上传特产图片
    @PostMapping("/upload")
    @ResponseBody
    public Product uploading(@RequestParam("file") MultipartFile file) throws IOException {
        Product product=new Product();
        try {
            uploadService.uploadFile(file.getBytes(),file.getOriginalFilename());
            String proImg="/upload/img"+file.getOriginalFilename();
            product.setMsg("上传成功！");
            product.setProImg(proImg);
        } catch (IOException e) {
            e.printStackTrace();
            product.setMsg("上传失败！");
        }
        return product;
    }

    //实现添加特产类型，等同增加单个特产
    @PostMapping("/addProduct")
    @ResponseBody
    public Product addProduct(Product product){
        Integer proTypeId=product.getCateProTypeId();
        String proTypeName=cateService.findProTypeNameByProTypeId(proTypeId);
        product.setProTypeName(proTypeName);
        product.setRelation("暂无关联，可以删除");
        try {
            prodService.addProduct(product);
            product.setMsg("添加成功");
            cateService.updateRelationByProTypeId("已有关联，不可删除",proTypeId);
        } catch (Exception e) {
            e.printStackTrace();
            product.setMsg("添加失败");
        }
        return product;
    }

    //查询特产
    @GetMapping("/queryProduct")
    public String queryProduct(HttpServletRequest request,Product product, Model model, @PageableDefault(size=5, sort={"proId"}, direction= Sort.Direction.ASC) Pageable pageable) throws UnsupportedEncodingException {
        String proTypeId = request.getParameter("proTypeId"),proName=request.getParameter("proName");
        String URL=request.getServletPath()+"?proTypeId="+proTypeId+"&proName="+proName+"&";
        model.addAttribute("proTypeId", proTypeId);
        model.addAttribute("proName", proName);
        model.addAttribute("URL", URL);
        Integer pti=product.getCateProTypeId();
        String pn=product.getProName();

        //两者都不空
        if(pti!=null&&pn!=null){
            List<Product> list=prodService.findByCateProTypeIdAndProNameLike(pti,pn);
            packData(list, model,pageable);
        }
        //特产类型空、特产名称空
        else if(pti==null&&pn==null){
            getAllProductPage(request,model,pageable);
        }
        //特产类型空、特产名称不空
        else if(pti==null&&pn!=null){
            List<Product> list=prodService.findByProNameLike(pn);
            packData(list, model,pageable);
        }
        else if(pti!=null&&pn==null){
            List<Product> list=prodService.findByCateProTypeId(pti);
            packData(list, model,pageable);
        }
        return "product/admProduct";
    }

    public void packData(List<Product> list, Model model,Pageable pageable){
        for (Product product:list){
            Integer proStock=product.getProStock();
            if (proStock==null){
                product.setProStock(0);
            }
            product.setProTypeName(cateService.findProTypeNameByProTypeId(product.getCateProTypeId()));
        }
        //将数据封装成Page类型,并且存进model
        ChangToPage<Product> changToPage=new ChangToPage<>();
        Page<Product> page=changToPage.getPage(list,pageable);
        model.addAttribute("datas", page);
    }

    //根据特产id查询和返回数据
    @GetMapping("/getMsg")
    @ResponseBody
    public Product findByProId(Integer proId){
        Product product=prodService.findByProId(proId);
        Integer proTypeId=prodService.findCateProTypeIdByProId(proId);
        String proTypeName=cateService.findProTypeNameByProTypeId(proTypeId);
        String proUnit=product.getProUnit().substring(1);
        product.setProUnit(proUnit);
        product.setProTypeName(proTypeName);
        return product;
    }

    //执行修改特产操作
    @PostMapping("/modProduct")
    @ResponseBody
    public Product modProduct(@ModelAttribute Product product){
        Integer proId=product.getProId();
        String relation=prodService.findRelationByProId(proId);
        Integer proTypeId=prodService.findCateProTypeIdByProId(proId);
        product.setRelation(relation);
        try {
            prodService.modProductFirst(product);
            product.setMsg("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            product.setMsg("修改失败");
        }
        codeBlockOne(proTypeId);
        List<BuyProduct> buyProducts=buyProService.findBuyProductsByProId(proId);
        for (BuyProduct buy:buyProducts){
            Integer buyOrderId=buy.getBuyOrderId();
            updateTablePurOrder(buyOrderId);
            updateTableSaleOrder(buyOrderId);
        }
        return product;
    }

    //通过id删除特产
    @PostMapping("/delProduct")
    @ResponseBody
    public Product delProType(@ModelAttribute Product product){
        Integer proId=product.getProId();
        Integer proTypeId=prodService.findCateProTypeIdByProId(proId);
        try {
            prodService.delProduct(product.getProId());
            product.setMsg("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            product.setMsg("删除失败");
        }
        codeBlockOne(proTypeId);
        return product;
    }

    public void codeBlockOne(Integer proTypeId){
        Integer size=prodService.findProductsByCateProTypeId(proTypeId).size();
        if (size==0){
            cateService.updateRelationByProTypeId("暂无关联，可以删除",proTypeId);
        }
        else{
            cateService.updateRelationByProTypeId("已有关联，不可删除",proTypeId);
        }
    }

    /*查询库存pro_stock大于0的数据*/
    @GetMapping("/findProductsByStock")
    @ResponseBody
    public List<Product> findProductsByStock(){
        return prodService.findProductsByStock();
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

    /*更新数据表sale_order,字段pro_msg和字段total_price*/
    public void updateTableSaleOrder(Integer saleOrderId){
        List<SaleProduct> list=saleProService.findSaleProductsBySaleOrderId(saleOrderId);
        StringBuffer proMsg=new StringBuffer();
        int i=1;
        /*更新字段pro_msg的值*/
        for (SaleProduct sp:list){
            codeBlockTwo(sp);
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

    /*代码块封装2：为SaleProduct JavaBean的proName、orderNumber、proUnit属性赋值*/
    public void codeBlockTwo(SaleProduct saleProduct){
        Integer saleProId=saleProduct.getSaleProId();
        Integer saleOrderId=saleProduct.getSaleOrderId();
        String proUnit=prodService.findProUnitByProId(saleProId);
        String proName=prodService.findProNameByProId(saleProId);
        String orderNumber=saleOrderService.findOrderNumberByOrderId(saleOrderId);
        saleProduct.setProUnit(proUnit);
        saleProduct.setProName(proName);
        saleProduct.setOrderNumber(orderNumber);
    }
}
