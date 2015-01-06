package com.skysoft.util;

import com.skysoft.framework.NetWorkHandlerData;
import org.apache.commons.io.IOUtils;
import org.codehaus.plexus.util.FileUtils;
import org.springframework.util.StringUtils;

import java.io.*;

/**
 * User: pinaster1130
 * Date: 14-1-4
 * Time: 下午11:39
 */

public class FilesUtils {
    private static String IMG_WEBAPP_DIR = null;

    public static void main(String[] args) {
        String ImageURL = "http://www.sfda.gov.cn/directory/web/WS01/images/1388044975577.jpg";


//      CreateImageFile("directory/web/WS01/images/");
//      String imageText=getImageName("http://www.sfda.gov.cn/directory/web/WS01/images/1388044975577.jpg");
//      System.out.println(imageText);
        CreateImageFile(ImageURL);
        byte[] imgeStream = NetWorkHandlerData.fetchNetWorkAttachment(ImageURL);
//        saveAttachment(imgeStream, getImageName(ImageURL));

    }

    // 创建图片文件的位置
    public static void CreateImageFile(String url) {
        url = url.replaceAll("\\w+\\:\\/\\/\\w+\\.\\w+\\.\\w+\\.\\w+\\/", "");
        String fileName = StringUtils.getFilename(url);
        IMG_WEBAPP_DIR = url.substring(0, url.indexOf(fileName));
        File file = new File(IMG_WEBAPP_DIR);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    // 获得文件的名称
    public static String getImageName(String urls) {
        return IMG_WEBAPP_DIR + StringUtils.getFilename(urls);
    }



}
