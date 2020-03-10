package cn.dmwqaq.web.controller;

import cn.dmwqaq.web.service.ArticleService;
import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@RequestMapping("/article")
public class ArticleController {

    @Resource
    ArticleService articleService;

    @GetMapping("/{id}")
    @ResponseBody
    public String getArticle(@PathVariable String id) {
        return JSON.toJSONString(articleService.findById(id));
    }

}
