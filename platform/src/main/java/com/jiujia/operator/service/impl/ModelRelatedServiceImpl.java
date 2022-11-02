package com.jiujia.operator.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jiujia.common.core.text.Convert;
import com.jiujia.common.utils.DateUtils;
import com.jiujia.operator.domain.Channel;
import com.jiujia.operator.service.IChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.jiujia.operator.mapper.ModelRelatedMapper;
import com.jiujia.operator.domain.ModelRelated;
import com.jiujia.operator.service.IModelRelatedService;

/**
 * 模板关系e_model_relatedService业务层处理
 * 
 * @author ruoyi
 * @date 2022-10-12
 */
@Service
public class ModelRelatedServiceImpl implements IModelRelatedService 
{
    @Autowired
    private ModelRelatedMapper modelRelatedMapper;
    @Autowired
    private IChannelService channelService;

    /**
     * 查询模板关系e_model_related
     * 
     * @param id 模板关系e_model_related主键
     * @return 模板关系e_model_related
     */
    @Override
    public ModelRelated selectModelRelatedById(Long id)
    {
        return modelRelatedMapper.selectModelRelatedById(id);
    }

    /**
     * 查询模板关系e_model_related列表
     * 
     * @param modelRelated 模板关系e_model_related
     * @return 模板关系e_model_related
     */
    @Override
    public List<ModelRelated> selectModelRelatedList(ModelRelated modelRelated){
        List<ModelRelated> relatedList = modelRelatedMapper.selectModelRelatedList(modelRelated);
        List<Channel> channellist = channelService.selectChannelList(0L);
        Map<Long,String> map = new HashMap<Long,String>();
        for (Channel channel:channellist){
            map.put(channel.getChannelId(),channel.getChannelId()+":"+channel.getChannelName());
        }
        for (ModelRelated related:relatedList){
            related.setChannelName(map.containsKey(related.getChannelId())?map.get(related.getChannelId()):related.getChannelId()+"");
        }
        return relatedList;
    }

    /**
     * 新增模板关系e_model_related
     * 
     * @param modelRelated 模板关系e_model_related
     * @return 结果
     */
    @Override
    public int insertModelRelated(ModelRelated modelRelated)
    {
        modelRelated.setCreateTime(DateUtils.getNowDate());
        return modelRelatedMapper.insertModelRelated(modelRelated);
    }

    /**
     * 修改模板关系e_model_related
     * 
     * @param modelRelated 模板关系e_model_related
     * @return 结果
     */
    @Override
    public int updateModelRelated(ModelRelated modelRelated)
    {
        modelRelated.setUpdateTime(DateUtils.getNowDate());
        return modelRelatedMapper.updateModelRelated(modelRelated);
    }

    /**
     * 批量删除模板关系e_model_related
     * 
     * @param ids 需要删除的模板关系e_model_related主键
     * @return 结果
     */
    @Override
    public int deleteModelRelatedByIds(String ids)
    {
        return modelRelatedMapper.deleteModelRelatedByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除模板关系e_model_related信息
     * 
     * @param id 模板关系e_model_related主键
     * @return 结果
     */
    @Override
    public int deleteModelRelatedById(Long id)
    {
        return modelRelatedMapper.deleteModelRelatedById(id);
    }
}
