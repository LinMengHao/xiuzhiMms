package com.jiujia.service;

import java.util.List;
import java.util.Map;

public interface ICommonService {

	/**
	 * 批量执行sql语句
	 * @param sqlList
	 */
	void batchUpdate(List<String> sqlList);
	
	/**
	 * 批量查询用户信息列表
	 */
	List<Map<String, Object>> appInfoList();
	/**
	 * 批量查询模板信息列表
	 */
	List<Map<String, Object>> modelInfoList();
	/**
	 * 批量查询通道信息列表
	 */
	List<Map<String, Object>> channelInfoList();
	/**
	 * 批量查询路由信息列表
	 */
	List<Map<String, Object>> routeBaseList();
	/**
	 * 查询数据表信息列表
	 */
	List<Map<String, Object>> tableInfoList(String tableName);

	/**
	 * 创建数据表
	 * @param createSql
	 */
	void createNewTable(String createSql);

	/**
	 * 查询信息列表
	 */
	List<Map<String, Object>> commonInfoList(String selectSql);

	/**
	 * 批量查询号段信息表
	 */
	List<Map<String, Object>> segmentList();

}
