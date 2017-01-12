package com.linzaixian.util.javamail;
/**
 * QQ邮箱的封装
 * @author linzaixian
 *
 */
public class QQMailUtil extends JavaMailUtil{
	
	/**
	 * 
	 * @param fromAddress  发件人邮箱
	 * @param password  发件人密码
	 */
	public QQMailUtil(String fromAddress,String password) {
		super("smtp.qq.com", 465, fromAddress, fromAddress, password, true);
	}
	public static void main(String[] args) throws Exception {
		QQMailUtil util=new QQMailUtil("linzaixian888@qq.com", "wndeixiciootcafb");
		util.send("abc", "abc", "linzaixian888@qq.com");
	}

}
