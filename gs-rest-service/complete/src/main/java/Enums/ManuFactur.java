package Enums;

public enum ManuFactur {

	HARLEY_DAVIDSON(VehicleType.motorcycle),
	AUDI(VehicleType.jeep),
	HONDA(VehicleType.motorcycle),
	BMW(VehicleType.jeep),
	YAMAHA(VehicleType.motorcycle),
	MERCEDES(VehicleType.truck);

	public VehicleType getType() {
		return type;
	}

	private final VehicleType type;

	/**
	 * @param text
	 */
	private ManuFactur(final VehicleType type) {
		this.type = type;
	}

	/* (non-Javadoc)
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
		return type.toString();
	}
}
