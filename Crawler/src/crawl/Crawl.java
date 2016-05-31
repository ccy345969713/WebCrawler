package crawl;

import queue.*;
import parse.*;
import store.*;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

import com.hankcs.hanlp.HanLP;

/**
 * 本类的功能是实现一个爬虫的底层
 * @version 1.0
 * @since version 1.0
 * @author 陈超逸
 */
public class Crawl 
{  
	/**起始URL*/
	private String[] seeds = {"http://sports.sohu.com","http://sports.163.com","http://sports.ifeng.com"};
	/**爬取的深度*/
	private int layers = 2;
	/**多线程数目*/
	private int threadsnum = 1;
	/**采集间隔时间*/
	private int threadsblank = 0;
	/**网络重连次数*/
	private int netconnum = 3;
	/**网络连接超时*/
	private int netovertime = 5000;
	/**网络读取超时*/
	private int netrequestovertime = 5000;
	/**URL过滤器*/
	private LinkFilter filter = new LinkFilter();
	/**文件保存的路径*/
	private String fileroot = "C:\\spider\\sd\\";
	/**是否将网页保存为文件*/
	private boolean setsave = true;
	/**保存其他类型的文件*/
	private boolean js = false;
	private boolean css = false;
	private boolean img = false;
	private boolean xml = false;
	private boolean word = false;
	private boolean pdf = false;
	private boolean ppt = false;
	private boolean excel = false;
	/**是否使用数据库*/
	private boolean setdb = true;
	/**主机的地址*/
	private String HOSTNAME = "127.0.0.1";
	/**端口号*/
	private String PORT = "3306";
	/**数据库名*/
	private String DBNAME = "graduationproject";
	/**表名*/
	private String TABLENAME = "news";  
	/**用户名*/
	private String USERID = "ccy";
	/**口令*/
	private String PASSWORD = "abc1234";
	/**关键词填充*/
	private boolean addkeywords = true;
	/**摘要填充*/
	private boolean adddescription = true;
	/**正文填充*/
	private boolean addcontent = true;
	/**是否使用代理*/
	private boolean setproxy = false;
	/**代理IP*/
	private String proxyIP = "";
	/**代理端口*/
	private String proxyport = "";
	/**代理用户名*/
	private String proxyusername = "";
	/**代理密码*/
	private String proxyuserpsw = "";
	/**是否执行数据更新*/
	private boolean renew = false;
	/**数据更新周期(分钟)*/
	private int period = 20;
	/**词典跟路径*/
	private String dicroot = "C:/";
	/**配置URL对列*/
	private URLQueue queue = null;
	/**线程池*/
	static ExecutorService pool = null;
	/**计时器*/
	static Timer timer = null;
	/**标志位*/
	static boolean isStop = false;
	
	/**构造方法
	 * @param  无
	 * @return 无
	 * @throws 无
	 */
	public Crawl()
	{
		//为了查询
		StoreToDB.setParameter(HOSTNAME, PORT, DBNAME, TABLENAME, USERID, PASSWORD,fileroot,
		addkeywords,adddescription,addcontent);
	}
	
	/**设置种子
	 * @param aseeds 过滤器
	 * @return 无
	 * @throws 无
	 */
	public void setSeeds(String[] aseeds)
	{
		seeds = aseeds;	
	}
	public String getSeeds()
	{
		if (seeds != null)
		{
			String urlseed = "";
			for(String a:seeds)
			{
				urlseed = urlseed + a+ ";";
			}
			urlseed = urlseed.substring(0, urlseed.length()-1);
			return urlseed;
		}
		else
		{
			return "";
		}
	}
	
	/**设置层数
	 * @param alayers 层数
	 * @return 无
	 * @throws 无
	 */
	public void setLayer(int alayers)
	{
		layers = alayers;	
	}
	public String getLayer()
	{
		return ""+layers;
	}
	
	/**设置过滤器
	 * @param afilter 过滤器
	 * @return 无
	 * @throws 无
	 */
	public void setFilter(LinkFilter afilter)
	{
		filter = afilter;
	}
	
	/**
	 * 设置线程数目
	 * @param athreadsnum 线程数
	 * @return 无
	 * @throws 无
	 */
	public void setThreadsnum(int athreadsnum)
	{
		threadsnum = athreadsnum;
	}
	public String getThreadsnum()
	{
		return ""+threadsnum;
	}
	
