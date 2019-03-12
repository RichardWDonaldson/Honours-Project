package model;
/*
 * Default Values
 * Iterations: 500
 * Learning Rate: 0.3
 * Momentum: 0.2
 * Seed: 0
 * Structure: a 
 * Validation Threshold: 20 
 * Validation Size:  0
 * 
 * 	//-L 0.3 -M 0.2 -N 500 -V 0 -S 0 -E 20 -H 4,3

	// -L learning rate
	// -M momentum
	// -N training iterations
	// - S seed
	// -H structure of network in form "h1,h2,..." where h1=neurons in hidden layer
	// 1, h2=neurons in hidden layer 2 etc
	// -V validation set size
	// -E validation threshold (ere dictates how many times in a row the validation
	// set error can get worse before training is terminated.)

 */
import java.math.BigDecimal;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import view.ChartView;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.timeseries.WekaForecaster;
import weka.classifiers.timeseries.eval.TSEvaluation;
import weka.core.Instances;
public class Model {
//TODO Create method to flush maps and arrayLists
	
	public ArrayList<Evaluation> evaluations = new ArrayList<Evaluation>();
	public ArrayList<Result> results = new ArrayList<Result>();	
	public ArrayList<AdvancedSettings> testSettings = new ArrayList<AdvancedSettings>();


	


	public void saveEvaluation(Instances testInstances, Classifier classifier, AdvancedSettings settings) throws Exception {

		System.out.println("Evaluating Model on Test Set");

		Evaluation eval = new Evaluation(testInstances);
		eval.evaluateModel(classifier, testInstances);

		Result result = new Result(
				settings,
				eval.correlationCoefficient(), 
				eval.meanAbsoluteError(), 
				eval.rootMeanSquaredError(), 
				eval.relativeAbsoluteError(), 
				eval.rootRelativeSquaredError(), 
				eval.numInstances());
		results.add(result);		
		eval.evaluateModel(classifier, testInstances);

		System.out.println(eval.toSummaryString("\nResults\n======\n", false));

	}

	public void outputMLPResults(ChartSettings chartSettings, AdvancedSettings settings) {


		double correlationCoefficient = 0;
		double meanAbsoluteError = 0;
		double rootMeanSquaredError = 0;
		double relativeAbsoluteError = 0;
		double rootRelativeSquaredError = 0;
		double instances = 0;

		
		ChartView window = new ChartView(chartSettings.getTitle(), chartSettings.getSubTitle(), chartSettings.getxAxisTitle(), chartSettings.getyAxisTitle(), results);
		window.initialize(window);

		int count = results.size();

		for(Result result : results) {

			correlationCoefficient += result.getCorrelationCoefficient();
			meanAbsoluteError += result.getMeanAbsoluteError();
			rootMeanSquaredError += result.getRootMeanSquaredError();
			relativeAbsoluteError += result.getRelativeAbsoluteError();
			rootRelativeSquaredError += result.getRootRelativeSquaredError();
			instances += result.getInstances();
			
			
		}
		
		Result sumResult = new Result(settings, correlationCoefficient, meanAbsoluteError, rootMeanSquaredError, relativeAbsoluteError, rootRelativeSquaredError, instances);
		Result meanResult = getMeanResult(sumResult, count);
		//TODO standard deviation and other statistics stuff
		getStandardDeviation(sumResult, meanResult);
	
		
		
		System.out.println("Mean Results");	
		System.out.println(meanResult.toString());

	}
	
	public void outputRegressionResults(ChartSettings chartSettings, RegressionSetting settings) {
		ChartView window = new ChartView(chartSettings.getTitle(), chartSettings.getSubTitle(), chartSettings.getxAxisTitle(), chartSettings.getyAxisTitle(), results);
		window.initialize(window);

		
	}

