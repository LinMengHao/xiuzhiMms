package com.jiujia.sms.constants;

import com.alibaba.fastjson.JSONObject;
import com.jiujia.redis.RedisUtils;
import com.jiujia.sms.handler.HandlerBeiJingCmcc;
import com.jiujia.sms.handler.HandlerBeiJingCmcc2;
import com.jiujia.util.BusinessWorker;
import com.jiujia.util.MmsUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class ThreadNoticeChannel extends Thread{
	public static Logger logger = LoggerFactory.getLogger("ThreadNoticeChannel");

	public void run() { 
		logger.info("开始执行视频短信渠道任务。。。");
		RedisUtils.string_set(RedisUtils.STR_KEY_AI_TASK,"run");
		int i=0;
		while(true){
			//long bt = System.currentTimeMillis();
			String conf_key = RedisUtils.HASH_CHANNEL_INFO+"mms";
			Map<String, String> map = RedisUtils.hash_getFields(conf_key);
			for (Map.Entry<String,String> en : map.entrySet()) {
				String channelId = en.getKey();
				String values = en.getValue();
				if(channelId.equals("0")){
					continue;
				}
				JSONObject json = JSONObject.parseObject(values);
				int link_max=json.getInteger("link_max");//最大连接数
				int link_speed=json.getInteger("link_speed");//每个连接最大发送速度

				String channelThread = RedisUtils.hash_get(RedisUtils.HASH_CHANNEL_THREAD_COUNT,channelId);
				int threadCount = Integer.parseInt(StringUtils.isNotBlank(channelThread)?channelThread:"0");
				if(threadCount>=link_max){
					continue;
				}
				Map<String, String> channelMap = RedisUtils.hash_getFields(RedisUtils.HASH_CHANNEL_REQ_COUNT+channelId);
				for (Map.Entry<String,String> channelEn : channelMap.entrySet()) {
					String counts = channelEn.getValue();
					int count=Integer.parseInt(counts);
					if(count<=0){
						continue;
					}
					RedisUtils.hash_incrBy(RedisUtils.HASH_CHANNEL_THREAD_COUNT,channelId,1);
					String channelMmsId = channelEn.getKey();
					switch (channelId) {
						case "1"://beijing cmcc
							BusinessWorker.getExecutor().execute(new HandlerBeiJingCmcc(link_speed,channelId,channelMmsId,json));
							break;
						case "2"://beijing cmcc2
							BusinessWorker.getExecutor().execute(new HandlerBeiJingCmcc2(link_speed,channelId,channelMmsId,json));
							break;
						default:
							break;
					}
				}

				
			}
			/*i++;
			if(i%1000==0){
				logger.info(BusinessWorker.WorkerInfo());
				i=0;
			}*/
			//System.out.println("----------------本次循环耗时:"+(System.currentTimeMillis() - bt)+"----------------------------");
			try{
				if(RedisUtils.string_exist(RedisUtils.STR_KEY_AI_TASK)){
					if(RedisUtils.string_get(RedisUtils.STR_KEY_AI_TASK).equals("stop")){
						logger.info("..........视频短信渠道发送任务退出..........");
						break;
					}
				}else{
					RedisUtils.string_set(RedisUtils.STR_KEY_AI_TASK,"run");
				}
			}catch(Exception e){
				logger.error("ThreadNoticeChannel error:string_exist:", e);
			}
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
