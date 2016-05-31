package store;

import queue.*;
import java.io.*;
import java.net.*;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
import org.htmlparser.Parser;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;

/**
 * 本类的功能是保存网页中的资源文件
 * @version 1.0
 * @since version 1.0
 * @author 陈超逸
 */
public class StoreToFileOthers 
{
	/**网络重连次数*/
	private static int netconnum = 3;
	/**网络连接超时*/
	private static int netovertime = 5000;
	/**网络读取超时*/
	private static int netrequestovertime = 5000;
    /**保存文件的类型*/
	private static boolean js = false;
	private static boolean css = false;
	private static boolean img = false;
	private static boolean xml = false;
	private static boolean word = false;
	private static boolean pdf = false;
	private static boolean ppt = false;
	private static boolean excel = false;
	
	/**
	 * 设置各项参数
	 * @param ajs 是否保存js文件
	 * @param acss 是否保存css文件
	 * @param aimg 是否保存img文件
	 * @param axml 是否保存xml文件
	 * @param aword 是否保存word文件
	 * @param apdf 是否保存pdf文件
	 * @param appt 是否保存ppt文件
	 * @param aexcel 是否保存excel文件
	 * @param anetconnum 网络重连次数
	 * @param anetovertime 网络连接超时
	 * @param anetrequestovertime 网络请求超时
	 * @return 无 
	 * @throws 无
	 */
	public static void setParameter(boolean ajs,boolean acss,boolean aimg,
			boolean axml,boolean aword,boolean apdf,boolean appt,boolean aexcel,
			int anetconnum,int anetovertime,int anetrequestovertime)
	{
		js = ajs;
		css = acss;
		img = aimg;
		xml = axml;
		word = aword;
		pdf = apdf;
		ppt = appt;
		excel = aexcel;
		netconnum = anetconnum;
		netovertime = anetovertime;
		netrequestovertime = anetrequestovertime;
	}
	
