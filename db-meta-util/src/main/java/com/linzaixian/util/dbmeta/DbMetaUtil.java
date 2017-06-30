package com.linzaixian.util.dbmeta;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

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
	private TranslateAdapter adapter=new DefaultTranslateAdapter();
	private  String driver;
	private  String url;
	private  String username;
	private  String password;
	private  String schema;
	private DbTypeEnum dbType;
	
	public DbMetaUtil(String driver, String url, String username, String password, String schema) {
		this.driver = driver;
		this.url = url;
		this.username = username;
		this.password = password;
		this.dbType=DbTypeEnum.getEnum(driver);
		if(DbTypeEnum.ORACLE.equals(dbType)&&schema==null){
		    schema=username.toUpperCase();
		}
		
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
	 * @param tableName
	 * @return
	 * @throws Exception
	 */
	private Column getIdColumn(DatabaseMetaData db,String tableName) throws Exception{
		//id列的处理
		ResultSet idColumns=db.getPrimaryKeys(null, schema, tableName);
		Column column=null;
		while(idColumns.next()){
			column=new Column();
			column.setColumnName(processColumnName(idColumns.getString("COLUMN_NAME")));
			logger.debug("获取表{}的主键名为：{}",tableName,column.getColumnName());
		}
		idColumns.close();
		if(logger.isDebugEnabled()){
			if(column==null){
				logger.debug("表{}没有设置主键",tableName);
			}else{
				logger.debug("表{}设置的主键名:{}",tableName,column.getColumnName());
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
	 * 获取转换后的列对象
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private Column getColumn(ResultSet rs,Table table) throws SQLException{
	    Column column=new Column();
        column.setTableCat(rs.getString("TABLE_CAT"));
        column.setTableSchem(rs.getString("TABLE_SCHEM"));
       // column.setTableName(processTableName(rs.getString("TABLE_NAME")));
        column.setTableName(table.getTableName());
        column.setColumnName(processColumnName(rs.getString("COLUMN_NAME")));
        column.setDataType(rs.getInt("DATA_TYPE"));
        column.setTypeName(rs.getString("TYPE_NAME"));
        column.setColumnSize(rs.getInt("COLUMN_SIZE"));
        column.setDecimalDigits(rs.getInt("DECIMAL_DIGITS"));
        column.setNumPrecRadix(rs.getInt("NUM_PREC_RADIX"));
        column.setNullable(rs.getInt("NULLABLE"));
        column.setRemarks(rs.getString("REMARKS"));
        column.setColumnDef(rs.getString("COLUMN_DEF"));
        column.setCharOctetLength(rs.getInt("CHAR_OCTET_LENGTH"));
        column.setOrdinal_posuition(rs.getInt("ORDINAL_POSITION"));
        column.setIsNullable(rs.getString("IS_NULLABLE"));
        //获取SCOPE_CATLOG在mysql里面报错
//      column.setScopeCatlog(rs.getString("SCOPE_CATLOG "));
        column.setScopeSchema(rs.getString("SCOPE_SCHEMA"));
        column.setScopeTable(rs.getString("SCOPE_TABLE"));
        column.setSourceDataType(rs.getShort("SOURCE_DATA_TYPE"));
        column.setIsAutoincrement(rs.getString("IS_AUTOINCREMENT"));
        logger.debug("当前获取的列数据为{}",column);
        return column;
	}
	
	/**
	 * 设置ID列和其他列
	 * @param db
	 * @param table
	 * @param tableName  
	 * @throws SQLException
	 */
    private void setAllColumn(DatabaseMetaData db,Table table ,String tableName) throws SQLException{
        logger.debug("开始获取所有列");
        ResultSet columns=db.getColumns(null, schema, tableName, null);
        while(columns.next()){
            table.appendColumn(getColumn(columns,table));
        }
        columns.close();
    }
	/**
	 * 设置所有索引对象
	 * @param db
	 * @param table
	 * @param tableName
	 * @throws SQLException
	 */
	private void setAllIndex(DatabaseMetaData db,Table table ,String tableName) throws SQLException{
		logger.debug("开始获取所有索引");
		ResultSet indexes=db.getIndexInfo(null, schema, tableName, false, true);
		while(indexes.next()){
			table.appendIndex(getIndex(indexes));
		}
		indexes.close();
	}
	
	private void setAllKey(DatabaseMetaData db,Table table,String tableName) throws SQLException{
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
		Table table=new Table(processTableName(tableName));
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
		setAllColumn(db,table, tableName);
		
		//设置主外键表相关信息
		setAllKey(db, table, tableName);
		
		//设置索引信息
		setAllIndex(db,table, tableName);
		
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
		Properties info=new Properties();
		if (username != null) {
            info.put("user", username);
        }
        if (password != null) {
            info.put("password", password);
        }
        info.put("remarksReporting", "true");
		Connection conn=DriverManager.getConnection(url, info);
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
	
	
	/**
     * 处理字段名
     * @param sourceColumnName
     * @return
     */
    private String processColumnName(String sourceColumnName){
        logger.debug("原始列名称为:{}",sourceColumnName);
        String targetColumnName=adapter.translateColumnName(sourceColumnName);
        logger.debug("列名称处理后为:{}",targetColumnName);
          return sourceColumnName.toLowerCase();
    }
    
    
    /**
     * 处理表名
     * @param sourceTableName
     * @return
     */
    private String processTableName(String sourceTableName){
        logger.debug("原始表名称为:{}",sourceTableName);
        String targetTableName=adapter.translateTableName(sourceTableName);
        logger.debug("表名称处理后为:{}",targetTableName);
        return targetTableName;
    }
    
    
    
	public void setAdapter(TranslateAdapter adapter) {
        this.adapter = adapter;
    }



    public DbTypeEnum getDbType() {
        return dbType;
    }
    public static void main(String[] args) throws Exception {
//	    String driver="com.mysql.jdbc.Driver";
//        String url="jdbc:mysql://121.43.170.94:3306/ipay?useUnicode=true&characterEncoding=utf8";
//        String username="root";
//        String password="9EY9fOfeyJYp1iBO";
//        String schema=null;
	       String driver="oracle.jdbc.driver.OracleDriver";
	        String url="jdbc:oracle:thin:@121.196.216.209:1521:orcl";
	        String username="IPAY_PAY";
	        String password="pay0608";
	        String schema="IPAY_PAY";
		DbMetaUtil util=new DbMetaUtil(driver, url, username, password, schema);
		System.out.println(util.getTables("TEST"));
	}
}
