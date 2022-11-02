package com.jiujia.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.jiujia.redis.RedisUtils;
import com.jiujia.util.ConstantsReport;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("backService")
public class BackServiceController {
    public static Logger logger = LoggerFactory.getLogger("BackServiceImpl");

    @RequestMapping("/mt01")
    public String mt01(@RequestParam String body,@RequestParam String channelId,@RequestParam String ipAddress) {
        //请求内容{"mobile":"15811047193","messageId":"20220816_1659346550002","type":0,"notifyUrl":"http://103.29.16.3:9130/back/mt01","status":"0","msg":"DELIVRD","bizCode":"2384075185690528863"}
        if (StringUtils.isBlank(body)) {
            return ConstantsReport.report01Resp("9999","内容为空");
        }
        JSONObject bodyJson=null;
        try {
            bodyJson = JSONObject.parseObject(body);
        }catch (Exception e){
            return ConstantsReport.report01Resp("9998","参数异常，非json格式");
        }
        String mobile = bodyJson.containsKey("mobile")?bodyJson.getString("mobile"):"";
        String messageId = bodyJson.containsKey("messageId")?bodyJson.getString("messageId"):"";
        //下发状态, 0：短信  1：彩信
        int type = bodyJson.containsKey("type")?bodyJson.getInteger("type"):1;
        //下发状态：0-成功，其他-失败(对应msg描述)
        String status = bodyJson.containsKey("status")?bodyJson.getString("status"):"-1";
        String msg = bodyJson.containsKey("msg")?bodyJson.getString("msg"):"-1";
        //平台唯一ID
        String bizcode = bodyJson.containsKey("bizcode")?bodyJson.getString("bizcode"):"";

        String jsonStr =  RedisUtils.hash_get(RedisUtils.HASH_MMS_MT_CHANNEL+channelId+":"+messageId,mobile);
        if(StringUtils.isBlank(jsonStr)){
            return ConstantsReport.report01Resp("9997","重复推送或无对应数据");
        }
        JSONObject json = JSONObject.parseObject(jsonStr);
        ConstantsReport.updateMmsReport(json,channelId,messageId,mobile,status,msg,bizcode);

        return ConstantsReport.report01Resp("0","接收成功");
    }

}
