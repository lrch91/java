package com.mg.filter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.mg.util.Utils;

public class DefaultExceptionHandler implements HandlerExceptionResolver {  
 
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,  
            Exception ex) {  
    	ModelAndView mv = new ModelAndView();             
        /*  使用response返回    */  
        response.setStatus(HttpStatus.OK.value()); //设置状态码  
        response.setContentType(MediaType.APPLICATION_JSON_VALUE); //设置ContentType  
        response.setCharacterEncoding("UTF-8"); //避免乱码  
        response.setHeader("Cache-Control", "no-cache, must-revalidate");  
        PrintWriter out;
		try {
			ex.printStackTrace();
			out = response.getWriter();
			out.write(Utils.getErrorStatusJson("系统异常-01").toJSONString());
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
        return mv;  
    }  
}  