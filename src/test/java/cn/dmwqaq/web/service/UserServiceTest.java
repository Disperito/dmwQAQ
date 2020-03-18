package cn.dmwqaq.web.service;

import cn.dmwqaq.web.BaseUnitTest;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class UserServiceTest extends BaseUnitTest {

    final String username = "t1";
    final String password = "a";

    @Autowired
    private UserService userService;

    @Autowired
    private SecurityManager securityManager;

    @Test
    public void testPasswordMatch() {
        SecurityUtils.setSecurityManager(securityManager);
        AuthenticationToken token = new UsernamePasswordToken(username, password);
        Subject subject = SecurityUtils.getSubject();
        subject.login(token);
    }

    @Test
    public void createUser() {
        userService.createUser(username, password, "damowang");
    }
}