package com.jiujia.operator.mapper;

import java.util.List;
import com.jiujia.operator.domain.Channel;

/**
 * 通道信息Mapper接口
 * 
 * @author admin
 * @date 2022-09-25
 */
public interface ChannelMapper 
{
    /**
     * 查询通道信息
     * 
     * @param id 通道信息主键
     * @return 通道信息
     */
    public Channel selectChannelById(Long id);

    /**
     * 查询通道信息列表
     * 
     * @param channel 通道信息
     * @return 通道信息集合
     */
    public List<Channel> selectChannelListPage(Channel channel);
    public List<Channel> selectChannelList(Long CompanyId);

    /**
     * 新增通道信息
     * 
     * @param channel 通道信息
     * @return 结果
     */
    public int insertChannel(Channel channel);

    /**
     * 修改通道信息
     * 
     * @param channel 通道信息
     * @return 结果
     */
    public int updateChannel(Channel channel);

    /**
     * 删除通道信息
     * 
     * @param id 通道信息主键
     * @return 结果
     */
    public int deleteChannelById(Long id);

    /**
     * 批量删除通道信息
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteChannelByIds(String[] ids);
}
