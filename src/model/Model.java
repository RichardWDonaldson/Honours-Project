package model;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import view.ChartView;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Instances;

public class Model {
//TODO Create method to flush maps and arrayLists

	public ArrayList<Evaluation> evaluations = new ArrayList<Evaluation>();

	public ArrayList<MLP> testSettings = new ArrayList<MLP>();

	public void outputMLPResults(ChartSettings chartSettings, MLP settings, ArrayList<Result> results) {

		ChartView window = new ChartView(chartSettings.getTitle(), chartSettings.getSubTitle(),
				chartSettings.getxAxisTitle(), chartSettings.getyAxisTitle(), results);
		ChartView.initialize(window);

	}

	public void outputRegressionResults(ChartSettings chartSettings, Regression settings,
			ArrayList<Result> results) {
		ChartView window = new ChartView(chartSettings.getTitle(), chartSettings.getSubTitle(),
				chartSettings.getxAxisTitle(), chartSettings.getyAxisTitle(), results);
		ChartView.initialize(window);

	}

	public Map<String, MLP> populateMLPEvaluation() {
		Map<String, MLP> map = new TreeMap<String, MLP>();
		double defaultLearningRate = 0.3;
		double defaultMomentum = 0.2;
		double defaultTrainingIterations = 500;
		String defaultStructure = "a";
		double defaultSeed = 0;
		double defaultValidationSetSize = 0;
		double defaultValidationThreshold = 20;

		double[] learningRate = { 0.1, 0.2, 0.3, 0.4, 0.5, 1, 2 };
		double[] momentum = { 0.2, 0.4, 0.6, 0.8, 2, 5, 10 };
		// double[] momentum = {0.1,0.2,0.3,0.4,0.5,0.6,0.7,0.8,0.9,1};
		// double[] momentum = {0,0.05,0.91,0.92,0.93,0.94,0.95,0.96,0.97,0.98,0.99};
		double[] trainingIterations = { 100, 250, 500, 750, 1000, 2000, 10000 };
		String[] structure = { "a", "a,a", "a,a,a", "a,a,a,a", "a,a,a,a,a", "a,a,a,a,a,a", "a,a,a,a,a,a,a" };
		// String[] structure = {"10,5", "5,10", "10,5,5", "10,5,10","5,5,10",
		// "10,10,5"};
		// String[] structure = {"5", "10", "5,5", "10,10", "5,5,5", "10,10,10"};

		double[] validationSetSize = { 0, 1, 2, 5, 10, 50, 100 };
		double[] validationThreshold = { 0, 10, 20, 40, 60, 80, 100 };

		for (int j = 0; j < structure.length; j++) {
			MLP defaultSettings = new MLP(defaultStructure, defaultTrainingIterations,
					defaultLearningRate, defaultMomentum, defaultSeed, defaultValidationThreshold,
					defaultValidationSetSize);

			MLP structureTest = new MLP(structure[j], defaultTrainingIterations,
					defaultLearningRate, defaultMomentum, defaultSeed, defaultValidationThreshold,
					defaultValidationSetSize);
			MLP trainingIterationsTest = new MLP(defaultStructure, trainingIterations[j],
					defaultLearningRate, defaultMomentum, defaultSeed, defaultValidationThreshold,
					defaultValidationSetSize);
			MLP learningRateTest = new MLP(defaultStructure, defaultTrainingIterations,
					learningRate[j], defaultMomentum, defaultSeed, defaultValidationThreshold,
					defaultValidationSetSize);
			MLP momentumTest = new MLP(defaultStructure, defaultTrainingIterations,
					defaultLearningRate, momentum[j], defaultSeed, defaultValidationThreshold,
					defaultValidationSetSize);
			MLP validationThresholdTest = new MLP(defaultStructure, defaultTrainingIterations,
					defaultLearningRate, defaultMomentum, defaultSeed, validationThreshold[j],
					defaultValidationSetSize);
			MLP validationSizeTest = new MLP(defaultStructure, defaultTrainingIterations,
					defaultLearningRate, defaultMomentum, defaultSeed, defaultValidationThreshold,
					validationSetSize[j]);
			map.put("DEFAULT", defaultSettings);
			map.put("STRUCTURE" + j, structureTest);
			map.put("TRAINING_ITERATIONS" + j, trainingIterationsTest);
			map.put("LEARNING_RATE" + j, learningRateTest);
			map.put("MOMENTUM" + j, momentumTest);
			map.put("VALIDATION_THRESHOLD" + j, validationThresholdTest);
			map.put("VALIDATION_SIZE" + j, validationSizeTest);

		}

		return map;
	}

	/*
	 * No Attribute: 0 M5: 1 Greedy: 2 Default Ridge: 1.0E-8 Default Decimal Places:
	 * 4 Default Attribute: 1
	 */
	public Map<String, Regression> populateRegressionEvaluation() {

		Map<String, Regression> map = new TreeMap<String, Regression>();

		double[] ridge = { 1.0E-9, 0.5E-8, 1.0E-8, 2.0E-8, 3.0E-8, 1.0E-7 };
		Regression defaultSetting = new Regression(ridge[0], 4, 1);
		for (int i = 0; i < ridge.length; i++) {

			Regression ridgeTestGreedy = new Regression(ridge[i], 4, 2);
			Regression ridgeTestM5 = new Regression(ridge[i], 4, 1);
			Regression ridgeTestNoAttribute = new Regression(ridge[i], 4, 0);

			map.put("RIDGE_TEST_GREEDY" + i, ridgeTestGreedy);
			map.put("RIDGE_TEST_M" + i, ridgeTestM5);
			map.put("RIDGE_TEST_NO_ATTRIBUTE" + i, ridgeTestNoAttribute);

		}
		map.put("DEFAULT", defaultSetting);
		return map;
	}

}
