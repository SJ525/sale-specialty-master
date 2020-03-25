package com.hsj.controller;

import com.hsj.entity.User;
import com.hsj.service.UserService;
import com.hsj.util.CheckFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/*
 *@ClassName UserController
 *@Description 登陆控制，处理用户登陆
 *@Author hsj
 *Date 2020/2/26 14:38
 */
@Controller
public class UserController {

    @Autowired
    UserService userService;

    //跳转到登陆页面
    @GetMapping("/")
    public String toLogin(){
        return "login";
    }

    //跳转到首页
    @GetMapping("/index")
    public String toIndex(){
        return "index";
    }

    //跳转到注册页面
    @GetMapping("/user/register")
    public String userRegister(){
        return "user/register";
    }

    //跳转到找回密码页面
    @GetMapping("/user/modPasswd")
    public String userModPasswd(){
        return "user/modPasswd";
    }

    //用户登陆控制器
    @GetMapping("/toLogin")
    public String handleLogin(@ModelAttribute User user, HttpSession session, Model model){
        if(userService.findByUserName(user.getUserName())==null){
            model.addAttribute("msg", "用户名不存在，请注册！");
            model.addAttribute("name", user.getUserName());
            model.addAttribute("passwd", user.getUserPasswd());
            return "login";
        }
        User loginUser= userService.toLogin(user);
        //登陆验证
        if (loginUser==null){
            //登陆失败，返回登陆界面
            model.addAttribute("msg", "密码错误，请重新输入");
            model.addAttribute("name", user.getUserName());
            model.addAttribute("passwd", user.getUserPasswd());
            return "login";
        }
        else {
            // 登录成功
            //session.setAttribute("user", loginUser);
            return "index";
        }
    }

    //用户注册控制器
    @PostMapping("/toRegister")
    public String handleRegister(@ModelAttribute User user,Model model)
    {
        String username=user.getUserName();
        String passwd=user.getUserPasswd();
        String email=user.getUserEmail();
        User dbUser=userService.findByUserName(username);
        if(dbUser!=null){
            model.addAttribute("msg1", "用户名已存在！");
            model.addAttribute("msg4", "注册失败！");
            model.addAttribute("username", username);
            model.addAttribute("passwd", passwd);
            model.addAttribute("email", email);
            return "/user/register";
        }
        if(CheckFormat.checkLength(passwd)==false){
            model.addAttribute("msg2", "密码长度少于6位！");
            model.addAttribute("msg4", "注册失败！");
            model.addAttribute("username", username);
            model.addAttribute("passwd", passwd);
            model.addAttribute("email", email);
            return "/user/register";
        }
        if(CheckFormat.checkEmail(email)==false){
            model.addAttribute("msg3", "邮箱格式不正确！");
            model.addAttribute("msg4", "注册失败！");
            model.addAttribute("username", username);
            model.addAttribute("passwd", passwd);
            model.addAttribute("email", email);
            return "/user/register";
        }
        User user1=userService.toRegister(user);
        if(user1!=null){
            model.addAttribute("msg4", "注册成功！");
        }
        return "/user/register";
    }

    //用户密码修改控制器
    @PostMapping("/toModPasswd")
    public String handleMOdify(@ModelAttribute User user,Model model){
        String name=user.getUserName();
        String passwd=user.getUserPasswd();
        String repasswd=user.getRePasswd();
        if(userService.findByUserName(name)==null){
            model.addAttribute("msg1", "该用户名尚未注册！");
            model.addAttribute("msg4", "修改失败！");
            model.addAttribute("name", name);
            model.addAttribute("passwd", passwd);
            model.addAttribute("repasswd", repasswd);
            return "user/modPasswd";
        }
        if(CheckFormat.checkLength(passwd)==false){
            model.addAttribute("msg2", "密码长度小于6位！");
            model.addAttribute("msg4", "修改失败！");
            model.addAttribute("name", name);
            model.addAttribute("passwd", passwd);
            model.addAttribute("repasswd", repasswd);
            return "user/modPasswd";
        }
        if (passwd.equals(repasswd)==false){
            model.addAttribute("msg3", "上下密码不一致！");
            model.addAttribute("msg4", "修改失败！");
            model.addAttribute("name", name);
            model.addAttribute("passwd", passwd);
            model.addAttribute("repasswd", repasswd);
            return "user/modPasswd";
        }
        User user1=userService.toModPassWd(user);
        if(user1!=null){
            model.addAttribute("msg4", "修改成功！");
        }
        return "user/modPasswd";
    }

    //退出系统控制器
    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.removeAttribute("user");
        return "login";
    }

    //注销账户控制器
    @PostMapping("/cancel")
    @ResponseBody
    public User delUser(HttpSession session){
        User user=(User)session.getAttribute("user");
        User user1=new User();
        //根据用户删除数据库对应的数据
        try {
            userService.delUser(user);
            session.removeAttribute("user");
            user1.setMsg("注销成功！");
            return user1;
        } catch (Exception e) {
            e.printStackTrace();
            user1.setMsg("注销失败！");
            return user1;
        }
    }
}
