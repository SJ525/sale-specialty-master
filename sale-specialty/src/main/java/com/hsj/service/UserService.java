package com.hsj.service;

import com.hsj.dao.UserDao;
import com.hsj.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/*
 *@ClassName UserService
 *@Description
 *@Author hsj
 *Date 2020/2/26 18:34
 */
@Service
@Transactional
public class UserService {
    @Autowired
    UserDao userDao;

    //实现登陆方法
    public User toLogin(User user){
        User user1=userDao.findByUserNameAndUserPasswd(user.getUserName(),user.getUserPasswd());
        return user1;
    }

    //实现注册方法,等同增加单个用户
    public User toRegister(User user){
        return userDao.save(user);
    }

    //实现修改密码方法,根据userId和userName修改密码
    public User toModPassWd(User user){
        User user1=userDao.findByUserName(user.getUserName());
        //更新密码
        user1.setUserPasswd(user.getUserPasswd());
        return userDao.save(user1);
    }

    //实现根据用户名查找用户
    public User findByUserName(String userName){
        return userDao.findByUserName(userName);
    }

    //实现根据用户来删除用户
    public void delUser(User user){userDao.delete(user);}
}