	/**
	 * 设置采集间隔
	 * @param athreadsblank 采集间隔
	 * @return 无
	 * @throws 无
	 */
	public void setThreadsblank(int athreadsblank)
	{
		threadsblank = athreadsblank;
	}
	public String getThreadsblank()
	{
		return ""+threadsblank;
	}
	
	/**
	 * 设置连接次数
	 * @param anetconnum 连接次数
	 * @return 无
	 * @throws 无
	 */
	public void setNetconnum(int anetconnum)
	{
		netconnum = anetconnum;
	}
	public String getNetconnum()
	{
		return ""+netconnum;
	}
	
	/**
	 * 设置连接超时
	 * @param anetovertime 连接超时
	 * @return 无
	 * @throws 无
	 */
	public void setNetovertime(int anetovertime)
	{
		netovertime = anetovertime;
	}
	public String getNetovertime()
	{
		return ""+netovertime;
	}
	
	/**
	 * 设置请求超时
	 * @param anetrequestovertime 请求超时
	 * @return 无
	 * @throws 无
	 */
	public void setNetrequestovertime(int anetrequestovertime)
	{
		netrequestovertime = anetrequestovertime;
	}
	public String getNetrequestovertime()
	{
		return ""+netrequestovertime;
	}
	
	/**
	 * 设置文件保存地址
	 * @param afileroot 根目录
	 * @return 无
	 * @throws 无
	 */
	public void setFileroot(String afileroot)
	{
		fileroot = afileroot;
	}
	public String getFileroot()
	{
		return ""+fileroot;
	}
	
	/**
	 * 设置保存文件的类型
	 * @param ajs 是否保存js文件
	 * @param acss 是否保存css文件
	 * @param aimg 是否保存img文件
	 * @param axml 是否保存xml文件
	 * @param aword 是否保存word文件
	 * @param apdf 是否保存pdf文件
	 * @param appt 是否保存ppt文件
	 * @param aexcel 是否保存excel文件
	 * @return 无 
	 * @throws 无
	 */
	public void setSaveType(boolean ajs,boolean acss,
			boolean aimg,boolean axml,boolean aword,
			boolean apdf,boolean appt,boolean aexcel)
	{
		js = ajs;
		css = acss;
		img = aimg;
		xml = axml;
		word = aword;
		pdf = apdf;
		ppt = appt;
		excel = aexcel;
	}
	public void setSave(boolean asetsave)
	{
		setsave = asetsave;
	}
	public boolean getSave()
	{
		return setsave;
	}
	public Boolean[] getSaveType()
	{
		Boolean[] savetype = new Boolean[8];
		savetype[0] = js;
		savetype[1] = css;
		savetype[2] = img;
		savetype[3] = xml;
		savetype[4] = word;
		savetype[5] = pdf;
		savetype[6] = ppt;
		savetype[7] = excel;
		return savetype;
	}

	/**
	 * 设置代理参数
	 * @param proxyIP 代理IP
	 * @param proxyport 代理端口
	 * @param proxyusername 代理名称
	 * @param proxyuserpsw 代理口令
	 * @return 无
	 * @throws 无
	 */
	public void setProxy(boolean asetproxy,String aproxyIP,String aproxyport
			,String aproxyusername,String aproxyuserpsw)
	{
		setproxy = asetproxy;
		proxyIP = aproxyIP;
		proxyport = aproxyport;
		proxyusername = aproxyusername;
		proxyuserpsw = aproxyuserpsw;
		if (setproxy)
		{
			new UseProxy(proxyIP,proxyport,proxyusername,proxyuserpsw);
		}
		else
		{
			new UseProxy();
		}
	}
	public boolean getProxy()
	{
		return setproxy;
	}
	public String[] getProxyInfo()
	{
		String[] info = new String[4];
		info[0] = proxyIP;
		info[1] = proxyport;
		info[2] = proxyusername;
		info[3] = proxyuserpsw;
		return info;
	}

