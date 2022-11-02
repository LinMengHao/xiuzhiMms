package com.jiujia.service.impl;

import com.jiujia.mapper.ICommonMapper;
import com.jiujia.service.ICommonService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class CommonServiceImpl implements ICommonService {
	
    @Resource
    private ICommonMapper voiceMapper ;
    
	@Override
	public void batchUpdate(List<String> sqlList){
		/*Map<String,Object> param = new HashMap<String,Object>();
		param.put("list", sqlList);*/
		voiceMapper.batchUpdate(sqlList);
	}
	
	@Override
	public List<Map<String, Object>> appInfoList(){
		return voiceMapper.appInfoList();
	}
	@Override
	public List<Map<String, Object>> modelInfoList(){
		return voiceMapper.modelInfoList();
	}
	@Override
	public List<Map<String, Object>> channelInfoList(){
		return voiceMapper.channelInfoList();
	}
	@Override
	public List<Map<String, Object>> routeBaseList(){
		return voiceMapper.routeBaseList();
	}
	@Override
	public List<Map<String, Object>> tableInfoList(String tableName){
		return voiceMapper.tableInfoList(tableName);
	}
	@Override
	public void createNewTable(String createSql){
		voiceMapper.createNewTable(createSql);
	}
	@Override
	public List<Map<String, Object>> commonInfoList(String selectSql){
		return voiceMapper.commonInfoList(selectSql);
	}
	@Override
	public List<Map<String, Object>> segmentList(){
		return voiceMapper.segmentList();
	}
}
