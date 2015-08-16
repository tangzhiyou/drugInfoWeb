/**
 *
 */
package com.skysoft.service;

import com.skysoft.domain.Druggds;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;
import java.util.List;

/**
 * @author pinaster.tang@hotmail.com
 */
public class CFDAServiceTest {
    private ApplicationContext context = null;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        context = new ClassPathXmlApplicationContext(new String[]{"classpath:META-INF/applicationContext.xml"});
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {

    }

    /**
     * Test method for {@link com.skysoft.service.CFDAService#saveBeans(java.util.List)}.
     */
    @Test
    public void testSaveBeans() {
//        CFDAService cfdaService = context.getBean(CFDAService.class);
//        Druggds druggds = new Druggds();
//        druggds.setDrugBrand("222");
//        List<Druggds> lists = new ArrayList<Druggds>();
//        lists.add(druggds);
//        cfdaService.saveBeans(lists);
//        System.out.println();
    }

}
