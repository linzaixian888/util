package com.linzaixian.util.dbmeta;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.linzaixian.util.dbmeta.pojo.ChildTable;
import com.linzaixian.util.dbmeta.pojo.Column;
import com.linzaixian.util.dbmeta.pojo.ParentTable;
import com.linzaixian.util.dbmeta.pojo.Table;

/**
 * 数据库元数据获取工具库
 * @author linzaixian
 *
 */
public class DbMetaUtil {
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
	 * 获取id列
	 * @param db
	 * @param schema
	 * @param table
	 * @return
	 * @throws Exception
	 */
	private Column getIdColumn(DatabaseMetaData db,String schema,String table) throws Exception{
		//id列的处理
		ResultSet idColumns=db.getPrimaryKeys(null, schema, table);
		Column column=new Column();
		while(idColumns.next()){
			column.setColumnName(idColumns.getString("COLUMN_NAME"));
		}
		idColumns.close();
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
			
		return list;
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
		table.setRemark(tableSet.getString("REMARKS"));
		//对没有表注释的处理
		if(table.getRemark()==null){
			table.setRemark("");
		}
		//id列的处理
		table.setIdColumn(getIdColumn(db, schema, tableName));
		
		ResultSet columns=db.getColumns(null, schema, tableName, null);
		List<Column> columnList=table.getColumns();
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
			if(table.getIdColumn()!=null&&column.getColumnName().equals(table.getIdColumn().getColumnName())){
				table.setIdColumn(column);
				continue;
			}
			columnList.add(column);
		}
		columns.close();
		ResultSet parents=db.getImportedKeys(null, schema, tableName);
		List<ParentTable> parentList=table.getParents();
		while (parents.next()) {
			ParentTable parentTable=new ParentTable();
			parentTable.setTableName(parents.getString("PKTABLE_NAME"));
			parentTable.setColumnName(parents.getString("FKCOLUMN_NAME"));
			parentTable.setPkTable(parents.getString("PKTABLE_NAME"));
			parentTable.setFkTable(parents.getString("FKTABLE_NAME"));
			parentTable.setPkColumn(parents.getString("PKCOLUMN_NAME"));
			parentTable.setFkColumn(parents.getString("FKCOLUMN_NAME"));
			parentList.add(parentTable);
		}
		ResultSet childs=db.getExportedKeys(null, schema, tableName);
		List<ChildTable> childTableList=table.getChilds();
		while(childs.next()){
			ChildTable childTable=new ChildTable();
			childTable.setTableName(childs.getString("FKTABLE_NAME"));
			childTable.setColumnName(childs.getString("FKCOLUMN_NAME"));
			childTable.setPkTable(childs.getString("PKTABLE_NAME"));
			childTable.setFkTable(childs.getString("FKTABLE_NAME"));
			childTable.setPkColumn(childs.getString("PKCOLUMN_NAME"));
			childTable.setFkColumn(childs.getString("FKCOLUMN_NAME"));
			childTableList.add(childTable);
		}
		childs.close();
		return table;
	}
	/**
	 * 获得数据库所有表信息
	 * @return
	 * @throws Exception
	 */
	private  List<Table> getAllTable(Connection conn) throws Exception {
		List<Table> tableList=new ArrayList<Table>();
		DatabaseMetaData db=conn.getMetaData();
		ResultSet tables=db.getTables(null, schema, null, new String[]{"TABLE"});
		while(tables.next()){
			tableList.add(getTable(db, tables));
		}
		tables.close();
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
		return conn;
	}
	
	/**
	 * 关闭连接
	 * @param conn
	 */
	private void closeConn(Connection conn){
		try {
			if(conn!=null){
				conn.close();
			}
		} catch (SQLException e) {
		}
	}
}
