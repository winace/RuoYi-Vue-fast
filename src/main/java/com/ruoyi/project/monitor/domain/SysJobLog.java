package com.ruoyi.project.monitor.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 定时任务调度日志表 sys_job_log
 * 
 * @author ruoyi
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysJobLog extends BaseTenantEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    @Excel(name = "日志序号")
    @TableId(type = IdType.AUTO)
    private Long jobLogId;

    /** 任务名称 */
    @Excel(name = "任务名称")
    private String jobName;

    /** 任务组名 */
    @Excel(name = "任务组名")
    private String jobGroup;

    /** 调用目标字符串 */
    @Excel(name = "调用目标字符串")
    private String invokeTarget;

    /** 日志信息 */
    @Excel(name = "日志信息")
    private String jobMessage;

    /** 执行状态（0正常 1失败） */
    @Excel(name = "执行状态", readConverterExp = "0=正常,1=失败")
    private String status;

    /** 异常信息 */
    @Excel(name = "异常信息")
    private String exceptionInfo;

    /** 开始时间 */
    private Date startTime;

    /** 停止时间 */
    private Date stopTime;

}
