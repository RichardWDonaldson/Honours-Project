package controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Map.Entry;

import model.AdvancedSettings;
import model.ChartSettings;
import model.Model;
import model.RegressionSetting;
import model.Result;
import weka.core.Instances;
import weka.core.Utils;
import weka.filters.supervised.attribute.TSLagMaker;
import weka.classifiers.functions.LinearRegression;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.Evaluation;
import weka.classifiers.evaluation.NumericPrediction;
import weka.classifiers.timeseries.WekaForecaster;

public class Forecaster {
	Model model = new Model();

	public void mlpSimpleForecast(File arffFile, String fieldsToForecast, int classIndex, int iterations,
			int numberOfPredictions) {
		try {
			Instances train = null;
			Instances test = null;
			ArrayList<Result> meanResults = new ArrayList<Result>();
			for (int i = 1; i <= iterations; i++) {
				ArrayList<Result> foldResults = new ArrayList<Result>();
				Instances data = getInstances(arffFile.getName(), classIndex);

				Random generator = new Random(System.currentTimeMillis());

				Instances randData = new Instances(data);
				MultilayerPerceptron classifier = new MultilayerPerceptron();
				randData.randomize(generator);
				int folds = randData.size();
				Evaluation eval = new Evaluation(randData);
				for (int n = 0; n < folds; n++) {
					train = randData.trainCV(folds, n, generator);
					test = randData.testCV(folds, n);

					AdvancedSettings settings = null;
					classifier.buildClassifier(train);
					eval.evaluateModel(classifier, test);

					Result result = new Result(settings, eval.correlationCoefficient(), eval.meanAbsoluteError(),
							eval.rootMeanSquaredError(), eval.relativeAbsoluteError(), eval.rootRelativeSquaredError(),

							eval.numInstances());

					foldResults.add(result);
					System.out.println(eval.toSummaryString("=== " + folds + "-fold Cross-validation ===", false));

				}

				int count = foldResults.size();

				Result sumResult = calculateSumResult(foldResults, null);

				Result meanResult = calculateMeanResult(sumResult, count);

				meanResults.add(meanResult);

				// Actual Forecast
				WekaForecaster forecaster = new WekaForecaster();
				forecaster.setFieldsToForecast(fieldsToForecast);
				forecaster.setBaseForecaster(classifier);
				forecaster.buildForecaster(train, System.out);
				forecaster.primeForecaster(train);

				List<List<NumericPrediction>> forecast = forecaster.forecast(numberOfPredictions, System.out);

				for (int j = 0; j < numberOfPredictions; j++) {

					List<NumericPrediction> predsAtStep = forecast.get(j);
					NumericPrediction predForTarget = predsAtStep.get(0);

					System.out.println("" + predForTarget.predicted() + " ");
				}

			}

			ChartSettings chartSettings = new ChartSettings("Forecast", fieldsToForecast, "Iteration", "");

			model.outputMLPResults(chartSettings, null, meanResults);

//			for (Result result : meanResults) {
//				System.out.println("RESULT: ");
//				System.out.println(result.toString());
//			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	public void mlpAdvancedForecast(File arffFile, String fieldsToForecast, int classIndex, int iterations,
			AdvancedSettings settings, int numberOfPredictions) {
		try {
			Instances train = null;
			Instances test = null;
			ArrayList<Result> meanResults = new ArrayList<Result>();
			for (int i = 1; i <= iterations; i++) {
				ArrayList<Result> foldResults = new ArrayList<Result>();
				Instances data = getInstances(arffFile.getName(), classIndex);

				Random generator = new Random(System.currentTimeMillis());

				Instances randData = new Instances(data);
				MultilayerPerceptron classifier = new MultilayerPerceptron();
				
				classifier.setHiddenLayers(settings.getStructure());
				classifier.setTrainingTime((int) settings.getIterations());
				classifier.setLearningRate(settings.getLearningRate());
				classifier.setValidationSetSize((int) settings.getValidationSize());
				classifier.setValidationThreshold((int) settings.getValidationThreshold());
				classifier.setMomentum(settings.getMomentum());
				classifier.setSeed((int) settings.getSeed());

				randData.randomize(generator);
				int folds = randData.size();
				Evaluation eval = new Evaluation(randData);
				for (int n = 0; n < folds; n++) {
					train = randData.trainCV(folds, n, generator);
					test = randData.testCV(folds, n);

					classifier.buildClassifier(train);
					eval.evaluateModel(classifier, test);

					Result result = new Result(settings, eval.correlationCoefficient(), eval.meanAbsoluteError(),
							eval.rootMeanSquaredError(), eval.relativeAbsoluteError(), eval.rootRelativeSquaredError(),

							eval.numInstances());

					foldResults.add(result);
					System.out.println(eval.toSummaryString("=== " + folds + "-fold Cross-validation ===", false));

				}
				int count = foldResults.size();

				Result sumResult = calculateSumResult(foldResults, null);

				Result meanResult = calculateMeanResult(sumResult, count);

				meanResults.add(meanResult);

				// Actual Forecast
				WekaForecaster forecaster = new WekaForecaster();
				forecaster.setFieldsToForecast(fieldsToForecast);
				forecaster.setBaseForecaster(classifier);
				forecaster.buildForecaster(train, System.out);
				forecaster.primeForecaster(train);

				List<List<NumericPrediction>> forecast = forecaster.forecast(numberOfPredictions, System.out);

				for (int j = 0; j < numberOfPredictions; j++) {

					List<NumericPrediction> predsAtStep = forecast.get(j);
					NumericPrediction predForTarget = predsAtStep.get(0);

					System.out.println("" + predForTarget.predicted() + " ");
				}

			}
			ChartSettings chartSettings = new ChartSettings("Forecast", fieldsToForecast, "Iteration", "");

			model.outputMLPResults(chartSettings, settings, meanResults);

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	public void regressionSimpleForecast(File arffFile, String fieldsToForecast, int classIndex, int iterations,
			int numberOfPredictions) {
		try {
			Instances train = null;
			Instances test = null;
			ArrayList<Result> meanResults = new ArrayList<Result>();
			for (int i = 1; i <= iterations; i++) {
				ArrayList<Result> foldResults = new ArrayList<Result>();
				Instances data = getInstances(arffFile.getName(), classIndex);

				Random generator = new Random(System.currentTimeMillis());

				Instances randData = new Instances(data);
				LinearRegression classifier = new LinearRegression();
				randData.randomize(generator);
				int folds = randData.size();
				Evaluation eval = new Evaluation(randData);
				for (int n = 0; n < folds; n++) {
					train = randData.trainCV(folds, n, generator);
					test = randData.testCV(folds, n);

					AdvancedSettings settings = null;
					
					classifier.buildClassifier(train);
					eval.evaluateModel(classifier, test);

					Result result = new Result(settings, eval.correlationCoefficient(), eval.meanAbsoluteError(),
							eval.rootMeanSquaredError(), eval.relativeAbsoluteError(), eval.rootRelativeSquaredError(),

							eval.numInstances());

					foldResults.add(result);
					System.out.println(eval.toSummaryString("=== " + folds + "-fold Cross-validation ===", false));

				}

				int count = foldResults.size();

				Result sumResult = calculateSumResult(foldResults, null);

				Result meanResult = calculateMeanResult(sumResult, count);

				meanResults.add(meanResult);

				// Actual Forecast
				WekaForecaster forecaster = new WekaForecaster();
				forecaster.setFieldsToForecast(fieldsToForecast);
				forecaster.setBaseForecaster(classifier);
				forecaster.buildForecaster(train, System.out);
				forecaster.primeForecaster(train);

				List<List<NumericPrediction>> forecast = forecaster.forecast(numberOfPredictions, System.out);

				for (int j = 0; j < numberOfPredictions; j++) {

					List<NumericPrediction> predsAtStep = forecast.get(j);
					NumericPrediction predForTarget = predsAtStep.get(0);

					System.out.println("" + predForTarget.predicted() + " ");
				}

			}

			ChartSettings chartSettings = new ChartSettings("Forecast", fieldsToForecast, "Iteration", "");

			model.outputMLPResults(chartSettings, null, meanResults);
			
//			for (Result result : meanResults) {
//				System.out.println("RESULT: ");
//				System.out.println(result.toString());
//			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	public void regressionAdvancedForecast(File arffFile, String fieldsToForecast, int classIndex, int iterations,
			RegressionSetting settings, int numberOfPredictions) {
		try {
			Instances train = null;
			Instances test = null;
			ArrayList<Result> meanResults = new ArrayList<Result>();
			String tempRidge = " -R " + Double.toString(settings.getRidge());
			String tempDecimalPlaces = " -num-decimal-places " + Integer.toString(settings.getDecimalPlaces());
			String tempAttributeSelection = " -S " + Integer.toString(settings.getAttributeSelectionMethod());

			for (int i = 1; i <= iterations; i++) {
				ArrayList<Result> foldResults = new ArrayList<Result>();
				Instances data = getInstances(arffFile.getName(), classIndex);

				Random generator = new Random(System.currentTimeMillis());

				Instances randData = new Instances(data);
				LinearRegression classifier = new LinearRegression();
				((LinearRegression) classifier)
						.setOptions(Utils.splitOptions(tempAttributeSelection + tempRidge + tempDecimalPlaces));

				randData.randomize(generator);
				int folds = randData.size();
				Evaluation eval = new Evaluation(randData);
				for (int n = 0; n < folds; n++) {
					train = randData.trainCV(folds, n, generator);
					test = randData.testCV(folds, n);

					classifier.buildClassifier(train);
					eval.evaluateModel(classifier, test);

					Result result = new Result(settings, eval.correlationCoefficient(), eval.meanAbsoluteError(),
							eval.rootMeanSquaredError(), eval.relativeAbsoluteError(), eval.rootRelativeSquaredError(),

							eval.numInstances());

					foldResults.add(result);
					System.out.println(eval.toSummaryString("=== " + folds + "-fold Cross-validation ===", false));

				}
				int count = foldResults.size();

				Result sumResult = calculateSumResult(foldResults, null);

				Result meanResult = calculateMeanResult(sumResult, count);

				meanResults.add(meanResult);

				// Actual Forecast
				WekaForecaster forecaster = new WekaForecaster();
				forecaster.setFieldsToForecast(fieldsToForecast);
				forecaster.setBaseForecaster(classifier);
				forecaster.buildForecaster(train, System.out);
				forecaster.primeForecaster(train);

				List<List<NumericPrediction>> forecast = forecaster.forecast(numberOfPredictions, System.out);
				System.out.println("===FORECAST===");
				for (int j = 0; j < numberOfPredictions; j++) {

					List<NumericPrediction> predsAtStep = forecast.get(j);
					NumericPrediction predForTarget = predsAtStep.get(0);

					System.out.println("" + predForTarget.predicted() + " ");
				}

			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	private Result calculateSumResult(ArrayList<Result> results, AdvancedSettings settings) {

		double correlationCoefficient = 0;
		double meanAbsoluteError = 0;
		double rootMeanSquaredError = 0;
		double relativeAbsoluteError = 0;
		double rootRelativeSquaredError = 0;
		double instances = 0;

		for (Result result : results) {
			correlationCoefficient += result.getCorrelationCoefficient();
			meanAbsoluteError += result.getMeanAbsoluteError();
			rootMeanSquaredError += result.getRootMeanSquaredError();
			relativeAbsoluteError += result.getRelativeAbsoluteError();
			rootRelativeSquaredError += result.getRootRelativeSquaredError();
			instances += result.getInstances();
		}

		Result sumResult = new Result(settings, correlationCoefficient, meanAbsoluteError, rootMeanSquaredError,
				relativeAbsoluteError, rootRelativeSquaredError, instances);

		return sumResult;

	}

	private Result calculateMeanResult(Result result, int count) {
		double tempCorrelationCoefficient = result.getCorrelationCoefficient() / count;
		double tempMeanAbsoluteError = result.getMeanAbsoluteError() / count;
		double tempRootMeanSquaredError = result.getRootMeanSquaredError() / count;
		double tempRelativeAbsoluteError = result.getRelativeAbsoluteError() / count;
		double tempRootRelativeSquaredError = result.getRootRelativeSquaredError() / count;

		Result meanResult = new Result(result.getSettings(), tempCorrelationCoefficient, tempMeanAbsoluteError,
				tempRootMeanSquaredError, tempRelativeAbsoluteError, tempRootRelativeSquaredError,
				result.getInstances());
		return meanResult;

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
