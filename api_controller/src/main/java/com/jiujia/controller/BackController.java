package com.jiujia.controller;

import com.jiujia.service.IBackService;
import com.jiujia.util.PublicUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(value="back")
public class BackController {
    public static Logger logger = LoggerFactory.getLogger("BackController");
    @Resource
    private IBackService backService;

    //北京移动3878--01
    @RequestMapping("mt01")
    @ResponseBody
    public String mt01(HttpServletRequest request, HttpServletResponse response,@RequestBody String body) {
        long begin = System.currentTimeMillis();

        String ipAddress = PublicUtil.getClientIp(request);
        String result = backService.mt01(body,"1",ipAddress);

        long duration = System.currentTimeMillis()-begin;
        logger.info("mmsReport01,body:{},ipAddress:{},result:{},duration:{}",body,ipAddress,result,duration);
        return result;
    }
    //北京移动3878--02
    @RequestMapping("mt02ts")
    @ResponseBody
    public String mt02ts(HttpServletRequest request, HttpServletResponse response,@RequestBody String body) {
        long begin = System.currentTimeMillis();

        String ipAddress = PublicUtil.getClientIp(request);
        String result = backService.mt01(body,"2",ipAddress);

        long duration = System.currentTimeMillis()-begin;
        logger.info("mmsReport02,body:{},ipAddress:{},result:{},duration:{}",body,ipAddress,result,duration);
        return result;
    }


}
