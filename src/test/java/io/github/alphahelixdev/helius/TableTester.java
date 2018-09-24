package io.github.alphahelixdev.helius;

import io.github.alphahelixdev.helius.sql.annotations.constraints.PrimaryKey;
import io.github.alphahelixdev.helius.sql.annotations.datatypes.mysql.Text;
import io.github.alphahelixdev.helius.sql.annotations.datatypes.sqlite.Blob;
import io.github.alphahelixdev.helius.sql.annotations.datatypes.sqlite.Integer;
import io.github.alphahelixdev.helius.sql.annotations.datatypes.sqlite.Real;

public class TableTester {
	
	@Text
	@PrimaryKey
	private String name = "max";
	@Blob
	private boolean sexy = true;
	@Integer
	private int age = 18;
	@Real
	private double weight = 85.3;
	
	
	@Override
	public String toString() {
		return "TableTester{" +
				"name='" + name + '\'' +
				", sexy=" + sexy +
				", age=" + age +
				", weight=" + weight +
				'}';
	}
}
