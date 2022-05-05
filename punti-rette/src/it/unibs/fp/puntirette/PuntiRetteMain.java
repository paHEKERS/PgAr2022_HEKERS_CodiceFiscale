package it.unibs.fp.puntirette;

import it.unibs.fp.mylib.InputDati;

/*
 Questa classe riceve in ingresso le coordinate di due punti e 
 ne calcola la distanza. 
 Quindi se i punti sono distinti:
	- visualizza l'equazione della retta congiungente
	- richiede le coordinate di un terzo punto e verifica se
 	  appartiene alla stessa retta
 */

public class PuntiRetteMain {

	public static Punto creaPunto(String descrizionePunto) {
		double x = InputDati.leggiDouble(descrizionePunto + ", inserisci la coordinata x: ");
		double y = InputDati.leggiDouble(descrizionePunto + ", inserisci la coordinata y: ");
		return new Punto(x, y);
	}

	public static void main(String args[]) {
		Punto p1 = creaPunto("P1");
		Punto p2 = creaPunto("P2");

		double distanza = p1.distanzaDa(p2);

		System.out.println(String.format("La distanza tra i due punti e': %.5f", distanza));

		if (p1.ugualeA(p2))
			System.out.println("I due punti coincidono! Impossibile calcolare la retta");
		else {
			Retta r = new Retta(p1, p2);

			System.out.println(r.toString());

			Punto p3 = creaPunto("P3");

			boolean allineato = r.appartiene(p3);

			System.out.print("Il punto" + (!allineato ? " NON " : " ") + "appartiene alla retta");
		}
	}

}
