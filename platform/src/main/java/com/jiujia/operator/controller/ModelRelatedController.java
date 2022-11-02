package com.jiujia.operator.controller;

import java.util.List;

import com.jiujia.common.annotation.Log;
import com.jiujia.common.core.controller.BaseController;
import com.jiujia.common.core.domain.AjaxResult;
import com.jiujia.common.core.page.TableDataInfo;
import com.jiujia.common.enums.BusinessType;
import com.jiujia.common.utils.poi.ExcelUtil;
import com.jiujia.operator.domain.Channel;
import com.jiujia.operator.service.IChannelService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.jiujia.operator.domain.ModelRelated;
import com.jiujia.operator.service.IModelRelatedService;

/**
 * 模板关系e_model_relatedController
 * 
 * @author ruoyi
 * @date 2022-10-12
 */
@Controller
@RequestMapping("/operator/related")
public class ModelRelatedController extends BaseController
{
    private String prefix = "operator/related";

    @Autowired
    private IModelRelatedService modelRelatedService;
    @Autowired
    private IChannelService channelService;

    @RequiresPermissions("operator:related:view")
    @GetMapping()
    public String related(ModelMap mmap){
        List<Channel> channellist = channelService.selectChannelList(0L);
        mmap.put("channellist", channellist);
        return prefix + "/related";
    }

    /**
     * 查询模板关系e_model_related列表
     */
    @RequiresPermissions("operator:related:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(ModelRelated modelRelated)
    {
        startPage();
        List<ModelRelated> list = modelRelatedService.selectModelRelatedList(modelRelated);
        return getDataTable(list);
    }

    /**
     * 导出模板关系e_model_related列表
     */
    @RequiresPermissions("operator:related:export")
    @Log(title = "模板关系e_model_related", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(ModelRelated modelRelated)
    {
        List<ModelRelated> list = modelRelatedService.selectModelRelatedList(modelRelated);
        ExcelUtil<ModelRelated> util = new ExcelUtil<ModelRelated>(ModelRelated.class);
        return util.exportExcel(list, "模板关系e_model_related数据");
    }

    /**
     * 新增模板关系e_model_related
     */
    @GetMapping("/add")
    public String add(ModelMap mmap){
        List<Channel> channellist = channelService.selectChannelList(0L);
        mmap.put("channellist", channellist);
        return prefix + "/add";
    }

    /**
     * 新增保存模板关系e_model_related
     */
    @RequiresPermissions("operator:related:add")
    @Log(title = "模板关系e_model_related", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(ModelRelated modelRelated)
    {
        return toAjax(modelRelatedService.insertModelRelated(modelRelated));
    }

    /**
     * 修改模板关系e_model_related
     */
    @RequiresPermissions("operator:related:edit")
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap){
        List<Channel> channellist = channelService.selectChannelList(0L);
        mmap.put("channellist", channellist);
        ModelRelated modelRelated = modelRelatedService.selectModelRelatedById(id);
        mmap.put("modelRelated", modelRelated);
        return prefix + "/edit";
    }

    /**
     * 修改保存模板关系e_model_related
     */
    @RequiresPermissions("operator:related:edit")
    @Log(title = "模板关系e_model_related", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(ModelRelated modelRelated)
    {
        return toAjax(modelRelatedService.updateModelRelated(modelRelated));
    }

    /**
     * 删除模板关系e_model_related
     */
    @RequiresPermissions("operator:related:remove")
    @Log(title = "模板关系e_model_related", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(modelRelatedService.deleteModelRelatedByIds(ids));
    }
}
