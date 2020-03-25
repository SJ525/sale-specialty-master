package com.hsj.controller;

import com.hsj.entity.AnaProfit;
import com.hsj.service.AnaProService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

/*
 *@ClassName AnaProfitController
 *@Description 控制器类-支出与收入统计
 *@Author hsj
 *Date 2020/3/17 14:55
 */
@Controller
@RequestMapping("/anaProfit")
public class AnaProfitController {
    @Autowired
    AnaProService anaProService;


    @GetMapping("/list")
    public String findAll(Model model){
        //实例AnaProfit
        AnaProfit anaProfit=new AnaProfit();
        //获取与设置采购总支出
        BigDecimal pay=anaProService.buyPriceSumNor();
        if (pay==null){
            anaProfit.setTotalPay(new BigDecimal(0));
        }
        else{
            anaProfit.setTotalPay(pay);
        }
        //获取与设置销售总收入
        BigDecimal income=anaProService.salePriceSumNor();
        if (income==null){
            anaProfit.setTotalInCome(new BigDecimal(0));
        }
        else{
            anaProfit.setTotalInCome(income);
        }
        if(pay==null&&income==null){
            anaProfit.setProfit(new BigDecimal(0));
        }
        if (pay!=null&&income!=null){
            anaProfit.setProfit(income.subtract(pay));
        }
        if(pay==null&&income!=null){
            anaProfit.setProfit(income.subtract(new BigDecimal(0)));
        }
        if(pay!=null&&income==null){
            anaProfit.setProfit((new BigDecimal(0)).subtract(pay));
        }
        model.addAttribute("data",anaProfit);
        //采购的最早日期
        String srtStart=packDateToString(anaProService.findPurOrderMinDate());
        //销售的最晚日期
        String srtEnd=packDateToString(anaProService.findSaleOrderMaxDate());
        //设置信息提示
        String msg=srtStart+"到"+srtEnd+"-正常订单的采购支出和销售收入统计如下：";
        model.addAttribute("msg",msg);
        return "analyse/anaProfit";
    }

    @GetMapping("/find")
    public String findByDateBetween(Model model,AnaProfit anaProfit){
        //获取前台传来的时间区间查询起始与结尾
        Date startDate=anaProfit.getStartDate();
        Date endDate=anaProfit.getEndDate();
        if(startDate==null||endDate==null){
            startDate=anaProService.findPurOrderMinDate();
            endDate=anaProService.findSaleOrderMaxDate();
        }
        //获取与设置采购总支出
        BigDecimal pay=anaProService.buyPriceSumNorByDateBetween(startDate,endDate);
        if (pay==null){
            anaProfit.setTotalPay(new BigDecimal(0));
        }
        else{
            anaProfit.setTotalPay(pay);
        }
        //获取与设置销售总收入
        BigDecimal income=anaProService.salePriceSumNorByDateBetween(startDate,endDate);
        if (income==null){
            anaProfit.setTotalInCome(new BigDecimal(0));
        }
        else{
            anaProfit.setTotalInCome(income);
        }
        if(pay==null&&income==null){
            anaProfit.setProfit(new BigDecimal(0));
        }
        if (pay!=null&&income!=null){
            anaProfit.setProfit(income.subtract(pay));
        }
        if(pay==null&&income!=null){
            anaProfit.setProfit(income.subtract(new BigDecimal(0)));
        }
        if(pay!=null&&income==null){
            anaProfit.setProfit((new BigDecimal(0)).subtract(pay));
        }
        model.addAttribute("data",anaProfit);
        //采购的最早日期
        String srtStart=packDateToString(startDate);
        //销售的最晚日期
        String srtEnd=packDateToString(endDate);
        //设置信息提示
        String msg=srtStart+"到"+srtEnd+"-正常订单的采购支出和销售收入统计如下：";
        model.addAttribute("msg",msg);
        return "analyse/anaProfit";
    }

    /*将日期由Date类型封装成String类型，并且改变格式*/
    public String packDateToString(Date date){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy年MM月dd日");
        String srtDate=sdf.format(date);
        return srtDate;
    }
}
