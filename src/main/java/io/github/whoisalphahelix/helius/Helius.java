package io.github.whoisalphahelix.helius;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.whoisalphahelix.helius.cipher.HeliusCipher;
import io.github.whoisalphahelix.helius.file.json.JsonReadFile;
import io.github.whoisalphahelix.helius.reflection.Reflections;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class Helius {

	private static final Set<Cache> CACHES = new HashSet<>();
	private static final HeliusCipher CIPHER = new HeliusCipher();
	private static final Reflections REFLECTIONS = new Reflections();
	private static final Gson GSON = new GsonBuilder().create();
	private static final String HOME_PATH = System.getProperty("user.home") + "/storage";
	@Getter
	@Setter
	private static Helius instance;
	@Getter
	private static Timer cacheTimer;
	@Getter
	@Setter
	private static Logger log;

	static {
		Helius.setLog(Logger.getLogger("Helius"));
		System.setProperty(
				"java.util.logging.SimpleFormatter.format",
				"[%1$tF %1$tT] [%4$-7s]: %5$s %n");

		try {
			File logFile = createFile(new File(Helius.getHomePath() + "/helius/helius.log"));

			FileHandler fh = new FileHandler(logFile.getAbsolutePath());
			Helius.getLog().addHandler(fh);

			fh.setFormatter(new Formatter() {
				@Override
				public String format(LogRecord record) {
					SimpleDateFormat logTime = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
					Calendar cal = new GregorianCalendar();
					cal.setTimeInMillis(record.getMillis());
					return "[" + logTime.format(cal.getTime()) + "] [" + record.getLevel() + "]: "
							+ record.getMessage() + "\n";
				}
			});
		} catch(SecurityException | IOException e) {
			e.printStackTrace();
		}
	}
	
	private Helius() {
		JsonReadFile appFile = new JsonReadFile(Helius.getHomePath() + "/helius/apps.json");

		for(JsonElement obj : appFile.getArrayValues()) {
			if(!obj.isJsonObject())
				continue;
			JsonObject info = (JsonObject) obj;

			try {
				Helius.loadJar(new File(info.get("jar").getAsString()), info.get("main").getAsString()).newInstance();
				Helius.getLog().info("Loaded " + info.get("name").getAsString());
			} catch(ReflectiveOperationException e) {
				e.printStackTrace();
			}
		}
		
		startCacheClearTask();
		Helius.getLog().info("Helius has been loaded!");
	}

	public static String getHomePath() {
		return Helius.HOME_PATH;
	}

	public static Class<?> loadJar(File jarFile, String mainClass) throws ClassNotFoundException {
		return Helius.loadJar(jarFile, mainClass, Helius.class.getClassLoader());
	}

	private static void startCacheClearTask() {
		if(cacheTimer != null)
			return;
		
		cacheTimer = new Timer();
		
		cacheTimer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				for(Cache c : Helius.getCaches()) {
					c.save();
					c.clear();
					Helius.getLog().info(c.clearMessage());
				}
			}
		}, Cache.TIME * 1000 * 60, Cache.TIME * 1000 * 60);
	}

	public static <T extends ClassLoader> Class<?> loadJar(File jarFile, String mainClass, T classLoader)
	throws ClassNotFoundException {
		for(Class<?> classes : Helius.loadJar(jarFile, classLoader)) {
			if(classes.getName().equals(mainClass))
				return classes;
		}
		throw new ClassNotFoundException("Unable to find the main class '" + mainClass + "' inside '" + jarFile + "'");
	}

	public static Set<Cache> getCaches() {
		return Helius.CACHES;
	}

	public static <T extends ClassLoader> Set<Class<?>> loadJar(File jarFile, T classLoader) {
		Set<Class<?>> classes = new HashSet<>();

		try {
			URLClassLoader loader;

			if(classLoader != null)
				loader = new URLClassLoader(new URL[]{jarFile.toURI().toURL()}, classLoader);
			else
				loader = new URLClassLoader(new URL[]{jarFile.toURI().toURL()});

			JarFile file = new JarFile(jarFile);

			for(Enumeration<JarEntry> entries = file.entries(); entries.hasMoreElements(); ) {
				JarEntry entry = entries.nextElement();
				String jarName = entry.getName().replace('/', '.');

				if(jarName.endsWith(".class")) {
					String clName = jarName.substring(0, jarName.length() - 6);

					if(clName.startsWith("io.github.alphahelixdev")) {
						getLogger().info("Loading: " + clName + " for " + loader);

						Class<?> cls = Class.forName(clName, true, loader);

						if(cls != null)
							classes.add(cls);
					}
				}
			}
			file.close();
		} catch(IOException | ReflectiveOperationException ex) {
			Helius.getLog().severe("Error ocurred at getting classes, log: " + ex);
			ex.printStackTrace();
		}

		return classes;
	}

	public static File createFile(File file) {
		if(!file.exists() && !file.isDirectory()) {
			try {
				file.getParentFile().mkdirs();
				file.createNewFile();
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
		return file;
	}

	public static String read(File file) {
		try {
			return FileUtils.readFileToString(file, Charset.defaultCharset());
		} catch(IOException e) {
			e.printStackTrace();
			return "";
		}
	}

	public static File createFolder(File folder) {
		if(folder.isDirectory())
			folder.mkdirs();
		return folder;
	}

	public static String replaceLast(String text, String regex, String replacement) {
		return text.replaceFirst("(?s)(.*)" + regex, "$1" + replacement);
	}

	public static <V> V runAsync(Callable<V> task) {
		ExecutorService service = Executors.newFixedThreadPool(1);
		CompletionService<V> completionService = new ExecutorCompletionService<>(service);
		completionService.submit(task);

		try {
			V val = completionService.take().get();

			service.shutdown();

			return val;
		} catch(InterruptedException | ExecutionException e) {
			e.printStackTrace();
			service.shutdown();
			return null;
		}
	}

	public static Set<Class<?>> loadJar(File jarFile) {
		return Helius.loadJar(jarFile, Helius.class.getClassLoader());
	}
	
	public static Logger getLogger() {
		return getLog();
	}
	
	public static void main(String[] args) {
		Helius.setInstance(new Helius());
	}

	public static HeliusCipher getCipher() {
		return Helius.CIPHER;
	}

	public static Reflections getReflections() {
		return Helius.REFLECTIONS;
	}
	
	public static Gson getGson() {
		return Helius.GSON;
	}
	
	public static void addCache(Cache cache) {
		Helius.getCaches().add(cache);
	}
}
