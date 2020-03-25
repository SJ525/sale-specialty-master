package com.hsj.util;

import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;

/*
 *@ClassName ImgSavePath
 *@Description
 *@Author hsj
 *Date 2020/3/1 13:28
 */
public class ImgSavePath {
    //获取图片文件上传后保存的绝对路径
    public static String getSavePath() throws FileNotFoundException {
        //获取根目录
        File path = new File(ResourceUtils.getURL("classpath:").getPath());
        if(!path.exists()) path = new File("");
        File upload = new File(path.getAbsolutePath(),"/src/main/resources/static/upload/img");
        if(!upload.exists()) upload.mkdirs();
        return upload.getAbsolutePath();
    }
}
