package com.linzaixian.util.dbmeta.pojo;

public class Key {
	/**
	 * PKTABLE_CAT String => 被导入的主键表类别（可为 null）
	 */
	private String pkTableCat; 
	/**
	 * PKTABLE_SCHEM String => 被导入的主键表模式（可为 null）
	 */
	private String pkTableSchem;
	/**
	 * PKTABLE_NAME String => 被导入的主键表名称
	 */
	private String pkTableName;
	/**
	 * PKCOLUMN_NAME String => 被导入的主键列名称
	 */
	private String pkColumnName;
	/**
	 * FKTABLE_CAT  String => 外键表类别（可为 null）
	 */
	private String fkTableCat;
	/**
	 * FKTABLE_SCHEM String => 外键表模式（可为 null）
	 */
	private String fkTableSchem;
	/**
	 * FKTABLE_NAME String => 外键表名称
	 */
	private String fkTableName;
	/**
	 * FKCOLUMN_NAME String => 外键列名称
	 */
	private String fkColumnName;
	/**
	 * KEY_SEQ short => 外键中的序列号（值 1 表示外键中的第一列，值 2 表示外键中的第二列）。
	 */
	private short keySeq;
	/**
	 * UPDATE_RULE short => 更新主键时外键发生的变化：
				importedNoAction - 如果已经被导入，则不允许更新主键
				importedKeyCascade - 将导入的键更改为与主键更新一致
				importedKeySetNull - 如果已更新导入键的主键，则将导入键更改为 NULL
				importedKeySetDefault - 如果已更新导入键的主键，则将导入键更改为默认值
				importedKeyRestrict - 与 importedKeyNoAction 相同（为了与 ODBC 2.x 兼容）
	 */
	private short updateRule;
	/**
	 *DELETE_RULE  short => 删除主键时外键发生的变化。
				importedKeyNoAction - 如果已经导入，则不允许删除主键
				importedKeyCascade - 删除导入删除键的行
				importedKeySetNull - 如果已删除导入键的主键，则将导入键更改为 NULL
				importedKeyRestrict - 与 importedKeyNoAction 相同（为了与 ODBC 2.x 兼容）
				importedKeySetDefault - 如果已删除导入键的主键，则将导入键更改为默认值
	 */
	private short deleteRule;
	/**
	 * FK_NAME String => 外键的名称（可为 null）
	 */
	private String fkName;
	/**
	 * PK_NAME String => 主键的名称（可为 null）
	 */
	private String pkName;
	/**
	 *DEFERRABILITY   short => 是否可以将对外键约束的评估延迟到提交时间
				importedKeyInitiallyDeferred - 有关定义，请参见 SQL92
				importedKeyInitiallyImmediate - 有关定义，请参见 SQL92
				importedKeyNotDeferrable - 有关定义，请参见 SQL92
	 */
	private short deferrability;
	public String getPkTableCat() {
		return pkTableCat;
	}
	public String getPkTableSchem() {
		return pkTableSchem;
	}
	public String getPkTableName() {
		return pkTableName;
	}
	public String getPkColumnName() {
		return pkColumnName;
	}
	public String getFkTableCat() {
		return fkTableCat;
	}
	public String getFkTableSchem() {
		return fkTableSchem;
	}
	public String getFkTableName() {
		return fkTableName;
	}
	public String getFkColumnName() {
		return fkColumnName;
	}
	public short getKeySeq() {
		return keySeq;
	}
	public short getUpdateRule() {
		return updateRule;
	}
	public short getDeleteRule() {
		return deleteRule;
	}
	public String getFkName() {
		return fkName;
	}
	public String getPkName() {
		return pkName;
	}
	public short getDeferrability() {
		return deferrability;
	}
	public void setPkTableCat(String pkTableCat) {
		this.pkTableCat = pkTableCat;
	}
	public void setPkTableSchem(String pkTableSchem) {
		this.pkTableSchem = pkTableSchem;
	}
	public void setPkTableName(String pkTableName) {
		this.pkTableName = pkTableName;
	}
	public void setPkColumnName(String pkColumnName) {
		this.pkColumnName = pkColumnName;
	}
	public void setFkTableCat(String fkTableCat) {
		this.fkTableCat = fkTableCat;
	}
	public void setFkTableSchem(String fkTableSchem) {
		this.fkTableSchem = fkTableSchem;
	}
	public void setFkTableName(String fkTableName) {
		this.fkTableName = fkTableName;
	}
	public void setFkColumnName(String fkColumnName) {
		this.fkColumnName = fkColumnName;
	}
	public void setKeySeq(short keySeq) {
		this.keySeq = keySeq;
	}
	public void setUpdateRule(short updateRule) {
		this.updateRule = updateRule;
	}
	public void setDeleteRule(short deleteRule) {
		this.deleteRule = deleteRule;
	}
	public void setFkName(String fkName) {
		this.fkName = fkName;
	}
	public void setPkName(String pkName) {
		this.pkName = pkName;
	}
	public void setDeferrability(short deferrability) {
		this.deferrability = deferrability;
	}
	@Override
	public String toString() {
		return "Key [pkTableCat=" + pkTableCat + ", pkTableSchem=" + pkTableSchem + ", pkTableName=" + pkTableName
				+ ", pkColumnName=" + pkColumnName + ", fkTableCat=" + fkTableCat + ", fkTableSchem=" + fkTableSchem
				+ ", fkTableName=" + fkTableName + ", fkColumnName=" + fkColumnName + ", keySeq=" + keySeq
				+ ", updateRule=" + updateRule + ", deleteRule=" + deleteRule + ", fkName=" + fkName + ", pkName="
				+ pkName + ", deferrability=" + deferrability + "]";
	}
	
	
}
