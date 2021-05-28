package Data;

import java.sql.ResultSet;
import java.util.LinkedList;

import javax.servlet.http.HttpServletRequest;

public class URLManager 
{
	//local constant
	
	//local variable
	public LinkedList<URL> urlList;
	public LinkedList<URLContent> urlContentList;
	SQL sql;
	private String schema = "PublicCode";
	
	public URLManager(String ip, int port, String username, String password)
	{
		sql = new SQL(ip, port, username, password);
//		updateSQLContent();
	}
	public void updateSQL(String ip, int port, String username, String password)
	{
		sql.changeSQL(ip, port, username, password);
	}
	public URL getURL(String url)
	{
		
		try
		{
			URL urlT = null;
			ResultSet rs = sql.executeQuery(String.format("SELECT * FROM %s.URL where URL.URL = '%s';", schema, url));
			
			while(rs.next())
			{
				urlT = new URL(rs.getInt("id"), rs.getString("name"), rs.getString("URL"), rs.getBoolean("protected"), rs.getString("user_group"));
				
				if(urlT.path.compareTo(url) == 0)
				{
					break;
				}
			}
			rs.close();
			sql.closeConnection();
			return urlT;
		}
		catch(Exception ex)
		{
			log(ex.getMessage());
		}
		return null;
	}
	
	public String getContent(String URL)
	{
		String result = "";
		try(ResultSet rs = sql.executeQuery(String.format("select * from %s.URL_content join PublicCode.URL where URL.URL = '%s' and URL.id = URL_content.URL_id", schema, URL)))
		{
			String url;
			String content;
			
			while(rs.next())
			{
				url = rs.getString("URL");
				content = rs.getString("content");
				if(url.compareTo(URL) == 0)
					result += content;
			}
			rs.close();
			sql.closeConnection();
			return result;
		}
		catch(Exception ex)
		{
			log(ex.getMessage());
			return null;
		}
	}
	
	public int login(String username, String password)
	{
		int id = -1;
		try(ResultSet rs = sql.executeQuery(String.format("select * from %s.user where username = '%s' and password = %s;", schema, username, password)))
		{
			
			String sqlUsername;
			String sqlPassword;
			
			while(rs.next())
			{
				sqlUsername = rs.getString("username");
				sqlPassword = rs.getString("password");
				if(sqlUsername.compareTo(username) == 0 && sqlPassword.compareTo(password) == 0)
				{
					id = rs.getInt("id");
				}
			}
			
			rs.close();
			
			sql.closeConnection();
			
			return id;
		}
		catch(Exception ex)
		{
			return -1;
		}
	}
	
	
	
	public User getUser(int id)
	{
		try(ResultSet rs = sql.executeQuery(String.format("select * from %s.user where id = %s", schema, id)))
		{
			User user = null;
			while(rs.next())
			{
				user = new User(rs.getInt("id"), rs.getString("name"), rs.getString("username"), rs.getString("password"), rs.getString("email"), rs.getString("security_question"), rs.getString("security_answer"), rs.getString("user_group"), rs.getDate("create_time"));
			}
			
			rs.close();
			
			sql.closeConnection();
			
			return user;
			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			return null;
		}
	}
	
	public UserPrivilege[] getUserPrivileges()
	{
		LinkedList<UserPrivilege> list;
		try(ResultSet rs = sql.executeQuery(String.format("SELECT * FROM %s.user_privilege;", schema)))
		{
			list = new LinkedList<UserPrivilege>();
			while(rs.next())
			{
				list.add(new UserPrivilege(rs.getInt("id"), rs.getString("user_group").toUpperCase()));
			}
			rs.close();
			sql.closeConnection();
			UserPrivilege[] array = new UserPrivilege[list.size()];
			for(int count = 0; count < list.size(); count++)
			{
				array[count] = list.get(count);
			}
			return array;
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			return null;
		}
	}
	
	
	public void log(String message)
	{
		sql.executeUpdate(String.format("INSERT INTO `%s`.`log` (`content`) VALUES ('%s');", schema, message));
	}
	public void logConnection(HttpServletRequest request)
	{
		sql.executeUpdate(String.format("INSERT INTO `%s`.`connection_log` (`URL`, `address`, `host`, `port`) VALUES ('%s', '%s', '%s', '%s');", schema, request.getRequestURI(), request.getRemoteAddr(), request.getRemoteHost(), request.getRemotePort()));
	}
}
