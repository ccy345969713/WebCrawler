package store;

import queue.*;
import crawl.*;
import parse.*;
import java.sql.Timestamp;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

/**
 * 本类的功能是将网页的结构化信息存入数据库
 * @version 1.0
 * @since version 1.0
 * @author 陈超逸
 */
public class RenewToDB 
{
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
	
	/**
	 * 设置各项参数
	 * @param hostname 主机名
	 * @param port 端口号 
	 * @param dbname 数据库名称
	 * @param tablename 表名
	 * @param userid 用户名
	 * @param password 用户口令
	 * @return 无
	 * @throws 无
	 */
	public static void setParameter(String hostname, String port, String dbname,
			                        String tablename, String userid, String password)
	{
		HOSTNAME = hostname;
		PORT = port;
		DBNAME = dbname;
		TABLENAME = tablename;
		USERID = userid;
		PASSWORD = password;
	}
	
	/**
	 * 更新网页的结构化信息并存入数据库
	 * @param htmlcode 网页的HTML代码
	 * @param url 网页的URL
	 * @throws MyException 
	 * @throws InterruptedException 
	 */
	public static void renewHtml(String htmlcode,Url url) throws MyException, InterruptedException
	{
		//1、连接数据库
		String driver = "com.mysql.jdbc.Driver";
		String dbcon = "jdbc:mysql://"+HOSTNAME+":"+PORT+"/"+DBNAME;
		Connection conn = null;
		PreparedStatement ps = null;
		try
		{
			Class.forName(driver);
			conn = DriverManager.getConnection(dbcon, USERID, PASSWORD);
			ps = conn.prepareStatement("UPDATE "+TABLENAME+" SET time=?,title=?,keywords=?,description=?,mainbody=? WHERE url='"+url.url+"'");
		//2、获得抽取网页的结果
			String[] content = HtmlExtract.extractHtml(htmlcode, url);
			try
			{
				if (content!=null)
				{
					ps.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
					ps.setString(2,content[3]);
					ps.setString(3,content[4]);
					ps.setString(4,content[5]);
					ps.setString(5,content[6]);
		//3、将获取的结果存入数据库
					ps.executeUpdate();
				}
			}
			catch(Exception e)
			{
				throw new MyException("RenewToDB错误---"+url.url+"---SQL语句错误");
			}
		}
		catch(InterruptedException e)
		{
			throw e;
		}
		catch(MyException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			throw new MyException("RenewToDB错误---"+url.url+"---数据库连接错误");
		}
		finally
		{
			if (ps != null)
			{
				try 
				{
					ps.close();
				} 
				catch (Exception e) 
				{
					throw new MyException("RenewToDB错误---"+url.url+"---数据库连接错误");
				}
			}
			if (conn != null)
			{
				try 
				{
					conn.close();
				} 
				catch (Exception e) 
				{
					throw new MyException("RenewToDB错误---"+url.url+"---数据库连接错误");
				}
			}
		}
	}
}
