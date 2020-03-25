package com.hsj.dao;

import com.hsj.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/*用户-JPA操作接口*/
public interface UserDao extends JpaRepository<User, Integer> {
    /*根据用户名和密码查询用户数据*/
    public User findByUserNameAndUserPasswd(String userName, String userPasswd);
    /*根据用户名查询用户数据*/
    public User findByUserName(String userName);
}
