package com.hsj.controller;

import com.hsj.entity.Category;
import com.hsj.service.CateService;
import com.hsj.util.ChangToPage;
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
 *@ClassName CateController
 *@Description 特产类型管理控制器
 *@Author hsj
 *Date 2020/2/27 15:30
 */
@Controller
@RequestMapping("/category")
public class CateController {

    @Autowired
    CateService cateService;

    //进行分页并跳转到管理特产类页面
    //每页最多8行
    @GetMapping("/list")
    public String admProductType(HttpServletRequest request,Model model,
                                 @PageableDefault(size=8, sort={"proTypeId"}, direction=Sort.Direction.ASC) Pageable pageable){
        String proTypeName=request.getParameter("proTypeName");
        String URL=request.getServletPath()+"?";
        Page<Category> page=cateService.findAll(pageable);
        model.addAttribute("proTypeName",proTypeName);
        model.addAttribute("URL", URL);
        model.addAttribute("datas",page);
        return "product/admProType";
    }

    /*查找表category的所有数据，返回数据类型是List*/
    @GetMapping("/getAllCategory")
    @ResponseBody
    public List<Category> getAllCategory(){
        return cateService.findAll();
    }

    //实现根据特产类型名称进行模糊查找数据
    @GetMapping("/findType")
    public String findByProTypeNameLike(HttpServletRequest request,Model model,Category category,
                                        @PageableDefault(size=8, sort={"proTypeId"}, direction=Sort.Direction.ASC) Pageable pageable){

        Page<Category> page=changeToPage(category,pageable);
        String proTypeName=request.getParameter("proTypeName");
        String URL=request.getServletPath()+"?proTypeName="+proTypeName+"&";
        model.addAttribute("proTypeName",proTypeName);
        model.addAttribute("URL", URL);
        model.addAttribute("datas",page);
        return "product/admProType";
    }

    //将查询到的数据封装成Page<T>类型
    public Page<Category> changeToPage(Category category,Pageable pageable)
    {
        List<Category> list=cateService.findByProTypeNameLike(category.getProTypeName());
        ChangToPage<Category> changToPage=new ChangToPage<>();
        return changToPage.getPage(list,pageable);
    }

    //实现添加特产类型
    @PostMapping("/addProType")
    @ResponseBody
    public Category addProType(Category category){
        category.setRelation("暂无关联，可以删除");
        try {
            cateService.addProType(category);
            category.setMsg("添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            category.setMsg("添加失败");
        }
        return category;
    }

    //通过id获取修改的特产类型信息
    @GetMapping("/getMsg")
    @ResponseBody
    public Category getMsg(Category category){
        return cateService.findByProTypeId(category.getProTypeId());
    }

    // 执行修改特产类型操作
    @PostMapping("/modProType")
    @ResponseBody
    public Category modProType(@ModelAttribute Category category){
        Integer proTypeId=category.getProTypeId();
        String relation=cateService.findRelationByProTypeId(proTypeId);
        category.setRelation(relation);
        try {
            cateService.modProType(category);
            category.setMsg("修改成功！");
        } catch (Exception e) {
            e.printStackTrace();
            category.setMsg("修改失败！");
        }
        return category;
    }

    //通过id删除特产类型
    @PostMapping("/delProType")
    @ResponseBody
    public Category delProType(Category category){
        try {
            cateService.delProType(category.getProTypeId());
            category.setMsg("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            category.setMsg("删除失败");
        }
        return category;
    }
}
