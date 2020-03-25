package com.hsj.controller;

import com.hsj.entity.Supplier;
import com.hsj.service.SupService;
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
 *@ClassName SupController
 *@Description 供应商控制器
 *@Author hsj
 *Date 2020/3/4 19:33
 */
@Controller
@RequestMapping("/supplier")
public class SupController {
    @Autowired
    SupService supService;

    /*查询所有供应商,数据类型是Page*/
    @GetMapping("/list")
    public String findAll(HttpServletRequest request, Model model,
                          @PageableDefault(size=8, sort={"supId"}, direction= Sort.Direction.ASC) Pageable pageable){
        String URL=request.getServletPath()+"?";
        Page<Supplier> page=supService.findAll(pageable);
        model.addAttribute("URL", URL);
        model.addAttribute("datas",page);
        return "material/admSupplier";
    }

    /*查询表supplier所有数据，返回类型是List*/
    @GetMapping("/getAllSupplier")
    @ResponseBody
    public List<Supplier> findAllWithList(){
        return supService.findAllWithList();
    }

    /*根据各种条件进行查询*/
    @GetMapping("/find")
    public String findSupplier(HttpServletRequest request,Model model,Supplier supplier,
                               @PageableDefault(size=8, sort={"supId"}, direction=Sort.Direction.ASC) Pageable pageable){
        String supType=request.getParameter("supType");
        String supName=request.getParameter("supName");
        String ct=supplier.getSupType();
        String cn=supplier.getSupName();
        String path=request.getServletPath();
        String URL=path+"?supType="+supType+"&supName="+supName+"&";
        model.addAttribute("URL", URL);
        if(ct.equals("-请选择-")){
            ct=null;
        }
        if (ct==null&&cn==null){
            Page<Supplier> page=supService.packToPage(null,pageable);
            model.addAttribute("datas",page);
        }
        else if(ct!=null&&cn!=null){
            Page<Supplier> page=supService.findByBoth(ct,cn,pageable);
            model.addAttribute("datas",page);
        }
        else if(ct!=null&&cn==null){
            Page<Supplier> page=supService.findBySupType(ct,pageable);
            model.addAttribute("datas",page);
        }
        else if(ct==null&&cn!=null){
            Page<Supplier> page=supService.findBySupNameLike(cn,pageable);
            model.addAttribute("datas",page);
        }
        return "material/admSupplier";
    }

    /*实现添加单个供应商*/
    @PostMapping("/addSupplier")
    @ResponseBody
    public Supplier addSupplier(Supplier supplier){
        supplier.setRelation("暂无关联，可以删除");
        try {
            supService.addSupplier(supplier);
            supplier.setMsg("添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            supplier.setMsg("添加失败");
        }
        return supplier;
    }

    /*通过id获取要修改的供应商信息*/
    @GetMapping("/getSupMsg")
    @ResponseBody
    public Supplier getMsg(Supplier supplier){
        Integer id=supplier.getSupId();
        return supService.findBySupId(id);
    }

    /*修改供应商，并保存*/
    @PostMapping("/modSupMsg")
    @ResponseBody
    public Supplier modSupMsg(@ModelAttribute Supplier supplier){
        Integer supId=supplier.getSupId();
        String relation=supService.findRelationBySupId(supId);
        supplier.setRelation(relation);
        try {
            supService.updateSupplier(supplier);
            supplier.setMsg("修改成功！");
        } catch (Exception e) {
            e.printStackTrace();
            supplier.setMsg("修改失败！");
        }
        return supplier;
    }

    /*根据供应商id,删除供应商*/
    @PostMapping("/delSupMsg")
    @ResponseBody
    public Supplier delSupMsg(Supplier supplier){
        Integer supId=supplier.getSupId();
        try {
            supService.delSupplier(supId);
            supplier.setMsg("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            supplier.setMsg("删除失败");
        }
        return supplier;
    }
}
