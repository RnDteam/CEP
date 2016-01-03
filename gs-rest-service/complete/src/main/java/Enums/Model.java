package Enums;
import Enums.ManuFactur;

public enum Model {

	MSX125("HONDA"),
	CBR600RR("HONDA"),
	X5("BMW"),
	X6("BMW"),
	R3("YAMAHA");

	private final ManuFactur manufactur;

	/**
	 * @param text
	 */
	private Model(final String manufactur) {
		this.manufactur = ManuFactur.valueOf(manufactur);
	}

	/* (non-Javadoc)
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
		return manufactur.name();
	}
}

