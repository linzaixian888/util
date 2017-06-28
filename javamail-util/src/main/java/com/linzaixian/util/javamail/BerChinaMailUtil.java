package com.linzaixian.util.javamail;

/**
 * @author linzaixian
 * @since 2017-04-01 16:53:22 
 */
public class BerChinaMailUtil extends JavaMailUtil{

    /**
     * @param mailServerHost
     * @param mailServerPort
     * @param fromAddress
     * @param username
     * @param password
     * @param isSSL
     */
    public BerChinaMailUtil(String fromAddress,  String password) {
        super("mail.berchina.com", 25, fromAddress, fromAddress, password, false);
    }
    public static void main(String[] args) throws Exception {
        BerChinaMailUtil util=new BerChinaMailUtil("zhuchaoyang@berchina.com",  null);
        util.send("致贤哥", "贤哥我是你的小弟的小弟", "linzaixian@berchina.com");
    }

}
