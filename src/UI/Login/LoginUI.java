package UI.Login;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter; 
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;

import DataInfo.EnumType;
import Util.Email.CheckEmail;
import Util.Email.EmailDataManager;
import UI.Main.MainUI;;

public class LoginUI extends JFrame 
		implements MouseListener, ActionListener {
	// JLabels
	private JLabel messageJL;
	private JLabel addrJLabel;
	private JLabel pswdJLabel;
	// ������Ϣ��ʾ
	private JLabel txtErrorMsg;
	// JTextField
	// �����ַ�����
	private JTextField addrJTF;
	// JComboBox
	// ֧�ֵ����������
	private JComboBox<String> emailJCBHost;
	// JPasswordField
	// �������������
	private JPasswordField pswdJPF;
	// buttons
	// ��¼��ť
	private JButton loginBtn;
	// ���ð�ť
	private JButton resetBtn;
	// 
	JPanel loginPnl;
	// ����Ƿ��ʼ��
	private boolean isInitComps = false;
	// �߼�������
	private CheckEmail cEmailer = new CheckEmail();
	
	// ************************************************************
	
	public LoginUI() {
		// initialize components
		initComps();
		// init the frame
		initSetting();
		
		// ��ʼ�����
		isInitComps = true;
		// add listener for components
		addCompsListeners();
	}
	
	// read from property file
	public String[] getEmailHosts() {
		// currently do nothing
		return null;
	}
	
	private void initComps() {
		// JPanel
		loginPnl = new JPanel();
		// JLabel
		messageJL = new JLabel(EnumType.MSG_WRITING_INIT);
		messageJL.addMouseListener(this);
		messageJL.setSize(new Dimension(100, 60));
		
		addrJLabel = new JLabel("email addr : ");
		addrJLabel.addMouseListener(this);
		pswdJLabel = new JLabel("email pswd : ");
		pswdJLabel.addMouseListener(this);
		txtErrorMsg = new JLabel("");
		// JComboBox<String>
		emailJCBHost = new JComboBox<String>(new String[] { 
			"126.com",
			"foxmail.com",
			"qq.com",
			"gmail.com",
			"163.com", 
			"vip.163.com", 
			"mail.qq.com",
			"sina.com.cm",
			"mail.sina.com.cn",
			"yahoo.com",
			"mail.yahoo.com",
			"mail.google.com",
			"yeah.net",
			"netease.com",
			"vip.sina.com",
			"souhu.com"
		});
		
		// JTextField
		addrJTF = new JTextField(20);
		
		// JPasswordField
		pswdJPF = new JPasswordField(20);
		
		/**
		 * for test
		 */
		// addrJTF.setText(EmailDataManager.getDefaultEmailAddr()
		//		.substring(0, EmailDataManager.
		//				getDefaultEmailAddr().indexOf("@")));
		// pswdJPF.setText(EmailDataManager.getDefaultEmailPswd());
		
		// button
		loginBtn = new JButton("Login");
		resetBtn = new JButton("Reset");
		// set layout
		setLayout(new BorderLayout(5, 5));

		//�Ϸ�����ʾ����
		JPanel topPanel = new JPanel();
		topPanel.add(messageJL);
		topPanel.add(txtErrorMsg);
		loginPnl.add(topPanel, BorderLayout.NORTH);
		
		// �м����ʾ����, ����2�������1�д�����Ϣ��ʾ
		GridBagLayout centerLayout = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		JPanel centerPanel = new JPanel(centerLayout);

		c.gridwidth = 1;
		centerLayout.setConstraints(addrJLabel, c);
		centerLayout.setConstraints(addrJTF, c);
		centerPanel.add(addrJLabel);
		centerPanel.add(addrJTF);
		c.gridwidth = GridBagConstraints.REMAINDER;
		centerLayout.setConstraints(emailJCBHost, c);
		centerPanel.add(emailJCBHost);

		c.gridwidth = 1;
		centerLayout.setConstraints(pswdJLabel, c);
		centerPanel.add(pswdJLabel);
		c.gridwidth = GridBagConstraints.REMAINDER;
		
		centerLayout.setConstraints(pswdJPF, c);
		centerPanel.add(pswdJPF);
		
		// 
		loginPnl.add(centerPanel, BorderLayout.CENTER);

		// �·��İ�ť����
		JPanel bottomPanel = new JPanel();
		bottomPanel.add(loginBtn);
		bottomPanel.add(resetBtn);
		loginPnl.add(bottomPanel, BorderLayout.SOUTH);

		// add loginPnl 
		add(loginPnl);
	}

	private void initSetting() {
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
		SwingUtilities.updateComponentTreeUI(this);
		
		// set title
		setTitle(EnumType.ZIMENGLAN_LOGIN_TITLE);
		// set visible
		setVisible(true);
		// ���ñ�����ɫ
		setBackground(EnumType.LOGIN_BG_COLOR);
		// ���ô�С
		this.setSize(480,200);

		// ��ֹ�������ڴ�С
		setResizable(false);
		
		// ���д���
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((screen.width - getSize().width) / 2, 
				(screen.height - getSize().height) / 2);
		
		// ���ÿɼ�
		this.setVisible(true);
		
		// �رմ���
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	}

	public boolean createMainFrame() {
		return true;
	}
	
	private void addCompsListeners() {
		if (!isInitComps) {
			return;
		}
		loginBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if ("".equals(addrJTF.getText().trim())) {
					 messageJL.setForeground(Color.red);
				     messageJL.setText(EnumType.WRONG_EMAIL_ADDR_MSG);
				     addrJTF.setText("");
				     return;
				}
				if ("".equals(new String(pswdJPF.getPassword()).trim())) {
					 messageJL.setForeground(Color.red);
				     messageJL.setText(EnumType.WRONG_EMAIL_PSWD_MSG);
				     pswdJPF.setText("");
				     return;
				}
				
				// get host
				String host = emailJCBHost.getItemAt(emailJCBHost
						.getSelectedIndex());
				// get username
				String userName = addrJTF.getText().trim() + "@" + host;
				if(!userName.matches(EnumType.REGEX_EMAIL_ADDRESS)) {
					 messageJL.setForeground(Color.orange);
				     messageJL.setText(EnumType.INVALID_EMAIL_ADDR_MSG);
				     // addrJTF.setText("");
				     return;
				}
				
				// get password
				String userPswd = new String(pswdJPF.getPassword()).trim();
				
				messageJL.setText(EnumType.LOGINING);
				messageJL.setForeground(Color.red);
				boolean result = CheckEmail.checkEmailAccount(
						EnumType.MAIL_SMTP_HOSTTYPE,
						host,"smtp", userName, userPswd
				);
				
				if (result) {
					messageJL.setText(EnumType.LOGIN_SUCCESS_MSG);
					int selectID = JOptionPane.showConfirmDialog(null,
							"�ɹ������˺ţ����Ͻ��룿", "", 
							JOptionPane.OK_CANCEL_OPTION);
					// ********************************************************
					// maybe modify 
					if (selectID == JOptionPane.OK_OPTION) {
						JOptionPane.showMessageDialog(null, "����ɹ���");
						EmailDataManager.addData(
								EmailDataManager.getEmailAddr(), userName);
						EmailDataManager.addData(
								EmailDataManager.getEmailPswd(), userPswd);
						String smtpHostName = EnumType.MAIL_SMTP_HOSTTYPE + "." + 
								userName.substring(userName.indexOf("@") + 1);
						String pop3HostName = EnumType.MAIL_POP3_HOSTTYPE+ "." + 
								userName.substring(userName.indexOf("@") + 1);
						String imapHostName = EnumType.MAIL_IMAP_HOSTTYPE + "." + 
								userName.substring(userName.indexOf("@") + 1);
						
						System.out.println("longin - host name:" + smtpHostName);
						EmailDataManager.addData(
								EmailDataManager.getSmtpEmailHost(), smtpHostName);
						EmailDataManager.addData(
								EmailDataManager.getPop3EmailHost(), pop3HostName);
						EmailDataManager.addData(
								EmailDataManager.getImapEmailHost(), imapHostName);
						
						// �����ʼ��ͻ��˵�������
						// JFrame frame = new MainUI(EnumType.ZIMENGLAN_CLIENT_TITLE);
						MainUI.getMainUIInstance(EnumType.ZIMENGLAN_CLIENT_TITLE);
						dispose();
					}
				} else {
					if (host.contains("qq.com")) {
						messageJL.setText(EnumType.LOGIN_VALIDED_FAIL_MSG);
					} else {
						messageJL.setText(EnumType.LOGIN_MAIN_FAIL_MSG);
					}
				}
			}
		});
		
		resetBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//
				addrJTF.setText("");
				pswdJPF.setText("");
			}
		});
	}

	/**
	 * {@link MouseListener}
	 */
	// Click
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub	
	}
	// Enterd
	@Override
	public void mouseEntered(MouseEvent e) {
		//
		if(e.getSource() instanceof JLabel) {
			//
			JLabel tempJL= (JLabel)e.getSource();
			tempJL.setForeground(Color.RED);
			tempJL.setFont(tempJL.getFont().deriveFont(Font.BOLD));
			tempJL.setCursor(new Cursor(Cursor.HAND_CURSOR));
		} else if(e.getSource() instanceof JTextField) {
			//
			JTextField tempJTF = (JTextField)e.getSource();
			tempJTF.setForeground(Color.RED);
			tempJTF.setFont(tempJTF.getFont().deriveFont(Font.BOLD));
			tempJTF.setCursor(new Cursor(Cursor.HAND_CURSOR));
		} else {
			// To Do
		}
	}
	
	// Exited
	@Override
	public void mouseExited(MouseEvent e) {
		//
		if(e.getSource() instanceof JLabel) {
			//
			JLabel tempJL = (JLabel)e.getSource();
			tempJL.setForeground(Color.BLACK);
			tempJL.setFont(tempJL.getFont().deriveFont(Font.PLAIN));
			tempJL.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		} else if(e.getSource() instanceof JTextField) {
			//
			JTextField tempJTF = (JTextField)e.getSource();
			tempJTF.setForeground(Color.BLACK);
			tempJTF.setFont(tempJTF.getFont().deriveFont(Font.PLAIN));
			tempJTF.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));	
		} else {
			// To Do
		}
	}
	// Pressed
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
	}
	// Released
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	public void setWarningMsg(String msg) {
		txtErrorMsg.setFont(EnumType.LOGIN_WARN_FONT);
		txtErrorMsg.setText(msg);
	}

	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}