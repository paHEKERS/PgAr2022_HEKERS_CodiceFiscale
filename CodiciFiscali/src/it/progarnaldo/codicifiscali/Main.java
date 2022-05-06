package it.progarnaldo.codicifiscali;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

public class Main {
	
	public static final String INPUT  = "files\\inputPersone.xml";
	public static final String CODICI = "files\\codiciFiscali.xml";
	public static final String COMUNI = "files\\comuni.xml";
	public static final String OUTPUT = "files\\output.xml";
	
	
	public static void main(String[] args) {
		
		ArrayList<String> codici = XMLUtils.readList(CODICI);
		Collections.sort(codici);
		
		StringMap comuniMap = XMLUtils.readMap(COMUNI, "nome", "codice");
		
		
		
		XMLWriter xmlw = XMLUtils.createWriter(OUTPUT);
		xmlw.write(() -> {
		xmlw.tag("output", () -> {
			
			XMLReader persone = XMLUtils.createXMLReader(INPUT);
			xmlw.tag("persone", "numero", persone.size(), () -> {
			persone.forEach(Persona::new, (i,persona) -> {
				
				xmlw.tag("persona", "id", i, () -> {
					xmlw.tag("nome", persona.nome);
					xmlw.tag("cognome", persona.cognome);
					xmlw.tag("sesso", persona.sesso);
					xmlw.tag("comune_nascita", persona.comune_nascita);
					xmlw.tag("data_nascita", persona.data_nascita);
					
					String codice = FiscalCodeUtils.calculateCodeFromPerson(persona, comuniMap);
					int index = Collections.binarySearch(codici, codice);
					
					xmlw.tag("codice_fiscale", index>=0? codici.remove(index) : "ASSENTE");
				}); // </persona>
				
			}); // persone.forEach
			}); // </persone>
			
			
			ArrayList<String> validi = new ArrayList<>();
			migrate(codici, validi, FiscalCodeUtils::isValid);
			
			
			xmlw.tag("codici", () -> {
				xmlw.tagList("invalidi", codici, (i,codice) -> {
					xmlw.tag("codice", "id", i, codice);
				}); // </invalidi>
				xmlw.tagList("spaiati", validi, (i,codice) -> {
					xmlw.tag("codice", "id", i, codice);
				}); // </spaiati>
			}); // </codici>
			
			
		}); // </output>
		}); // xmlw.write
		
		
	}
	
	
	/**
	 * Migrate every element from one List that matches the given predicate to the other list. 
	 * 
	 * @param <T> the type of the elements to be migrated
	 * @param from the list from which the elements may migrate from
	 * @param to the list from which the elements may migrate to
	 * @param test the predicate used to test if an element should be migrated
	 */
	public static <T> void migrate(List<? extends T> from, List<? super T> to, Predicate<? super T> test) {
		for (int i=from.size()-1; i>=0; i--) {
			T t = from.get(i);
			if (test.test(t)) to.add(from.remove(i));
		}
		Collections.reverse(to);
	}
	
}