	/**
	 * 设置数据库参数
	 * @param ahostname 主机名
	 * @param port 端口号 
	 * @param dbname 数据库名称
	 * @param tablename 表名
	 * @param userid 用户名
	 * @param password 用户口令
	 * @return 无
	 * @throws 无
	 */
	public void setDB(boolean asetdb,String hostname, String port, String dbname,
			                        String tablename, String userid, String password)
	{
		setdb = asetdb;
		HOSTNAME = hostname;
		PORT = port;
		DBNAME = dbname;
		TABLENAME = tablename;
		USERID = userid;
		PASSWORD = password;
		StoreToDB.setParameter(HOSTNAME, PORT, DBNAME, TABLENAME, USERID, PASSWORD,fileroot,
				   addkeywords,adddescription,addcontent);
	}
	public void setDBFunc(boolean aaddkeywords,boolean aadddescription,boolean aaddcontent)
	{
		addkeywords = aaddkeywords;
		adddescription = aadddescription;
		addcontent = aaddcontent;
	}
	public boolean getDB()
	{
		return setdb;
	}
	public String[] getDBInfo()
	{
		String[] info = new String[6];
		info[0] = HOSTNAME;
		info[1] = PORT;
		info[2] = DBNAME;
		info[3] = TABLENAME;
		info[4] = USERID;
		info[5] = PASSWORD;
		return info;
	}
	public Boolean[] getDBFunc()
	{
		Boolean[] info = new Boolean[3];
		info[0] = addkeywords;
		info[1] = adddescription;
		info[2] = addcontent;
		return info;
	}
	
	/**
	 * 设置更新的周期
	 * @param aperiod 更新周期
	 * @return 无
	 * @throws 无
	 */
	public void setPeriod(boolean arenew,int aperiod)
	{
		renew = arenew;
		period = aperiod;
	}
	public void setPeriod(boolean arenew)
	{
		renew = arenew;
	}
	public boolean getPeriod()
	{
		return renew;
	}
	public String getPeriodTime()
	{
		return ""+period;
	}

	/**
	 * 设置词典路径
	 * @param aDicroot 词典路径
	 * @return 无
	 * @throws 无
	 */
	public void setDicroot(String adicroot)
	{
		dicroot = adicroot;
	}
	public String getDicroot()
	{
		return dicroot;
	}
	
	/**
	 *根据各项配置参数开始爬取数据
	 *@param 无
	 *@return 无
	 *@throws 无
	 */
	public void start()
	{
		//配置各项参数
		queue = new URLQueue();
		HanLP.refresh(dicroot);
		LinkExtract.setParameter(filter,layers);
		Loader.setParameter(netconnum,netovertime,netrequestovertime);
		StoreToFile.setParameter(fileroot,js,css,img,xml,word,pdf,ppt,excel,
								netconnum,netovertime,netrequestovertime);
		StoreToDB.setParameter(HOSTNAME, PORT, DBNAME, TABLENAME, USERID, PASSWORD,fileroot,
							   addkeywords,adddescription,addcontent);
		RenewToDB.setParameter(HOSTNAME, PORT, DBNAME, TABLENAME, USERID, PASSWORD);
		Renew.setParameter(HOSTNAME, PORT, DBNAME, TABLENAME, USERID, PASSWORD, 
				   			netconnum, netovertime, netrequestovertime, fileroot,setdb,setsave);
		//创建起始目录的Url对象，并加入队列
		for (int i=0;i<seeds.length;i++)
		{
			queue.addUnvisited(new Url(seeds[i],1));
		}
		//设置数据自动更新
		if (renew)
		{
			timer = new Timer();
			timer.schedule(new renewTask(), period*60*1000, period*60*1000);
		}
		//创建爬取线程
		pool = Executors.newFixedThreadPool(threadsnum);
		Url forvisit;
		while((forvisit = queue.getUnvisited()) != null)
		{
			try
			{
				pool.execute(new Multicrawl(forvisit));
			}
			catch(Exception e)
			{}
		}
		pool.shutdown();
		isStop = true;
	}
	
	/**
	 *中止爬取数据
	 *@param 无
	 *@return 无
	 *@throws 无
	 */
	public void stop()
	{
		try
		{
			if (pool != null&&!pool.isTerminated())
			{
				pool.shutdownNow();
				while(!pool.isTerminated())
				{
					continue;
				}
			}
			if(timer != null)
			{
				timer.cancel();
			}
		}
		catch(Exception e)
		{}
	}
	
