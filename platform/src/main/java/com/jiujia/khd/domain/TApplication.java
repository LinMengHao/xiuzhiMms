package com.jiujia.khd.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jiujia.common.annotation.Excel;
import com.jiujia.common.core.domain.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * 账户管理对象 t_application
 * 
 * @author Lixl
 * @date 2022-02-08
 */
@Data
public class TApplication extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /**  */
    private Long id;

    /** 账户名称 */
    @Excel(name = "账户名称")
    private String appName;

    /** 账户密码 */
    @Excel(name = "账户密码")
    private String password;

    /** 状态 */
    @Excel(name = "状态")
    private String status;

    /** 产品id */
    @Excel(name = "产品id")
    private Long productId;

    /** 添加时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "添加时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date addTime;

    /** 发送量 */
    @Excel(name = "发送量")
    private Long sentCount;

    /** 限制条数 */
    @Excel(name = "限制条数")
    private Long limitCount;

    /**  */
    @Excel(name = "")
    private Long payCount;

    /** 状态报告推送方式 */
    @Excel(name = "状态报告推送方式")
    private String rptSyncModel;

    /** 状态报告推送地址 */
    @Excel(name = "状态报告推送地址")
    private String rptSyncAddress;

    /** 上行推送方式 */
    @Excel(name = "上行推送方式")
    private String moSyncModel;

    /** 上行回复地址 */
    @Excel(name = "上行回复地址")
    private String moSyncAddress;

    /**  */
    @Excel(name = "")
    private String fromFlag;

    /**  */
    @Excel(name = "")
    private String sourceMent;

    /**  */
    @Excel(name = "")
    private String appExt;

    /**  */
    @Excel(name = "")
    private String defaultSign;

    /**  */
    @Excel(name = "")
    private String appType;

    /** ip鉴权 */
    @Excel(name = "ip鉴权")
    private String authIp;

    /** 公司id */
    @Excel(name = "公司id")
    private Long companyId;

    /**  */
    @Excel(name = "")
    private String payment;

    /**  */
    @Excel(name = "")
    private String pushReportFormat;

    /** 黑名单支持的级别集合 */
    @Excel(name = "黑名单支持的级别集合")
    private String blackLevels;



}
