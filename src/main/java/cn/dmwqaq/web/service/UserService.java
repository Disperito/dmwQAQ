package cn.dmwqaq.web.service;

import cn.dmwqaq.web.pojo.po.User;

public interface UserService {

    User findByUsername(String username);

    boolean createUser(String username, String password, String nickname);
}
