package cn.tx.controller;


import cn.tx.model.Clazz;
import cn.tx.model.Grade;
import cn.tx.page.Page;
import cn.tx.service.ClazzService;
import cn.tx.service.GradeService;
import cn.tx.util.StringUtil;
import net.sf.json.JSONArray;
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
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(path = "/clazz")
public class ClazzController {

    @Autowired
    private ClazzService clazzService;
    @Autowired
    private GradeService gradeService;

    /*首页展示*/
    @RequestMapping(path = "/list")
    public ModelAndView list(ModelAndView modelAndView) {
        modelAndView.setViewName("clazz/clazz_list");
        List<Grade> gradeList = gradeService.findAll();
        modelAndView.addObject("gadeList",gradeList);
        modelAndView.addObject("gadeListJson", JSONArray.fromObject(gradeList));
        return modelAndView;
    }


    /*展示班级列表*/
    @RequestMapping(path = "/get_list", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> getList(
            @RequestParam(value="name",required=false,defaultValue="") String name,
            @RequestParam(value="gradeId",required=false,defaultValue="") Long gradeId,
            Page page
    ) {
        Map<String,Object> map = new HashMap<String,Object>();
        Map<String,Object> querymap = new HashMap<String,Object>();
        querymap.put("name","%"+name+"%");
        if (gradeId!=null){
            querymap.put("gradeId",gradeId);
        }
        querymap.put("offset", page.getOffset());
        querymap.put("pageSize", page.getRows());
        map.put("rows",clazzService.findList(querymap));
        map.put("total",clazzService.findTotal(querymap));
        return map;
    }



    /*添加班级*/
    @RequestMapping(path = "/add", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> add(Clazz clazz) {
        Map<String, String> map = new HashMap<>();
        if (clazz == null) {
            map.put("type", "error");
            map.put("msg", "添加失败，请联系管理员");
            return map;
        }
        if (clazz.getGradeId()==null){
            map.put("type", "error");
            map.put("msg", "请选择一个年级");
            return map;
        }
        if (StringUtils.isEmpty(clazz.getName())) {
            map.put("type", "error");
            map.put("msg", "班级名不能为空");
            return map;
        }
        if (StringUtils.isEmpty(clazz.getRemark())) {
            map.put("type", "error");
            map.put("msg", "备注不能为空");
            return map;
        }
        Clazz userName = clazzService.findByUserName(clazz.getName());
        if (clazzService.addclazz(clazz) <= 0) {
            map.put("type", "error");
            map.put("msg", "添加失败，请联系管理员");
            return map;
        }
        map.put("type", "success");
        map.put("msg", "添加成功");
        return map;
    }


    /*编辑班级*/
    @RequestMapping(path = "/edit", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> edit(Clazz clazz) {
        Map<String, String> map = new HashMap<>();
        Clazz userName = clazzService.findByUserName(clazz.getName());
        if (userName!=null){
            if (clazz.getId()!=userName.getId()){
                map.put("type", "error");
                map.put("msg", "id不匹配");
                return map;
            }
        }
        if (clazz.getGradeId()==null){
            map.put("type", "error");
            map.put("msg", "年级不能为空");
            return map;
        }
        System.out.println(clazz);
        if (clazzService.UpdateGradeById(clazz)<= 0) {
            map.put("type", "error");
            map.put("msg", "修改失败，请联系管理员");
            return map;
        }
        map.put("type", "success");
        map.put("msg", "修改成功");
        return map;
    }


    /*删除班级*/
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
            if(clazzService.delete(StringUtil.joinString(Arrays.asList(ids), ",")) <= 0){
                map.put("type", "error");
                map.put("msg", "删除失败！");
                return map;
            }
        } catch (Exception e) {
            map.put("type", "error");
            map.put("msg", "该班级下存在学生信息，请勿冲动！");
            return map;
        }
        map.put("type", "success");
        map.put("msg", "删除成功");
        return map;
    }



}
