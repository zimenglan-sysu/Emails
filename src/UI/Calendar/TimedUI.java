package UI.Calendar;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.WindowConstants;

import EmailProcessor.Sends.SendedSMTPProcessor;
import Util.Email.Logging.EmailLogger;

import DataInfo.EnumType;

import java.awt.Canvas;
import java.awt.Font;
import java.awt.Graphics;
import java.text.SimpleDateFormat;

public class TimedUI extends JFrame {
	// VERSION
	private static final long serialVersionUID = 90464999L;
	private final int dayNum = 49;
	// default five minutes
	private final long defaultDuration = 2;
	/**
	 * JButton
	 */
	private JButton sureBtn= new JButton("确认");
	private JButton quitBtn = new JButton("取消");
	/**
	 * JPanel
	 */
	private JPanel wholeJPanel = new JPanel(new BorderLayout());
	//
	private JPanel firstJPanel = new JPanel(new BorderLayout());
	private JPanel topJPanel = new JPanel();
	private JPanel centerJPanel = new JPanel(new GridLayout(7, 7));
	private JPanel bottomJPanel = new JPanel();
	//
	private JPanel btnJPanel = new JPanel();
	private JPanel secondJPanel = new JPanel();
	private JPanel realtimeJPanel = new JPanel();
	/**
	 * JLabel
	 */
	private JLabel lastJL;
	private JLabel[] dayJL = new JLabel[dayNum];
	private JLabel yearJL = new JLabel("年");
	private JLabel monthJL = new JLabel("月");
	private JLabel hourJL = new JLabel("时");
	private JLabel minuteJL = new JLabel("分");
	private JLabel secondJL = new JLabel("秒");
	/**
	 * JComboBox
	 */
	private JComboBox<String> yeadJCB = new JComboBox<String>();
	private JComboBox<String> monthJCB = new JComboBox<String>();
	//
	private JComboBox<String> hourJCB = new JComboBox<String>();
	private JComboBox<String> minuteJCB = new JComboBox<String>();
	private JComboBox<String> secondJCB = new JComboBox<String>();
	/**
	 * Variables
	 */
	private final static String title = "             " +
			"         " +
			"********* Calendar ********* ";
	private int re_year, re_mouth;
	private int x_size, y_size;
	private final boolean isResizable = false;
	private final int Height = 360;
	private final int Width = 360;
	private final int mHeight = 360;
	private final int mWidth = 360;
	private int mYear, mMonth, mDay;
	private int mHour, mMinute, mSecond;
	
	private long duration;
	private Date date;
	private boolean isSureOrQuit = false;
	private boolean isClose = false;
	private boolean isFirst = false;
    //得到一个当前时间的日历对象
	private Calendar now = Calendar.getInstance();
	
	private CallBack callBack;
	
	// *****************************************************
	
