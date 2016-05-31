package crawl;

import java.util.Properties;
import java.net.Authenticator;
import java.net.PasswordAuthentication;

/**
 * 本类的功能是创建代理
 * @version 1.0
 * @since version 1.0
 * @author 陈超逸
 */
public class UseProxy 
{
	/**
	 * 不使用与使用代理的情况
	 * @param proxyIP 代理IP
	 * @param proxyport 代理端口
	 * @param proxyusername 代理名称
	 * @param proxyuserpsw 代理口令
	 * @return 无
	 * @throws 无
	 */
	public UseProxy()
	{
		Properties prop = System.getProperties();
		//设置http是否要使用代理服务器        
		prop.setProperty("http.proxySet", "false");   
		//设置https是否要使用代理服务器        
		prop.setProperty("https.proxySet", "false"); 
	}
	public UseProxy(String proxyIP,String proxyport
	,String proxyusername,String proxyuserpsw)
	{
		Properties prop = System.getProperties();
		//设置http是否要使用代理服务器        
		prop.setProperty("http.proxySet", "true");   
		//设置http访问要使用的代理服务器的地址        
		prop.setProperty("http.proxyHost",proxyIP);
	    //设置http访问要使用的代理服务器的端口        
		prop.setProperty("http.proxyPort",proxyport);
		//设置https是否要使用代理服务器        
		prop.setProperty("https.proxySet", "true"); 
	    //设置https访问要使用的代理服务器的地址           
		prop.setProperty("https.proxyHost",proxyIP);
		//设置https访问要使用的代理服务器的端口        
		prop.setProperty("https.proxyPort",proxyport);
		// 设置登陆到代理服务器的用户名和密码        
		Authenticator.setDefault(new MyAuthenticator(proxyusername,proxyuserpsw));
	}
	
	/**
	* 本类的功能是创建鉴权类
 	* @version 1.0
 	* @since version 1.0
 	* @author 陈超逸
	 */
	private class MyAuthenticator extends Authenticator 
	{
		/**用户名*/
		private String user = "";
		/**密码*/
		private String password = "";
		
		public MyAuthenticator(String user, String password) 
		{            
			this.user = user;            
			this.password = password;        
		}        
		   
		protected PasswordAuthentication getPasswordAuthentication() 
		{            
			return new PasswordAuthentication(user, password.toCharArray());        
		}    
	}
}
