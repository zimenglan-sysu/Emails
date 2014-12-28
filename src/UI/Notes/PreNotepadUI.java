package UI.Notes;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import DataInfo.EmailMessage;
import DataInfo.EnumType;
import EmailProcessor.Recieves.ReceivedPoP3Processor;
import Util.Email.Logging.EmailLogger;

public class PreNotepadUI extends JPanel {
	/**
	 * variables
	 */
	private int totalMsgLen;			// record the length of whole messages
	private int pageNum;				// record the current page number
	private static final int DefaultPerMsgsLen = 
			EnumType.DefaultPreViewPerMsgsLen; // show default value: 10
	private int perMsgsLen;		// the maximum of messages in each page 
	private int realPageMsgLen;			// current number of messages in current page
	private boolean refresh;
	private EmailMessage[] messages;
	/**
	 * Variables
	 */
	JLabel pageJLabel;
	/**
	 * JButton
	 */
	JButton preBtn;
	JButton nextBtn;
	/**
	 * JPanel
	 */
	JPanel bottomJPanel = null;
	JPanel topJPanel = null;
	/**
	 * JScrollPane
	 */
	JScrollPane topJScrollPane;
	/**
	 * PreViewNotepadUI
	 */
	PreViewNotepadUI[] PreViewNotepadUI = null;
	
	// ************************************************************
	
	public PreNotepadUI() {
		pageNum = 0;
		perMsgsLen = DefaultPerMsgsLen;
		
		setLayout(new BorderLayout());
		setBackground(Color.WHITE);
		setVisible(true);
	}
	
	public PreNotepadUI(EmailMessage[] messages, int perMsgsLen) {	
		this.pageNum = 0;
		this.totalMsgLen = messages.length;
		this.messages = messages;
		this.perMsgsLen = perMsgsLen;
		
		setLayout(new BorderLayout());
		//
		setInits();
		
		setBackground(Color.WHITE);
		setVisible(true);
	}
	
	// **************************************************************
	
