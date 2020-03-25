package com.hsj.controller;

import com.hsj.entity.AnaNumber;
import com.hsj.service.AnaNumService;
import com.hsj.util.ChangToPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/*
 *@ClassName AnaNumController
 *@Description 控制器-采购量与销售量统计
 *@Author hsj
 *Date 2020/3/17 14:54
 */
@Controller
@RequestMapping("/anaNumber")
public class AnaNumController {
    @Autowired
    AnaNumService anaNumService;

    @GetMapping("/list")
    public String findAll(HttpServletRequest request, Model model, @PageableDefault(size=7) Pageable pageable){
        String URL=request.getServletPath()+"?";
        model.addAttribute("URL", URL);
        //获取表buy_product所有pro_id
        List<Integer> proIds=anaNumService.findAllProIds();
        List<AnaNumber> anaNumList=new ArrayList<>();
        for (Integer proId:proIds){
            AnaNumber anaNum=queryByProId(proId);
            anaNumList.add(anaNum);
        }
        /*将List数据封装成Page数据*/
        Page<AnaNumber> anaNumPage=packListToPage(anaNumList,pageable);
        model.addAttribute("datas", anaNumPage);
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy年MM月dd日");
        //采购的最早日期
        String srtStart=packDateToString(anaNumService.findPurOrderMinDate());
        //销售的最晚日期
        String srtEnd=packDateToString(anaNumService.findSaleOrderMaxDate());
        //设置信息提示
        String msg=srtStart+"到"+srtEnd+"-各种特产采购数量与销售数量统计如下：";
        model.addAttribute("msg",msg);
        return "analyse/anaNumber";
    }

    @GetMapping("/find")
    public String findByIdAndDateBetween(HttpServletRequest request, Model model,AnaNumber anaNumber,
                                         @PageableDefault(size=7) Pageable pageable){
        String proId=request.getParameter("proId");
        String startDate=request.getParameter("startDate");
        String endDate=request.getParameter("endDate");
        String path=request.getServletPath();
        if (proId==null){
            proId="";
        }
        String URL=path+"?proId="+proId+"&startDate="+startDate+"&endDate="+endDate+"&";
        model.addAttribute("URL", URL);
        Integer pd=anaNumber.getProId();
        Date sd=anaNumber.getStartDate();
        Date ed=anaNumber.getEndDate();
        if (sd==null||ed==null){
            sd=null;
            ed=null;
        }
        if(pd==null&&sd==null){
            findAll(request,model,pageable);
        }
        if (pd!=null&&sd==null){
            Date startDate1=anaNumService.findPurOrderMinDate();
            Date endDate1=anaNumService.findSaleOrderMaxDate();
            AnaNumber anaNum=queryByProIdAndDateBetween(pd,startDate1,endDate1);
            List<AnaNumber> list = new ArrayList<>();
            list.add(anaNum);
            Page<AnaNumber> page=packListToPage(list, pageable);
            model.addAttribute("datas",page);
            String srtStart=packDateToString(anaNumService.findPurOrderMinDate());
            String srtEnd=packDateToString(anaNumService.findSaleOrderMaxDate());
            String pn=anaNumService.findProNameByProId(pd);
            String msg=srtStart+"到"+srtEnd+"-"+pn+"采购数量与销售数量统计如下：";
            model.addAttribute("msg",msg);
        }
        if (pd==null&&sd!=null){
            //获取表buy_product所有pro_id
            List<Integer> proIds=anaNumService.findAllProIds();
            List<AnaNumber> anaNumList=new ArrayList<>();
            for (Integer id:proIds){
                AnaNumber anaNum=queryByProIdAndDateBetween(id,sd,ed);
                anaNumList.add(anaNum);
            }
            /*将List数据封装成Page数据*/
            Page<AnaNumber> anaNumPage=packListToPage(anaNumList,pageable);
            model.addAttribute("datas", anaNumPage);
            String srtStart=packDateToString(sd);
            String srtEnd=packDateToString(ed);
            String msg=srtStart+"到"+srtEnd+"-各种特产采购数量与销售数量统计如下：";
            model.addAttribute("msg",msg);
        }
        if(pd!=null&&sd!=null){
            AnaNumber anaNumber1 = queryByProIdAndDateBetween(pd,sd,ed);
            List<AnaNumber> list = new ArrayList<>();
            list.add(anaNumber1);
            /*将List数据封装成Page数据*/
            Page<AnaNumber> page=packListToPage(list,pageable);
            model.addAttribute("datas", page);
            String srtStart=packDateToString(sd);
            String srtEnd=packDateToString(ed);
            String pn=anaNumService.findProNameByProId(pd);
            String msg=srtStart+"到"+srtEnd+"-"+pn+"采购数量与销售数量统计如下：";
            model.addAttribute("msg",msg);
        }
        return "analyse/anaNumber";
    }

