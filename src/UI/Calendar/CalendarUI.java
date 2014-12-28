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
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import UI.Notes.NotepadUITest;
import UI.Sends.SendedUITest;
import Util.Email.Logging.EmailLogger;
import DataInfo.EnumType;

import java.awt.Canvas;
import java.awt.Font;
import java.awt.Graphics;
import java.text.SimpleDateFormat;

public class CalendarUI extends JPanel {
	// VERSION
	private static final long serialVersionUID = 90464999L;
	private final int dayNum = 49;
	/**
	 * JPanel
	 */
	private JPanel wholeJPanel = new JPanel(new BorderLayout());
	//
	private JPanel firstJPanel = new JPanel(new BorderLayout());
	private JPanel topJPanel = new JPanel();
	private JPanel centerJPanel = new JPanel(new GridLayout(7, 7));
	//
	private JPanel secondJPanel = new JPanel();
	/**
	 * JLabel
	 */
	private JLabel lastJL;
	private JLabel[] dayJL = new JLabel[dayNum];
	private JLabel yearJL = new JLabel("年");
	private JLabel monthJL = new JLabel("月");
	/**
	 * JComboBox
	 */
	private JComboBox<String> yeadJCB = new JComboBox<String>();
	private JComboBox<String> monthJCB = new JComboBox<String>();
	
	/**
	 * Variables
	 */
	private int re_year, re_mouth;
	private final int Height = EnumType.CALENDER_HEIGHT;
	private final int Width = EnumType.CALENDER_WIDTH;
	private int mDay, mYear, mMonth, mHour, mSecond, mMinute;
	private boolean isFirst = false;
    //得到一个当前时间的日历对象
	private Calendar now = Calendar.getInstance();
	
	// *****************************************************
	
    public CalendarUI() {
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
	    //
	    topJPanel.setBackground(Color.white);
	    centerJPanel.setBackground(Color.white);
	    // firstJPanel
	    firstJPanel.add(topJPanel, BorderLayout.NORTH);
	    firstJPanel.add(centerJPanel, BorderLayout.CENTER);
	    firstJPanel.setBackground(Color.white);
	    // sencondJPanel
	    secondJPanel.add(new Clock(this));
	    // wholeJPanel
	    wholeJPanel.add(firstJPanel, BorderLayout.CENTER);
	    wholeJPanel.add(secondJPanel, BorderLayout.NORTH);
	    wholeJPanel.setBackground(Color.white);
	    
	    //
	    initSettings();
	    
	    // set listener
	    yeadJCB.addActionListener(new ClockAction());
	    monthJCB.addActionListener(new ClockAction());
	    addCompsListeners();
	    
	    this.add(wholeJPanel);
	 
		// set visible
		setVisible(true);
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
	
	    public Clock (CalendarUI mf){
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
    	// 
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
					
					// get year
					mYear = Integer.parseInt(yeadJCB.getItemAt(
							yeadJCB.getSelectedIndex())) - 1900;
					// get month
					mMonth = Integer.parseInt(monthJCB.getItemAt(
							monthJCB.getSelectedIndex()));
					// get day
					mDay = Integer.parseInt(lastJL.getText());
				
					@SuppressWarnings("deprecation")
					Date notepadDate = new Date(mYear, mMonth - 1, mDay);
					
					EmailLogger.info("notepad date: " + notepadDate.toString());
					// 
					if (e.getClickCount() == 2) {
						EmailLogger.info("Prepare to write note?");
						
						int selectID = JOptionPane.showConfirmDialog(null, 
								"开始写记事？", "等待确认",
								JOptionPane.YES_NO_OPTION);
						if (selectID == JOptionPane.OK_OPTION) {
							EmailLogger.info("Start to write note.");
							
							sendNotepadFunc(notepadDate);
						}
			        }
				}
			});
		}
    }
    
    /**
	 * send email
	 */
	private void sendNotepadFunc(final Date date) {
		Thread thread = new Thread() {
			@Override  
            public void run() {  
              new NotepadUITest(date, null);
            }  
		};
		thread.start();
	}
}