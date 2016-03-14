package fr.ufrt.searchengine.filter;

import java.io.IOException;

import javax.faces.application.ResourceHandler;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginFilter implements Filter {

	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest req = (HttpServletRequest) request;
        HttpSession session = req.getSession();
        
        HttpServletResponse resp = (HttpServletResponse) response;
        
        String requestUrl = req.getRequestURI(); 
        
        resp.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // Secure caching
    	resp.setHeader("Pragma", "no-cache"); // HTTP 1.0.
    	resp.setDateHeader("Expires", 0); // Proxies.

        if(session.getAttribute("username") != null
            || requestUrl.endsWith("index.jsf") 
            || requestUrl.endsWith("signup.jsf") 
            || requestUrl.endsWith("index.xhtml") 
            || requestUrl.endsWith("signup.xhtml")
            || requestUrl.startsWith(req.getContextPath() + ResourceHandler.RESOURCE_IDENTIFIER)
        	|| requestUrl.endsWith(".js.htm")
            || requestUrl.endsWith(".css.htm") 
            || requestUrl.endsWith(".gif.htm") 
            || requestUrl.endsWith(".png.htm") 
            || requestUrl.endsWith(".jpg.htm") 
            || requestUrl.endsWith(".jpeg.htm")) {
        	
        	chain.doFilter(request, response);
        } else {
            resp.sendRedirect("index.xhtml");
            return;
        }
		
	}

	public void destroy() {
		// TODO Auto-generated method stub
		
	}

}
