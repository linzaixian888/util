package com.linzaixian.util.dbpo;

import com.linzaixian.util.dbmeta.pojo.Column;
import com.linzaixian.util.dbmeta.pojo.Table;
import com.linzaixian.util.dbpo.pojo.MyClass;
import com.linzaixian.util.dbpo.pojo.MyField;

/**
 * @author linzaixian
 * @since 2017-06-02 14:16:02 
 */
public interface TranslateAdapter {
    MyClass translateTable(Table table);
    MyField translateColumn(Column column,Table table);
}
