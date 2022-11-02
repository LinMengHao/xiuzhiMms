package com.jiujia.util;

import com.alibaba.fastjson.JSONObject;
import com.jiujia.redis.RedisUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ConstantsReport {

    public static final String SUCCESS = "0";
    public static final String RESP_IS_EMPTY = "99";//空响应
    public static final String MODEL_IS_EMPTY = "MMS:MODEL";
    public static final String MT_RPT_DELIVERED = "DELIVRD";
    public static final String MT_RPT_UNDELIVERED = "UNDELIV";
    public static final String MT_RPT_BALANCE = "MMS:LIMIT";
    public static final String MT_RPT_NOROUTE = "MMS:NOROUTE";
    public static final String MT_RPT_TO_BLACKLISTED = "MMS:BLACK";
    public static final String MT_RPT_TO_PROVINCE_BLOCK = "MMS:PROVINCE_BLOCK";

    public static void updateMmsReport(JSONObject json,String channelId,String messageId,String mobile,String status,String msg,String bizcode){

        String companyId = json.getString("companyId");
        String tableName = json.getString("tableName");
        String linkId = json.getString("linkId");

        String nowTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String reportTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        if(status.equals("0")){
            status="DELIVRD";
        }else{
            //status="UNDELIVRD";
            String appName=json.getString("appName");
            //客户消费数减一
            RedisUtils.hash_incrBy(RedisUtils.HASH_ACC_SEND, appName,-1);
        }
        //客户状态报告推送
        json.put("reportStatus",status);
        json.put("reportTime",reportTime);
        json.put("info",msg);
        RedisUtils.fifo_push(RedisUtils.FIFO_MMS_MT_CLIENT+companyId,json.toJSONString());
        RedisUtils.hash_incrBy(RedisUtils.HASH_MMS_MT_COUNT, companyId+"", 1);


        //数据入库
        String updateSql = String.format("update %s set status='%s',info='%s',report_time='%s' where link_id='%s';",
                tableName,status,msg,nowTime,linkId);
        RedisUtils.fifo_push(RedisUtils.FIFO_SQL_LIST+companyId,updateSql);
        RedisUtils.hash_incrBy(RedisUtils.HASH_SQL_COUNT, companyId+"", 1);

        //删除渠道状态报告主键
        RedisUtils.hash_remove(RedisUtils.HASH_MMS_MT_CHANNEL+channelId+":"+messageId,mobile);
    }

    public static String report01Resp(String code,String message){
        JSONObject json = new JSONObject();
        json.put("code",code);
        json.put("msg",message);

        return json.toJSONString();
    }

}
