package io.github.alphahelixdev.helius.web;

import com.gargoylesoftware.css.parser.CSSErrorHandler;
import com.gargoylesoftware.css.parser.CSSException;
import com.gargoylesoftware.css.parser.CSSParseException;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.ScriptException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HTMLParserListener;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.JavaScriptErrorListener;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;
import java.util.logging.Level;

public class WebReader {
	
	private static final WebClient CLIENT = new WebClient(BrowserVersion.BEST_SUPPORTED);
	private static final int DEFAULT_TIME_OUT = 5000;
	
	static {
		LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");
		
		java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);
		java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF);
		
		WebReader.getClient().getOptions().setJavaScriptEnabled(true);
		WebReader.getClient().getOptions().setThrowExceptionOnScriptError(false);
		WebReader.getClient().getOptions().setThrowExceptionOnFailingStatusCode(false);
		WebReader.getClient().getOptions().setCssEnabled(false);
		
		WebReader.getClient().setIncorrectnessListener((message, origin) -> {});
		
		WebReader.getClient().setCssErrorHandler(new CSSErrorHandler() {
			@Override
			public void warning(CSSParseException exception) throws CSSException {
			}
			
			@Override
			public void fatalError(CSSParseException exception) throws CSSException {
			}
			
			@Override
			public void error(CSSParseException exception) throws CSSException {
			}
		});
		
		WebReader.getClient().setJavaScriptErrorListener(new JavaScriptErrorListener() {
			@Override
			public void timeoutError(HtmlPage arg0, long arg1, long arg2) {
			}
			
			@Override
			public void scriptException(HtmlPage arg0, ScriptException arg1) {
			}
			
			@Override
			public void malformedScriptURL(HtmlPage arg0, String arg1, MalformedURLException arg2) {
			}
			
			@Override
			public void loadScriptError(HtmlPage arg0, URL arg1, Exception arg2) {
			}
		});
		
		WebReader.getClient().setHTMLParserListener(new HTMLParserListener() {
			
			@Override
			public void error(String message, URL url, String html, int line, int column, String key) {
			
			}
			
			@Override
			public void warning(String message, URL url, String html, int line, int column, String key) {
			
			}
		});
	}
	
	private final String url;
	private String cachedHTML;
	
	public WebReader(String url) {
		this.url = url;
	}
	
	public static int getDefaultTimeOut() {
		return WebReader.DEFAULT_TIME_OUT;
	}
	
	public String getSyncHTML() throws IOException {
		return this.getSyncHTML("", DEFAULT_TIME_OUT);
	}
	
	public String getSyncHTML(String subPage, int millis) throws IOException {
		HtmlPage page = WebReader.getClient().getPage(this.url + "/" + subPage);
		
		WebReader.getClient().waitForBackgroundJavaScript(millis);
		
		this.setCachedHTML(page.asXml());
		
		return this.getCachedHTML();
	}
	
	public static WebClient getClient() {
		return WebReader.CLIENT;
	}
	
	public String getCachedHTML() {
		return this.cachedHTML;
	}
	
	public WebReader setCachedHTML(String cachedHTML) {
		this.cachedHTML = cachedHTML;
		return this;
	}
	
	public String getSyncHTML(String subPage) throws IOException {
		return this.getSyncHTML(subPage, DEFAULT_TIME_OUT);
	}
	
	public String getSyncHTML(int millis) throws IOException {
		return this.getSyncHTML("", millis);
	}
	
	public WebReader getHTML(WebConsumer<String> html) {
		return this.getHTML("", DEFAULT_TIME_OUT, html);
	}
	
	public WebReader getHTML(String subPage, int millis, WebConsumer<String> html) {
		new Thread(() -> {
			try {
				html.success(this.getSyncHTML(subPage, millis));
			} catch(IOException e) {
				html.fail(e);
			}
		}).start();
		
		return this;
	}
	
	public WebReader getHTML(String subPage, WebConsumer<String> html) {
		return this.getHTML(subPage, DEFAULT_TIME_OUT, html);
	}
	
	public WebReader getHTML(int millis, WebConsumer<String> html) {
		return this.getHTML("", millis, html);
	}
	
	public WebReader customSyncQuery(WebConsumer<HtmlPage> htmlPage) throws IOException {
		return this.customSyncQuery("", DEFAULT_TIME_OUT, htmlPage);
	}
	
	public WebReader customSyncQuery(String subPage, int millis, WebConsumer<HtmlPage> htmlPage) throws IOException {
		HtmlPage page = WebReader.getClient().getPage(this.url + "/" + subPage);
		
		WebReader.getClient().waitForBackgroundJavaScript(millis);
		
		htmlPage.success(page);
		return this;
	}
	
	public WebReader customSyncQuery(String subPage, WebConsumer<HtmlPage> htmlPage) throws IOException {
		return this.customSyncQuery(subPage, DEFAULT_TIME_OUT, htmlPage);
	}
	
	public WebReader customSyncQuery(int millis, WebConsumer<HtmlPage> htmlPage) throws IOException {
		return this.customSyncQuery("", millis, htmlPage);
	}
	
	public WebReader customQuery(WebConsumer<HtmlPage> htmlPage) {
		return this.customQuery("", DEFAULT_TIME_OUT, true, htmlPage);
	}
	
	public WebReader customQuery(String subPage, int millis, boolean stack, WebConsumer<HtmlPage> htmlPage) {
		new Thread(() -> {
			try {
				this.customSyncQuery(subPage, millis, htmlPage);
			} catch(IOException e) {
				if(stack) e.printStackTrace();
			}
		}).start();
		return this;
	}
	
	public WebReader customQuery(boolean stack, WebConsumer<HtmlPage> htmlPage) {
		return this.customQuery("", DEFAULT_TIME_OUT, stack, htmlPage);
	}
	
	public WebReader customQuery(String subPage, boolean stack, WebConsumer<HtmlPage> htmlPage) {
		return this.customQuery(subPage, DEFAULT_TIME_OUT, stack, htmlPage);
	}
	
	public WebReader customQuery(int millis, boolean stack, WebConsumer<HtmlPage> htmlPage) {
		return this.customQuery("", millis, stack, htmlPage);
	}
	
	public WebReader customQuery(String subPage, int millis, WebConsumer<HtmlPage> htmlPage) {
		return this.customQuery(subPage, millis, true, htmlPage);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(this.getUrl(), this.getCachedHTML());
	}
	
	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		WebReader reader = (WebReader) o;
		return Objects.equals(this.getUrl(), reader.getUrl()) &&
				Objects.equals(this.getCachedHTML(), reader.getCachedHTML());
	}
	
	public String getUrl() {
		return this.url;
	}
	
	@Override
	public String toString() {
		return "WebReader{" +
				"                            url='" + this.url + '\'' +
				",                             cachedHTML='" + this.cachedHTML + '\'' +
				'}';
	}
}
