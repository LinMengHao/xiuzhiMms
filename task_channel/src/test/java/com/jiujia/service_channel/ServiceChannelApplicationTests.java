package com.jiujia.service_channel;

import com.alibaba.fastjson.JSONObject;
import com.jiujia.util.Base64Util;
import com.jiujia.util.HttpInterface;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Hashtable;

@SpringBootTest
class ServiceChannelApplicationTests {

    @Test
    void contextLoads() {

        try {
            String apiKey="XIUZHIBJ01";
            String apiSecrect="x1&@9%&6";
            String url = "http://36.138.133.214:30080/api/send/mms";

            String channelId="1";
            String mmsId="1234567";
            String token=getToken(apiKey,apiSecrect,channelId);
            System.out.println("------token-----"+token);

            String mobile="15811047193";//手机号
            String messageId="12345678";//客户唯一id
            int sendType = 1;//1-普通视频短信发送，2-变量视频短信发送
            String signStr="messageId="+messageId+"&mmsId="+mmsId+"&mobile="+mobile;
            String variableText="";//非必填，变量参数 姓名=599,性别=599
            if(sendType==2){
                variableText+="";
                signStr="messageId="+messageId+"&mmsId="+mmsId+"&mobile="+mobile+"&variableText="+variableText;
            }
            String sign= Base64Util.encodeBASE64(DigestUtils.sha256(signStr));
            JSONObject submitJson = new JSONObject();
            submitJson.put("mmsId",mmsId);
            submitJson.put("mobile",mobile);
            submitJson.put("messageId",messageId);
            //submitJson.put("variableText",variableText);
            submitJson.put("sign",sign);

            Hashtable<String, String> formHeaders = new Hashtable<String, String>();
            formHeaders.put("Content-Type", "application/json;charset=utf-8");
            formHeaders.put("Authorization", token);
            String str = HttpInterface.httpClientPost(url, submitJson.toJSONString(), 30000, "UTF-8", formHeaders);
            if(str.indexOf("http请求异常")==0){
                str = HttpInterface.httpClientPost(url, submitJson.toJSONString(), 30000, "UTF-8", formHeaders);
            }
            System.out.println("-----------str----------"+str);
            String code="";
            String msg="";
            String channneMsgId="";
            if(str.indexOf("http请求异常")==0){
                code="-1";
                msg=str;
            }else{
                JSONObject result = JSONObject.parseObject(str);
                code = result.getString("code");
                msg = result.getString("msg");
                channneMsgId = result.getString("messageId");
            }
            System.out.println("-----------code----------"+code);
            //提交成功
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    private synchronized String getToken(String apiKey,String apiSecrect,String channelId) {
        /*String token=RedisUtils.string_get(RedisUtils.STR_CHANNEL_TOKEN+channelId);
        if(StringUtils.isNotBlank(token)){
            return token;
        }*/
        String token="";
        String url = "http://36.138.133.214:30080/api/user/token";
        long reqTime=System.currentTimeMillis();
        String signStr="apiKey="+apiKey+"&apiSecrect="+apiSecrect+"&reqTime="+reqTime;
        String sign= Base64Util.encodeBASE64(Base64Util.getSHA256(signStr));
        JSONObject json = new JSONObject();
        json.put("apiKey",apiKey);
        json.put("apiSecrect",apiSecrect);
        json.put("reqTime",reqTime);
        json.put("sign",sign);
        System.out.println("-------------------"+json.toJSONString());
        String str = HttpInterface.httpClientPostBody(url, json.toJSONString(), 30000, "UTF-8");
        if(str.indexOf("http请求异常")==0){
            str = HttpInterface.httpClientPostBody(url, json.toJSONString(), 30000, "UTF-8");
        }
        if(str.indexOf("http请求异常")==0){
            token="";
        }else{
            JSONObject result = JSONObject.parseObject(str);
            String code = result.getString("code");
            if(code.equals("0")){
                token=result.getString("token");
                //RedisUtils.string_set(RedisUtils.STR_CHANNEL_TOKEN+channelId,24*3600,token);
            }else{
                token="";
            }
        }
        System.out.println("-------------------"+str);
        return token;
    }
}
