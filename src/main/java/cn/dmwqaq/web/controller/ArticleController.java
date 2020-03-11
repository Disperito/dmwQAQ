package cn.dmwqaq.web.controller;

import cn.dmwqaq.web.pojo.po.Article;
import cn.dmwqaq.web.service.ArticleService;
import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/article")
public class ArticleController {

    @Resource
    ArticleService articleService;

    @GetMapping("/{id:\\d+}")
    public ModelAndView dispatchToArticlePage(@PathVariable String id) {
        return new ModelAndView("/WEB-INF/article.html?id=" + id);
    }

    @GetMapping(value = "/get/{id:\\d+}", produces = "application/json; charset=utf-8")
    @ResponseBody
    public String getArticle(@PathVariable String id) {
        return JSON.toJSONString(articleService.findById(id));
    }

    @PostMapping("")
    @ResponseBody
    public String publishArticle(HttpServletRequest request) {
        String title = request.getParameter("title");
        String content = request.getParameter("content");
        String authorName = request.getParameter("authorName");
        Article article = new Article(title, authorName, content);
        articleService.insert(article);
        return String.valueOf(article.getId());
    }
}
