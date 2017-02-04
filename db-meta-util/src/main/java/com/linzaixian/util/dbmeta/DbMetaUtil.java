package com.linzaixian.util.dbmeta;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linzaixian.util.dbmeta.pojo.Column;
import com.linzaixian.util.dbmeta.pojo.Index;
import com.linzaixian.util.dbmeta.pojo.Key;
import com.linzaixian.util.dbmeta.pojo.Table;

/**
 * 数据库元数据获取工具库
 * @author linzaixian
 *
 */
public class DbMetaUtil {
	private static final Logger logger=LoggerFactory.getLogger(DbMetaUtil.class);
	private  String driver;
	private  String url;
	private  String username;
	private  String password;
	private  String schema;
	
	public DbMetaUtil(String driver, String url, String username, String password, String schema) {
		this.driver = driver;
		this.url = url;
		this.username = username;
		this.password = password;
		this.schema = schema;
		logger.debug("初始化,传递参数为driver:[{}],url:[{}],username:[{}],password:[{}],schema:[{}]",driver,url,username,password,schema);;
	}
	/**
	 * 获取所有表
	 * @return
	 * @throws Exception
	 */
	public  List<Table> getAllTable()throws Exception{
		List<Table> list = null;
		Connection conn=null;
		try {
			conn=getConn();
			list = getAllTable(conn);
		} catch (Exception e) {
			throw e;
		}finally{
			closeConn(conn);
		}
		return list;
	}
	
	/**
	 * 获取只包含ID列名的ID列
	 * @param db
	 * @param schema
	 * @param table
	 * @return
	 * @throws Exception
	 */
	private Column getIdColumn(DatabaseMetaData db,String table) throws Exception{
		//id列的处理
		ResultSet idColumns=db.getPrimaryKeys(null, schema, table);
		Column column=null;
		while(idColumns.next()){
			column=new Column();
			column.setColumnName(idColumns.getString("COLUMN_NAME"));
			logger.debug("获取表{}的主键名为：{}",table,column.getColumnName());
		}
		idColumns.close();
		if(logger.isDebugEnabled()){
			if(column==null){
				logger.debug("表{}没有设置主键",table);
			}else{
				logger.debug("表{}设置的主键名:{}",table,column.getColumnName());
			}
		}
		return column;
	}
	/**
	 * 通过表名获取指定表
	 * @param db
	 * @param tableName
	 * @return
	 * @throws Exception
	 */
	private Table getTable(DatabaseMetaData db,String tableName) throws Exception{
		ResultSet tableSet=db.getTables(null, schema, tableName, new String[]{"TABLE"});
		if(tableSet.next()){
			return getTable(db, tableSet);
		}
		return null;
		
	}
	/**
	 * 通过表名获取指定表
	 * @param tableNames
	 * @return
	 * @throws Exception
	 */
	public List<Table> getTables(String...tableNames) throws Exception{
		List<String> tableList=Arrays.asList(tableNames);
		return getTables(tableList);
	}
	
	/**
	 * 通过表名获取指定表
	 * @param tableList
	 * @return
	 * @throws Exception
	 */
	public List<Table> getTables(List<String> tableList) throws Exception{
		List<Table> list = new ArrayList<Table>();
		Connection conn=null;
		 try {
			conn=getConn();
			DatabaseMetaData db=conn.getMetaData();
			for(String tableName:tableList){
				Table table=getTable(db, tableName);
				if(table!=null){
					list.add(table);
				}
			}
		} catch (Exception e) {
			throw e;
		}finally{
			closeConn(conn);
		}
		if(logger.isDebugEnabled()){
			logger.debug("获取到{}张表",list.size());
			for(Table table:list){
				logger.debug("{}",table);
			}
			
		}
		return list;
	}
	
