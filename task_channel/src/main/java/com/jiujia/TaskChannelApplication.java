package com.jiujia;

import com.jiujia.sms.constants.ThreadNoticeChannel;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TaskChannelApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaskChannelApplication.class, args);
        //视频短信by渠道
        new ThreadNoticeChannel().start();
    }

}
