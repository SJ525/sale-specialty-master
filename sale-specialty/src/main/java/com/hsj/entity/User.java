package com.hsj.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

/*
 *@ClassName User
 *@Description
 *@Author hsj
 *Date 2020/2/26 12:19
 */
@Entity
@Table(name = "user")
@NoArgsConstructor
@Setter
@Getter
@ToString
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;     //用户id
    private String userName;    //用户名
    private String userPasswd;  //用户密码
    @Transient
    private String rePasswd;    //确认密码
    private String userEmail;   //用户邮箱
    @Transient
    private String msg;         //消息提示
}
