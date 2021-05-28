package Resources;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.LinkedList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Data.URL;
import Data.URLManager;
import Data.User;
import Data.UserPrivilege;

/**
 * Servlet implementation class Main
 */
@WebServlet("/*")
public class Main extends HttpServlet {
	private static final long serialVersionUID = 1L;
    URLManager urlManager = new URLManager("67.241.4.218", 3307, "vincentbui1999", "Tivinh265@Vv");
    private String domain = "/PublicCode";
    
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Main() 
    {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		// TODO Auto-generated method stub
		try
		{
			urlManager.logConnection(request);
			
			HttpSession session = request.getSession();
			PrintWriter pw = response.getWriter();
			response.setContentType("text/html");
			
			String body = request.getRequestURI();
			String url = body.substring(domain.length());
			
			URL sqlURL = urlManager.getURL(url);
			
			if(sqlURL == null)
			{
				pw.append("code 404<br>");
				pw.append(String.format("<a href=%s>Go to home page</a>", domain + "/home"));
				return;
			}
			
			int code = checkAuthorized(request, url);
			
			if(code == 403)
			{
				pw.append("code 403<hr>");
				pw.append("Forbidden<br>");
				pw.append(String.format("<a href=%s>Go to home page</a>", domain + "/home"));
				return;
			}
			else if(code == 401)
			{
				session.setAttribute("currentUnauthorizedPage", body);
				response.sendRedirect(String.format("%s/login", domain));
				return;
			}
			
			
			String content = urlManager.getContent(url);
			
			if(content == null)
				response.sendError(404);
			else
			{
				User user = getUser(request);
				
				if(user != null && user.name != null)
				{
					content = content.replaceAll("user.getName", user.name);
				}
				else
				{
					content = content.replaceAll("user.getName", "...");
				}
				
				pw.append(content);
			}
				
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		try
		{
			HttpSession session = request.getSession();
			PrintWriter pw = response.getWriter();
			response.setContentType("text/html");
			
			String body = request.getRequestURI();
			body = body.substring(domain.length());
			
			if(body.compareTo("/sql") == 0)
			{
				InputStreamReader reader = new InputStreamReader(request.getInputStream());
				BufferedReader br = new BufferedReader(reader);
		        String rawData = br.readLine();
		        String[] data = rawData.split("&");
		        
		        String ip = getData(data, "ip");
		        int port = Integer.parseInt(getData(data, "port"));
		        String username = getData(data, "username");
		        String password = getData(data, "password");
		        urlManager.updateSQL(ip, port, username, password);
			}
			else if(body.compareTo("/login") == 0)
			{
				InputStreamReader reader = new InputStreamReader(request.getInputStream());
				BufferedReader br = new BufferedReader(reader);
		        String rawData = br.readLine();
		        String[] data = rawData.split("&");
		        
		        String username = getData(data, "username");
		        String password = getData(data, "password");
		        
		        int id = urlManager.login(username, password);
		        
		        if(id != -1)
		        {
		        	Object backToPageURL = session.getAttribute("currentUnauthorizedPage");
		        	System.out.println(backToPageURL);

		        	session.setAttribute("userID", id);
		        	
		        	if(backToPageURL != null && backToPageURL.toString().compareTo(domain + "/login") != 0)
		        		response.sendRedirect(backToPageURL.toString());
		        	else
		        	{
		        		pw.append("<h1>");
		        		pw.append("code 200<br>");
				        pw.append("login successful<br>");
				        pw.append("</h1>");
		        	}
		        }
		        	
		        else
		        {
		        	pw.append("<h1>");
			        pw.append("login fail, please go back to login page<br>");
			        pw.append("<a href=\"#\" onclick=\"history.go(-1)\">back to login page</a>");
			        pw.append("</h1>");
		        }
			}
			else if(body.compareTo("/logout") == 0)
			{
				request.getSession().invalidate();
				pw.append("<h1>");
		        pw.append("logout successful<br>");
		        pw.append("<a href=\"#\" onclick=\"history.go(-1)\">go back</a>");
		        pw.append("</h1>");
			}
			else
				response.sendError(401);
			
			
		}
		catch(Exception ex)
		{
			urlManager.log("message: " + ex.getMessage() + "\nstack trace: " + ex.getStackTrace().toString());
			response.sendError(401);
		}
	}
	
	private String getData(String array[], String key) throws IOException
	{
		for(int count = 0; count < array.length; count++)
		{
			String block[] = array[count].split("=");
			if(block[0].compareTo(key) == 0)
				return block[1];
		}
		throw new IOException();
	}
	
	private User getUser(HttpServletRequest request)
	{
		try
		{
			HttpSession session = request.getSession();
			
			int id = Integer.parseInt(session.getAttribute("userID").toString());
			User user = urlManager.getUser(id);
			return user;
		}
		catch(Exception ex)
		{
			return null;
		}
	}
	
	private int checkAuthorized(HttpServletRequest request, String url)
	{
		try
		{	
			URL sqlUrl = urlManager.getURL(url);
			
			if(!sqlUrl.protected_resources)
				return 200;
				
			String pageUserGroup = sqlUrl.userGroup.toUpperCase();
			
			User user = getUser(request);
			
			user.userGroup = user.userGroup.toUpperCase();
			
			if(user.userGroup.compareTo("OWNER") == 0)
				return 200;
			
			UserPrivilege[] userPrivileges = urlManager.getUserPrivileges();
			
			int currentUserGroupID = -1;		//id of current login user
			int currentPageUserGroupID = -1;	//id of current page user group
			
			for(int count = 0; count < userPrivileges.length; count++)
			{
				if(user.userGroup.compareTo(userPrivileges[count].userGroup) == 0)
					currentUserGroupID = userPrivileges[count].id;
				if(pageUserGroup.compareTo(userPrivileges[count].userGroup) == 0)
					currentPageUserGroupID = userPrivileges[count].id;
				if(currentUserGroupID != -1 && currentPageUserGroupID != -1)
					break;
			}
			
			if(currentUserGroupID == currentPageUserGroupID)
				return 200;
			else if(currentUserGroupID <= currentPageUserGroupID)
				return 200;
			else
				return 403;
		}
		catch(Exception ex)
		{
			return 401;
		}
	}
	
	
}
