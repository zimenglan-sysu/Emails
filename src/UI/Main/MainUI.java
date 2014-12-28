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
 * ��¼���������
 * ��������ʼ�list�е�����
 * @author ddk
 * @see 2014-12-07
 * @version 1.0
 */
public class MainUI extends JFrame {
	/**
	 * JToolBar
	 */
	// �˵�
	private JToolBar toolBar;
	/** 
	 * JButton
	 */
	// д�ʼ���ť
	private JButton composedBtn;
	// ���ʼ���ť
	private JButton receiveBtn;
	// ���±���ť
	private JButton notepadBtn;
	// ��ϵ�˰�ť
	private JButton contractorBtn;
	// �˳���ť
	private JButton quitBtn;
	/**
	 * JLabel
	 */
	JLabel calendarJL;
	/** 
	 * JTree
	 */
	// ����ʼ��˻�: �շ�����, ��������, �ݸ�����, ��ϵ��
	private JTree accJTree;
	
	/** 
	 * JPanel
	 */
	// �ʼ��������Ŀ�ֱ�ӷ���������ʾ
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

		// �������
		// ���岼��ΪBorderLayout
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
		 * JToolBar, �˵���
		 */
		// ��ʼ��������
		toolBar = new JToolBar();
		// �����϶�
		toolBar.setFloatable(false);
		// ����ƶ�������Ϸ�����ʾ�߿�
		toolBar.setRollover(true);
		// add
		add(toolBar, BorderLayout.NORTH);
		// д�ʼ���ť
		composedBtn = new JButton(EnumType.SendEmailBtnTitle, new ImageIcon(
				PathManager.getIconsResourcePath() + EnumType.SendEmailIcon));
		//
		toolBar.add(composedBtn);
		// ��ȡ�ʼ���ť
		receiveBtn = new JButton(EnumType.ReceiveEmailBtnTitle, new ImageIcon(
				PathManager.getIconsResourcePath() + EnumType.ReceiveEmailIcon)
		);
		toolBar.add(receiveBtn);
		// ���±���ť
		notepadBtn = new JButton(EnumType.NotepadEmailBtnTitle, new ImageIcon(
				PathManager.getIconsResourcePath() + EnumType.NotepadEmailIcon)
		);
		toolBar.add(notepadBtn);
		// �鿴��ϵ�˰�ť
		contractorBtn = new JButton(EnumType.ContractorEmailBtnTitle, new ImageIcon(
				PathManager.getIconsResourcePath() + EnumType.ContractorEmailIcon)
		);
		toolBar.add(contractorBtn);
		// �˳���ť
		quitBtn = new JButton(EnumType.QuitEmailBtnTitle, new ImageIcon(
				PathManager.getIconsResourcePath() + EnumType.QuitEmailIcon)
		);
		toolBar.add(quitBtn);

		/**
		 * ����Ϸ��˻��ʼ�ѡ��
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
		 * ����·��˻���ϵ��ѡ��
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
		 * �����
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
		// ֻ��ͬʱѡ��һ���ڵ�
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
		 * �м�:
		 * 		���ʼ�
		 * 		д�ʼ�
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
		//��ӷָ���  
		JSplitPane jSpeparator =new JSplitPane(
				JSplitPane.HORIZONTAL_SPLIT,leftPanel, middleJPanel);  
		//���÷ָ��߿��  
		final int thickness = 10;
		jSpeparator.setDividerSize(thickness);  
		//ʹ�ָ������������϶�  
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
			// ����NimbusƤ��
			UIManager.setLookAndFeel(
					"javax.swing.plaf.nimbus.NimbusLookAndFeel");

		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException 
				| UnsupportedLookAndFeelException e) {
			// ��ʾ���л�����JRE7
			e.printStackTrace();
		}
		// 
		SwingUtilities.updateComponentTreeUI(this);
		
		// set visible
		setVisible(true);
		// ���ñ�����ɫ
		setBackground(EnumType.MAIN_UI_BG_COLOR);
		// ���ô�С (width, height)
		this.setSize(EnumType.MAIN_UI_WIDTH,
				EnumType.MAIN_UI_HEIGHT);

		// ��ֹ�������ڴ�С
		setResizable(EnumType.MAIN_UI_IS_RESIZE);
		
		// ���д���
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((screen.width - getSize().width) / 2, 
				(screen.height - getSize().height) / 2);
		
		// ���ÿɼ�
		this.setVisible(true);
		
		// �رմ���
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	}

	/**
	 * add and implement the listeners
     */
	public void initCompsListener() {
		/**
		 * д�ʼ���ť
		 */
		composedBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int selectID = JOptionPane.showConfirmDialog(null, 
						"��ʼд�ʼ���", "�ȴ�ȷ��",
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
		 * ��ȡ�ʼ���ť
		 */
		receiveBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int selectID = JOptionPane.showConfirmDialog(null, 
						"��ʼ���ʼ���", "�ȴ�ȷ��",
						JOptionPane.YES_NO_OPTION);
				if (selectID == JOptionPane.OK_OPTION) {
					initPreViewReceivedUI();
				}
			}
		});
		
		/**
		 * д���±���ť
		 */
		notepadBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int selectID = JOptionPane.showConfirmDialog(null, 
						"��ʼд���±���", "�ȴ�ȷ��",
						JOptionPane.YES_NO_OPTION);
				if (selectID == JOptionPane.OK_OPTION) {
					sendNotepadFunc();
				}
			}
		});
		
		/**
		 * �˳���ť
		 */
		quitBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int selectID = JOptionPane.showConfirmDialog(null, 
						"�˳����䣿", "�ȴ�ȷ��",
						JOptionPane.YES_NO_OPTION);
				if (selectID == JOptionPane.OK_OPTION) {
					dispose();
				}
			}
		});
		
		/**
		 * �����˻�-�ļ������α�
		 * �ʼ��б�
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
					 * �鿴�ʼ�(Ԥ��)
					 */
					if (EnumType.ReceivedFolder.equals(strInfoStr)) {
						EmailLogger.info("start receiving email");
						
						// set pre-view received email UI
						initPreViewReceivedUI();
					/**
					 * ���ʼ�
					 */
					} else if(EnumType.SendedEmailItem.equals(strInfoStr)) {
						EmailLogger.info("start sending email");
						
						// set send email UI
						sendEmailFunc();
					/**
					 * ���±��鿴����
					 */
					} else if(EnumType.NotepadFolder.equals(strInfoStr)) {
						EmailLogger.info("start receiving notepad");

						// set pre-view notepad email UI
						initPreViewNotepadUI();
						
					/**
					 * ������ϵ�˴���
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
					JOptionPane.showMessageDialog(null, "��ȡ�ʼ�ʧ��");
					return;
				} else {
					EmailLogger.info("***********************");
					EmailLogger.info("�ɹ���ȡ�ʼ�");
					EmailLogger.info("�ʼ�����: " + messageList.length);
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
					JOptionPane.showMessageDialog(null, "��ȡ���±�ʧ��");
					return;
				} else {
					EmailLogger.info("***********************");
					EmailLogger.info("�ɹ���ȡ���±�");
					EmailLogger.info("���±�����: " + messageList.length);
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
	 * ���ò�ͬ����ʾ����
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