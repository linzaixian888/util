package com.linzaixian.util.dbmeta;

/**
 * 默认的转化处理适配器
 * @author linzaixian
 * @since 2017-06-28 14:28:43 
 */
public class DefaultTranslateAdapter implements TranslateAdapter{

    @Override
    public String translateColumnName(String name) {
        return name.toLowerCase();
    }

    @Override
    public String translateTableName(String name) {
        return name.toLowerCase();
    }

}
