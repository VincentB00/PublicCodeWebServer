package Data;

public class URL 
{
	public int id;
	public String name;
	public String path;
	public boolean protected_resources;
	public String userGroup;
	public URL(int id, String name, String path, boolean protected_resources, String userGroup)
	{
		this.id = id;
		this.name = name;
		this.path = path;
		this.protected_resources = protected_resources;
		this.userGroup = userGroup;
	}
	public URL(int id, String name, String path)
	{
		this.id = id;
		this.name = name;
		this.path = path;
	}
	public URL(String name, String path)
	{
		this.name = name;
		this.path = path;
	}
	@Override
	public String toString() {
		return "URL [id=" + id + ", name=" + name + ", path=" + path + ", protected_resources=" + protected_resources
				+ ", userGroup=" + userGroup + "]";
	}

}
