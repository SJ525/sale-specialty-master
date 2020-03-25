package com.hsj.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/*
 *@ClassName GenerateNumber
 *@Description
 *@Author hsj
 *Date 2020/3/7 20:41
 */
public class GenNumber {
    /**
     * 生成订单编号
     * @return
     */
    private static long orderNum =(long)(Math.random()*100);
    private static String date ;

    public static synchronized String getOrderNo() {
        String str = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        if(date==null||!date.equals(str)){
            date = str;
        }
        long orderNo = Long.parseLong((date));
        orderNo += orderNum;;
        return orderNo+"";
    }


}
