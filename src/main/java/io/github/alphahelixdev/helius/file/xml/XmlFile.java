package io.github.alphahelixdev.helius.file.xml;

import io.github.alphahelixdev.helius.Helius;
import io.github.alphahelixdev.helius.xml.XMLObject;
import io.github.alphahelixdev.helius.xml.XMLParser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;

public class XmlFile extends File {
	
	private static final XMLParser PARSER = new XMLParser();
	
	public XmlFile(String pathname) {
		super(pathname);
		Helius.createFile(this);
	}
	
	public XmlFile(String parent, String child) {
		super(parent, child);
		Helius.createFile(this);
	}
	
	public XmlFile(File parent, String child) {
		super(parent, child);
		Helius.createFile(this);
	}
	
	public XmlFile(URI uri) {
		super(uri);
		Helius.createFile(this);
	}
	
	public XmlFile write(String name, Object obj) {
		return update(XmlFile.getParser()
		                     .toXMLObject(null, name, obj));
	}
	
	public XmlFile update(XMLObject obj) {
		try(BufferedWriter writer = new BufferedWriter(new FileWriter(this))) {
			writer.write(obj.asXML());
		} catch(IOException e) {
			e.printStackTrace();
		}
		return this;
	}
	
	public static XMLParser getParser() {
		return XmlFile.PARSER;
	}
	
	public XMLObject read() {
		return XmlFile.getParser().get(Helius.read(this));
	}
	
	@Override
	public String toString() {
		return "XmlFile{}";
	}
}
