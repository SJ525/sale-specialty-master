package com.hsj.controller;

import com.hsj.entity.AnaReturn;
import com.hsj.service.AnaRetService;
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
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/*
 *@ClassName AnaRetController
 *@Description 控制器类-采购与销售退货统计
 *@Author hsj
 *Date 2020/3/17 14:56
 */
@Controller
@RequestMapping("/anaReturn")
public class AnaRetController {
    @Autowired
    AnaRetService anaRetService;

    @GetMapping("/list")
    public String findAll(HttpServletRequest request, Model model, @PageableDefault(size=7) Pageable pageable){
        String URL=request.getServletPath()+"?";
        model.addAttribute("URL", URL);
        //获取表buy_product所有pro_id
        List<Integer> proIds=anaRetService.findAllProIds();
        List<AnaReturn> anaRetList=new ArrayList<>();
        for (Integer proId:proIds){
            AnaReturn anaRet=queryByProId(proId);
            anaRetList.add(anaRet);
        }
        /*将List数据封装成Page数据*/
        Page<AnaReturn> anaRetPage=packListToPage(anaRetList,pageable);
        model.addAttribute("datas", anaRetPage);
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy年MM月dd日");
        //采购的最早日期
        String srtStart=packDateToString(anaRetService.findPurOrderMinDate());
        //销售的最晚日期
        String srtEnd=packDateToString(anaRetService.findSaleOrderMaxDate());
        //设置信息提示
        String msg=srtStart+"到"+srtEnd+"-各种特产的采购与销售退货统计如下：";
        model.addAttribute("msg",msg);
        return "analyse/anaReturn";
    }

    @GetMapping("/find")
    public String findByIdAndDateBetween(HttpServletRequest request, Model model,AnaReturn anaReturn,
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
        Integer pd=anaReturn.getProId();
        Date sd=anaReturn.getStartDate();
        Date ed=anaReturn.getEndDate();
        if (sd==null||ed==null){
            sd=null;
            ed=null;
        }
        if(pd==null&&sd==null){
            findAll(request,model,pageable);
        }
        if (pd!=null&&sd==null){
            Date startDate1=anaRetService.findPurOrderMinDate();
            Date endDate1=anaRetService.findSaleOrderMaxDate();
            AnaReturn anaRet=queryByProIdAndDateBetween(pd,startDate1,endDate1);
            List<AnaReturn> list = new ArrayList<>();
            list.add(anaRet);
            Page<AnaReturn> page=packListToPage(list, pageable);
            model.addAttribute("datas",page);
            String srtStart=packDateToString(anaRetService.findPurOrderMinDate());
            String srtEnd=packDateToString(anaRetService.findSaleOrderMaxDate());
            String pn=anaRetService.findProNameByProId(pd);
            String msg=srtStart+"到"+srtEnd+"-"+pn+"特产的采购与销售退货统计如下：";
            model.addAttribute("msg",msg);
        }
        if (pd==null&&sd!=null){
            //获取表buy_product所有pro_id
            List<Integer> proIds=anaRetService.findAllProIds();
            List<AnaReturn> anaRetList=new ArrayList<>();
            for (Integer id:proIds){
                AnaReturn anaRet=queryByProIdAndDateBetween(id,sd,ed);
                anaRetList.add(anaRet);
            }
            /*将List数据封装成Page数据*/
            Page<AnaReturn> anaRetPage=packListToPage(anaRetList,pageable);
            model.addAttribute("datas", anaRetPage);
            String srtStart=packDateToString(sd);
            String srtEnd=packDateToString(ed);
            String msg=srtStart+"到"+srtEnd+"-各种特产的采购与销售退货统计如下：";
            model.addAttribute("msg",msg);
        }
        if(pd!=null&&sd!=null){
            AnaReturn anaReturn1 = queryByProIdAndDateBetween(pd,sd,ed);
            List<AnaReturn> list = new ArrayList<>();
            list.add(anaReturn1);
            /*将List数据封装成Page数据*/
            Page<AnaReturn> page=packListToPage(list,pageable);
            model.addAttribute("datas", page);
            String srtStart=packDateToString(sd);
            String srtEnd=packDateToString(ed);
            String pn=anaRetService.findProNameByProId(pd);
            String msg=srtStart+"到"+srtEnd+"-"+pn+"特产的采购与销售退货统计如下：";
            model.addAttribute("msg",msg);
        }
        return "analyse/anaReturn";
    }

