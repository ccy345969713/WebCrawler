package crawl;

/**
 * 本类的功能是创建一个异常类
 * @version 1.0
 * @since version 1.0
 * @author 陈超逸
 */
@SuppressWarnings("serial")
public class MyException extends Exception
{
	/**
	 * 构造方法，初始化异常类
	 * @param 无
	 * @return 无
	 * @throws 无
	 */
	public MyException()
	{
		super();
	}
	
	/**
	 * 构造方法，初始化异常类
	 * @param err 异常信息
	 * @return 无
	 * @throws 无
	 */
	public MyException(String err)
	{
		super(err);
	}
}
