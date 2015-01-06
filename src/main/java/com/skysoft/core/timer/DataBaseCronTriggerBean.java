package com.skysoft.core.timer;


import com.skysoft.domain.TSTimeTaskEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.CronTriggerBean;
/**
 * 在原有功能的基础上面增加数据库的读取
 * @author JueYue
 * @date 2013-9-22
 * @version 1.0
 */
public class DataBaseCronTriggerBean extends CronTriggerBean{

	private static final long serialVersionUID = 1L;
	
//	@Autowired
//	private TimeTaskServiceI timeTaskService;
	/**
	 * 读取数据库更新文件
	 */
	public void afterPropertiesSet() throws Exception {
		super.afterPropertiesSet();
        System.out.println("读取数据库更新文件");
//		TSTimeTaskEntity task = timeTaskService.findUniqueByProperty
//				(TSTimeTaskEntity.class,"taskId",this.getName());
//		if(task!=null&&task.getIsEffect().equals("1")
//				&&!task.getCronExpression().equals(this.getCronExpression())){
//			this.setCronExpression(task.getCronExpression());
//			DynamicTask.updateSpringMvcTaskXML(this,task.getCronExpression());
//		}
//        TSTimeTaskEntity task =new TSTimeTaskEntity();
//        task.setIsEffect("1");
//		if(task!=null&&task.getIsEffect().equals("1")
//				&&!task.getCronExpression().equals(this.getCronExpression())){
//			this.setCronExpression(task.getCronExpression());
//			DynamicTask.updateSpringMvcTaskXML(this,task.getCronExpression());
//		}

	}

}
