package com.jiujia.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "demo-service",contextId = "backService")
public interface IBackService {

    /**
     * 北京移动视频短信通道-10693878--01--状态报告
     * @return
     */
    @RequestMapping(value = "/backService/mt01", method = RequestMethod.GET)
    String mt01(@RequestParam String body,@RequestParam String channelId,@RequestParam String ipAddress);

}
