package com.hsj.util;

/*
 *@ClassName CheckFormat
 *@Description 校验格式类
 *@Author hsj
 *Date 2020/2/26 21:55
 */
public class CheckFormat {
    public static final String EMAIL="\\w[-\\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\\.)+[A-Za-z]{2,14}";
    //校验密码长度，不小于6位
    public static boolean checkLength(String str){
        return str.length()>=6?true:false;
    }

    // 校验邮箱地址
    public static boolean checkEmail(String email) {
        return email.matches(EMAIL);
    }

}
