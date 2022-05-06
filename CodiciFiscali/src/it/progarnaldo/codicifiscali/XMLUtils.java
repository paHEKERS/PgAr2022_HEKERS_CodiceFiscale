package it.progarnaldo.codicifiscali;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import javax.xml.stream.*;

/**
 * Utility class for creating XML-readers and XML-writers.
 * 
 * @see XMLReader
 * @see XMLWriter
 */
public class XMLUtils {
	
	/**
	 * Create a new XMLStreamReader with specified file name.
	 * 
	 * @param fileName the name of the file to be written
	 * @return a new XMLStreamReader with specified file name
	 */
	public static XMLStreamReader getXMLReaderFromPath(String fileName) {
		XMLStreamReader xmlr = null;
		
		try {
			XMLInputFactory xmlif = XMLInputFactory.newInstance();
			xmlr = xmlif.createXMLStreamReader(fileName, new FileInputStream(fileName));
		} catch (Exception e) {
			System.out.println("Errore nell'inizializzazione del reader:");
			System.out.println(e.getMessage());
		}
		
		return xmlr;
	}
	
	
	/**
	 * Create a new XMLReader with specified file name.
	 * 
	 * @param fileName the name of the file to be red
	 * @return a new XMLReader with specified file name
	 */
	public static XMLReader createXMLReader(String fileName) {
		return new XMLReader( getXMLReaderFromPath(fileName) );
	}
	
	/**
	 * Create a new XMLWriter with specified file name.
	 * 
	 * @param fileName the name of the file to be written
	 * @return a new XMLWriter with specified file name
	 */
	public static XMLWriter createWriter(String fileName) {
		return new XMLWriter(fileName);
	}
	
	
	/**
	 * Iterate each element from the XML build by specified constructor.
	 * 
	 * @param <T> the type that each XML element represent
	 * @param fileName the name of the XML file to be read
	 * @param constructor Function to convert a <code>StringMap</code> into an object of type <code>T</code>
	 * @param action action to be performed for each pair of (index,element)
	 * @see XMLReader
	 * @see XMLReader#forEach
	 */
	public static <T> void iterateFrom(String fileName, Function<StringMap,T> constructor, BiConsumer<Integer,T> action) {
		XMLReader iter = createXMLReader(fileName);
		iter.forEach(constructor, action);
	}
	/**
	 * Iterate each element from the XML build by specified constructor.
	 * 
	 * @param <T> the type that each XML element represent
	 * @param fileName the name of the XML file to be read
	 * @param constructor Function to convert a <code>StringMap</code> into an object of type <code>T</code>
	 * @param action action to be performed for each element
	 * @see XMLReader
	 * @see XMLReader#forEach
	 */
	public static <T> void iterateFrom(String fileName, Function<StringMap,T> constructor, Consumer<T> action) {
		iterateFrom(fileName, constructor, (i,t)->action.accept(t));
	}
	
	
	/**
	 * Iterate each element from the XML build by specified constructor.
	 * 
	 * @implSpec
	 * It assume the XML is in the form:
	 * <code><pre>{@literal
	 * <listName>
	 *   <elementName>elementA</elementName>
	 *   <elementName>elementB</elementName>
	 *   ...
	 *   <elementName>elementZ</elementName>
	 * </listName>
	 * }</pre></code>
	 * 
	 * @param fileName the name of the XML file to be read
	 * @param action action to be performed for each pair of (index,element)
	 */
	public static void iterateFrom(String fileName, BiConsumer<Integer,String> action) {
		iterateFrom(fileName, map -> map.get(XMLReader.SINGLE_VALUE_KEY), action);
	}
	/**
	 * Iterate each entry from the XML build by specified constructor.
	 * 
	 * @param fileName the name of the XML file to be read
	 * @param action action to be performed for each entry
	 * @see #iterateFrom(String, BiConsumer)
	 */
	public static void iterateFrom(String fileName, Consumer<String> action) {
		iterateFrom(fileName, (i,t) -> action.accept(t));
	}
	
	
	/**
	 * Read the specified XML list and return an <code>ArrayList</code> of it.
	 * 
	 * @param <T> the type that each XML element represents
	 * @param fileName the name of the XML file to be read
	 * @param constructor Function to convert a <code>StringMap</code> into an object of type <code>T</code>
	 * @return the read <code>ArrayList</code>
	 * @see XMLReader#forEach
	 */
	public static <T> ArrayList<T> readList(String fileName, Function<StringMap,T> constructor) {
		Objects.requireNonNull(constructor);
		ArrayList<T> list = new ArrayList<>();
		iterateFrom(fileName, constructor, t -> {
			list.add(t);
		});
		return list;
	}
	/**
	 * Read the specified XML list and return an <code>{@literal ArrayList<String>}</code> of it.
	 * 
	 * @param fileName the name of the XML file to be read
	 * @return the read <code>{@literal ArrayList<String>}</code>
	 * @see XMLReader#forEach
	 * @see #iterateFrom(String, BiConsumer)
	 */
	public static ArrayList<String> readList(String fileName) {
		ArrayList<String> list = new ArrayList<>();
		iterateFrom(fileName, t -> {
			list.add(t);
		});
		return list;
	}
	
	/**
	 * Read the specified XML map and return a <code>StringMap</code> of it
	 * 
	 * @implSpec
	 * It assume the XML is in the form:
	 * <code><pre>{@literal
	 * <mapName>
	 *   <elementA>
	 *     <keyName>keyA</keyName>
	 *     <valueName>valueA</valueName>
	 *   </elementA>
	 *   <elementB>
	 *     <keyName>keyB</keyName>
	 *     <valueName>valueB</valueName>
	 *   </elementB>
	 *   ...
	 *   <elementZ>
	 *     <keyName>keyZ</keyName>
	 *     <valueName>valueZ</valueName>
	 *   </elementZ>
	 * </mapName>
	 * }</pre></code>
	 * 
	 * @param fileName the name of the file to be read
	 * @param keyName the name of the key tag
	 * @param valueName the name of the value tag
	 * @return the read <code>StringMap</code>
	 */
	public static StringMap readMap(String fileName, String keyName, String valueName) {
		StringMap map = new StringMap();
		XMLReader iter = createXMLReader(fileName);
		iter.forEach((i,m) -> {
			map.put(m.get(keyName), m.get(valueName));
		});
		return map;
	}
	
}