	/**
	 * 保存网页中的资源文件
	 * @param htmlcode 网页的HTML代码
	 * @param url 网页的URL
	 * @param srcFolderName 保存资源文件的文件夹
	 * @return 无
	 * @throws 无
	 */
	public static void save(String htmlcode,Url url,String srcFolderName)
	{
		//不需要下载资源文件
		if (!(js||css||img||xml||word||pdf||ppt||excel))
		{
			return;
		}
		//需要下载资源文件
		//1、过滤出网页中需要下载的文件的URL
		Parser parser = Parser.createParser(htmlcode, "UTF-8");
		Set<String> links = new HashSet<>();
		if (js)
		{
			try
			{
				AndFilter filter = new AndFilter(new TagNameFilter("script"),
									new HasAttributeFilter("src")); 
				parser.reset();
				NodeList nodes = parser.extractAllNodesThatMatch(filter);
				for (int i = 0; i < nodes.size(); i++)
				{
					try
					{
						String link = ((TagNode)nodes.elementAt(i)).getAttribute("src").trim();
						link = new URL(new URL(url.url),link).toString();
						links.add(link + ".js");
					}
					catch(Exception e)
					{
						continue;
					}
				}
			}
			catch(Exception e)
			{
				System.out.println("StoreToFileOthers错误---"+url.url+"---中js文件解析失败");
			}
		}
		if (css)
		{
			try
			{
				AndFilter filter = new AndFilter(new TagNameFilter("link"),
								   new HasAttributeFilter("rel","stylesheet"));
				parser.reset();
				NodeList nodes = parser.extractAllNodesThatMatch(filter);
				for (int i = 0; i < nodes.size(); i++)
				{
					try
					{
						String link = ((TagNode)nodes.elementAt(i)).getAttribute("href").trim();
						link = new URL(new URL(url.url),link).toString();
						links.add(link + ".css");
					}
					catch(Exception e)
					{
						continue;
					}
				}
			}
			catch(Exception e)
			{
				System.out.println("StoreToFileOthers错误---"+url.url+"---中css文件解析失败");
			}
		}
		if(img)
		{
			try
			{
				AndFilter filter = new AndFilter(new TagNameFilter("img"),
								   new HasAttributeFilter("src"));  
				parser.reset();
				NodeList nodes = parser.extractAllNodesThatMatch(filter);
				for (int i = 0; i < nodes.size(); i++)
				{
					try
					{
						String link = ((TagNode)nodes.elementAt(i)).getAttribute("src").trim();
						link = new URL(new URL(url.url),link).toString();
						links.add(link + ".jpg");
					}
					catch(Exception e)
					{
						continue;
					}
				}
			}
			catch(Exception e)
			{
				System.out.println("StoreToFileOthers错误---"+url.url+"---中img文件解析失败");
			}
		}
		if(xml||word||pdf||ppt||excel)
		{
			try
			{
				NodeClassFilter filter = new NodeClassFilter(LinkTag.class);
				parser.reset();
			    NodeList nodes = parser.extractAllNodesThatMatch(filter);
				for (int i = 0; i < nodes.size(); i++)
				{
					try
					{
						String link = ((TagNode)nodes.elementAt(i)).getAttribute("href").trim();
						if (toDownLoad(link,xml,word,pdf,ppt,excel))
						{
							link = new URL(new URL(url.url),link).toString();
							links.add(link);
						}
					}
					catch(Exception e)
					{
						continue;
					}
				}
			}
			catch(Exception e)
			{
				System.out.println("StoreToFileOthers错误---"+url.url+"---中xml，word，pdf，ppt，excel文件解析失败");
			}
		}
        //2、下载过滤出的URL对应的资源
		Iterator<String> iterator=links.iterator();
	    while (iterator.hasNext())
		{
	    	String linkname = iterator.next();
	    	String link = null;
	    	//确定资源文件类型
	    	if (linkname.substring(linkname.lastIndexOf(".")).equals(".js")||
	    		linkname.substring(linkname.lastIndexOf(".")).equals(".css"))
	    	{
	    		link = linkname.substring(0,linkname.lastIndexOf("."));
	    	}
	    	else if(linkname.substring(linkname.lastIndexOf(".")).equals(".jpg"))
	    	{
	    		link = linkname.substring(0,linkname.lastIndexOf("."));
	    		if (!((link.substring(link.lastIndexOf("/")+1)).contains(".")))
	    		{
	    			linkname = link + ".jpg";
	    		}
	    		else
	    		{
	    			linkname = link;
	    		}
	    	}
	    	else
	    	{
	    		link = linkname;
	    	}
	    	//下载文件
	    	DataOutputStream out = null;
			try
			{
		    	HttpClient httpClient = new HttpClient();
		    	httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(netovertime);
		    	GetMethod getMethod = new GetMethod(link);
		    	getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, netrequestovertime);
		    	getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,new DefaultHttpMethodRetryHandler(netconnum,false));//重连次数
		    	int statusCode = httpClient.executeMethod(getMethod);
				if (statusCode != HttpStatus.SC_OK)
				{
					throw new Exception();
				}
				byte[] srcbyte = getMethod.getResponseBody();
				//将二进制流保存到文件
				out = new DataOutputStream(new FileOutputStream(
					  new File(srcFolderName+"\\"+getFileNameByUrl(linkname))));
				for (int  j= 0; j < srcbyte.length; j++)
				{
					out.write(srcbyte[j]);
				}
			    out.flush();
			}
			catch(Exception e)
			{
				System.out.println("StoreToFileOthers错误---"+url.url+" 中   "+link+"---资源文件下载失败");
				continue;
			}
			finally
			{
				try
				{
					out.close();
				}
				catch(Exception e)
				{
					System.out.println("StoreToFileOthers错误---"+url.url+" 中   "+link+"---资源文件下载失败");
					continue;
				}
			}
		}
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
		//变换资源类型的文件名,替换文件名中不能存在的字符
		url = url.replaceAll("[\\?/:*|<>\"]", "_");
		//文件名太长
		if (url.length()>100)
		{
			url = url.substring(url.length()-99);
		}
		return url;
	}
	
	/**
	 * 判断xml，word，pdf，ppt，excel是否应该被下载
	 * @param link 网页中抽取到的链接
	 * @param xml 是否下载xml
	 * @param word 是否下载word
	 * @param pdf 是否下载pdf
	 * @param ppt 是否下载ppt
	 * @param excel 是否下载excel
	 * @return 是否下载
	 * @throws 无
	 */
	private static boolean toDownLoad(String link,boolean xml,boolean word,
									boolean pdf,boolean ppt,boolean excel)
	{
		//不需要下载这些类型的文件
		if (link == null)
		{
			return false;
		}
		//是否需要下载xml文件
		if (xml&&link.substring(link.lastIndexOf(".")+1).equals("xml"))
		{
			return true;
		}
		//是否需要下载word文件
		if (word&&(link.substring(link.lastIndexOf(".")+1).equals("doc")||
				   link.substring(link.lastIndexOf(".")+1).equals("docx")))
		{
			return true;
		}
		//是否需要下载pdf文件
		if (pdf&&link.substring(link.lastIndexOf(".")+1).equals("pdf"))
		{
			return true;
		}
		//是否需要下载ppt文件
		if (ppt&&(link.substring(link.lastIndexOf(".")+1).equals("ppt")||
				   link.substring(link.lastIndexOf(".")+1).equals("pptx")))
		{
			return true;
		}
		//是否需要下载excel文件
		if (excel&&(link.substring(link.lastIndexOf(".")+1).equals("xls")||
				   link.substring(link.lastIndexOf(".")+1).equals("xlsx")))
		{
			return true;
		}
		return false;
	}
}
