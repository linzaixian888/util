package com.linzaixian.util.dbmeta.pojo;

import java.util.ArrayList;
import java.util.List;

public class Table {
	private String tableName;
	private Column idColumn;
	private String remark;
	private List<Column> columns=new ArrayList<Column>();
	private List<ParentTable> parents=new ArrayList<ParentTable>();
	private List<ChildTable> childs=new ArrayList<ChildTable>();
	
	public Table(String tableName) {
		this.tableName = tableName;
	}
	public List<Column> getAllColumns(){
		List<Column> list=new ArrayList<Column>();
		if(idColumn!=null){
			list.add(idColumn);
		}
		list.addAll(columns);
		return list;
	}
	public List<Column> getColumns() {
		return columns;
	}
	public void setColumns(List<Column> columns) {
		this.columns = columns;
	}
	public List<ParentTable> getParents() {
		return parents;
	}
	public void setParents(List<ParentTable> parents) {
		this.parents = parents;
	}
	public List<ChildTable> getChilds() {
		return childs;
	}
	public void setChilds(List<ChildTable> childs) {
		this.childs = childs;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public Column getIdColumn() {
		return idColumn;
	}
	public void setIdColumn(Column idColumn) {
		this.idColumn = idColumn;
	}
	
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	@Override
	public String toString() {
		return "Table [tableName=" + tableName + ", idColumn=" + idColumn
				+ ", columns=" + columns + ", parents=" + parents + ", childs="
				+ childs + "]";
	}
}
