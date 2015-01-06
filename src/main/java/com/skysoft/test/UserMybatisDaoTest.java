//package com.skysoft.test;
//
//import com.skysoft.domain.User;
//import com.skysoft.repository.mybatis.UserMybatisDao;
//import org.junit.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.annotation.DirtiesContext;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
//
//import javax.sql.DataSource;
//
///**
// * User: tangzhiyou
// * Date: 14-3-24
// * Time: 下午6:37
// */
//@DirtiesContext
//@ContextConfiguration(locations = { "/META-INF/applicationContext.xml" })
//public class UserMybatisDaoTest extends AbstractTransactionalJUnit4SpringContextTests {
//    protected DataSource dataSource;
//
//    @Override
//    @Autowired
//    public void setDataSource(DataSource dataSource) {
//        super.setDataSource(dataSource);
//        this.dataSource = dataSource;
//    }
//
//    @Autowired
//    private UserMybatisDao userDao;
//
//    @Test
//    public void createAndDeleteUser() throws Exception {
////        String userName = "tangzhiyou";
////
////        User user = new User();
////        user.setLoginName(userName);
////        user.setName(userName);
////        user.setPlainPassword("123456");
////        user.setEmail(userName + "@springside.org.cn");
////        userDao.save(user);
//        User user=userDao.get(1L);
//        System.out.println(user.getEmail());
//    }
//}