	/**
	 * 设置ID列和其他列
	 * @param table
	 * @param columns
	 * @throws SQLException
	 */
	private void setAllColumn(Table table ,ResultSet columns) throws SQLException{
		logger.debug("开始获取所有列");
		while(columns.next()){
			Column column=new Column();
			column.setTableCat(columns.getString("TABLE_CAT"));
			column.setTableSchem(columns.getString("TABLE_SCHEM"));
			column.setTableName(columns.getString("TABLE_NAME"));
			column.setColumnName(columns.getString("COLUMN_NAME"));
			column.setDataType(columns.getInt("DATA_TYPE"));
			column.setTypeName(columns.getString("TYPE_NAME"));
			column.setColumnSize(columns.getInt("COLUMN_SIZE"));
			column.setDecimalDigits(columns.getInt("DECIMAL_DIGITS"));
			column.setNumPrecRadix(columns.getInt("NUM_PREC_RADIX"));
			column.setNullable(columns.getInt("NULLABLE"));
			column.setRemarks(columns.getString("REMARKS"));
			column.setColumnDef(columns.getString("COLUMN_DEF"));
			column.setCharOctetLength(columns.getInt("CHAR_OCTET_LENGTH"));
			column.setOrdinal_posuition(columns.getInt("ORDINAL_POSITION"));
			column.setIsNullable(columns.getString("IS_NULLABLE"));
			//获取SCOPE_CATLOG在mysql里面报错
//			column.setScopeCatlog(columns.getString("SCOPE_CATLOG "));
			column.setScopeSchema(columns.getString("SCOPE_SCHEMA"));
			column.setScopeTable(columns.getString("SCOPE_TABLE"));
			column.setSourceDataType(columns.getShort("SOURCE_DATA_TYPE"));
			column.setIsAutoincrement(columns.getString("IS_AUTOINCREMENT"));
			logger.debug("当前获取的列数据为{}",column);
			table.appendColumn(column);
		}
		columns.close();
	}
	/**
	 * 设置所有索引对象
	 * @param table
	 * @param indexes
	 * @throws SQLException
	 */
	private void setAllIndex(Table table ,ResultSet indexes) throws SQLException{
		logger.debug("开始获取所有索引");
		while(indexes.next()){
			table.appendIndex(getIndex(indexes));
		}
		indexes.close();
	}
	/**获取索引对象
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private Index getIndex(ResultSet rs) throws SQLException{
		Index index=new Index();
		index.setTableCat(rs.getString("TABLE_CAT"));
		index.setTableName(rs.getString("TABLE_SCHEM"));
		index.setTableName(rs.getString("TABLE_NAME"));
		index.setNonUnique(rs.getBoolean("NON_UNIQUE"));
		index.setIndexQualifier(rs.getString("INDEX_QUALIFIER"));
		index.setIndexName(rs.getString("INDEX_NAME"));
		index.setType(rs.getShort("TYPE"));
		index.setOrdinalPosition(rs.getShort("ORDINAL_POSITION"));
		index.setColumnName(rs.getString("COLUMN_NAME"));
		index.setAscOrDesc(rs.getString("ASC_OR_DESC"));
		index.setCardinality(rs.getInt("CARDINALITY"));
		index.setPages(rs.getInt("PAGES"));
		index.setFilterCondition(rs.getString("FILTER_CONDITION"));
		logger.debug("当前获取的索引数据为{}",index);
		return index;
	}
	
	
	/**
	 * 转换为主外键信息对象
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private Key getKey(ResultSet rs) throws SQLException{
	
		Key key=new Key();
		key.setPkTableCat(rs.getString("PKTABLE_CAT"));
		key.setPkTableSchem(rs.getString("PKTABLE_SCHEM"));
		key.setPkTableName(rs.getString("PKTABLE_NAME"));
		key.setPkColumnName(rs.getString("PKCOLUMN_NAME"));
		key.setFkTableCat(rs.getString("FKTABLE_CAT"));
		key.setFkTableSchem(rs.getString("FKTABLE_SCHEM"));
		key.setFkTableName(rs.getString("FKTABLE_NAME"));
		key.setFkColumnName(rs.getString("FKCOLUMN_NAME"));
		key.setKeySeq(rs.getShort("KEY_SEQ"));
		key.setUpdateRule(rs.getShort("UPDATE_RULE"));
		key.setDeleteRule(rs.getShort("DELETE_RULE"));
		key.setFkName(rs.getString("FK_NAME"));
		key.setPkName(rs.getString("PK_NAME"));
		key.setDeferrability(rs.getShort("DEFERRABILITY"));
		logger.debug("当前获取的主外键相关信息为{}",key);
		return key;
	}
	
	/**
	 * 获取指定表
	 * @param db
	 * @param tableSet
	 * @return
	 * @throws Exception
	 */
	private Table getTable(DatabaseMetaData db,ResultSet tableSet) throws Exception{
		String tableName=tableSet.getString("TABLE_NAME");
		Table table=new Table(tableName);
		logger.debug("获取的表名称:{}",table.getTableName());
		table.setRemark(tableSet.getString("REMARKS"));
		logger.debug("获取的表注释:{}",table.getRemark());
		//对没有表注释的处理
		if(table.getRemark()==null){
			logger.debug("没有设置表注释，默认设为空字符串");
			table.setRemark("");
		}
		//设置ID列的基本列名信息，方便后续填充
		table.setIdColumn(getIdColumn(db, tableName));
		//设置所有列的详细信息，包含ID列
		ResultSet columns=db.getColumns(null, schema, tableName, null);
		setAllColumn(table, columns);
		
		//设置主键表相关信息
		ResultSet parents=db.getImportedKeys(null, schema, tableName);
		while (parents.next()) {
			logger.debug("获取主表(即父表)相关信息");
			table.appendImportedKey(getKey(parents));
		}
		parents.close();
		
		//设置外键表相关信息
		ResultSet childs=db.getExportedKeys(null, schema, tableName);
		while(childs.next()){
			logger.debug("获取从表(即子表)相关信息");
			table.appendExportedKey(getKey(childs));
		}
		childs.close();
		
		//设置索引信息
		ResultSet indexes=db.getIndexInfo(null, schema, tableName, false, true);
		setAllIndex(table, indexes);
		
		return table;
		
	}
	/**
	 * 获得数据库所有表信息
	 * @return
	 * @throws Exception
	 */
	private  List<Table> getAllTable(Connection conn) throws Exception {
		logger.debug("通过连接{}获取所有数据表信息",conn);
		List<Table> tableList=new ArrayList<Table>();
		DatabaseMetaData db=conn.getMetaData();
		ResultSet tables=db.getTables(null, schema, null, new String[]{"TABLE"});
		while(tables.next()){
			tableList.add(getTable(db, tables));
		}
		tables.close();
		if(logger.isDebugEnabled()){
			logger.debug("获取到{}张表",tableList.size());
			for(Table table:tableList){
				logger.debug("{}",table);
			}
			
		}
		return tableList;
	}
	
	/**
	 * 获取连接
	 * @return
	 * @throws Exception
	 */
	private Connection getConn() throws Exception{
		Class.forName(driver);
		Connection conn=DriverManager.getConnection(url, username, password);
		logger.debug("获取连接:{}",conn);
		return conn;
	}
	
	/**
	 * 关闭连接
	 * @param conn
	 */
	private void closeConn(Connection conn){
		try {
			if(conn!=null){
				logger.debug("关闭连接:{}",conn);
				conn.close();
			}
		} catch (SQLException e) {
		}
	}
	public static void main(String[] args) throws Exception {
		String driver="oracle.jdbc.driver.OracleDriver";
		String url="jdbc:oracle:thin:@192.168.1.250:1521:orcl";
		String username="o2odev";
		String password="o2odev";
		String schema="O2ODEV";
		DbMetaUtil util=new DbMetaUtil(driver, url, username, password, schema);
		System.out.println(util.getAllTable());
	}
}
