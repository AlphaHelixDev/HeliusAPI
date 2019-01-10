package io.github.alphahelixdev.helius.file.yaml;

import java.util.*;

public class ConfigSection {

	private final ConfigSection parent;
	private final String key;
	private Object value;

	public ConfigSection(String key, Object value) {
		this(null, key, value);
	}

	public ConfigSection(ConfigSection parent, String key, Object value) {
		this.parent = parent;
		this.key = key;
		this.setValue(value);
	}

	public boolean isRoot() {
		return this.getParent() == null;
	}

	public ConfigSection getRoot() {
		if(this.isRoot())
			return this;
		return this.getParent().getRoot();
	}

	public String getPath() {
		if(this.isRoot())
			return "";

		if(this.getParent().isRoot())
			return this.getKey();

		StringBuilder path = new StringBuilder();

		if(!isRoot())
			path.append(this.getParent().getPath()).append(".");

		path.append(this.getKey());

		return path.toString();
	}

	public boolean contains(String path) {
		return this.get(path) != null;
	}

	public Object get() {
		return this.get("");
	}

	public Object get(String path) {
		return this.get(path, null);
	}

	public Object get(String path, Object def) {
		if(path == null)
			return def;

		path = path.trim();

		if(path.equals(""))
			return this.getValue();

		final ConfigSection section = getConfigSection(path);

		if(section == null)
			return def;

		return section.get("");
	}

	public String getString(String path) {
		return this.getString(path, "");
	}

	public String getString(String path, String def) {
		final Object value = this.get(path);

		if(value == null)
			return def;

		if(value instanceof String)
			return (String) value;
		return def;
	}

	public boolean isString() {
		return this.isString("");
	}

	public boolean isString(String path) {
		final Object value = this.get(path);

		return (value instanceof String);
	}

	public int getInt() {
		return this.getInt("");
	}

	public int getInt(String path) {
		return this.getInt(path, 0);
	}

	public int getInt(String path, int def) {
		final Object value = this.get(path);

		if(value == null)
			return def;

		if(value instanceof Integer)
			return (int) value;
		return def;
	}

	public boolean isInt() {
		return this.isInt("");
	}

	public boolean isInt(String path) {
		final Object value = this.get(path);

		return (value instanceof Integer);
	}

	public boolean getBoolean() {
		return this.getBoolean("");
	}

	public boolean getBoolean(String path) {
		return this.getBoolean(path, false);
	}

	public boolean getBoolean(String path, boolean def) {
		final Object value = this.get(path);

		if(value == null)
			return def;

		if(value instanceof Boolean)
			return (boolean) value;
		return def;
	}

	public boolean isBoolean() {
		return this.isBoolean("");
	}

	public boolean isBoolean(String path) {
		final Object value = this.get(path);

		return (value instanceof Boolean);
	}

	public double getDouble() {
		return this.getDouble("");
	}

	public double getDouble(String path) {
		return this.getDouble(path, 0);
	}

	public double getDouble(String path, double def) {
		final Object value = this.get(path);

		if(value == null)
			return def;

		if(value instanceof Double)
			return (double) value;
		return def;
	}

	public boolean isDouble() {
		return this.isDouble("");
	}

	public boolean isDouble(String path) {
		final Object value = this.get(path);

		return (value instanceof Double);
	}

	public float getFloat() {
		return this.getFloat("");
	}

	public float getFloat(String path) {
		return this.getFloat(path, 0);
	}

	public float getFloat(String path, float def) {
		final Object value = this.get(path);

		if(value == null)
			return def;

		if(value instanceof Float)
			return (float) value;
		return def;
	}

	public boolean isFloat() {
		return this.isFloat("");
	}

	public boolean isFloat(String path) {
		final Object value = this.get(path);

		return (value instanceof Float);
	}

	public long getLong() {
		return this.getLong("");
	}

	public long getLong(String path) {
		return this.getLong(path, 0);
	}

	public long getLong(String path, long def) {
		final Object value = this.get(path);

		if(value == null)
			return def;

		if(value instanceof Long)
			return (long) value;
		return def;
	}

	public boolean isLong() {
		return this.isLong("");
	}

