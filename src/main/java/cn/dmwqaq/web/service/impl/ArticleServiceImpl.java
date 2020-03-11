package cn.dmwqaq.web.service.impl;

import cn.dmwqaq.web.mapper.ArticleMapper;
import cn.dmwqaq.web.pojo.po.Article;
import cn.dmwqaq.web.service.ArticleService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {

    private static Logger logger = LogManager.getLogger(ArticleServiceImpl.class);

    @Resource
    private ArticleMapper articleMapper;

    @Override
    public boolean insert(Article article) {
        try {
            return articleMapper.insert(article) > 0;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return false;
    }

    @Override
    public Article findById(String id) {
        try {
            return articleMapper.findById(id);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return new Article();
    }

    @Override
    public List<Article> getRecentArticles() {
        try {
            return articleMapper.getRecentArticles();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    @Override
    public List<Article> findAll() {
        try {
            return articleMapper.findAll();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Collections.emptyList();
        }
    }
}
