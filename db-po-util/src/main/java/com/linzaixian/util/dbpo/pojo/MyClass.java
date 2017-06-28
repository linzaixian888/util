package com.linzaixian.util.dbpo.pojo;

import java.util.ArrayList;
import java.util.List;

import com.linzaixian.util.dbmeta.pojo.Table;

public class MyClass {
	private Table table;
	private String className;
	private String tableName;
	private MyField idField;
	private List<MyField> myFields=new ArrayList<MyField>();
	private String remark;
	public String getClassName() {
		return className;
	}
	public String getTableName() {
		return tableName;
	}
	public MyField getIdField() {
		return idField;
	}
	public String getRemark() {
		return remark;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public void setIdField(MyField idField) {
		this.idField = idField;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Table getTable() {
		return table;
	}
	public void setTable(Table table) {
		this.table = table;
	}
	public List<MyField> getMyFields() {
        return myFields;
    }
    public void appendMyField(MyField myField){
		this.myFields.add(myField);
	}
	@Override
	public String toString() {
		return "MyClass [table=" + table + ", className=" + className + ", tableName=" + tableName + ", idField="
				+ idField + ", myFields=" + myFields + ", remark=" + remark + "]";
	}
	
	
	
}
