package queue;

import java.util.Set;
import java.util.Queue;
import java.util.HashSet;
import java.util.LinkedList;

/**
 * 本类将创建两个队列(线程安全)
 * 1、已访问的URL集合
 * 2、待访问的URL队列
 * 并提供URL的get和add方法
 * @version 1.0
 * @since version 1.0
 * @author 陈超逸
 */
public class URLQueue 
{
	/**已访问的URL集合*/
	private Set<String> visitedUrl = new HashSet<>();
	/**待访问的URL队列*/
	private Queue<Url> unvisited = new LinkedList<>();
	private Set<String> unvisitedUrl = new HashSet<>();
	/**当前已调用get方法但未调用add方法的线程数目**/
	private int onGoingThreadNum = 0;
	
     /**
      * 向待访问队列add新URL(线程安全)
      * 实现URL的去重
      * @param url 待添加的URL
      * @param set 待添加的URL集合
      * @return 无
      * @throws 无
      */
	 public synchronized void addUnvisited(Set<Url> set) 
	 {
		 if (set != null)
		 {
			for (Url url : set)
			{
				//实现去重
				if (url != null && !url.url.trim().equals("") && !visitedUrl.contains(url.url)&& !unvisitedUrl.contains(url.url))
				{
					unvisited.add(url);
					unvisitedUrl.add(url.url);
				}   
			}
		 }
		 onGoingThreadNum--;
	 }
	 public synchronized void addUnvisited(Url url) 
	 {
		if (url != null && !url.url.trim().equals("") && !visitedUrl.contains(url.url)&& !unvisitedUrl.contains(url.url))
		{
			unvisited.add(url);
			unvisitedUrl.add(url.url);
		}   
	 }
	 
	 /**
	  * 从待访问队列get的新URL(线程安全)
	  * @param 无
	  * @return 待访问的URL
	  * @throws 无
	  */
	 public synchronized Url getUnvisited() 
	 {
		 //等待线程add新URL
		 while(unvisited.isEmpty() && onGoingThreadNum != 0)
		 {
			 try
			 {
				 this.wait(100);
			 }
			 catch(Exception e)
			 {
				 continue;
			 }
		 }
		 //获取待访问队列中的新URL
		 Url out = unvisited.poll();
		 //将取出的新URL加入已访问URL集合
		 if (out != null)
		 {
			 unvisitedUrl.remove(out.url);
			 visitedUrl.add(out.url);
			 onGoingThreadNum++;
		 }
		 return out;
	 }
	 
	 /**
	  * 当前已调用get方法但未调用add方法的线程发生错误(线程安全)
	  * @param 无
	  * @return 无
	  * @throws 无
	  */
	 public synchronized void err() 
	 {
		 onGoingThreadNum--;
	 }
}
