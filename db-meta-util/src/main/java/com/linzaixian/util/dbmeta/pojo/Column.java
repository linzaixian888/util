package com.linzaixian.util.dbmeta.pojo;

public class Column {
	/**
	 * TABLE_CAT String => 表类别（可为 null） 
	 */
	private String tableCat;
	/**
	 * TABLE_SCHEM String => 表模式（可为 null） 
	 */
	private String tableSchem;
	/**
	 * TABLE_NAME String => 表名称 
	 */
	private String tableName;
	/**
	 * COLUMN_NAME String => 列名称 
	 */
	private String columnName;
	/**
	 * DATA_TYPE int => 来自 java.sql.Types 的 SQL 类型 
	 */
	private int dataType;
	/**
	 * TYPE_NAME String => 数据源依赖的类型名称，对于 UDT，该类型名称是完全限定的 
	 */
	private String typeName;
	/**
	 * COLUMN_SIZE int => 列的大小
	 */
	private int columnSize;
	/**
	 * BUFFER_LENGTH 未被使用。 
	 */
	private Object bufferLength;
	/**
	 * DECIMAL_DIGITS int => 小数部分的位数。对于 DECIMAL_DIGITS 不适用的数据类型，则返回 Null
	 */
	private int decimalDigits;
	/**
	 * NUM_PREC_RADIX int => 基数（通常为 10 或 2） 
	 */
	private int numPrecRadix;
	/**
	 * NULLABLE int => 是否允许使用 NULL。 
	 *	columnNoNulls - 可能不允许使用 NULL 值 
	 *	columnNullable - 明确允许使用 NULL 值 
	 *	columnNullableUnknown - 不知道是否可使用 null 
	 */
	private int nullable;
	/**
	 * REMARKS String => 描述列的注释（可为 null） 
	 */
	private String remarks;
	/**
	 * COLUMN_DEF String => 该列的默认值，当值在单引号内时应被解释为一个字符串（可为 null） 
	 */
	private String columnDef;
	/**
	 * SQL_DATA_TYPE int => 未使用 
	 */
	private int sqlDataType;
	/**
	 * SQL_DATETIME_SUB int => 未使用 
	 */
	private int sqlDatetimeSub;
	/**
	 * CHAR_OCTET_LENGTH int => 对于 char 类型，该长度是列中的最大字节数 
	 */
	private int charOctetLength;
	/**
	 * ORDINAL_POSITION int => 表中的列的索引（从 1 开始） 
	 */
	private int ordinalPosuition;
	/**
	 * IS_NULLABLE String => ISO 规则用于确定列是否包括 null。 
	 *	YES --- 如果参数可以包括 NULL 
	 *	NO --- 如果参数不可以包括 NULL 
	 *	空字符串 --- 如果不知道参数是否可以包括 null 
	 */
	private String isNullable;
	/**
	 * SCOPE_CATLOG String => 表的类别，它是引用属性的作用域（如果 DATA_TYPE 不是 REF，则为 null） 
	 */
	private String scopeCatlog;
	/**
	 * SCOPE_SCHEMA String => 表的模式，它是引用属性的作用域（如果 DATA_TYPE 不是 REF，则为 null） 
	 */
	private String scopeSchema;
	/**
	 * SCOPE_TABLE String => 表名称，它是引用属性的作用域（如果 DATA_TYPE 不是 REF，则为 null） 
	 */
	private String scopeTable;
	/**
	 * SOURCE_DATA_TYPE short => 不同类型或用户生成 Ref 类型、来自 java.sql.Types 的 SQL 类型的源类型（如果 DATA_TYPE 不是 DISTINCT 或用户生成的 REF，则为 null） 
	 */
	private short sourceDataType;
	/**
	 * IS_AUTOINCREMENT String => 指示此列是否自动增加 
	 *	YES --- 如果该列自动增加 
	 *	NO --- 如果该列不自动增加 
	 *	空字符串 --- 如果不能确定该列是否是自动增加参数 
	 */
	private String isAutoincrement;
	public String getTableCat() {
		return tableCat;
	}
	public void setTableCat(String tableCat) {
		this.tableCat = tableCat;
	}
	public String getTableSchem() {
		return tableSchem;
	}
	public void setTableSchem(String tableSchem) {
		this.tableSchem = tableSchem;
	}
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
	public int getDataType() {
		return dataType;
	}
	public void setDataType(int dataType) {
		this.dataType = dataType;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public int getDecimalDigits() {
		return decimalDigits;
	}
	public int getColumnSize() {
		return columnSize;
	}
	public void setColumnSize(int columnSize) {
		this.columnSize = columnSize;
	}
	public Object getBufferLength() {
		return bufferLength;
	}
	public void setDecimalDigits(int decimalDigits) {
		this.decimalDigits = decimalDigits;
	}
	public int getNumPrecRadix() {
		return numPrecRadix;
	}
	public void setNumPrecRadix(int numPrecRadix) {
		this.numPrecRadix = numPrecRadix;
	}
	public int getNullable() {
		return nullable;
	}
	public void setNullable(int nullable) {
		this.nullable = nullable;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getColumnDef() {
		return columnDef;
	}
	public void setColumnDef(String columnDef) {
		this.columnDef = columnDef;
	}
	public int getCharOctetLength() {
		return charOctetLength;
	}
	public void setCharOctetLength(int charOctetLength) {
		this.charOctetLength = charOctetLength;
	}
	
	public int getOrdinalPosuition() {
		return ordinalPosuition;
	}
	public void setOrdinalPosuition(int ordinalPosuition) {
		this.ordinalPosuition = ordinalPosuition;
	}
	public String getIsNullable() {
		return isNullable;
	}
	public void setIsNullable(String isNullable) {
		this.isNullable = isNullable;
	}
	public String getScopeCatlog() {
		return scopeCatlog;
	}
	public void setScopeCatlog(String scopeCatlog) {
		this.scopeCatlog = scopeCatlog;
	}
	public String getScopeSchema() {
		return scopeSchema;
	}
	public void setScopeSchema(String scopeSchema) {
		this.scopeSchema = scopeSchema;
	}
	public String getScopeTable() {
		return scopeTable;
	}
	public void setScopeTable(String scopeTable) {
		this.scopeTable = scopeTable;
	}
	public short getSourceDataType() {
		return sourceDataType;
	}
	public void setSourceDataType(short sourceDataType) {
		this.sourceDataType = sourceDataType;
	}
	public String getIsAutoincrement() {
		return isAutoincrement;
	}
	public void setIsAutoincrement(String isAutoincrement) {
		this.isAutoincrement = isAutoincrement;
	}
	public int getSqlDataType() {
		return sqlDataType;
	}
	public void setSqlDataType(int sqlDataType) {
		this.sqlDataType = sqlDataType;
	}
	public int getSqlDatetimeSub() {
		return sqlDatetimeSub;
	}
	public void setSqlDatetimeSub(int sqlDatetimeSub) {
		this.sqlDatetimeSub = sqlDatetimeSub;
	}
	public void setBufferLength(Object bufferLength) {
		this.bufferLength = bufferLength;
	}
	@Override
	public String toString() {
		return "Column [tableCat=" + tableCat + ", tableSchem=" + tableSchem + ", tableName=" + tableName
				+ ", columnName=" + columnName + ", dataType=" + dataType + ", typeName=" + typeName + ", columnSize="
				+ columnSize + ", bufferLength=" + bufferLength + ", decimalDigits=" + decimalDigits + ", numPrecRadix="
				+ numPrecRadix + ", nullable=" + nullable + ", remarks=" + remarks + ", columnDef=" + columnDef
				+ ", sqlDataType=" + sqlDataType + ", sqlDatetimeSub=" + sqlDatetimeSub + ", charOctetLength="
				+ charOctetLength + ", ordinalPosuition=" + ordinalPosuition + ", isNullable=" + isNullable
				+ ", scopeCatlog=" + scopeCatlog + ", scopeSchema=" + scopeSchema + ", scopeTable=" + scopeTable
				+ ", sourceDataType=" + sourceDataType + ", isAutoincrement=" + isAutoincrement + "]";
	}
	
	
	
	
}
