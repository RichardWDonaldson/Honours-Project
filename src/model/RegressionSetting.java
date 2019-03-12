package model;

public class RegressionSetting {
// -S 0: M5, 1: No attribute 2: Greedy Method     Attribute Selection
	//-R Ridge 1.0E-8
	//Decimal places -num-decimal-places 4
	
	
	double ridge;
	int decimalPlaces;
	int attributeSelectionMethod;
	public RegressionSetting(double ridge, int decimalPlaces, int attributeSelectionMethod) {
		super();
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
