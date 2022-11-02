package com.jiujia.khd.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.util.StringUtil;
import com.jiujia.common.annotation.Log;
import com.jiujia.common.core.controller.BaseController;
import com.jiujia.common.core.domain.AjaxResult;
import com.jiujia.common.core.page.TableDataInfo;
import com.jiujia.common.enums.BusinessType;
import com.jiujia.common.utils.poi.ExcelUtil;
import com.jiujia.khd.domain.Statistics;
import com.jiujia.khd.service.StatisticsService;
import com.jiujia.khd.utils.DateUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/mmskhd/statistics")
public class StatisticController extends BaseController {

    private String prefix = "mmskhd/statistics";

    @Autowired
    private StatisticsService statisticsService;

    @RequiresPermissions("mmskhd:statistics:list")
    @GetMapping()
    public String statistics()
    {
        return prefix + "/statistics";
    }

    @RequiresPermissions("mmskhd:statistics:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo findStatisticList(Statistics statistics) {
        logger.debug("[BINS][STATISTIC] statistics="+ JSONObject.toJSONString(statistics));
        long time = System.currentTimeMillis();
        startPage();

        if (StringUtil.isEmpty(statistics.getStartTime()) || StringUtil.isEmpty(statistics.getEndTime())) {
            statistics.setStartTime(DateUtil.convertDate3(new Date()));
            statistics.setEndTime(DateUtil.convertDate3(new Date()));
        }

        List<Statistics> statisticsList = new ArrayList<Statistics>();
        try {
            statisticsList = statisticsService.findStatisticsList(statistics);
        } catch (Exception e) {
            logger.error("[BINS][APP_STATISTIC] ", e);
        }
        Integer sendTotalSum=0;
        Integer delivrdSum=0;
        Integer unDelivrdSum=0;
        Integer blackSum=0;
        Integer unknownSum=0;
        Integer loaddelivrdSum=0;

        for(Statistics stat:statisticsList){
            sendTotalSum+=stat.getSendTotal();
            delivrdSum+=stat.getReportDelivrd();
            unDelivrdSum+=stat.getReportUndeliv();
            blackSum+=stat.getReportBlack();
            unknownSum+=stat.getReportUnknown();
            loaddelivrdSum+=stat.getLoadDelivrd();
        }
        Statistics stat=new Statistics();
        stat.setAppName("总计");
        stat.setSendTotal(sendTotalSum);
        stat.setReportDelivrd(delivrdSum);
        stat.setReportUndeliv(unDelivrdSum);
        stat.setReportBlack(blackSum);
        stat.setReportUnknown(unknownSum);
        stat.setLoadDelivrd(loaddelivrdSum);
        statisticsList.add(stat);
        logger.debug("[BINS][STATISTIC] find list size="+statisticsList.size()+",time="+(System.currentTimeMillis()-time));
        return getDataTable(statisticsList);
    }

    /**
     * 导出下行日志的显示查询列表
     */
    @RequiresPermissions("mmskhd:statistics:export")
    @Log(title = "统计导出", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(Statistics statistics)
    {
        logger.debug("[BINS][STATISTIC] statistics="+ JSONObject.toJSONString(statistics));
        if (StringUtil.isEmpty(statistics.getStartTime()) || StringUtil.isEmpty(statistics.getEndTime())) {
            statistics.setStartTime(DateUtil.convertDate3(new Date()));
            statistics.setEndTime(DateUtil.convertDate3(new Date()));
        }
        List<Statistics> statisticsList = new ArrayList<Statistics>();
        try {
            statisticsList = statisticsService.findStatisticsList(statistics);
        } catch (Exception e) {
            logger.error("[BINS][APP_STATISTIC] ", e);
        }
        Integer sendTotalSum=0;
        Integer delivrdSum=0;
        Integer unDelivrdSum=0;
        Integer blackSum=0;
        Integer unknownSum=0;
        Integer loaddelivrdSum=0;

        for(Statistics stat:statisticsList){
            sendTotalSum+=stat.getSendTotal();
            delivrdSum+=stat.getReportDelivrd();
            unDelivrdSum+=stat.getReportUndeliv();
            blackSum+=stat.getReportBlack();
            unknownSum+=stat.getReportUnknown();
            loaddelivrdSum+=stat.getLoadDelivrd();
        }
        Statistics stat=new Statistics();
        stat.setAppName("总计");
        stat.setSendTotal(sendTotalSum);
        stat.setReportDelivrd(delivrdSum);
        stat.setReportUndeliv(unDelivrdSum);
        stat.setReportBlack(blackSum);
        stat.setReportUnknown(unknownSum);
        stat.setLoadDelivrd(loaddelivrdSum);
        statisticsList.add(stat);
        ExcelUtil<Statistics> util = new ExcelUtil<Statistics>(Statistics.class);
        return util.exportExcel(statisticsList, "统计导出");
    }

}
