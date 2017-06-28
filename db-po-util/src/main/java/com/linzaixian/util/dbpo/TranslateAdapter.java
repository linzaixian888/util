package com.linzaixian.util.dbpo;

import com.linzaixian.util.dbmeta.pojo.Column;
import com.linzaixian.util.dbmeta.pojo.Table;
import com.linzaixian.util.dbpo.pojo.MyClass;
import com.linzaixian.util.dbpo.pojo.MyField;

/**
 * 类以及属性的转化适配器
 * @author linzaixian
 * @since 2017-06-02 14:16:02 
 */
public interface TranslateAdapter {
    /**
     * 对表信息的转化处理
     * @param table
     * @return
     */
    MyClass translateTable(Table table);
    /**
     * 对列信息的转化处理
     * @param column
     * @param table
     * @return
     */
    MyField translateColumn(Column column,Table table);
}
