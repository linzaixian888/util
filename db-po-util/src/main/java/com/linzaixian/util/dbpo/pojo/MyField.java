package com.linzaixian.util.dbpo.pojo;

import com.linzaixian.util.dbmeta.pojo.Column;

public class MyField {
	private Column column;
	private String fieldName;
	private String columnName;
	private String type;
	private String remark;
    public Column getColumn() {
        return column;
    }
    public void setColumn(Column column) {
        this.column = column;
    }
    public String getFieldName() {
        return fieldName;
    }
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
    public String getColumnName() {
        return columnName;
    }
    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
	
	
	
	
	
}
