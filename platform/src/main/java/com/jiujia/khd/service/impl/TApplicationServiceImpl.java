package com.jiujia.khd.service.impl;

import com.jiujia.common.core.text.Convert;
import com.jiujia.khd.domain.TApplication;
import com.jiujia.khd.mapper.TApplicationMapper;
import com.jiujia.khd.service.ITApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 账户管理Service业务层处理
 * 
 * @author Lixl
 * @date 2022-02-08
 */
@Service
public class TApplicationServiceImpl implements ITApplicationService
{
    @Autowired
    private TApplicationMapper tApplicationMapper;

    /**
     * 查询账户管理
     * 
     * @param id 账户管理主键
     * @return 账户管理
     */
    @Override
    public TApplication selectTApplicationById(Long id)
    {
        return tApplicationMapper.selectTApplicationById(id);
    }

    /**
     * 查询账户管理列表
     * 
     * @param tApplication 账户管理
     * @return 账户管理
     */
    @Override
    public List<TApplication> selectTApplicationList(TApplication tApplication)
    {
        return tApplicationMapper.selectTApplicationList(tApplication);
    }

    @Override
    public List<TApplication> selectTApplicationListN(Long CompanyId) {
        return tApplicationMapper.selectTApplicationListN(CompanyId);
    }

    /**
     * 新增账户管理
     * 
     * @param tApplication 账户管理
     * @return 结果
     */
    @Override
    public int insertTApplication(TApplication tApplication)
    {
        return tApplicationMapper.insertTApplication(tApplication);
    }

    /**
     * 修改账户管理
     * 
     * @param tApplication 账户管理
     * @return 结果
     */
    @Override
    public int updateTApplication(TApplication tApplication)
    {
        return tApplicationMapper.updateTApplication(tApplication);
    }

    /**
     * 批量删除账户管理
     * 
     * @param ids 需要删除的账户管理主键
     * @return 结果
     */
    @Override
    public int deleteTApplicationByIds(String ids)
    {
        return tApplicationMapper.deleteTApplicationByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除账户管理信息
     * 
     * @param id 账户管理主键
     * @return 结果
     */
    @Override
    public int deleteTApplicationById(Long id)
    {
        return tApplicationMapper.deleteTApplicationById(id);
    }
}
