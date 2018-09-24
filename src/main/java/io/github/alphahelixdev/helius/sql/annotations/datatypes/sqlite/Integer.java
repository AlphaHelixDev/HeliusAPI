package io.github.alphahelixdev.helius.sql.annotations.datatypes.sqlite;

import io.github.alphahelixdev.helius.sql.SQLDataType;
import io.github.alphahelixdev.helius.sql.sqlite.SQLiteDataType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Integer {
	SQLDataType DATA_TYPE = SQLiteDataType.INTEGER;
}
