package cn.tx.service.Impl;

import cn.tx.dao.GradeDao;
import cn.tx.model.Grade;
import cn.tx.model.User;
import cn.tx.service.GradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service
public class GradeServiceImpl implements GradeService {

    @Autowired
    private GradeDao gradeDao;

    @Override
    public Grade findByUserName(String username) {
        return gradeDao.findByUserName(username);
    }

    @Override
    public int addGrade(Grade grade) {
        return gradeDao.addGrade(grade);
    }

    @Override
    public List<Grade> findList(Map<String, Object> querymap) {
        return gradeDao.findList(querymap);
    }

    @Override
    public List<Grade> findAll() {
        return gradeDao.findAll();
    }

    @Override
    public int findTotal(Map<String, Object> querymap) {
        return gradeDao.findTotal(querymap);
    }

    @Override
    public int UpdateGradeById(Grade grade) {
        return gradeDao.UpdateGradeById(grade);
    }

    @Override
    public int delete(String ids) {
        return gradeDao.delete(ids);
    }

}
