package Test;

import Data.URLManager;

public class testtt 
{

	public static void main(String[] args) 
	{
		// TODO Auto-generated method stub
		URLManager urlManager = new URLManager("67.241.4.218", 3307, "vincentbui1999", "Tivinh265@Vv");
//		urlManager.updateSQLContent();
//		System.out.println(urlManager.getURL("/Test").toString());
//		System.out.println(urlManager.login("vbui", "123456"));
		System.out.println(urlManager.getUser(1));
	}
}
