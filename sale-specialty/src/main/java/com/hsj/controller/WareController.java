package com.hsj.controller;

import com.hsj.entity.StoreMan;
import com.hsj.entity.Warehouse;
import com.hsj.service.StoreManService;
import com.hsj.service.WareService;
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
import java.util.List;

/*
 *@ClassName WareController
 *@Description 仓库控制器类
 *@Author hsj
 *Date 2020/3/4 21:35
 */
@Controller
@RequestMapping("/warehouse")
public class WareController {
    @Autowired
    StoreManService storeManService;
    @Autowired
    WareService wareService;

    /*1.查询所有仓库,数据类型是Page*/
    @GetMapping("/list")
    public String findAllWithPage(HttpServletRequest request, Model model,
                          @PageableDefault(size=8, sort={"wareId"}, direction= Sort.Direction.ASC) Pageable pageable){
        String URL=request.getServletPath()+"?";
        Page<Warehouse> page=wareService.findAllWithPage(pageable);
        List<Warehouse> list=page.getContent();
        for (Warehouse warehouse:list){
            Integer wareSmId=warehouse.getWareSmId();
            String wareSmName= null;
            try {
                wareSmName = wareService.findSmNameByWareSmId(wareSmId);
            } catch (Exception e) {
                e.printStackTrace();
            }
            warehouse.setWareSmName(wareSmName);
        }
        model.addAttribute("URL", URL);
        model.addAttribute("datas",page);
        return "material/admWarehouse";
    }

    /*2.查询所有仓库,数据类型是List*/
    @GetMapping("/getAllWarehouse")
    @ResponseBody
    public List<Warehouse> getAllWarehouse(){
        return wareService.findAllWithList();
    }

    /*3.根据各种条件进行查询*/
    @GetMapping("/find")
    public String findWarehouse(HttpServletRequest request,Model model,Warehouse warehouse,
                               @PageableDefault(size=8, sort={"wareId"}, direction=Sort.Direction.ASC) Pageable pageable){
        String wareName=request.getParameter("wareName");
        String wareSmName=request.getParameter("wareSmName");
        String startSize=request.getParameter("startSize");
        String endSize=request.getParameter("endSize");
        String path=request.getServletPath();
        String URL=path+"?wareName="+wareName+"&wareSmName="+wareSmName+"&startSize="+startSize+"&endSize="+endSize+"&";
        model.addAttribute("URL", URL);
        String wn=warehouse.getWareName();
        String wsn=warehouse.getWareSmName();
        Integer wsId=storeManService.findSmIdBySmName(wsn);
        double sz=warehouse.getStartSize();
        double ez=warehouse.getEndSize();
        if(wn==null&&wsId==null){
            Page<Warehouse> page=wareService.findByWareSizeBetween(sz,ez,pageable);
            repeatCode(page,wsn,model);
        }
        if(wn!=null&&wsId!=null){
            Page<Warehouse> page=wareService.findByThreeWays(wn,wsId,sz,ez,pageable);
            repeatCode(page,wsn,model);
        }
        if (wn!=null&&wsId==null){
            Page<Warehouse> page=wareService.findByWareNameLikeAndWareSizeBetween(wn,sz,ez,pageable);
            repeatCode(page,wsn,model);
        }
        if (wn==null&&wsId!=null){
            Page<Warehouse> page=wareService.findByWareSmIdAndWareSizeBetween(wsId,sz,ez,pageable);
            repeatCode(page,wsn,model);
        }
        return "material/admWarehouse";
    }

    /*4.封装-减少重复代码*/
    public void repeatCode(Page<Warehouse> page,String wsn,Model model){
        List<Warehouse> list = page.getContent();
        for (Warehouse ware:list){
            ware.setWareSmName(wsn);
        }
        model.addAttribute("datas",page);
    }

    /*5.实现添加单个仓库*/
    @PostMapping("/addWarehouse")
    @ResponseBody
    public Warehouse addWarehouse(Warehouse warehouse){
        Integer wareSmId=warehouse.getWareSmId();
        String wareSmName=storeManService.findSmNameBySmId(wareSmId);
        warehouse.setWareSmName(wareSmName);
        warehouse.setRelation("暂无关联，可以删除");
        try {
            wareService.addWarehouse(warehouse);
            warehouse.setMsg("添加成功！");
            storeManService.updateRelationBySmId("已有关联，不可删除",wareSmId);
        } catch (Exception e) {
            e.printStackTrace();
            warehouse.setMsg("添加失败！");
        }
        return warehouse;
    }

    /*6.通过id获取要修改的仓库信息*/
    @GetMapping("/getWareMsg")
    @ResponseBody
    public Warehouse getMsg(Warehouse warehouse){
        Integer wareId=warehouse.getWareId();
        Warehouse warehouse1=wareService.findByWareId(wareId);
        Integer wareSmId=warehouse1.getWareSmId();
        String wareSmName=wareService.findSmNameByWareSmId(wareSmId);
        warehouse1.setWareSmName(wareSmName);
        return warehouse1;
    }

    /*7.修改单个仓库信息，并保存*/
    @PostMapping("/modWareMsg")
    @ResponseBody
    public Warehouse modWareMsg(Warehouse warehouse){
        String wareSmName=warehouse.getWareSmName();
        Integer wareSmId=storeManService.findSmIdBySmName(wareSmName);
        warehouse.setWareSmId(wareSmId);
        Integer wareId=warehouse.getWareId();
        String relation=wareService.findRelationByWareId(wareId);
        warehouse.setRelation(relation);
        try {
            wareService.updateWarehouse(warehouse);
            warehouse.setMsg("修改成功！");
        } catch (Exception e) {
            e.printStackTrace();
            warehouse.setMsg("修改失败！");
        }
        codeBlockOne(wareSmId);
        return warehouse;
    }

    /*8.根据仓库id,删除仓库*/
    @PostMapping("/delWareMsg")
    @ResponseBody
    public Warehouse delWareMsg(Warehouse warehouse){
        Integer wareId=warehouse.getWareId();
        Integer wareSmId=wareService.findByWareId(wareId).getWareSmId();
        try {
            wareService.delWarehouse(wareId);
            warehouse.setMsg("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            warehouse.setMsg("删除失败");
        }
        codeBlockOne(wareSmId);
        return warehouse;
    }

    /*9.获取所有仓库管理员的数据，并以List类型返回*/
    @GetMapping("/getAllSm")
    @ResponseBody
    public List<StoreMan> getSmMsg(){
        List<StoreMan> list= storeManService.findAllList();
        return list;
    }

    public void codeBlockOne(Integer wareSmId){
        Integer size=wareService.findWarehousesByWareSmId(wareSmId).size();
        if (size==0){
            storeManService.updateRelationBySmId("暂无关联，可以删除",wareSmId);
        }
        else{
            storeManService.updateRelationBySmId("已有关联，不可删除",wareSmId);
        }
    }
}
