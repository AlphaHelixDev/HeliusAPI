package io.github.alphahelixdev.helius.sql;

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
	
	public SQLColumn(String name, SQLDataType columnType, int size, SQLConstraint... constraints) {
		this.name = name;
		this.columnType = columnType;
		this.size = size;
		this.constraints = Arrays.asList(constraints);
	}
	
	public SQLColumn(String name, SQLDataType columnType, SQLConstraint... constraints) {
		this(name, columnType, -1, constraints);
	}
	
	@Override
	public String toString() {
		return build();
	}
	
	public String build() {
		StringBuilder column = new StringBuilder(this.name + " " + this.columnType.sqlNameWithSize(this.size));
		
		for(SQLConstraint constraint : this.constraints)
			column.append(" ").append(constraint);
		
		return column.append(",").toString();
	}
	
	public String getName() {
		return name;
	}
	
	public SQLColumn setName(String name) {
		this.name = name;
		return this;
	}
	
	public SQLDataType getColumnType() {
		return columnType;
	}
	
	public SQLColumn setColumnType(SQLDataType columnType) {
		this.columnType = columnType;
		return this;
	}
	
	public int getSize() {
		return size;
	}
	
	public SQLColumn setSize(int size) {
		this.size = size;
		return this;
	}
	
	public List<SQLConstraint> getConstraints() {
		return constraints;
	}
	
	public SQLColumn setConstraints(List<SQLConstraint> constraints) {
		this.constraints = constraints;
		return this;
	}
}
