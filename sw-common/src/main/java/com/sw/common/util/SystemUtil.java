package com.sw.common.util;


import com.sw.common.constants.BaseConstants;

import java.io.File;
import java.net.URISyntaxException;

/**
 * 系统工具类
 * @Author: yu.leilei
 * @Date: 上午 11:17 2018/6/8 0008
 */
public class SystemUtil {

    /**
     * 获取class文件
     * @param path
     * @param classLoader
     * @return
     */
    public static File[] getClassFile(String path, ClassLoader classLoader){
        if(path.contains(BaseConstants.POINT)){
            path = path.replace(".", "/");
        }
        File file = null;
        try {
            file = new File(classLoader.getResource(path).toURI());
        } catch (URISyntaxException e) {
            new RuntimeException("没有找到指定的路径文件");
        }
        return file.listFiles(pathname -> {
            if(pathname.getName().endsWith(BaseConstants.END_CLASS)){
                return true;
            }
            return false;
        });
    }
}
