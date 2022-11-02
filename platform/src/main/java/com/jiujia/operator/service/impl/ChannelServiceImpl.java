package com.jiujia.operator.service.impl;

import java.util.List;

import com.jiujia.common.core.text.Convert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.jiujia.operator.mapper.ChannelMapper;
import com.jiujia.operator.domain.Channel;
import com.jiujia.operator.service.IChannelService;

/**
 * 通道信息Service业务层处理
 * 
 * @author admin
 * @date 2022-09-25
 */
@Service
public class ChannelServiceImpl implements IChannelService 
{
    @Autowired
    private ChannelMapper channelMapper;

    /**
     * 查询通道信息
     * 
     * @param id 通道信息主键
     * @return 通道信息
     */
    @Override
    public Channel selectChannelById(Long id)
    {
        return channelMapper.selectChannelById(id);
    }

    /**
     * 查询通道信息列表
     * 
     * @param channel 通道信息
     * @return 通道信息
     */
    @Override
    public List<Channel> selectChannelListPage(Channel channel)
    {
        return channelMapper.selectChannelListPage(channel);
    }
    @Override
    public List<Channel> selectChannelList(Long CompanyId)
    {
        return channelMapper.selectChannelList(CompanyId);
    }

    /**
     * 新增通道信息
     * 
     * @param channel 通道信息
     * @return 结果
     */
    @Override
    public int insertChannel(Channel channel)
    {
        return channelMapper.insertChannel(channel);
    }

    /**
     * 修改通道信息
     * 
     * @param channel 通道信息
     * @return 结果
     */
    @Override
    public int updateChannel(Channel channel)
    {
        return channelMapper.updateChannel(channel);
    }

    /**
     * 批量删除通道信息
     * 
     * @param ids 需要删除的通道信息主键
     * @return 结果
     */
    @Override
    public int deleteChannelByIds(String ids)
    {
        return channelMapper.deleteChannelByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除通道信息信息
     * 
     * @param id 通道信息主键
     * @return 结果
     */
    @Override
    public int deleteChannelById(Long id)
    {
        return channelMapper.deleteChannelById(id);
    }
}
