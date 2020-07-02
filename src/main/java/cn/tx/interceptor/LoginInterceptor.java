package cn.tx.interceptor;

import cn.tx.model.User;
import net.sf.json.JSONObject;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/*登录拦截器器*/
public class LoginInterceptor implements HandlerInterceptor {

    /*请求发生前*/
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        Object user = request.getSession().getAttribute("user");
        if (user==null){
            System.out.println("Interceptor is lose:"+requestURI);
            if (("XMLHttpRequest").equals(request.getHeader("X-Requested-With"))){
                Map<String,String> map = new HashMap<>();
                    map.put("type","error");
                    map.put("msg","登录状态已失效，请重新登录");
                    response.getWriter().write(JSONObject.fromObject(map).toString());
                return false;
            }
            response.sendRedirect(request.getContextPath()+"/system/login");
            return false;
        }
        return true;
    }

    /*请求发送时*/
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    /*请求发送后*/
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }
}
