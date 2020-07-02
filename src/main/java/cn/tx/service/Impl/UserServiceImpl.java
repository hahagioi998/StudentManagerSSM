package cn.tx.service.Impl;

import cn.tx.dao.UserDao;
import cn.tx.model.User;
import cn.tx.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public User findByUserName(String username) {
        return userDao.findByUserName(username);
    }

    @Override
    public int addUser(User user) {
        return userDao.addUser(user);
    }

    @Override
    public List<User> findList(Map<String, Object> querymap) {
        return userDao.findList(querymap);
    }

    @Override
    public int findTotal(Map<String, Object> querymap) {
        return userDao.findTotal(querymap);
    }

    @Override
    public int UpdateUserById(User user) {
        return userDao.UpdateUserById(user);
    }

    @Override
    public int delete(String ids) {
        return userDao.delete(ids);
    }


}
