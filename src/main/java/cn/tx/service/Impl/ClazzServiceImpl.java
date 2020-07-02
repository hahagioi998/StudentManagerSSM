package cn.tx.service.Impl;

import cn.tx.dao.ClazzDao;
import cn.tx.dao.GradeDao;
import cn.tx.model.Clazz;
import cn.tx.model.Grade;
import cn.tx.service.ClazzService;
import cn.tx.service.GradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ClazzServiceImpl implements ClazzService {

    @Autowired
    private ClazzDao clazzDao;

    @Override
    public Clazz findByUserName(String username) {
        return clazzDao.findByUserName(username);
    }

    @Override
    public int addclazz(Clazz clazz) {
        return clazzDao.addclazz(clazz);
    }

    @Override
    public List<Clazz> findList(Map<String, Object> querymap) {
        return clazzDao.findList(querymap);
    }

    @Override
    public List<Clazz> findAll() {
        return clazzDao.findAll();
    }

    @Override
    public int findTotal(Map<String, Object> querymap) {
        return clazzDao.findTotal(querymap);
    }

    @Override
    public int UpdateGradeById(Clazz clazz) {
        return clazzDao.UpdateGradeById(clazz);
    }

    @Override
    public int delete(String ids) {
        return clazzDao.delete(ids);
    }
}
