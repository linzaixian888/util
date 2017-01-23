package com.linzaixian.util.dbmeta.pojo;

public class ParentTable {
	private String pkTable;
	private String fkTable;
	private String pkColumn;
	private String fkColumn;
	private String tableName;
	private String columnName;

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	
	public String getPkTable() {
		return pkTable;
	}

	public void setPkTable(String pkTable) {
		this.pkTable = pkTable;
	}

	public String getFkTable() {
		return fkTable;
	}

	public void setFkTable(String fkTable) {
		this.fkTable = fkTable;
	}

	public String getPkColumn() {
		return pkColumn;
	}

	public void setPkColumn(String pkColumn) {
		this.pkColumn = pkColumn;
	}

	public String getFkColumn() {
		return fkColumn;
	}

	public void setFkColumn(String fkColumn) {
		this.fkColumn = fkColumn;
	}

	@Override
	public String toString() {
		return "ParentTable [pkTable=" + pkTable + ", fkTable=" + fkTable
				+ ", pkColumn=" + pkColumn + ", fkColumn=" + fkColumn
				+ ", tableName=" + tableName + ", columnName=" + columnName
				+ "]";
	}

	
}
