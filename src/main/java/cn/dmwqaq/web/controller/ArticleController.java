package cn.dmwqaq.web.controller;

import cn.dmwqaq.web.pojo.po.Article;
import cn.dmwqaq.web.service.ArticleService;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
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

    //  @GetMapping(value="/{id:\\d+}",produces = "text/html; charset=utf-8")
    @GetMapping(value = "/{id:\\d+}")
    public ModelAndView dispatchToArticlePage(@PathVariable String id) {
        return new ModelAndView("/WEB-INF/article.html?id=" + id);
    }

    @GetMapping(value = "/get/{id:\\d+}", produces = "application/json; charset=utf-8")
    @ResponseBody
    public String getArticle(@PathVariable String id) {
        return JSON.toJSONString(articleService.findById(id));
    }

    @GetMapping(value = "/get/{pageSize:\\d+}/{pageNum:\\d+}", produces = "application/json; charset=utf-8")
    @ResponseBody
    public String getAllArticles(@PathVariable Integer pageSize, @PathVariable Integer pageNum) {
        PageHelper.startPage(pageNum, pageSize);
        return JSON.toJSONString(new PageInfo<>(articleService.findAll(), pageSize));
    }

    @PostMapping("")
    @ResponseBody
    public String publishArticle(HttpServletRequest request) {
        String title = request.getParameter("title");
        String content = request.getParameter("content");
        String authorName = request.getParameter("authorName");
        Article article = new Article(title, authorName, content);
        return articleService.insert(article) ? String.valueOf(article.getId()) : String.valueOf(-1);
    }

    //    @GetMapping(value = "/getRecentArticles", produces = "application/json; charset=utf-8")
    @GetMapping(value = "/getRecentArticles")
    @ResponseBody
    public String getRecentArticles() {
        return JSON.toJSONString(articleService.getRecentArticles());
    }
}
