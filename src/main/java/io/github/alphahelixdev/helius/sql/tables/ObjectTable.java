package io.github.alphahelixdev.helius.sql.tables;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.alphahelixdev.helius.Helius;
import io.github.alphahelixdev.helius.reflection.SaveField;
import io.github.alphahelixdev.helius.reflection.SaveMethod;
import io.github.alphahelixdev.helius.sql.SQLColumn;
import io.github.alphahelixdev.helius.sql.SQLConstraint;
import io.github.alphahelixdev.helius.sql.SQLDataType;
import io.github.alphahelixdev.helius.sql.SQLTableHandler;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;


public class ObjectTable {
	
	private final Class<?> objClass;
	private final List<SaveField> fields = new ArrayList<>();
	private final Gson gson = new GsonBuilder().create();
	private SQLTableHandler tableHandler;
	
	public ObjectTable(SQLTableHandler tableHandler, Class<?> objClass) throws ReflectiveOperationException {
		this.tableHandler = tableHandler;
		this.objClass = objClass;
		
		createTable();
	}
	
	private void createTable() throws ReflectiveOperationException {
		List<SQLColumn> columns = new ArrayList<>();
		
		for(SaveField field : Helius.getReflections().getDeclaredFields(this.objClass)) {
			SQLConstraint constraint = null;
			SQLDataType dataType = null;
			
			for(Annotation a : field.asNormal().getDeclaredAnnotations()) {
				String pack = a.annotationType().getName();
				
				if(pack.startsWith("io.github.alphahelixdev.helius.sql.annotations.constraints")) {
					
					SaveMethod valueMeth;
					
					try {
						valueMeth = Helius.getReflections().getDeclaredMethod("value", a.annotationType());
					} catch(NoSuchMethodException e) {
						valueMeth = null;
					}
					
					if(valueMeth == null) {
						constraint = new SQLConstraint(a.annotationType().getSimpleName(), "");
					} else {
						constraint = new SQLConstraint(a.annotationType().getSimpleName(), (String) valueMeth.invoke(a));
					}
				} else if(pack.startsWith("io.github.alphahelixdev.helius.sql.annotations.datatypes")) {
					SaveField type = Helius.getReflections().getDeclaredField("DATA_TYPE", a.annotationType());
					
					dataType = (SQLDataType) type.get(null);
				}
			}
			
			if(dataType != null) {
				columns.add(new SQLColumn(field.asNormal().getName(), dataType, constraint));
				this.fields.add(field);
			}
		}
		
		this.tableHandler.create(columns.toArray(new SQLColumn[columns.size()]));
	}
	
	public ObjectTable addObject(Object obj) {
		if(!obj.getClass().equals(this.objClass)) return this;
		
		String[] values = new String[this.fields.size()];
		
		for(int i = 0; i < values.length; i++) {
			SaveField field = fields.get(i);
			
			values[i] = gson.toJsonTree(field.get(obj), field.asNormal().getType()).toString();
		}
		
		this.tableHandler.insert(values);
		
		return this;
	}
	
	public List<Object> getObjects() throws ReflectiveOperationException {
		List<Object> objs = new ArrayList<>();
		
		for(int row = 0; row < this.tableHandler.getSyncRows().size(); row++) {
			Object inst = this.objClass.newInstance();
			
			for(int field = 0; field < this.fields.size(); field++) {
				String jsonedObj = this.tableHandler.getSyncRows().get(row).get(field);
				
				this.fields.get(field).set(inst, this.gson.fromJson(jsonedObj, Object.class), false);
			}
			objs.add(inst);
		}
		
		return objs;
	}
	
	public SQLTableHandler getTableHandler() {
		return tableHandler;
	}
}
