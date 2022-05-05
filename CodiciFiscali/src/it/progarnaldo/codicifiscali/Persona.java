package it.progarnaldo.codicifiscali;

public class Persona {
	
	private static final String TO_STRING_MESSAGE = "Persona [%n nome=%s,%n cognome=%s,%n sesso=%s,%n data_nascita=%s,%n comune_nascita=%s%n]";
	
	public final String nome, cognome, sesso, data_nascita, comune_nascita;
	
	
	public Persona(StringMap map) {
		nome = map.get("nome");
		cognome = map.get("cognome");
		sesso = map.get("sesso");
		data_nascita = map.get("data_nascita");
		comune_nascita = map.get("comune_nascita");
	}
	
	
	@Override
	public String toString() {
		return String.format(TO_STRING_MESSAGE, nome, cognome, sesso, data_nascita, comune_nascita);
	}
	
}
