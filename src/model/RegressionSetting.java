package model;

public class RegressionSetting extends Settings {

		
	double ridge;
	int decimalPlaces;
	int attributeSelectionMethod;
	
	
	public RegressionSetting(String algorithmType, double ridge, int decimalPlaces, int attributeSelectionMethod) {
		super(algorithmType);
		this.ridge = ridge;
		this.decimalPlaces = decimalPlaces;
		this.attributeSelectionMethod = attributeSelectionMethod;
	}


	public double getRidge() {
		return ridge;
	}


	public void setRidge(double ridge) {
		this.ridge = ridge;
	}


	public int getDecimalPlaces() {
		return decimalPlaces;
	}


	public void setDecimalPlaces(int decimalPlaces) {
		this.decimalPlaces = decimalPlaces;
	}


	public int getAttributeSelectionMethod() {
		return attributeSelectionMethod;
	}


	public void setAttributeSelectionMethod(int attributeSelectionMethod) {
		this.attributeSelectionMethod = attributeSelectionMethod;
	}


	@Override
	public String toString() {
		return "RegressionSetting [ridge=" + ridge + ", decimalPlaces=" + decimalPlaces + ", attributeSelectionMethod="
				+ attributeSelectionMethod + "]";
	}
	
	
	
	
	
	

	
	
}
