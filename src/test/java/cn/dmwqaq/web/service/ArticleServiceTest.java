package cn.dmwqaq.web.service;

import cn.dmwqaq.web.BaseUnitTest;
import cn.dmwqaq.web.pojo.po.Article;
import com.alibaba.fastjson.JSON;
import org.junit.Test;

import javax.annotation.Resource;

import static org.junit.Assert.*;

public class ArticleServiceTest extends BaseUnitTest {

    @Resource
    ArticleService articleService;

    @Test
    public void getRecentArticles() {
        System.out.println(JSON.toJSONString(articleService.getRecentArticles()));
    }

    @Test
    public void insert(){
        for (int i = 0; i < 17; i++) {
            articleService.insert(new Article("title","name","context"));
        }
    }
}