	/**
	 *初始化数据
	 *@param 无
	 *@return 无
	 *@throws 无
	 */
	public void ini()
	{
		isStop = false;
	}
	
	/**
	 *是否停止爬取数据
	 *@param 无
	 *@return 是否停止
	 * @throws InterruptedException 
	 *@throws 无
	 */
	public static boolean isTerminate()
	{
		try
		{
			if (isStop&&pool != null&&pool.isTerminated()&&timer == null)
			{
				return true;
			}
			else
			{
				Thread.sleep(100);
				return false;
			}
		}
		catch(Exception e)
		{
			return false;
		}
	}
	
	/**
	 * 本类的功能是创建一个爬虫线程
	 * @version 1.0
	 * @since version 1.0
	 * @author 陈超逸
	 */
	private class Multicrawl extends Thread
	{
		/**待爬取的URL*/
		private Url forvisit = null;
		
		/**
		 * 设置待爬取的URL
		 * @param aforvisit 待爬取的URL
		 * @return 无
		 * @throws 无
		 */
		public Multicrawl(Url aforvisit)
		{
			forvisit = aforvisit;
		}
		
		/**
		 * 执行爬取的线程
		 * @param 无
		 * @return 无
		 * @throws 无
		 */
		public void run()
		{
			//循环爬取网页
			String htmlcode = null;
			Set<Url> set = null;
			try
			{
				//下载网页
				htmlcode = Loader.downLoadFile(forvisit);
				//抽取URL链接
				set = LinkExtract.extracLinks(htmlcode,forvisit);
				if (setsave)
				{
					//保存网页至文件
					StoreToFile.saveHtml(htmlcode, forvisit);
				}
				if (setdb)
				{
					//保存网页至数据库
					StoreToDB.saveHtml(htmlcode, forvisit);
				}
				//新链接入队
				queue.addUnvisited(set);
				
			}
			catch(InterruptedException e)
			{
				queue.err();
			}
			catch(MyException e)
			{
				System.err.println(e.getMessage());
				queue.err();
			}
			catch(Exception e)
			{
				System.err.println("Crawl错误---"+forvisit+"---发生未知错误");
				queue.err();
			}
			catch(Error e)
			{
				System.err.println("内存溢出！");
				queue.err();
			}
			//采集时间间隔
			try 
			{
				Thread.sleep(threadsblank);
			} 
			catch (InterruptedException e) 
			{}
		}

	}
	
	/**
	 * 本类的功能是启动更新
	 * @version 1.0
	 * @since version 1.0
	 * @author 陈超逸
	 */
	private class renewTask extends TimerTask
	{
		public void run() 
		{
			if (pool.isTerminated())
			{
				try
				{
					//重置设置
					Renew.init();
					//创建更新线程
					pool = Executors.newFixedThreadPool(threadsnum);
					String[] urlTime;
					while((urlTime = Renew.getUrl()) != null)
					{
						pool.execute(new Renewcrawl(urlTime));
					}
					pool.shutdown();
				}
				catch(MyException e)
				{
					System.err.println(e.getMessage());
				}
			}
			else
			{
				return;
			}
		}
	}

	/**
	 * 本类的功能是创建更新线程
	 * @version 1.0
	 * @since version 1.0
	 * @author 陈超逸
	 */
	private class Renewcrawl extends Thread
	{
		/**待更新的URL*/
		private String[] forvisit = null;
		
		/**
		 * 设置待更新的URL
		 * @param aforvisit 待爬取的URL
		 * @return 无
		 * @throws 无
		 */
		public Renewcrawl(String[] aforvisit)
		{
			forvisit = aforvisit;
		}
		
		/**
		 * 执行更新的线程
		 * @param 无
		 * @return 无
		 * @throws 无
		 */
		public void run()
		{
			try
			{
				Renew.renew(forvisit);
			}
			catch(InterruptedException e)
			{}
			catch(MyException e)
			{
				System.err.println(e.getMessage());
			}
			catch(Exception e)
			{
				System.err.println("Crawl错误---"+forvisit+"---发生未知错误");
			}
			catch(Error e)
			{
				System.err.println("内存溢出！");
			}
		}
	}
}
