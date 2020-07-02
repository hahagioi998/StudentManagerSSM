package cn.tx.controller;

import cn.tx.model.Student;
import cn.tx.model.User;
import cn.tx.service.StudentService;
import cn.tx.service.UserService;
import cn.tx.util.CpachaUtil;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(path = "/system")
public class SystemController {

    @Autowired
    private UserService userService;

    @Autowired
    private StudentService studentService;

    /*跳转登录页*/
    @RequestMapping(path = "/login", method = RequestMethod.GET)
    public String login() {
        return "system/login";
    }


    @RequestMapping(path = "/index")
    public ModelAndView index(ModelAndView modelAndView) {
        modelAndView.setViewName("system/index");
        return modelAndView;
    }


    /*输入账号后的登录跳转*/
   @RequestMapping(path = "/checklogin", method = RequestMethod.POST)
   @ResponseBody
    public Map<String,String> login1(
           @RequestParam(value = "username",required = true) String username,
           @RequestParam(value = "password",required = true) String password,
           @RequestParam(value = "vcode",required = true) String vcode,
           @RequestParam(value = "type",required = true) int type,
           HttpServletRequest request
   ) {
       Map<String,String> map = new HashMap<>();
       if (StringUtils.isEmpty(username)){
           map.put("type","error");
           map.put("msg","用户名不能为空");
           return map;
       }
       if (StringUtils.isEmpty(password)){
           map.put("type","error");
           map.put("msg","密码不能为空");
           return map;
       }
       if (StringUtils.isEmpty(vcode)){
           map.put("type","error");
           map.put("msg","验证码不能为空");
           return map;
       }
       String loginCpacha = (String) request.getSession().getAttribute("loginCpacha");
       if (!vcode.toUpperCase().equals(loginCpacha.toUpperCase())){
           map.put("type","error");
           map.put("msg","验证码输入错误");
           return map;
       }
       request.getSession().setAttribute("loginCpacha",null);
       //数据库查询用户名和密码
       if(type==1){
           //管理员
           User name = userService.findByUserName(username);
           if (name==null){
               map.put("type","error");
               map.put("msg","用户不存在，请先注册");
               return map;
           }
           if (!password.equals(name.getPassword())){
               map.put("type","error");
               map.put("msg","密码错误请重试");
               return map;
           }
           //保存账号状态
           request.getSession().setAttribute("user",name);
       }
       if (type==2){
           //学生
           Student student = studentService.findByUserName(username);
           if(student == null){
               map.put("type", "error");
               map.put("msg", "不存在该学生!");
               return map;
           }
           if(!password.equals(student.getPassword())){
               map.put("type", "error");
               map.put("msg", "密码错误!");
               return map;
           }
           request.getSession().setAttribute("user", student);
       }
       //保存账号状态
       request.getSession().setAttribute("usertype",type);
       map.put("type","success");
       map.put("msg","登录成功!");
       return map;
    }


    /*设置验证码*/
    @RequestMapping(path = "/get_Cpacha", method = RequestMethod.GET)
    public void getCpacha(HttpServletRequest request,
                          @RequestParam(value = "vl", defaultValue = "4", required = false) Integer vl,
                          @RequestParam(value = "w", defaultValue = "98", required = false) Integer w,
                          @RequestParam(value = "h", defaultValue = "33", required = false) Integer h,
                          HttpServletResponse response) {
        CpachaUtil cpachaUtil = new CpachaUtil(vl, w, h);
        String generatorVCode = cpachaUtil.generatorVCode();
        //验证码储存在域对象
        request.getSession().setAttribute("loginCpacha", generatorVCode);
        BufferedImage generatorRotateVCodeImage = cpachaUtil.generatorRotateVCodeImage(generatorVCode, true);
        try {
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(response.getOutputStream());
            encoder.encode(generatorRotateVCodeImage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @RequestMapping(path = "/out")
    public void loginout(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        session.invalidate();
        request.getRequestDispatcher("/login").forward(request,response);
    }
}
