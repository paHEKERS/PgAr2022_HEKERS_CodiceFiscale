package it.progarnaldo.codicifiscali;

import java.util.Arrays;


/**
 * Class with methods for calculating the fiscal code of a given person
 * or to check if a given code is valid.
 */
public class FiscalCodeUtils {
	
	/**
	 * Calculate the fiscal code for the given <code>Persona</code>.
	 * 
	 * @param p the <code>Persona</code> from which to calculate the fiscal code
	 * @param comuniMap a <code>StringMap<code> used to get the "Belfiore code"
	 * @return the fiscal code for the given <code>Persona</code>.
	 */
	public static String calculateCodeFromPerson(Persona p, StringMap comuniMap) {
		String str = modificaNC(p.cognome, false)
					+ modificaNC(p.nome, true)
					+ getData(p)
					+ comuniMap.get(p.comune_nascita);
		str = str.toUpperCase();
		return str + calculateControlCharacter(str);
	}
	
	
	private static String modificaNC(String stringa, boolean cod) {
		String nuovastringa = "";
		stringa = stringa.replaceAll(" ", ""); // Rimuovo eventuali spazi
		stringa = stringa.toLowerCase();
		
		String consonanti = getConsonanti(stringa); // Ottengo tutte le consonanti e tutte le vocali della stringa
		String vocali = getVocali(stringa);
		
		// Controlla i possibili casi
		if (consonanti.length() == 3) { // La stringa contiene solo 3 consonanti, quindi ho gia' la modifica
			nuovastringa = consonanti;
		}
		// Le consonanti non sono sufficienti, e la stinga e' più lunga o
		// uguale a 3 caratteri [aggiungo le vocali mancanti]
		else if ((consonanti.length() < 3) && (stringa.length() >= 3)) {
			nuovastringa = consonanti;
			nuovastringa = aggiungiVocali(nuovastringa, vocali);
		}
		// Le consonanti non sono sufficienti, e la stringa
		// contiene meno di 3 caratteri [aggiungo consonanti e vocali, e le x]
		else if ((consonanti.length() < 3) && (stringa.length() < 3)) {
			nuovastringa = consonanti;
			nuovastringa += vocali;
			nuovastringa = aggiungiX(nuovastringa);
		}
		// Le consonanti sono in eccesso, prendo solo le
		// prime 3 nel caso del cognome; nel caso del nome la 0, 2, 3
		else if (consonanti.length() > 3) {
			// true indica il nome e false il cognome
			if (!cod)
				nuovastringa = consonanti.substring(0, 3);
			else
				nuovastringa = consonanti.charAt(0) + "" + consonanti.charAt(2) + "" + consonanti.charAt(3);
		}
		
		return nuovastringa;
	}
	private static String getVocali(String stringa) {
		return stringa.replaceAll("[^aeiou]", "");
	}
	private static String getConsonanti(String stringa) {
		return stringa.replaceAll("[aeiou]", "");
	}
	private static String aggiungiVocali(String stringa, String vocali) {
		int index = 0;
		while (stringa.length() < 3) {
			stringa += vocali.charAt(index);
			index++;
		}
		return stringa;
	}
	private static String aggiungiX(String stringa) {
		while (stringa.length() < 3) {
			stringa += "x";
		}
		return stringa;
	}
	
	private static String getData(Persona p) {
		String[] ss = p.data_nascita.split("");
		// 0123456789
		// yyyy_mm_dd
		return getAnno(p, ss)
			+ getMese(p, ss)
			+ getGiorno(p,ss);
	}
	private static String getAnno(Persona p, String[] ss) {
		return ss[2]+ss[3];
	}
	private static char getMese(Persona p, String[] ss) {
		String mesiValidi = "ABCDEHLMPRST";
		return mesiValidi.charAt(Integer.parseInt(ss[5]+ss[6])-1);
	}
	private static String getGiorno(Persona p, String[] ss) {
		int giorno = Integer.parseInt(ss[8]+ss[9])
					+ (p.sesso.equalsIgnoreCase("F")? 40 : 0);
		return String.format("%02d", giorno);
	}
	
	
	/**
	 * Calculate the 16th of the fiscal code, namely the control character,
	 * from the 15 before it.
	 * 
	 * @param str a string with the first 15 character of the fiscal code
	 * @return the control character
	 */
	public static char calculateControlCharacter(String str) {
		final char[] elencoPari = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
		final int[] elencoDispari = { 1, 0, 5, 7, 9,13,15,17,19,21, 1, 0, 5, 7, 9,13,15,17,
		                             19,21, 2, 4,18,20,11, 3, 6, 8,12,14,16,10,22,25,24,23};
		int pari = 0, dispari = 0;
		
		for (int i = 0; i < str.length(); i++) {
			char ch = str.charAt(i); // i-esimo carattere della stringa
			
			// Il primo carattere e' il numero 1 non 0
			if ((i + 1) % 2 == 0) {
				int index = Arrays.binarySearch(elencoPari, ch);
				pari += (index >= 10) ? index - 10 : index;
			} else {
				int index = Arrays.binarySearch(elencoPari, ch);
				dispari += elencoDispari[index];
			}
		}
		
		int controllo = (pari + dispari) % 26;
		controllo += 10;
		
		return elencoPari[controllo];
	}
	
	
	/**
	 * Return <code>true</code> if the specified fiscal code is valid.
	 * 
	 * @param code the fiscal code whose validation is to be checked
	 * @return <code>true</code> if the specified fiscal code is valid
	 */
	public static boolean isValid(String code) {
		return matchesRegex(code) && isCarattereControlloCorretto(code);
	}
	private static boolean matchesRegex(String code) {
		String regex = "^([A-Z][AEIOU][AEIOUX]"	// lettera	 -vocale	-vocale|X
		+ "|[AEIOU]X{2}" 						// vocale	 -X			-X
		+ "|[B-DF-HJ-NP-TV-Z]{2}[A-Z]){2}"		// consonante-consonante-lettera
		+ "\\d{2}" // anno
		+ "([ABCDEHLMPRST]([04][1-9]|[15]\\d|[26][0-8])" // any:28
		+ "|[ACDEHLMPRST][26]9|[DHPS][37]0|[ACELMRT][37][01])" // 29|30|31
		+ "([A-MZ][1-9]\\d{2}|[A-M]0(0[1-9]|[1-9]\\d))" // comune
		+ "[A-Z]$"; // carattere controllo
		return code.matches(regex);
	}
	private static boolean isCarattereControlloCorretto(String code) {
		int last = code.length()-1;
		String fifteenBefore = code.substring(0, last);
		return code.charAt(last) == calculateControlCharacter(fifteenBefore);
	}
	
}
