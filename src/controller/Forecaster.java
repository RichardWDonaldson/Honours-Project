package controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.Random;

import model.AdvancedSettings;
import model.ChartSettings;
import model.Model;
import model.RegressionSetting;
import view.ChartView;
import weka.core.Instances;
import weka.core.Utils;
import weka.filters.Filter;
import weka.filters.supervised.attribute.TSLagMaker;
import weka.filters.unsupervised.attribute.Remove;
import weka.classifiers.functions.LinearRegression;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.Evaluation;
import weka.classifiers.evaluation.NumericPrediction;
import weka.classifiers.timeseries.WekaForecaster;
import weka.classifiers.timeseries.eval.MAEModule;
import weka.classifiers.timeseries.eval.TSEvalModule;
import weka.classifiers.timeseries.eval.TSEvaluation;


public class Forecaster {
	//TODO Create graph that outputs forecast, outputs the dataset and then the forecast
	//TODO Look at All but One Validation
	//TODO sort the dataset so there are more instances
	

	Instances trainingInstances;
	Instances testInstances;

	Model model = new Model();

	public void mlpSimpleForecast(File arffFile, String fieldsToForecast, int classIndex, int iterations, int numberOfPredictions) {
		try {

			for(int i = 1; i <= iterations; i++) {

				splitTrainingSet(arffFile, classIndex);

				MultilayerPerceptron multiLayerPerceptron = new MultilayerPerceptron();
				multiLayerPerceptron.buildClassifier(trainingInstances);

				WekaForecaster forecaster = new WekaForecaster();
				forecaster.setFieldsToForecast(fieldsToForecast);
				forecaster.setBaseForecaster(multiLayerPerceptron);
				forecaster.buildForecaster(trainingInstances, System.out);
				forecaster.primeForecaster(trainingInstances);

				List<List<NumericPrediction>> forecast = forecaster.forecast(numberOfPredictions, System.out);

				for(int j = 0; j < numberOfPredictions; j++) {

					List<NumericPrediction> predsAtStep = forecast.get(j);
					NumericPrediction predForTarget = predsAtStep.get(0);

					System.out.println("" + predForTarget.predicted() + " ");
				}
				// evaluate on test data
				System.out.println("Evaluating Model on Test Set");

				model.saveEvaluation(testInstances, multiLayerPerceptron, null);

			}

			

			ChartSettings chartSettings = new ChartSettings("Forecast",fieldsToForecast,"Iteration","");
			
			
			model.outputMLPResults(chartSettings, null);

		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
	}

	public void mlpAdvancedForecast(File arffFile, String fieldsToForecast, int classIndex, int iterations, AdvancedSettings settings, int numberOfPredictions) {
		try {
			for(int i = 0; i <= iterations; i++) {

				splitTrainingSet(arffFile, classIndex);

				MultilayerPerceptron multiLayerPerceptron = new MultilayerPerceptron();

				multiLayerPerceptron.setHiddenLayers(settings.getStructure());
				multiLayerPerceptron.setTrainingTime((int) settings.getIterations());
				multiLayerPerceptron.setLearningRate(settings.getLearningRate());
				multiLayerPerceptron.setValidationSetSize((int) settings.getValidationSize());
				multiLayerPerceptron.setValidationThreshold((int) settings.getValidationThreshold());
				multiLayerPerceptron.setMomentum(settings.getMomentum());
				multiLayerPerceptron.setSeed((int) settings.getSeed());

				WekaForecaster forecaster = new WekaForecaster();	

				forecaster.setFieldsToForecast(fieldsToForecast);
				forecaster.setBaseForecaster(multiLayerPerceptron);
				forecaster.buildForecaster(trainingInstances, System.out);
				forecaster.primeForecaster(trainingInstances);

				List<List<NumericPrediction>> forecast = forecaster.forecast(numberOfPredictions, System.out);

				for(int j = 0; j < numberOfPredictions; i++) {
					List<NumericPrediction> predsAtStep = forecast.get(i);
					NumericPrediction predForTarget = predsAtStep.get(0);

					System.out.println("" + predForTarget.predicted() + " ");
				}

				model.saveEvaluation(testInstances, multiLayerPerceptron, settings);
			}
			
			ChartSettings chartSettings = new ChartSettings("Forecast",fieldsToForecast,"Iteration","");
			
			model.outputMLPResults(chartSettings, settings);
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}

	public void regressionSimpleForecast(File arffFile, String fieldsToForecast, int classIndex, int iterations, int numberOfPredictions) {
		try {
			
			for(int i = 1; i <= iterations; i++) {

				splitTrainingSet(arffFile, classIndex);

				LinearRegression linearRegression = new LinearRegression();
				linearRegression.buildClassifier(trainingInstances);


				WekaForecaster forecaster = new WekaForecaster();
				forecaster.setFieldsToForecast(fieldsToForecast);
				forecaster.setBaseForecaster(linearRegression);
				forecaster.buildForecaster(trainingInstances, System.out);
				forecaster.primeForecaster(trainingInstances);

				List<List<NumericPrediction>> forecast = forecaster.forecast(numberOfPredictions, System.out);

				for(int j = 0; j < numberOfPredictions; j++) {

					List<NumericPrediction> predsAtStep = forecast.get(j);
					NumericPrediction predForTarget = predsAtStep.get(0);

					System.out.println("" + predForTarget.predicted() + " ");
				}

				model.saveEvaluation(testInstances, linearRegression, null);

			}
			ChartSettings chartSettings = new ChartSettings("Forecast",fieldsToForecast,"Iteration","");
			model.outputRegressionResults(chartSettings, null);

		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}


	}

	public void regressionAdvancedBuild(File arffFile, String fieldsToForecast, int classIndex, int iterations, RegressionSetting settings, int numberOfPredictions) {

		String tempRidge = " -R " + Double.toString(settings.getRidge());
		String tempDecimalPlaces = " -num-decimal-places " + Integer.toString(settings.getDecimalPlaces());
		String tempAttributeSelection = " -S " + Integer.toString(settings.getAttributeSelectionMethod());

		try {

			for(int i = 1; i <= iterations; i++) {

				splitTrainingSet(arffFile, classIndex);

				LinearRegression linearRegression = new LinearRegression();	
				((LinearRegression) linearRegression).setOptions(Utils.splitOptions(tempAttributeSelection + tempRidge + tempDecimalPlaces));
				linearRegression.buildClassifier(trainingInstances);

				WekaForecaster forecaster = new WekaForecaster();
				forecaster.setFieldsToForecast(fieldsToForecast);
				forecaster.setBaseForecaster(linearRegression);
				forecaster.buildForecaster(trainingInstances, System.out);
				forecaster.primeForecaster(trainingInstances);

				List<List<NumericPrediction>> forecast = forecaster.forecast(numberOfPredictions, System.out);

				for(int j = 0; j < numberOfPredictions; j++) {

					List<NumericPrediction> predsAtStep = forecast.get(j);
					NumericPrediction predForTarget = predsAtStep.get(0);

					System.out.println("" + predForTarget.predicted() + " ");
				}

				model.saveEvaluation(testInstances, linearRegression, null);

			}

			
			ChartSettings chartSettings = new ChartSettings("Forecast",fieldsToForecast,"Iteration","Value");
			model.outputRegressionResults(chartSettings, settings);

		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}

	}

	private void splitTrainingSet(File arffFile, int classIndex) throws Exception {

		Instances data = getInstances(arffFile.getName(), classIndex);

		// randomise the data.
		long seed = System.currentTimeMillis();
		Random rand = new Random(seed);
		data.randomize(rand);
		double trainingSetRatio = 0.661;
		int dataSize = data.numInstances();
		int trainingSize = (int) Math.ceil(dataSize * trainingSetRatio);
		int testSize = dataSize - trainingSize;

		// actual split
		trainingInstances = new Instances(data, 0, trainingSize);
		testInstances = new Instances(data, trainingSize, testSize);
	}

	private static Instances getInstances(String filename, int classIndex) throws Exception {
		Instances data = null;
		InputStream is = null;
		Reader in = null;
		BufferedReader reader = null;

		is = new FileInputStream(filename);
		in = new InputStreamReader(is);
		reader = new BufferedReader(in);
		data = new Instances(reader);
		System.out.println(classIndex);
		reader.close();
		in.close();
		is.close();
		data.setClassIndex(classIndex);
		return data;
	}

}
