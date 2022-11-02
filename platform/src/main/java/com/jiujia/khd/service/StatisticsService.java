package com.jiujia.khd.service;

import com.jiujia.khd.domain.Statistics;

import java.util.List;

public interface StatisticsService {

    public List<Statistics> findStatisticsList(Statistics statistics);
    public List<Statistics> findStatisticsListM(String date);

}
