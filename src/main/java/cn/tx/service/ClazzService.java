package cn.tx.service;

import cn.tx.model.Clazz;
import cn.tx.model.Grade;

import java.util.List;
import java.util.Map;

public interface ClazzService {
    public Clazz findByUserName(String username);
    public int addclazz(Clazz clazz);
    public List<Clazz> findList(Map<String, Object> querymap);
    public List<Clazz> findAll();
    public int findTotal(Map<String, Object> querymap);
    public int UpdateGradeById(Clazz clazz);
    public int delete(String ids);
}
