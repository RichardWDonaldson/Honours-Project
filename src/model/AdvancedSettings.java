package model;

public class AdvancedSettings {
	
	String structure;
	double iterations;
	double learningRate;
	double momentum;
	double seed;
	double validationThreshold;
	double validationSize;
	
	
	public AdvancedSettings(String structure, double iterations, double learningRate, double momentum, double seed,
			double validationThreshold, double validationSize) {
		super();
		this.structure = structure;
		this.iterations = iterations;
		this.learningRate = learningRate;
		this.momentum = momentum;
		this.seed = seed;
		this.validationThreshold = validationThreshold;
		this.validationSize = validationSize;
	}
	public String getStructure() {
		return structure;
	}
	public void setStructure(String structure) {
		this.structure = structure;
	}
	public double getIterations() {
		return iterations;
	}
	public void setIterations(double iterations) {
		this.iterations = iterations;
	}
	public double getLearningRate() {
		return learningRate;
	}
	public void setLearningRate(double learningRate) {
		this.learningRate = learningRate;
	}
	public double getMomentum() {
		return momentum;
	}
	public void setMomentum(double momentum) {
		this.momentum = momentum;
	}
	public double getSeed() {
		return seed;
	}
	public void setSeed(double seed) {
		this.seed = seed;
	}
	public double getValidationThreshold() {
		return validationThreshold;
	}
	public void setValidationThreshold(double validationThreshold) {
		this.validationThreshold = validationThreshold;
	}
	public double getValidationSize() {
		return validationSize;
	}
	public void setValidationSize(double validationSize) {
		this.validationSize = validationSize;
	}
	@Override
	public String toString() {
		return "AdvancedSettings [structure=" + structure + ", iterations=" + iterations + ", learningRate="
				+ learningRate + ", momentum=" + momentum + ", seed=" + seed + ", validationThreshold="
				+ validationThreshold + ", validationSize=" + validationSize + "]";
	}
	
	
	

	
	
	
	
	
	
	
	
	
}
