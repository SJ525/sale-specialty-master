package com.hsj.controller;

import com.hsj.entity.Product;
import com.hsj.entity.SaleOrderReturn;
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
import java.util.Date;
import java.util.List;

/*
 *@ClassName SalOrdRetController
 *@Description 销售退货订单控制器
 *@Author hsj
 *Date 2020/3/13 16:52
 */
@Controller
@RequestMapping("/saleOrderReturn")
public class SalOrdRetController {
    @Autowired
    SalOrdRetService salOrdRetService;
    @Autowired
    ProdService prodService;
    @Autowired
    SaleProService saleProService;
    @Autowired
    SaleOrderService saleOrderService;
    @Autowired
    BuyProService buyProService;

    /*查询表sale_order_return所有数据，以Page类型返回,跳转到视图sale/saleOrderReturn*/
    @GetMapping("/list")
    public String findAllWithPage(HttpServletRequest request, Model model,
                                  @PageableDefault(size=8, sort={"returnId"}, direction= Sort.Direction.ASC) Pageable pageable){
        String URL=request.getServletPath()+"?";
        model.addAttribute("URL", URL);
        Page<SaleOrderReturn> page=salOrdRetService.findAllWithPage(pageable);
        model.addAttribute("datas", page);
        return "sale/saleOrderReturn";
    }

    /*查询表sale_order_return所有数据，以List类型返回*/
    @GetMapping("/getAllSaleOrderReturn")
    @ResponseBody
    public List<SaleOrderReturn> findAllWithLst(){
        return salOrdRetService.findAllWithList();
    }

    /*根据各种条件对表格表sale_order_return查询数据*/
    @GetMapping("/query")
    public String query(HttpServletRequest request, Model model,SaleOrderReturn saleOrderReturn,
                        @PageableDefault(size=8, sort={"returnId"}, direction= Sort.Direction.ASC) Pageable pageable){
        String orderId=request.getParameter("orderId");
        String queryStart=request.getParameter("queryStart");
        String queryEnd=request.getParameter("queryEnd");
        String status=request.getParameter("status");
        String path=request.getServletPath();
        String URL=path+"?orderId="+orderId+"&queryStart="+queryStart+"&queryEnd="+queryEnd+"&status="+status+"&";
        model.addAttribute("URL",URL);
        Integer oId=saleOrderReturn.getOrderId();
        Date qs=saleOrderReturn.getStartDate();
        Date qe=saleOrderReturn.getEndDate();
        String st=saleOrderReturn.getStatus();
        if (qs==null||qe==null){
            qs=null;
            qe=null;
        }
        if(st.equals("-请选择-")){
            st=null;
        }
        if(oId==null&&qs==null&&st==null){
            findAllWithPage(request,model,pageable);
        }
        if(oId!=null&&qs!=null&&st!=null){
            Page<SaleOrderReturn> page=salOrdRetService.findByOrderIdAndStartDateBetweenAndStatus(oId,qs,qe,st,pageable);
            model.addAttribute("datas",page);
        }
        if (oId!=null&&qs==null&&st==null){
            Page<SaleOrderReturn> page=salOrdRetService.findByOrderId(oId,pageable);
            model.addAttribute("datas",page);
        }
        if(oId==null&&qs!=null&&st==null){
            Page<SaleOrderReturn> page=salOrdRetService.findByStartDateBetween(qs,qe,pageable);
            model.addAttribute("datas",page);
        }
        if(oId==null&&qs==null&&st!=null){
            Page<SaleOrderReturn> page=salOrdRetService.findByStatus(st,pageable);
            model.addAttribute("datas",page);
        }
        if(oId!=null&&qs!=null&&st==null){
            Page<SaleOrderReturn> page=salOrdRetService.findByOrderIdAndStartDateBetween(oId,qs,qe,pageable);
            model.addAttribute("datas",page);
        }
        if(oId!=null&&qs==null&&st!=null){
            Page<SaleOrderReturn> page=salOrdRetService.findByOrderIdAndStatus(oId,st,pageable);
            model.addAttribute("datas",page);
        }
        if(oId==null&&qs!=null&&st!=null){
            Page<SaleOrderReturn> page=salOrdRetService.findByStartDateBetweenAndStatus(qs,qe,st,pageable);
            model.addAttribute("datas",page);
        }
        return "sale/saleOrderReturn";
    }

