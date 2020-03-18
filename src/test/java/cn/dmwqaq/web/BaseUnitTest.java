package cn.dmwqaq.web;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath*:spring-config.xml",
                       "classpath*:spring-shiro.xml",
                       "classpath*:spring-mybatis.xml"})
public class BaseUnitTest {

}
