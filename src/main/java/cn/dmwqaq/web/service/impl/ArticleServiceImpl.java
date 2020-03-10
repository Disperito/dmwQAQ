package cn.dmwqaq.web.service.impl;

import cn.dmwqaq.web.mapper.ArticleMapper;
import cn.dmwqaq.web.pojo.po.Article;
import cn.dmwqaq.web.service.ArticleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


@Service
public class ArticleServiceImpl implements ArticleService {

    @Resource
    private ArticleMapper articleMapper;

    @Override
    public boolean insert(Article article) {
        try {
            return articleMapper.insert(article) > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Article findById(String id) {
        try {
            return articleMapper.findById(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Article();
    }
}
