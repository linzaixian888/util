package com.linzaixian.util.dbpo;

import java.sql.Types;

import com.linzaixian.util.dbmeta.pojo.Column;
import com.linzaixian.util.dbmeta.pojo.Table;
import com.linzaixian.util.dbpo.pojo.MyClass;
import com.linzaixian.util.dbpo.pojo.MyField;
import com.linzaixian.util.string.StringUtil;

/**
 * @author linzaixian
 * @since 2017-06-20 20:19:18 
 */
public abstract class AbstractTranslateAdapter implements TranslateAdapter{

   
    
    
    /**
     * 数据库类型转换为java类型
     * @param type
     * @param column
     * @param table
     * @return
     */
    public String getTypeString(int type,Column column,Table table){
        String temp="String";
        switch (type) {
        case Types.INTEGER:
            temp="Integer";break;
        case Types.VARCHAR:
            temp="String";break;
        case Types.CHAR:
            temp="String";break;
        case Types.LONGVARCHAR:
            temp="String";break;
        case Types.TIMESTAMP:
            temp="Date";break;
        case Types.BIT:
            temp="Boolean";break;
        case Types.SMALLINT:
            temp="Integer";break;
        case Types.BIGINT:
            temp="Long";break;
        case Types.DECIMAL:
            temp="Float";break;
        case Types.DATE:
            temp="Date";break;
        case Types.LONGVARBINARY:
            temp="byte[]";break;
        default:
//            logger.error("{}表的{}列的数据类型 [{}]未定义",table.getTableName(),column.getColumnName(),type);
            throw new RuntimeException(table.getTableName()+"表的"+column.getColumnName()+"列的数据类型["+type+"]未定义");
//          temp="String";
        }
        return temp;
    }
    
    
    /**
     * 对字符串根据分隔符进行单词化
     * @param str
     * @param separator
     * @param isFirstUp
     * @return
     */
    public String toWord(String str,String separator,Boolean isFirstUp){
        return StringUtil.toWord(str, separator, isFirstUp);
    }
    
    /**
     * 首字母大写
     * @param str
     * @return
     */
    public static String firstUp(String str){
        return StringUtil.firstUp(str);
    }
    /**
     * 首字母小写
     * @param str
     * @return
     */
    public static String firstLow(String str){
        return StringUtil.firstLow(str);
    }
    
    

}
