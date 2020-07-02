package cn.tx.controller;

import cn.tx.model.Clazz;
import cn.tx.model.Grade;
import cn.tx.model.Student;
import cn.tx.page.Page;
import cn.tx.service.ClazzService;
import cn.tx.service.GradeService;
import cn.tx.service.StudentService;
import cn.tx.util.StringUtil;
import net.sf.json.JSONArray;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping(path = "/student")
public class StudentController {

    @Autowired
    private ClazzService clazzService;
    @Autowired
    private StudentService studentService;

    /*首页展示*/
    @RequestMapping(path = "/list")
    public ModelAndView list(ModelAndView modelAndView) {
        modelAndView.setViewName("student/student_list");
        List<Clazz> clazzList = clazzService.findAll();
        modelAndView.addObject("clazzList", clazzList);
        modelAndView.addObject("clazzListJson", JSONArray.fromObject(clazzList));
        return modelAndView;
    }


    /*展示学生列表*/
    @RequestMapping(path = "/get_list", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> getList(
            @RequestParam(value = "name", required = false, defaultValue = "") String name,
            @RequestParam(value = "clazzId", required = false, defaultValue = "") Long clazzId,
            Page page,
            HttpServletRequest request
    ) {
        Map<String, Object> map = new HashMap<String, Object>();
        Map<String, Object> querymap = new HashMap<String, Object>();
        querymap.put("username", "%" + name + "%");
        Object attribute = request.getSession().getAttribute("usertype");
        if ("2".equals(attribute.toString())) {
            //说明是学生
            Student loginedStudent = (Student) request.getSession().getAttribute("user");
            querymap.put("username", "%" + loginedStudent.getUsername() + "%");
        }
        if (clazzId != null) {
            querymap.put("clazzId", clazzId);
        }
        querymap.put("offset", page.getOffset());
        querymap.put("pageSize", page.getRows());
        map.put("rows", studentService.findList(querymap));
        map.put("total", studentService.getTotal(querymap));
        return map;
    }


    /*添加学生*/
    @RequestMapping(path = "/add", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> add(Student student) {
        Map<String, String> map = new HashMap<>();
        if (student == null) {
            map.put("type", "error");
            map.put("msg", "添加失败，请联系管理员");
            return map;
        }
        if (student.getClazzId() == null) {
            map.put("type", "error");
            map.put("msg", "请选择一个班级");
            return map;
        }
        if (StringUtils.isEmpty(student.getUsername())) {
            map.put("type", "error");
            map.put("msg", "姓名不能为空");
            return map;
        }
        if (StringUtils.isEmpty(student.getPassword())) {
            map.put("type", "error");
            map.put("msg", "密码不能为空");
            return map;
        }
        if (isExist(student.getUsername(), null)) {
            map.put("type", "error");
            map.put("msg", "该姓名已存在！");
            return map;
        }
        student.setSn(StringUtil.generateSn("S", ""));
        if (studentService.add(student) <= 0) {
            map.put("type", "error");
            map.put("msg", "学生添加失败！");
            return map;
        }
        map.put("type", "success");
        map.put("msg", "添加成功");
        return map;
    }


    /*编辑学生*/
    @RequestMapping(path = "/edit", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> edit(Student student) throws IOException {
        Map<String, String> map = new HashMap<>();
        Student userName = studentService.findByUserName(student.getUsername());
        if (userName != null) {
            if (student.getId() != userName.getId()) {
                map.put("type", "error");
                map.put("msg", "id不匹配");
                return map;
            }
        }
        if (student.getClazzId() == null) {
            map.put("type", "error");
            map.put("msg", "班级不能为空");
            return map;
        }
        if (isExist(student.getUsername(), student.getId())) {
            map.put("type", "error");
            map.put("msg", "该姓名已存在！");
            return map;
        }
        student.setSn(StringUtil.generateSn("S", ""));
        if (studentService.edit(student) <= 0) {
            map.put("type", "error");
            map.put("msg", "学生修改失败！");
            return map;
        }
        map.put("type", "success");
        map.put("msg", "修改成功");
        return map;
    }


    /*删除学生*/
    @RequestMapping(path = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> delete(
            @RequestParam(value = "ids[]", required = true) Long[] ids
    ) {
        Map<String, String> map = new HashMap<>();
        if (ids == null || ids.length == 0) {
            map.put("type", "error");
            map.put("msg", "请至少选择一条数据");
            return map;
        }
        if (studentService.delete(StringUtil.joinString(Arrays.asList(ids), ",")) <= 0) {
            map.put("type", "error");
            map.put("msg", "删除失败！");
            return map;
        }
        map.put("type", "success");
        map.put("msg", "删除成功");
        return map;
    }


    /*头像上传*/
    @RequestMapping(path = "/upload_photo", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> uploadPhoto(MultipartFile photo,
                                           HttpServletRequest request
    ) throws IOException {
        Map<String, String> map = new HashMap<>();
        if (photo == null) {
            map.put("type", "error");
            map.put("msg", "请上传图片");
            return map;
        }
        if (photo.getSize() > 10485760) {
            map.put("type", "error");
            map.put("msg", "图片大小已超过10M");
            return map;
        }
        String suffix = photo.getOriginalFilename().substring(photo.getOriginalFilename().lastIndexOf(".") + 1, photo.getOriginalFilename().length());
        if (!"jpg,png,gif,jpeg".contains(suffix.toLowerCase())) {
            map.put("type", "error");
            map.put("msg", "图片不属于gif/png/jpg格式");
            return map;
        }
        String s = UUID.randomUUID().toString() + "." + suffix;
        String savePath = request.getSession().getServletContext().getRealPath("/") + "\\photo\\";
        File savefile = new File(savePath);
        if (!savefile.exists()) {
            savefile.mkdirs();
        }
        photo.transferTo(new File(savePath, s));
        map.put("type", "success");
        map.put("msg", "图片上传成功！");
        map.put("src", request.getSession().getServletContext().getContextPath() + "/photo/" + s);
        return map;
    }

    /*编辑重复*/
    private boolean isExist(String username, Long id) {
        Student student = studentService.findByUserName(username);
        if (student != null) {
            if (id == null) {
                return true;
            }
            if (student.getId().longValue() != id.longValue()) {
                return true;
            }
        }
        return false;
    }

}
