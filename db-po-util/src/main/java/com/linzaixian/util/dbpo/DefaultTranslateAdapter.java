package com.linzaixian.util.dbpo;

import java.sql.Types;

import com.linzaixian.util.dbmeta.pojo.Column;
import com.linzaixian.util.dbmeta.pojo.Table;
import com.linzaixian.util.dbpo.pojo.MyClass;
import com.linzaixian.util.dbpo.pojo.MyField;
import com.linzaixian.util.string.StringUtil;

/**
 * 默认类以及属性的转化适配器
 * @author linzaixian
 * @since 2017-06-02 14:36:12 
 */
public  class DefaultTranslateAdapter extends AbstractTranslateAdapter{

    @Override
    public MyClass translateTable(Table table) {
        MyClass myClass=new MyClass();
        myClass.setTable(table);
        myClass.setRemark(table.getRemark());
        myClass.setTableName(table.getTableName());
        myClass.setClassName(getClassName(table.getTableName(), table));
        return myClass;
    }

    @Override
    public MyField translateColumn(Column column,Table table ) {
        MyField myField=new MyField();
        myField.setColumn(column);
        myField.setColumnName(column.getColumnName());
        myField.setRemark(column.getRemarks());
        myField.setFieldName(getFieldName(column.getColumnName(), column, table));
        myField.setType(getTypeString(column.getDataType(), column, table));
        return myField;
    }
    
    
    public String getClassName(String name,Table table){
        return firstUp(toWord(name.toLowerCase(), "_", true));
    }
    public String getFieldName(String name,Column column,Table table){
        return firstLow(toWord(name.toLowerCase(), "_", true));
    }
    
    
    
    
    
    
    
   

}
