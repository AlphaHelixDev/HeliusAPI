/*
 * Decompiled with CFR 0_132.
 */
package io.github.alphahelixdev.helius.sql.query;

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
	
	public int hashCode() {
		int PRIME = 59;
		int result = 1;
		String $constraint = this.getConstraint();
		result = result * 59 + ($constraint == null ? 43 : $constraint.hashCode());
		String $value = this.getValue();
		result = result * 59 + ($value == null ? 43 : $value.hashCode());
		return result;
	}
	
	public boolean equals(Object o) {
		if(o == this) {
			return true;
		}
		if(!(o instanceof SQLConstraint)) {
			return false;
		}
		SQLConstraint other = (SQLConstraint) o;
		if(!other.canEqual(this)) {
			return false;
		}
		String this$constraint = this.getConstraint();
		String other$constraint = other.getConstraint();
		if(this$constraint == null ? other$constraint != null : !this$constraint.equals(other$constraint)) {
			return false;
		}
		String this$value = this.getValue();
		String other$value = other.getValue();
		return this$value == null ? other$value == null : this$value.equals(other$value);
	}
	
	public String toString() {
		return this.getConstraint().replace("$arg", this.getValue());
	}
	
	public String getConstraint() {
		return this.constraint;
	}
	
	public String getValue() {
		return this.value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	public void setConstraint(String constraint) {
		this.constraint = constraint;
	}
	
	protected boolean canEqual(Object other) {
		return other instanceof SQLConstraint;
	}
}

