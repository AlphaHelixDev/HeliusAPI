package io.github.alphahelixdev.helius.file.yaml;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.representer.Representer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class YamlFile extends FileConfig {
	
	private static final int YAML_INDENT_SIZE = 4;
	private static final DumperOptions.FlowStyle YAML_FLOW_STYLE = DumperOptions.FlowStyle.BLOCK;
	private final DumperOptions options = new DumperOptions();
	private final Representer representer = new Representer();
	
	private final Yaml yaml = new Yaml(new Constructor(), representer, options);
	
	public YamlFile() {
		options.setIndent(YAML_INDENT_SIZE);
		
		options.setDefaultFlowStyle(YAML_FLOW_STYLE);
		representer.setDefaultFlowStyle(YAML_FLOW_STYLE);
	}
	
	public static YamlFile loadFromFile(String filePath) {
		final File file = new File(filePath);
		
		if(!file.isFile())
			return new YamlFile();
		
		return loadFromFile(file);
	}
	
	public static YamlFile loadFromFile(File file) {
		if(file == null)
			return new YamlFile();
		
		final YamlFile config = new YamlFile();
		
		try {
			config.load(file);
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		return config;
	}
	
	public static YamlFile loadFromStream(InputStream stream) {
		if(stream == null)
			return new YamlFile();
		
		final YamlFile config = new YamlFile();
		
		try {
			config.load(stream);
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		return config;
	}
	
	public String saveToString() {
		return yaml.dump(getValues());
	}
	
	public void loadFromString(String config) {
		if(config == null)
			return;
		
		Map<?, ?> input = null;
		try {
			input = yaml.load(config);
		} catch(YAMLException | ClassCastException e) {
			e.printStackTrace();
		}
		
		if(input != null)
			convertMapsToSections(input, this);
	}
	
	private void convertMapsToSections(Map<?, ?> input, ConfigSection section) {
		for(Map.Entry<?, ?> entry : input.entrySet()) {
			final String key = entry.getKey().toString();
			final Object value = entry.getValue();
			
			if(value instanceof Map)
				convertMapsToSections((Map<?, ?>) value, section.createSection(key));
			else
				section.set(key, value);
		}
	}
}
