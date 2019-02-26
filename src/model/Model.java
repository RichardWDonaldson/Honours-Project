package model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import view.ChartView;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Instances;
public class Model {

public ArrayList<Evaluation> evaluations = new ArrayList<Evaluation>();
public ArrayList<Result> results = new ArrayList<Result>();	
public ArrayList<AdvancedSettings> testSettings = new ArrayList<AdvancedSettings>();
 
double correlationCoefficient;
double meanAbsoluteError;
double rootMeanSquaredError;
double relativeAbsoluteError;
double rootRelativeSquaredError;
double instances;
 
 
 public void saveEvaluation(Instances instance, Classifier classifier) throws Exception {
	
	Evaluation eval = new Evaluation(instance); 
	System.out.println("Evaluating Model on Test Set");
	
	eval.evaluateModel(classifier, instance);
	
	//System.out.println(eval.correlationCoefficient());

	
	
	
	//float correlationCoefficient, float meanAbsoluteError, float rootMeanSquredError, float relativeAbsoluteError, float rootRelativeSquredError, int instances;
	Result result = new Result(
			eval.correlationCoefficient(), 
			eval.meanAbsoluteError(), 
			eval.rootMeanSquaredError(), 
			eval.relativeAbsoluteError(), 
			eval.rootRelativeSquaredError(), 
			eval.numInstances());
	
	
		
	
	results.add(result);
		
	
			
	eval.evaluateModel(classifier, instance);
	
//	System.out.println(Double.valueOf(eval.correlationCoefficient()));
	System.out.println(eval.toSummaryString("\nResults\n======\n", false));
	
	//outputResults();
	
	
 }
 
public void outputResults() {

	ChartView window = new ChartView("Title", "other title", "Week", "Value", results);
	 window.initialize(results, window);
	
	
	int count = results.size();
	//System.out.println("Count: " + count);
	
	
	
	
for(Result result : results) {
//	System.out.println("correlation: ");

	
	correlationCoefficient += result.getCorrelationCoefficient();
	//System.out.println("112 " + result.getCorrelationCoefficient());
	meanAbsoluteError += result.getMeanAbsoluteError();
	rootMeanSquaredError += result.getRootMeanSquredError();
	relativeAbsoluteError += result.getRelativeAbsoluteError();
	rootRelativeSquaredError += result.getRootRelativeSquredError();
	instances += result.getInstances();
	
	
	
}
correlationCoefficient /= count;
meanAbsoluteError  /= count;
rootMeanSquaredError  /= count;
relativeAbsoluteError  /= count;
rootRelativeSquaredError  /= count;
instances /= count;


round(correlationCoefficient, 2);
round(meanAbsoluteError, 2);
round(rootMeanSquaredError, 2);
round(relativeAbsoluteError, 2);
round(rootRelativeSquaredError, 2);



Result meanResult = new Result(correlationCoefficient, meanAbsoluteError, rootMeanSquaredError, relativeAbsoluteError, rootRelativeSquaredError, instances);

//TODO standard deviation and other statistics stuff

System.out.println("Mean Results");	
System.out.println(meanResult.toString());




	
	
}

public static double round(double value, int places) {
    if (places < 0) throw new IllegalArgumentException();

    BigDecimal bd = new BigDecimal(value);
    bd = bd.setScale(places, RoundingMode.HALF_UP);
    return bd.doubleValue();
}

public ArrayList<AdvancedSettings> populateEvaluation() {
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
	//String structure, int iterations, int learningRate, int momentum, int seed  validationThreshold, int validationSize
	
	
		for(int j = 0; j <= 6; j++) {
			
			AdvancedSettings structureTest = new AdvancedSettings(structure[j], defaultTrainingIterations, defaultLearningRate, defaultMomentum, defaultSeed, defaultValidationThreshold, defaultValidationSetSize );
			AdvancedSettings trainingIterationsTest = new AdvancedSettings(defaultStructure, trainingIterations[j], defaultLearningRate, defaultMomentum, defaultSeed, defaultValidationThreshold, defaultValidationSetSize );
			AdvancedSettings learningRateTest = new AdvancedSettings(defaultStructure, defaultTrainingIterations, learningRate[j], defaultMomentum, defaultSeed, defaultValidationThreshold, defaultValidationSetSize);
			AdvancedSettings momentumTest = new AdvancedSettings(defaultStructure, defaultTrainingIterations, defaultLearningRate, momentum[j], defaultSeed, defaultValidationThreshold, defaultValidationSetSize );
			AdvancedSettings validationThresholdTest = new AdvancedSettings(defaultStructure, defaultTrainingIterations, defaultLearningRate, defaultMomentum, defaultSeed, validationThreshold[j], defaultValidationSetSize );
			AdvancedSettings validationSizeTest = new AdvancedSettings(defaultStructure, defaultTrainingIterations, defaultLearningRate, defaultMomentum, defaultSeed, defaultValidationThreshold, validationSetSize[j] );
		
			testSettings.add(structureTest);
			testSettings.add(trainingIterationsTest);
			testSettings.add(learningRateTest);
			testSettings.add(momentumTest);
			testSettings.add(validationThresholdTest);
			testSettings.add(validationSizeTest);
			

		}


	return testSettings;
	
	

	
	
	
}

public ArrayList<AdvancedSettings> getTestSettings() {
	return testSettings;
}

public void setTestSettings(ArrayList<AdvancedSettings> testSettings) {
	this.testSettings = testSettings;
}



 

	
}
