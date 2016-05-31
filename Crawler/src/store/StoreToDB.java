package store;

import queue.*;
import crawl.*;
import parse.*;
import java.sql.Types;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.ArrayList;

/**
 * 本类的功能是将网页的结构化信息存入数据库
 * @version 1.0
 * @since version 1.0
 * @author 陈超逸
 */
public class StoreToDB 
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
	 * @param afileroot 保存文件的根目录
	 * @return 无
	 * @throws 无
	 */
	public static void setParameter(String hostname, String port, String dbname,
			                        String tablename, String userid, String password,String afileroot,
			                        boolean addkeywords,boolean adddescription,boolean addcontent)
	{
		HOSTNAME = hostname;
		PORT = port;
		DBNAME = dbname;
		TABLENAME = tablename;
		USERID = userid;
		PASSWORD = password;
		HtmlExtract.setParameter(afileroot,addkeywords,adddescription,addcontent);
	}
	
	/**
	 * 将网页的结构化信息存入数据库
	 * @param htmlcode 网页的HTML代码
	 * @param url 网页的URL
	 * @throws MyException 
	 * @throws InterruptedException 
	 */
	public static void saveHtml(String htmlcode,Url url) throws MyException, InterruptedException
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
			ps = conn.prepareStatement("INSERT INTO "+TABLENAME+" VALUES(?,?,?,?,?,?,?,?,?)");
		//2、获得抽取网页的结果
			String[] content = HtmlExtract.extractHtml(htmlcode, url);
			try
			{
				if (content!=null)
				{
					ps.setNull(1, Types.BIGINT);
					ps.setString(2,content[1]);
					ps.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
					ps.setString(4,content[3]);
					ps.setString(5,content[4]);
					ps.setString(6,content[5]);
					ps.setString(7,content[6]);
					ps.setString(8,content[7]);
					ps.setString(9,content[8]);
		//3、将获取的结果存入数据库
					ps.executeUpdate();
				}
			}
			catch(Exception e)
			{
				throw new MyException("StoreToDB错误---"+url.url+"---SQL语句错误");
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
			throw new MyException("StoreToDB错误---"+url.url+"---数据库连接错误");
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
					throw new MyException("StoreToDB错误---"+url.url+"---数据库连接错误");
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
					throw new MyException("StoreToDB错误---"+url.url+"---数据库连接错误");
				}
			}
		}
	}
	
	public static String[] queryHtml(String searchWord) throws MyException
	{
		//1、连接数据库
		String driver = "com.mysql.jdbc.Driver";
		String dbcon = "jdbc:mysql://"+HOSTNAME+":"+PORT+"/"+DBNAME;
		Connection conn = null;
		Statement statement = null;
		ResultSet rs = null;
		try 
		{
			//连接数据库
			Class.forName(driver);
			conn = DriverManager.getConnection(dbcon, USERID, PASSWORD);
			statement = conn.createStatement();
		//2、查询数据库并获得结果
			String sql = "SELECT url,time,keywords,htmlPosition,srcPosition FROM "+TABLENAME
						+" WHERE mainbody LIKE '%"+searchWord.trim()+"%'";
			rs= statement.executeQuery(sql);
		//3、取出结果并存入数组
			ArrayList<String> arr = new ArrayList<>();
			while (rs.next())
			{
				arr.add(rs.getString("url"));
				arr.add(rs.getString("time"));
				arr.add(rs.getString("keywords"));
				arr.add(rs.getString("htmlPosition"));
				arr.add(rs.getString("srcPosition"));
			}
			String[] str = null;
			if (arr.size()>0)
			{
				str = arr.toArray(new String[]{});
			}
		//3、返回结果
			return str;
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
			throw new MyException("查询失败"); 
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
					throw new MyException("查询失败");
				}
			}
		}		
	}
}
