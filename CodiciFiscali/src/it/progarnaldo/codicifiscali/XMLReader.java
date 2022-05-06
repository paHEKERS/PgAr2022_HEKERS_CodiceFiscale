package it.progarnaldo.codicifiscali;

import java.util.Objects;
import java.util.Stack;
import java.util.function.BiConsumer;
import java.util.function.Function;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 * Class used to read an XML list like it was an <code>Iterable</code>.
 * 
 * @implSpec
 * It assume the XML has one of the following form:
 * <ol>
 * <li>List of objects:
 * <code><pre>{@literal
 * <listName listSize=n>
 *   <elementA>
 *     <property1>...</property1>
 *     <property2>...</property2>
 *     ...
 *     <propertyN>...</propertyN>
 *   </elementA>
 *   <elementB>
 *     <property1>...</property1>
 *     <property2>...</property2>
 *     ...
 *     <propertyN>...</propertyN>
 *   </elementB>
 *   ...
 *   <elementZ>
 *     <property1>...</property1>
 *     <property2>...</property2>
 *     ...
 *     <propertyN>...</propertyN>
 *   </elementZ>
 * </listName>
 * }</pre></code></li>
 * 
 * <li>List of values:
 * <code><pre>{@literal
 * <listName listSize=n>
 *   <elementA>...</elementA>
 *   <elementB>...</elementB>
 *   ...
 *   <elementZ>...</elementZ>
 * </listName>
 * }</pre></code></li>
 * </ol>
 * 
 */
public class XMLReader {
	
	/**
	 * When reading a list of single values, it can be retrieved
	 * from the <code>StringMap</code> with this key.
	 */
	public static final String SINGLE_VALUE_KEY = "SingleValue";
	private int size = -1;
	private final XMLStreamReader xmlr;
	
	
	/**
	 * Constructor for an XMLIterableReader.
	 * 
	 * @param xmlr the <code>XMLStreamReader</code> to be read
	 */
	public XMLReader(XMLStreamReader xmlr) {
		this.xmlr = xmlr;
		try {
			xmlr.next();
			if (xmlr.getAttributeCount()==1)
				size = Integer.parseInt( xmlr.getAttributeValue(0) );
			
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Return the size of the iterable.<br>
	 * <p>
	 * It may return -1 if and only if <code>size()</code> is called before
	 * any of the <code>forEach</code> <strong>and</strong> the opening list tag
	 * in the XML doesn't contains only one attribute representing it's length, like
	 * <code>{@literal <listName listSize=n>}</code> .
	 * 
	 * @return the size of the iterable
	 */
	public int size() {
		return size;
	}
	
	
	/**
	 * Performs the specified action for each pair of (index,element) of the XML list.
	 * 
	 * @implSpec
	 * The conversion from XML to an <code>Iterable</code>-like
	 * of type <code>StringMap</code> is as follow:
	 * <code><pre>{@literal
	 * 
	 *<elementX id=i>                     StringMap mapX = [
	 *  <property1>value1</property1>       property1:value1,
	 *  <property2>value2</property2>  =>   property2:value2, =>  action.accept(i, mapX)    
	 *  <property3>value3</property3>       property3:value3
	 *</elementX>                         ]
	 * 
	 * 
	 *                                    StringMap mapY = [
	 *<elementY>valueY</elementY>    =>     elementY:valueY   =>  action.accept(i, mapY)
	 *                                    ]
	 * }</pre></code>
	 * 
	 * @param action the action to be performed for each pair of (index,element)
	 */
	public void forEach(BiConsumer<Integer,StringMap> action) {
		Objects.requireNonNull(action);
		int tagCount=0, index=0;
		StringMap map = new StringMap();
		String value = "";
		Stack<String> stack = new Stack<>();
		stack.add(xmlr.getLocalName());
		
		try {
			xmlr.next();
			
			while (xmlr.hasNext()) {
				switch (xmlr.getEventType()) {
				case XMLStreamConstants.START_ELEMENT:
					stack.add(xmlr.getLocalName());
					tagCount++;
					break;
					
				case XMLStreamConstants.CHARACTERS:
					String text = xmlr.getText();
					if (text.trim().length() > 0) {
						String key = stack.peek();
						map.put(key, value=text);
					}
					break;
					
				case XMLStreamConstants.END_ELEMENT:
					stack.pop();
					if (--tagCount == 0) {
						if (map.size()==1) {
							map.put(SINGLE_VALUE_KEY, value);
						}
						action.accept(index++,map);
						map.clear();
					}
					break;
					
				}
				
				xmlr.next();
			}
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
		
		size=index;
	}
	
	
	/**
	 * Convert each element of the XML list into an object of type <code>T</code>,
	 * then pass it as argument of action.
	 * 
	 * @implNote
	 * The default implementation behaves as if:
	 * <code><pre>
	 * forEach((i,map) -> {
	 *     T t = constructor.apply(map);
	 *     action.accept(i,t);
	 * });
	 * </pre></code>
	 * 
	 * @param <T> the type that each XML element represents
	 * @param constructor Function to convert a <code>StringMap</code> into an object of type <code>T</code>
	 * @param action the action to be performed for each object of type <code>T</code>
	 */
	public <T> void forEach(Function<StringMap,T> constructor, BiConsumer<Integer,T> action) {
		Objects.requireNonNull(constructor);
		forEach((i,map) -> {
			action.accept(i, constructor.apply(map));
		});
	}
	
}
