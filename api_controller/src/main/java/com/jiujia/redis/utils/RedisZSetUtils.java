package com.jiujia.redis.utils;

import com.jiujia.redis.client.RedisZSetClient;

/**
 * Redis 字符串缓存
 * @author admin
 * @since 2017/10/10.
 */
public class RedisZSetUtils {
	
	/**
	 * 单条add 
	 * @param key 键
	 * @param score 排序
	 * @param member 成员
	 * @return
	 */
    public static void set(String key,double score, String member) {
    	RedisZSetClient.set(key,score, member);
    }
    

}
