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
import java.util.logging.Level;

public class WebReader {
	
	private static final WebClient CLIENT = new WebClient(BrowserVersion.BEST_SUPPORTED);
	private static final int DEFAULT_TIME_OUT = 5000;
	
	static {
		LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");
		
		java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);
		java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF);
		
		CLIENT.getOptions().setJavaScriptEnabled(true);
		CLIENT.getOptions().setThrowExceptionOnScriptError(false);
		CLIENT.getOptions().setThrowExceptionOnFailingStatusCode(false);
		CLIENT.getOptions().setCssEnabled(false);
		
		CLIENT.setIncorrectnessListener((message, origin) -> {});
		
		CLIENT.setCssErrorHandler(new CSSErrorHandler() {
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
		
		CLIENT.setJavaScriptErrorListener(new JavaScriptErrorListener() {
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
		
		CLIENT.setHTMLParserListener(new HTMLParserListener() {
			
			@Override
			public void error(String message, URL url, String html, int line, int column, String key) {
			
			}
			
			@Override
			public void warning(String message, URL url, String html, int line, int column, String key) {
			
			}
		});
	}
	
	private String url;
	private String cachedHTML;
	
	public WebReader(String url) {
		this.url = url;
	}
	
	public String getSyncHTML() throws IOException {
		return getSyncHTML("", DEFAULT_TIME_OUT);
	}
	
	public String getSyncHTML(String subPage, int millis) throws IOException {
		HtmlPage page = CLIENT.getPage(this.url + "/" + subPage);
		
		CLIENT.waitForBackgroundJavaScript(millis);
		
		this.cachedHTML = page.asXml();
		
		return this.cachedHTML;
	}
	
	public String getSyncHTML(String subPage) throws IOException {
		return getSyncHTML(subPage, DEFAULT_TIME_OUT);
	}
	
	public String getSyncHTML(int millis) throws IOException {
		return getSyncHTML("", millis);
	}
	
	public WebReader getHTML(WebConsumer<String> html) {
		return getHTML("", DEFAULT_TIME_OUT, html);
	}
	
	public WebReader getHTML(String subPage, int millis, WebConsumer<String> html) {
		new Thread(() -> {
			try {
				html.success(getSyncHTML(subPage, millis));
			} catch(IOException e) {
				html.fail(e);
			}
		}).start();
		
		return this;
	}
	
	public WebReader getHTML(String subPage, WebConsumer<String> html) {
		return getHTML(subPage, DEFAULT_TIME_OUT, html);
	}
	
	public WebReader getHTML(int millis, WebConsumer<String> html) {
		return getHTML("", millis, html);
	}
	
	public WebReader customSyncQuery(WebConsumer<HtmlPage> htmlPage) throws IOException {
		return customSyncQuery("", DEFAULT_TIME_OUT, htmlPage);
	}
	
	public WebReader customSyncQuery(String subPage, int millis, WebConsumer<HtmlPage> htmlPage) throws IOException {
		HtmlPage page = CLIENT.getPage(this.url + "/" + subPage);
		
		CLIENT.waitForBackgroundJavaScript(millis);
		
		htmlPage.success(page);
		return this;
	}
	
	public WebReader customSyncQuery(String subPage, WebConsumer<HtmlPage> htmlPage) throws IOException {
		return customSyncQuery(subPage, DEFAULT_TIME_OUT, htmlPage);
	}
	
	public WebReader customSyncQuery(int millis, WebConsumer<HtmlPage> htmlPage) throws IOException {
		return customSyncQuery("", millis, htmlPage);
	}
	
	public WebReader customQuery(WebConsumer<HtmlPage> htmlPage) {
		return customQuery("", DEFAULT_TIME_OUT, true, htmlPage);
	}
	
	public WebReader customQuery(String subPage, int millis, boolean stack, WebConsumer<HtmlPage> htmlPage) {
		new Thread(() -> {
			try {
				customSyncQuery(subPage, millis, htmlPage);
			} catch(IOException e) {
				if(stack) e.printStackTrace();
			}
		}).start();
		return this;
	}
	
	public WebReader customQuery(boolean stack, WebConsumer<HtmlPage> htmlPage) {
		return customQuery("", DEFAULT_TIME_OUT, stack, htmlPage);
	}
	
	public WebReader customQuery(String subPage, boolean stack, WebConsumer<HtmlPage> htmlPage) {
		return customQuery(subPage, DEFAULT_TIME_OUT, stack, htmlPage);
	}
	
	public WebReader customQuery(int millis, boolean stack, WebConsumer<HtmlPage> htmlPage) {
		return customQuery("", millis, stack, htmlPage);
	}
	
	public WebReader customQuery(String subPage, int millis, WebConsumer<HtmlPage> htmlPage) {
		return customQuery(subPage, millis, true, htmlPage);
	}
}
