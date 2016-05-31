package store;

import crawl.*;
import queue.*;
import java.util.Locale;
import java.io.File;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.sql.Connection;
import java.sql.DriverManager;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.HeadMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;

/**
 * 本类的功能是实现网页的更新
 * @version 1.0
 * @since version 1.0
 * @author 陈超逸
 */
public class Renew 
{
	/**当前取出记录的位置*/
	private static int now;
	/**总的记录的数量*/
	private static int total;
	/**主机的地址*/
	private static String HOSTNAME;
	/**端口号*/
	private static String PORT;
	/**数据库名*/
	private static String DBNAME;
	/**表名*/
	private static String TABLENAME;  
	/**用户名*/
	private static String USERID;
	/**口令*/
	private static String PASSWORD;
	/**网络重连次数*/
	private static int netconnum;
	/**网络连接超时*/
	private static int netovertime;
	/**网络读取超时*/
	private static int netrequestovertime;
	/**文件保存的路径*/
	private static String fileroot;
	/**是否使用数据库*/
	private static boolean setdb = true;
	/**是否将网页保存为文件*/
	private static boolean setsave = true;
	/**数据库的设置*/
	private static Connection conn = null;
	private static Statement statement = null;
	private static ResultSet rs = null;
	
	/**
	 * 设置各项参数
	 * @param hostname 主机名
	 * @param port 端口号 
	 * @param dbname 数据库名称
	 * @param tablename 表名
	 * @param userid 用户名
	 * @param password 用户口令
	 * @param anetconnum 网络重连次数
	 * @param anetovertime 网络连接超时
	 * @param anetrequestovertime 网络请求超时
	 * @param afileroot 保存文件的根目录
	 * @return 无
	 * @throws 无
	 */
	public static void setParameter(String hostname, String port, String dbname,
				String tablename, String userid, String password,int anetconnum,
				int anetovertime,int anetrequestovertime,String afileroot,boolean asetdb,boolean asetsave)
	{
		HOSTNAME = hostname;
		PORT = port;
		DBNAME = dbname;
		TABLENAME = tablename;
		USERID = userid;
		PASSWORD = password;
		netconnum = anetconnum;
		netovertime = anetovertime;
		netrequestovertime = anetrequestovertime;
		fileroot = afileroot;
		setdb = asetdb;
		setsave = asetsave;
	}
	
	/**
	 * 重置起始位置和获取记录数量
	 * @return 无
	 * @throws MyException
	 */
	public static void init() throws MyException 
	{
		//重置起始位置为0
		now = 0;
		//获取记录数数量
		String driver = "com.mysql.jdbc.Driver";
		String dbcon = "jdbc:mysql://"+HOSTNAME+":"+PORT+"/"+DBNAME;
		try
		{
			Class.forName(driver);
			conn = DriverManager.getConnection(dbcon, USERID, PASSWORD);
			statement = conn.createStatement();
			String sql = "SELECT COUNT(*) FROM "+TABLENAME;
			rs = statement.executeQuery(sql);
			rs.next();
			total = rs.getInt("count(*)");
		}
		catch(Exception e)
		{
			if (rs != null)
			{
				try 
				{
					rs.close();
				} 
				catch (Exception ee) 
				{}
			}
			if (statement != null)
			{
				try 
				{
					statement.close();
				} 
				catch (Exception ee) 
				{}
			}
			if (conn != null)
			{
				try 
				{
					conn.close();
				} 
				catch (Exception ee) 
				{}
			}
			throw new MyException("Renew错误---数据库连接错误"); 
		}
		finally
		{
			if (rs != null)
			{
				try 
				{
					rs.close();
				} 
				catch (Exception ee) 
				{
					throw new MyException("Renew错误---数据库连接错误");
				}
			}
		}
	}
	
