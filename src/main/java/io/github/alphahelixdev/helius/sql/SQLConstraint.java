package io.github.alphahelixdev.helius.sql;


public class SQLConstraint {
	
	public static final String NOT_NULL = "NOT_NULL";
	public static final String UNIQUE = "UNIQUE";
	public static final String PRIMARY_KEY = "PRIMARY_KEY";
	public static final String CHECK = "CHECK ($arg)";
	public static final String DEFAULT = "DEFAULT '$arg'";
	
	private String constraint;
	private String value;
	
	public SQLConstraint(String constraint, String value) {
		this.setConstraint(constraint);
		this.setValue(value);
	}
	
	@Override
	public String toString() {
		return this.getConstraint().replace("$arg", this.getValue());
	}
	
	public String getConstraint() {
		return this.constraint;
	}
	
	public SQLConstraint setConstraint(String constraint) {
		this.constraint = constraint;
		return this;
	}
	
	public String getValue() {
		return this.value;
	}
	
	public SQLConstraint setValue(String value) {
		this.value = value;
		return this;
	}
}
