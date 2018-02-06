package com.mg.filter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

public class CrossDomainFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
		System.out.println("跨域---------");
		HttpServletRequest request = (HttpServletRequest) req;
		CharacterEncodingRequest requestWrapper = new CharacterEncodingRequest(request);
		HttpServletResponse response = (HttpServletResponse) resp;
		Error404ResponseWrapper responseWrapper = new Error404ResponseWrapper(this, response); 
		
		response.setHeader("Access-Control-Allow-Origin", "*");
	   /* response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
	    response.setHeader("Access-Control-Max-Age", "3600");
	    response.setHeader("Access-Control-Allow-Headers", "x-requested-with");
	    response.setContentType("application/json;charset=UTF-8");
//	    response.setHeader("Content-type","application/json; charset=UTF-8");
	    response.setCharacterEncoding("UTF-8");*/
		response.addHeader("P3P", "CP=CAO PSA OUR");  
		System.out.println(request.getContentType()+" contenttype");
		System.out.println(request.getCharacterEncoding()+" CharacterEncoding");
		chain.doFilter(requestWrapper, responseWrapper);
	}
	
	@Override
	public void destroy() {
	}
	
	/** * 对Get方式传递的请求参数进行编码 */ 
    class CharacterEncodingRequest extends HttpServletRequestWrapper { 
    	private HttpServletRequest request;
    	public CharacterEncodingRequest(HttpServletRequest request) { 
    		super(request); 
    		this.request = request; 
    	} 
    	/** * 对参数重新编码 */ 
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
      
        public Error404ResponseWrapper(CrossDomainFilter crossDomainFilter,  HttpServletResponse response) {  
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
