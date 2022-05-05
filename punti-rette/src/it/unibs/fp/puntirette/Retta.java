
package it.unibs.fp.puntirette;

public class Retta {

	private boolean verticale;
	private double coeffAng;
	private double offset;

	public Retta(Punto p1, Punto p2) {
		verticale = (p1.getX() == p2.getX());

		if (verticale)
			offset = p1.getX();
		else {
			coeffAng = (p2.getY() - p1.getY()) / (p2.getX() - p1.getX());
			offset = p2.getY() - coeffAng * p2.getX();
		}
	}

	public String toString() {
		StringBuffer res = new StringBuffer();
		res.append("L'equazione e': ");
		if (verticale)
			res.append(String.format("x = %.1f", offset));
		else {
			if (coeffAng == 0) {
				res.append(String.format("y = %.1f", offset));
			} else {
				if (offset == 0) {
					res.append(String.format("y = %.1f * x", coeffAng));
				} else {
					String sign = (offset > 0 ? "+" : "-");
					res.append(String.format("y = %.1f * x %s %.1f", coeffAng, sign, Math.abs(offset)));
				}
			}
		}
		return res.toString();
	}

	public boolean appartiene(Punto p) {
		if (!verticale)
			return (p.getY() == (coeffAng * p.getX() + offset));
		else
			return (offset == p.getX());
	}

}
