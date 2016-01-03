package Entities;

import Enums.ManuFactur;
import Enums.Model;
import Enums.VehicleType;

public class Vehicle {
    private ManuFactur manufacturer;
    private Model model;
    private int year;
    
    public Vehicle(String manufacturer, String model, int year) {
        this.manufacturer = ManuFactur.valueOf(manufacturer);
        this.model = Model.valueOf(model);
        this.year = year;
    }
    
    public ManuFactur getManufacturer() {
		return manufacturer;
	}

    
	public void setManufacturer(ManuFactur manufacturer) {
		this.manufacturer = manufacturer;
	}

	public Model getModel() {
		return model;
	}

	public void setModel(Model model) {
		this.model = model;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public String toString(){
		return manufacturer + "\\" + model + "\\" + year;
	}

	public VehicleType getType() {
		return manufacturer.getType();
	}
}
