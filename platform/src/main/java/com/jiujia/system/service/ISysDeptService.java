package com.jiujia.system.service;

import com.jiujia.common.core.domain.Ztree;
import com.jiujia.common.core.domain.entity.SysDept;
import com.jiujia.common.core.domain.entity.SysRole;

import java.util.List;

/**
 * 部门管理 服务层
 *
 * @author ruoyi
 */
public interface ISysDeptService
{
    /**
     * 查询部门管理数据
     *
     * @param dept 部门信息
     * @return 部门信息集合
     */
    public List<SysDept> selectDeptList(SysDept dept);

    /**
     * 查询部门管理树
     *
     * @param dept 部门信息
     * @return 所有部门信息
     */
    public List<Ztree> selectDeptTree(SysDept dept);

    /**
     * 查询部门管理树（排除下级）
     *
     * @param dept 部门信息
     * @return 所有部门信息
     */
    public List<Ztree> selectDeptTreeExcludeChild(SysDept dept);

    /**
     * 根据角色ID查询菜单
     *
     * @param role 角色对象
     * @return 菜单列表
     */
    public List<Ztree> roleDeptTreeData(SysRole role);

    /**
     * 查询部门人数
     *
     * @param parentId 父部门ID
     * @return 结果
     */
    public int selectDeptCount(Long parentId);

    /**
     * 查询部门是否存在用户
     *
     * @param companyId 部门ID
     * @return 结果 true 存在 false 不存在
     */
    public boolean checkDeptExistUser(Long companyId);

    /**
     * 删除部门管理信息
     *
     * @param companyId 部门ID
     * @return 结果
     */
    public int deleteDeptById(Long companyId);

    /**
     * 新增保存部门信息
     *
     * @param dept 部门信息
     * @return 结果
     */
    public int insertDept(SysDept dept);

    /**
     * 修改保存部门信息
     *
     * @param dept 部门信息
     * @return 结果
     */
    public int updateDept(SysDept dept);

    /**
     * 根据部门ID查询信息
     *
     * @param companyId 部门ID
     * @return 部门信息
     */
    public SysDept selectDeptById(Long companyId);

    /**
     * 根据ID查询所有子部门（正常状态）
     *
     * @param companyId 部门ID
     * @return 子部门数
     */
    public int selectNormalChildrenDeptById(Long companyId);

    /**
     * 校验公司名称是否唯一
     *
     * @param dept 部门信息
     * @return 结果
     */
    public String checkCompanyNameUnique(SysDept dept);

    /**
     * 校验部门是否有数据权限
     *
     * @param companyId 部门id
     */
    public void checkDeptDataScope(Long companyId);
}
