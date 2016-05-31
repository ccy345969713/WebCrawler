package store;

import queue.*;
import crawl.*;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;
import java.util.zip.GZIPInputStream;
import org.htmlparser.Parser;
import org.htmlparser.tags.MetaTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;

/**
 * 本类的功能是加载网页(但不保存)
 * @version 1.0
 * @since version 1.0
 * @author 陈超逸
 */
public class Loader 
{
	/**网络重连次数*/
	private static int netconnum;
	/**网络连接超时*/
	private static int netovertime;
	/**网络读取超时*/
	private static int netrequestovertime;
	
	/**屏蔽警告*/
	static 
	{
		System.setProperty("org.apache.commons.logging.Log",
				"org.apache.commons.logging.impl.SimpleLog");
		System.setProperty("org.apache.commons.logging.simplelog.showdatetime","true");
		System.setProperty("org.apache.commons.logging"
				+".simplelog.log.org.apache.commons.httpclient", "error");
	}
	
	/**
	 * 设置各项参数
	 * @param anetconnum 网络重连次数
	 * @param anetovertime 网络连接超时
	 * @param anetrequestovertime 网络请求超时
	 * @return 无
	 * @throws 无
	 */
	public static void setParameter(int anetconnum,int anetovertime,int anetrequestovertime)
	{
		netconnum = anetconnum;
		netovertime = anetovertime;
		netrequestovertime = anetrequestovertime;
	}
	
	/**
	 * 根据URL加载网页
	 * @param aurl 待加载的URL
	 * @return 加载得到的HTML代码
	 * @throws MyException 
	 * @throws InterruptedException 
	 */
	public static String downLoadFile(Url aurl) throws MyException, InterruptedException
	{
		//输入和输出（url输入，htmlcode输出）
		String url = aurl.url;
		String htmlcode = null;
		//1.生成 HttpClinet对象并设置参数（设置网络连接超时）
		HttpClient httpClient = new HttpClient();
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(netovertime);
		//2.生成 GetMethod对象并设置参数（设置网络重连次数，网络读取超时）
		GetMethod getMethod = null;
		try
		{
			getMethod = new GetMethod(url);
			//设置网络重连次数，网络读取超时
			getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, netrequestovertime);//请求超时
			getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,new DefaultHttpMethodRetryHandler(netconnum,false));//重连次数
			//爬虫伪装
			getMethod.setRequestHeader("User-Agent", "Mozilla/5.0 (Windows NT 5.1; rv:5.0) Gecko/20100101 Firefox/5.0");  
			getMethod.setRequestHeader("Accept-Encoding", "GB2312,utf-8;q=0.7,*;q=0.7");  
			getMethod.setRequestHeader("Cache-Control", "max-age=0");  
			//getMethod.setRequestHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");  
			//getMethod.setRequestHeader("Cookie", cookieManager.getCookies(url.getHost()));
			//getMethod.setRequestHeader("Accept-Language", "zh-cn,zh;q=0.5");  
		//3.执行 HTTP GET请求
			try
			{
				int statusCode = 0;
				try
				{
					statusCode = httpClient.executeMethod(getMethod);
				}
				catch(Exception e)
				{
					throw new MyException("Loader错误---"+url+"---HTML文件加载错误或请求超时");
				}
				//判断访问的状态码
				if (statusCode != HttpStatus.SC_OK)
				{
					throw new MyException("Loader错误---"+url+"---HTML请求被服务器拒绝");
				}
		//4.处理 HTTP响应的内容
				//将下载的网页默认以UTF-8解码,并保存为返回为UTF-8格式
				boolean canbeparsed = false;//网页能否解析标志
				try
				{
					String charset = "UTF-8";
					//如果文件被gzip压缩过则进行解压,获得比特流
					ByteArrayOutputStream tempStream = null;
					int BUFFER = 1024;
					byte data[] = new byte[BUFFER];
					if (getMethod.getResponseHeader("Content-Encoding") != null)
					{
						GZIPInputStream gis = new GZIPInputStream(getMethod.getResponseBodyAsStream());
						tempStream = new ByteArrayOutputStream();
						while (gis.read(data, 0, BUFFER) > 0) 
						{  
							tempStream.write(data); 
					    }
					}
					else
					{
						tempStream = new ByteArrayOutputStream();
						InputStream input = getMethod.getResponseBodyAsStream();
						int len;
						while ((len = input.read(data)) > -1 ) 
						{  
							tempStream.write(data, 0, len);  
						} 
					}
					//如果遇到refresh页面的处理方式
				    Parser parser = Parser.createParser((new String(tempStream.toByteArray(),"UTF-8")).toLowerCase(), "UTF-8");
				    AndFilter filter = new AndFilter
						    (new TagNameFilter("meta"),new HasAttributeFilter("http-equiv","refresh")); 
				    NodeList nodes = parser.extractAllNodesThatMatch(filter);
				    if (nodes.size() >= 1)
				    {
				    	throw new InterruptedException();
				    }
 					//如果解析的编码格式为GBK,则改以GBK解码并保存(两种类型的过滤器)
				    parser.reset();
				    filter = new AndFilter(new TagNameFilter("meta"),
				    		 new HasAttributeFilter("http-equiv","content-type"));   
					nodes = parser.extractAllNodesThatMatch(filter);
					if (nodes.size() >= 1)
					{
						canbeparsed = true;
						MetaTag metatag = (MetaTag) nodes.elementAt(0);
						charset = metatag.getAttribute("content");
						charset = charset.substring(charset.indexOf("=")+1,charset.length()).trim();
						if (!charset.contains("utf-8"))
						{
							htmlcode = new String(tempStream.toByteArray(),"GBK");
						}
						else
						{
							htmlcode = new String(tempStream.toByteArray(),"UTF-8");
						}
					}
					parser.reset();
					filter = new AndFilter(new TagNameFilter("meta"),
				             new HasAttributeFilter("charset"));
					nodes = parser.extractAllNodesThatMatch(filter);
					if (nodes.size() >= 1)
					{
						canbeparsed = true;
						MetaTag metatag = (MetaTag) nodes.elementAt(0);
						charset = metatag.getAttribute("charset");
						if (!charset.contains("utf-8"))
						{
							htmlcode = new String(tempStream.toByteArray(),"GBK");
						}
						else
						{
							htmlcode = new String(tempStream.toByteArray(),"UTF-8");
						}
					}
					if (!canbeparsed)
					{
						throw new Exception();
					}
				}
				catch(InterruptedException e)
				{
					throw e;
				}
				catch(Exception e)
				{
					throw new MyException("Loader错误---"+url+"---HTML网页无法解码");
				}
			}
			catch(MyException e)
			{
				throw e;
			}
		}
		finally 
		{
			try
			{
				// 释放连接
				getMethod.releaseConnection();
			}
			catch(Exception e)
			{
				throw new MyException("Loader错误---"+url+"---HTML文件加载错误或请求超时");
			}
		}
		//5.返回html代码
		return htmlcode;
	}
}