	public Map<String,AdvancedSettings> populateMLPEvaluation() {
		Map<String, AdvancedSettings> map = new TreeMap<String, AdvancedSettings>();
		double defaultLearningRate = 0.3;
		double defaultMomentum = 0.2;
		double defaultTrainingIterations = 500;
		String defaultStructure = "a";
		double defaultSeed = 0;
		double defaultValidationSetSize = 0;
		double defaultValidationThreshold = 20;

		double[] learningRate = {0.1,0.2,0.3,0.4,0.5,1,2};
		double[] momentum = {0.2,0.4,0.6,0.8,1,2,10};
		double[] trainingIterations = {100,250,500,750,1000,2000,10000};
		String[] structure = {"a", "a,a","a,a,a","a,a,a,a","a,a,a,a,a", "a,a,a,a,a,a", "a,a,a,a,a,a,a"};
		double[] validationSetSize = {0,1,2,5,10,50,100};
		double[] validationThreshold = {0,10,20,40,60,80,100};

		for(int j = 0; j < learningRate.length; j++) {

			AdvancedSettings structureTest = new AdvancedSettings(structure[j], defaultTrainingIterations, defaultLearningRate, defaultMomentum, defaultSeed, defaultValidationThreshold, defaultValidationSetSize );
			AdvancedSettings trainingIterationsTest = new AdvancedSettings(defaultStructure, trainingIterations[j], defaultLearningRate, defaultMomentum, defaultSeed, defaultValidationThreshold, defaultValidationSetSize );
			AdvancedSettings learningRateTest = new AdvancedSettings(defaultStructure, defaultTrainingIterations, learningRate[j], defaultMomentum, defaultSeed, defaultValidationThreshold, defaultValidationSetSize);
			AdvancedSettings momentumTest = new AdvancedSettings(defaultStructure, defaultTrainingIterations, defaultLearningRate, momentum[j], defaultSeed, defaultValidationThreshold, defaultValidationSetSize );
			AdvancedSettings validationThresholdTest = new AdvancedSettings(defaultStructure, defaultTrainingIterations, defaultLearningRate, defaultMomentum, defaultSeed, validationThreshold[j], defaultValidationSetSize );
			AdvancedSettings validationSizeTest = new AdvancedSettings(defaultStructure, defaultTrainingIterations, defaultLearningRate, defaultMomentum, defaultSeed, defaultValidationThreshold, validationSetSize[j] );

			map.put("STRUCTURE" + j, structureTest);
			map.put("TRAINING_ITERATIONS" + j, trainingIterationsTest);
			map.put("LEARNING_RATE" + j, learningRateTest);
			map.put("MOMENTUM" + j, momentumTest);
			map.put("VALIDATION_THRESHOLD" + j, validationThresholdTest);
			map.put("VALIDATION_SIZE" + j, validationSizeTest);

		}

		return map;
	}
	
	public Map<String, RegressionSetting> populateRegressionEvaluation() {
		
		Map<String, RegressionSetting> map = new TreeMap<String, RegressionSetting>();

		/*
		 * No Attribute: 0
		 * M5: 1
		 * Greedy: 2
		 * Default Ridge: 1.0E-8
		 * Default Decimal Places: 4
		 * Default Attribute: 1
		 */
//TODO sort this so it's in order
		double[] ridge = {0.5E-8, 1.0E-7,1.0E-8,2.0E-8,3.0E-8, 1.0E-9};
		
		RegressionSetting standardNoAttribute = new RegressionSetting(1.0E-8,4,0);
		RegressionSetting standardM5 = new RegressionSetting(1.0E-8,4,1);
		RegressionSetting standardGreedy = new RegressionSetting(1.0E-8, 4, 2);
		
	//	map.put("STANDARD_NO_ATTRIBUTE", standardNoAttribute);
	//	map.put("STANDARD_M5", standardM5);
	//	map.put("STANDARD_GREEDY", standardGreedy);
		
		
		for(int i = 0; i < ridge.length; i++) {
		
			RegressionSetting ridgeTestGreedy = new RegressionSetting(ridge[i], 4, 2);
			RegressionSetting ridgeTestM5 = new RegressionSetting(ridge[i],4,1);
			RegressionSetting ridgeTestNoAttribute = new RegressionSetting(ridge[i],4,0);
					
					map.put("RIDGE_TEST_GREEDY" + i, ridgeTestGreedy);
					map.put("RIDGE_TEST_M" + i, ridgeTestM5);
					map.put("RIDGE_TEST_NO_ATTRIBUTE" + i, ridgeTestNoAttribute);
			
		}
		
		
		
		
		return map;
	}
	
	
	
	public Result getMeanResult(Result result, int count) {
		
		double tempCorrelationCoefficient = result.getCorrelationCoefficient() / count;
		double tempMeanAbsoluteError = result.getMeanAbsoluteError()  / count;
		double tempRootMeanSquaredError =  result.getRootMeanSquaredError() / count;
		double tempRelativeAbsoluteError= result.getRelativeAbsoluteError()  / count;
		double tempRootRelativeSquaredError = result.getRootRelativeSquaredError()  / count;
		
		Result meanResult = new Result(result.getSettings(), 
				tempCorrelationCoefficient, 
				tempMeanAbsoluteError, 
				tempRootMeanSquaredError, 
				tempRelativeAbsoluteError, 
				tempRootRelativeSquaredError, 
				result.getInstances()
				);
		return meanResult;

	}
	
	public void getStandardDeviation(Result result, Result meanResult) {
		
	
	}

	public ArrayList<AdvancedSettings> getTestSettings() {
		return testSettings;
	}

	public void setTestSettings(ArrayList<AdvancedSettings> testSettings) {
		this.testSettings = testSettings;
	}

}
