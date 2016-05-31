package store;

import queue.*;
import crawl.*;

import java.io.*;

/**
 * 本类的功能是将加载的网页保存为文件
 * @version 1.0
 * @since version 1.0
 * @author 陈超逸
 */
public class StoreToFile 
{
	/**保存网页的根目录*/
	private static String fileroot;
	
	/**
	 * 设置保存文件的类型
	 * @param afileroot 保存文件的根目录
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
	public static void setParameter(String afileroot,boolean ajs,boolean acss,
			boolean aimg,boolean axml,boolean aword,boolean apdf,boolean appt,boolean aexcel,
			int anetconnum,int anetovertime,int anetrequestovertime)
	{
		fileroot = afileroot;
		StoreToFileOthers.setParameter(ajs, acss, aimg, axml, aword, apdf, appt, aexcel, 
										anetconnum, anetovertime, anetrequestovertime);
	}
	
	/**
	 * 将加载的网页保存为文件
	 * @param htmlcode 网页的HTML代码
	 * @param url 网页的URL
	 * @return 无
	 * @throws MyException
	 */
	public static void saveHtml(String htmlcode,Url url) throws MyException
	{
		try
		{
			//生成保存HTML文件的文件夹和文件名，并输出流
			String folderName = fileroot+getFileNameByUrl(url.url);
			new File(folderName).mkdir();
			String fileName = folderName + "\\" + getFileNameByUrl(url.url) + ".html";
			BufferedWriter out = new BufferedWriter(new FileWriter(new File(fileName).getAbsolutePath(),false));
			try
			{
				out.write(htmlcode);
				out.flush();
			}
			finally
			{
				out.close();
			}
			//生成保存资源文件的文件夹和文件名，并调用函数输出流
			String srcFolderName = folderName + "\\src";
			new File(srcFolderName).mkdir();
			StoreToFileOthers.save(htmlcode, url, srcFolderName);
		}
		catch(Exception e)
		{
			throw new MyException("StoreToFile错误---"+url.url+"---HTML保存到本地文件错误");
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
		//变换html类型的文件名,替换文件名中不能存在的字符
		url = url.replaceAll("[\\?/:*|<>\"]", "_");
		//处理长度超过100的文件名
		if (url.length()>100)
		{
			url = url.substring(url.length()-99);
		}
		return url;
	}
}
