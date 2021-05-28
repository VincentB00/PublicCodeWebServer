package Data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class SQL 
{
	private String connectionUrl = "jdbc:mysql://67.241.4.218:3306";
    String username = "vbui";
    String password = "123456";
    Connection connection = null;
    
    public SQL(String ip, int port, String username, String password)
    {
        changeConnectionURL(ip, port);
        this.username = username;
        this.password = password;
    }
    public void changeConnectionURL(String ip, int port)
    {
        connectionUrl = "jdbc:mysql://" + ip + ":" + port;
    }
    public ResultSet executeQuery(String query)
    {
        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(connectionUrl,username,password);
            Statement statment = connection.createStatement();
            ResultSet rs = statment.executeQuery(query);
            return rs;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return null;
        }

    }
    public int executeUpdate(String query)
    {
        try(Connection connectionT = DriverManager.getConnection(connectionUrl,username,password);
        Statement statment = connectionT.createStatement())
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
            int numberOfEffectRow = statment.executeUpdate(query);
            return numberOfEffectRow;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return -1;
        }
    }
    
    public void closeConnection()
    {
        try 
        {
            if(connection != null)
                connection.close();
            connection = null;
        } 
        catch (Exception ex) 
        {
            ex.printStackTrace();
        }
    }
    public void changeSQL(String ip, int port, String username, String password)
    {
    	changeConnectionURL(ip, port);
    	this.username = username;
    	this.password = password;
    }
}
