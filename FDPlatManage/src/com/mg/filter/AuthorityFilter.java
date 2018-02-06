package com.mg.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.mg.service.CommonService;
import com.mg.util.Utils;
import com.mg.vo.UserAuthVo;

public class AuthorityFilter implements Filter {
	private String str = "";

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		CharacterEncodingRequest requestWrapper = new CharacterEncodingRequest(request);
		HttpServletResponse response = (HttpServletResponse) resp;
		Error404ResponseWrapper responseWrapper = new Error404ResponseWrapper(this, response);  
		
		/*过滤非法字符*/
		//获得所有请求参数名
        Enumeration<?> params = request.getParameterNames();
        while (params.hasMoreElements()) {
            String name = params.nextElement().toString();
            String[] value = req.getParameterValues(name);
            String valueStr = "";
            for (int i = 0; i < value.length; i++) {
            	valueStr = valueStr + value[i];
        	}
            if(name.toLowerCase().contains("content")||name.toLowerCase().contains("detail")){
            	//script脚本检测
            	if (scriptValidate(valueStr)) {
            		PrintWriter out = response.getWriter();
            		out.write(Utils.getErrorStatusJson("illegalScript:"+str).toJSONString());
            		out.flush();
            		return;
            	}
            }else{
            	//sql注入检测
                if (sqlValidate(valueStr)) {
                	PrintWriter out = response.getWriter();
                	out.write(Utils.getErrorStatusJson("illegalSqlInjection:"+str).toJSONString());
                	out.flush();
                	return;
                }
            }
            //上传文件名检测
            if(name.toLowerCase().contains("path")){
            	if (suffixValidate(valueStr)) {
            		response.setContentType("text/html;charset=utf-8");
            		PrintWriter out = response.getWriter();
            		out.write(Utils.getErrorStatusJson("illegalFileName:"+str).toJSONString());
            		out.flush();
            		return;
            	}
            }
        }
		/*过滤非法字符*/
		
		String path = request.getRequestURI();
		/*path = path.substring(0, path.indexOf("?"));
		String[] url = path.split("/");
		System.out.println("--------------URL-PATH--------------------------------");
		System.out.println(path);
		if(url.length>=4){
			path = "/"+url[1]+"/"+url[2]+"/"+url[3];
		}else if(url.length>=1){
			path = "/api/fdmanage/"+url[1];
		}*/
		
		String[] pathArray = path.split("/");
		if(pathArray.length<5){
			PrintWriter out = response.getWriter();
			out.write(Utils.getErrorStatusJson("errorPath").toJSONString());
			out.flush();
			return;
		}
		String authPath = pathArray[4];
		ServletContext servletContext = request.getSession().getServletContext();    
        ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		CommonService commonService = (CommonService)ctx.getBean("commonServiceImpl"); 
		if(pathArray[3].equals("common")){ 
			chain.doFilter(requestWrapper, responseWrapper);  
		}else{
			UserAuthVo ua = commonService.userAuth(request);
			if(ua==null){
				PrintWriter out = response.getWriter();
				out.write(Utils.getErrorStatusJson("unLogin").toJSONString());
				out.flush();
				return;
			}else if(!"YES".equals(ua.getManager())){
				PrintWriter out = response.getWriter();
				out.write(Utils.getErrorStatusJson("unManagerLogin").toJSONString());
				out.flush();
				return;
			}else if("YES".equals(ua.getSuperManager())){
				chain.doFilter(requestWrapper, responseWrapper);  
			}else{
				String p = commonService.menuAuthority(authPath, ua.getId());
				if(p==null){
					chain.doFilter(requestWrapper, responseWrapper);  
				}else{
					System.out.println("===noMenuAccess==========="+p);
					PrintWriter out = response.getWriter();
					out.write(Utils.getErrorStatusJson("noMenuAccess").toJSONString());
					out.flush();
					return;
				}		
			}
		}
        if (!responseWrapper.isFound()) {  
        	PrintWriter out = response.getWriter();
			out.write(Utils.getErrorStatusJson("404_NOT_FOUND").toJSONString());
			out.flush();
			return;
        }
	}
	
	@Override
	public void destroy() {
	}
	
    protected static boolean sqlValidate(String str) {
        str = str.toLowerCase();
        String badStr = "1=1|and|or|execute|insert|select|delete|update|count|declare|+|drop|table|from|column_name|information_schema.columns|table_schema|union|order|by|count|truncate|char|like";
        String[] badStrs = badStr.split("\\|");
        for (int i = 0; i < badStrs.length; i++) {
            if (str.indexOf(badStrs[i]) >= 0) {
            	System.out.println("str:"+badStrs[i]);
            	str = badStrs[i];
                return true;
            }
        }
        return false;
    }
    
    protected static boolean suffixValidate(String str) {
        str = str.toLowerCase();
        String badStr = ".jsp|.js|.exe|.sh|.bat";
        String[] badStrs = badStr.split("\\|");
        for (int i = 0; i < badStrs.length; i++) {
            if (str.endsWith(badStr)) {
            	str = badStr;
                return true;
            }
        }
        return false;
    }
    
    protected static boolean scriptValidate(String str) {
    	str = str.toLowerCase();
    	String badStr = "<script>|</script>|<javascript>|</javascript>";
    	String[] badStrs = badStr.split("\\|");
    	for (int i = 0; i < badStrs.length; i++) {
    		if (str.indexOf(badStrs[i]) >= 0) {
    			str = badStrs[i];
    			return true;
    		}
    	}
    	return false;
    }
    
    //对Get方式传递的请求参数进行编码 
    class CharacterEncodingRequest extends HttpServletRequestWrapper { 
    	private HttpServletRequest request;
    	public CharacterEncodingRequest(HttpServletRequest request) { 
    		super(request); 
    		this.request = request; 
    	} 
    	//** * 对参数重新编码 *//*
    	@Override 
    	public String getParameter(String name) { 
    		String value = super.getParameter(name); 
    		if (value == null) {
    			return null; 
    		} 
    		String method = request.getMethod(); 
    		if ("Get".equalsIgnoreCase(method)) { 
    			try{ 
    				value = new String(value.getBytes("ISO8859-1"), request.getCharacterEncoding()); 
    			} catch (UnsupportedEncodingException e) { 
    				e.printStackTrace(); 
    			} 
    		} 
    		return value; 
    	}
    }
    
    class Error404ResponseWrapper extends HttpServletResponseWrapper {  
        private int status = SC_OK;  
      
        public Error404ResponseWrapper(AuthorityFilter authorityFilter,  HttpServletResponse response) {  
            super(response);  
        }  
      
        @Override  
        public void sendError(int sc) throws IOException {  
            this.status = sc;  
            if (isFound()) {  
                super.sendError(sc);  
            } else {  
                super.setStatus(SC_OK);  
            }  
        }  
      
        @Override  
        public void sendError(int sc, String msg) throws IOException {  
            this.status = sc;  
            if (isFound()) {  
                super.sendError(sc, msg);  
            } else {  
                super.setStatus(SC_OK);  
            }  
        }  
      
        public void setStatus(int status) {  
            this.status = status;  
            super.setStatus(status);  
        }  
      
        @Override  
        public void reset() {  
            this.status = SC_OK;  
            super.reset();  
        }  
      
        public boolean isFound() {  
            return status != SC_NOT_FOUND;  
        }  
    }  

}