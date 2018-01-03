package filter;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Servlet Filter implementation class PowerCheckFilter
 */
//@WebFilter("/PowerCheckFilter")
public class PowerCheckFilter implements Filter {

    /**
     * Default constructor. 
     */
    public PowerCheckFilter() {
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		JSONObject resJson=new JSONObject();
		HttpServletRequest httpRequest=(HttpServletRequest)request;
		HttpSession session= httpRequest.getSession(false);
		if(session == null || session.getAttribute("UserNumber")==null) {
			resJson.put("status", 403);
			resJson.put("msg", "PLEASE GET OUT!");
			response.setContentType("application/json");
			response.getWriter().append(resJson.toString());
		}else {
			//传递到servlet 或者 jsp 处理
			chain.doFilter(request, response);
		}
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
	}

}
