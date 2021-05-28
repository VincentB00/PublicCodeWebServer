package Data;

public class UserPrivilege 
{
	public int id;
	public String userGroup;
	public UserPrivilege(int id, String userGroup)
	{
		this.id = id;
		this.userGroup = userGroup;
	}
	
	@Override
	public String toString() 
	{
		return "id=" + id + ", userGroup=" + userGroup;
	}

}
