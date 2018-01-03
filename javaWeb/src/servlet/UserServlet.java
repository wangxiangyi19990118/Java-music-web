package servlet;

import Bean.UserBean;
import Service.UserService;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;


import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Servlet implementation class UserServlet
 */
public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private MongoClient mongoClient;
	private MongoDatabase mongoData;
	private UserService userService;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UserServlet() {
		super();
	}

	/**
	 * @see 	 */
	public void destroy() {
		mongoClient.close();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String requestUrl = request.getRequestURI();
		if (requestUrl.contains("getValidate")) {
			response.setHeader("Pragma", "No-cache");
			response.setHeader("Cache-Control", "no-cache");
			response.setDateHeader("Expires", 0);
			response.setContentType("image/jpeg");
			// 存入会话session
			HttpSession session = request.getSession(true);
			// 删除以前的
			session.removeAttribute("validate");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		JSONObject resJson = new JSONObject();
		String requestUrl = request.getRequestURI();

		try {
			if (requestUrl.contains("register")) {
				int status = userService.register(request);
				resJson.put("status", status);
				if (status == 200)
					resJson.put("msg", "successful!");
				else if(status==2001)
					resJson.put("msg", "NumberId has registered!");
				else if(status==20010)
					resJson.put("msg", "Input error!");
				else 
					resJson.put("msg", "Server error!");
			} else if (requestUrl.contains("login")) {
				// 验证验证码
				HttpSession session = request.getSession();
				String standardVerify = session.getAttribute("validate").toString();
				String userVerify = request.getParameter("Validate").toString().toLowerCase();
				if (standardVerify == null || !userVerify.equals(standardVerify)) {
					resJson.put("status", 2002);
					resJson.put("msg", "validate error!");
				} else {
					UserBean user = userService.login(request);
					if (user == null) {
						resJson.put("status", 2003);
						resJson.put("msg", "numberid or password error!");
					} else {
						resJson.put("status", 200);
						resJson.put("msg", "successful!");
						//设置seesion
						session.setAttribute("UserNumber", user.getNumberid());
						Map<String, String> data = new HashMap<String, String>();
						data.put("NikeName", user.getNikename());
						data.put("NumberId", user.getNumberid());
						resJson.put("data", data);
					}
				}
			} else if (requestUrl.contains("editInfo")) {
				UserBean user = userService.updateInfo(request);
				if (user == null) {
					resJson.put("status", 500);
					resJson.put("msg", "update error!");
				} else {
					resJson.put("status", 200);
					resJson.put("msg", "successful!");
					Map<String, String> data = new HashMap<String, String>();
					data.put("NikeName", user.getNikename());
					resJson.put("data", data);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			resJson.put("status", 500);
		}
		finally {
			response.setContentType("application/json;charset=UTF-8");
			response.getWriter().append(resJson.toJSONString());

		}
	}

}
