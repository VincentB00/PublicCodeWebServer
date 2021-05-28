package Data;

public class URLContent 
{
	public int id;
	public int URL_id;
	public String content;
	public URLContent(int id, int URL_id, String content)
	{
		this.id = id;
		this.URL_id = URL_id;
		this.content = content;
	}
	public URLContent(int URL_id, String content)
	{
		this.URL_id = URL_id;
		this.content = content;
	}
	@Override
	public String toString() {
		return "URLContent [id=" + id + ", URL_content_id=" + URL_id + ", content=" + content + "]";
	}
	
}
