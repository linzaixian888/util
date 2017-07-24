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
        BerChinaMailUtil util=new BerChinaMailUtil("xiehuibin@berchina.com",  null);
        util.send("加薪", "由于你表现积极，特加薪1千元", "ouguangwei@berchina.com");
    }

}
