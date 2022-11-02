package com.jiujia;

import com.jiujia.sms.constants.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TaskProxyApplication {

    public static void main(String[] args) {

        SpringApplication.run(TaskProxyApplication.class, args);

        //数据库
        new ThreadDataBase().start();
        //加载用户信息进redis缓存
        new ThreadAppBase().start();
        //视频彩信发送路由预处理
        new ThreadSendPretreat().start();

        //视频短信补呼路由预处理
        //new ThreadNoticeRecall().start();

        //客户回调通知
        new ThreadCallBack().start();
        //回调补呼
        new ThreadCallbackMore().start();

        //api重推客户状态报告
        new ThreadPushReport().start();
    }

}
