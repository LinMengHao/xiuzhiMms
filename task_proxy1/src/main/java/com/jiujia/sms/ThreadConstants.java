package com.jiujia.sms;

import com.alibaba.fastjson.JSONObject;

import com.jiujia.conf.GlobalBlackFilterConfig;
import com.jiujia.redis.RedisUtils;
import com.jiujia.util.HttpInterface;
import com.jiujia.util.MD5Utils;
import com.jiujia.util.UUIDUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.*;

public class ThreadConstants {

	public static Logger logger = LoggerFactory.getLogger("ThreadConstants");
	public static void main(String[] args) {
		String allChannelNos="";
		
		System.out.println("-------------");
		System.out.println(allChannelNos.replaceAll("egg",""));
		System.out.println("-------------");
		String callId = UUIDUtil.getUUID32();
		JSONObject paramMap=new JSONObject();
		String sign = MD5Utils.MD5Encode(GlobalBlackFilterConfig.DY_DX_ACCESSKEY + callId + GlobalBlackFilterConfig.DY_DX_SECRETKEY);
		paramMap.put("ak",GlobalBlackFilterConfig.DY_DX_ACCESSKEY);
		paramMap.put("caller","123");
		paramMap.put("callee","18756232770");
		paramMap.put("callId", callId);
		paramMap.put("sign",sign);
//		paramMap.put("version","1");
		Hashtable<String, String> formHeaders=new Hashtable<>();
		formHeaders.put("Content-Type","application/json");
		String str = HttpInterface.httpClientPost(GlobalBlackFilterConfig.DY_DX_SERVICEURL, paramMap.toJSONString(), 30000, "UTF-8",formHeaders);
		System.out.println(str);
	}
	public static List<String> getRouteList(String routeKey) {
		List<Double> scores = new ArrayList<Double>();
		List<String> members = new ArrayList<String>();
		RedisUtils.zset_revrangeWithScore(routeKey, 0, -1, scores, members);
		List<String> routes = new ArrayList<String>();
		int length=scores.size();
		if(length==0){
			return routes;
		}
		double prfDou=scores.get(0);
		String member="|"+members.get(0);
		for (int i=1;i<length;i++) {
			if(prfDou==scores.get(i).doubleValue()){
				member+="|"+members.get(i);
			}else{
				routes.add(member.substring(1));
				member="|"+members.get(i);
				prfDou=scores.get(i);
			}
		}
		routes.add(member.substring(1));
		return routes;
	}
	/**
	 * 新路由预处理
	 * @param companyId 公司id
	 * @param appId 用户id
	 * @param provider //运营商 1-电信2-联通3-移动4-国际0-所有
	 * @param province 省份 
	 * @param route 路由组
	 * @return
	 */
	public static JSONObject disRoutePretreat(String companyId, String appId, int provider,String signName, String province,String today,String route,Map<String, String> relatedMap,String param){
		JSONObject jsonPre = null;
		double dispratePre=0;
		long countPre = 0L;
		int routeIdPre =0;
		String channelIdPre="";
		String routeCountKey = RedisUtils.HASH_ROUTE_DISPENSE+companyId+":"+appId+":"+today;
		String[] routArr = route.split("\\|");
		for (String routeOne : routArr) {
			JSONObject routejson = JSONObject.parseObject(routeOne);

			//路由json串：route_id,to_cmcc，to_unicom，to_telecom，to_international，sign_name，province，disprate，channel_id，channel_limit，channel_caller
			int routeId = routejson.getInteger("route_id");//路由id
			String to_cmcc = routejson.getString("to_cmcc");//移动，yes，no
			String to_unicom = routejson.getString("to_unicom");//联通，yes，no
			String to_telecom = routejson.getString("to_telecom");//电信，yes，no
			String to_international = routejson.getString("to_international");//国际，yes，no
			String sign_name = routejson.getString("sign_name");//签名
			String routeProvince = routejson.getString("province");//省份，多个以半角逗号分隔
			double disprate = routejson.getDouble("disprate");//分发比例
			String channel_id = routejson.getString("channel_id");//渠道id
			int channel_limit = routejson.getInteger("channel_limit");//渠道限制发送数
			String channel_caller = routejson.getString("channel_caller");//渠道主叫

			if(disprate==0){
				continue;
			}
			if(StringUtils.isNotBlank(sign_name)&&sign_name.indexOf(signName)==-1){
				continue;
			}
			//运营商 1-电信2-联通3-移动4-国际0-所有
			if(provider==1&&to_telecom.equals("no")){
				continue;
			}if(provider==2&&to_unicom.equals("no")){
				continue;
			}if(provider==3&&to_cmcc.equals("no")){
				continue;
			}if(provider==4&&to_international.equals("no")){
				continue;
			}
			if(StringUtils.isNotBlank(routeProvince)){
				if(StringUtils.isBlank(province)){
					continue;
				}
				if(routeProvince.indexOf(province)==-1){
					continue;
				}
			}
			String countStr = RedisUtils.hash_get(routeCountKey,routeId+"_"+channel_id);
			long count = Long.parseLong(StringUtils.isNotBlank(countStr)?countStr:"0");
			if(count>=channel_limit){
				continue;
			}

			//模板映射关系
			boolean flag=false;
			String channelModelId="";
			String channelParam="";
			String channelModelKey = RedisUtils.HASH_CHANNEL_MODEL_LIMIT+today+":"+channel_id;
			for(Map.Entry<String, String> entry:relatedMap.entrySet()){
				String[] keyArr = entry.getKey().split("_");
				if(!keyArr[0].equals(channel_id)){
					continue;
				}
				JSONObject relateJson = JSONObject.parseObject(entry.getValue());
				long limitCount = relateJson.getLong("limit_count");//通道模板限制数
				String param_ext = relateJson.getString("param_ext");

				String total = RedisUtils.hash_get(channelModelKey,keyArr[1]);
				long modelTotal = StringUtils.isNotBlank(total)?Long.parseLong(total):0L;
				if(modelTotal>=limitCount){
					flag = true;
					continue;
				}
				flag=false;
				channelModelId = keyArr[1];
				if(StringUtils.isNotBlank(param_ext)){
					String[] paramExtArr=param_ext.split("&");
					for (String paramExt:paramExtArr){
						String[] extArr = paramExt.split("=");
						channelParam=param.replace(extArr[0]+"=",extArr[1]+"=");
					}
				}
			}
			if(flag){
				continue;
			}

			if(dispratePre==0){
				routejson.put("channel_model_id",channelModelId);
				routejson.put("channel_param",channelParam);
				dispratePre=disprate;
				countPre = count;
				jsonPre = routejson;
				routeIdPre = routeId;
				channelIdPre = channel_id;
				continue;
			}
			if(count/disprate<countPre/dispratePre){
				routejson.put("channel_model_id",channelModelId);
				routejson.put("channel_param",channelParam);
				dispratePre=disprate;
				countPre = count;
				jsonPre = routejson;
				routeIdPre = routeId;
				channelIdPre = channel_id;
				continue;
			}
			if(count/disprate==countPre/dispratePre&&disprate>dispratePre){
				routejson.put("channel_model_id",channelModelId);
				routejson.put("channel_param",channelParam);
				dispratePre=disprate;
				countPre = count;
				jsonPre = routejson;
				routeIdPre = routeId;
				channelIdPre = channel_id;
				continue;
			}
		}
		if(routeIdPre!=0){
			RedisUtils.hash_incrBy(routeCountKey,routeIdPre+"_"+channelIdPre,1);
		}
		return jsonPre;
	}
	/**
	 * 补呼路由预处理
	 * @param seqNo 商户编号
	 * @param itemNum 项目编号
	 * @param allChannelNos 呼叫过的渠道编号，半角逗号分隔
	 * @param channelNo 上次呼叫失败的渠道编号
	 * @param sip_code 上次呼叫失败的sip失败码
	 * @param provider 运营商 1-电信2-联通3-移动4-国际0-所有
	 * @param province 省份 
	 * @param route 路由组
	 * @return
	 */
	public static JSONObject disRecallRoutePretreat(String seqNo,String itemNum,String allChannelNos,String channelNo,String sip_code,int provider,String province,String route){
		JSONObject jsonPre = null;
		long dispratePre=0L;
		long countPre = 0L;
		int routeIdPre =0;
		String channelNoPre="";
		String routeCountKey = RedisUtils.HASH_ROUTE_RECALL_DISPENSE+seqNo+":"+itemNum;
		String[] routArr = route.split("\\|");
		for (String routeOne : routArr) {
			JSONObject routejson = JSONObject.parseObject(routeOne);
			//补呼路由json串：route_id,failure_code，to_cmcc，to_unicom，to_telecom，to_international，sign_name，province，disprate，channel_id，channel_limit，channel_caller

			String channelNofst = routejson.getString("channelNo");//原始渠道编号
			String channelRecall = routejson.getString("channelRecall");//补呼渠道编号
			String failureCode = routejson.getString("failureCode");//补呼失败码，多个以半角逗号分开
			long disprate = routejson.getLong("disprate");//分发比例
			int routeId = routejson.getInteger("routeId");//路由id
			int routeProvider = routejson.getInteger("provider");//运营商 1-电信2-联通3-移动4-国际0-所有
			String routeProvince = routejson.getString("province");//省份，多个以半角逗号分隔
			if(disprate==0){
				continue;
			}
			if(routeProvider!=0&&routeProvider!=provider){
				continue;
			}
			String caller = routejson.getString("caller");//渠道主叫号码
			if(allChannelNos.indexOf("egg"+caller+channelRecall)!=-1){
				continue;//该通道发送过
			}
			if(StringUtils.isNotBlank(channelNofst)&&!channelNofst.equals(channelNo)){
				continue;//发送通道不匹配
			}
			if(StringUtils.isNotBlank(failureCode)&&failureCode.indexOf(sip_code)==-1){
				continue;//失败码规则不匹配
			}
			if(StringUtils.isNotBlank(routeProvince)){
				if(StringUtils.isBlank(province)){
					continue;
				}
				if(routeProvince.indexOf(province)==-1){
					continue;
				}
				routejson.put("prefixCalled", province.substring(1));
			}
			String countStr = RedisUtils.hash_get(routeCountKey,routeId+"_"+channelRecall);
			long count = Long.parseLong(StringUtils.isNotBlank(countStr)?countStr:"0");
			if(dispratePre==0){
				dispratePre=disprate;
				countPre = count;
				jsonPre = routejson;
				routeIdPre = routeId;
				channelNoPre = channelRecall;
				continue;
			}
			if(count/disprate<countPre/dispratePre){
				dispratePre=disprate;
				countPre = count;
				jsonPre = routejson;
				routeIdPre = routeId;
				channelNoPre = channelRecall;
				continue;
			}
			if(count/disprate==countPre/dispratePre&&disprate>dispratePre){
				dispratePre=disprate;
				countPre = count;
				jsonPre = routejson;
				routeIdPre = routeId;
				channelNoPre = channelRecall;
				continue;
			}
		}
		if(routeIdPre!=0){
			RedisUtils.hash_incrBy(routeCountKey,routeIdPre+"_"+channelNoPre,1);
		}
		return jsonPre;
	}
	
