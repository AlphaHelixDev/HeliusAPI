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
import java.util.Objects;

public class ObjectTable {
	
	private final Class<?> objClass;
	private final List<SaveField> fields = new ArrayList<>();
	private final Gson gson = new GsonBuilder().create();
	private SQLTableHandler tableHandler;
	
	public ObjectTable(SQLTableHandler tableHandler, Class<?> objClass) {
		this.setTableHandler(tableHandler);
		this.objClass = objClass;
		
		createTable();
	}
	
	private void createTable() {
		List<SQLColumn> columns = new ArrayList<>();

		for(SaveField field : Helius.getReflections().getDeclaredFields(this.getObjClass(), true)) {
			SQLConstraint constraint = null;
			SQLDataType dataType = null;
			
			for(Annotation a : field.asNormal().getDeclaredAnnotations()) {
				String pack = a.annotationType().getName();
				
				if(pack.startsWith("io.github.alphahelixdev.helius.sql.annotations.constraints")) {
					SaveMethod valueMeth = Helius.getReflections().getDeclaredMethod("value", a.annotationType());
					
					if(valueMeth == null) {
						constraint = new SQLConstraint(a.annotationType().getSimpleName(), "");
					} else {
						constraint = new SQLConstraint(a.annotationType().getSimpleName(), (String) valueMeth.invoke(a, true));
					}
				} else if(pack.startsWith("io.github.alphahelixdev.helius.sql.annotations.datatypes")) {
					dataType = (SQLDataType) Helius.getReflections().getDeclaredField("DATA_TYPE", a.annotationType()).getStatic();
				}
			}
			
			if(dataType != null) {
				columns.add(new SQLColumn(field.asNormal().getName(), dataType, constraint));
				this.getFields().add(field);
			}
		}
		
		this.getTableHandler().create(columns.toArray(new SQLColumn[columns.size()]));
	}
	
	public Class<?> getObjClass() {
		return this.objClass;
	}
	
	public List<SaveField> getFields() {
		return this.fields;
	}
	
	public SQLTableHandler getTableHandler() {
		return this.tableHandler;
	}
	
	public ObjectTable setTableHandler(SQLTableHandler tableHandler) {
		this.tableHandler = tableHandler;
		return this;
	}
	
	public ObjectTable addObject(Object obj) {
		if(!obj.getClass().equals(this.getObjClass())) return this;
		
		String[] values = new String[this.getFields().size()];
		
		for(int i = 0; i < values.length; i++) {
			SaveField field = this.getFields().get(i);
			
			values[i] = this.getGson().toJsonTree(field.get(obj), field.asNormal().getType()).toString();
		}
		
		this.getTableHandler().insert(values);
		
		return this;
	}
	
	public Gson getGson() {
		return this.gson;
	}
	
	public List<Object> getObjects() throws ReflectiveOperationException {
		List<Object> objs = new ArrayList<>();
		
		for(int row = 0; row < this.getTableHandler().getSyncRows().size(); row++) {
			Object inst = this.getObjClass().newInstance();
			
			for(int field = 0; field < this.getFields().size(); field++) {
				String jsonedObj = this.getTableHandler().getSyncRows().get(row).get(field);
				
				this.getFields().get(field).set(inst, this.getGson().fromJson(jsonedObj, Object.class), false);
			}
			objs.add(inst);
		}
		
		return objs;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(this.getObjClass(), this.getFields(), this.getGson(), this.getTableHandler());
	}
	
	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		ObjectTable that = (ObjectTable) o;
		return Objects.equals(this.getObjClass(), that.getObjClass()) &&
				Objects.equals(this.getFields(), that.getFields()) &&
				Objects.equals(this.getGson(), that.getGson()) &&
				Objects.equals(this.getTableHandler(), that.getTableHandler());
	}
	
	@Override
	public String toString() {
		return "ObjectTable{" +
				"                            objClass=" + this.objClass +
				",                             fields=" + this.fields +
				",                             gson=" + this.gson +
				",                             tableHandler=" + this.tableHandler +
				'}';
	}
}
