package it.progarnaldo.codicifiscali;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.function.BiConsumer;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

/**
 * Class used to write an XML document.<br>
 * Will also auto-format.
 */
public class XMLWriter {
	
	private static final String LIST_SIZE_ATTRIBUTE_NAME = "numero";
	private int indentLevel = 0;
	private final XMLStreamWriter xmlw;
	
	
	public XMLWriter(String fileName) {
		XMLStreamWriter xw = null;
		
		try {
			xw = XMLOutputFactory.newInstance()
				.createXMLStreamWriter(new FileOutputStream(fileName), "utf-8");
			xw.writeStartDocument("utf-8", "1.0");
		} catch(Exception e) {
			System.out.println("Errore nella scrittura");
		}
		
		xmlw = xw;
		if (xmlw!=null) newLine();
		
	}
	
	
	public void write(Runnable runnable) {
		runnable.run();
		flushAndClose();
	}
	
	public void flushAndClose() {
		try {
			xmlw.writeEndDocument();
			xmlw.flush();
			xmlw.close();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
	}
	
	
	private void indent() throws XMLStreamException {
		xmlw.writeCharacters("  ".repeat(indentLevel));
	}
	
	public void newLine() {
		try {
			xmlw.writeCharacters("\n");
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
	}
	
	private void writeStartElement(String localName) throws XMLStreamException {
		indent();
		xmlw.writeStartElement(localName);
	}
	private void writeEndElement() throws XMLStreamException {
		indent();
		xmlw.writeEndElement();
	}
	
	public void comment(String comment) {
		try {
			indent();
			xmlw.writeComment(comment);
			newLine();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
	}
	
	
	public void tag(String name, String text) {
		tag(name, "", "", text);
	}
	public void tag(String name, Runnable inner) {
		tag(name, "", "", inner);
	}
	
	public void tag(String name, String attName, String attValue, String text) {
		try {
			writeStartElement(name);
			
			if (attName.length()>0)
				xmlw.writeAttribute(attName, attValue);
			
			xmlw.writeCharacters(text);
			
			xmlw.writeEndElement();
			newLine();
			
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
	}
	public void tag(String name, String attName, int attValue, String text) {
		tag(name, attName, attValue+"", text);
	}
	public void tag(String name, String attName, String attValue, Runnable inner) {
		try {
			writeStartElement(name);
			
			if (attName.length()>0)
				xmlw.writeAttribute(attName, attValue);
			newLine();
			
			indentLevel++;
			inner.run();
			indentLevel--;
			
			writeEndElement();
			newLine();
			
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
	}
	public void tag(String name, String attName, int attValue, Runnable inner) {
		tag(name, attName, attValue+"", inner);
	}
	
	public <T> void tagList(String name, ArrayList<T> list, BiConsumer<Integer,T> inner) {
		tag(name, LIST_SIZE_ATTRIBUTE_NAME, list.size()+"", () -> {
			int i=0;
			for (T t : list) inner.accept(i++,t);
		});
	}
	
}
