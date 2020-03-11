package cn.dmwqaq.web.service;

import cn.dmwqaq.web.pojo.po.Article;

import java.util.List;

public interface ArticleService {

    boolean insert(Article article);
    Article findById(String id);
    List<Article> getRecentArticles();
    List<Article> findAll();
}