	/**
	 * 路由选择
	 * @param seqNo 商户编号
	 * @param itemNum 项目编号
	 * @param callCount 发送数量
	 * @return
	 */
	public static Map<String, JSONObject> getBestRoute(String seqNo,String itemNum,int callCount){
		//路由配置--分发比例
		String routeListKey = RedisUtils.HASH_CUR_ROUTE_ALL+seqNo+":"+itemNum;
		Map<String, String> mapResult = RedisUtils.hash_getFields(routeListKey);
		if(mapResult==null||mapResult.isEmpty()){
			return new HashMap<String, JSONObject>();
		}
		//路由配置--分发数量
		String routeCountKey = RedisUtils.HASH_ROUTE_DISPENSE+seqNo+":"+itemNum;
		Map<String, String> mapCount = RedisUtils.hash_getFields(routeCountKey);
		
		for (Map.Entry<String,String> men : mapResult.entrySet()) {
			String keyNo = men.getKey();
			String valueJson = men.getValue();
			JSONObject countJson = JSONObject.parseObject(valueJson);
			if(mapCount!=null&&mapCount.containsKey(keyNo)){
				int hisCount = Integer.parseInt(mapCount.get(keyNo).toString()) ;
				countJson.put("hisCount", hisCount);
			}else{
				countJson.put("hisCount", 0);
			}
			countJson.put("curCount", 0);
			mapResult.put(keyNo, countJson.toString());
		}
		for(int i=0;i<callCount;i++){
			String bestKey="";
			String bestChannelNo="";
			String bestVosGw="";
			double bestDispRate=0;
			int bestHisCount=0;
			int bestCurCount=0;
			
			for (Map.Entry<String,String> men : mapResult.entrySet()) {
				String keyNo = men.getKey();
				String valueJson = men.getValue();
				JSONObject countJson = JSONObject.parseObject(valueJson);
				double dispRate=countJson.getDouble("disprate");
				if(dispRate==0){
					continue;
				}
				String vosGw = countJson.getString("vosGw");
				String channelNo = countJson.getString("channelNo");
				int hisCount=countJson.getInteger("hisCount");
				int curCount=countJson.getInteger("curCount");
				if(bestDispRate==0){
					bestKey=keyNo;
					bestVosGw=vosGw;
					bestChannelNo=channelNo;
					bestDispRate=dispRate;
					bestHisCount=hisCount;
					bestCurCount=curCount;
				}else{
					int bestCount=bestHisCount+bestCurCount;
					int totalCount=hisCount+curCount;
					if((bestCount/bestDispRate>totalCount/dispRate)||(bestCount/bestDispRate==totalCount/dispRate&&dispRate>bestDispRate)){
						bestKey=keyNo;
						bestVosGw=vosGw;
						bestChannelNo=channelNo;
						bestDispRate=dispRate;
						bestHisCount=hisCount;
						bestCurCount=curCount;
					}
				}
			}
			if(bestDispRate>0){
				JSONObject countJson = new JSONObject();
				countJson.put("disprate",bestDispRate);
				countJson.put("vosGw", bestVosGw);
				countJson.put("channelNo", bestChannelNo);
				countJson.put("hisCount", bestHisCount);
				countJson.put("curCount", bestCurCount+1);
				mapResult.put(bestKey, countJson.toString());
			}
		}
		Map<String, JSONObject> map = new HashMap<String, JSONObject>();
		for (Map.Entry<String,String> men : mapResult.entrySet()) {
			String keyNo = men.getKey();
			String valueJson = men.getValue();
			JSONObject countJson = JSONObject.parseObject(valueJson);
			int curCount=countJson.getInteger("curCount");
			if(curCount==0){
				continue;
			}
			RedisUtils.hash_incrBy(routeCountKey,keyNo,curCount);
			map.put(keyNo, countJson);
		}
		
		return map;
	}

