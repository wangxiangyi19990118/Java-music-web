package filter;



import javax.servlet.*;
import java.io.IOException;

/**
 * Servlet Filter implementation class CharsetFilter
 */
//@WebFilter("/CharsetFilter")
public class CharsetFilter implements Filter {
	private String encoding;

    /**
     * Default constructor. 
     */
    public CharsetFilter() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		//set request charset UTF-8, this method must should be done before call doFilter.
		request.setCharacterEncoding(encoding);
		chain.doFilter(request, response);
		
		//set response charset UTF-8, this method must shoule be done after call doFilter.
		//maybe call response.setCharacterEncoding don't solve this problem, shoul call
		// response.setContentType("application/json;charset=UTF-8")
		response.setCharacterEncoding(encoding);
//		System.out.println("test filter ecoding:"+response.getCharacterEncoding());
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		encoding=fConfig.getInitParameter("Encoding");
		
	}

}
