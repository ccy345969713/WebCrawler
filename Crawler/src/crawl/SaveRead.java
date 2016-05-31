package crawl;

import parse.*;
import javax.swing.JTextArea;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.BufferedInputStream;

/**
 * 配置的读取和保存
 * @version 1.0
 * @since version 1.0
 * @author 陈超逸
 */
public class SaveRead 
{
	/**
	 * 将属性保存到配置文件
	 * @param spider 爬虫类
	 * @param notice 显示框
	 * @param path 保存路径
	 * @return 无
	 * @throws 无
	 */
	public static void save(Crawl spider,JTextArea notice,String path)
	{
		try 
		{
			//创建文件的输出流
			Properties prop = new Properties();
			FileOutputStream outputstream = new FileOutputStream(path, true); 
			//将属性写入文件的输出流
			prop.setProperty("起始URL", spider.getSeeds());
			prop.setProperty("爬取深度", spider.getLayer());
			prop.setProperty("是否更新", yesORno(spider.getPeriod()));
			prop.setProperty("更新周期", spider.getPeriodTime());
			prop.setProperty("是否使用代理", yesORno(spider.getProxy()));
			prop.setProperty("代理IP", spider.getProxyInfo()[0]);
			prop.setProperty("代理端口", spider.getProxyInfo()[1]);
			prop.setProperty("代理用户名称", spider.getProxyInfo()[2]);
			prop.setProperty("代理用户密码", spider.getProxyInfo()[3]);
			prop.setProperty("线程间隔时间", spider.getThreadsblank());
			prop.setProperty("线程数目", spider.getThreadsnum());
			prop.setProperty("网络超时", spider.getNetovertime());
			prop.setProperty("请求超时", spider.getNetrequestovertime());
			prop.setProperty("重拨次数", spider.getNetconnum());
			prop.setProperty("过滤器包含", LinkFilter.getIncludeProperty());
			prop.setProperty("过滤器不含", LinkFilter.getExcludeProperty());
			prop.setProperty("是否保存到文件", yesORno(spider.getSave()));
			prop.setProperty("保存地址", spider.getFileroot());
			prop.setProperty("保存javascript", yesORno(spider.getSaveType()[0]));
			prop.setProperty("保存css", yesORno(spider.getSaveType()[1]));
			prop.setProperty("保存图片", yesORno(spider.getSaveType()[2]));
			prop.setProperty("保存xml", yesORno(spider.getSaveType()[3]));
			prop.setProperty("保存word", yesORno(spider.getSaveType()[4]));
			prop.setProperty("保存pdf", yesORno(spider.getSaveType()[5]));
			prop.setProperty("保存ppt", yesORno(spider.getSaveType()[6]));
			prop.setProperty("保存excel", yesORno(spider.getSaveType()[7]));
			prop.setProperty("是否保存到数据库", yesORno(spider.getDB()));
			prop.setProperty("数据库主机名", spider.getDBInfo()[0]);
			prop.setProperty("数据库端口号", spider.getDBInfo()[1]);
			prop.setProperty("数据库名", spider.getDBInfo()[2]);
			prop.setProperty("数据库表名", spider.getDBInfo()[3]);
			prop.setProperty("数据库用户名", spider.getDBInfo()[4]);
			prop.setProperty("数据库密码", spider.getDBInfo()[5]);
			prop.setProperty("是否关键词智能填充", yesORno(spider.getDBFunc()[0]));
			prop.setProperty("是否摘要智能填充", yesORno(spider.getDBFunc()[1]));
			prop.setProperty("是否正文智能填充", yesORno(spider.getDBFunc()[2]));
			prop.setProperty("词典位置", spider.getDicroot());
			//关闭文件的输出流
			prop.storeToXML(outputstream, "爬虫配置文件");
			outputstream.close();
			//显示消息
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			notice.append("\n保存配置文件成功:"+df.format(new Date())+"\n");
		} 
		catch (Exception e) 
		{
			//显示消息
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			notice.append("\n保存配置文件失败:"+df.format(new Date())+"\n");
		}
	}
	
	/**
	 * 从配置文件读取属性
	 * @param spider 爬虫类
	 * @param notice 显示框
	 * @param path 文件路径
	 * @return 无
	 * @throws 无
	 */
	public static void read(Crawl spider,JTextArea notice,String path)
	{
		try 
		{
			//创建文件的输入流
			Properties prop = new Properties();
			InputStream in = new BufferedInputStream (new FileInputStream(path));
			prop.loadFromXML(in);
			//从输入流读文件的属性
			spider.setSeeds(prop.getProperty("起始URL").split(";"));
			spider.setLayer(Integer.parseInt(prop.getProperty("爬取深度")));
			spider.setPeriod(shiORfou(prop.getProperty("是否更新")), Integer.parseInt(prop.getProperty("更新周期")));
			spider.setProxy(shiORfou(prop.getProperty("是否使用代理")), prop.getProperty("代理IP"), 
			prop.getProperty("代理端口"), prop.getProperty("代理用户名称"), prop.getProperty("代理用户密码"));
			spider.setThreadsblank(Integer.parseInt(prop.getProperty("线程间隔时间")));
			spider.setThreadsnum(Integer.parseInt(prop.getProperty("线程数目")));
			spider.setNetovertime(Integer.parseInt(prop.getProperty("网络超时")));
			spider.setNetrequestovertime(Integer.parseInt(prop.getProperty("请求超时")));
			spider.setNetconnum(Integer.parseInt(prop.getProperty("重拨次数")));
			LinkFilter.setIncludeProperty(prop.getProperty("过滤器包含"));
			LinkFilter.setExcludeProperty(prop.getProperty("过滤器不含"));
			spider.setSave(shiORfou(prop.getProperty("是否保存到文件")));
			spider.setFileroot(prop.getProperty("保存地址"));
			spider.setSaveType(shiORfou(prop.getProperty("保存javascript")), shiORfou(prop.getProperty("保存css")),
			shiORfou(prop.getProperty("保存图片")), shiORfou(prop.getProperty("保存xml")), shiORfou(prop.getProperty("保存word")),
			shiORfou(prop.getProperty("保存pdf")), shiORfou(prop.getProperty("保存ppt")), shiORfou(prop.getProperty("保存excel")));
			spider.setDB(shiORfou(prop.getProperty("是否保存到数据库")), prop.getProperty("数据库主机名"), prop.getProperty("数据库端口号"), 
			prop.getProperty("数据库名"), prop.getProperty("数据库表名"), prop.getProperty("数据库用户名"), prop.getProperty("数据库密码"));
			spider.setDBFunc(shiORfou(prop.getProperty("是否关键词智能填充")), shiORfou(prop.getProperty("是否摘要智能填充")),
			shiORfou(prop.getProperty("是否正文智能填充")));
			spider.setDicroot(prop.getProperty("词典位置"));
			//关闭文件的输入流
			in.close();
			//显示消息
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			notice.append("\n读取配置文件成功:"+df.format(new Date())+"\n");
		} 
		catch (Exception e) 
		{
			//显示消息
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			notice.append("\n读取配置文件失败:"+df.format(new Date())+"\n");
		}

	}
	
	/**
	 * 将boolean转换为字符
	 * @param judge true或false
	 * @return 无
	 * @throws 无
	 */
	private static String yesORno(boolean judge)
	{
		if (judge == true)
		{
			return "是";
		}
		else
		{
			return "否";
		}
	}
	
	/**
	 * 将字符转换为boolean
	 * @param judge 是或否
	 * @return 无
	 * @throws 无
	 */
	private static boolean shiORfou(String judge)
	{
		if (judge.equals("是"))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}