	public boolean isLong(String path) {
		final Object value = this.get(path);

		return (value instanceof Long);
	}

	public List<?> getList() {
		return this.getList("");
	}

	public List<?> getList(String path) {
		return this.getList(path, null);
	}

	public List<?> getList(String path, List<?> def) {
		final Object value = this.get(path);

		if(value == null)
			return def;

		if(value instanceof List)
			return (List<?>) value;
		return def;
	}

	public boolean isList() {
		return this.isList("");
	}

	public boolean isList(String path) {
		final Object value = this.get(path);

		return (value instanceof List);

	}

	public List<String> getKeys() {
		return this.getKeys("");
	}

	public List<String> getKeys(String path) {
		if(path == null)
			return new ArrayList<>();

		path = path.trim();

		if(!this.isConfigSection(path))
			return new ArrayList<>();

		final ConfigSection section = getConfigSection(path);

		if(!section.isHoldingConfigSections())
			return new ArrayList<>();

		@SuppressWarnings("unchecked") final List<ConfigSection> sections = (List<ConfigSection>) section.get("");
		final List<String> keys = new ArrayList<>();

		for(ConfigSection entry : sections)
			keys.add(entry.getKey());

		return keys;
	}

	public ConfigSection getSection(String path) {
		return this.getConfigSection(path);
	}

	public ConfigSection getConfigSection(String path) {
		if(path == null)
			return null;

		path = path.trim();

		if(path.equals(""))
			return this;

		if(!this.isSet(""))
			return null;

		if(!path.contains(".")) {
			if(!this.isConfigSection(path))
				return null;

			if(this.getValue() instanceof List) {
				try {
					@SuppressWarnings("unchecked")
					List<ConfigSection> sections = (List<ConfigSection>) this.getValue();
					for(ConfigSection section : sections) {
						if(section == null)
							continue;

						if(section.getKey().equals(path))
							return section;
					}
				} catch(ClassCastException ignored) {
				}
				return null;

			} else
				return null;

		} else {
			String[] keys = path.split("\\.");
			String key = path;
			if(keys.length > 0)
				key = keys[0];
			StringBuilder subPath = new StringBuilder();
			if(keys.length > 1) {
				subPath = new StringBuilder(keys[1]);
				for(int i = 2; i < keys.length; i++)
					subPath.append(".").append(keys[i]);
			}

			if(key.equals(""))
				return this;

			ConfigSection section = getConfigSection(key);

			if(section == null)
				return null;

			return section.getConfigSection(subPath.toString());
		}
	}

	public ConfigSection createSection(String path) {
		return this.createConfigSection(path);
	}

	public ConfigSection createConfigSection(String path) {
		if(path == null)
			return null;

		path = path.trim();

		if(path.equals(""))
			return this;

		String[] keys = path.split("\\.");
		String key = path;
		if(keys.length > 0)
			key = keys[0];
		StringBuilder subPath = new StringBuilder();
		if(keys.length > 1) {
			subPath = new StringBuilder(keys[1]);
			for(int i = 2; i < keys.length; i++)
				subPath.append(".").append(keys[i]);
			subPath = new StringBuilder(subPath.toString().trim());
		}

		if(this.isConfigSection(key)) {
			final ConfigSection section = getConfigSection(key);

			if(subPath.length() == 0)
				return section;

			return section.createConfigSection(subPath.toString());
		} else {
			if(this.getValue() instanceof List) {
				try {
					List<ConfigSection> sections = (List<ConfigSection>) this.getValue();
					ConfigSection section = new ConfigSection(this, key, null);
					sections.add(section);
					this.setValue(sections);

					if(subPath.length() == 0)
						return section;

					return section.createConfigSection(subPath.toString());
				} catch(ClassCastException ignored) {
				}
			}

			final ConfigSection section = new ConfigSection(this, key, null);
			List<ConfigSection> sections = new ArrayList<>();
			sections.add(section);
			this.setValue(sections);

			if(subPath.length() == 0)
				return section;

			return section.createConfigSection(subPath.toString());
		}
	}

	public void set(Object value) {
		this.set("", value);
	}

