package com.hsj.controller;

import com.hsj.entity.Salesperson;
import com.hsj.service.SalPerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/*
 *@ClassName SalPerController
 *@Description 销售员-控制器
 *@Author hsj
 *Date 2020/3/5 13:30
 */
@Controller
@RequestMapping("/salesperson")
public class SalPerController {
    @Autowired
    SalPerService salPerService;

    /*1.查询所有销售员,返回数据类型是Page*/
    @GetMapping("/list")
    public String findAll(HttpServletRequest request, Model model,
                          @PageableDefault(size=8, sort={"spId"}, direction= Sort.Direction.ASC) Pageable pageable){
        String URL=request.getServletPath()+"?";
        Page<Salesperson> page=salPerService.findAll(pageable);
        model.addAttribute("URL", URL);
        model.addAttribute("datas",page);
        return "staff/admSalesperson";
    }

    /*查询所有销售员,返回数据类型是List*/
    @GetMapping("/getAllSalesperson")
    @ResponseBody
    public List<Salesperson> findAllWithList(){
        return salPerService.findAllWithList();
    }

    /*2.通过各种条件查找*/
    @GetMapping("/find")
    public String findSalesperson(HttpServletRequest request,Model model,Salesperson salesperson,
                               @PageableDefault(size=8, sort={"spId"}, direction=Sort.Direction.ASC) Pageable pageable){
        String spName=request.getParameter("spName");
        String spSex=request.getParameter("spSex");
        String sn=salesperson.getSpName();
        String ss=salesperson.getSpSex();
        String path=request.getServletPath();
        String URL=path+"?spName="+spName+"&spSex="+spSex+"&";
        model.addAttribute("URL",URL);
        if (ss.equals("-请选择-")){
            ss=null;
        }
        if (sn==null&&ss==null){
            Page<Salesperson> page=salPerService.packToPage(null,pageable);
            model.addAttribute("datas",page);
        }
        if(sn!=null&&ss!=null){
            Page<Salesperson> page=salPerService.findByBoth(sn, ss, pageable);
            model.addAttribute("datas",page);
        }
        if (sn==null&&ss!=null){
            Page<Salesperson> page=salPerService.findBySpSex(ss,pageable);
            model.addAttribute("datas",page);
        }
        if (sn!=null&&ss==null){
            Page<Salesperson> page=salPerService.findBySpNameLike(sn,pageable);
            model.addAttribute("datas",page);
        }
        return "staff/admSalesperson";
    }

    /*3.实现添加单个销售员*/
    @PostMapping("/addSalesperson")
    @ResponseBody
    public Salesperson addSalesperson(Salesperson salesperson){
        salesperson.setRelation("暂无关联，可以删除");
        try {
            salPerService.addSalesperson(salesperson);
            salesperson.setMsg("添加成功！");
        } catch (Exception e) {
            e.printStackTrace();
            salesperson.setMsg("添加失败！");
        }
        return salesperson;
    }
    
    /*4.通过id获取要修改的销售员信息*/
    @GetMapping("/getSpMsg")
    @ResponseBody
    public Salesperson getMsg(Integer spId){
        return salPerService.findBySpId(spId);
    }

    /*5.修改销售员，并保存*/
    @PostMapping("/modSpMsg")
    @ResponseBody
    public Salesperson modSpMsg(@ModelAttribute Salesperson salesperson){
        Integer spId=salesperson.getSpId();
        String relation=salPerService.findRelationBySpId(spId);
        salesperson.setRelation(relation);
        try {
            salPerService.updateSalesperson(salesperson);
            salesperson.setMsg("修改成功！");
        } catch (Exception e) {
            e.printStackTrace();
            salesperson.setMsg("修改失败！");
        }
        return salesperson;
    }

    /*6.根据销售员id,删除销售员*/
    @PostMapping("/delSpMsg")
    @ResponseBody
    public Salesperson delSpMsg(Integer spId){
        Salesperson salesperson=new Salesperson();
        try {
            salPerService.delSalesperson(spId);
            salesperson.setMsg("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            salesperson.setMsg("删除失败");
        }
        return salesperson;
    }
}
