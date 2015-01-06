package com.skysoft.domain;

import java.util.Date;


/**
 * @author JueYue
 * @version V1.0
 * @Title: Entity
 * @Description: 定时任务管理
 * @date 2013-09-21 20:47:43
 */
public class TSTimeTaskEntity implements java.io.Serializable {
    /**
     * id
     */
    private String id;
    /**
     * 任务ID
     */
    private String taskId;
    /**
     * 任务描述
     */
    private String taskDescribe;
    /**
     * cron表达式
     */
    private String cronExpression;
    /**
     * 是否生效了0未生效,1生效了
     */
    private String isEffect;
    /**
     * 是否运行0停止,1运行
     */
    private String isStart;
    /**
     * 创建时间
     */
    private Date createDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskDescribe() {
        return taskDescribe;
    }

    public void setTaskDescribe(String taskDescribe) {
        this.taskDescribe = taskDescribe;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public String getIsEffect() {
        return isEffect;
    }

    public void setIsEffect(String isEffect) {
        this.isEffect = isEffect;
    }

    public String getIsStart() {
        return isStart;
    }

    public void setIsStart(String isStart) {
        this.isStart = isStart;
    }
}
