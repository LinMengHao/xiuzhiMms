package com.jiujia.khd.controller;

import com.alibaba.fastjson.JSONObject;
import com.jiujia.common.annotation.Log;
import com.jiujia.common.core.controller.BaseController;
import com.jiujia.common.core.domain.AjaxResult;
import com.jiujia.common.core.page.TableDataInfo;
import com.jiujia.common.enums.BusinessType;
import com.jiujia.common.utils.poi.ExcelUtil;
import com.jiujia.khd.domain.MmsSender;
import com.jiujia.khd.service.IMmsSenderService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 下行日志的显示查询Controller测试
 * 
 * @author lixl
 * @date 2022-01-24
 */
@Controller
@RequestMapping("/mmskhd/mt")
public class MtController extends BaseController
{
    private String prefix = "mmskhd/mt";

    @Autowired
    private IMmsSenderService mmsSenderService;

    @RequiresPermissions("mmskhd:mt:list")
    @GetMapping()
    public String mt()
    {
        return prefix + "/mt";
    }

    /**
     * 查询下行日志的显示查询列表
     */
    @RequiresPermissions("mmskhd:mt:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(MmsSender mmsSender)
    {
        startPage();
        List<MmsSender> list = mmsSenderService.selectMmsSenderList(mmsSender);
        logger.info("[mtres][RECHARGE] data="+ JSONObject.toJSONString(list));
        return getDataTable(list);
    }
    /**
     * 导出下行日志的显示查询列表
     */
    @RequiresPermissions("mmskhd:mt:export")
    @Log(title = "下行日志的显示查询", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(MmsSender mmsSender)
    {
        List<MmsSender> list = mmsSenderService.selectMmsSenderList(mmsSender);
        ExcelUtil<MmsSender> util = new ExcelUtil<MmsSender>(MmsSender.class);
        return util.exportExcel(list, "下行日志的显示查询数据");
    }
}
