package cn.dmwqaq.web.mapper;

import cn.dmwqaq.web.pojo.po.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserMapper {

    User findByUsername(String username) throws Exception;

    int createUser(User user) throws Exception;
}
