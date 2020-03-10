package cn.dmwqaq.web.mapper;

import cn.dmwqaq.web.pojo.po.Article;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface ArticleMapper {

    int insert(Article article) throws Exception;

    Article findById(String id) throws Exception;

}
