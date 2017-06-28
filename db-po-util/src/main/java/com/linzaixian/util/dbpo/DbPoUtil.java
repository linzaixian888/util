package com.linzaixian.util.dbpo;

import java.util.ArrayList;
import java.util.List;

import com.linzaixian.util.dbmeta.DbMetaUtil;
import com.linzaixian.util.dbmeta.pojo.Column;
import com.linzaixian.util.dbmeta.pojo.Table;
import com.linzaixian.util.dbpo.pojo.MyClass;
import com.linzaixian.util.dbpo.pojo.MyField;

public class DbPoUtil {
    private TranslateAdapter adapter=new DefaultTranslateAdapter();
	private List<Table> tables;
	public DbPoUtil(List<Table> tables) {
		this.tables = tables;
	}
	
	public void setAdapter(TranslateAdapter adapter) {
        this.adapter = adapter;
    }

    /**
	 * 转换初始化
	 */
	public List<MyClass> translate(){
	    List<MyClass> classes=new ArrayList<>();
		for(Table table:tables){
		    MyClass myClass=adapter.translateTable(table);
		    classes.add(myClass);
		    if(table.getIdColumn()!=null){
		        myClass.setIdField(adapter.translateColumn(table.getIdColumn(),table ));
		    }
		    for(Column column:table.getColumns()){
		        MyField myField=adapter.translateColumn(column,table );
	            myClass.appendMyField(myField);
	        }
		}
        return classes;
	}
	
	public static void main(String[] args) throws Exception {
		String driver="com.mysql.jdbc.Driver";
		String url="jdbc:mysql://121.43.170.94:3306/ipay?useUnicode=true&characterEncoding=utf8";
		String username="root";
		String password="9EY9fOfeyJYp1iBO";
		String schema=null;
		DbMetaUtil util=new DbMetaUtil(driver, url, username, password, schema);
		new DbPoUtil(util.getAllTable()).translate();
	}
}
