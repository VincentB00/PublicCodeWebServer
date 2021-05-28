package Data;

import java.sql.Date;

public class User 
{
	public int id;
	public String name;
	public String username;
	public String password;
	public String email;
	public String security_question;
	public String security_answer;
	public String userGroup;
	public Date date;
	
	
	public User(int id, String name, String username, String password, String email, String security_question, String security_answer, String userGroup, Date date)
	{
		this.id = id;
		this.name = name;
		this.username = username;
		this.password = password;
		this.email = email;
		this.security_question = security_question;
		this.security_answer = security_answer;
		this.userGroup = userGroup;
		this.date = date;
	}


	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", username=" + username + ", password=" + password + ", email="
				+ email + ", security_question=" + security_question + ", security_answer=" + security_answer
				+ ", date=" + date + "]";
	}
	
}
