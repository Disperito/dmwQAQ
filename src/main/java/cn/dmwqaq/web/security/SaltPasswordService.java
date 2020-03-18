package cn.dmwqaq.web.security;

import org.apache.shiro.authc.credential.HashingPasswordService;
import org.apache.shiro.util.ByteSource;

/**
 * 扩展了HashingPasswordService，允许在加密明文时传入盐值
 */
public interface SaltPasswordService extends HashingPasswordService {

    /**
     * 允许在调用该加密方法时传入一个盐值，是对{@link org.apache.shiro.authc.credential.PasswordService#encryptPassword(Object)}方法的改进
     *
     * @param plaintextPassword 要加密的明文
     * @param salt              加密所用的盐
     * @return 加密后的密码
     * @throws IllegalArgumentException if the argument cannot be easily converted to bytes as defined by
     *                                  *                           {@link ByteSource.Util#isCompatible(Object)}.
     */
    String encryptPassword(Object plaintextPassword, ByteSource salt) throws IllegalArgumentException;

    boolean passwordsMatch(Object submittedPlaintext, String saved, ByteSource salt);
}
