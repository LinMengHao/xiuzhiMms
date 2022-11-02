package com.jiujia.controller;

import com.jiujia.redis.RedisUtils;
import com.jiujia.service.ICommonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
@EnableScheduling
public class SchedulerController {
    public static Logger logger = LoggerFactory.getLogger("SchedulerController");
    @Resource
    private ICommonService commonService;

    //每日凌晨1点执行
    @Scheduled(cron = "0 0 1 * * ? ")
    public void createTables() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 1);//增加一天
        //cal.add(Calendar.MONTH, n);//增加一个月
        String destTableName="mms_sender_"+new SimpleDateFormat("yyyyMMdd").format(cal.getTime());
        String tableName = "mms_sender_demo";
        String createSql = null;
        try {
            List<Map<String, Object>> result = commonService.tableInfoList(tableName);
            if (result != null && result.size() != 0) {
                Map map = (Map) result.get(0);
                createSql = (String) map.get("Create Table");
            }
            if (createSql != null) {
                createSql = createSql.replaceFirst(tableName, destTableName);
                commonService.createNewTable(createSql);
            }
            logger.info("创建数据表成功【{}】建表语句【{}】",destTableName,createSql);
        } catch (Exception ex) {
            logger.info("创建数据表失败【{}】建表语句【{}】异常信息:{}",destTableName,createSql,ex.getMessage());
            ex.printStackTrace();
        }
    }

    //每间隔一分钟执行
    @Scheduled(initialDelay=1000, fixedDelay=60000)
    public void updateAppBalance() {
        try {
            String companyId="consume";
            List<String> sqlList = new ArrayList<String>();
            //客户消费数减一
            Map<String, String> map = RedisUtils.hash_getFields(RedisUtils.HASH_ACC_SEND);
            for (Map.Entry<String,String> en: map.entrySet()) {
                String appName=en.getKey();
                String value=en.getValue();
                int total = Integer.parseInt(value);
                if(total==0){
                    continue;
                }
                String sql="";
                if(total<0){
                    sql = String.format("UPDATE t_application SET limit_count=limit_count+%s WHERE app_name='%s';",(0-total),appName);
                }else{
                    sql = String.format("UPDATE t_application SET limit_count=limit_count-%s WHERE app_name='%s';",total,appName);
                }
                //客户消费数核减
                RedisUtils.hash_incrBy(RedisUtils.HASH_ACC_SEND, appName,0-total);
                sqlList.add(sql);
            }
            if(sqlList.size()>0){
                RedisUtils.fifo_push(RedisUtils.FIFO_SQL_LIST+companyId,sqlList);
                RedisUtils.hash_incrBy(RedisUtils.HASH_SQL_COUNT, companyId+"", sqlList.size());
                sqlList.clear();
            }
            //入库消费记录
            //logger.info("更新余额表成功");
        } catch (Exception ex) {
            logger.info("更新余额表成功异常信息:{}",ex.getMessage());
            ex.printStackTrace();
        }
    }
}
