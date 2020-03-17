package cn.dmwqaq.web.security;

import cn.dmwqaq.web.pojo.po.User;
import cn.dmwqaq.web.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class UserRealm extends AuthorizingRealm {


    @Override
    public String getName() {
        return "UserRealm";
    }

    @Resource
    private UserService userService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        // 暂定 token 只支持 用户名密码类型
        return token instanceof UsernamePasswordToken;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken)
            throws AuthenticationException {
        String username = (String) authenticationToken.getPrincipal();
//        String password = String.valueOf((char[]) authenticationToken.getCredentials());
        User user;

        if ((user = userService.findByUsername(username)) == null) {
            throw new UnknownAccountException();
        }

//        String hashedPassword = new Sha256Hash(password, user.getPasswordSalt(), 1024).toBase64();
//        if (!user.getHashedPassword().equals(hashedPassword)) {
//            throw new IncorrectCredentialsException();
//        }

        return new SimpleAuthenticationInfo(user.getUsername(),
                                            user.getHashedPassword(),
                                            ByteSource.Util.bytes(user.getPasswordSalt()),
                                            this.getName());

    }
}
