/*
 * Decompiled with CFR 0_132.
 */
package io.github.alphahelixdev.helius.sql.query;

import java.util.Arrays;
import java.util.List;

public class SQLColumn {
	private String name;
	private SQLDataType columnType;
	private int size;
	private List<SQLConstraint> constraints;
	
	public SQLColumn(String name, SQLDataType columnType) {
		this(name, columnType, -1);
	}
	
	public /* varargs */ SQLColumn(String name, SQLDataType columnType, int size, SQLConstraint... constraints) {
		this.setName(name);
		this.setColumnType(columnType);
		this.setSize(size);
		this.setConstraints(Arrays.asList(constraints));
	}
	
	public /* varargs */ SQLColumn(String name, SQLDataType columnType, SQLConstraint... constraints) {
		this(name, columnType, -1, constraints);
	}
	
	public int hashCode() {
		int PRIME = 59;
		int result = 1;
		String $name = this.getName();
		result = result * 59 + ($name == null ? 43 : $name.hashCode());
		SQLDataType $columnType = this.getColumnType();
		result = result * 59 + ($columnType == null ? 43 : $columnType.hashCode());
		result = result * 59 + this.getSize();
		List<SQLConstraint> $constraints = this.getConstraints();
		result = result * 59 + ($constraints == null ? 43 : $constraints.hashCode());
		return result;
	}
	
	public boolean equals(Object o) {
		if(o == this) {
			return true;
		}
		if(!(o instanceof SQLColumn)) {
			return false;
		}
		SQLColumn other = (SQLColumn) o;
		if(!other.canEqual(this)) {
			return false;
		}
		String this$name = this.getName();
		String other$name = other.getName();
		if(this$name == null ? other$name != null : !this$name.equals(other$name)) {
			return false;
		}
		SQLDataType this$columnType = this.getColumnType();
		SQLDataType other$columnType = other.getColumnType();
		if(this$columnType == null ? other$columnType != null : !this$columnType.equals(other$columnType)) {
			return false;
		}
		if(this.getSize() != other.getSize()) {
			return false;
		}
		List<SQLConstraint> this$constraints = this.getConstraints();
		List<SQLConstraint> other$constraints = other.getConstraints();
		return this$constraints == null ? other$constraints == null : this$constraints.equals(other$constraints);
	}
	
	public String toString() {
		return this.build();
	}
	
	public String build() {
		StringBuilder column = new StringBuilder(this.getName() + " " + this.getColumnType().sqlNameWithSize(this.getSize()));
		for(SQLConstraint constraint : this.getConstraints()) {
			column.append(" ").append(constraint);
		}
		return column.append(",").toString();
	}
	
	public String getName() {
		return this.name;
	}
	
	public SQLDataType getColumnType() {
		return this.columnType;
	}
	
	public int getSize() {
		return this.size;
	}
	
	public List<SQLConstraint> getConstraints() {
		return this.constraints;
	}
	
	public void setConstraints(List<SQLConstraint> constraints) {
		this.constraints = constraints;
	}
	
	public void setSize(int size) {
		this.size = size;
	}
	
	public void setColumnType(SQLDataType columnType) {
		this.columnType = columnType;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	protected boolean canEqual(Object other) {
		return other instanceof SQLColumn;
	}
}

