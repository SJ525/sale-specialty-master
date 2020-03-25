package com.hsj.controller;

import com.hsj.entity.BuyProduct;
import com.hsj.entity.Product;
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
import java.util.Date;
import java.util.List;

/*
 *@ClassName PurOrdRetController
 *@Description 采购退货订单控制器类
 *@Author hsj
 *Date 2020/3/11 11:09
 */
@Controller
@RequestMapping("/purOrderReturn")
public class PurOrdRetController {
    @Autowired
    ProdService prodService;
    @Autowired
    BuyProService buyProService;
    @Autowired
    PurOrderService purOrderService;
    @Autowired
    PurOrdRetService purOrdRetService;
    @Autowired
    SaleProService saleProService;

    /*查询表pur_order_return所有数据，以Page类型返回,跳转到视图purchase/purOrderReturn*/
    @GetMapping("/list")
    public String findAllWithPage(HttpServletRequest request, Model model,
                                  @PageableDefault(size=8, sort={"returnId"}, direction= Sort.Direction.ASC) Pageable pageable){
        String URL=request.getServletPath()+"?";
        model.addAttribute("URL", URL);
        Page<PurOrderReturn> page=purOrdRetService.findAllWithPage(pageable);
        model.addAttribute("datas", page);
        return "purchase/purOrderReturn";
    }

    /*查询表pur_order_return所有数据，以List类型返回*/
    @GetMapping("/getAllPurOrderReturn")
    @ResponseBody
    public List<PurOrderReturn> findAllWithLst(){
        return purOrdRetService.findAllWithList();
    }

