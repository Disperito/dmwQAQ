package cn.dmwqaq.web.controller;

import com.alibaba.fastjson.JSON;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/user")
public class UserController {

    private static Logger logger = LogManager.getLogger(UserController.class);

    @PostMapping(value = "/login", produces = "application/json; charset=utf-8")
    @ResponseBody
    public String login(@RequestParam String username, @RequestParam String password) {
        Subject subject = SecurityUtils.getSubject();
        AuthenticationToken token = new UsernamePasswordToken(username, password, true);

        LoginResponse response = new LoginResponse("-1", "登录成功");
        try {
            subject.login(token);
            response.setCode("1");
        } catch (UnknownAccountException | IncorrectCredentialsException e) {
            response.setMessage("用户名/密码错误！");
        } catch (LockedAccountException e) {
            response.setMessage("账户已被锁定！");
        } catch (AuthenticationException e) {
            logger.error(e.getMessage(), e);
            response.setMessage("登录失败！未知原因！");
        }

        return JSON.toJSONString(response);
    }

    @GetMapping("/logout")
    public ModelAndView logout() {
        SecurityUtils.getSubject().logout();
        return new ModelAndView("/index.html");
    }

    private static class LoginResponse {

        private String code;
        private String message;

        public LoginResponse(String code, String message) {
            this.code = code;
            this.message = message;
        }

        public LoginResponse() {
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}