    public TimedUI(CallBack callBack) {
    	this.callBack = callBack;
    	
    	// set title
    	// super(title);
    	setSize(mWidth, mHeight);
    	//获取屏幕宽度
	    x_size = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	    //获取屏幕高度
	    y_size = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	    setLocation((x_size - Width) / 2, (y_size - Height) / 2);
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    // topJPanel
	    topJPanel.add(yearJL);
	    topJPanel.add(yeadJCB);
	    topJPanel.add(monthJL);
	    topJPanel.add(monthJCB);
	    // centerJPanel
	    for (int i = 0; i < dayNum; i++) {
	      // 将显示的字符设置为居中
	      dayJL[i] = new JLabel("", JLabel.CENTER);
	      centerJPanel.add(dayJL[i]);
	    }
	    // bottomJPanel
	    bottomJPanel.add(hourJL);
	    bottomJPanel.add(hourJCB);
	    bottomJPanel.add(minuteJL);
	    bottomJPanel.add(minuteJCB);
	    bottomJPanel.add(secondJL);
	    bottomJPanel.add(secondJCB);
	    //
	    topJPanel.setBackground(Color.white);
	    centerJPanel.setBackground(Color.white);
	    bottomJPanel.setBackground(Color.white);
	    // firstJPanel
	    firstJPanel.add(topJPanel, BorderLayout.NORTH);
	    firstJPanel.add(centerJPanel, BorderLayout.CENTER);
	    firstJPanel.add(bottomJPanel, BorderLayout.SOUTH);
	    firstJPanel.setBackground(Color.white);
	    // sencondJPanel
	    btnJPanel.add(sureBtn);
	    btnJPanel.add(quitBtn);
	    secondJPanel.add(btnJPanel, BorderLayout.NORTH);
	    realtimeJPanel.add(new Clock(this));
	    secondJPanel.add(realtimeJPanel, BorderLayout.SOUTH);
	    secondJPanel.setBackground(Color.white);
	    // wholeJPanel
	    wholeJPanel.add(firstJPanel, BorderLayout.NORTH);
	    wholeJPanel.add(secondJPanel, BorderLayout.CENTER);
	    wholeJPanel.setBackground(Color.white);
	    //添加分隔符  
  		JSplitPane jSpeparator =new JSplitPane(JSplitPane.VERTICAL_SPLIT, 
  				firstJPanel, secondJPanel);  
  		//设置分割线宽度  
  		final int thickness = 6;
  		jSpeparator.setDividerSize(thickness);  
  		//使分隔符不能上下拖动  
  		jSpeparator.setEnabled(false);
  		jSpeparator.setBackground(Color.blue);
  		jSpeparator.setForeground(Color.blue);
  		wholeJPanel.add(jSpeparator);
	    //
	    initSettings();
	    
	    // set listener
	    yeadJCB.addActionListener(new ClockAction());
	    monthJCB.addActionListener(new ClockAction());
	    addCompsListeners();
	    
	    this.add(wholeJPanel);
	 
	    /**
	     * JFrame
	     */
		// set title
		setTitle(title);
		// set visible
		setVisible(true);
		// 禁止拉动窗口大小
		setResizable(isResizable);
		// 设置可见
		// 关闭窗口
		setDefaultLookAndFeelDecorated(true);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    public 
    class ClockAction implements ActionListener {
	    public void actionPerformed(ActionEvent e) {
	    	//
	    	int c_year, c_month, c_week;
		    // 得到当前所选年份
		    c_year = Integer.parseInt(yeadJCB.getSelectedItem().toString()); 
		    // 得到当前月份, 并减1, 计算机中的月为: 0-11
		    c_month = Integer.parseInt(monthJCB.getSelectedItem().toString()) - 1; 
		    
		    // 调用函数use, 得到星期几
		    c_week = use(c_year, c_month);
		    // 调用函数Resetday
		    Resetday(c_week, c_year, c_month); 
	    }
    }

    public void initSettings() {
    	int year, monthNum, firstDayNum;
    	int hour, minute, second;
    	int day;
    	
    	String log[] = { "日", "一", "二", "三", "四", "五", "六" };
    	// 
    	for (int i = 0; i < 7; i++) {
    		dayJL[i].setText(log[i]);
    	}
    	// 将星期日的日期设置为magenta
    	for (int i = 0; i < dayNum; i = i + 7) {
    		dayJL[i].setForeground(Color.magenta); 
    	}
    	// 将星期六的日期设置为red
	    for (int i = 6; i < dayNum; i = i + 7) {
	    	dayJL[i].setForeground(Color.red);
	    }
	    // year
	    for (int i = 1; i < 10000; i++) {
	    	yeadJCB.addItem("" + i);
	    }
	    // month
	    for (int i = 1; i < 13; i++) {
	    	monthJCB.addItem("" + i);
	    }
	    // hour
	    for (int i = 0; i < 24; i++) {
	    	hourJCB.addItem("" + i);
	    }
	    // minute
	    for (int i = 0; i < 60; i++) {
	    	minuteJCB.addItem("" + i);
	    }
	    // second
	    for (int i = 0; i < 60; i++) {
	    	secondJCB.addItem("" + i);
	    }
	    
	    // 得到当前时间的`年,月,时,分,秒`
	    monthNum = (int)(now.get(Calendar.MONTH)); 
	    year = (int)(now.get(Calendar.YEAR)); 
	    day = (int)(now.get(Calendar.DATE));
	    hour = (int)(now.get(Calendar.HOUR_OF_DAY));
	    minute = (int)(now.get(Calendar.MINUTE));
	    second = (int)(now.get(Calendar.SECOND));
	    
	    // 设置下拉列表显示为当前`年,月`
	    yeadJCB.setSelectedIndex(year - 1); 
	    monthJCB.setSelectedIndex(monthNum); 
	    
	    // 设置下拉列表显示为当前`时,分,秒`
	    hourJCB.setSelectedIndex(hour);
	    minuteJCB.setSelectedIndex(minute);
	    secondJCB.setSelectedIndex(second);
	    
	    // 调用函数use, 得到星期几
	    firstDayNum = use(year, monthNum);
	    // 调用函数Resetday
	    Resetday(firstDayNum, year, monthNum);
    }

    public int use(int reYear, int reMonth) {
	    int weekNum;
	    // 设置时间为所要查询的年月的第一天
	    now.set(reYear, reMonth, 1); 
	    // 获取当前日期在星期中的第几天 从1-7对应 日-六 
	    weekNum = (int) (now.get(Calendar.DAY_OF_WEEK));
	   
	    return weekNum;
    }

    public void Resetday(int weekLog, int yearLog, int monthLog) {
    	// 存储月份的天数
    	int monthDayScore; 
	    int count = 1;
	    monthDayScore = 0;
	
	    now = Calendar.getInstance();	
	    int day = now.get(Calendar.DATE);
	    // EmailLogger.info("day: " + day);
	    now.set(yearLog + 1900, monthLog, 1);
	    
	    // 得到一个月中最后一天
	    monthDayScore = now.getActualMaximum(Calendar.DAY_OF_MONTH);
	    // 当前天数所在月份的第几天 
	    
	    // 初始化标签(除去前七个标签的下标值)
	    for (int i = 0; i < weekLog; i++) { 
	    	dayJL[i + 7].setText("");
	    }
	    //
	    monthDayScore = monthDayScore + weekLog;
	    //
	    for (int i = weekLog; i < monthDayScore; i++) {
	    	//加上前七个标签的下标值
	    	dayJL[i + 6].setText("" + count);
	    	count++;
	    }
	    //
	    for (int i = monthDayScore + 6; i < dayNum; i++) {
	    	//加上前七个标签的下标值
	    	dayJL[i].setText("");
	    }
	
	    if(isFirst == false) {
	    	isFirst = true;
	    	lastJL = dayJL[day + 6 + weekLog - 1];
	    	lastJL.setForeground(Color.green);
	    	lastJL.setFont(lastJL.getFont().deriveFont(Font.BOLD));
			lastJL.setCursor(new Cursor(Cursor.HAND_CURSOR));
			// get day
			mDay = day;
	    }
    }
    /**
     * repaint
     */
    public class Clock extends Canvas implements Runnable {
	    private static final long serialVersionUID = 82782L;
	    private Thread t;
	    private String time;
	
	    public Clock (TimedUI mf){
		    setSize(280, 40);
		    setBackground(Color.white);
		    t=new Thread(this);			 //实例化线程
		    t.start();				    			
	    }

	    @Override
	    public void run() {
	    	while(true) {
	    		try {
	    			//休眠1秒钟
	    			Thread.sleep(1000);    			
	    		} catch (InterruptedException e) {
	    			System.out.println("发生异常");
	    		}   	
	
	    		//在100毫秒内重绘组件
	    		repaint(100);
	    	}
        }

        public void paint(Graphics g) {
    	    // set font
    	    Font f=new Font("宋体",Font.BOLD,16);

    	    //格式化时间显示类型
    	    SimpleDateFormat SDF=new SimpleDateFormat(
    	    		"yyyy'年'MM'月'dd'日'HH:mm:ss");

            Calendar now=Calendar.getInstance();
            //得到当前日期和时间
            time = SDF.format(now.getTime());	   
            g.setFont(f);
            g.setColor(Color.orange);
            g.drawString(time, 45, 25);
        }
    }
    /**
     * Listener & MouseListener
     */
    private void addCompsListeners() {
	    sureBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// get host
				mYear = Integer.parseInt(yeadJCB.getItemAt(
						yeadJCB.getSelectedIndex())) - 1900;
				mMonth = Integer.parseInt(monthJCB.getItemAt(
						monthJCB.getSelectedIndex()));
				//
				mHour = Integer.parseInt(hourJCB.getItemAt(
						hourJCB.getSelectedIndex()));
				mMinute = Integer.parseInt(minuteJCB.getItemAt(
						minuteJCB.getSelectedIndex()));
				mSecond = Integer.parseInt(secondJCB.getItemAt(
						secondJCB.getSelectedIndex()));
				
				Date nowDate = new Date();
				@SuppressWarnings("deprecation")
				Date latedate = new Date(mYear, mMonth - 1, mDay, mHour, mMinute, mSecond);
				// EmailLogger.info("nowDate: " + nowDate);
				// EmailLogger.info("latedate " + latedate);
				//
				duration = latedate.getTime() - nowDate.getTime();
				// second
				duration = duration / 1000;
				// minutes
				duration = duration / 60;
				// EmailLogger.info("duaration - before: " + duration);
				if(duration < defaultDuration) {
					duration = defaultDuration;
				}
				// EmailLogger.info("duaration - after: " + duration);
				//
				isSureOrQuit = true;
				isClose = true;
				
				// close
				dispose();
				EmailLogger.info("duration: " + duration + " minutes");
				// callback
				TimedUI.this.callBack.call(duration, isSureOrQuit);
			}
		});
		
		quitBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//
				isSureOrQuit = false;
				isClose = true;
				duration = -1;
				
				// close
				dispose();
				EmailLogger.info("duration: " + duration + " minutes");
				EmailLogger.info("cancel the calender");
				// callback
				TimedUI.this.callBack.call(duration, isSureOrQuit);
			}
		});
		
		for(int i = 7; i < dayNum; i++) {
			final int idx = i;
			dayJL[idx].addMouseListener(new MouseListener() {
				@Override
				public void mouseReleased(MouseEvent e) {
					// TODO Auto-generated method stub
				}
				
				@Override
				public void mousePressed(MouseEvent e) {
					// TODO Auto-generated method stub
				}
				
				@Override
				public void mouseExited(MouseEvent e) {
					// TODO Auto-generated method stub
				}
				
				@Override
				public void mouseEntered(MouseEvent e) {
					// TODO Auto-generated method stub
				}
				
				@Override
				public void mouseClicked(MouseEvent e) {
					// 
					String content = dayJL[idx].getText().trim();
					if(content == null || content.equalsIgnoreCase("")) {
						return;
					}
					
					//
				    lastJL.setForeground(Color.black);
				    lastJL.setFont(lastJL.getFont().deriveFont(Font.PLAIN));
					lastJL.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
					
					lastJL = dayJL[idx];
					lastJL.setForeground(Color.green);
					lastJL.setFont(lastJL.getFont().deriveFont(Font.BOLD));
					lastJL.setCursor(new Cursor(Cursor.HAND_CURSOR));
					
					// get day
					mDay = Integer.parseInt(lastJL.getText());
					// EmailLogger.info("mDay: " + mDay);
				}
			});
		}
    }
    /**
     * gets and sets
     * @param args
     */
    public void setDate(Date date) {
    	this.date = date;
    }
    
    public Date getDate() {
    	return this.date;
    }
    
    public void setDuration(long duration) {
    	this.duration = duration;
    }
    
    public long getDuration() {
    	return this.duration;
    }
    
    public void set(boolean isSureOrQuit) {
    	this.isSureOrQuit = isSureOrQuit;
    }
    
    public boolean getIsSureOrQuit() {
    	return this.isSureOrQuit;
    }
    
    public void setIsClose(boolean isClose) {
    	this.isClose = isClose;
    }
    
    public boolean getIsClose() {
    	return this.isClose;
    }
    
    // ******************************************************************
    
    /**
     * CallBack
     */
    public interface CallBack{
    	public void call(long duration, boolean isSureOrQuit);
    }
    
    // ******************************************************************
    
    public static void main(String[] args) {
    	TimedUI TimedUI = new TimedUI(new TimedUI.CallBack() {
			@Override
			public void call(long duration, boolean isSureOrQuit) {
				
			}
		});
    }
}