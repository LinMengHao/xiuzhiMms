package com.jiujia.controller;

import com.alibaba.fastjson.JSONObject;
import com.jiujia.service.IApiService;
import com.jiujia.util.Base64Util;
import com.jiujia.util.HttpInterface;
import com.jiujia.util.PublicUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(value="mms")
public class ApiController {
    public static Logger logger = LoggerFactory.getLogger("ApiController");
    @Resource
    private IApiService apiService;

    @RequestMapping("submit")
    @ResponseBody
    public String mmsSend(HttpServletRequest request, HttpServletResponse response, @RequestBody String body) {
        long begin = System.currentTimeMillis();

        String ipAddress = PublicUtil.getClientIp(request);
        String result = apiService.mmsSend(body,ipAddress);

        long duration = System.currentTimeMillis()-begin;
        logger.info("mmsSend,body:{},ipAddress:{},result:{},duration:{}",body,ipAddress,result,duration);
        return result;
    }
    @RequestMapping("list")
    @ResponseBody
    public String mmsList(HttpServletRequest request, HttpServletResponse response, @RequestBody String body) {
        long begin = System.currentTimeMillis();

        String ipAddress = PublicUtil.getClientIp(request);
        String result = apiService.mmsList(body,ipAddress);

        long duration = System.currentTimeMillis()-begin;
        logger.info("mmsSend,body:{},ipAddress:{},result:{},duration:{}",body,ipAddress,result,duration);
        return result;
    }

    @RequestMapping("model/submit")
    @ResponseBody
    public String modelSubmit(HttpServletRequest request, HttpServletResponse response) {
        long begin = System.currentTimeMillis();
        System.out.println("-----------------getToken-------------");
        String ipAddress = PublicUtil.getClientIp(request);
        String apiKey="XIUZHIBJ01";
        String apiSecrect="x1&@9%&6";

        String token="";
        String url = "http://36.138.133.214:30080/api/user/token";
        long reqTime=System.currentTimeMillis();
        String signStr="apiKey="+apiKey+"&apiSecrect="+apiSecrect+"&reqTime="+reqTime;
        String sign= Base64Util.encodeBASE64(DigestUtils.sha256(signStr));
        JSONObject json = new JSONObject();
        json.put("apiKey",apiKey);
        json.put("apiSecrect",apiSecrect);
        json.put("reqTime",reqTime);
        json.put("sign",sign);

        String str = HttpInterface.httpClientPostBody(url, json.toJSONString(), 30000, "UTF-8");
        if(str.indexOf("http请求异常")==0){
            str = HttpInterface.httpClientPostBody(url, json.toJSONString(), 30000, "UTF-8");
        }
        System.out.println("-----------------getToken-------------"+str);
        long duration = System.currentTimeMillis()-begin;
        logger.info("mmsSend,body:{},ipAddress:{},result:{},duration:{}","body",ipAddress,"result",duration);
        return str;
    }

    @RequestMapping("report")
    @ResponseBody
    public String mmsReport(HttpServletRequest request, HttpServletResponse response, @RequestParam String table,@RequestParam String ids) {
        long begin = System.currentTimeMillis();

        String ipAddress = PublicUtil.getClientIp(request);
        String result = apiService.mmsReport(table,ids,ipAddress);

        long duration = System.currentTimeMillis()-begin;
        logger.info("mmsSend,table:{},ids:{},ipAddress:{},result:{},duration:{}",table,ids,ipAddress,result,duration);
        return result;
    }
}
