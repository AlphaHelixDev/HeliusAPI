package io.github.alphahelixdev.helius.sql;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class SQLColumn {
	
	private String name;
	private SQLDataType columnType;
	private int size;
	private List<SQLConstraint> constraints;
	
	public SQLColumn(String name, SQLDataType columnType) {
		this(name, columnType, -1);
	}
	
	public SQLColumn(String name, SQLDataType columnType, int size, SQLConstraint... constraints) {
		this.setName(name);
		this.setColumnType(columnType);
		this.setSize(size);
		this.setConstraints(Arrays.asList(constraints));
	}
	
	public SQLColumn(String name, SQLDataType columnType, SQLConstraint... constraints) {
		this(name, columnType, -1, constraints);
	}
	
	public SQLColumn setName(String name) {
		this.name = name;
		return this;
	}
	
	public int getSize() {
		return this.size;
	}
	
	public SQLColumn setColumnType(SQLDataType columnType) {
		this.columnType = columnType;
		return this;
	}
	
	public String getName() {
		return this.name;
	}
	
	public SQLColumn setSize(int size) {
		this.size = size;
		return this;
	}
	
	public SQLDataType getColumnType() {
		return this.columnType;
	}
	
	public SQLColumn setConstraints(List<SQLConstraint> constraints) {
		this.constraints = constraints;
		return this;
	}
	
	public List<SQLConstraint> getConstraints() {
		return this.constraints;
	}
	
	@Override
	public String toString() {
		return build();
	}
	
	public String build() {
		StringBuilder column = new StringBuilder(this.getName() + " " + this.getColumnType().sqlNameWithSize(this.getSize()));
		
		for(SQLConstraint constraint : this.getConstraints())
			column.append(" ").append(constraint);
		
		return column.append(",").toString();
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, columnType, size, constraints);
	}

	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		SQLColumn sqlColumn = (SQLColumn) o;
		return size == sqlColumn.size &&
				Objects.equals(name, sqlColumn.name) &&
				Objects.equals(columnType, sqlColumn.columnType) &&
				Objects.equals(constraints, sqlColumn.constraints);
	}
}
