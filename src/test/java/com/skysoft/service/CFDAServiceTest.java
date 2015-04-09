/**
 * 
 */
package com.skysoft.service;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.skysoft.domain.Druggds;
import com.skysoft.repository.DruggdsMapper;

/**
 * @author pinaster.tang@hotmail.com
 * @date 2015Äê4ÔÂ9ÈÕ
 */
public class CFDAServiceTest {
	private ApplicationContext context=null;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		context=new ClassPathXmlApplicationContext(new String[]{"classpath:META-INF/applicationContext.xml"});
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		
	}

	/**
	 * Test method for {@link com.skysoft.service.CFDAService#saveBeans(java.util.List, java.lang.String)}.
	 */
	@Test
	public void testSaveBeans() {
		CFDAService cfdaService=context.getBean(CFDAService.class);
		Druggds druggds=new Druggds();
		druggds.setDrugBrand("222");
		List<Druggds> lists=new ArrayList<Druggds>();
		lists.add(druggds);
		cfdaService.saveBeans(lists, "drug_gds");
		System.out.println();
	}

}
