package com.linzaixian.util.dbmeta;

/**
 * 获取数据库信息的转化处理适配器
 * @author linzaixian
 * @since 2017-06-28 14:24:21 
 */
public interface TranslateAdapter {
    /**
     * 对列名的转化处理
     * @param name
     * @return
     */
    String translateColumnName(String name);
    /**
     * 对表名的转化处理
     * @param name
     * @return
     */
    String translateTableName(String name);
}
