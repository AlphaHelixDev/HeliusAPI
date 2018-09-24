package io.github.alphahelixdev.helius.sql.annotations.datatypes.mysql;

import io.github.alphahelixdev.helius.sql.SQLDataType;
import io.github.alphahelixdev.helius.sql.mysql.MySQLDataType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Text {
	
	SQLDataType DATA_TYPE = MySQLDataType.TEXT;
	
}
