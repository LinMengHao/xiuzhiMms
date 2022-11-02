package com.jiujia.mapper;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface ICommonMapper {

	/**
	 * 批量
	 * @param sqlList
	 */
	int batchUpdate(List<String> sqlList);

	List<Map<String, Object>> appInfoList();
	List<Map<String, Object>> modelInfoList();
	List<Map<String, Object>> channelInfoList();
	List<Map<String, Object>> routeBaseList();
	List<Map<String, Object>> tableInfoList(@Param("tableName")String tableName);
	int createNewTable(@Param("createSql")String createSql);
	List<Map<String, Object>> commonInfoList(@Param("selectSql")String selectSql);
	List<Map<String, Object>> segmentList();
}
