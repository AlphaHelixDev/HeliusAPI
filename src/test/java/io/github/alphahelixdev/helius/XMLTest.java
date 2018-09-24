package io.github.alphahelixdev.helius;

import io.github.alphahelixdev.helius.sql.exceptions.NoConnectionException;
import io.github.alphahelixdev.helius.sql.tables.ObjectTable;
import org.junit.Test;

public class XMLTest {
	
	@Test
	public void test() throws NoConnectionException, ReflectiveOperationException {
		ObjectTable objectTable = new ObjectTable(
				Helius.fastSQLiteConnect(Helius.getHomePath() + "/test.db", "alright"),
				TableTester.class);
		
		objectTable.addObject(new TableTester());
		
		System.out.println(objectTable.getObjects());
	}
}
