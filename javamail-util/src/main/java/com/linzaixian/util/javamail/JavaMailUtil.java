package com.linzaixian.util.javamail;

import java.util.Date;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class JavaMailUtil {
	/**
	 * 是否开启javamail的日志输出调试
	 */
	private static final boolean DEBUG=false;
	
	private static final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
	private static final String PROTOCOL="smtp";
	private static final String PROTOCOL_KEY="mail.transport.protocol";
	private static final String HOST_KEY="mail."+PROTOCOL+".host";
	private static final String PORT_KEY="mail."+PROTOCOL+".port";
	private static final String AUTH_KEY="mail."+PROTOCOL+".auth";
	
	/**
	 * 发送邮件的服务器的IP
	 */
	private String mailServerHost;
	/**
	 * 发送邮件的服务器的端口
	 */
	private int mailServerPort = -1;
	/**
	 * 邮件发送者的地址
	 */
	private String fromAddress;
	/**
	 * 登陆邮件发送服务器的用户名
	 */
	private String username;
	/**
	 * 登陆邮件发送服务器的密码
	 */
	private String password;
	/**
	 * 是否使用ssl加密传输
	 */
	private boolean isSSL=false;
	/**
	 * 发送邮件的session
	 */
	private Session sendMailSession;

	
	
	/**
	 * 
	 * @param mailServerHost 发送服务器
	 * @param mailServerPort 发送服务器端口(一般为25,ssl的一般为465)
	 * @param fromAddress 发件人邮箱
	 * @param username 发件人帐号
	 * @param password 发件人密码
	 * @param isSSL 是否采用ssl加密传输
	 */
	public JavaMailUtil(String mailServerHost, int mailServerPort, String fromAddress, String username, String password,
			boolean isSSL) {
		this.mailServerHost = mailServerHost;
		this.mailServerPort = mailServerPort;
		this.fromAddress = fromAddress;
		this.username = username;
		this.password = password;
		this.isSSL = isSSL;
		init();
	}

	private Properties getProperties(){
		Properties props = new Properties();
		props.setProperty(HOST_KEY, this.mailServerHost);
		props.setProperty(PORT_KEY, Integer.toString(this.mailServerPort));
		props.setProperty(PROTOCOL_KEY, PROTOCOL);
		if(isSSL){
			props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
		}
		if(this.password==null){
			props.setProperty(AUTH_KEY, Boolean.toString(false));
		}else{
			props.setProperty(AUTH_KEY, Boolean.toString(true));
		}
		return props;
	}
	private Session getSession(){
		Session session=Session.getInstance(getProperties());
		return session;
	}
	
	/**
	 * 初始化
	 */
	private void init() {
		sendMailSession=getSession();
		sendMailSession.setDebug(DEBUG);
	}

	public void send(String subject, String content, String... toAddrs)
			throws Exception {
		Message mailMessage = new MimeMessage(sendMailSession);
		mailMessage.setFrom(new InternetAddress(fromAddress));
		int len = toAddrs.length;
		Address[] addrs = new Address[len];
		for (int i = 0; i < len; i++) {
			addrs[i] = new InternetAddress(toAddrs[i]);
		}
		mailMessage.setRecipients(Message.RecipientType.TO, addrs);
		// 设置邮件消息的主题
		mailMessage.setSubject(subject);
		// 设置邮件消息发送的时间
		mailMessage.setSentDate(new Date());
		mailMessage.setText(content);
		Transport transport=sendMailSession.getTransport();
		try {
			transport.connect(mailServerHost,mailServerPort,username, password);
			transport.sendMessage(mailMessage, addrs);
		} catch (Exception e) {
			throw e;
		}finally{
			transport.close();
		}
	}
	public static void main(String[] args) throws Exception {
		JavaMailUtil util=new JavaMailUtil("smtp.qq.com", 465, "linzaixian888@qq.com", "linzaixian888@qq.com", "wndeixiciootcafb", true);
		util.send("abc", "abc", "linzaixian888@qq.com");
	}

}
