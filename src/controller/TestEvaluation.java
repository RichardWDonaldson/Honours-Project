package controller;

import java.io.File;
import java.util.ArrayList;

import model.AdvancedSettings;
import model.Model;
import model.Result;

public class TestEvaluation {

	//Create an arrayList for each factor, run the rest and then use each arraylist to output the results
	//this should all be automated so you can just hit run and leave it going for however long it takes
	
	Model model = new Model();
	
	ArrayList<AdvancedSettings> settings = model.populateEvaluation();
	
	
	public void mlpEvaluation(File arffFile, int classIndex, int iterations) {
		
		
		
	for(AdvancedSettings setting : settings) {
		
		
		MLP mlp = new MLP();
		
		mlp.advancedBuild(setting.getIterations(), 
				setting.getLearningRate(), 
				setting.getMomentum(), 
				setting.getSeed(), 
				setting.getStructure(), 
				setting.getValidationThreshold(), 
				setting.getValidationSize(), 
				arffFile, classIndex, iterations);
		
		
		
		
	}
	
	}
	public void regressionEvaluation(File arffFile, int classIndex, int iterations) {
		
	}
		
		
		
		
		
		
	}
	
	
	
	
	
	
	
	

