package it.unibs.fp.puntirette;

public class Punto {

	private double[] coordinate;

	public Punto(double x, double y) {
		coordinate = new double[2];
		coordinate[0] = x;
		coordinate[1] = y;
	}

	public double getX() {
		return coordinate[0];
	}

	public double getY() {
		return coordinate[1];
	}

	public void setX(double x) {
		coordinate[0] = x;
	}

	public void setY(double y) {
		coordinate[1] = y;
	}

	public String toString() {
		return "x=" + coordinate[0] + "; y=" + coordinate[1];
	}

	public boolean ugualeA(Punto altro) {
		return (distanzaDa(altro) == 0);
	}

	public double distanzaDa(Punto altro) {
		return Math.sqrt(Math.pow((coordinate[0] - altro.coordinate[0]), 2)
				+ Math.pow((coordinate[1] - altro.coordinate[1]), 2));
	}

}