    /*查询表buy_product、表product、sale_product等表，根据proId查询到各种值，封装成AnaNumber javaBean*/
    public AnaNumber queryByProId(Integer proId){
        AnaNumber anaNumber=new AnaNumber();
        //设置特产id
        anaNumber.setProId(proId);
        //根据特产id,查询特产名称
        String proName=anaNumService.findProNameByProId(proId);
        anaNumber.setProName(proName);
        //根据特产id,查询特产计价单位
        String proUnit=anaNumService.findProUnitByProId(proId).substring(1);
        anaNumber.setProUnit(proUnit);
        //根据特产id，查询采购数量
        Integer purQuantity=anaNumService.findBuyQuantitySumAll(proId);
        anaNumber.setPurQuantity(purQuantity);
        //根据特产id，查询销售数量
        Integer saleQuantity=anaNumService.findSaleQuantitySumAll(proId);
        if (saleQuantity==null){
            anaNumber.setSaleQuantity(0);
        }else {
            anaNumber.setSaleQuantity(saleQuantity);
        }
        //根据特产id，查询采购支出
        BigDecimal pay=anaNumService.findPriceSumByBuyProId(proId);
        anaNumber.setPay(pay);
        //根据特产id，查询销售收入
        BigDecimal income=anaNumService.findPriceSumBySaleProId(proId);
        if (income==null){
            anaNumber.setIncome(new BigDecimal(0));
        }
        else{
            anaNumber.setIncome(income);
        }
        //利润
        if (income==null){
            anaNumber.setProfit((new BigDecimal(0)).subtract(pay));
        }
        else{
            anaNumber.setProfit(income.subtract(pay));
        }
        return anaNumber;
    }

    /*查询表buy_product、表product、sale_product等表，根据proId和日期区间查询到各种值，封装成AnaNumber javaBean*/
    public AnaNumber queryByProIdAndDateBetween(Integer proId,Date start,Date end){
        AnaNumber anaNumber=new AnaNumber();
        //设置特产id
        anaNumber.setProId(proId);
        //根据特产id,查询特产名称
        String proName=anaNumService.findProNameByProId(proId);
        anaNumber.setProName(proName);
        //根据特产id,查询特产计价单位
        String proUnit=anaNumService.findProUnitByProId(proId).substring(1);
        anaNumber.setProUnit(proUnit);
        //根据特产id和查询日期区间，查询采购数量
        Integer purQuantity=anaNumService.buyQuaSumByIdAndDateBetween(proId,start,end);
        if (purQuantity==null){
            anaNumber.setPurQuantity(0);
        }else{
            anaNumber.setPurQuantity(purQuantity);
        }
        //根据特产id和查询日期区间，查询销售数量
        Integer saleQuantity=anaNumService.saleQuaSumByIdAndDateBetween(proId,start,end);
        if (saleQuantity==null){
            anaNumber.setSaleQuantity(0);
        }else {
            anaNumber.setSaleQuantity(saleQuantity);
        }
        //根据特产id和查询日期区间，查询采购支出
        BigDecimal pay=anaNumService.buyPriceSumByIdAndDateBetween(proId,start,end);
        if (pay==null){
            anaNumber.setPay(new BigDecimal(0));
        }else{
            anaNumber.setPay(pay);
        }
        //根据特产id和查询日期区间，查询销售收入
        BigDecimal income=anaNumService.salePriceSumByIdAndDateBetween(proId,start,end);
        if (income==null){
            anaNumber.setIncome(new BigDecimal(0));
        }
        else{
            anaNumber.setIncome(income);
        }
        //利润
        if (pay==null&income==null){
            anaNumber.setProfit((new BigDecimal(0)));
        }
        else if(pay!=null&income==null){
            anaNumber.setProfit((new BigDecimal(0)).subtract(pay));
        }
        else if(pay==null&income!=null){
            anaNumber.setProfit(income.subtract(new BigDecimal(0)));
        }
        else{
            anaNumber.setProfit(income.subtract(pay));
        }
        return anaNumber;
    }

    /*将List数据封装成Page数据*/
    public Page<AnaNumber> packListToPage(List<AnaNumber> list, Pageable pageable){
        //调用util包里面的ChangToPage类
        ChangToPage<AnaNumber> changToPage=new ChangToPage<>();
        return changToPage.getPage(list,pageable);
    }

    /*将日期由Date类型封装成String类型，并且改变格式*/
    public String packDateToString(Date date){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy年MM月dd日");
        String srtDate=sdf.format(date);
        return srtDate;
    }
}