    /*查询表buy_product、表product、sale_product等表，根据proId查询到各种值，封装成AnaReturn javaBean*/
    public AnaReturn queryByProId(Integer proId){
        AnaReturn anaReturn=new AnaReturn();
        //设置特产id
        anaReturn.setProId(proId);
        //根据特产id,查询特产名称
        String proName=anaRetService.findProNameByProId(proId);
        anaReturn.setProName(proName);
        //根据特产id,查询特产计价单位
        String proUnit=anaRetService.findProUnitByProId(proId).substring(1);
        anaReturn.setProUnit(proUnit);
        //根据特产id,查询采购总数量
        Integer purQuaAll=anaRetService.findBuyQuantitySumAll(proId);
        if(purQuaAll==null){
            anaReturn.setPurQuaAll(0);
        }
        else{
            anaReturn.setPurQuaAll(purQuaAll);
        }
        //根据特产id,查询采购退货数量
        Integer purQuaRet=anaRetService.findBuyQuantitySumRet(proId);
        if (purQuaRet==null){
            anaReturn.setPurRetNum(0);
            purQuaRet=0;
        }
        else{
            anaReturn.setPurRetNum(purQuaRet);
        }
        DecimalFormat df = new DecimalFormat("0.00");//格式化小数
        String purRetRate = df.format((float)purQuaRet/purQuaAll*100);//返回的是String类型
        anaReturn.setPurRetRate(purRetRate+"%");
        //根据特产id,查询销售总数量
        Integer saleQuaAll=anaRetService.findSaleQuantitySumAll(proId);
        if (saleQuaAll==null){
            anaReturn.setSaleQuaAll(0);
        }
        else{
            anaReturn.setSaleQuaAll(saleQuaAll);
        }
        //根据特产id,查询销售退货数量
        Integer saleQuaRet=anaRetService.findSaleQuantitySumRet(proId);
        if (saleQuaAll==null&&saleQuaRet==null){
            anaReturn.setSaleRetNum(0);
            anaReturn.setSaleRetRate("0.00%");
        }
        else if(saleQuaAll!=null&&saleQuaRet==null){
            anaReturn.setSaleRetNum(0);
            anaReturn.setSaleRetRate("0.00%");
        }
        else if(saleQuaAll==null&&saleQuaRet!=null){
            anaReturn.setSaleRetNum(0);
            anaReturn.setSaleRetRate("0.00%");
        }
        else{
            anaReturn.setSaleRetNum(saleQuaRet);
            String saleRetRate=df.format((float)saleQuaRet/saleQuaAll*100);//返回的是String类型
            anaReturn.setSaleRetRate(saleRetRate+"%");
        }
        return anaReturn;
    }

    /*查询表buy_product、表product、sale_product等表，
    根据proId和时间区间查询到各种值，封装成AnaReturn javaBean*/
    public AnaReturn queryByProIdAndDateBetween(Integer proId,Date start,Date end){
        AnaReturn anaReturn=new AnaReturn();
        //设置特产id
        anaReturn.setProId(proId);
        //根据特产id,查询特产名称
        String proName=anaRetService.findProNameByProId(proId);
        anaReturn.setProName(proName);
        //根据特产id,查询特产计价单位
        String proUnit=anaRetService.findProUnitByProId(proId).substring(1);
        anaReturn.setProUnit(proUnit);
        //根据特产id和时间区间,查询采购数量
        Integer purQuaAll=anaRetService.buyQuaSumByIdAndDateBetween(proId,start,end);
        //根据特产id和时间区间,查询采购退货数量
        Integer purQuaRet=anaRetService.purQuaSumRetByProIdAndDateBetween(proId,start,end);
        if (purQuaRet==null){
            anaReturn.setPurRetNum(0);
        }
        else{
            anaReturn.setPurRetNum(purQuaRet);
        }
        if (purQuaAll==null){
            anaReturn.setPurQuaAll(0);
            anaReturn.setPurRetRate("0.00%");
        }
        else{
            anaReturn.setPurQuaAll(purQuaAll);
            if (purQuaRet==null){
                purQuaRet=0;
            }
            DecimalFormat df = new DecimalFormat("0.00");//格式化小数
            String purRetRate = df.format((float)purQuaRet/purQuaAll*100);//返回的是String类型
            anaReturn.setPurRetRate(purRetRate+"%");
        }
        //根据特产id和时间区间,查询销售数量
        Integer saleQuaAll=anaRetService.saleQuaSumByIdAndDateBetween(proId,start,end);
        if (saleQuaAll==null){
            anaReturn.setSaleQuaAll(0);
        }
        else{
            anaReturn.setSaleQuaAll(saleQuaAll);
        }
        //根据特产id和时间区间,查询销售退货数量
        Integer saleQuaRet=anaRetService.saleQuaSumRetByProIdAndDateBetween(proId,start,end);
        if (saleQuaRet==null){
            anaReturn.setSaleRetNum(0);
        }
        else{
            anaReturn.setSaleRetNum(saleQuaRet);
        }
        if (saleQuaAll==null){
            anaReturn.setSaleRetRate("0.00%");
        }
        else{
            if (saleQuaRet==null){
                saleQuaRet=0;
            }
            DecimalFormat df = new DecimalFormat("0.00");//格式化小数
            String saleRetRate = df.format((float)saleQuaRet/saleQuaAll*100);//返回的是String类型
            anaReturn.setSaleRetRate(saleRetRate+"%");
        }
        return anaReturn;
    }

    /*将List数据封装成Page数据*/
    public Page<AnaReturn> packListToPage(List<AnaReturn> list, Pageable pageable){
        //调用util包里面的ChangToPage类
        ChangToPage<AnaReturn> changToPage=new ChangToPage<>();
        return changToPage.getPage(list,pageable);
    }

    /*将日期由Date类型封装成String类型，并且改变格式*/
    public String packDateToString(Date date){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy年MM月dd日");
        String srtDate=sdf.format(date);
        return srtDate;
    }
}
