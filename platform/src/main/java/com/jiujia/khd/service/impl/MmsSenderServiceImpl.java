package com.jiujia.khd.service.impl;

import com.jiujia.common.core.text.Convert;
import com.jiujia.common.utils.DateUtils;
import com.jiujia.khd.domain.MmsSender;
import com.jiujia.khd.mapper.MmsSenderMapper;
import com.jiujia.khd.service.IMmsSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 下行日志的显示查询Service业务层处理
 * 
 * @author lixl
 * @date 2022-01-24
 */
@Service
public class MmsSenderServiceImpl implements IMmsSenderService
{
    @Autowired
    private MmsSenderMapper mmsSenderMapper;

    /**
     * 查询下行日志的显示查询
     * 
     * @param linkId 下行日志的显示查询主键
     * @return 下行日志的显示查询
     */
    @Override
    public MmsSender selectMmsSenderByLinkId(String linkId)
    {
        return mmsSenderMapper.selectMmsSenderByLinkId(linkId);
    }

    /**
     * 查询下行日志的显示查询列表
     * 
     * @param mmsSender 下行日志的显示查询
     * @return 下行日志的显示查询
     */
    @Override
    public List<MmsSender> selectMmsSenderList(MmsSender mmsSender)
    {
        if(mmsSender.getLogDate()==""){
            DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            Date date = new Date();
            mmsSender.setLogDate(dateFormat.format(date));
        }else{
            mmsSender.setLogDate( mmsSender.getLogDate().replaceAll("-",""));
        }
        return mmsSenderMapper.selectMmsSenderList(mmsSender);
    }

    /**
     * 新增下行日志的显示查询
     * 
     * @param mmsSender 下行日志的显示查询
     * @return 结果
     */
    @Override
    public int insertMmsSender(MmsSender mmsSender)
    {
        mmsSender.setCreateTime(DateUtils.getNowDate());
        return mmsSenderMapper.insertMmsSender(mmsSender);
    }

    /**
     * 修改下行日志的显示查询
     * 
     * @param mmsSender 下行日志的显示查询
     * @return 结果
     */
    @Override
    public int updateMmsSender(MmsSender mmsSender)
    {
        return mmsSenderMapper.updateMmsSender(mmsSender);
    }

    /**
     * 批量删除下行日志的显示查询
     * 
     * @param linkIds 需要删除的下行日志的显示查询主键
     * @return 结果
     */
    @Override
    public int deleteMmsSenderByLinkIds(String linkIds)
    {
        return mmsSenderMapper.deleteMmsSenderByLinkIds(Convert.toStrArray(linkIds));
    }

    /**
     * 删除下行日志的显示查询信息
     * 
     * @param linkId 下行日志的显示查询主键
     * @return 结果
     */
    @Override
    public int deleteMmsSenderByLinkId(String linkId)
    {
        return mmsSenderMapper.deleteMmsSenderByLinkId(linkId);
    }
}
