package cn.dmwqaq.web.mapper;

import cn.dmwqaq.web.BaseUnitTest;
import cn.dmwqaq.web.pojo.po.Article;
import org.junit.Test;

import javax.annotation.Resource;

import static org.junit.Assert.*;

public class ArticleMapperTest extends BaseUnitTest {

    @Resource
    private ArticleMapper articleMapper;

    @Test
    public void insert() {
        Article article = new Article(1L,"Title","Test Author",null,null,"TextText");
        try {
            articleMapper.insert(article);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}