package parse;

import crawl.*;
import queue.*;
import java.util.List;

import cn.edu.hfut.dmic.htmlbot.contentextractor.ContentExtractor;

import com.hankcs.hanlp.HanLP;
import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.NodeFilter;
import org.htmlparser.tags.MetaTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.filters.OrFilter;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.filters.HasAttributeFilter;


/**
 * 本类的功能是提取网页的结构化信息
 * @version 1.0
 * @since version 1.0
 * @author 陈超逸
 */
public class HtmlExtract 
{
	/**保存网页的根目录*/
	private static String fileroot;
	/**是否关键词填充*/
	private static boolean addkeywords = true;
	/**是否摘要填充*/
	private static boolean adddescription = true;
	/**是否正文填充*/
	private static boolean addcontent = true;
	
	/**
	 * 设置各项参数
	 * @param afileroot 保存文件的根目录
	 * @param aaddkeywords 是否关键词填充
	 * @param aadddescription 是否摘要填充
	 * @param aaddcontent 是否正文填充
	 * @return 无
	 * @throws 无
	 */
	public static void setParameter(String afileroot,boolean aaddkeywords,
	boolean aadddescription,boolean aaddcontent)
	{
		fileroot = afileroot;
		addkeywords = aaddkeywords;
		adddescription = aadddescription;
		addcontent = aaddcontent;
	}
	
	/**
	 * 提取网页的结构化信息
	 * @param htmlcode 网页的HTML代码
	 * @param url 网页的URL
	 * @return 结构化信息
	 * @throws MyException
	 * @throws InterruptedException 
	 */
	public static String[] extractHtml(String htmlcode, Url url) throws MyException, InterruptedException
	{
		//1、提取网页的地址
		String address = url.url;
		//2、网页名称
		//<title>标签中包含网页名称
		String title = null;
		Parser parser = Parser.createParser(htmlcode, "UTF-8");
		NodeFilter filter_title = new TagNameFilter("title");
		try 
		{
			NodeList titleNode = parser.extractAllNodesThatMatch(filter_title);
			if (titleNode.size() >= 1)
			{
	            Node node_title = titleNode.elementAt(0);
	            title = (node_title.toPlainTextString()).trim();
				if (title.equals(""))
				{
					title = null;
				}
			}
		} 
		catch (Exception e) 
		{
			throw new MyException("HtmlExtract错误---"+url.url+"---HTML结构化抽取失败失败");
		}
		//<title>标签中不包含网页名称
		if (title == null)
		{
			throw new InterruptedException();
		}
		//3、网页关键词
		//<meta>标签中包含网页关键词
		String keywords = null;
		parser.reset();
		AndFilter filter_keywords = new AndFilter(new TagNameFilter("meta"),
		new OrFilter(new HasAttributeFilter("name","keywords"),
		new HasAttributeFilter("name","Keywords")));
		try 
		{
			NodeList keywordsNode = parser.extractAllNodesThatMatch(filter_keywords);
			if (keywordsNode.size() >= 1)
			{
	            MetaTag node_keywords = (MetaTag) keywordsNode.elementAt(0);
	            keywords = node_keywords.getAttribute("content").trim();
	            keywords = keywords.replaceAll("[|，,;、 ]", ",");
	            keywords = keywords.replaceAll(",+", ",");
	            if (keywords.endsWith(","))
	            {
	            	keywords = keywords.substring(0,keywords.length()-1);
	            }
				if (keywords.equals(""))
				{
					keywords = null;
				}
			}
		} 
		catch (Exception e) 
		{
			throw new MyException("HtmlExtract错误---"+url.url+"---HTML结构化抽取失败失败");
		}
		//<meta>标签中不包含网页关键词
		String text = HtmlToText.Html2Text(htmlcode);
		if (addkeywords)
		{
			if (keywords == null)
			{
				List<String> keywordList = HanLP.extractKeyword(text, 5);
				String[] keyString = keywordList.toArray(new String[]{});
				if (keyString.length > 0)
				{
					keywords = "";
					for (String temp:keyString)
					{
						keywords =keywords + temp + ",";
					}
					keywords = keywords.substring(0,keywords.length()-1);
				}
			}
		}
		//4、网页的描述
		//<meta>标签中包含网页描述
		String description = null;
		parser.reset();
		AndFilter filter_description = new AndFilter(new TagNameFilter("meta"),
		new OrFilter(new HasAttributeFilter("name","description"),
		new HasAttributeFilter("name","Description")));
		try 
		{
			NodeList descriptionNode = parser.extractAllNodesThatMatch(filter_description);
			if (descriptionNode.size() >= 1)
			{
	            MetaTag node_description = (MetaTag) descriptionNode.elementAt(0);
	            description = node_description.getAttribute("content").trim();
				if (description.equals(""))
				{
					description = null;
				}
			}
		} 
		catch (Exception e) 
		{
			throw new MyException("HtmlExtract错误---"+url.url+"---HTML结构化抽取失败失败");
		}
		//<meta>标签中不包含网页描述
		if (adddescription)
		{
			if (description == null)
			{
				List<String> descriptionList = HanLP.extractSummary(text, 1);
				if (descriptionList != null)
				{
					description = descriptionList.get(0);
					description = description.replaceAll("[ |]+", "，");
				}
			}
		}
		//5、网页的正文
		String  mainbody = null;
		if (addcontent)
		{
			try 
			{
				mainbody = ContentExtractor.getContentByHtml(htmlcode).trim();
				if (mainbody == null || mainbody.equals(""))
				{
					mainbody = null;
				}
			} 
			catch (Exception e) 
			{
				mainbody = null;
			}
		}
		//6、网页的html内容位置
		String htmlPosition  = null;
		if (fileroot != null)
		{
			htmlPosition = fileroot+getFileNameByUrl(url.url) +
					"\\" + getFileNameByUrl(url.url) + ".html";
		}
		//7、网页的资源文件的位置
		String srcPosition = null;
		if (fileroot != null)
		{
			srcPosition = fileroot+getFileNameByUrl(url.url) + "\\src";
		}
		
		//8、将文件的内容至入数组中
		String[] content = new String[9];
		content[0] = null;
		content[1] = address;
		content[2] = null;
		content[3] = title;
		content[4] = keywords;
		content[5] = description;
		content[6] = mainbody;
		content[7] = htmlPosition;
		content[8] = srcPosition;
		//返回结构化信息
		return content;
	}
	
	/**
	 * 根据 URL构造文件名
	 * @param url 网页的URL
	 * @return 返回文件名
	 * @throws 无
	 */
	private static String getFileNameByUrl(String url)
	{
		//去除URL头部的http://或https://标记
		if (url.startsWith("https://"))
		{
			url = url.substring(8);
		}
		else
		{
			url = url.substring(7);
		}
		//变换html类型的文件名,替换文件名中不能存在的字符
		url = url.replaceAll("[\\?/:*|<>\"]", "_");
		//处理长度超过100的文件名
		if (url.length()>100)
		{
			url = url.substring(url.length()-99);
		}
		//返回结果
		return url;
	}
}