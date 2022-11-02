package com.jiujia.operator.controller;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.jiujia.common.annotation.Log;
import com.jiujia.common.core.controller.BaseController;
import com.jiujia.common.core.domain.AjaxResult;
import com.jiujia.common.core.page.TableDataInfo;
import com.jiujia.common.enums.BusinessType;
import com.jiujia.common.utils.poi.ExcelUtil;
import com.jiujia.khd.domain.TApplication;
import com.jiujia.khd.domain.TModel;
import com.jiujia.khd.service.ITApplicationService;
import com.jiujia.operator.domain.Channel;
import com.jiujia.operator.domain.RouteBase;
import com.jiujia.operator.service.IChannelService;
import com.jiujia.operator.service.IRouteBaseService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 路由信息e_route_baseController
 * @author ruoyi
 * @date 2022-09-18
 */
@Controller
@RequestMapping("/operator/routeBase")
public class RouteBaseController extends BaseController
{
    private String prefix = "operator/routeBase";

    @Autowired
    private IRouteBaseService routeBaseService;
    @Autowired
    private ITApplicationService tApplicationService;
    @Autowired
    private IChannelService channelService;

    @RequiresPermissions("operator:routeBase:view")
    @GetMapping()
    public String base()
    {
        return prefix + "/base";
    }

    /**
     * 查询路由信息e_route_base列表
     */
    @RequiresPermissions("operator:routeBase:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(RouteBase routeBase)
    {
        startPage();
        List<RouteBase> list = routeBaseService.selectRouteBaseList(routeBase);
        return getDataTable(list);
    }

    /**
     * 导出路由信息e_route_base列表
     */
    @RequiresPermissions("operator:routeBase:export")
    @Log(title = "路由信息e_route_base", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(RouteBase routeBase)
    {
        List<RouteBase> list = routeBaseService.selectRouteBaseList(routeBase);
        ExcelUtil<RouteBase> util = new ExcelUtil<RouteBase>(RouteBase.class);
        return util.exportExcel(list, "路由信息e_route_base数据");
    }

    /**
     * 新增路由信息e_route_base
     */
    @GetMapping("/add")
    public String add(ModelMap mmap){
        List<Channel> channellist = channelService.selectChannelList(0L);
        mmap.put("channellist", channellist);
        return prefix + "/add";
    }

    /**
     * 新增保存路由信息e_route_base
     */
    @RequiresPermissions("operator:routeBase:add")
    @Log(title = "路由信息e_route_base", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(RouteBase routeBase)
    {
        return toAjax(routeBaseService.insertRouteBase(routeBase));
    }

    /**
     * 修改路由信息e_route_base
     */
    @RequiresPermissions("operator:routeBase:edit")
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        RouteBase eRouteBase = routeBaseService.selectRouteBaseById(id);
        mmap.put("eRouteBase", eRouteBase);
        return prefix + "/edit";
    }

    /**
     * 修改保存路由信息e_route_base
     */
    @RequiresPermissions("operator:routeBase:edit")
    @Log(title = "路由信息e_route_base", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(RouteBase eRouteBase)
    {
        return toAjax(routeBaseService.updateRouteBase(eRouteBase));
    }

    /**
     * 删除路由信息e_route_base
     */
    @RequiresPermissions("operator:routeBase:remove")
    @Log(title = "路由信息e_route_base", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(routeBaseService.deleteRouteBaseByIds(ids));
    }

    @GetMapping("/selectByCompanyID")
    @ResponseBody
    public AjaxResult selectByCompanyID(long companyId){
        List<TApplication> applist = tApplicationService.selectTApplicationListN(companyId);
        return success(applist);

    }
}
