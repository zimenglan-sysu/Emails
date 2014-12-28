package UI.Main;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Date;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

import DataInfo.EmailMessage;
import DataInfo.EnumType;
import EmailProcessor.Recieves.ReceivedPoP3Processor;
import Util.Email.PathManager;
import Util.Email.EmailDataManager;
import Util.Email.Logging.EmailLogger;
import UI.Calendar.CalendarUI;
import UI.Login.LoginUI;
import UI.Notes.NotepadUITest;
import UI.Notes.PreNotepadUI;
import UI.Recieves.View.ReceivedUI;
import UI.Recieves.View.ReceivedUITest;
import UI.Recieves.PreView.PreReceivedUI;
import UI.Sends.*;


/**
 * 登录后的主界面
 * 负责管理邮件list中的内容
 * @author ddk
 * @see 2014-12-07
 * @version 1.0
 */
public class MainUI extends JFrame {
	/**
	 * JToolBar
	 */
	// 菜单
	private JToolBar toolBar;
	/** 
	 * JButton
	 */
	// 写邮件按钮
	private JButton composedBtn;
	// 收邮件按钮
	private JButton receiveBtn;
	// 记事本按钮
	private JButton notepadBtn;
	// 联系人按钮
	private JButton contractorBtn;
	// 退出按钮
	private JButton quitBtn;
	/**
	 * JLabel
	 */
	JLabel calendarJL;
	/** 
	 * JTree
	 */
	// 左侧邮件账户: 收发邮箱, 垃圾邮箱, 草稿邮箱, 联系人
	private JTree accJTree;
	
	/** 
	 * JPanel
	 */
	// 邮件内容正文可直接放入这里显示
	private JPanel calendarJPanel;
	private JPanel contentJPanel;
	private static JPanel defaultJPanel;
	
	private JPanel leftTopPanel;
	private JPanel leftBottomPanel;
	
	private static JPanel leftPanel;
	private static JPanel middleJPanel;
	/**
	 * UI JPanel
	 */
	private static SendedUI SendedUIPanel;
	private static PreReceivedUI preViewJPanel;
	private static PreNotepadUI preNotepadJPanel;
	private static ReceivedUI receivedUIPanel;
	private static CalendarUI calendarUIPanel;
	/**
	 * CardLayout
	 */
	static CardLayout cardLayout;
	/**
	 * JEditorPane
	 */
	private JEditorPane editorPane;
	/**
	 * Variables
	 */
	private boolean isFinishedInit = false;
	private boolean isFirstpreViewReceivedJPanel = false;
	private boolean isFirstpreViewNotepadJPanel = false;
	/**
	 * singleton
	 */
	private static MainUI mainUI = null;

	// *****************************************************
	
	private MainUI() {
		
	}
	
	private MainUI(String title) {
		super(title);

		// 布局设计
		// 整体布局为BorderLayout
		setLayout(new BorderLayout());
		//
		this.initComps();
		// 
		this.initNorm();
		//
		this.isFinishedInit = true;
		//
		initCompsListener();
	}

	public static MainUI getMainUIInstance(String title) {
		if(mainUI == null) {
			if(title == null) {
				mainUI = new MainUI();
			} else {
				mainUI = new MainUI(title);
			}
		}
		
		return mainUI;
	}
	
	public static MainUI getInitedMainUIInstance(){
		return mainUI;
	}
	
	// *****************************************************
	
