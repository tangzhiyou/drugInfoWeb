package com.skysoft.service;

import org.springframework.stereotype.Service;

@Service("taskDemoService")
public class TaskDemoServiceImpl implements TaskDemoServiceI{
	public void work() {
      System.out.println("----------任务测试-------");
	}

}
