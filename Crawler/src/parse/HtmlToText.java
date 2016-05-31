package parse;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * 网页的纯文本提取
 * @version 1.0
 * @since version 1.0
 * @author 陈超逸
 */
public class HtmlToText 
{	 
	/**
	 * 过滤掉网页中的各种标签
	 * @param htmlcode 网页的HTML代码
	 * @return 网页的text纯文本
	 * @throws 无
	 */
	public static String Html2Text(String htmlcode)
	{
		 //输入和输出（htmlcode输入，textStr输出）
	     String htmlStr = htmlcode;
	     String textStr ="";
	     //定义过滤得正则表达式并过滤
	     Pattern p_noscript,p_script,p_style,p_html,p_escape,p_num;
	     Matcher m_noscript,m_script,m_style,m_html,m_escape,m_num;
	     try
	     {
	    	 //定义script,style,HTML的正则表达式
	    	 String regEx_noscript = "<[\\s]*?noscript[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?noscript[\\s]*?>";
	    	 String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>"; 
	    	 String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>"; 
	    	 String regEx_html = "<[^>]+>";
	    	 String regEx_escape = "&[a-z0-9]+|&#[0-9a-z]+";
	    	 String regEx_num = "[0-9 \n\t]+";
	    	 //过滤noscript标签
	    	 p_noscript = Pattern.compile(regEx_noscript,Pattern.CASE_INSENSITIVE);
	    	 m_noscript = p_noscript.matcher(htmlStr);
	    	 htmlStr = m_noscript.replaceAll(""); 
	    	 //过滤script标签
	    	 p_script = Pattern.compile(regEx_script,Pattern.CASE_INSENSITIVE);
	    	 m_script = p_script.matcher(htmlStr);
	    	 htmlStr = m_script.replaceAll(""); 
	    	 //过滤style标签
	    	 p_style = Pattern.compile(regEx_style,Pattern.CASE_INSENSITIVE);
	    	 m_style = p_style.matcher(htmlStr);
	    	 htmlStr = m_style.replaceAll(""); 
	    	 //过滤html标签
	    	 p_html = Pattern.compile(regEx_html,Pattern.CASE_INSENSITIVE);
	    	 m_html = p_html.matcher(htmlStr);
	    	 htmlStr = m_html.replaceAll(""); 
	    	 //过滤转义字符
	    	 p_escape = Pattern.compile(regEx_escape,Pattern.CASE_INSENSITIVE);
	    	 m_escape = p_escape.matcher(htmlStr);
	    	 htmlStr = m_escape.replaceAll(""); 
	    	 //过滤数字
	    	 p_num = Pattern.compile(regEx_num,Pattern.CASE_INSENSITIVE);
	    	 m_num = p_num.matcher(htmlStr);
	    	 htmlStr = m_num.replaceAll(",");
	    	 textStr = htmlStr;
	    }
	    catch(Exception e)
	    {}
	     return textStr;//返回文本字符串
	 } 
}