	public void initComps() {
		/** 
		 * JToolBar, 菜单栏
		 */
		// 初始化工具栏
		toolBar = new JToolBar();
		// 不可拖动
		toolBar.setFloatable(false);
		// 鼠标移动至组件上方才显示边框
		toolBar.setRollover(true);
		// add
		add(toolBar, BorderLayout.NORTH);
		// 写邮件按钮
		composedBtn = new JButton(EnumType.SendEmailBtnTitle, new ImageIcon(
				PathManager.getIconsResourcePath() + EnumType.SendEmailIcon));
		//
		toolBar.add(composedBtn);
		// 收取邮件按钮
		receiveBtn = new JButton(EnumType.ReceiveEmailBtnTitle, new ImageIcon(
				PathManager.getIconsResourcePath() + EnumType.ReceiveEmailIcon)
		);
		toolBar.add(receiveBtn);
		// 记事本按钮
		notepadBtn = new JButton(EnumType.NotepadEmailBtnTitle, new ImageIcon(
				PathManager.getIconsResourcePath() + EnumType.NotepadEmailIcon)
		);
		toolBar.add(notepadBtn);
		// 查看联系人按钮
		contractorBtn = new JButton(EnumType.ContractorEmailBtnTitle, new ImageIcon(
				PathManager.getIconsResourcePath() + EnumType.ContractorEmailIcon)
		);
		toolBar.add(contractorBtn);
		// 退出按钮
		quitBtn = new JButton(EnumType.QuitEmailBtnTitle, new ImageIcon(
				PathManager.getIconsResourcePath() + EnumType.QuitEmailIcon)
		);
		toolBar.add(quitBtn);

		/**
		 * 左侧上方账户邮件选择
		 */
		String emailBoxName = EnumType.EmailBoxName;
		DefaultMutableTreeNode muLTreeNode = new DefaultMutableTreeNode(
				emailBoxName);
		/* Un Do
		 * muLTreeNode.add(new DefaultMutableTreeNode(
		 * 		EnumType.SendedFolder));
		 * muLTreeNode.add(new DefaultMutableTreeNode(
		 * 		EnumType.DraftFolder));
		 * muLTreeNode.add(new DefaultMutableTreeNode(
		 * 		EnumType.RubbishFolder));
		 */
		muLTreeNode.add(new DefaultMutableTreeNode(
				EnumType.SendedEmailItem));
		muLTreeNode.add(new DefaultMutableTreeNode(
				EnumType.ReceivedFolder));
		muLTreeNode.add(new DefaultMutableTreeNode(
				EnumType.NotepadFolder));
		/**
		 * 左侧下方账户联系人选择
		 */
		String contactorName = EnumType.MAIL_CONTACTOR;
		DefaultMutableTreeNode muRTreeNode = new DefaultMutableTreeNode(
				contactorName);
		muRTreeNode.add(new DefaultMutableTreeNode(
				EnumType.RecentlyContactor));
		muRTreeNode.add(new DefaultMutableTreeNode(
				EnumType.UsuallyContactor));
		muRTreeNode.add(new DefaultMutableTreeNode(
				EnumType.StartContactor));
		/**
		 * 左侧栏
		 */
		String accEmailName = EnumType.MAIL_ACCOUNT + "(" +
				EmailDataManager.getData(
						EmailDataManager.getEmailAddr()).toString() + ")";
		DefaultMutableTreeNode accEmailTreeNode = new DefaultMutableTreeNode(
				accEmailName);
		accEmailTreeNode.add(muLTreeNode);
		accEmailTreeNode.add(muRTreeNode);

		// accJTree = new JTree(accEmailTreeNode);
		accJTree = new JTree(accEmailTreeNode);
		// 只能同时选中一个节点
		accJTree.getSelectionModel().setSelectionMode(
				TreeSelectionModel.SINGLE_TREE_SELECTION); 
		accJTree.setMinimumSize(new Dimension(
				EnumType.ACC_JTREE_WIDTH, EnumType.ACC_JTREE_HEIGHT));
		
		/**
		 * rows, cols
		 */
		leftPanel = new JPanel(new BorderLayout());
		leftTopPanel = new JPanel();
		leftTopPanel.add(accJTree);
		leftTopPanel.setBackground(Color.WHITE);
		//
		calendarUIPanel = new CalendarUI();
		//
		leftPanel.add(leftTopPanel, BorderLayout.CENTER);
		leftPanel.add(calendarUIPanel, BorderLayout.SOUTH);
		leftPanel.setMinimumSize(new Dimension(
				EnumType.ACC_JTREE_WIDTH, EnumType.ACC_JTREE_HEIGHT));
		leftPanel.setBackground(Color.white);
		leftPanel.setAutoscrolls(true);
		
		/**
		 * 中间:
		 * 		收邮件
		 * 		写邮件
		 */
		cardLayout = new CardLayout();
		middleJPanel = new JPanel(cardLayout);
		defaultJPanel = new JPanel();
		defaultJPanel.setSize(EnumType.ACC_JSCROLLPANE_WIDTH, 
				EnumType.ACC_JSCROLLPANE_HEIGHT);
		/**
		 * UI Panel
		 */
		// SendedUIPanel
		SendedUIPanel = new SendedUI(null);
		SendedUIPanel.setBackground(Color.red);
		SendedUIPanel.setSize(EnumType.ACC_JSCROLLPANE_WIDTH, 
				EnumType.ACC_JSCROLLPANE_HEIGHT);
		
		// preViewJPanel
		preViewJPanel = new PreReceivedUI();
		preViewJPanel.setBackground(Color.PINK);
		preViewJPanel.setSize(EnumType.ACC_JSCROLLPANE_WIDTH, 
				EnumType.ACC_JSCROLLPANE_HEIGHT);
		
		// receivedUIPanel
		receivedUIPanel = new ReceivedUI();
		receivedUIPanel.setBackground(Color.orange);
		receivedUIPanel.setSize(EnumType.ACC_JSCROLLPANE_WIDTH, 
				EnumType.ACC_JSCROLLPANE_HEIGHT);
		
		// preNotepadJPanel
		preNotepadJPanel = new PreNotepadUI();
		preNotepadJPanel.setBackground(Color.PINK);
		preNotepadJPanel.setSize(EnumType.ACC_JSCROLLPANE_WIDTH, 
				EnumType.ACC_JSCROLLPANE_HEIGHT);
		
		/**
		 *  CardLayout Settings
		 */
		middleJPanel.add(EnumType.DefaultEmailCardLayout, defaultJPanel);
		middleJPanel.add(EnumType.SendedEmailCardLayout, SendedUIPanel);
		middleJPanel.add(EnumType.PreReceivedEmailCardLayout, preViewJPanel);
		middleJPanel.add(EnumType.PreRNotepadEmailCardLayout, preNotepadJPanel);
		middleJPanel.add(EnumType.ReceivedEmailCardLayout, receivedUIPanel);
		
		middleJPanel.setSize(EnumType.ACC_JSCROLLPANE_WIDTH, 
				EnumType.ACC_JSCROLLPANE_HEIGHT);
		middleJPanel.setBackground(Color.LIGHT_GRAY);
		
		/**
		 * add JPanel instances
		 */
		add(leftPanel, BorderLayout.WEST);
		add(middleJPanel, BorderLayout.CENTER);
		// add(rightPanel, BorderLayout.EAST);
		
		/**
		 * JSplitPane 
		 */
		//添加分隔符  
		JSplitPane jSpeparator =new JSplitPane(
				JSplitPane.HORIZONTAL_SPLIT,leftPanel, middleJPanel);  
		//设置分割线宽度  
		final int thickness = 10;
		jSpeparator.setDividerSize(thickness);  
		//使分隔符不能上下拖动  
		jSpeparator.setEnabled(false);
		jSpeparator.setBackground(Color.red);
		jSpeparator.setForeground(Color.red);
		this.add(jSpeparator);
		
		// default UI
		cardLayout.show(middleJPanel, EnumType.DefaultEmailCardLayout);
	}

