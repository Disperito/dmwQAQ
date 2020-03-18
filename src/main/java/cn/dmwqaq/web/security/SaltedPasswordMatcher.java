package cn.dmwqaq.web.security;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SaltedAuthenticationInfo;
import org.apache.shiro.authc.credential.HashingPasswordService;
import org.apache.shiro.authc.credential.PasswordMatcher;
import org.apache.shiro.authc.credential.PasswordService;
import org.apache.shiro.crypto.hash.Hash;

public class SaltedPasswordMatcher extends PasswordMatcher {

    private PasswordService passwordService;

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        PasswordService service = ensurePasswordService();

        Object submittedPassword = getSubmittedPassword(token);
        Object storedCredentials = getStoredPassword(info);
        assertStoredCredentialsType(storedCredentials);

        if (storedCredentials instanceof Hash) {
            Hash hashedPassword = (Hash) storedCredentials;
            HashingPasswordService hashingService = assertHashingPasswordService(service);
            return hashingService.passwordsMatch(submittedPassword, hashedPassword);
        } else if (info instanceof SaltedAuthenticationInfo && passwordService instanceof SaltPasswordService) {
            String formatted = (String) storedCredentials;
            return ((SaltPasswordService) passwordService).passwordsMatch(
                    submittedPassword
                    , formatted
                    , ((SaltedAuthenticationInfo) info).getCredentialsSalt()
            );
        } else {
            //otherwise they are a String (asserted in the 'assertStoredCredentialsType' method call above):
            String formatted = (String) storedCredentials;
            return passwordService.passwordsMatch(submittedPassword, formatted);
        }
    }

    private PasswordService ensurePasswordService() {
        PasswordService service = getPasswordService();
        if (service == null) {
            String msg = "Required PasswordService has not been configured.";
            throw new IllegalStateException(msg);
        }

        return service;
    }

    private HashingPasswordService assertHashingPasswordService(PasswordService service) {
        if (service instanceof HashingPasswordService) {
            return (HashingPasswordService) service;
        }
        String msg = "AuthenticationInfo's stored credentials are a Hash instance, but the " +
                "configured passwordService is not a " +
                HashingPasswordService.class.getName() + " instance.  This is required to perform Hash " +
                "object password comparisons.";
        throw new IllegalStateException(msg);
    }

    private void assertStoredCredentialsType(Object credentials) {
        if (credentials instanceof String || credentials instanceof Hash) {
            return;
        }

        String msg = "Stored account credentials are expected to be either a " +
                Hash.class.getName() + " instance or a formatted hash String.";
        throw new IllegalArgumentException(msg);
    }

    public PasswordService getPasswordService() {
        return passwordService;
    }

    public void setPasswordService(PasswordService passwordService) {
        this.passwordService = passwordService;
    }
}
