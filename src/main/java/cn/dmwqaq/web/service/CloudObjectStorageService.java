package cn.dmwqaq.web.service;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.region.Region;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Component;

import java.io.BufferedInputStream;
import java.io.File;
import java.net.URL;
import java.util.Properties;

@Component
public class CloudObjectStorageService {

    private static Logger logger = LogManager.getLogger(CloudObjectStorageService.class);

    private String secretId;
    private String secretKey;
    private final String bucketName = "dmwqaq-1300596096";
    private final String regionName = "ap-shanghai";
    private final String fileUrlPrefix = "https://dmwqaq-1300596096.cos.ap-shanghai.myqcloud.com/";

    public CloudObjectStorageService() {
        Properties properties;
        try {
            properties = PropertiesLoaderUtils.loadAllProperties("properties/cos.properties");
            secretId = (String) properties.get("cos.secretId");
            secretKey = (String) properties.get("cos.secretKey");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    public String uploadFile(File file) throws Exception {
        if (file.length() >= 50 * 1024 * 1024) {
            throw new Exception("The file size could not exceed 50MB.");
        }
        COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
        Region region = new Region(regionName);
        ClientConfig clientConfig = new ClientConfig(region);
        COSClient cosClient = new COSClient(cred, clientConfig);

        String key = getFixedFileName(file);

        if (!keyExistsInBucket(key)) {
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, file);
            cosClient.putObject(putObjectRequest);
        } else {
            //TODO :判断本地文件和库内文件是否相同，是的话直接返回其url，否则改名上传
        }

//        putObjectRequest.putCustomRequestHeader("Content-Encoding", "UTF-8");

        return fileUrlPrefix + key;
    }

    public String getFixedFileName(File file) {
        String fileName = file.getName();
        fileName = fileName.substring(fileName.lastIndexOf('\\') + 1);
        String prefix = fileName.substring(0, fileName.indexOf("."));
        String suffix = fileName.substring(fileName.indexOf("."));
        prefix += String.format("%x", System.currentTimeMillis());

        return prefix + suffix;
    }

    public boolean keyExistsInBucket(String key) {
        String fileUrl = fileUrlPrefix + key;
        URL url;
        try {
            url = new URL(fileUrl);
            BufferedInputStream bis = new BufferedInputStream(url.openStream());
            if (bis.read() != -1) {
                bis.close();
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }
}
