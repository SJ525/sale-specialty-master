package com.hsj.controller;

import com.hsj.entity.StoreMan;
import com.hsj.service.StoreManService;
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
 *@ClassName StoreManController
 *@Description 仓库管理员-控制器
 *@Author hsj
 *Date 2020/3/5 13:28
 */
@Controller
@RequestMapping("/storeMan")
public class StoreManController {
    @Autowired
    StoreManService storeManService;

    /*1.查询所有仓库管理员*/
    @GetMapping("/list")
    public String findAll(HttpServletRequest request, Model model,
                          @PageableDefault(size=8, sort={"smId"}, direction= Sort.Direction.ASC) Pageable pageable){
        String URL=request.getServletPath()+"?";
        Page<StoreMan> page=storeManService.findAllPage(pageable);
        model.addAttribute("URL", URL);
        model.addAttribute("datas",page);
        return "staff/admStoreMan";
    }

    /*查询所有仓库管理员数据,以List类型返回*/
    @GetMapping("/getAllStoreMan")
    @ResponseBody
    public List<StoreMan> findAllList(){
        return storeManService.findAllList();
    }

    /*2.通过各种条件查找*/
    @GetMapping("/find")
    public String findStoreMan(HttpServletRequest request,Model model,StoreMan storeMan,
                                @PageableDefault(size=8, sort={"smId"}, direction=Sort.Direction.ASC) Pageable pageable){
        String smName=request.getParameter("smName");
        String smSex=request.getParameter("smSex");
        String sn=storeMan.getSmName();
        String ss=storeMan.getSmSex();
        String path=request.getServletPath();
        String URL=path+"?smName="+smName+"&smSex="+smSex+"&";
        model.addAttribute("URL",URL);
        if (ss.equals("-请选择-")){
            ss=null;
        }
        if (sn==null&&ss==null){
            Page<StoreMan> page=storeManService.packToPage(null,pageable);
            model.addAttribute("datas",page);
        }
        if(sn!=null&&ss!=null){
            Page<StoreMan> page=storeManService.findByBoth(sn, ss, pageable);
            model.addAttribute("datas",page);
        }
        if (sn==null&&ss!=null){
            Page<StoreMan> page=storeManService.findBySmSex(ss,pageable);
            model.addAttribute("datas",page);
        }
        if (sn!=null&&ss==null){
            Page<StoreMan> page=storeManService.findBySmNameLike(sn,pageable);
            model.addAttribute("datas",page);
        }
        return "staff/admStoreMan";
    }

    /*3.实现添加单个仓库管理员*/
    @PostMapping("/addStoreMan")
    @ResponseBody
    public StoreMan addStoreMan(StoreMan storeMan){
        storeMan.setRelation("暂无关联，可以删除");
        try {
            storeManService.addStoreMan(storeMan);
            storeMan.setMsg("添加成功！");
        } catch (Exception e) {
            e.printStackTrace();
            storeMan.setMsg("添加失败！");
        }
        return storeMan;
    }

    /*4.通过id获取要修改的仓库管理员信息*/
    @GetMapping("/getSmMsg")
    @ResponseBody
    public StoreMan getMsg(StoreMan storeMan){
        Integer id=storeMan.getSmId();
        return storeManService.findBySmId(id);
    }

    /*5.修改仓库管理员，并保存*/
    @PostMapping("/modSmMsg")
    @ResponseBody
    public StoreMan modSmMsg(@ModelAttribute StoreMan storeMan){
        Integer smId=storeMan.getSmId();
        String relation=storeManService.findRelationBySmId(smId);
        storeMan.setRelation(relation);
        try {
            storeManService.updateStoreMan(storeMan);
            storeMan.setMsg("修改成功！");
        } catch (Exception e) {
            e.printStackTrace();
            storeMan.setMsg("修改失败！");
        }
        return storeMan;
    }

    /*6.根据仓库管理员id,删除仓库管理员*/
    @PostMapping("/delSmMsg")
    @ResponseBody
    public StoreMan delSmMsg(StoreMan storeMan){
        Integer smId=storeMan.getSmId();
        try {
            storeManService.delStoreMan(smId);
            storeMan.setMsg("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            storeMan.setMsg("删除失败");
        }
        return storeMan;
    }

}
