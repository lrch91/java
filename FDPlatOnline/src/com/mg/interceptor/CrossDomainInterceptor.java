package com.mg.interceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class CrossDomainInterceptor implements HandlerInterceptor {

@Override
public void afterCompletion(HttpServletRequest req, HttpServletResponse reponse, Object arg2, Exception arg3) throws Exception {
}

@Override
public void postHandle(HttpServletRequest req, HttpServletResponse reponse, Object arg2, ModelAndView arg3) throws Exception {
}

@Override
public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object arg2) throws Exception {
	System.out.println("跨域---------");
	response.setHeader("Access-Control-Allow-Origin", "*");
   /* response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
    response.setHeader("Access-Control-Max-Age", "3600");
    response.setHeader("Access-Control-Allow-Headers", "x-requested-with");*/
	response.setCharacterEncoding("UTF-8");
	response.setContentType("text/html;charset=UTF-8");
	return true;
}

}