    /*给表sale_order_return新增一条数据*/
    @PostMapping("/addSaleOrderReturn")
    @ResponseBody
    public SaleOrderReturn addSaleOrderReturn(SaleOrderReturn saleOrderReturn){
        //saleOrderReturn的数据信息获取与设置
        Integer orderId=saleOrderReturn.getOrderId();
        String orderNumber=saleOrderService.findOrderNumberByOrderId(orderId);
        Date date=saleOrderService.findDateByOrderId(orderId);
        Date startDate=saleOrderReturn.getStartDate();
        if (startDate!=null&&date.compareTo(startDate)>0){
            saleOrderReturn.setMsg("添加失败:开始退货日期不可早于该订单的采购日期");
            return saleOrderReturn;
        }
        saleOrderReturn.setOrderNumber(orderNumber);
        saleOrderReturn.setStatus("退货办理中");
        //进行修改操作
        try {
            salOrdRetService.addSaleOrderReturn(saleOrderReturn);
            saleOrderReturn.setMsg("添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            saleOrderReturn.setMsg("添加失败:采购退货订单号不可重复添加");
        }
        //更新表格sale_order，通过orderId将字段type的值设置为'退货订单'
        saleOrderService.updateTypeByOrderId("退货订单",orderId);
        List<Integer> list=saleOrderService.findProIdsByOrderId(orderId);
        for (Integer proId:list){
            updateProductTwo(proId);
        }
        return saleOrderReturn;
    }

    /*根据returnId查询数据*/
    @GetMapping("/getByReturnId")
    @ResponseBody
    public SaleOrderReturn findByReturnId(Integer returnId){
        SaleOrderReturn saleOrderReturn=salOrdRetService.findByReturnId(returnId);
        saleOrderReturn.setChangeType("false");
        return saleOrderReturn;
    }

    /*修改单个SaleOrderReturn数据*/
    @PostMapping("/modSaleOrderReturn")
    @ResponseBody
    public SaleOrderReturn modSaleOrderReturn(SaleOrderReturn saleOrderReturn){
        Integer returnId=saleOrderReturn.getReturnId();
        Integer orderId=salOrdRetService.findOrderIdByReturnId(returnId);
        String changeType=saleOrderReturn.getChangeType();
        if (changeType.equals("true")){
            try {
                saleOrderService.updateTypeByOrderId("正常订单", orderId);
                salOrdRetService.delDataByReturnId(returnId);
                saleOrderReturn.setMsg("修改成功:退货订单成功转成正常订单，可到'采购订单管理'查看");
                List<Integer> list=saleOrderService.findProIdsByOrderId(orderId);
                for (Integer proId:list){
                    updateProductOne(proId);
                }
                return saleOrderReturn;
            } catch (Exception e) {
                e.printStackTrace();
                saleOrderReturn.setMsg("修改失败");
            }
        }
        saleOrderReturn.setOrderId(orderId);
        saleOrderReturn.setOrderNumber(salOrdRetService.findOrderNumberByReturnId(returnId));
        Date startDate=saleOrderReturn.getStartDate();
        Date endDate=saleOrderReturn.getEndDate();
        String status=saleOrderReturn.getStatus();
        if (endDate==null||status.equals("退货办理中")){
            saleOrderReturn.setEndDate(null);
            saleOrderReturn.setStatus("退货办理中");
            saleOrderReturn.setDays(null);
        }
        if (endDate!=null&&startDate.compareTo(endDate)>0){
            saleOrderReturn.setMsg("修改失败:完成退货日期不可早于开始退货日期");
            return saleOrderReturn;
        }
        if (startDate!=null&&endDate!=null){
            Integer days = (int) ((endDate.getTime() - startDate.getTime()) / (24*3600*1000));
            saleOrderReturn.setDays(days);
        }
        try {
            salOrdRetService.modSaleOrderReturn(saleOrderReturn);
            saleOrderReturn.setMsg("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            saleOrderReturn.setMsg("修改失败");
        }
        return saleOrderReturn;
    }

    /*根据returnId删除数据*/
    @PostMapping("/delByReturnId")
    @ResponseBody
    public String deleteByReturnId(HttpServletRequest request){
        String srtID=request.getParameter("returnId"),msg=null;
        System.out.println(srtID);
        Integer returnId= null;
        try {
            returnId = Integer.parseInt(srtID);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        Integer orderId=salOrdRetService.findOrderIdByReturnId(returnId);
        try {
            salOrdRetService.delDataByReturnId(returnId);
            //先删除order_id的外键绑定表sale_product的相关数据
            saleProService.delBySaleOrderId(orderId);
            //再删除主表sale_order表的数据
            saleOrderService.deleteByOrderId(orderId);
            msg="删除成功";
        } catch (Exception e) {
            e.printStackTrace();
            msg="删除失败";
        }
        List<Integer> list=saleOrderService.findProIdsByOrderId(orderId);
        return msg;
    }

    /*更新数据表product,字段pro_stock*/
    public void updateProductOne(Integer proId){
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

    public void updateProductTwo(Integer proId){
        int saleQuantity=0;
        if(saleProService.findSaleProductsByProId(proId).size()!=0){
            saleQuantity=saleProService.findSaleQuantitySumRet(proId);
        }
        Integer proStock=prodService.findProStockByProId(proId);
        Integer quantity =proStock+saleQuantity;
        Product product=prodService.findByProId(proId);
        product.setProStock(quantity);
        try {
            prodService.modProductSecond(product);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
