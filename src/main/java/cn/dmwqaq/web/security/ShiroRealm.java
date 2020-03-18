package cn.dmwqaq.web.security;

import cn.dmwqaq.web.pojo.po.User;
import cn.dmwqaq.web.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class ShiroRealm extends AuthorizingRealm {

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
        User user;

        if ((user = userService.findByUsername(username)) == null) {
            throw new UnknownAccountException();
        }

        return new SimpleAuthenticationInfo(user.getUsername(),
                                            user.getHashedPassword(),
                                            ByteSource.Util.bytes(Base64.decode(user.getPasswordSalt())),
                                            this.getName());

    }
}
