package cn.dmwqaq.web.security;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.crypto.hash.DefaultHashService;
import org.apache.shiro.crypto.hash.Hash;
import org.apache.shiro.crypto.hash.HashRequest;
import org.apache.shiro.crypto.hash.HashService;
import org.apache.shiro.crypto.hash.format.DefaultHashFormatFactory;
import org.apache.shiro.crypto.hash.format.HashFormat;
import org.apache.shiro.crypto.hash.format.HashFormatFactory;
import org.apache.shiro.crypto.hash.format.ParsableHashFormat;
import org.apache.shiro.util.ByteSource;

import java.security.MessageDigest;

public class DefaultSaltPasswordService implements SaltPasswordService {

    private static final Logger logger = LogManager.getLogger(DefaultSaltPasswordService.class);

    private HashService hashService;
    private HashFormat hashFormat;
    private HashFormatFactory hashFormatFactory;

    private volatile boolean hashFormatWarned; //used to avoid excessive log noise

    public DefaultSaltPasswordService() {
        this.hashFormatWarned = false;
        DefaultHashService hashService = new DefaultHashService();
        hashService.setGeneratePublicSalt(true); //always want generated salts for user passwords to be most secure
        this.hashService = hashService;

        this.hashFormatFactory = new DefaultHashFormatFactory();
    }

    public String encryptPassword(Object plaintext) {
        Hash hash = hashPassword(plaintext);
        checkHashFormatDurability();
        return this.hashFormat.format(hash);
    }

    public String encryptPassword(Object plaintext, ByteSource salt) {
        Hash hash = hashPassword(plaintext, salt);
        checkHashFormatDurability();
        return this.hashFormat.format(hash);
    }

    public Hash hashPassword(Object plaintext, ByteSource salt) {
        ByteSource plaintextBytes = createByteSource(plaintext);
        if (plaintextBytes == null || plaintextBytes.isEmpty()) {
            return null;
        }
        HashRequest request = createHashRequest(plaintextBytes, salt);
        return hashService.computeHash(request);
    }

    @Override
    public Hash hashPassword(Object plaintext) throws IllegalArgumentException {
        ByteSource plaintextBytes = createByteSource(plaintext);
        if (plaintextBytes == null || plaintextBytes.isEmpty()) {
            return null;
        }
        HashRequest request = createHashRequest(plaintextBytes);
        return hashService.computeHash(request);
    }

    public boolean passwordsMatch(Object plaintext, Hash saved) {
        ByteSource plaintextBytes = createByteSource(plaintext);

        if (saved == null || saved.isEmpty()) {
            return plaintextBytes == null || plaintextBytes.isEmpty();
        } else {
            if (plaintextBytes == null || plaintextBytes.isEmpty()) {
                return false;
            }
        }

        HashRequest request = buildHashRequest(plaintextBytes, saved);

        Hash computed = this.hashService.computeHash(request);

        return constantEquals(saved.toString(), computed.toString());
    }

    public boolean passwordsMatch(Object submittedPlaintext, String saved) {
        return passwordsMatch(submittedPlaintext,saved,null);
    }

    public boolean passwordsMatch(Object submittedPlaintext, String saved, ByteSource salt) {
        ByteSource plaintextBytes = createByteSource(submittedPlaintext);

        if (saved == null || saved.length() == 0) {
            return plaintextBytes == null || plaintextBytes.isEmpty();
        } else {
            if (plaintextBytes == null || plaintextBytes.isEmpty()) {
                return false;
            }
        }

        //First check to see if we can reconstitute the original hash - this allows us to
        //perform password hash comparisons even for previously saved passwords that don't
        //match the current HashService configuration values.  This is a very nice feature
        //for password comparisons because it ensures backwards compatibility even after
        //configuration changes.
        HashFormat discoveredFormat = this.hashFormatFactory.getInstance(saved);

        if (discoveredFormat != null && discoveredFormat instanceof ParsableHashFormat) {

            ParsableHashFormat parsableHashFormat = (ParsableHashFormat) discoveredFormat;
            Hash savedHash = parsableHashFormat.parse(saved);

            return passwordsMatch(submittedPlaintext, savedHash);
        }

        //If we're at this point in the method's execution, We couldn't reconstitute the original hash.
        //So, we need to hash the submittedPlaintext using current HashService configuration and then
        //compare the formatted output with the saved string.  This will correctly compare passwords,
        //but does not allow changing the HashService configuration without breaking previously saved
        //passwords:

        //The saved text value can't be reconstituted into a Hash instance.  We need to format the
        //submittedPlaintext and then compare this formatted value with the saved value:
        HashRequest request = createHashRequest(plaintextBytes, salt);
        Hash computed = this.hashService.computeHash(request);
        String formatted = this.hashFormat.format(computed);

        return constantEquals(saved, formatted);
    }

    private boolean constantEquals(String savedHash, String computedHash) {

        byte[] savedHashByteArray = savedHash.getBytes();
        byte[] computedHashByteArray = computedHash.getBytes();

        return MessageDigest.isEqual(savedHashByteArray, computedHashByteArray);
    }

    protected void checkHashFormatDurability() {

        if (!this.hashFormatWarned) {

            HashFormat format = this.hashFormat;

            if (!(format instanceof ParsableHashFormat) && logger.isWarnEnabled()) {
                String msg = "The configured hashFormat instance [" + format.getClass().getName() + "] is not a " +
                        ParsableHashFormat.class.getName() + " implementation.  This is " +
                        "required if you wish to support backwards compatibility for saved password checking (almost " +
                        "always desirable).  Without a " + ParsableHashFormat.class.getSimpleName() + " instance, " +
                        "any hashService configuration changes will break previously hashed/saved passwords.";
                logger.warn(msg);
                this.hashFormatWarned = true;
            }
        }
    }

    protected HashRequest createHashRequest(ByteSource plaintext) {
        return new HashRequest.Builder().setSource(plaintext).build();
    }

    protected HashRequest createHashRequest(ByteSource plaintext, ByteSource salt) {
        return new HashRequest.Builder().setSource(plaintext)
                                        .setSalt(salt)
                                        .build();
    }

    protected ByteSource createByteSource(Object o) {
        return ByteSource.Util.bytes(o);
    }

    protected HashRequest buildHashRequest(ByteSource plaintext, Hash saved) {
        //keep everything from the saved hash except for the source:
        return new HashRequest.Builder().setSource(plaintext)
                                        //now use the existing saved data:
                                        .setAlgorithmName(saved.getAlgorithmName())
                                        .setSalt(saved.getSalt())
                                        .setIterations(saved.getIterations())
                                        .build();
    }

    public HashService getHashService() {
        return hashService;
    }

    public void setHashService(HashService hashService) {
        this.hashService = hashService;
    }

    public HashFormat getHashFormat() {
        return hashFormat;
    }

    public void setHashFormat(HashFormat hashFormat) {
        this.hashFormat = hashFormat;
    }

    public HashFormatFactory getHashFormatFactory() {
        return hashFormatFactory;
    }

    public void setHashFormatFactory(HashFormatFactory hashFormatFactory) {
        this.hashFormatFactory = hashFormatFactory;
    }
}
