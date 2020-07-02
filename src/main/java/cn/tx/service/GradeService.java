package cn.tx.service;

import cn.tx.model.Grade;
import cn.tx.model.User;
import java.util.List;
import java.util.Map;

public interface GradeService {
    public Grade findByUserName(String username);
    public int addGrade(Grade grade);
    public List<Grade> findList(Map<String,Object> querymap);
    public List<Grade> findAll();
    public int findTotal(Map<String,Object> querymap);
    public int UpdateGradeById(Grade grade);
    public int delete(String ids);
}
