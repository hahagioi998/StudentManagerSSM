package cn.tx.controller;

import cn.tx.model.User;
import cn.tx.page.Page;
import cn.tx.service.UserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(path = "/user")
public class UserController {

    @Autowired
    private UserService userService;

    /*首页展示*/
    @RequestMapping(path = "/list")
    public ModelAndView list(ModelAndView modelAndView) {
        modelAndView.setViewName("user/user_list");
        return modelAndView;
    }


    /*展示用户列表*/
    @RequestMapping(path = "/get_list", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> getList(
            @RequestParam(value="username",required=false,defaultValue="") String username,
            Page page
    ) {
        Map<String,Object> map = new HashMap<String,Object>();
        Map<String,Object> querymap = new HashMap<String,Object>();
        querymap.put("username","%"+username+"%");
        querymap.put("offset", page.getOffset());
        querymap.put("pageSize", page.getRows());
        map.put("rows",userService.findList(querymap));
        map.put("total",userService.findTotal(querymap));
        return map;
    }


    /*添加用户*/
    @RequestMapping(path = "/add", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> add(User user) {
        Map<String, String> map = new HashMap<>();
        if (user == null) {
            map.put("type", "error");
            map.put("msg", "添加失败，请联系管理员");
            return map;
        }
        if (StringUtils.isEmpty(user.getUsername())) {
            map.put("type", "error");
            map.put("msg", "用户名不能为空");
            return map;
        }
        if (StringUtils.isEmpty(user.getPassword())) {
            map.put("type", "error");
            map.put("msg", "密码不能为空");
            return map;
        }
        User userName = userService.findByUserName(user.getUsername());
        if (userName!=null){
            map.put("type", "error");
            map.put("msg", "用户名已存在");
            return map;
        }
        if (userService.addUser(user) <= 0) {
            map.put("type", "error");
            map.put("msg", "添加失败，请联系管理员");
            return map;
        }
        map.put("type", "success");
        map.put("msg", "添加成功");
        return map;
    }



    /*编辑用户*/
    @RequestMapping(path = "/edit", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> edit(User user) {
        Map<String, String> map = new HashMap<>();
        User userName = userService.findByUserName(user.getUsername());
        if (userName!=null){
            if (user.getId()!=userName.getId()){
                map.put("type", "error");
                map.put("msg", "id不匹配");
                return map;
            }
        }
        System.out.println(user);
        if (userService.UpdateUserById(user)<= 0) {
            map.put("type", "error");
            map.put("msg", "修改失败，请联系管理员");
            return map;
        }
        map.put("type", "success");
        map.put("msg", "修改成功");
        return map;
    }


    /*删除用户*/
    @RequestMapping(path = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> delete(
            @RequestParam(value = "ids[]",required = true) Long[] ids
    ) {
        Map<String, String> map = new HashMap<>();
        if (ids==null){
            map.put("type", "error");
            map.put("msg", "请至少选择一条数据");
            return map;
        }
        String idsstring ="";
        for (Long id : ids) {
            idsstring += id +",";
        }
        idsstring = idsstring.substring(0,idsstring.length()-1);
        if (userService.delete(idsstring)<=0){
            map.put("type", "error");
            map.put("msg", "删除失败");
            return map;
        }
        map.put("type", "success");
        map.put("msg", "删除成功");
        return map;
    }


}