	public void set(String path, Object value) {
		if(path == null)
			return;

		path = path.trim();

		if(path.equals("")) {
			this.setValue(value);
			return;
		}

		String[] keys = path.split("\\.");
		String key = path;
		if(keys.length > 0)
			key = keys[0];
		StringBuilder subPath = new StringBuilder();
		if(keys.length > 1) {
			subPath = new StringBuilder(keys[1]);
			for(int i = 2; i < keys.length; i++)
				subPath.append(".").append(keys[i]);
		}

		if(this.isConfigSection(key)) {
			final ConfigSection section = getConfigSection(key);
			section.set(subPath.toString(), value);

		} else {
			final ConfigSection section = new ConfigSection(this, key, null);
			if(this.getValue() instanceof List) {
				try {
					List<ConfigSection> sections = (List<ConfigSection>) this.getValue();
					sections.add(section);

				} catch(ClassCastException ex) {
					List<ConfigSection> sections = new ArrayList<>();
					sections.add(section);
					this.setValue(sections);
				}
			} else {
				List<ConfigSection> sections = new ArrayList<>();
				sections.add(section);
				this.setValue(sections);
			}

			section.set(subPath.toString(), value);
		}
	}

	public boolean isSet(String path) {
		if(path == null)
			return false;

		final ConfigSection section = this.getConfigSection(path);

		return section != null && section.get("") != null;
	}

	public boolean isHoldingConfigSections() {
		if(this.getValue() == null)
			return false;

		try {
			final List<ConfigSection> sections = (List<ConfigSection>) this.getValue();
			return sections.size() > 0 && (sections.get(0) != null);
		} catch(ClassCastException e) {
			return false;
		}
	}

	public boolean isSection() {
		return this.isSection("");
	}

	public boolean isSection(String path) {
		return this.isConfigSection(path);
	}

	public boolean isConfigSection() {
		return this.isConfigSection("");
	}

	public boolean isConfigSection(String path) {
		if(path == null)
			return false;

		path = path.trim();

		if(path.equals(""))
			return true;

		if(!(this.getValue() instanceof List))
			return false;

		try {
			final List<ConfigSection> sections = (List<ConfigSection>) this.getValue();
			for(ConfigSection section : sections) {
				if(section == null)
					continue;

				String[] keys = path.split("\\.");
				String key = path;
				if(keys.length > 0)
					key = keys[0];
				StringBuilder subPath = new StringBuilder();
				if(keys.length > 1) {
					subPath = new StringBuilder(keys[1]);
					for(int i = 2; i < keys.length; i++)
						subPath.append(".").append(keys[i]);
				}

				if(!section.getKey().equals(key))
					continue;

				return section.isConfigSection(subPath.toString());
			}

			return false;

		} catch(ClassCastException e) {
			return false;
		}
	}

	public Map<String, Object> getValues() {
		final Map<String, Object> out = new LinkedHashMap<>();

		if(this.getKey() == null)
			return out;

		if(this.getValue() instanceof List) {
			try {
				List<ConfigSection> sections = (List<ConfigSection>) this.getValue();
				for(ConfigSection entry : sections)
					if(entry.isHoldingConfigSections())
						out.put(entry.getKey(), entry.getValues());
					else
						out.put(entry.getKey(), entry.get(""));

			} catch(ClassCastException e) {
				out.put(this.getKey(), this.getValue());
			}
		} else
			out.put(this.getKey(), this.getValue());

		return out;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.getParent(), this.getKey(), this.getValue());
	}

	public ConfigSection getParent() {
		return this.parent;
	}

	public String getKey() {
		return this.key;
	}

	public Object getValue() {
		return this.value;
	}

	private ConfigSection setValue(Object value) {
		this.value = value;
		return this;
	}

	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		ConfigSection that = (ConfigSection) o;
		return Objects.equals(this.getParent(), that.getParent()) &&
				Objects.equals(this.getKey(), that.getKey()) &&
				Objects.equals(this.getValue(), that.getValue());
	}

	@Override
	public String toString() {
		return "ConfigSection{" +
				"parent=" + parent +
				", key='" + key + '\'' +
				", value=" + value +
				'}';
	}
}
