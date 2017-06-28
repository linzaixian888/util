package com.linzaixian.util.dbmeta;

/**
 * @author linzaixian
 * @since 2017-06-21 13:57:23 
 */
public enum DbTypeEnum {
    MYSQL(""),ORACLE("oracle.jdbc.driver.OracleDriver");
    private String code;

    private DbTypeEnum(String code) {
        this.code = code;
    }
    public static DbTypeEnum getEnum(String code){
        for(DbTypeEnum item:values()){
            if(item.getCode().equals(code)){
                return item;
            }
        }
        return null;
    }
    public String getCode() {
        return code;
    }
    
    
   
}
