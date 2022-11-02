package com.jiujia.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "demo-service",contextId = "apiService")
public interface IApiService {

    /**
     * 视频短信提交
     * @return
     */
    @RequestMapping(value = "/apiService/mmsSend", method = RequestMethod.GET)
    String mmsSend(@RequestParam String body,@RequestParam String ipAddress);

    /**
     * 视频短信状态报告查询
     * @return
     */
    @RequestMapping(value = "/apiService/mmsList", method = RequestMethod.GET)
    String mmsList(@RequestParam String body,@RequestParam String ipAddress);

    /**
     * 视频短信状态报告重推
     * @return
     */
    @RequestMapping(value = "/apiService/mmsReport", method = RequestMethod.GET)
    String mmsReport(@RequestParam String table,@RequestParam String ids,@RequestParam String ipAddress);
}