    /*根据各种条件对表格表pur_order_return查询数据*/
    @GetMapping("/query")
    public String query(HttpServletRequest request, Model model,PurOrderReturn purOrderReturn,
                        @PageableDefault(size=8, sort={"returnId"}, direction= Sort.Direction.ASC) Pageable pageable){
        String orderId=request.getParameter("orderId");
        String queryStart=request.getParameter("queryStart");
        String queryEnd=request.getParameter("queryEnd");
        String status=request.getParameter("status");
        String path=request.getServletPath();
        String URL=path+"?orderId="+orderId+"&queryStart="+queryStart+"&queryEnd="+queryEnd+"&status="+status+"&";
        model.addAttribute("URL",URL);
        Integer oId=purOrderReturn.getOrderId();
        Date qs=purOrderReturn.getStartDate();
        Date qe=purOrderReturn.getEndDate();
        String st=purOrderReturn.getStatus();
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
            Page<PurOrderReturn> page=purOrdRetService.findByOrderIdAndStartDateBetweenAndStatus(oId,qs,qe,st,pageable);
            model.addAttribute("datas",page);
        }
        if (oId!=null&&qs==null&&st==null){
            Page<PurOrderReturn> page=purOrdRetService.findByOrderId(oId,pageable);
            model.addAttribute("datas",page);
        }
        if(oId==null&&qs!=null&&st==null){
            Page<PurOrderReturn> page=purOrdRetService.findByStartDateBetween(qs,qe,pageable);
            model.addAttribute("datas",page);
        }
        if(oId==null&&qs==null&&st!=null){
            Page<PurOrderReturn> page=purOrdRetService.findByStatus(st,pageable);
            model.addAttribute("datas",page);
        }
        if(oId!=null&&qs!=null&&st==null){
            Page<PurOrderReturn> page=purOrdRetService.findByOrderIdAndStartDateBetween(oId,qs,qe,pageable);
            model.addAttribute("datas",page);
        }
        if(oId!=null&&qs==null&&st!=null){
            Page<PurOrderReturn> page=purOrdRetService.findByOrderIdAndStatus(oId,st,pageable);
            model.addAttribute("datas",page);
        }
        if(oId==null&&qs!=null&&st!=null){
            Page<PurOrderReturn> page=purOrdRetService.findByStartDateBetweenAndStatus(qs,qe,st,pageable);
            model.addAttribute("datas",page);
        }
        return "purchase/purOrderReturn";
    }

    /*给表pur_order_return新增一条数据*/
    @PostMapping("/addPurOrderReturn")
    @ResponseBody
    public PurOrderReturn addPurOrderReturn(PurOrderReturn purOrderReturn){
        //purOrderReturn的数据信息获取与设置
        Integer orderId=purOrderReturn.getOrderId();
        String orderNumber=purOrderService.findOrderNumberByOrderId(orderId);
        Date date=purOrderService.findDateByOrderId(orderId);
        Date startDate=purOrderReturn.getStartDate();
        if (startDate!=null&&date.compareTo(startDate)>0){
            purOrderReturn.setMsg("添加失败:开始退货日期不可早于该订单的采购日期");
            return purOrderReturn;
        }
        purOrderReturn.setOrderNumber(orderNumber);
        purOrderReturn.setStatus("退货办理中");
        //进行修改操作
        try {
            purOrdRetService.addPurOrderReturn(purOrderReturn);
            purOrderReturn.setMsg("添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            purOrderReturn.setMsg("添加失败:采购退货订单号不可重复添加");
        }
        //更新表格pur_order，通过orderId将字段type的值设置为'退货订单'
        purOrderService.updateTypeByOrderId("退货订单",orderId);
        /*更新数据表product,字段pro_stock*/
        List<Integer> list=purOrderService.findProIdsByOrderId(orderId);
        for (Integer proId:list){
            updateProductTwo(proId);
        }
        return purOrderReturn;
    }

    /*根据returnId查询数据*/
    @GetMapping("/getByReturnId")
    @ResponseBody
    public PurOrderReturn findByReturnId(HttpServletRequest request){
        String srtID=request.getParameter("returnId");
        System.out.println(srtID);
        Integer returnId= null;
        try {
            returnId = Integer.parseInt(srtID);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        PurOrderReturn purOrderReturn=purOrdRetService.findByReturnId(returnId);
        purOrderReturn.setChangeType("false");
        return purOrderReturn;
    }

    /*修改单个PurOrderReturn数据*/
    @PostMapping("/modPurOrderReturn")
    @ResponseBody
    public PurOrderReturn modPurOrderReturn(PurOrderReturn purOrderReturn){
        Integer returnId=purOrderReturn.getReturnId();
        Integer orderId=purOrdRetService.findOrderIdByReturnId(returnId);
        String changeType=purOrderReturn.getChangeType();
        if (changeType.equals("true")){
            try {
                purOrderService.updateTypeByOrderId("正常订单", orderId);
                purOrdRetService.delDataByReturnId(returnId);
                purOrderReturn.setMsg("修改成功:退货订单成功转成正常订单，可到'采购订单管理'查看");
                /*更新数据表product,字段pro_stock*/
                List<Integer> list=purOrderService.findProIdsByOrderId(orderId);
                for (Integer proId:list){
                    updateProductOne(proId);
                }
                return purOrderReturn;
            } catch (Exception e) {
                e.printStackTrace();
                purOrderReturn.setMsg("修改失败");
            }
        }
        purOrderReturn.setOrderId(orderId);
        purOrderReturn.setOrderNumber(purOrdRetService.findOrderNumberByReturnId(returnId));
        Date startDate=purOrderReturn.getStartDate();
        Date endDate=purOrderReturn.getEndDate();
        String status=purOrderReturn.getStatus();
        if (endDate==null||status.equals("退货办理中")){
            purOrderReturn.setEndDate(null);
            purOrderReturn.setStatus("退货办理中");
            purOrderReturn.setDays(null);
        }
        if (endDate!=null&&startDate.compareTo(endDate)>0){
            purOrderReturn.setMsg("修改失败:完成退货日期不可早于开始退货日期");
            return purOrderReturn;
        }
        if (startDate!=null&&endDate!=null){
            Integer days = (int) ((endDate.getTime() - startDate.getTime()) / (24*3600*1000));
            purOrderReturn.setDays(days);
        }
        try {
            purOrdRetService.modPurOrderReturn(purOrderReturn);
            purOrderReturn.setMsg("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            purOrderReturn.setMsg("修改失败");
        }
        return purOrderReturn;
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
        Integer orderId=purOrdRetService.findOrderIdByReturnId(returnId);
        try {
            purOrdRetService.delDataByReturnId(returnId);
            //先删除order_id的外键绑定表buy_product的相关数据
            List<BuyProduct> list=buyProService.findByBuyOrderId(orderId);
            if (list.size()!=0){
                buyProService.deleteByBuyOrderId(orderId);
            }
            //再删除主表pur_order表的数据
            purOrderService.deleteByOrderId(orderId);
            msg="删除成功";
        } catch (Exception e) {
            e.printStackTrace();
            msg="删除失败";
        }
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
        int buyQuantity=0;
        if(buyProService.findBuyProductsByProId(proId).size()!=0){
            buyQuantity=buyProService.findBuyQuantitySumRet(proId);
        }
        Integer proStock=prodService.findProStockByProId(proId);
        Integer quantity =proStock-buyQuantity;
        Product product=prodService.findByProId(proId);
        product.setProStock(quantity);
        try {
            prodService.modProductSecond(product);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
