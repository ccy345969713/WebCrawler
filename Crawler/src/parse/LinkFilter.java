package parse;
 
/**
 * 自定义的URL过滤器
 * @version 1.0
 * @since version 1.0
 * @author 陈超逸
 */
public class LinkFilter  
{
	/**包括的正则表达式*/
	private static String include = ".*";
	/**不包括的正则表达式*/
	private static String exclude = "";
	
	/**
	 * 初始化过滤器
	 * @param ainclude 包括
	 * @param aexclude 不包括
	 * @return 无
	 * @throws 无
	 */
	public LinkFilter()
	{
		include = ".*";
		exclude = "";
	}
	public LinkFilter(String ainclude,String aexclude)
	{
		if (!ainclude.equals(""))
		{
			ainclude = ainclude.replaceAll("\\.", "\\\\.");
			include = ainclude.substring(1).replaceAll(";", "(.*)");
		}
		else
		{
			include = ".*";
		}
		if (!aexclude.equals(""))
		{
			aexclude = aexclude.replaceAll("\\.", "\\\\.");
			exclude = aexclude.substring(1).replaceAll(";", "(.*)");
		}
		else
		{
			exclude = "";
		}
	}
	
	/**
	 * 界面的读取
	 * @param 无
	 * @return 包括字符，不包括字符
	 * @throws 无
	 */
	public static String[] getInclude()
	{
		if (include.equals(".*"))
		{
			String[] temp = {"","","",""};
			return temp;
		}
		else
		{
			String[] in = include.replaceAll("\\(\\.\\*\\)", ";").replaceAll("\\\\.", ".").split("\\|");
			for (int i=0;i <= in.length-1; i++)
			{
				in[i] = in[i].substring(1, in[i].length()-1);
			}
			return in;
		}
	}
	public static String[] getExclude()
	{
		if (exclude.equals(""))
		{
			String[] temp = {"","","",""};
			return temp;
		}
		else
		{
			String[] ex = exclude.replaceAll("\\(\\.\\*\\)", ";").replaceAll("\\\\.", ".").split("\\|");
			for (int i=0;i <= ex.length-1; i++)
			{
				ex[i] = ex[i].substring(1, ex[i].length()-1);
			}
			return ex;
		}
	}
	
	/**
	 * 配置文件读取和写入
	 * @param 无
	 * @return 无
	 * @throws 无
	 */
	public static String getIncludeProperty()
	{
		return include;
	}
	public static String getExcludeProperty()
	{
		return exclude;
	}
	public static void setIncludeProperty(String includeProperty)
	{
		include = includeProperty;
	}
	public static void setExcludeProperty(String excludeProperty)
	{
		exclude = excludeProperty;
	}
	
	/**
	 * 实现URL过滤的函数
	 * @param url 待判断的URL
	 * @return 判断是否过滤
	 * @throws 无
	 */
	public boolean accept(String url)
	{
		if (url.matches(include)&&!url.matches(exclude))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}
