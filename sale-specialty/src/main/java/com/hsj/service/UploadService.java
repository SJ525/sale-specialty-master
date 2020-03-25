package com.hsj.service;

import com.hsj.util.ImgSavePath;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/*
 *@ClassName UploadService
 *@Description 文件上传服务器
 *@Author hsj
 *Date 2020/3/1 13:57
 */
@Service
@Transactional
public class UploadService {

    public void uploadFile(byte[] file, String fileName) throws IOException {
        String filePath= ImgSavePath.getSavePath();
        File targetFile = new File(filePath);
        if(!targetFile.exists()){
            targetFile.mkdirs();
        }
        FileOutputStream out = new FileOutputStream(filePath+fileName);
        out.write(file);
        out.flush();
        out.close();
    }
}
