package cn.dmwqaq.web.service;

import cn.dmwqaq.web.BaseUnitTest;
import org.junit.Test;

import javax.annotation.Resource;
import java.io.File;

public class CloudObjectStorageServiceTest extends BaseUnitTest {

    @Resource
    CloudObjectStorageService cloudObjectStorageService;

    @Test
    public void uploadFile() throws Exception {
        File file = new File("D:\\IdeaProjects\\dmwQAQ\\src\\main\\webapp\\img\\logo.png");
        System.out.println(cloudObjectStorageService.uploadFile(file));
    }

    @Test
    public void getFixedFileName() {
        File file = new File("D:\\GitHub\\DmwQaQ\\img\\logo.png");
        System.out.println(cloudObjectStorageService.getFixedFileName(file));
    }

    @Test
    public void keyExistInBucket() {
        String key = "1170c425a468.jpg";
        System.out.println(cloudObjectStorageService.keyExistsInBucket(key));
    }
}
