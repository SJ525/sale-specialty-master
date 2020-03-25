package com.hsj.controller;

import com.hsj.entity.Customer;
import com.hsj.service.CusService;
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
 *@ClassName CusController
 *@Description 客户控制器类
 *@Author hsj
 *Date 2020/3/4 13:07
 */
@Controller
@RequestMapping("/customer")
public class CusController {
    @Autowired
    CusService cusService;

    /*查询所有客户,返回数据类型是Page*/
    @GetMapping("/list")
    public String findAll(HttpServletRequest request, Model model,
                          @PageableDefault(size=8, sort={"cusId"}, direction=Sort.Direction.ASC) Pageable pageable){
        String URL=request.getServletPath()+"?";
        Page<Customer> page=cusService.findAll(pageable);
        model.addAttribute("URL", URL);
        model.addAttribute("datas",page);
        return "material/admCustomer";
    }

    /*查询所有客户,返回数据类型是List*/
    @GetMapping("/getAllCustomer")
    @ResponseBody
    public List<Customer> findAllCustomer(){
        return cusService.findAllWithList();
    }

    /*根据各种条件进行查询*/
    @GetMapping("/find")
    public String findCustomer(HttpServletRequest request,Model model,Customer customer,
                               @PageableDefault(size=8, sort={"cusId"}, direction=Sort.Direction.ASC) Pageable pageable){
        String cusType=request.getParameter("cusType");
        String cusName=request.getParameter("cusName");
        String ct=customer.getCusType();
        String cn=customer.getCusName();
        String path=request.getServletPath();
        String URL=path+"?cusType="+cusType+"&cusName="+cusName+"&";
        model.addAttribute("URL", URL);
        if(ct.equals("-请选择-")){
            ct=null;
        }
        if (ct==null&&cn==null){
            Page<Customer> page=cusService.packToPage(null,pageable);
            model.addAttribute("datas",page);
        }
        else if(ct!=null&&cn!=null){
            Page<Customer> page=cusService.findByBoth(ct,cn,pageable);
            model.addAttribute("datas",page);
        }
        else if(ct!=null&&cn==null){
            Page<Customer> page=cusService.findByCusType(ct,pageable);
            model.addAttribute("datas",page);
        }
        else if(ct==null&&cn!=null){
            Page<Customer> page=cusService.findByCusNameLike(cn,pageable);
            model.addAttribute("datas",page);
        }
        return "material/admCustomer";
    }

    /*实现添加单个用户*/
    @PostMapping("/addCustomer")
    @ResponseBody
    public Customer addCustomer(Customer customer){
        customer.setRelation("暂无关联，可以删除");
        try {
            cusService.addCustomer(customer);
            customer.setMsg("添加成功！");
        } catch (Exception e) {
            e.printStackTrace();
            customer.setMsg("添加失败！");
        }
        return customer;
    }

    /*通过id获取要修改的客户信息*/
    @GetMapping("/getCusMsg")
    @ResponseBody
    public Customer getMsg(Customer customer){
        Integer id=customer.getCusId();
        return cusService.findByCusId(id);
    }

    /*修改客户，并保存*/
    @PostMapping("/modCusMsg")
    @ResponseBody
    public Customer modCusMsg(@ModelAttribute Customer customer){
        Integer cusId=customer.getCusId();
        String relation=cusService.findRelationByCusId(cusId);
        customer.setRelation(relation);
        try {
            cusService.updateCustomer(customer);
            customer.setMsg("修改成功！");
        } catch (Exception e) {
            e.printStackTrace();
            customer.setMsg("修改失败！");
        }
        return customer;
    }

    /*根据客户id,删除客户*/
    @PostMapping("/delCusMsg")
    @ResponseBody
    public Customer delCusMsg(Customer customer){
        Integer cusId=customer.getCusId();
        try {
            cusService.delCustomer(cusId);
            customer.setMsg("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            customer.setMsg("删除失败");
        }
        return customer;
    }

}