	public static JSONObject mobileCheck(String mobile,String companyId,String appId){
		JSONObject json = new JSONObject();
		//白名单校验
		if(mobileWhite(mobile,companyId,appId)){
			json.put("check",1);//1-白名单 2-受限 3-未受限
			return json;
		}
		//黑名单校验
		String blackUrl = "http://bj.mxtx.cn:5678/Api/blackcheckbyphonefordate/121/000f4f4b3250b1b73e06ee09cc5723e6/"+mobile;
		Hashtable<String, String> formHeaders = new Hashtable<String, String>();
		formHeaders.put("Content-Type", "application/xml");
		String str = HttpInterface.httpClientGet(blackUrl, 30000, "UTF-8", formHeaders);
		if(str.indexOf("http请求异常")==0){
			str = HttpInterface.httpClientGet(blackUrl, 30000, "UTF-8", formHeaders);
		}
		int blackStatus=1;
		if(str.indexOf("http请求异常")==0){
			logger.info("黑名单三方校验提交结果异常：{}：{}",mobile,str);
		}else{
			//{"code":0,"msg":"Success","data":{"unique_id":"1661155223067838744","status":32,"date":"20191225"}}
			JSONObject black = JSONObject.parseObject(str);
			int code = black.getInteger("code");
			if(code==0){
				JSONObject blackData = black.getJSONObject("data");
				//1为正常号码，31、32、33分别为高、中、低危号码，30为私有库号码，20为白名单号码
				blackStatus=blackData.getInteger("status");
			}
		}
		json.put("check",2);//1-白名单 2-受限 3-未受限
		//全局控制策略
		if(GlobalBlackFilterConfig.JS_BLACK_LEVEL.size()==0){
			//默认使用最高级别
			if(blackStatus==30||blackStatus==31||blackStatus==32){
				//黑名单
				json.put("status","BLACK");//状态
				json.put("statusName","黑名单");//状态描述
				return json;
			}
		}else {
			for (int i = 0; i < GlobalBlackFilterConfig.JS_BLACK_LEVEL.size(); i++) {
				if(blackStatus==GlobalBlackFilterConfig.JS_BLACK_LEVEL.get(i)){
					logger.info("过滤码号：{}",GlobalBlackFilterConfig.JS_BLACK_LEVEL.get(i));
					//黑名单
					json.put("status","BLACK");//状态
					json.put("statusName","黑名单");//状态描述
					return json;
				}
			}
		}

//		//if(blackStatus!=1&&blackStatus!=20){
//		//if(blackStatus==30||blackStatus==31||blackStatus==32){
//		//if(blackStatus==31){
//		if(blackStatus==31||blackStatus==32){
//			//黑名单
//			json.put("status","BLACK");//状态
//			json.put("statusName","黑名单");//状态描述
//			return json;
//		}
		if(blackStatus==20){//白名单
			json.put("check",1);//1-白名单 2-受限 3-未受限
			return json;
		}
		//频次校验
		if(bandLimit(mobile,companyId,appId)){
			//频次受限
			json.put("status","LIMIT");//状态
			json.put("statusName","频次受限");//状态描述
			return json;
		}
		//地区校验
		if(areaLimit(mobile,companyId,appId)){
			//频次受限
			json.put("status","LIMIT");//状态
			json.put("statusName","省市受限");//状态描述
			return json;
		}
		json.put("check",3);//1-白名单 2-受限 3-未受限
		return json;
	}

