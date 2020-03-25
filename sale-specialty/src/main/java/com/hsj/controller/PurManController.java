package com.hsj.controller;

import com.hsj.entity.Purchaser;
import com.hsj.service.PurManService;
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
 *@ClassName PurManController
 *@Description 采购员-控制器类
 *@Author hsj
 *Date 2020/3/5 12:36
 */
@Controller
@RequestMapping("/purchaser")
public class PurManController {
    @Autowired
    PurManService purManService;

    /*1.查询所有采购员,数据类型是Page*/
    @GetMapping("/list")
    public String findAllWithPage(HttpServletRequest request, Model model,
                          @PageableDefault(size=8, sort={"purId"}, direction= Sort.Direction.ASC) Pageable pageable){
        String URL=request.getServletPath()+"?";
        Page<Purchaser> page=purManService.findAllWithPage(pageable);
        model.addAttribute("URL", URL);
        model.addAttribute("datas",page);
        return "staff/admPurchaser";
    }

    /*2.查询所有采购员,数据类型是List*/
    @GetMapping("/getAllPurchaser")
    @ResponseBody
    public List<Purchaser> getAllPurchaser(){
        return purManService.findAllWithList();
    }

    /*3.通过各种条件查找*/
    @GetMapping("/find")
    public String findPurchaser(HttpServletRequest request,Model model,Purchaser purchaser,
                               @PageableDefault(size=8, sort={"purId"}, direction=Sort.Direction.ASC) Pageable pageable){
        String purName=request.getParameter("purName");
        String purSex=request.getParameter("purSex");
        String pn=purchaser.getPurName();
        String ps=purchaser.getPurSex();
        String path=request.getServletPath();
        String URL=path+"?purName="+purName+"&purSex="+purSex+"&";
        model.addAttribute("URL",URL);
        if (ps.equals("-请选择-")){
            ps=null;
        }
        if (pn==null&&ps==null){
            Page<Purchaser> page=purManService.packToPage(null,pageable);
            model.addAttribute("datas",page);
        }
        if(pn!=null&&ps!=null){
            Page<Purchaser> page=purManService.findByBoth(pn, ps, pageable);
            model.addAttribute("datas",page);
        }
        if (pn==null&&ps!=null){
            Page<Purchaser> page=purManService.findByPurSex(ps,pageable);
            model.addAttribute("datas",page);
        }
        if (pn!=null&&ps==null){
            Page<Purchaser> page=purManService.findByPurNameLike(pn,pageable);
            model.addAttribute("datas",page);
        }
        return "staff/admPurchaser";
    }


    /*4.实现添加单个采购员*/
    @PostMapping("/addPurchaser")
    @ResponseBody
    public Purchaser addPurchaser(Purchaser purchaser){
        purchaser.setRelation("暂无关联，可以删除");
        try {
            purManService.addPurchaser(purchaser);
            purchaser.setMsg("添加成功！");
        } catch (Exception e) {
            e.printStackTrace();
            purchaser.setMsg("添加失败！");
        }
        return purchaser;
    }

    /*5.通过id获取要修改的采购员信息*/
    @GetMapping("/getPurMsg")
    @ResponseBody
    public Purchaser getMsg(Purchaser purchaser){
        Integer id=purchaser.getPurId();
        return purManService.findByPurId(id);
    }

    /*6.修改采购员，并保存*/
    @PostMapping("/modPurMsg")
    @ResponseBody
    public Purchaser modPurMsg(@ModelAttribute Purchaser purchaser){
        Integer purId=purchaser.getPurId();
        String relation=purManService.findRelationByPurId(purId);
        purchaser.setRelation(relation);
        try {
            purManService.updatePurchaser(purchaser);
            purchaser.setMsg("修改成功！");
        } catch (Exception e) {
            e.printStackTrace();
            purchaser.setMsg("修改失败！");
        }
        return purchaser;
    }

    /*7.根据采购员id,删除采购员*/
    @PostMapping("/delPurMsg")
    @ResponseBody
    public Purchaser delPurMsg(Purchaser purchaser){
        Integer purId=purchaser.getPurId();
        try {
            purManService.delPurchaser(purId);
            purchaser.setMsg("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            purchaser.setMsg("删除失败");
        }
        return purchaser;
    }
    
}
