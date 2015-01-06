package com.skysoft.domain;

/**
 * User: pinaster
 * Date: 14-1-9
 * Time: 下午4:25
 */
public class SchedulingJob {
    public static final int JS_ENABLED = 0;    // 任务启用状态
    public static final int JS_DISABLED = 1;    // 任务禁用状态
    public static final int JS_DELETE = 2;    // 任务已删除状态

    private String jobId;                        // 任务的Id，一般为所定义Bean的ID
    private String jobName;                        // 任务的描述
    private String jobGroup;                    // 任务所属组的名称
    private int jobStatus;                        // 任务的状态，0：启用；1：禁用；2：已删除
    private String cronExpression;                // 定时任务运行时间表达式
    private String memos;                       //　备注
    private String lastUpdateID;
    private int syncData;
    private int isAutoFetch;
    private String IntervalTime;
    private int allUpdate;
    private String errorDataID;
    private String Datatable;
    private String attachposition;

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobGroup() {
        return jobGroup;
    }

    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
    }

    public int getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(int jobStatus) {
        this.jobStatus = jobStatus;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public String getMemos() {
        return memos;
    }

    public void setMemos(String memos) {
        this.memos = memos;
    }

    public String getLastUpdateID() {
        return lastUpdateID;
    }

    public void setLastUpdateID(String lastUpdateID) {
        this.lastUpdateID = lastUpdateID;
    }

    public int getSyncData() {
        return syncData;
    }

    public void setSyncData(int syncData) {
        this.syncData = syncData;
    }

    public String getIntervalTime() {
        return IntervalTime;
    }

    public void setIntervalTime(String intervalTime) {
        IntervalTime = intervalTime;
    }

    public int getAllUpdate() {
        return allUpdate;
    }

    public void setAllUpdate(int allUpdate) {
        this.allUpdate = allUpdate;
    }

    public String getErrorDataID() {
        return errorDataID;
    }

    public void setErrorDataID(String errorDataID) {
        this.errorDataID = errorDataID;
    }

    public String getDatatable() {
        return Datatable;
    }

    public void setDatatable(String datatable) {
        Datatable = datatable;
    }

    public String getAttachposition() {
        return attachposition;
    }

    public void setAttachposition(String attachposition) {
        this.attachposition = attachposition;
    }

    public int getAutoFetch() {
        return isAutoFetch;
    }

    public void setAutoFetch(int autoFetch) {
        isAutoFetch = autoFetch;
    }
}