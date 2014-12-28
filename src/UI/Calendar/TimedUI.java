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
	private JButton sureBtn= new JButton("ȷ��");
	private JButton quitBtn = new JButton("ȡ��");
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
	private JLabel yearJL = new JLabel("��");
	private JLabel monthJL = new JLabel("��");
	private JLabel hourJL = new JLabel("ʱ");
	private JLabel minuteJL = new JLabel("��");
	private JLabel secondJL = new JLabel("��");
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
    //�õ�һ����ǰʱ�����������
	private Calendar now = Calendar.getInstance();
	
	private CallBack callBack;
	
	// *****************************************************
	
    public TimedUI(CallBack callBack) {
    	this.callBack = callBack;
    	
    	// set title
    	// super(title);
    	setSize(mWidth, mHeight);
    	//��ȡ��Ļ���
	    x_size = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	    //��ȡ��Ļ�߶�
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
	      // ����ʾ���ַ�����Ϊ����
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
	    //��ӷָ���  
  		JSplitPane jSpeparator =new JSplitPane(JSplitPane.VERTICAL_SPLIT, 
  				firstJPanel, secondJPanel);  
  		//���÷ָ��߿��  
  		final int thickness = 6;
  		jSpeparator.setDividerSize(thickness);  
  		//ʹ�ָ������������϶�  
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
		// ��ֹ�������ڴ�С
		setResizable(isResizable);
		// ���ÿɼ�
		// �رմ���
		setDefaultLookAndFeelDecorated(true);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    public 
    class ClockAction implements ActionListener {
	    public void actionPerformed(ActionEvent e) {
	    	//
	    	int c_year, c_month, c_week;
		    // �õ���ǰ��ѡ���
		    c_year = Integer.parseInt(yeadJCB.getSelectedItem().toString()); 
		    // �õ���ǰ�·�, ����1, ������е���Ϊ: 0-11
		    c_month = Integer.parseInt(monthJCB.getSelectedItem().toString()) - 1; 
		    
		    // ���ú���use, �õ����ڼ�
		    c_week = use(c_year, c_month);
		    // ���ú���Resetday
		    Resetday(c_week, c_year, c_month); 
	    }
    }

    public void initSettings() {
    	int year, monthNum, firstDayNum;
    	int hour, minute, second;
    	int day;
    	
    	String log[] = { "��", "һ", "��", "��", "��", "��", "��" };
    	// 
    	for (int i = 0; i < 7; i++) {
    		dayJL[i].setText(log[i]);
    	}
    	// �������յ���������Ϊmagenta
    	for (int i = 0; i < dayNum; i = i + 7) {
    		dayJL[i].setForeground(Color.magenta); 
    	}
    	// ������������������Ϊred
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
	    
	    // �õ���ǰʱ���`��,��,ʱ,��,��`
	    monthNum = (int)(now.get(Calendar.MONTH)); 
	    year = (int)(now.get(Calendar.YEAR)); 
	    day = (int)(now.get(Calendar.DATE));
	    hour = (int)(now.get(Calendar.HOUR_OF_DAY));
	    minute = (int)(now.get(Calendar.MINUTE));
	    second = (int)(now.get(Calendar.SECOND));
	    
	    // ���������б���ʾΪ��ǰ`��,��`
	    yeadJCB.setSelectedIndex(year - 1); 
	    monthJCB.setSelectedIndex(monthNum); 
	    
	    // ���������б���ʾΪ��ǰ`ʱ,��,��`
	    hourJCB.setSelectedIndex(hour);
	    minuteJCB.setSelectedIndex(minute);
	    secondJCB.setSelectedIndex(second);
	    
	    // ���ú���use, �õ����ڼ�
	    firstDayNum = use(year, monthNum);
	    // ���ú���Resetday
	    Resetday(firstDayNum, year, monthNum);
    }

    public int use(int reYear, int reMonth) {
	    int weekNum;
	    // ����ʱ��Ϊ��Ҫ��ѯ�����µĵ�һ��
	    now.set(reYear, reMonth, 1); 
	    // ��ȡ��ǰ�����������еĵڼ��� ��1-7��Ӧ ��-�� 
	    weekNum = (int) (now.get(Calendar.DAY_OF_WEEK));
	   
	    return weekNum;
    }

    public void Resetday(int weekLog, int yearLog, int monthLog) {
    	// �洢�·ݵ�����
    	int monthDayScore; 
	    int count = 1;
	    monthDayScore = 0;
	
	    now = Calendar.getInstance();	
	    int day = now.get(Calendar.DATE);
	    // EmailLogger.info("day: " + day);
	    now.set(yearLog + 1900, monthLog, 1);
	    
	    // �õ�һ���������һ��
	    monthDayScore = now.getActualMaximum(Calendar.DAY_OF_MONTH);
	    // ��ǰ���������·ݵĵڼ��� 
	    
	    // ��ʼ����ǩ(��ȥǰ�߸���ǩ���±�ֵ)
	    for (int i = 0; i < weekLog; i++) { 
	    	dayJL[i + 7].setText("");
	    }
	    //
	    monthDayScore = monthDayScore + weekLog;
	    //
	    for (int i = weekLog; i < monthDayScore; i++) {
	    	//����ǰ�߸���ǩ���±�ֵ
	    	dayJL[i + 6].setText("" + count);
	    	count++;
	    }
	    //
	    for (int i = monthDayScore + 6; i < dayNum; i++) {
	    	//����ǰ�߸���ǩ���±�ֵ
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
		    t=new Thread(this);			 //ʵ�����߳�
		    t.start();				    			
	    }

	    @Override
	    public void run() {
	    	while(true) {
	    		try {
	    			//����1����
	    			Thread.sleep(1000);    			
	    		} catch (InterruptedException e) {
	    			System.out.println("�����쳣");
	    		}   	
	
	    		//��100�������ػ����
	    		repaint(100);
	    	}
        }

        public void paint(Graphics g) {
    	    // set font
    	    Font f=new Font("����",Font.BOLD,16);

    	    //��ʽ��ʱ����ʾ����
    	    SimpleDateFormat SDF=new SimpleDateFormat(
    	    		"yyyy'��'MM'��'dd'��'HH:mm:ss");

            Calendar now=Calendar.getInstance();
            //�õ���ǰ���ں�ʱ��
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