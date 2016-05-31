package queue;

/**
 * 本类的功能是封装URL
 * @version 1.0
 * @since version 1.0
 * @author 陈超逸
 */
public class Url 
{
	/**URL名称*/
	public final String url;
	/**URL层数*/
	public final int layer;
	
	/**
	 * 初始化url和layer
	 * @param aurl
	 * @param alayer
	 * @return 无
	 * @throws 无
	 */
	public Url(String aurl,int alayer)
	{
		url = aurl;
		layer = alayer;
	}
}
