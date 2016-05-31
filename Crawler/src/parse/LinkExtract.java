package parse;

import queue.*;
import crawl.*;
import java.net.URL;
import java.util.Set;
import java.util.HashSet;
import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.NodeFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.filters.OrFilter;
import org.htmlparser.filters.NodeClassFilter;

/**
 * 抽取网页中的URL链接
 * @version 1.0
 * @since version 1.0
 * @author 陈超逸
 */
public class LinkExtract 
{
	/**过滤器*/
	public static LinkFilter filter;
	/**爬取层数控制*/
	public static int layers;
	
	/**
	 * 设置各项参数
	 * @param afilter 过滤器
	 * @param alayers 爬取层数
	 * @return 无
	 * @throws 无
	 */
	public static void setParameter(LinkFilter afilter,int alayers)
	{
		filter = afilter;
		layers = alayers;
	}
	
	/**
	 * 抽取网页中的URL连接
	 * @param htmlcode 网页的HTML代码
	 * @param aurl 网页的URL
	 * @return 返回解析出来的链接
	 * @throws MyException
	 */
	public static Set<Url> extracLinks(String htmlcode,Url aurl) throws MyException
	{
		//输入和输出（url输入，links输出）
		String url = aurl.url;
		Set<Url> links = new HashSet<>();
		//控制爬取层数
		int layer = aurl.layer + 1;
		if (layer > layers)
		{
			return null;
		}
		//解析URL链接
		try
		{
			Parser parser = Parser.createParser(htmlcode, "UTF-8");
			//过滤 <frame>的过滤器，用来提取标签里的 src指示的链接
			@SuppressWarnings("serial")
			NodeFilter frameFilter = new NodeFilter()
			{
				public boolean accept(Node node) 
				{
					if (node.getText().startsWith("frame src=")) 
					{
						return true;
					} 
					else 
					{
						return false;
					}
				}
			};
			//过滤 <a>和 <frame>的过滤器，用来提取其中的链接
			OrFilter linkFilter = new OrFilter(new NodeClassFilter(LinkTag.class), frameFilter);
			//获到所有经过过滤的标签
			NodeList list = parser.extractAllNodesThatMatch(linkFilter);
			for (int i = 0; i < list.size(); i++)
			{
				Node tag = list.elementAt(i);
				if (tag instanceof LinkTag) //获取<a>标签中的链接
			    {
					LinkTag link = (LinkTag) tag;
					String linkUrl = link.getLink().trim();
					//关于网址头部的处理
					if (linkUrl.startsWith("#") || linkUrl.startsWith("void"))
					{
						continue;
					}
					else if (!linkUrl.startsWith("http"))
					{
						try
						{
							linkUrl = new URL(new URL(aurl.url),linkUrl).toString();
						}
						catch(Exception e)
						{
							continue;
						}
					}
					//关于网址尾部的处理
					if (linkUrl.endsWith("/"))
					{
						linkUrl = linkUrl.substring(0,linkUrl.length()-1);
					}
					else if (linkUrl.endsWith("/;"))
					{
						linkUrl = linkUrl.substring(0,linkUrl.length()-2);
					}
					//LinkFilter过滤器是否接受
					if (filter.accept(linkUrl))
					{
						links.add(new Url(linkUrl,layer));
					}
			    }
				else//获取<frame>标签中的链接
			    {
					//提取 frame里 src属性的链接
					String frame = tag.getText();
					int start = frame.indexOf("src=");
					frame = frame.substring(start);
					int end = frame.indexOf(" ");
					if (end == -1)
					{
						end = frame.indexOf(">");
					}
					String frameUrl = frame.substring(5, end - 1).trim();
					//关于网址头部的处理					
					if(frameUrl.startsWith("#") || frameUrl.startsWith("javascript"))
					{
						continue;
					}
					else if (!frameUrl.startsWith("http"))
					{
						try
						{
							frameUrl = new URL(new URL(aurl.url),frameUrl).toString();
						}
						catch(Exception e)
						{
							continue;
						}
					}
					//关于网址尾部的处理
					if (frameUrl.endsWith("/"))
					{
						frameUrl = frameUrl.substring(0,frameUrl.length()-1);
					}
					else if (frameUrl.endsWith("/;"))
					{
						frameUrl = frameUrl.substring(0,frameUrl.length()-2);
					}
					//LinkFilter过滤器是否接受
					if (filter.accept(frameUrl))
					{
						links.add(new Url(frameUrl,layer));
					}
			    }
			}
		}
		catch (Exception e) 
		{
			throw new MyException("LinkExtract错误---"+url+"---HTML解析错误");
		}
		return links;
	}
}
