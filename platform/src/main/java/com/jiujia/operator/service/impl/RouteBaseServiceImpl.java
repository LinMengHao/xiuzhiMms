package com.jiujia.operator.service.impl;

import java.util.List;

import com.jiujia.common.core.text.Convert;
import com.jiujia.common.utils.DateUtils;
import com.jiujia.operator.domain.RouteBase;
import com.jiujia.operator.mapper.RouteBaseMapper;
import com.jiujia.operator.service.IRouteBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 路由信息e_route_baseService业务层处理
 * 
 * @author ruoyi
 * @date 2022-09-18
 */
@Service
public class RouteBaseServiceImpl implements IRouteBaseService
{
    @Autowired
    private RouteBaseMapper routeBaseMapper;

    /**
     * 查询路由信息e_route_base
     * 
     * @param id 路由信息e_route_base主键
     * @return 路由信息e_route_base
     */
    @Override
    public RouteBase selectRouteBaseById(Long id)
    {
        return routeBaseMapper.selectRouteBaseById(id);
    }

    /**
     * 查询路由信息e_route_base列表
     * 
     * @param routeBase 路由信息e_route_base
     * @return 路由信息e_route_base
     */
    @Override
    public List<RouteBase> selectRouteBaseList(RouteBase routeBase)
    {
        return routeBaseMapper.selectRouteBaseList(routeBase);
    }

    /**
     * 新增路由信息e_route_base
     * 
     * @param routeBase 路由信息e_route_base
     * @return 结果
     */
    @Override
    public int insertRouteBase(RouteBase routeBase)
    {
        routeBase.setCreateTime(DateUtils.getNowDate());
        return routeBaseMapper.insertRouteBase(routeBase);
    }

    /**
     * 修改路由信息e_route_base
     * 
     * @param routeBase 路由信息e_route_base
     * @return 结果
     */
    @Override
    public int updateRouteBase(RouteBase routeBase)
    {
        routeBase.setUpdateTime(DateUtils.getNowDate());
        return routeBaseMapper.updateRouteBase(routeBase);
    }

    /**
     * 批量删除路由信息e_route_base
     * 
     * @param ids 需要删除的路由信息e_route_base主键
     * @return 结果
     */
    @Override
    public int deleteRouteBaseByIds(String ids)
    {
        return routeBaseMapper.deleteRouteBaseByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除路由信息e_route_base信息
     * 
     * @param id 路由信息e_route_base主键
     * @return 结果
     */
    @Override
    public int deleteRouteBaseById(Long id)
    {
        return routeBaseMapper.deleteRouteBaseById(id);
    }
}
