package crawl;

import java.io.File;
import java.util.Date;
import java.awt.Font;
import java.awt.Color;
import java.awt.Container;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JDialog;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.JFileChooser;
import javax.swing.BorderFactory;
import javax.swing.JPasswordField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.text.SimpleDateFormat;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import parse.LinkFilter;
import store.StoreToDB;

public class UserInterface 
{
	/**图形界面的框架*/
	JFrame frame = null;
	/**底层爬虫对象*/
	Crawl spider = new Crawl();
	/**文本框*/
	JTextArea notice;
	/**状态栏*/
	JLabel state;
	/**主界面按钮*/
	JButton basicset;
	JButton threadset;
	JButton networkset;
	JButton filterset;
	JButton storeset;
	JButton setsave;
	JButton setread;
	JButton start;
	JButton cancel;
	JButton search;
	/**查询线程*/
	Thread isStop;
	/**滚动条*/
	JScrollPane scroll;
	
	/**启动界面*/
	public UserInterface()
	{
		//设置界面的大框架
		frame = new JFrame("多功能信息采集系统");
		frame.setBounds(450, 150, 400, 400);
		frame.setResizable(false);
		frame.setVisible(false);
		Container c = frame.getContentPane();
		c.setLayout(null);
		//设置标题
		JLabel label = new JLabel("多功能信息采集系统",JLabel.CENTER);
		label.setFont(new Font("宋体",Font.BOLD,20));
		label.setBounds(100, 0, 200, 50);
		c.add(label);
		//设置文本框
		JLabel label3 = new JLabel("显示消息:",JLabel.LEFT);
		label3.setFont(new Font("宋体",Font.PLAIN,12));
		label3.setBounds(10, 130, 240, 15);
		c.add(label3);
		notice = new JTextArea(13,10);
		notice.setEditable(false);
		scroll = new JScrollPane(notice);
		scroll.setBounds(10, 150, 250, 200);
		scroll.doLayout();
		c.add(scroll);
		//功能区按钮
		JPanel mainFunction = new JPanel();
		mainFunction.setBorder(BorderFactory.createTitledBorder("功能区"));
		mainFunction.setBounds(10, 50, 250, 80);
		mainFunction.setLayout(null);
		c.add(mainFunction);
		start = new JButton("确认");
		start.setBounds(10, 30, 70, 30);
		mainFunction.add(start);
		cancel = new JButton("取消");
		cancel.setBounds(90, 30, 70, 30);
		cancel.setEnabled(false);
		mainFunction.add(cancel);
		search = new JButton("查询");
		search.setBounds(170, 30, 70, 30);
		mainFunction.add(search);
		//设置区按钮
		JPanel set = new JPanel();
		set.setBorder(BorderFactory.createTitledBorder("设置区"));
		set.setBounds(270, 50, 110, 300);
		set.setLayout(null);
		c.add(set);
		basicset = new JButton("基本设置");
		basicset.setBounds(10, 20, 90, 30);
		set.add(basicset);
		threadset = new JButton("线程设置");
		threadset.setBounds(10, 60, 90, 30);
		set.add(threadset);
		networkset = new JButton("网络设置");
		networkset.setBounds(10, 100, 90, 30);
		set.add(networkset);
		filterset = new JButton("过滤设置");
		filterset.setBounds(10, 140, 90, 30);
		set.add(filterset);
		storeset = new JButton("存储设置");
		storeset.setBounds(10, 180, 90, 30);
		set.add(storeset);
		JPanel saveset = new JPanel();
		saveset.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.GRAY));
		saveset.setBounds(5, 215, 100, 80);
		saveset.setLayout(null);
		set.add(saveset);
		setsave = new JButton("设置保存");
		setsave.setBounds(5, 5, 90, 30);
		saveset.add(setsave);
		setread = new JButton("设置读取");
		setread.setBounds(5, 45, 90, 30);
		saveset.add(setread);
		//状态栏
		state = new JLabel("准备就绪",JLabel.LEFT);
		state.setFont(new Font("宋体",Font.PLAIN,10));
		state.setBounds(10, 350, 370, 15);
		c.add(state);
		//设置监听器
		basicset.addActionListener(new BasicSet());
		threadset.addActionListener(new ThreadSet());
		networkset.addActionListener(new NetworkSet());
		filterset.addActionListener(new FilterSet());
		storeset.addActionListener(new StoreSet());
		start.addActionListener(new StartFunction());
		cancel.addActionListener(new cancelFunction());
		search.addActionListener(new Search());
		setread.addActionListener(new SetRead());
		setsave.addActionListener(new SetSave());
		//设置关闭动作
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//显示界面
		frame.setVisible(true);
	}
	
	/**基本设置*/
	private class BasicSet implements ActionListener
	{
		public void actionPerformed(ActionEvent event) 
		{
			final JDialog dialog = new JDialog(frame,"基本设置");
			dialog.setBounds(450, 205, 400, 310);
			dialog.setResizable(false);
			Container c = dialog.getContentPane();
			c.setLayout(null);
			//起始的URL
			JLabel initURL = new JLabel("起始URL",JLabel.CENTER);
			initURL.setFont(new Font("宋体",Font.PLAIN,15));
			initURL.setBounds(20, 10, 80, 20);
			c.add(initURL);
			final JTextField initURLtext = new JTextField(20);
			initURLtext.setBounds(100, 10, 250, 20);
			initURLtext.setText(spider.getSeeds());
			c.add(initURLtext);
			//爬取层数
			JLabel layer = new JLabel("采集深度",JLabel.CENTER);
			layer.setFont(new Font("宋体",Font.PLAIN,15));
			layer.setBounds(20, 40, 80, 20);
			c.add(layer);
		    final JComboBox<String> layertext =new JComboBox<>();
		    layertext.addItem("1");
		    layertext.addItem("2");
		    layertext.addItem("3");
		    layertext.addItem("4");
		    layertext.setBounds(100, 40, 60, 20);
		    layertext.setSelectedItem(spider.getLayer());
		    c.add(layertext);
		    //数据更新
			JLabel renew = new JLabel("是否更新",JLabel.CENTER);
			renew.setFont(new Font("宋体",Font.PLAIN,15));
			renew.setBounds(20, 70, 80, 20);
			c.add(renew);
			final JCheckBox isrenew = new JCheckBox();
			isrenew.setBounds(100, 70, 17, 20);
			isrenew.setSelected(spider.getPeriod());
			c.add(isrenew);
			JLabel circle = new JLabel("更新周期/min",JLabel.CENTER);
			circle.setFont(new Font("宋体",Font.PLAIN,15));
			circle.setBounds(140, 70, 100, 20);
			c.add(circle);
			final JTextField circletext = new JTextField(20);
			circletext.setBounds(250, 70, 100, 20);
			circletext.setText(spider.getPeriodTime());
			c.add(circletext);
			isrenew.addChangeListener(new ChangeListener()
			{
				public void stateChanged(ChangeEvent event) 
				{
					if (isrenew.isSelected())
					{
						circletext.setEnabled(true);
					}
					else
					{
						circletext.setEnabled(false);
					}
				}
			});
			if (!isrenew.isSelected())
			{
				circletext.setEnabled(false);
			}
			//代理设置
			JLabel proxy = new JLabel("是否使用代理",JLabel.CENTER);
			proxy.setFont(new Font("宋体",Font.PLAIN,15));
			proxy.setBounds(30, 100, 90, 20);
			c.add(proxy);
			final JCheckBox isproxy = new JCheckBox();
			isproxy.setBounds(125, 100, 17, 20);
			isproxy.setSelected(spider.getProxy());
			c.add(isproxy);
			JLabel proxyIP = new JLabel("代理IP",JLabel.CENTER);
			proxyIP.setFont(new Font("宋体",Font.PLAIN,15));
			proxyIP.setBounds(5, 130, 60, 20);
			c.add(proxyIP);
			final JTextField proxyIPtext = new JTextField(20);
			proxyIPtext.setBounds(65, 130, 120, 20);
			proxyIPtext.setText(spider.getProxyInfo()[0]);
			c.add(proxyIPtext);
			JLabel proxyport = new JLabel("代理端口",JLabel.CENTER);
			proxyport.setFont(new Font("宋体",Font.PLAIN,15));
			proxyport.setBounds(205, 130, 60, 20);
			c.add(proxyport);
			final JTextField proxyporttext = new JTextField(20);
			proxyporttext.setBounds(265, 130, 120, 20);
			proxyporttext.setText(spider.getProxyInfo()[1]);
			c.add(proxyporttext);
			JLabel proxyusername = new JLabel("用户名称",JLabel.CENTER);
			proxyusername.setFont(new Font("宋体",Font.PLAIN,15));
			proxyusername.setBounds(5, 160, 60, 20);
			c.add(proxyusername);
			final JTextField proxyusernametext = new JTextField(20);
			proxyusernametext.setBounds(65, 160, 120, 20);
			proxyusernametext.setText(spider.getProxyInfo()[2]);
			c.add(proxyusernametext);
			JLabel proxyuserpsw = new JLabel("用户密码",JLabel.CENTER);
			proxyuserpsw.setFont(new Font("宋体",Font.PLAIN,15));
			proxyuserpsw.setBounds(205, 160, 60, 20);
			c.add(proxyuserpsw);
			final JTextField proxyuserpswtext = new JTextField(20);
			proxyuserpswtext.setBounds(265, 160, 120, 20);
			proxyuserpswtext.setText(spider.getProxyInfo()[3]);
			c.add(proxyuserpswtext);
			isproxy.addChangeListener(new ChangeListener()
			{
				public void stateChanged(ChangeEvent event) 
				{
					if(isproxy.isSelected())
					{
						proxyIPtext.setEnabled(true);
						proxyporttext.setEnabled(true);
						proxyusernametext.setEnabled(true);
						proxyuserpswtext.setEnabled(true);
					}
					else
					{
						proxyIPtext.setEnabled(false);
						proxyporttext.setEnabled(false);
						proxyusernametext.setEnabled(false);
						proxyuserpswtext.setEnabled(false);
					}
				}
			});
			if (!isproxy.isSelected())
			{
				proxyIPtext.setEnabled(false);
				proxyporttext.setEnabled(false);
				proxyusernametext.setEnabled(false);
				proxyuserpswtext.setEnabled(false);
			}
			//设置词典
			JLabel path = new JLabel("词典路径",JLabel.CENTER);
			path.setFont(new Font("宋体",Font.PLAIN,15));
			path.setBounds(30, 205, 60, 20);
			c.add(path);
			final JTextField pathtext = new JTextField(20);
			pathtext.setBounds(100, 205, 200, 20);
			pathtext.setText(spider.getDicroot());
			c.add(pathtext);
			JButton pathbutton = new JButton("...");
			pathbutton.setBounds(310, 205, 50, 20);
			c.add(pathbutton);
			pathbutton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent event) 
				{
					JFileChooser jfc=new JFileChooser();
					jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY );
					jfc.showDialog(new JLabel(), "选择data文件夹");
					File file=jfc.getSelectedFile();
					if (file != null)
					{
						String abpath = file.getAbsolutePath();
						abpath = abpath.replaceAll("\\\\", "/");
						abpath = abpath.substring(0, abpath.lastIndexOf("/")+1);
						pathtext.setText(abpath);
					}
				}
			});
			//确认和取消按钮
			JButton confirm = new JButton("确认");
			confirm.setBounds(100, 240, 90, 30);
			c.add(confirm);
			JButton cancel = new JButton("取消");
			cancel.setBounds(210, 240, 90, 30);
			c.add(cancel);
			confirm.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent event) 
				{
					//存储信息
					String[] url = initURLtext.getText().trim().split(";");
					spider.setSeeds(url);
					spider.setLayer(Integer.parseInt((String)layertext.getSelectedItem()));
					spider.setPeriod(isrenew.isSelected(), Integer.parseInt(circletext.getText().trim()));
					spider.setProxy(isproxy.isSelected(), proxyIPtext.getText().trim(), 
					proxyporttext.getText().trim(), proxyusernametext.getText().trim(), proxyuserpswtext.getText().trim());
					spider.setDicroot(pathtext.getText());
					//关闭对话框
					dialog.setVisible(false);
					dialog.dispose();
				}
			});
			cancel.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent enent) 
				{
					//关闭对话框
					dialog.setVisible(false);
					dialog.dispose();
				}
			});
			//设置关闭动作
			dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			//显示对话框
			dialog.setVisible(true);
		}
	}
	
	/**线程设置*/
	private class ThreadSet implements ActionListener
	{
		public void actionPerformed(ActionEvent event) 
		{
			final JDialog dialog = new JDialog(frame,"线程设置");
			dialog.setBounds(450, 250, 400, 150);
			dialog.setResizable(false);
			Container c = dialog.getContentPane();
			c.setLayout(null);
			//线程间隔时间
			JLabel threadsblank = new JLabel("线程间隔时间/ms",JLabel.CENTER);
			threadsblank.setFont(new Font("宋体",Font.PLAIN,15));
			threadsblank.setBounds(20, 10, 120, 20);
			c.add(threadsblank);
			final JTextField threadsblanktext = new JTextField(20);
			threadsblanktext.setBounds(140, 10, 200, 20);
			threadsblanktext.setText(spider.getThreadsblank());
			c.add(threadsblanktext);
			//线程数目
			JLabel threadsnum = new JLabel("线程数目",JLabel.CENTER);
			threadsnum.setFont(new Font("宋体",Font.PLAIN,15));
			threadsnum.setBounds(20, 40, 120, 20);
			c.add(threadsnum);
		    final JComboBox<String> threadsnumtext = new JComboBox<>();
		    for (int i=1;i<=40;i++)
		    {
		    	threadsnumtext.addItem(""+i);
		    }
		    threadsnumtext.setMaximumRowCount(5);
		    threadsnumtext.setBounds(140, 40, 60, 20);
		    threadsnumtext.setSelectedItem(spider.getThreadsnum());
		    c.add(threadsnumtext);
			//确认和取消按钮
			JButton confirm = new JButton("确认");
			confirm.setBounds(100, 80, 90, 30);
			c.add(confirm);
			JButton cancel = new JButton("取消");
			cancel.setBounds(210, 80, 90, 30);
			c.add(cancel);
			confirm.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent event) 
				{
					//存储信息
					spider.setThreadsblank(Integer.parseInt(threadsblanktext.getText().trim()));
					spider.setThreadsnum(Integer.parseInt((String)threadsnumtext.getSelectedItem()));
					//关闭对话框
					dialog.setVisible(false);
					dialog.dispose();
				}
			});
			cancel.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent enent) 
				{
					//关闭对话框
					dialog.setVisible(false);
					dialog.dispose();
				}
			});
			//设置关闭动作
			dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			//显示对话框
			dialog.setVisible(true);
		}
	}
	
	/**网络设置*/
	private class NetworkSet implements ActionListener
	{
		public void actionPerformed(ActionEvent event) 
		{
			final JDialog dialog = new JDialog(frame,"网络设置");
			dialog.setBounds(450, 250, 400, 150);
			dialog.setResizable(false);
			Container c = dialog.getContentPane();
			c.setLayout(null);
			//网络超时
			JLabel netovertime = new JLabel("网络超时/ms",JLabel.CENTER);
			netovertime.setFont(new Font("宋体",Font.PLAIN,15));
			netovertime.setBounds(10, 10, 90, 20);
			c.add(netovertime);
			final JTextField netovertimetext = new JTextField(20);
			netovertimetext.setBounds(100, 10, 90, 20);
			netovertimetext.setText(spider.getNetovertime());
			c.add(netovertimetext);
			//请求超时
			JLabel netrequestovertime = new JLabel("请求超时/ms",JLabel.CENTER);
			netrequestovertime.setFont(new Font("宋体",Font.PLAIN,15));
			netrequestovertime.setBounds(200, 10, 90, 20);
			c.add(netrequestovertime);
			final JTextField netrequestovertimetext = new JTextField(20);
			netrequestovertimetext.setBounds(290, 10, 90, 20);
			netrequestovertimetext.setText(spider.getNetrequestovertime());
			c.add(netrequestovertimetext);
			//重拨次数
			JLabel netconnum = new JLabel("重拨次数",JLabel.CENTER);
			netconnum.setFont(new Font("宋体",Font.PLAIN,15));
			netconnum.setBounds(10, 40, 90, 20);
			c.add(netconnum);
		    final JComboBox<String> netconnumtext = new JComboBox<>();
		    netconnumtext.addItem("1");
		    netconnumtext.addItem("2");
		    netconnumtext.addItem("3");
		    netconnumtext.addItem("4");
		    netconnumtext.addItem("5");
		    netconnumtext.setBounds(100, 40, 60, 20);
		    netconnumtext.setSelectedItem(spider.getNetconnum());
		    c.add(netconnumtext);
			//确认和取消按钮
			JButton confirm = new JButton("确认");
			confirm.setBounds(100, 80, 90, 30);
			c.add(confirm);
			JButton cancel = new JButton("取消");
			cancel.setBounds(210, 80, 90, 30);
			c.add(cancel);
			confirm.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent event) 
				{
					//存储信息
					spider.setNetovertime(Integer.parseInt(netovertimetext.getText().trim()));
					spider.setNetrequestovertime(Integer.parseInt(netrequestovertimetext.getText().trim()));
					spider.setNetconnum(Integer.parseInt((String)netconnumtext.getSelectedItem()));
					//关闭对话框
					dialog.setVisible(false);
					dialog.dispose();
				}
			});
			cancel.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent enent) 
				{
					//关闭对话框
					dialog.setVisible(false);
					dialog.dispose();
				}
			});
			//设置关闭动作
			dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			//显示对话框
			dialog.setVisible(true);
		}
	}
	
	/**过滤设置*/
	private class FilterSet implements ActionListener
	{
		public void actionPerformed(ActionEvent event) 
		{
			final JDialog dialog = new JDialog(frame,"过滤设置");
			dialog.setBounds(450, 250, 400, 150);
			dialog.setResizable(false);
			Container c = dialog.getContentPane();
			c.setLayout(null);
			//过滤器包含设置
			JLabel include = new JLabel("包含",JLabel.CENTER);
			include.setFont(new Font("宋体",Font.PLAIN,15));
			include.setBounds(20, 15, 30, 20);
			c.add(include);
			final JTextField includetext_1 = new JTextField(20);
			includetext_1.setBounds(60, 15, 70, 20);
			c.add(includetext_1);
			JLabel or_1 = new JLabel("或",JLabel.CENTER);
			or_1.setFont(new Font("宋体",Font.PLAIN,10));
			or_1.setBounds(130, 15, 10, 20);
			c.add(or_1);
			final JTextField includetext_2 = new JTextField(20);
			includetext_2.setBounds(140, 15, 70, 20);
			c.add(includetext_2);
			JLabel or_2 = new JLabel("或",JLabel.CENTER);
			or_2.setFont(new Font("宋体",Font.PLAIN,10));
			or_2.setBounds(210, 15, 10, 20);
			c.add(or_2);
			final JTextField includetext_3 = new JTextField(20);
			includetext_3.setBounds(220, 15, 70, 20);
			c.add(includetext_3);
			JLabel or_3 = new JLabel("或",JLabel.CENTER);
			or_3.setFont(new Font("宋体",Font.PLAIN,10));
			or_3.setBounds(290, 15, 10, 20);
			c.add(or_3);
			final JTextField includetext_4 = new JTextField(20);
			includetext_4.setBounds(300, 15, 70, 20);
			c.add(includetext_4);
			//过滤器不包含设置
			JLabel exclude = new JLabel("不含",JLabel.CENTER);
			exclude.setFont(new Font("宋体",Font.PLAIN,15));
			exclude.setBounds(20, 40, 30, 20);
			c.add(exclude);
			final JTextField excludetext_1 = new JTextField(20);
			excludetext_1.setBounds(60, 40, 70, 20);
			c.add(excludetext_1);
			JLabel nor_1 = new JLabel("或",JLabel.CENTER);
			nor_1.setFont(new Font("宋体",Font.PLAIN,10));
			nor_1.setBounds(130, 40, 10, 20);
			c.add(nor_1);
			final JTextField excludetext_2 = new JTextField(20);
			excludetext_2.setBounds(140, 40, 70, 20);
			c.add(excludetext_2);
			JLabel nor_2 = new JLabel("或",JLabel.CENTER);
			nor_2.setFont(new Font("宋体",Font.PLAIN,10));
			nor_2.setBounds(210, 40, 10, 20);
			c.add(nor_2);
			final JTextField excludetext_3 = new JTextField(20);
			excludetext_3.setBounds(220, 40, 70, 20);
			c.add(excludetext_3);
			JLabel nor_3 = new JLabel("或",JLabel.CENTER);
			nor_3.setFont(new Font("宋体",Font.PLAIN,10));
			nor_3.setBounds(290, 40, 10, 20);
			c.add(nor_3);
			final JTextField excludetext_4 = new JTextField(20);
			excludetext_4.setBounds(300, 40, 70, 20);
			c.add(excludetext_4);
			//填充文本框
			String[] includeStr = LinkFilter.getInclude();
			if (includeStr.length>=1)
			{
				includetext_1.setText(includeStr[0]);
			}
			if (includeStr.length>=2)
			{
				includetext_2.setText(includeStr[1]);
			}
			if (includeStr.length>=3)
			{
				includetext_3.setText(includeStr[2]);
			}
			if (includeStr.length>=4)
			{
				includetext_4.setText(includeStr[3]);
			}
			String[] excludeStr = LinkFilter.getExclude();
			if (excludeStr.length>=1)
			{
				excludetext_1.setText(excludeStr[0]);
			}
			if (excludeStr.length>=2)
			{
				excludetext_2.setText(excludeStr[1]);
			}
			if (excludeStr.length>=3)
			{
				excludetext_3.setText(excludeStr[2]);
			}
			if (excludeStr.length>=4)
			{
				excludetext_4.setText(excludeStr[3]);
			}
 			//确认和取消按钮
			JButton confirm = new JButton("确认");
			confirm.setBounds(100, 80, 90, 30);
			c.add(confirm);
			JButton cancel = new JButton("取消");
			cancel.setBounds(210, 80, 90, 30);
			c.add(cancel);
			confirm.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent event) 
				{
					//存储信息
					String include = "";
					if (!includetext_1.getText().trim().equals(""))
					{
						include = include + "|" + ";"+includetext_1.getText().trim()+";";
					}
					if (!includetext_2.getText().trim().equals(""))
					{
						include = include + "|" + ";"+includetext_2.getText().trim()+";";
					}
					if (!includetext_3.getText().trim().equals(""))
					{
						include = include + "|" + ";"+includetext_3.getText().trim()+";";
					}
					if (!includetext_4.getText().trim().equals(""))
					{
						include = include + "|" + ";"+includetext_4.getText().trim()+";";
					}
					String exclude = "";
					if (!excludetext_1.getText().trim().equals(""))
					{
						exclude = exclude + "|" + ";"+excludetext_1.getText().trim()+";";
					}
					if (!excludetext_2.getText().trim().equals(""))
					{
						exclude = exclude + "|" + ";"+excludetext_2.getText().trim()+";";
					}
					if (!excludetext_3.getText().trim().equals(""))
					{
						exclude = exclude + "|" + ";"+excludetext_3.getText().trim()+";";
					}
					if (!excludetext_4.getText().trim().equals(""))
					{
						exclude = exclude + "|" + ";"+excludetext_4.getText().trim()+";";
					}
					spider.setFilter(new LinkFilter(include,exclude));
					//关闭对话框
					dialog.setVisible(false);
					dialog.dispose();
				}
			});
			cancel.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent enent) 
				{
					//关闭对话框
					dialog.setVisible(false);
					dialog.dispose();
				}
			});
			//设置关闭动作
			dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			//显示对话框
			dialog.setVisible(true);
		}
	}

	/**存储设置*/
	private class StoreSet implements ActionListener
	{
		public void actionPerformed(ActionEvent event) 
		{
			final JDialog dialog = new JDialog(frame,"存储设置");
			dialog.setBounds(450, 170, 400, 360);
			dialog.setResizable(false);
			Container c = dialog.getContentPane();
			c.setLayout(null);
			//是否保存到文件
			JLabel storeToFile = new JLabel("①是否保存到文件:",JLabel.CENTER);
			storeToFile.setFont(new Font("宋体",Font.BOLD,12));
			storeToFile.setBounds(0, 2, 120, 20);
			c.add(storeToFile);
			final JCheckBox isstoreToFile = new JCheckBox();
			isstoreToFile.setBounds(120, 2, 17, 20);
			isstoreToFile.setSelected(spider.getSave());
			c.add(isstoreToFile);
			JLabel address = new JLabel("保存地址:",JLabel.CENTER);
			address.setFont(new Font("宋体",Font.PLAIN,12));
			address.setBounds(26, 25, 60, 20);
			c.add(address);
			final JTextField addresstext = new JTextField(20);
			addresstext.setBounds(86, 25, 200, 20);
			addresstext.setText(spider.getFileroot());
			c.add(addresstext);
			final JButton addressbutton = new JButton("...");
			addressbutton.setBounds(290, 25, 30, 20);
			c.add(addressbutton);
			addressbutton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent event) 
				{
					JFileChooser jfc=new JFileChooser();
					jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY );
					jfc.showDialog(new JLabel(), "选择文件夹");
					File file=jfc.getSelectedFile();
					if (file != null)
					{
						addresstext.setText(""+file.getAbsolutePath()+"\\");
					}
				}
			});
			final JCheckBox js = new JCheckBox("javascript");
			js.setBounds(0, 60, 100, 20);
			js.setSelected(spider.getSaveType()[0]);
			c.add(js);
			final JCheckBox css = new JCheckBox("css");
			css.setBounds(100, 60, 100, 20);
			css.setSelected(spider.getSaveType()[1]);
			c.add(css);
			final JCheckBox img = new JCheckBox("图片");
			img.setBounds(200, 60, 100, 20);
			img.setSelected(spider.getSaveType()[2]);
			c.add(img);
			final JCheckBox xml = new JCheckBox("xml");
			xml.setBounds(300, 60, 100, 20);
			xml.setSelected(spider.getSaveType()[3]);
			c.add(xml);
			final JCheckBox word = new JCheckBox("word");
			word.setBounds(0, 90, 100, 20);
			word.setSelected(spider.getSaveType()[4]);
			c.add(word);
			final JCheckBox pdf = new JCheckBox("pdf");
			pdf.setBounds(100, 90, 100, 20);
			pdf.setSelected(spider.getSaveType()[5]);
			c.add(pdf);
			final JCheckBox ppt = new JCheckBox("ppt");
			ppt.setBounds(200, 90, 100, 20);
			ppt.setSelected(spider.getSaveType()[6]);
			c.add(ppt);
			final JCheckBox excel = new JCheckBox("excel");
			excel.setBounds(300, 90, 100, 20);
			excel.setSelected(spider.getSaveType()[7]);
			c.add(excel);
			isstoreToFile.addChangeListener(new ChangeListener()
			{
				public void stateChanged(ChangeEvent event) 
				{
					if (isstoreToFile.isSelected())
					{
						js.setEnabled(true);
						css.setEnabled(true);
						img.setEnabled(true);
						xml.setEnabled(true);
						word.setEnabled(true);
						pdf.setEnabled(true);
						ppt.setEnabled(true);
						excel.setEnabled(true);
						addresstext.setEnabled(true);
						addressbutton.setEnabled(true);	
					}
					else
					{
						js.setEnabled(false);
						css.setEnabled(false);
						img.setEnabled(false);
						xml.setEnabled(false);
						word.setEnabled(false);
						pdf.setEnabled(false);
						ppt.setEnabled(false);
						excel.setEnabled(false);
						addresstext.setEnabled(false);
						addressbutton.setEnabled(false);	
					}
				}
				
			});
			if (!isstoreToFile.isSelected())
			{
				js.setEnabled(false);
				css.setEnabled(false);
				img.setEnabled(false);
				xml.setEnabled(false);
				word.setEnabled(false);
				pdf.setEnabled(false);
				ppt.setEnabled(false);
				excel.setEnabled(false);
				addresstext.setEnabled(false);
				addressbutton.setEnabled(false);	
			}
			//是否保存到数据库
			JLabel storeToDB = new JLabel("②是否保存到数据库:",JLabel.CENTER);
			storeToDB.setFont(new Font("宋体",Font.BOLD,12));
			storeToDB.setBounds(0, 130, 130, 20);
			c.add(storeToDB);
			final JCheckBox isstoreToDB = new JCheckBox();
			isstoreToDB.setBounds(130, 130, 17, 20);
			isstoreToDB.setSelected(spider.getDB());
			c.add(isstoreToDB);
			final JLabel hostname = new JLabel("主机名",JLabel.CENTER);
			hostname.setFont(new Font("宋体",Font.PLAIN,12));
			hostname.setBounds(0, 160, 60, 20);
			c.add(hostname);
			final JTextField hostnametext = new JTextField(20);
			hostnametext.setBounds(60, 160, 100, 20);
			hostnametext.setText(spider.getDBInfo()[0]);
			c.add(hostnametext);
			final JLabel port = new JLabel("端口号",JLabel.CENTER);
			port.setFont(new Font("宋体",Font.PLAIN,12));
			port.setBounds(200, 160, 60, 20);
			c.add(port);
			final JTextField porttext = new JTextField(20);
			porttext.setBounds(260, 160, 100, 20);
			porttext.setText(spider.getDBInfo()[1]);
			c.add(porttext);
			final JLabel dbname = new JLabel("数据库名",JLabel.CENTER);
			dbname.setFont(new Font("宋体",Font.PLAIN,12));
			dbname.setBounds(0, 190, 60, 20);
			c.add(dbname);
			final JTextField dbnametext = new JTextField(20);
			dbnametext.setBounds(60, 190, 100, 20);
			dbnametext.setText(spider.getDBInfo()[2]);
			c.add(dbnametext);
			final JLabel tablename = new JLabel("表名",JLabel.CENTER);
			tablename.setFont(new Font("宋体",Font.PLAIN,12));
			tablename.setBounds(200, 190, 60, 20);
			c.add(tablename);
			final JTextField tablenametext = new JTextField(20);
			tablenametext.setBounds(260, 190, 100, 20);
			tablenametext.setText(spider.getDBInfo()[3]);
			c.add(tablenametext);
			final JLabel userid = new JLabel("用户名",JLabel.CENTER);
			userid.setFont(new Font("宋体",Font.PLAIN,12));
			userid.setBounds(0, 220, 60, 20);
			c.add(userid);
			final JTextField useridtext = new JTextField(20);
			useridtext.setBounds(60, 220, 100, 20);
			useridtext.setText(spider.getDBInfo()[4]);
			c.add(useridtext);
			final JLabel password = new JLabel("密码",JLabel.CENTER);
			password.setFont(new Font("宋体",Font.PLAIN,12));
			password.setBounds(200, 220, 60, 20);
			c.add(password);
			final JPasswordField passwordtext = new JPasswordField(20);
			passwordtext.setBounds(260, 220, 100, 20);
			passwordtext.setText(spider.getDBInfo()[5]);
			c.add(passwordtext);
			final JCheckBox keywordsFilling = new JCheckBox("关键词智能填充");
			keywordsFilling.setBounds(5, 250, 130, 20);
			keywordsFilling.setSelected(spider.getDBFunc()[0]);
			c.add(keywordsFilling);
			final JCheckBox descriptionFilling = new JCheckBox("摘要智能填充");
			descriptionFilling.setBounds(135, 250, 130, 20);
			descriptionFilling.setSelected(spider.getDBFunc()[1]);
			c.add(descriptionFilling);
			final JCheckBox contentFilling = new JCheckBox("正文智能填充");
			contentFilling.setBounds(265, 250, 130, 20);
			contentFilling.setSelected(spider.getDBFunc()[2]);
			c.add(contentFilling);
			isstoreToDB.addChangeListener(new ChangeListener()
			{
				public void stateChanged(ChangeEvent event) 
				{
					if (isstoreToDB.isSelected())
					{
						hostnametext.setEnabled(true);
						porttext.setEnabled(true);
						dbnametext.setEnabled(true);
						tablenametext.setEnabled(true);
						useridtext.setEnabled(true);
						passwordtext.setEnabled(true);
						keywordsFilling.setEnabled(true);
						descriptionFilling.setEnabled(true);
						contentFilling.setEnabled(true);
					}
					else
					{
						hostnametext.setEnabled(false);
						porttext.setEnabled(false);
						dbnametext.setEnabled(false);
						tablenametext.setEnabled(false);
						useridtext.setEnabled(false);
						passwordtext.setEnabled(false);
						keywordsFilling.setEnabled(false);
						descriptionFilling.setEnabled(false);
						contentFilling.setEnabled(false);
					}
				}
			});
			if (!isstoreToDB.isSelected())
			{
				hostnametext.setEnabled(false);
				porttext.setEnabled(false);
				dbnametext.setEnabled(false);
				tablenametext.setEnabled(false);
				useridtext.setEnabled(false);
				passwordtext.setEnabled(false);
				keywordsFilling.setEnabled(false);
				descriptionFilling.setEnabled(false);
				contentFilling.setEnabled(false);
			}
			//确认和取消按钮
			JButton confirm = new JButton("确认");
			confirm.setBounds(100, 290, 90, 30);
			c.add(confirm);
			JButton cancel = new JButton("取消");
			cancel.setBounds(210, 290, 90, 30);
			c.add(cancel);
			confirm.addActionListener(new ActionListener()
			{
				@SuppressWarnings("deprecation")
				public void actionPerformed(ActionEvent event) 
				{
					//存储信息
					spider.setSave(isstoreToFile.isSelected());
					spider.setFileroot(addresstext.getText());
					spider.setSaveType(js.isSelected(), css.isSelected(), img.isSelected(), 
							xml.isSelected(), word.isSelected(), pdf.isSelected(), 
							ppt.isSelected(), excel.isSelected());
					spider.setDB(isstoreToDB.isSelected(), hostnametext.getText(), porttext.getText(), 
					dbnametext.getText(), tablenametext.getText(), useridtext.getText(), passwordtext.getText());
					spider.setDBFunc(keywordsFilling.isSelected(), 
					descriptionFilling.isSelected(), contentFilling.isSelected());
					//关闭对话框
					dialog.setVisible(false);
					dialog.dispose();
				}
			});
			cancel.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent enent) 
				{
					//关闭对话框
					dialog.setVisible(false);
					dialog.dispose();
				}
			});
			//设置关闭动作
			dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			//显示对话框
			dialog.setVisible(true);
		}
	}
	
	/**设置读取*/
	private class SetRead implements ActionListener
	{
		public void actionPerformed(ActionEvent arg0) 
		{		
			final JDialog dialog = new JDialog(frame,"设置读取");
			dialog.setBounds(450, 205, 400, 145);
			dialog.setResizable(false);
			Container c = dialog.getContentPane();
			c.setLayout(null);
			//保存位置
			JLabel path = new JLabel("文件位置",JLabel.CENTER);
			path.setFont(new Font("宋体",Font.PLAIN,15));
			path.setBounds(30, 30, 60, 20);
			c.add(path);
			final JTextField pathtext = new JTextField(20);
			pathtext.setBounds(100, 30, 200, 20);
			c.add(pathtext);
			JButton pathbutton = new JButton("...");
			pathbutton.setBounds(310, 30, 50, 20);
			c.add(pathbutton);
			pathbutton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent event) 
				{
					JFileChooser jfc=new JFileChooser();
					jfc.setFileSelectionMode(JFileChooser.FILES_ONLY );
					jfc.showDialog(new JLabel(), "选择文件");
					File file=jfc.getSelectedFile();
					if (file != null)
					{
						pathtext.setText(""+file.getAbsolutePath());
					}
				}
			});
			//确认和取消按钮
			JButton confirm = new JButton("确认");
			confirm.setBounds(100, 75, 90, 30);
			c.add(confirm);
			JButton cancel = new JButton("取消");
			cancel.setBounds(210, 75, 90, 30);
			c.add(cancel);
			confirm.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent event) 
				{
					SaveRead.read(spider,notice,pathtext.getText().trim());
					//关闭对话框
					dialog.setVisible(false);
					dialog.dispose();
				}
			});
			cancel.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent enent) 
				{
					//关闭对话框
					dialog.setVisible(false);
					dialog.dispose();
				}
			});
			//设置关闭动作
			dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			//显示对话框
			dialog.setVisible(true);
		}
	}
	
	/**设置保存*/
	private class SetSave implements ActionListener
	{
		public void actionPerformed(ActionEvent e) 
		{
			final JDialog dialog = new JDialog(frame,"设置保存");
			dialog.setBounds(450, 205, 400, 145);
			dialog.setResizable(false);
			Container c = dialog.getContentPane();
			c.setLayout(null);
			//文件名
			JLabel filename = new JLabel("文件名",JLabel.CENTER);
			filename.setFont(new Font("宋体",Font.PLAIN,15));
			filename.setBounds(40, 10, 50, 20);
			c.add(filename);
			final JTextField filenametext = new JTextField(20);
			filenametext.setBounds(100, 10, 200, 20);
			c.add(filenametext);
			//保存位置
			JLabel path = new JLabel("保存路径",JLabel.CENTER);
			path.setFont(new Font("宋体",Font.PLAIN,15));
			path.setBounds(30, 40, 60, 20);
			c.add(path);
			final JTextField pathtext = new JTextField(20);
			pathtext.setBounds(100, 40, 200, 20);
			c.add(pathtext);
			JButton pathbutton = new JButton("...");
			pathbutton.setBounds(310, 40, 50, 20);
			c.add(pathbutton);
			pathbutton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent event) 
				{
					JFileChooser jfc=new JFileChooser();
					jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY );
					jfc.showDialog(new JLabel(), "选择文件夹");
					File file=jfc.getSelectedFile();
					if (file != null)
					{
						pathtext.setText(""+file.getAbsolutePath()+"\\");
					}
				}
			});
			//确认和取消按钮
			JButton confirm = new JButton("确认");
			confirm.setBounds(100, 75, 90, 30);
			c.add(confirm);
			JButton cancel = new JButton("取消");
			cancel.setBounds(210, 75, 90, 30);
			c.add(cancel);
			confirm.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent event) 
				{
					SaveRead.save(spider,notice,pathtext.getText().trim()+filenametext.getText().trim()+".properties");
					//关闭对话框
					dialog.setVisible(false);
					dialog.dispose();
				}
			});
			cancel.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent enent) 
				{
					//关闭对话框
					dialog.setVisible(false);
					dialog.dispose();
				}
			});
			//设置关闭动作
			dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			//显示对话框
			dialog.setVisible(true);
		}
	}
	
	/**开始运行*/
	private class StartFunction implements ActionListener
	{
		public void actionPerformed(ActionEvent event) 
		{
			//状态栏显示
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			notice.append("\n开始运行:"+df.format(new Date())+"\n");
			notice.append("初始URL:\n");
			for (String url:spider.getSeeds().split(";"))
			{
				notice.append("  "+url+"\n");
			}
			notice.append("爬取深度:"+spider.getLayer()+"\n");
			if (spider.getDB())
			{
				notice.append("是否存入数据库:是"+"\n");
			}
			else
			{
				notice.append("是否存入数据库:否"+"\n");
			}
			if (spider.getSave())
			{
				notice.append("是否存为文件:是"+"\n");
			}
			else
			{
				notice.append("是否存为文件:否"+"\n\n");
			}
			state.setText("正在运行中。。。");
			//开始运行
			new Thread()
			{
				public void run()
				{
					spider.ini();
					spider.start();
				}
			}.start();
			//设置按钮不允许
			basicset.setEnabled(false);
			threadset.setEnabled(false);
			networkset.setEnabled(false);
			filterset.setEnabled(false);
			storeset.setEnabled(false);
			setsave.setEnabled(false);
			setread.setEnabled(false);
			start.setEnabled(false);
			cancel.setEnabled(true);
			search.setEnabled(false);
			//查询是否结束
			isStop = new Thread()
			{				
				public void run()
				{
					while(!Crawl.isTerminate())
					{
							continue;
					}
					SwingUtilities.invokeLater(new Runnable()
					{
						public void run() 
						{
							SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							notice.append("\n正常结束:"+df.format(new Date())+"\n");
							state.setText("准备就绪");
						}
					}); 
					//设置按钮不允许
					basicset.setEnabled(true);
					threadset.setEnabled(true);
					networkset.setEnabled(true);
					filterset.setEnabled(true);
					storeset.setEnabled(true);
					setsave.setEnabled(true);
					setread.setEnabled(true);
					start.setEnabled(true);
					cancel.setEnabled(false);
					search.setEnabled(true);
				}
			};
			isStop.start();
		}
	}
	
	/**取消运行*/
	private class cancelFunction implements ActionListener
	{
		@SuppressWarnings("deprecation")
		public void actionPerformed(ActionEvent event) 
		{
			//停止运行
			isStop.stop();
			spider.stop();
			//状态栏显示
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			notice.append("\n强制结束:"+df.format(new Date())+"\n");
			state.setText("准备就绪");
			//设置按钮不允许
			basicset.setEnabled(true);
			threadset.setEnabled(true);
			networkset.setEnabled(true);
			filterset.setEnabled(true);
			storeset.setEnabled(true);
			setsave.setEnabled(true);
			setread.setEnabled(true);
			start.setEnabled(true);
			cancel.setEnabled(false);
			search.setEnabled(true);
		}
	}
	
	/**查询数据*/
	private class Search implements ActionListener
	{
		public void actionPerformed(ActionEvent event) 
		{
			final JDialog dialog = new JDialog(frame,"查询");
			dialog.setBounds(450, 205, 400, 290);
			dialog.setResizable(false);
			Container c = dialog.getContentPane();
			c.setLayout(null);
			//查询按钮
			JLabel search = new JLabel("全文搜索",JLabel.CENTER);
			search.setFont(new Font("宋体",Font.PLAIN,15));
			search.setBounds(30, 10, 70, 20);
			c.add(search);
			final JTextField searchtext = new JTextField(20);
			searchtext.setBounds(110, 10, 150, 20);
			c.add(searchtext);
			final JButton searchbutton = new JButton("查询");
			searchbutton.setBounds(270, 10, 70, 20);
			c.add(searchbutton);
			//文章信息
			JLabel url = new JLabel("URL：",JLabel.CENTER);
			url.setFont(new Font("宋体",Font.PLAIN,15));
			url.setBounds(58, 40, 40, 20);
			c.add(url);
			final JLabel urltext = new JLabel("",JLabel.CENTER);
			urltext.setFont(new Font("宋体",Font.PLAIN,10));
			urltext.setBounds(98, 40, 282, 20);
			c.add(urltext);
			JLabel time = new JLabel("采集时间：",JLabel.CENTER);
			time.setFont(new Font("宋体",Font.PLAIN,15));
			time.setBounds(20, 70, 80, 20);
			c.add(time);
			final JLabel timetext = new JLabel("",JLabel.CENTER);
			timetext.setFont(new Font("宋体",Font.PLAIN,10));
			timetext.setBounds(100, 70, 280, 20);
			c.add(timetext);
			JLabel keywords = new JLabel("关键词语：",JLabel.CENTER);
			keywords.setFont(new Font("宋体",Font.PLAIN,15));
			keywords.setBounds(20, 100, 80, 20);
			c.add(keywords);
			final JLabel keywordstext = new JLabel("",JLabel.CENTER);
			keywordstext.setFont(new Font("宋体",Font.PLAIN,10));
			keywordstext.setBounds(100, 100, 280, 20);
			c.add(keywordstext);
			JLabel saveadd = new JLabel("保存地址：",JLabel.CENTER);
			saveadd.setFont(new Font("宋体",Font.PLAIN,15));
			saveadd.setBounds(20, 130, 80, 20);
			c.add(saveadd);
			final JLabel saveaddtext = new JLabel("",JLabel.CENTER);
			saveaddtext.setFont(new Font("宋体",Font.PLAIN,10));
			saveaddtext.setBounds(100, 130, 280, 20);
			c.add(saveaddtext);
			JLabel srcadd = new JLabel("资源地址：",JLabel.CENTER);
			srcadd.setFont(new Font("宋体",Font.PLAIN,15));
			srcadd.setBounds(20, 160, 80, 20);
			c.add(srcadd);
			final JLabel srcaddtext = new JLabel("",JLabel.CENTER);
			srcaddtext.setFont(new Font("宋体",Font.PLAIN,10));
			srcaddtext.setBounds(100, 160, 280, 20);
			c.add(srcaddtext);
			//前一页，后一页
			final JButton front = new JButton("前一条");
			front.setBounds(110, 190, 80, 20);
			front.setEnabled(false);
			c.add(front);
			final JButton next = new JButton("后一条");
			next.setBounds(210, 190, 80, 20);
			next.setEnabled(false);
			c.add(next);
			//搜索结果
			final JLabel resultnum = new JLabel("结果总数：",JLabel.CENTER);
			resultnum.setFont(new Font("宋体",Font.PLAIN,10));
			resultnum.setBounds(10, 200, 50, 20);
			c.add(resultnum);
			final JLabel resultnumtext = new JLabel("",JLabel.CENTER);
			resultnumtext.setFont(new Font("宋体",Font.PLAIN,10));
			resultnumtext.setBounds(50, 200, 40, 20);
			c.add(resultnumtext);
			final JLabel resultposition = new JLabel("当前位置：",JLabel.CENTER);
			resultposition.setFont(new Font("宋体",Font.PLAIN,10));
			resultposition.setBounds(10, 220, 50, 20);
			c.add(resultposition);
			final JLabel resultpositiontext = new JLabel("",JLabel.CENTER);
			resultpositiontext.setFont(new Font("宋体",Font.PLAIN,10));
			resultpositiontext.setBounds(50, 220, 40, 20);
			c.add(resultpositiontext);
			//关闭按钮
			JButton close = new JButton("关闭");
			close.setBounds(165, 220, 90, 30);
			c.add(close);
			close.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent enent) 
				{
					dialog.setVisible(false);
					dialog.dispose();
				}
			});
			//设置 监听器
			ActionListener listener = new ActionListener()
			{
				/**查询结果*/
				String[] result = {"","","","",""};
				/**查询到的结果条数*/
				int resultNum = 0;
				/**显示当前位置*/
				int resultPosition = 0;
				
				public void actionPerformed(ActionEvent event) 
				{
					if (event.getSource() == searchbutton)
					{
						//查询记录
						String searchWord = searchtext.getText().trim();
						if(!searchWord.equals(""))
						{
							try 
							{
								result = StoreToDB.queryHtml(searchWord);
								if (result == null)
								{
									result = new String[]{"","","未查询到记录！","",""};
									resultNum = 0;
									resultPosition = -5;
								}
								else
								{
									resultNum = result.length/5;
									resultPosition = 0;
								}
							} 
							catch (MyException e) 
							{
								result = new String[]{"","","查询失败!","",""};
								resultNum = 0;
								resultPosition = -5;
							}
						}
						else
						{
							result = new String[]{"","","","",""};
							resultNum = 0;
							resultPosition = -5;
						}
						//显示第一条信息
						urltext.setText(result[0]);
						timetext.setText(result[1]);
						keywordstext.setText(result[2]);
						saveaddtext.setText(result[3]);
						srcaddtext.setText(result[4]);
						resultnumtext.setText(String.valueOf(resultNum));
						resultpositiontext.setText(String.valueOf(resultPosition/5+1));
						//设置按钮
						if (resultPosition<=0)
						{
							front.setEnabled(false);
						}
						else
						{
							front.setEnabled(true);
						}
						if (resultPosition == -5||resultPosition+5 >= result.length)
						{
							next.setEnabled(false);
						}
						else
						{
							next.setEnabled(true);
						}
					}
					else if(event.getSource() == front)
					{
						//显示信息
						resultPosition = resultPosition - 5;
						urltext.setText(result[resultPosition]);
						timetext.setText(result[resultPosition+1]);
						keywordstext.setText(result[resultPosition+2]);
						saveaddtext.setText(result[resultPosition+3]);
						srcaddtext.setText(result[resultPosition+4]);
						resultpositiontext.setText(String.valueOf(resultPosition/5+1));
						//设置按钮
						if (resultPosition<=0)
						{
							front.setEnabled(false);
						}
						else
						{
							front.setEnabled(true);
						}
						if (resultPosition+5 >= result.length)
						{
							next.setEnabled(false);
						}
						else
						{
							next.setEnabled(true);
						}
					}
					else
					{
						//显示信息
						resultPosition = resultPosition + 5;
						urltext.setText(result[resultPosition]);
						timetext.setText(result[resultPosition+1]);
						keywordstext.setText(result[resultPosition+2]);
						saveaddtext.setText(result[resultPosition+3]);
						srcaddtext.setText(result[resultPosition+4]);
						resultpositiontext.setText(String.valueOf(resultPosition/5+1));
						//设置按钮
						if (resultPosition<=0)
						{
							front.setEnabled(false);
						}
						else
						{
							front.setEnabled(true);
						}
						if (resultPosition+5 >= result.length)
						{
							next.setEnabled(false);
						}
						else
						{
							next.setEnabled(true);
						}
					}
				}
			};
			searchbutton.addActionListener(listener);
			front.addActionListener(listener);
			next.addActionListener(listener);
			//设置关闭动作
			dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			//显示对话框
			dialog.setVisible(true);
		}
	}
	
	public static void main(String args[])
	{
		new UserInterface();
	}  
}
