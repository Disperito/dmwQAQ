package cn.dmwqaq.web.service;

import cn.dmwqaq.web.pojo.po.Article;

public interface ArticleService {

    boolean insert(Article article);
    Article findById(String id);
}
