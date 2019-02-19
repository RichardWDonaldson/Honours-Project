package model;

import java.io.File;

public class AdvancedSettings {
	
	String structure;
	int iterations;
	int learningRate;
	int momentum;
	int seed;
	int validationThreshold;
	int validationSize;
	
	
	public AdvancedSettings(String structure, int iterations, int learningRate, int momentum, int seed,
			int validationThreshold, int validationSize) {
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


	public int getIterations() {
		return iterations;
	}


	public void setIterations(int iterations) {
		this.iterations = iterations;
	}


	public int getLearningRate() {
		return learningRate;
	}


	public void setLearningRate(int learningRate) {
		this.learningRate = learningRate;
	}


	public int getMomentum() {
		return momentum;
	}


	public void setMomentum(int momentum) {
		this.momentum = momentum;
	}


	public int getSeed() {
		return seed;
	}


	public void setSeed(int seed) {
		this.seed = seed;
	}


	public int getValidationThreshold() {
		return validationThreshold;
	}


	public void setValidationThreshold(int validationThreshold) {
		this.validationThreshold = validationThreshold;
	}


	public int getValidationSize() {
		return validationSize;
	}


	public void setValidationSize(int validationSize) {
		this.validationSize = validationSize;
	}


	@Override
	public String toString() {
		return "AdvancedSettings [structure=" + structure + ", iterations=" + iterations + ", learningRate="
				+ learningRate + ", momentum=" + momentum + ", seed=" + seed + ", validationThreshold="
				+ validationThreshold + ", validationSize=" + validationSize + "]";
	}
	
	
	
	
	
	
	
	
	
	
}