	/**
	 * 依次获取待更新数据的URl
	 * @throws 无
	 * @return 无
	 */
	public static String[] getUrl()
	{
		while (now < total)
		{
			//输出URL和时间的组合
			String[] urlTime = new String[2];
			//从数据库中查询
			String sql = "SELECT  url, time FROM "+TABLENAME+" limit "+now+", 1";
			now++;
			try
			{
				rs = statement.executeQuery(sql);
				rs.next();
				urlTime[0] = rs.getString("url").trim();
				urlTime[1] = rs.getString("time").trim();
			}
			catch(Exception e)
			{
				continue;
			}
			finally
			{
				try 
				{
					rs.close();
				} 
				catch (Exception ee) 
				{}
			}
			return urlTime;
		}
		return null;
	}
	
	/**
	 * 获取数据的URL及采集时间
	 * @param urlTime URL和采集时间 
	 * @throws MyException 
	 * @throws InterruptedException 
	 * @return 无
	 */
	public static void renew(String[] urlTime) throws MyException, InterruptedException
	{
		//获取URL及采集时间
		String url = urlTime[0];
		String time = urlTime[1];
		//将网页采集时间处理为Timestamp
		Timestamp collectTime = new Timestamp(System.currentTimeMillis());   
        try 
        {   
        	collectTime = Timestamp.valueOf(time);   
        } 
        catch (Exception e) 
        {
        	throw new MyException("Renew错误---"+url+"---时间格式错误");
        }
        //将网页更新时间处理为Timestamp
		Timestamp modifiedTime;
        HttpClient httpClient = new HttpClient();
      	httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(netovertime);
        HeadMethod headMethod = new HeadMethod(url);
        headMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, netrequestovertime);//请求超时
        headMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,new DefaultHttpMethodRetryHandler(netconnum,false));//重连次数
		int statusCode = 0;
		try 
		{
			statusCode = httpClient.executeMethod(headMethod);
			//判断访问的状态码
			if (statusCode != HttpStatus.SC_OK)
			{
				throw new MyException("Renew错误---"+url+"---更新请求被服务器拒绝");
			}
			//获取网页的更新时间 
			String lastModified = headMethod.getResponseHeader("last-modified").getValue();
	        try 
	        {   
	        	DateFormat gmt = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z",Locale.ENGLISH);  
	        	long modifyTime = gmt.parse(lastModified.trim()).getTime();
	        	modifiedTime = new Timestamp(modifyTime);
	        } 
	        catch (Exception e) 
	        {
				throw new MyException("Renew错误---"+url+"---时间格式错误");
	        }
		} 
		catch (MyException e) 
		{
			throw e;
		}
		catch (Exception e)
		{
			throw new MyException("Renew错误---"+url+"---更新请求超时或无更新字段");
		}
		finally
		{
			try
			{
				headMethod.releaseConnection();
			}
			catch(Exception e)
			{
				throw new MyException("Renew错误---"+url+"---更新请求超时");
			}
		}
        //判断是否需要执行更新
		if (modifiedTime.getTime() > collectTime.getTime())
		{
			try
			{
				//进行数据库更新
				Url renewUrl = new Url(url,1);
				String renewHtmlcode = Loader.downLoadFile(renewUrl);
				if (setdb)
				{
					RenewToDB.renewHtml(renewHtmlcode,renewUrl);
				}
				if (setsave)
				{
					//删除文件夹
					String folderName = fileroot+getFileNameByUrl(url);
					File folder = new File(folderName);
					deleteDir(folder);
					//重新下载数据
					StoreToFile.saveHtml(renewHtmlcode, renewUrl);
				}
			}
			catch(Exception e)
			{
				throw e;
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
		//变换html类型的文件名,替换文件名中不能存在的字符
		url = url.replaceAll("[\\?/:*|<>\"]", "_");
		//处理长度超过100的文件名
		if (url.length()>100)
		{
			url = url.substring(url.length()-99);
		}
		return url;
	}
	
	/**
	 * 删除文件夹
	 * @param dir 要删除的文件
	 * @throws 无
	 * @return 无
	 */
    private static boolean deleteDir(File dir) 
    {
        if (dir.isDirectory()) 
        {
            String[] children = dir.list();
            //递归删除目录中的子目录下
            for (int i=0; i<children.length; i++) 
            {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) 
                {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }
}