	/**
	 * addPreViewNotepadUI
	 */
	private void addPreViewNotepadUI(boolean refresh) {
		//
		realPageMsgLen = totalMsgLen > perMsgsLen 
				? perMsgsLen : totalMsgLen;
		EmailLogger.info("realPageMsgLen: " + realPageMsgLen);
		
		// rows, cols - include JLable and JSplitPane
		if(topJPanel == null) {
			topJPanel = new JPanel(new GridLayout(this.realPageMsgLen - 1, 1));
		}
		if(refresh == true) {
			topJPanel.removeAll();
			// return;
		}
		// topJPanel = new JPanel(new GridLayout(this.realPageMsgLen - 1, 1));
		
		PreViewNotepadUI = new PreViewNotepadUI[realPageMsgLen];
		
		// create PreViewNotepadUI
		for(int idx = 0; idx < realPageMsgLen; idx++) {
			EmailMessage msg = messages[idx];
			if(msg == null) {
				EmailLogger.warning("message is null");
				continue;
			}
			// instance
			PreViewNotepadUI[idx] = new PreViewNotepadUI(msg, 
					new PreViewNotepadUI.CallBack() {
						@Override
						public void call(final Date date, final EmailMessage msg) {
							//
							Thread thread = new Thread() {
								@Override  
			                    public void run() {  
			                      new NotepadUITest(date, msg);
			                    }  
							};
							thread.start();
						}
				}
			);
		}
		
		// add PreViewNotepadUI and JSplitPane
		for(int idx = 0; idx < realPageMsgLen; idx++) {
			// add
			topJPanel.add(PreViewNotepadUI[idx]);	
			
			if(idx == 0) {
				continue;
			}
			
			//添加分隔符  
			JSplitPane jSpeparator = new JSplitPane(
					JSplitPane.VERTICAL_SPLIT, PreViewNotepadUI[idx - 1], 
					PreViewNotepadUI[idx]); 
			
			//设置分割线宽度  
			final int thickness = 6;
			jSpeparator.setDividerSize(thickness);  
			
			//使分隔符不能上下拖动  
			jSpeparator.setEnabled(false);
			jSpeparator.setBackground(Color.red);
			jSpeparator.setVisible(true);
			topJPanel.add(jSpeparator);
		}
	}
	/**
	 * addTopComps
	 */
	private void addTopComps() {
		// add pre-viewed received email UI
		addPreViewNotepadUI(false);
		
		// topJScrollPane.add(topJPanel);	// error !!
		topJScrollPane = new JScrollPane(topJPanel);
		add(topJScrollPane, BorderLayout.CENTER);
	}
	/**
	 * addBottomComps
	 */
	private void addBottomComps() {
		// Button
		preBtn = new JButton("Pre");
		nextBtn = new JButton("Next");
		//
		preBtn.setEnabled(false);
		if(totalMsgLen < perMsgsLen) {
			nextBtn.setEnabled(false);
		}
		
		// bottom JPanel
		bottomJPanel = new JPanel();
		bottomJPanel.add(preBtn);
		String pageString = "(" + pageNum + "页) - [" 
				+ (pageNum * perMsgsLen) +  ": " 
				+ (pageNum * perMsgsLen + realPageMsgLen - 1)
				+ "]";
		pageJLabel = new JLabel(pageString);
		bottomJPanel.add(pageJLabel);
		bottomJPanel.add(nextBtn);
		add(bottomJPanel, BorderLayout.SOUTH);
		JOptionPane.showMessageDialog(null, "初始化邮件页面成功");
	}
	/**
	 * addComps()
	 */
	private void addComps() {
		addTopComps();
		addBottomComps();
	}
	/**
	 * Listeners
	 */
	private void addCompsListeners() {
		// Pre
		preBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(pageNum <=0) {
					JOptionPane.showMessageDialog(null, 
							"已经是最新一页啦");
					preBtn.setEnabled(false);
					if(realPageMsgLen < perMsgsLen) {
						nextBtn.setEnabled(false);
					} else {
						nextBtn.setEnabled(true);
					}
					
					return;
				}
				//
				pageNum--;
				
				boolean isGetAttachment = EnumType.DefaultIsGetAttachment;
				EmailMessage[] messageList = null;
				try {
					boolean isNotepad = true;
					messageList = ReceivedPoP3Processor.
							getReceivedPoP3ProcessorInstance().
							receive(isGetAttachment, pageNum, perMsgsLen, isNotepad);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				if(messageList == null || messageList.length < 1) {
					JOptionPane.showMessageDialog(null, "获取记事本失败");
					// set back
					pageNum++;
					if(pageNum != 0) {
						preBtn.setEnabled(true);
					}
					// nextBtn.setEnabled(false);
					return;
				}
				// log & set messages
				EmailLogger.info("***********************");
				EmailLogger.info("成功获取记事本");
				EmailLogger.info("第" + pageNum + "页, 记事本个数: " 
						+ messageList.length);
				
				setEmailMessage(messageList);
				setRealPageMsgLen(messageList.length);
				
				// refresh
				boolean refresh = true;
				addPreViewNotepadUI(refresh);
				//
				String pageString = "(" + pageNum + "页) - [" 
						+ (pageNum * perMsgsLen) +  ": " 
						+ (pageNum * perMsgsLen + realPageMsgLen - 1)
						+ "]";
				pageJLabel.setText(pageString);
				nextBtn.setEnabled(true);
				setVisible(true);
				JOptionPane.showMessageDialog(null, "跳转成功");
			}
		});
	
		// Next
		nextBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//
				if(realPageMsgLen < perMsgsLen) {
					JOptionPane.showMessageDialog(null, 
							"已经是最后一页啦");
					nextBtn.setEnabled(false);
					return;
				} else {
					nextBtn.setEnabled(true);
				}
				
				//
				pageNum++;
				
				boolean isGetAttachment = EnumType.DefaultIsGetAttachment;
				EmailMessage[] messageList = null;
				try {
					boolean isNotepad = true;
					messageList = ReceivedPoP3Processor.
							getReceivedPoP3ProcessorInstance().
							receive(isGetAttachment, pageNum, perMsgsLen, isNotepad);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				if(messageList == null || messageList.length < 1) {
					JOptionPane.showMessageDialog(null, "获取记事本失败");
					// set back
					pageNum--;
					if(pageNum != 0) {
						preBtn.setEnabled(true);
					}
					// nextBtn.setEnabled(false);
					
					EmailLogger.warning("next failed in notepad pre ui");
					return;
				}
				
				EmailLogger.info("***********************");
				EmailLogger.info("成功获取记事本");
				EmailLogger.info("第" + pageNum + "页, 记事本个数: " 
						+ messageList.length);
				
				setEmailMessage(messageList);
				setRealPageMsgLen(messageList.length);
				
				// refresh
				boolean refresh = true;
				addPreViewNotepadUI(refresh);
				//
				String pageString = "(" + pageNum + "页) - [" 
						+ (pageNum * perMsgsLen) +  ": " 
						+ (pageNum * perMsgsLen + realPageMsgLen)
						+ "]";
				pageJLabel.setText(pageString);
				//
				preBtn.setEnabled(true);
				setVisible(true);
				JOptionPane.showMessageDialog(null, "跳转成功");
			}
		});
	}
	/**
	 * setInits
	 */
	public void setInits() {
		addComps();
		addCompsListeners();
	}
	/**
	 * RepaintView
	 */
	public void RepaintView() {
		pageNum = 0;
		
		boolean isGetAttachment = EnumType.DefaultIsGetAttachment;
		EmailMessage[] messageList = null;
		try {
			 boolean isNotepad = true;
			messageList = ReceivedPoP3Processor.
					getReceivedPoP3ProcessorInstance().
					receive(isGetAttachment, pageNum, perMsgsLen, isNotepad);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		if(messageList == null || messageList.length < 1) {
			JOptionPane.showMessageDialog(null, "获取记事本失败");

			preBtn.setEnabled(false);
			nextBtn.setEnabled(false);
			return;
		}
		
		// log & set messages
		EmailLogger.info("***********************");
		EmailLogger.info("成功获取记事本");
		EmailLogger.info("第" + pageNum + "页, 邮件个数: " 
				+ messageList.length);
		
		setEmailMessage(messageList);
		setRealPageMsgLen(messageList.length);
		
		// refresh
		boolean refresh = true;
		addPreViewNotepadUI(refresh);
		
		if(totalMsgLen < perMsgsLen) {
			nextBtn.setEnabled(false);
		} else {
			nextBtn.setEnabled(true);
		}
		//
		String pageString = "(" + pageNum + "页) - [" 
				+ (pageNum * perMsgsLen) +  ": " 
				+ (pageNum * perMsgsLen + realPageMsgLen - 1)
				+ "]";
		pageJLabel.setText(pageString);
		//
		setVisible(true);
		JOptionPane.showMessageDialog(null, "初始化记事本页面成功");
	}
	
	// **********************************************************
	
	/**
	 * gets & sets
	 */
	// EmailMessage
	public EmailMessage[] getEmailMessages() {
		return messages;
	}
	
	public void setEmailMessage(EmailMessage[] messages) {
		this.totalMsgLen = messages.length;
		this.messages = messages;
	}
	// pageNum
	public int getPaeNum() {
		return this.pageNum;
	}
	
	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	} 
	// perMsgsLen
	public void setPerMsgsLen(int perMsgsLen) {
		this.perMsgsLen = perMsgsLen;
	}
	// realPageMsgLen
	public int getRealPageMsgLen() {
		return this.realPageMsgLen;
	}
	// realPageMsgLen
	public void setRealPageMsgLen(int realPageMsgLen) {
		this.realPageMsgLen = realPageMsgLen;
	}
	
	public int getPerMsgsLen() {
		return this.perMsgsLen;
	}
}