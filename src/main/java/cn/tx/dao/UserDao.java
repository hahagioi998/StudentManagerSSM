package cn.tx.dao;

import cn.tx.model.User;
import java.util.List;
import java.util.Map;

public interface UserDao {

    public User findByUserName(String username);
    public int addUser(User user);
    public List<User> findList(Map<String,Object> querymap);
    public int findTotal(Map<String,Object> querymap);
    public int UpdateUserById(User user);
    public int delete(String ids);
}
