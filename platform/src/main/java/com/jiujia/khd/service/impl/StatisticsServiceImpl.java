package com.jiujia.khd.service.impl;

import com.jiujia.common.core.domain.entity.SysUser;
import com.jiujia.khd.domain.Statistics;
import com.jiujia.khd.mapper.StatisticsMapper;
import com.jiujia.khd.service.StatisticsService;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("statisticsService")
public class StatisticsServiceImpl implements StatisticsService {

    @Autowired
    private StatisticsMapper statisticsMapper;

    @Override
    public List<Statistics> findStatisticsList(Statistics statistics) {
        return statisticsMapper.findStatisticsList(statistics);
    }
    @Override
    public List<Statistics> findStatisticsListM(String date) {
        SysUser user = (SysUser) SecurityUtils.getSubject().getPrincipal();
        Long companyId=user.getCompanyId();
        return statisticsMapper.findStatisticsListM(companyId,date);
    }

}
