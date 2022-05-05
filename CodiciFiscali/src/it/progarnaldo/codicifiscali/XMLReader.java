package it.progarnaldo.codicifiscali;

public class XMLReader {
	
	
	
	/*
	 * XMLInputFactory xmlif = null;
		XMLReader xmlr = null;
		try {
		 xmlif = XMLInputFactory.newInstance();
		 xmlr = xmlif.createXMLStreamReader(filename, new FileInputStream(filename));
		} catch (Exception e) {
		 System.out.println("Errore nell'inizializzazione del reader:");
		 System.out.println(e.getMessage());
		}
		
		while (xmlr.hasNext()) { // continua a leggere finché ha eventi a disposizione
			 switch (xmlr.getEventType()) { // switch sul tipo di evento
			 case XMLStreamConstants.START_DOCUMENT: // inizio del documento: stampa che inizia il documento
			 System.out.println("Start Read Doc " + filename); break;
			 case XMLStreamConstants.START_ELEMENT: // inizio di un elemento: stampa il nome del tag e i suoi attributi
			 System.out.println("Tag " + xmlr.getLocalName());
			 for (int i = 0; i < xmlr.getAttributeCount(); i++)
			 System.out.printf(" => attributo %s->%s%n", xmlr.getAttributeLocalName(i), xmlr.getAttributeValue(i));
			 break;
			 case XMLStreamConstants.END_ELEMENT: // fine di un elemento: stampa il nome del tag chiuso
			 System.out.println("END-Tag " + xmlr.getLocalName()); break;
			 case XMLStreamConstants.COMMENT:
			 System.out.println("// commento " + xmlr.getText()); break; // commento: ne stampa il contenuto
			 case XMLStreamConstants.CHARACTERS: // content all’interno di un elemento: stampa il testo
			 if (xmlr.getText().trim().length() > 0) // controlla se il testo non contiene solo spazi
			 System.out.println("-> " + xmlr.getText());
			 break;
			 }
			 xmlr.next();
	 */
	}