	/**浙江棱镜信息科技有限公司黑名单质检
	 * @param mobile 码号
	 * @param companyId 公司id
	 * @param appId
	 * @param username
	 * @param password
	 * @param checktype 1 表示黑名单（目前只能取1）
	 * @param url
	 * @return
	 */
	public static JSONObject mobileCheckNj(String mobile,String companyId,String appId,String username,String password,String checktype,String url){
		JSONObject json = new JSONObject();
		//白名单校验
		if(mobileWhite(mobile,companyId,appId)){
			json.put("check",1);//1-白名单 2-受限 3-未受限
			return json;
		}
		//黑名单校验
		//url拼接
		StringBuilder blackUrl = new StringBuilder(url);
		blackUrl.append("?mobile=");
		blackUrl.append(mobile);
		blackUrl.append("&username=");
		blackUrl.append(username);
		blackUrl.append("&password=");
		blackUrl.append(password);
		blackUrl.append("&timestamp=");
		long timeStamp  = new Date().getTime();
		blackUrl.append(timeStamp);
		blackUrl.append("&sign=");
		String sign = MD5Utils.MD5Encode(checktype + mobile + GlobalBlackFilterConfig.NJ_SECRETKEY + timeStamp).toUpperCase();
		blackUrl.append(sign);
		blackUrl.append("&checktype=");
		blackUrl.append(checktype);
		//防止对方解析的是body不是url路由明文
		Map<String,String> paramMap=new HashMap<>();
//		paramMap.put("username",username);
//		paramMap.put("password",password);
//		paramMap.put("mobile",mobile);
//		paramMap.put("timestamp",String.valueOf(timeStamp));
//		paramMap.put("checktype",checktype);
//		paramMap.put("sign",sign);
		String str = HttpInterface.httpClientPostByBody(blackUrl.toString(), paramMap, 30000, "UTF-8");
		//重试
		if(str.indexOf("http请求异常")==0){
			str = HttpInterface.httpClientPostByBody(blackUrl.toString(), paramMap, 30000, "UTF-8");
		}
		int blackStatus=1;
		if(str.indexOf("http请求异常")==0){
			logger.info("黑名单三方校验提交结果异常：{}：{}",mobile,str);
		}else{
			//{"code":0,"msg":"Success","data":{"unique_id":"1661155223067838744","status":32,"date":"20191225"}}
			JSONObject black = JSONObject.parseObject(str);
			int code = black.getInteger("code");
			if(code==110){
				logger.info("黑名单三方校验接口异常：{}：{}",mobile,str);
			}else if(code==-1){
				logger.info("黑名单三方校验接口参数异常：{}：{}",mobile,str);
			}else if(code==-2){
				logger.info("黑名单三方校验接口签名不通过：{}：{}",mobile,str);
			}else if(code==-3){
				logger.info("黑名单三方校验接口查询号码超为最大单次数（单次最大为1000）：{}：{}",mobile,str);
			}
			if(code==1||code==5){
				//1为正常号码，5为危险
				blackStatus=code;
			}
		}
		json.put("check",2);//1-白名单 2-受限 3-未受限
		if(blackStatus==5){
			//黑名单
			json.put("status","BLACK");//状态
			json.put("statusName","黑名单");//状态描述
			return json;
		}
		//频次校验
		if(bandLimit(mobile,companyId,appId)){
			//频次受限
			json.put("status","LIMIT");//状态
			json.put("statusName","频次受限");//状态描述
			return json;
		}
		//地区校验
		if(areaLimit(mobile,companyId,appId)){
			//频次受限
			json.put("status","LIMIT");//状态
			json.put("statusName","省市受限");//状态描述
			return json;
		}
		json.put("check",3);//1-白名单 2-受限 3-未受限
		return json;
	}
	//东云
	public static JSONObject mobileCheckDy(String mobile,String companyId,String appId,String accessKey,String secretKey,String url){
		JSONObject json = new JSONObject();
		//白名单校验
		if(mobileWhite(mobile,companyId,appId)){
			json.put("check",1);//1-白名单 2-受限 3-未受限
			return json;
		}
		//黑名单校验
		//呼叫唯一标识
		String callId = UUIDUtil.getUUID32();
		JSONObject paramMap=new JSONObject();
		String sign = MD5Utils.MD5Encode(accessKey + callId + secretKey);
		paramMap.put("ak",accessKey);
		paramMap.put("caller","123");
		paramMap.put("callee",mobile);
		paramMap.put("callId", callId);
		paramMap.put("sign",sign);
		Hashtable<String, String> formHeaders=new Hashtable<>();
		formHeaders.put("Content-Type","application/json");
		String str = HttpInterface.httpClientPost(GlobalBlackFilterConfig.DY_DX_SERVICEURL, paramMap.toJSONString(), 30000, "UTF-8",formHeaders);
		//重试
		if(str.indexOf("http请求异常")==0){
			str = HttpInterface.httpClientPost(GlobalBlackFilterConfig.DY_DX_SERVICEURL, paramMap.toJSONString(), 30000, "UTF-8",formHeaders);
		}
		int blackStatus=1;
		if(str.indexOf("http请求异常")==0){
			logger.info("黑名单三方校验提交结果异常：{}：{}",mobile,str);
		}else{
			/*
				{
				"code": 1,
				"msg": "success",
				"callId": 321,
				"forbid": 1,
				"transactionId": "123456"
				}
			 */
			JSONObject black = JSONObject.parseObject(str);
			if(!callId.equals(black.getString("callId"))){
				logger.info("黑名单三方校验接口响应数据与查询数据无上下文关系，存在交叉响应，数据不准确：{}：{}",mobile,str);
			}
			int code = black.getInteger("code");
			if(code==4001){
				logger.info("黑名单三方校验接口参数异常：{}：{}",mobile,str);
			}else if(code==4000){
				logger.info("黑名单三方校验接口accessKey错误：{}：{}",mobile,str);
			}else if(code==4002){
				logger.info("黑名单三方校验接口账号错误：{}：{}",mobile,str);
			}else if(code==4010){
				logger.info("黑名单三方校验接口签名错误：{}：{}",mobile,str);
			}else if(code==4003){
				logger.info("IP地址禁止访问第三方黑名单接口：{}：{}",mobile,str);
			}else if (code==4011){
				logger.info("黑名单三方校验接口超过阈值：{}：{}",mobile,str);
			}else if(code==4004){
				logger.info("余额不足,无法访问第三方黑名单接口：{}：{}",mobile,str);
			}
			if(code==1){
				blackStatus=black.getInteger("forbid");
			}
		}
		json.put("check",2);//1-白名单 2-受限 3-未受限
		if(blackStatus>0){
			//黑名单
			json.put("status","BLACK");//状态
			json.put("statusName","黑名单");//状态描述
			return json;
		}
		//频次校验
		if(bandLimit(mobile,companyId,appId)){
			//频次受限
			json.put("status","LIMIT");//状态
			json.put("statusName","频次受限");//状态描述
			return json;
		}
		//地区校验
		if(areaLimit(mobile,companyId,appId)){
			//频次受限
			json.put("status","LIMIT");//状态
			json.put("statusName","省市受限");//状态描述
			return json;
		}
		json.put("check",3);//1-白名单 2-受限 3-未受限
		return json;
	}
	//地区控制
	private static boolean mobileWhite(String mobile,String companyId,String appId){
		//全局白名单
		String key = RedisUtils.HASH_MOBILE_WHITE+"0:0";
		if(RedisUtils.hash_exists(key,mobile)){
			return true;
		}
		//客户白名单
		key = RedisUtils.HASH_MOBILE_WHITE+companyId+":0";
		if(RedisUtils.hash_exists(key,mobile)){
			return true;
		}
		//账号白名单
		key = RedisUtils.HASH_MOBILE_WHITE+companyId+":"+appId;
		if(RedisUtils.hash_exists(key,mobile)){
			return true;
		}
		return false;
	}
	//频次控制
	private static boolean bandLimit(String mobile,String companyId,String appId){
		int total=1;//3天1条
		Calendar cal = Calendar.getInstance();
		String today=new SimpleDateFormat("yyyyMMdd").format(cal.getTime());

		String limitKey = RedisUtils.HASH_BAND_LIMIT+today+":"+companyId+":"+appId;
		String todayStr = RedisUtils.hash_get(limitKey,mobile);
		if(StringUtils.isNotBlank(todayStr)){
			return true;
		}
		cal.add(Calendar.DATE, -1);//昨天
		String yesterday=new SimpleDateFormat("yyyyMMdd").format(cal.getTime());
		limitKey = RedisUtils.HASH_BAND_LIMIT+yesterday+":"+companyId+":"+appId;
		String yesterdayStr = RedisUtils.hash_get(limitKey,mobile);
		if(StringUtils.isNotBlank(yesterdayStr)){
			return true;
		}
		cal.add(Calendar.DATE, -2);//前天
		String before=new SimpleDateFormat("yyyyMMdd").format(cal.getTime());
		limitKey = RedisUtils.HASH_BAND_LIMIT+before+":"+companyId+":"+appId;
		String beforeStr = RedisUtils.hash_get(limitKey,mobile);
		if(StringUtils.isNotBlank(beforeStr)){
			return true;
		}
		return false;
	}
	//地区控制
	private static boolean areaLimit(String mobile,String companyId,String appId){
		//屏蔽北京
		String segment = RedisUtils.hash_get(RedisUtils.HASH_MOBILE_SEGMENT,mobile.substring(0,7));
		if(StringUtils.isBlank(segment)){
			return false;
		}
		if(segment.indexOf("010_")!=-1){
			return true;
		}
		return false;
	}

}
