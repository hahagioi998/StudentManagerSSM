package cn.tx.controller;


import cn.tx.model.Grade;
import cn.tx.page.Page;
import cn.tx.service.GradeService;
import cn.tx.util.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(path = "/grade")
public class GradeController {

    @Autowired
    private GradeService gradeService;

    /*首页展示*/
    @RequestMapping(path = "/list")
    public ModelAndView list(ModelAndView modelAndView) {
        modelAndView.setViewName("grade/grade_list");
        return modelAndView;
    }


    /*展示年级列表*/
    @RequestMapping(path = "/get_list", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> getList(
            @RequestParam(value="name",required=false,defaultValue="") String name,
            Page page
    ) {
        Map<String,Object> map = new HashMap<String,Object>();
        Map<String,Object> querymap = new HashMap<String,Object>();
        querymap.put("name","%"+name+"%");
        querymap.put("offset", page.getOffset());
        querymap.put("pageSize", page.getRows());
        map.put("rows",gradeService.findList(querymap));
        map.put("total",gradeService.findTotal(querymap));
        return map;
    }



    /*添加年级*/
    @RequestMapping(path = "/add", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> add(Grade grade) {
        Map<String, String> map = new HashMap<>();
        if (grade == null) {
            map.put("type", "error");
            map.put("msg", "添加失败，请联系管理员");
            return map;
        }
        if (StringUtils.isEmpty(grade.getName())) {
            map.put("type", "error");
            map.put("msg", "年级名不能为空");
            return map;
        }
        if (StringUtils.isEmpty(grade.getRemark())) {
            map.put("type", "error");
            map.put("msg", "备注不能为空");
            return map;
        }
        Grade userName = gradeService.findByUserName(grade.getName());
        if (userName!=null){
            map.put("type", "error");
            map.put("msg", "年级名已存在");
            return map;
        }
        if (gradeService.addGrade(grade) <= 0) {
            map.put("type", "error");
            map.put("msg", "添加失败，请联系管理员");
            return map;
        }
        map.put("type", "success");
        map.put("msg", "添加成功");
        return map;
    }


    /*编辑年级*/
    @RequestMapping(path = "/edit", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> edit(Grade grade) {
        Map<String, String> map = new HashMap<>();
        Grade userName = gradeService.findByUserName(grade.getName());
        if (userName!=null){
            if (grade.getId()!=userName.getId()){
                map.put("type", "error");
                map.put("msg", "id不匹配");
                return map;
            }
        }
        System.out.println(grade);
        if (gradeService.UpdateGradeById(grade)<= 0) {
            map.put("type", "error");
            map.put("msg", "修改失败，请联系管理员");
            return map;
        }
        map.put("type", "success");
        map.put("msg", "修改成功");
        return map;
    }


    /*删除年级*/
    @RequestMapping(path = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> delete(
            @RequestParam(value = "ids[]",required = true) Long[] ids
    ) {
        Map<String, String> map = new HashMap<>();
        if (ids==null||ids.length==0){
            map.put("type", "error");
            map.put("msg", "请至少选择一条数据");
            return map;
        }
        try {
            if(gradeService.delete(StringUtil.joinString(Arrays.asList(ids), ",")) <= 0){
                map.put("type", "error");
                map.put("msg", "删除失败！");
                return map;
            }
        } catch (Exception e) {
            map.put("type", "error");
            map.put("msg", "该年级下存在班级信息，请勿冲动！");
            return map;
        }
        map.put("type", "success");
        map.put("msg", "删除成功");
        return map;
    }



}
