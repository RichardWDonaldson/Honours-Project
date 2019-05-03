package model;

public class Settings {
	
	String algorithmType;

	public Settings(String algorithmType) {
		super();
		this.algorithmType = algorithmType;
	}

	public String getAlgorithmType() {
		return algorithmType;
	}

	public void setAlgorithmType(String algorithmType) {
		this.algorithmType = algorithmType;
	}

	@Override
	public String toString() {
		return "Settings [algorithmType=" + algorithmType + "]";
	}
	
	
	
	
	

}
