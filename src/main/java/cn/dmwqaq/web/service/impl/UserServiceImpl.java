package cn.dmwqaq.web.service.impl;

import cn.dmwqaq.web.mapper.UserMapper;
import cn.dmwqaq.web.pojo.po.User;
import cn.dmwqaq.web.security.SaltPasswordService;
import cn.dmwqaq.web.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.util.ByteSource;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserServiceImpl implements UserService {

    private static Logger logger = LogManager.getLogger(UserServiceImpl.class);

    private final static RandomNumberGenerator randomNumberGenerator = new SecureRandomNumberGenerator();

    @Resource
    private SaltPasswordService passwordService;

    @Resource
    private UserMapper userMapper;

    @Override
    public @Nullable User findByUsername(String username) {
        try {
            return userMapper.findByUsername(username);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public boolean createUser(String username, String password, String nickname) {
        ByteSource salt = getSalt();

        String hashedPassword = passwordService.encryptPassword(password, salt);

        User user = new User(username, hashedPassword, salt.toBase64(), nickname);

        try {
            if (userMapper.createUser(user) > 0) {
                return true;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return false;
    }

    private ByteSource getSalt() {
        return randomNumberGenerator.nextBytes();
    }
}
