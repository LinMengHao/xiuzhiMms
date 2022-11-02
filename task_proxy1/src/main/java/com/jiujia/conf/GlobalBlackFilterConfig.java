package com.jiujia.conf;

import com.jiujia.util.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class GlobalBlackFilterConfig {
    /**
     * 能静黑名单配置
     */
    public static final String NJ_USERNAME;
    public static final String NJ_PASSWORD;
    //客户端url
    public static final String NJ_SECRETKEY;
    public static  final String NJ_SERVICEURL;
    /**
     * 江苏
     */
    public static final List<Integer> JS_BLACK_LEVEL=new ArrayList<>();

    /**
     * 黑名单过滤策略
     */
    public static final List<String> BLACK_SORT=new ArrayList<>();

    /**
     * 东云短信
     */
    public static final String DY_DX_ACCESSKEY;
    public static final String DY_DX_SECRETKEY;
    public static final String DY_DX_SERVICEURL;


    /**
     * 东云语音
     */
    public static final String DY_VOICE_ACCESSKEY;
    public static final String DY_VOICE_SECRETKEY;
    public static final String DY_VOICE_SERVICEURL;


    static {
        InputStream inputStream = ClassLoader.getSystemResourceAsStream("black.properties");
        Properties properties = new Properties();
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String levels = properties.getProperty("JS.BLACK.LEVEL");
        if (StringUtils.isNotBlank(levels)){
            String[] split = levels.split(",");
            for (int i = 0; i < split.length; i++) {
                JS_BLACK_LEVEL.add(Integer.valueOf(split[i]));
            }
        }
        NJ_SECRETKEY = properties.getProperty("NJ.SECRETKEY");
        String property = properties.getProperty("BLACK.SORT");
        String[] split1 = property.split(",");
        for (int i = 0; i < split1.length; i++) {
            BLACK_SORT.add(split1[i]);
        }
        NJ_USERNAME = properties.getProperty("NJ.USERNAME");
        NJ_PASSWORD = properties.getProperty("NJ.PASSWORD");
        NJ_SERVICEURL = properties.getProperty("NJ.SERVICEURL");

        DY_DX_ACCESSKEY = properties.getProperty("DY.DX.ACCESSKEY");
        DY_DX_SECRETKEY = properties.getProperty("DY.DX.SECRETKEY");
        DY_DX_SERVICEURL = properties.getProperty("DY.DX.SERVICEURL");

        DY_VOICE_ACCESSKEY = properties.getProperty("DY.VOICE.ACCESSKEY");
        DY_VOICE_SECRETKEY = properties.getProperty("DY.VOICE.SECRETKEY");
        DY_VOICE_SERVICEURL = properties.getProperty("DY.VOICE.SERVICEURL");
    }

    public static void main(String[] args) {
        System.out.println(NJ_PASSWORD);
        System.out.println(NJ_SECRETKEY);
        System.out.println(NJ_SERVICEURL);
        System.out.println(NJ_USERNAME);
        System.out.println(JS_BLACK_LEVEL.toString());
        System.out.println(BLACK_SORT.toString());
        System.out.println(DY_DX_ACCESSKEY);
        System.out.println(DY_DX_SECRETKEY);
        System.out.println(DY_DX_SERVICEURL);
        System.out.println(DY_VOICE_SECRETKEY);
        System.out.println(DY_VOICE_ACCESSKEY);
        System.out.println(DY_VOICE_SERVICEURL);
    }
}
