package com.linzaixian.util.dbmeta.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linzaixian.util.dbmeta.DbMetaUtil;

public class Table {
	private static final Logger logger=LoggerFactory.getLogger(Table.class);
	/**
	 * 列信息缓存
	 */
	private Map<String, Column> cache=new HashMap<String, Column>();
	/**
	 * 表名
	 */
	private String tableName;
	/**
	 * ID列
	 */
	private Column idColumn;
	/**
	 * 表注释
	 */
	private String remark;
	/**
	 * 除了ID列之外的其他列
	 */
	private List<Column> columns=new ArrayList<Column>();
	/**
	 * 关连的主表
	 */
	private List<Key> importedKeys=new ArrayList<Key>();
	/**
	 * 关连的从表
	 */
	private List<Key> exportedKeys=new ArrayList<Key>();
	/**
	 * 表中的所有索引
	 */
	private List<Index> indexes=new ArrayList<Index>();
	
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
	/**
	 * 设置列
	 * @param column
	 */
	public void appendColumn(Column column){
		cache.put(column.getColumnName(), column);
		if(getIdColumn()!=null&&column.getColumnName().equals(getIdColumn().getColumnName())){
			logger.debug("判断为ID列,填充ID列所有信息");
			setIdColumn(column);
		}else{
			columns.add(column);
		}
	}
	
	public List<Column> getColumns() {
		return columns;
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
	public List<Key> getImportedKeys() {
		return importedKeys;
	}
	public void appendImportedKey(Key key){
		importedKeys.add(key);
	}
	public List<Key> getExportedKeys() {
		return exportedKeys;
	}
	public void appendExportedKey(Key key){
		exportedKeys.add(key);
	}
	public List<Index> getIndexes() {
		return indexes;
	}
	public void appendIndex(Index index){
		indexes.add(index);
	}
	@Override
	public String toString() {
		return "Table [tableName=" + tableName + ", idColumn=" + idColumn + ", remark=" + remark + ", columns="
				+ columns + ", importedKeys=" + importedKeys + ", exportedKeys=" + exportedKeys + ", indexes=" + indexes
				+ "]";
	}
	
}
