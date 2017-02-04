package com.linzaixian.util.dbmeta.pojo;

public class Index {
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
	 * NON_UNIQUE boolean => 索引值是否可以不唯一。TYPE 为 tableIndexStatistic 时索引值为 false
	 */
	private boolean nonUnique;
	/**
	 * INDEX_QUALIFIER String => 索引类别（可为 null）；TYPE 为 tableIndexStatistic 时索引类别为 null
	 */
	private String indexQualifier;
	/**
	 * INDEX_NAME String => 索引名称；TYPE 为 tableIndexStatistic 时索引名称为 null
	 */
	private String indexName;
	/**
	 * TYPE short => 索引类型：
					tableIndexStatistic - 此标识与表的索引描述一起返回的表统计信息
					tableIndexClustered - 此为集群索引
					tableIndexHashed - 此为散列索引
					tableIndexOther - 此为某种其他样式的索引
	 */
	private short type;
	/**
	 * ORDINAL_POSITION short => 索引中的列序列号；TYPE 为 tableIndexStatistic 时该序列号为零
	 */
	private short ordinalPosition;
	/**
	 * COLUMN_NAME String => 列名称；TYPE 为 tableIndexStatistic 时列名称为 null
	 */
	private String columnName;
	/**
	 * ASC_OR_DESC String => 列排序序列，"A" => 升序，"D" => 降序，如果排序序列不受支持，可能为 null；TYPE 为 tableIndexStatistic 时排序序列为 null
	 */
	private String ascOrDesc;
	/**
	 * CARDINALITY int => TYPE 为 tableIndexStatistic 时，它是表中的行数；否则，它是索引中唯一值的数量。
	 */
	private int  cardinality;
	/**
	 * PAGES int => TYPE 为 tableIndexStatisic 时，它是用于表的页数，否则它是用于当前索引的页数。
	 */
	private int pages;
	/**
	 * FILTER_CONDITION String => 过滤器条件，如果有的话。（可能为 null）
	 */
	private String filterCondition;
	public String getTableCat() {
		return tableCat;
	}
	public String getTableSchem() {
		return tableSchem;
	}
	public String getTableName() {
		return tableName;
	}
	public boolean isNonUnique() {
		return nonUnique;
	}
	public String getIndexQualifier() {
		return indexQualifier;
	}
	public String getIndexName() {
		return indexName;
	}
	public short getType() {
		return type;
	}
	public short getOrdinalPosition() {
		return ordinalPosition;
	}
	public String getColumnName() {
		return columnName;
	}
	public String getAscOrDesc() {
		return ascOrDesc;
	}
	public int getCardinality() {
		return cardinality;
	}
	public int getPages() {
		return pages;
	}
	public String getFilterCondition() {
		return filterCondition;
	}
	public void setTableCat(String tableCat) {
		this.tableCat = tableCat;
	}
	public void setTableSchem(String tableSchem) {
		this.tableSchem = tableSchem;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public void setNonUnique(boolean nonUnique) {
		this.nonUnique = nonUnique;
	}
	public void setIndexQualifier(String indexQualifier) {
		this.indexQualifier = indexQualifier;
	}
	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}
	public void setType(short type) {
		this.type = type;
	}
	public void setOrdinalPosition(short ordinalPosition) {
		this.ordinalPosition = ordinalPosition;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	public void setAscOrDesc(String ascOrDesc) {
		this.ascOrDesc = ascOrDesc;
	}
	public void setCardinality(int cardinality) {
		this.cardinality = cardinality;
	}
	public void setPages(int pages) {
		this.pages = pages;
	}
	public void setFilterCondition(String filterCondition) {
		this.filterCondition = filterCondition;
	}
	@Override
	public String toString() {
		return "Index [tableCat=" + tableCat + ", tableSchem=" + tableSchem + ", tableName=" + tableName
				+ ", nonUnique=" + nonUnique + ", indexQualifier=" + indexQualifier + ", indexName=" + indexName
				+ ", type=" + type + ", ordinalPosition=" + ordinalPosition + ", columnName=" + columnName
				+ ", ascOrDesc=" + ascOrDesc + ", cardinality=" + cardinality + ", pages=" + pages
				+ ", filterCondition=" + filterCondition + "]";
	}
	
}
