package com.skysoft.core.timer;


import com.skysoft.domain.TSTimeTaskEntity;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
/**
 * 读取数据库 然后判断是否启动任务
 * @author JueYue
 * @date 2013-9-22
 * @version 1.0
 */
public class DataBaseSchedulerFactoryBean extends SchedulerFactoryBean {
	
//	@Autowired
//	private TimeTaskServiceI timeTaskService;
	/**
	 * 读取数据库判断是否开始定时任务
	 */
	public void afterPropertiesSet() throws Exception {
		super.afterPropertiesSet();
		String[] trigerrNames = this.getScheduler().getTriggerNames(Scheduler.DEFAULT_GROUP);
//		TSTimeTaskEntity task;

		for (String trigerrName : trigerrNames) {
            System.out.println("当前的triger"+trigerrName+"读取数据库判断是否开始定时任务");
//            task=new TSTimeTaskEntity();
//            task.setIsStart("1");

//			task = timeTaskService.findUniqueByProperty(TSTimeTaskEntity.class,"taskId",trigerrName);
			//数据库查询不到的定时任务或者定时任务的运行状态不为1时，都停止
			//TASK #327 定时器任务默认未启动
//			if(task==null || !"1".equals(task.getIsStart())){
//				this.getScheduler().pauseTrigger(trigerrName,Scheduler.DEFAULT_GROUP);
//			}
		}
	}
}