	/**
	 * Set some configurations for JFrame
	 */
	private void initNorm() {
		try {
			// 设置Nimbus皮肤
			UIManager.setLookAndFeel(
					"javax.swing.plaf.nimbus.NimbusLookAndFeel");

		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException 
				| UnsupportedLookAndFeelException e) {
			// 表示运行环境非JRE7
			e.printStackTrace();
		}
		// 
		SwingUtilities.updateComponentTreeUI(this);
		
		// set visible
		setVisible(true);
		// 设置背景颜色
		setBackground(EnumType.MAIN_UI_BG_COLOR);
		// 设置大小 (width, height)
		this.setSize(EnumType.MAIN_UI_WIDTH,
				EnumType.MAIN_UI_HEIGHT);

		// 禁止拉动窗口大小
		setResizable(EnumType.MAIN_UI_IS_RESIZE);
		
		// 居中窗口
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((screen.width - getSize().width) / 2, 
				(screen.height - getSize().height) / 2);
		
		// 设置可见
		this.setVisible(true);
		
		// 关闭窗口
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	}

	/**
	 * add and implement the listeners
     */
	public void initCompsListener() {
		/**
		 * 写邮件按钮
		 */
		composedBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int selectID = JOptionPane.showConfirmDialog(null, 
						"开始写邮件？", "等待确认",
						JOptionPane.YES_NO_OPTION);
				if (selectID == JOptionPane.OK_OPTION) {
					/*if(getInitedMainUIInstance().Logging() == false) {
						return;
					}
					getInitedMainUIInstance().cardLayout.show(middleJPanel, 
							EnumType.SendedEmailCardLayout);*/
					// getInitedMainUIInstance().showSendedUIPanelEmailCardLayout();
					
					sendEmailFunc();
				}
			}
		});
		
		/**
		 * 收取邮件按钮
		 */
		receiveBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int selectID = JOptionPane.showConfirmDialog(null, 
						"开始收邮件？", "等待确认",
						JOptionPane.YES_NO_OPTION);
				if (selectID == JOptionPane.OK_OPTION) {
					initPreViewReceivedUI();
				}
			}
		});
		
		/**
		 * 写记事本按钮
		 */
		notepadBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int selectID = JOptionPane.showConfirmDialog(null, 
						"开始写记事本？", "等待确认",
						JOptionPane.YES_NO_OPTION);
				if (selectID == JOptionPane.OK_OPTION) {
					sendNotepadFunc();
				}
			}
		});
		
		/**
		 * 退出按钮
		 */
		quitBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int selectID = JOptionPane.showConfirmDialog(null, 
						"退出邮箱？", "等待确认",
						JOptionPane.YES_NO_OPTION);
				if (selectID == JOptionPane.OK_OPTION) {
					dispose();
				}
			}
		});
		
		/**
		 * 邮箱账户-文件夹树形表
		 * 邮件列表
		 */
		accJTree.addTreeSelectionListener(new TreeSelectionListener() {
			@Override
			public void valueChanged(TreeSelectionEvent e) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) accJTree
						.getLastSelectedPathComponent();

				/* if noting is selected */
				if (node == null)
					return;
				
				Object nodeInfoObject = node.getUserObject();
				System.out.println(nodeInfoObject.toString());

				if (node.isLeaf()) {
					String strInfoStr = nodeInfoObject.toString();
					/* retrieve the node that was selected */
					/**
					 * 查看邮件(预览)
					 */
					if (EnumType.ReceivedFolder.equals(strInfoStr)) {
						EmailLogger.info("start receiving email");
						
						// set pre-view received email UI
						initPreViewReceivedUI();
					/**
					 * 发邮件
					 */
					} else if(EnumType.SendedEmailItem.equals(strInfoStr)) {
						EmailLogger.info("start sending email");
						
						// set send email UI
						sendEmailFunc();
					/**
					 * 记事本查看处理
					 */
					} else if(EnumType.NotepadFolder.equals(strInfoStr)) {
						EmailLogger.info("start receiving notepad");

						// set pre-view notepad email UI
						initPreViewNotepadUI();
						
					/**
					 * 邮箱联系人处理
					 */
					} else if(EnumType.MAIL_CONTACTOR.equals(strInfoStr)) {
						// To Do
						
					/**
					 * others 
					 */
					} else {
						// do nothing
					}
				}
			}
		});
	}
	
	/**
	 * send email
	 */
	private void sendEmailFunc() {
		Thread thread = new Thread() {
			@Override  
            public void run() {  
              new SendedUITest(null);
            }  
		};
		thread.start();
	}
	
	private void sendNotepadFunc() {
		Thread thread = new Thread() {
			@Override  
            public void run() {  
              new NotepadUITest(new Date(), null);;
            }  
		};
		thread.start();
	}
	/**
	 * initPreViewReceivedUI
	 */
	private void initPreViewReceivedUI() {
		boolean refresh = true;
		boolean isGetAttachment = EnumType.DefaultIsGetAttachment;
		EmailMessage[] messageList;
		try {
			// 
			if(isFirstpreViewReceivedJPanel == false) {
				isFirstpreViewReceivedJPanel = true;
				preViewJPanel.setPerMsgsLen(EnumType.DefaultPreViewPerMsgsLen);
				// the first page
				preViewJPanel.setPageNum(0);
				int pageNum = preViewJPanel.getPaeNum();
				int perMsgsLen = preViewJPanel.getPerMsgsLen();
				
				boolean isNotepad = false;
				messageList = ReceivedPoP3Processor.
						getReceivedPoP3ProcessorInstance().
						receive(isGetAttachment, pageNum, perMsgsLen, isNotepad);
				
				if(messageList == null || messageList.length < 1) {
					JOptionPane.showMessageDialog(null, "获取邮件失败");
					return;
				} else {
					EmailLogger.info("***********************");
					EmailLogger.info("成功获取邮件");
					EmailLogger.info("邮件个数: " + messageList.length);
				}
				
				preViewJPanel.setEmailMessage(messageList);
				preViewJPanel.setRealPageMsgLen(messageList.length);
				preViewJPanel.setInits();
				
				// show
				getInitedMainUIInstance().showPreViewReceivedEmailCardLayout();
			} else {
				preViewJPanel.RepaintView();
			}
		// 
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	/**
	 * initPreViewReceivedUI
	 */
	private void initPreViewNotepadUI() {
		boolean refresh = true;
		boolean isGetAttachment = EnumType.DefaultIsGetNotepadAttachment;
		EmailMessage[] messageList;
		try {
			// 
			if(isFirstpreViewNotepadJPanel== false) {
				isFirstpreViewNotepadJPanel = true;
				preNotepadJPanel.setPerMsgsLen(EnumType.DefaultPreViewPerMsgsLen);
				// the first page
				preNotepadJPanel.setPageNum(0);
				int pageNum = preNotepadJPanel.getPaeNum();
				int perMsgsLen = preNotepadJPanel.getPerMsgsLen();
				
				boolean isNotepad = true;
				messageList = ReceivedPoP3Processor.
						getReceivedPoP3ProcessorInstance().
						receive(isGetAttachment, pageNum, perMsgsLen, isNotepad);
				
				if(messageList == null || messageList.length < 1) {
					JOptionPane.showMessageDialog(null, "获取记事本失败");
					return;
				} else {
					EmailLogger.info("***********************");
					EmailLogger.info("成功获取记事本");
					EmailLogger.info("记事本个数: " + messageList.length);
				}
				
				preNotepadJPanel.setEmailMessage(messageList);
				preNotepadJPanel.setRealPageMsgLen(messageList.length);
				preNotepadJPanel.setInits();
				
				// show
				getInitedMainUIInstance().showPreViewNotepadCardLayout();
			} else {
				preNotepadJPanel.RepaintView();
			}
		// 
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	/**
	 * 调用不同的显示界面
	 */
	// get card layout
	public static CardLayout getCardLayout() {
		
		return getInitedMainUIInstance().cardLayout;
	}
	
	// send email UI
	public static void showSendedUIPanelEmailCardLayout() {
		if(getInitedMainUIInstance().Logging() == false) {
			return;
		}
		
		//
		getInitedMainUIInstance().cardLayout.show(middleJPanel, 
				EnumType.SendedEmailCardLayout);
		//
		getInitedMainUIInstance().setSendedUIPanelVisible();
	}
	
	// pre-viewed received email UI
	public static void showPreViewReceivedEmailCardLayout() {
		if(getInitedMainUIInstance().Logging() == false) {
			return;
		}
		
		//
		getInitedMainUIInstance().cardLayout.show(middleJPanel, 
				EnumType.PreReceivedEmailCardLayout);
		// 
		getInitedMainUIInstance().setPreReceivedEmailUIPanelVisible();
	}
	
	// pre-viewed received email UI
	public static void showPreViewNotepadCardLayout() {
		if(getInitedMainUIInstance().Logging() == false) {
			return;
		}
		
		//
		getInitedMainUIInstance().cardLayout.show(middleJPanel, 
				EnumType.PreRNotepadEmailCardLayout);
		// 
		getInitedMainUIInstance().setPreNotepadUIPanelVisible();
	}
	
	// single received email UI
	public static void showReceivedUIPanelEmailCardLayout(
			EmailMessage message) {
		if(getInitedMainUIInstance().Logging() == false) {
			return;
		}
		//
		getInitedMainUIInstance().receivedUIPanel.setInits(message);
		//
		getInitedMainUIInstance().cardLayout.show(middleJPanel, 
				EnumType.ReceivedEmailCardLayout);
		// 
		getInitedMainUIInstance().setReceivedUIPanelVisible();
	}
	
	/**
	 * 
	 */
	public static boolean Logging() {
		if(getInitedMainUIInstance() == null) {
			String msgStr = "before call this getInitedMainUIInstance function, "
					+ "please instance MainUI first";
			EmailLogger.info(msgStr);
			//
			JOptionPane.showMessageDialog(null, msgStr);
			
			return false;
		}
		
		return true;
	}
	
	/**
	 * 
	 */
	// default
	private static void setDefaultJPanelVisible() {
		final boolean visible = true;
		final boolean inVisible = false;
		
		defaultJPanel.setVisible(visible);
		SendedUIPanel.setVisible(inVisible);
		preViewJPanel.setVisible(inVisible);
		receivedUIPanel.setVisible(inVisible);
		preNotepadJPanel.setVisible(inVisible);
	}
	
	// send
	private static void setSendedUIPanelVisible() {
		final boolean visible = true;
		final boolean inVisible = false;
		
		defaultJPanel.setVisible(inVisible);
		SendedUIPanel.setVisible(visible);
		preViewJPanel.setVisible(inVisible);
		receivedUIPanel.setVisible(inVisible);
		preNotepadJPanel.setVisible(inVisible);
	}
	
	// pre-view receive
	private static void setPreReceivedEmailUIPanelVisible() {
		final boolean visible = true;
		final boolean inVisible = false;
		
		defaultJPanel.setVisible(inVisible);
		SendedUIPanel.setVisible(inVisible);
		preViewJPanel.setVisible(visible);
		receivedUIPanel.setVisible(inVisible);
		preNotepadJPanel.setVisible(inVisible);
	}
	
	// pre-view receive
	private static void setPreNotepadUIPanelVisible() {
		final boolean visible = true;
		final boolean inVisible = false;
		
		defaultJPanel.setVisible(inVisible);
		SendedUIPanel.setVisible(inVisible);
		preViewJPanel.setVisible(inVisible);
		receivedUIPanel.setVisible(inVisible);
		preNotepadJPanel.setVisible(visible);
	}

	// receive
	private static void setReceivedUIPanelVisible() {
		final boolean visible = true;
		final boolean inVisible = false;

		defaultJPanel.setVisible(inVisible);
		SendedUIPanel.setVisible(inVisible);
		preViewJPanel.setVisible(inVisible);
		receivedUIPanel.setVisible(visible);
		preNotepadJPanel.setVisible(inVisible);
	}
	
	/*
	 * for test
	 */
	 public static void main(String[] args) {
		 //
		 // PathManager.createAllDirs();
		 
		 // default login
		 EmailDataManager.DefaultLogin();
			
		 // MainUI mainUI = new MainUI(EnumType.ZIMENGLAN_CLIENT_TITLE);
		 MainUI mainUI = getMainUIInstance(EnumType.ZIMENGLAN_CLIENT_TITLE);
	  }
}