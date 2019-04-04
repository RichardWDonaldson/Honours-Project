package controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.TreeMap;

import org.jfree.data.category.DefaultCategoryDataset;

import model.AdvancedSettings;
import model.Model;
import model.RegressionSetting;
import model.Result;
import view.ChartView;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.evaluation.NumericPrediction;
import weka.classifiers.functions.LinearRegression;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.timeseries.WekaForecaster;
import weka.core.Instances;
import weka.core.Utils;

public class TestEvaluation {

	Instances trainingInstances;
	Instances testInstances;

	Model model = new Model();

	public void mlpEvaluation(File arffFile, String fieldsToForecast, int classIndex, int iterations,
			int numberOfPredictions) {

		try {
			Instances train = null;
			Instances test = null;
			String currentTest = "";
			Map<String, Long> mapTime = new TreeMap<String, Long>();
			Map<String, Result> mapMeanResults = new TreeMap<String, Result>();
			Map<String, List<List<NumericPrediction>>> mapPredictions = new TreeMap<String, List<List<NumericPrediction>>>();
			Map<String, AdvancedSettings> map = model.populateMLPEvaluation();
			Map<String, DefaultCategoryDataset> mapDatasets = new TreeMap<String, DefaultCategoryDataset>();

			for (Map.Entry<String, AdvancedSettings> entry : map.entrySet()) {
				long startTime = System.nanoTime();
				int testNumber = 1;
				String outputType = entry.getKey();

				System.out.println("==" + outputType + " TEST==");

				for (int i = 1; i <= iterations; i++) {
					ArrayList<Result> foldResults = new ArrayList<Result>();
					Instances data = getInstances(arffFile.getName(), classIndex);
					Random generator = new Random(System.currentTimeMillis());

					Instances randData = new Instances(data);
					MultilayerPerceptron classifier = new MultilayerPerceptron();
					classifier.setHiddenLayers(entry.getValue().getStructure());
					classifier.setTrainingTime((int) entry.getValue().getIterations());
					classifier.setLearningRate(entry.getValue().getLearningRate());
					classifier.setValidationSetSize((int) entry.getValue().getValidationSize());
					classifier.setValidationThreshold((int) entry.getValue().getValidationThreshold());
					classifier.setMomentum(entry.getValue().getMomentum());
					classifier.setSeed((int) entry.getValue().getSeed());

					randData.randomize(generator);
					int folds = randData.size();
				
					Evaluation eval = new Evaluation(randData);
					for (int n = 0; n < folds; n++) {
						train = randData.trainCV(folds, n, generator);
						test = randData.testCV(folds, n);

						classifier.buildClassifier(train);
						eval.evaluateModel(classifier, test);
						Result result = new Result(entry.getValue(), eval.correlationCoefficient(),
								eval.meanAbsoluteError(), eval.rootMeanSquaredError(), eval.relativeAbsoluteError(),
								eval.rootRelativeSquaredError(), eval.numInstances());

						foldResults.add(result);
						System.out.println(eval.toSummaryString("=== " + folds + "-fold Cross-validation ===", false));

					}
					int count = foldResults.size();
					Result sumResult = calculateSumResult(foldResults, "MLP");

					Result meanResult = calculateMeanResult(sumResult, count);
					meanResult.setSettings(entry.getValue());

					currentTest = outputType + testNumber;
					System.out.println("Adding " + test);
					System.out.println("XXXCurrent Test" + currentTest + "XXX");
					System.out.println("Mean Result " + meanResult.toString());
					mapMeanResults.put(currentTest, meanResult);
					testNumber++;

					// Actual Forecast
					WekaForecaster forecaster = new WekaForecaster();
					forecaster.setFieldsToForecast(fieldsToForecast);
					forecaster.setBaseForecaster(classifier);
					forecaster.buildForecaster(train, System.out);
					forecaster.primeForecaster(train);

					List<List<NumericPrediction>> forecast = forecaster.forecast(numberOfPredictions, System.out);
					mapPredictions.put(currentTest, forecast);
					for (int j = 0; j < numberOfPredictions; j++) {

						List<NumericPrediction> predsAtStep = forecast.get(j);
						NumericPrediction predForTarget = predsAtStep.get(0);

						System.out.println("" + predForTarget.predicted() + " ");

					}

				}
				long endTime = System.nanoTime();
				long duration = ((endTime - startTime) / 1000000000);

				mapTime.put(currentTest, duration);
			}
			// Finished Evaluations
			System.out.println("Test \t Time");
			for (Map.Entry<String, Long> entry : mapTime.entrySet()) {
				System.out.println(entry.getKey() + "\t" + entry.getValue());
			}
			System.out.println("==============================");

			for (Entry<String, Result> entry : mapMeanResults.entrySet()) {
				System.out.println(entry.getKey());
				System.out.println(entry.getValue().toString());
			}

			mapDatasets = createMLPDatasets(mapMeanResults);
			for (Map.Entry<String, DefaultCategoryDataset> entry : mapDatasets.entrySet()) {

				outputGraph(entry.getValue(), "Multi-Layer Perceptron", "Evaluation", entry.getKey(), "Value");

			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	public void regressionEvaluation(File arffFile, String fieldsToForecast, int classIndex, int iterations,
			int numberOfPredictions) {
		try {
			Instances train = null;
			Instances test = null;
			String currentTest = "";
			Map<String, Long> mapTime = new TreeMap<String, Long>();
			Map<String, Result> mapMeanResults = new TreeMap<String, Result>();
			Map<String, List<List<NumericPrediction>>> mapPredictions = new TreeMap<String, List<List<NumericPrediction>>>();
			Map<String, RegressionSetting> map = model.populateRegressionEvaluation();
			Map<String, DefaultCategoryDataset> mapDatasets = new TreeMap<String, DefaultCategoryDataset>();

			for (Map.Entry<String, RegressionSetting> entry : map.entrySet()) {
				long startTime = System.nanoTime();
				int testNumber = 1;
				String outputType = entry.getKey();

				System.out.println("==" + outputType + " TEST==");

				for (int i = 1; i <= iterations; i++) {
					String tempRidge = " -R " + Double.toString(entry.getValue().getRidge());
					String tempDecimalPlaces = " -num-decimal-places "
							+ Integer.toString(entry.getValue().getDecimalPlaces());
					String tempAttributeSelection = " -S "
							+ Integer.toString(entry.getValue().getAttributeSelectionMethod());

					ArrayList<Result> foldResults = new ArrayList<Result>();
					Instances data = getInstances(arffFile.getName(), classIndex);
					Random generator = new Random(System.currentTimeMillis());

					Instances randData = new Instances(data);
					LinearRegression classifier = new LinearRegression();

					classifier.setOptions(Utils.splitOptions(tempAttributeSelection + tempRidge + tempDecimalPlaces));
					randData.randomize(generator);
					int folds = randData.size();
					
					Evaluation eval = new Evaluation(randData);
					for (int n = 0; n < folds; n++) {
						train = randData.trainCV(folds, n, generator);
						test = randData.testCV(folds, n);

						classifier.buildClassifier(train);
						eval.evaluateModel(classifier, test);
						Result result = new Result(entry.getValue(), eval.correlationCoefficient(),
								eval.meanAbsoluteError(), eval.rootMeanSquaredError(), eval.relativeAbsoluteError(),
								eval.rootRelativeSquaredError(), eval.numInstances());

						foldResults.add(result);
						System.out.println(eval.toSummaryString("=== " + folds + "-fold Cross-validation ===", false));

					}
					int count = foldResults.size();
					Result sumResult = calculateSumResult(foldResults, "MLP");

					Result meanResult = calculateMeanResult(sumResult, count);
					meanResult.setRegressionSettings(entry.getValue());

					currentTest = outputType + testNumber;
					System.out.println("Adding " + test);
					System.out.println("XXXCurrent Test" + currentTest + "XXX");
					System.out.println("Mean Result " + meanResult.toString());
					mapMeanResults.put(currentTest, meanResult);
					testNumber++;

					// Actual Forecast
					WekaForecaster forecaster = new WekaForecaster();
					forecaster.setFieldsToForecast(fieldsToForecast);
					forecaster.setBaseForecaster(classifier);
					forecaster.buildForecaster(train, System.out);
					forecaster.primeForecaster(train);

					List<List<NumericPrediction>> forecast = forecaster.forecast(numberOfPredictions, System.out);
					mapPredictions.put(currentTest, forecast);
					for (int j = 0; j < numberOfPredictions; j++) {

						List<NumericPrediction> predsAtStep = forecast.get(j);
						NumericPrediction predForTarget = predsAtStep.get(0);

						System.out.println("" + predForTarget.predicted() + " ");

					}

				}
				long endTime = System.nanoTime();
				long duration = ((endTime - startTime) / 1000000000);

				mapTime.put(currentTest, duration);

			}
			// Finished Evaluations
			System.out.println("Test \t Time");
			for (Map.Entry<String, Long> entry : mapTime.entrySet()) {
				System.out.println(entry.getKey() + "\t" + entry.getValue());
			}

			System.out.println("==============================");

			for (Entry<String, Result> entry : mapMeanResults.entrySet()) {
				System.out.println(entry.getKey());
				System.out.println(entry.getValue().toString());
			}

			mapDatasets = createRegressionDatasets(mapMeanResults);
			for (Map.Entry<String, DefaultCategoryDataset> entry : mapDatasets.entrySet()) {

				outputGraph(entry.getValue(), "Regression", "Evaluation", entry.getKey(), "Value");

			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public Result evaluateMLPModel(Instances testInstances, Classifier classifier, AdvancedSettings settings,
			String outputType) {
//		System.out.println("Evaluating Model on Test Set");
//		try {
//
//			Evaluation eval = new Evaluation(testInstances);
//			Random generator = new Random();
//			eval.crossValidateModel(classifier, testInstances, testInstances.size(), generator);
//			// eval.evaluateModel(classifier, testInstances);
//
//			Result result = new Result(settings, eval.correlationCoefficient(), eval.meanAbsoluteError(),
//					eval.rootMeanSquaredError(), eval.relativeAbsoluteError(), eval.rootRelativeSquaredError(),
//					eval.numInstances());
//
//			System.out.println(eval.toSummaryString("\nResults\n======\n", false));
//			return result;
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			return null;
//		}
		return null;
	}

	public Result evaluateRegressionModel(Instances testInstances, Classifier classifier, RegressionSetting settings,
			String outputType) {
//		System.out.println("Evaluating Model on Test Set");
//
//		try {
//			Evaluation eval = new Evaluation(testInstances);
//			Random generator = new Random();
//
//			// eval.evaluateModel(classifier, testInstances);
//			System.out.println(testInstances.size());
//			eval.crossValidateModel(classifier, testInstances, testInstances.size(), generator);
//
//			Result result = new Result(settings, eval.correlationCoefficient(), eval.meanAbsoluteError(),
//					eval.rootMeanSquaredError(), eval.relativeAbsoluteError(), eval.rootRelativeSquaredError(),
//					eval.numInstances());
//
//			System.out.println(eval.toSummaryString("\nResults\n======\n", false));
//			return result;
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			return null;
//		}
		return null;

	}

	public void allProductsMLPEvaluation(File arffFile, Object[] fieldsToForecast, int iterations,
			int numberOfPredictions) {
		try {
			Instances train = null;
			Instances test = null;
			String testCase = "";
			Map<String, Long> mapTime = new TreeMap<String, Long>();
			Map<String, Result> mapMeanResults = new TreeMap<String, Result>();
			Map<String, List<List<NumericPrediction>>> mapPredictions = new TreeMap<String, List<List<NumericPrediction>>>();
			Map<String, AdvancedSettings> map = model.populateMLPEvaluation();
			Map<String, DefaultCategoryDataset> mapDatasets = new TreeMap<String, DefaultCategoryDataset>();

			for (int i = 1; i < fieldsToForecast.length; i++) {
				for (Map.Entry<String, AdvancedSettings> entry : map.entrySet()) {
					long startTime = System.nanoTime();
					int testNumber = 1;
					String outputType = entry.getKey();
					String currentProduct = (String) fieldsToForecast[i];
					testCase = outputType + "|" + currentProduct;

					System.out.println("==" + outputType + "|Product: " + currentProduct + " TEST==");

					for (int j = 1; j <= iterations; j++) {
						ArrayList<Result> foldResults = new ArrayList<Result>();
						Instances data = getInstances(arffFile.getName(), i);
						Random generator = new Random(System.currentTimeMillis());

						Instances randData = new Instances(data);
						MultilayerPerceptron classifier = new MultilayerPerceptron();
						classifier.setHiddenLayers(entry.getValue().getStructure());
						classifier.setTrainingTime((int) entry.getValue().getIterations());
						classifier.setLearningRate(entry.getValue().getLearningRate());
						classifier.setValidationSetSize((int) entry.getValue().getValidationSize());
						classifier.setValidationThreshold((int) entry.getValue().getValidationThreshold());
						classifier.setMomentum(entry.getValue().getMomentum());
						classifier.setSeed((int) entry.getValue().getSeed());

						randData.randomize(generator);
						//int folds = randData.size();
						int folds = 10;
						Evaluation eval = new Evaluation(randData);
						for (int n = 0; n < folds; n++) {
							train = randData.trainCV(folds, n, generator);
							test = randData.testCV(folds, n);

							classifier.buildClassifier(train);
							eval.evaluateModel(classifier, test);
							Result result = new Result(entry.getValue(), eval.correlationCoefficient(),
									eval.meanAbsoluteError(), eval.rootMeanSquaredError(), eval.relativeAbsoluteError(),
									eval.rootRelativeSquaredError(), eval.numInstances());

							foldResults.add(result);
							System.out.println(
									eval.toSummaryString("=== " + folds + "-fold Cross-validation ===", false));

						} // FOLDS

						int count = foldResults.size();
						Result sumResult = calculateSumResult(foldResults, "MLP");

						Result meanResult = calculateMeanResult(sumResult, count);
						meanResult.setSettings(entry.getValue());

						// currentTest = outputType + testNumber;
						System.out.println("Adding " + test);
						System.out.println("XXXCurrent Test" + testCase + "XXX");
						System.out.println("Mean Result " + meanResult.toString());
						mapMeanResults.put(testCase, meanResult);
						testNumber++;

						// Actual Forecast
						WekaForecaster forecaster = new WekaForecaster();
						forecaster.setFieldsToForecast((String) fieldsToForecast[i]);
						forecaster.setBaseForecaster(classifier);
						forecaster.buildForecaster(train, System.out);
						forecaster.primeForecaster(train);

						List<List<NumericPrediction>> forecast = forecaster.forecast(numberOfPredictions, System.out);
						mapPredictions.put(testCase, forecast);
						for (int k = 0; k < numberOfPredictions; k++) {

							List<NumericPrediction> predsAtStep = forecast.get(k);
							NumericPrediction predForTarget = predsAtStep.get(0);

							System.out.println("" + predForTarget.predicted() + " ");

						} // FOLDS

						long endTime = System.nanoTime();
						long duration = ((endTime - startTime) / 1000000000);

						mapTime.put(testCase, duration);

					} // ITERATIONS

				} // MAP

			} // PRODUCTS

			// Finished Evaluations
			System.out.println("Test \t Time");
			for (Map.Entry<String, Long> entry : mapTime.entrySet()) {
				System.out.println(entry.getKey() + "\t" + entry.getValue());
			}
			System.out.println("==============================");

			for (Entry<String, Result> entry : mapMeanResults.entrySet()) {
				System.out.println(entry.getKey());
				System.out.println(entry.getValue().toString());
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	} // CLASS

	public void allProductsRegressionEvaluation(File arffFile, Object[] fieldsToForecast, int iterations,
			int numberOfPredictions) {
		try {
			Instances train = null;
			Instances test = null;
			String testCase = "";

			Map<String, Long> mapTime = new TreeMap<String, Long>();
			Map<String, Result> mapMeanResults = new TreeMap<String, Result>();
			Map<String, List<List<NumericPrediction>>> mapPredictions = new TreeMap<String, List<List<NumericPrediction>>>();
			Map<String, RegressionSetting> map = model.populateRegressionEvaluation();
			Map<String, DefaultCategoryDataset> mapDatasets = new TreeMap<String, DefaultCategoryDataset>();
			for (int i = 1; i < fieldsToForecast.length; i++) {
				for (Map.Entry<String, RegressionSetting> entry : map.entrySet()) {
					long startTime = System.nanoTime();
					int testNumber = 1;
					String outputType = entry.getKey();
					String currentProduct = (String) fieldsToForecast[i];
					testCase = outputType + "|" + currentProduct;

					System.out.println("==" + outputType + "|Product: " + currentProduct + " TEST==");

					for (int j = 1; j <= iterations; j++) {
						String tempRidge = " -R " + Double.toString(entry.getValue().getRidge());
						String tempDecimalPlaces = " -num-decimal-places "
								+ Integer.toString(entry.getValue().getDecimalPlaces());
						String tempAttributeSelection = " -S "
								+ Integer.toString(entry.getValue().getAttributeSelectionMethod());

						ArrayList<Result> foldResults = new ArrayList<Result>();
						Instances data = getInstances(arffFile.getName(), i);
						Random generator = new Random(System.currentTimeMillis());

						Instances randData = new Instances(data);
						LinearRegression classifier = new LinearRegression();

						classifier
								.setOptions(Utils.splitOptions(tempAttributeSelection + tempRidge + tempDecimalPlaces));
						randData.randomize(generator);
						//int folds = randData.size();
						int folds = 10;
						Evaluation eval = new Evaluation(randData);
						for (int n = 0; n < folds; n++) {
							train = randData.trainCV(folds, n, generator);
							test = randData.testCV(folds, n);

							classifier.buildClassifier(train);
							eval.evaluateModel(classifier, test);
							Result result = new Result(entry.getValue(), eval.correlationCoefficient(),
									eval.meanAbsoluteError(), eval.rootMeanSquaredError(), eval.relativeAbsoluteError(),
									eval.rootRelativeSquaredError(), eval.numInstances());

							foldResults.add(result);
							System.out.println(
									eval.toSummaryString("=== " + folds + "-fold Cross-validation ===", false));

						}
						int count = foldResults.size();
						Result sumResult = calculateSumResult(foldResults, "MLP");

						Result meanResult = calculateMeanResult(sumResult, count);
						meanResult.setRegressionSettings(entry.getValue());

						// currentTest = outputType + testNumber;
						System.out.println("Adding " + test);
						System.out.println("XXXCurrent Test" + testCase + "XXX");
						System.out.println("Mean Result " + meanResult.toString());
						mapMeanResults.put(testCase, meanResult);
						testNumber++;

						// Actual Forecast
						WekaForecaster forecaster = new WekaForecaster();
						forecaster.setFieldsToForecast((String) fieldsToForecast[i]);
						forecaster.setBaseForecaster(classifier);
						forecaster.buildForecaster(train, System.out);
						forecaster.primeForecaster(train);

						List<List<NumericPrediction>> forecast = forecaster.forecast(numberOfPredictions, System.out);
						mapPredictions.put(testCase, forecast);
						for (int k = 0; k < numberOfPredictions; k++) {

							List<NumericPrediction> predsAtStep = forecast.get(k);
							NumericPrediction predForTarget = predsAtStep.get(0);

							System.out.println("" + predForTarget.predicted() + " ");

						}

					}
					long endTime = System.nanoTime();
					long duration = ((endTime - startTime) / 1000000000);

					mapTime.put(testCase, duration);

				}

			}
			// Finished Evaluations
			System.out.println("Test \t Time");
			for (Map.Entry<String, Long> entry : mapTime.entrySet()) {
				System.out.println(entry.getKey() + "\t" + entry.getValue());
			}

			System.out.println("==============================");

			for (Entry<String, Result> entry : mapMeanResults.entrySet()) {
				System.out.println(entry.getKey());
				System.out.println(entry.getValue().toString());
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private Map<String, DefaultCategoryDataset> createMLPDatasets(Map<String, Result> meanResults) {

		Map<String, DefaultCategoryDataset> mapDatasets = new TreeMap<String, DefaultCategoryDataset>();

//		DefaultCategoryDataset standard = new DefaultCategoryDataset();
		DefaultCategoryDataset structure = new DefaultCategoryDataset();
		DefaultCategoryDataset trainingIterations = new DefaultCategoryDataset();
		DefaultCategoryDataset learningRate = new DefaultCategoryDataset();
		DefaultCategoryDataset momentum = new DefaultCategoryDataset();
		DefaultCategoryDataset validationThreshold = new DefaultCategoryDataset();
		DefaultCategoryDataset validationSize = new DefaultCategoryDataset();

		for (Map.Entry<String, Result> entry : meanResults.entrySet()) {

			String currentKey = entry.getKey().toUpperCase();
			currentKey = currentKey.replaceAll("\\d", "");
			System.out.println("Current Key " + currentKey);

			switch (currentKey) {

			case "STRUCTURE":
				structure.addValue(entry.getValue().getMeanAbsoluteError(), "MAE",
						entry.getValue().getSettings().getStructure());
				structure.addValue(entry.getValue().getRelativeAbsoluteError(), "RAE",
						entry.getValue().getSettings().getStructure());
				structure.addValue(entry.getValue().getRootMeanSquaredError(), "RMSE",
						entry.getValue().getSettings().getStructure());
				structure.addValue(entry.getValue().getRootRelativeSquaredError(), "RRSE",
						entry.getValue().getSettings().getStructure());
				break;
			case "TRAINING_ITERATIONS":
				trainingIterations.addValue(entry.getValue().getMeanAbsoluteError(), "MAE",
						Double.toString(entry.getValue().getSettings().getIterations()));
				trainingIterations.addValue(entry.getValue().getRelativeAbsoluteError(), "RAE",
						Double.toString(entry.getValue().getSettings().getIterations()));
				trainingIterations.addValue(entry.getValue().getRootMeanSquaredError(), "RMSE",
						Double.toString(entry.getValue().getSettings().getIterations()));
				trainingIterations.addValue(entry.getValue().getRootRelativeSquaredError(), "RRSE",
						Double.toString(entry.getValue().getSettings().getIterations()));
				break;
			case "LEARNING_RATE":
				learningRate.addValue(entry.getValue().getMeanAbsoluteError(), "MAE",
						Double.toString(entry.getValue().getSettings().getLearningRate()));
				learningRate.addValue(entry.getValue().getRelativeAbsoluteError(), "RAE",
						Double.toString(entry.getValue().getSettings().getLearningRate()));
				learningRate.addValue(entry.getValue().getRootMeanSquaredError(), "RMSE",
						Double.toString(entry.getValue().getSettings().getLearningRate()));
				learningRate.addValue(entry.getValue().getRootRelativeSquaredError(), "RRSE",
						Double.toString(entry.getValue().getSettings().getLearningRate()));
				break;
			case "MOMENTUM":
				momentum.addValue(entry.getValue().getMeanAbsoluteError(), "MAE",
						Double.toString(entry.getValue().getSettings().getMomentum()));
				momentum.addValue(entry.getValue().getRelativeAbsoluteError(), "RAE",
						Double.toString(entry.getValue().getSettings().getMomentum()));
				momentum.addValue(entry.getValue().getRootMeanSquaredError(), "RMSE",
						Double.toString(entry.getValue().getSettings().getMomentum()));
				momentum.addValue(entry.getValue().getRootRelativeSquaredError(), "RRSE",
						Double.toString(entry.getValue().getSettings().getMomentum()));
				break;
			case "VALIDATION_THRESHOLD":
				validationThreshold.addValue(entry.getValue().getMeanAbsoluteError(), "MAE",
						Double.toString(entry.getValue().getSettings().getValidationThreshold()));
				validationThreshold.addValue(entry.getValue().getRelativeAbsoluteError(), "RAE",
						Double.toString(entry.getValue().getSettings().getValidationThreshold()));
				validationThreshold.addValue(entry.getValue().getRootMeanSquaredError(), "RMSE",
						Double.toString(entry.getValue().getSettings().getValidationThreshold()));
				validationThreshold.addValue(entry.getValue().getRootRelativeSquaredError(), "RRSE",
						Double.toString(entry.getValue().getSettings().getValidationThreshold()));
				break;
			case "VALIDATION_SIZE":
				validationSize.addValue(entry.getValue().getMeanAbsoluteError(), "MAE",
						Double.toString(entry.getValue().getSettings().getValidationSize()));
				validationSize.addValue(entry.getValue().getRelativeAbsoluteError(), "RAE",
						Double.toString(entry.getValue().getSettings().getValidationSize()));
				validationSize.addValue(entry.getValue().getRootMeanSquaredError(), "RMSE",
						Double.toString(entry.getValue().getSettings().getValidationSize()));
				validationSize.addValue(entry.getValue().getRootRelativeSquaredError(), "RRSE",
						Double.toString(entry.getValue().getSettings().getValidationSize()));
				break;
			default:
				throw new IllegalArgumentException("Invalid selection ");
			}
		}

		mapDatasets.put("LearningRate", learningRate);
		mapDatasets.put("structure", structure);
		mapDatasets.put("TrainingIterations", trainingIterations);
		mapDatasets.put("Momentum", momentum);
		mapDatasets.put("validationThreshold", validationThreshold);
		mapDatasets.put("validationSize", validationSize);

		return mapDatasets;

	}

	private Map<String, DefaultCategoryDataset> createRegressionDatasets(Map<String, Result> meanResults) {

		Map<String, DefaultCategoryDataset> mapDatasets = new TreeMap<String, DefaultCategoryDataset>();

		DefaultCategoryDataset ridgeTestGreedy = new DefaultCategoryDataset();
		DefaultCategoryDataset ridgeTestM5 = new DefaultCategoryDataset();
		DefaultCategoryDataset ridgeTestNoAttribute = new DefaultCategoryDataset();
//		DefaultCategoryDataset standardNoAttribute = new DefaultCategoryDataset();
//		DefaultCategoryDataset standardM5 = new DefaultCategoryDataset();
//		DefaultCategoryDataset standardGreedy = new DefaultCategoryDataset();

		for (Map.Entry<String, Result> entry : meanResults.entrySet()) {
			String currentKey = entry.getKey().toUpperCase();
			currentKey = currentKey.replaceAll("\\d", "");
			System.out.println("Current Key " + currentKey);

			switch (currentKey) {
			case "RIDGE_TEST_GREEDY":
				ridgeTestGreedy.addValue(entry.getValue().getMeanAbsoluteError(), "MAE",
						Double.toString(entry.getValue().getRegressionSettings().getRidge()));
				ridgeTestGreedy.addValue(entry.getValue().getRelativeAbsoluteError(), "RAE",
						Double.toString(entry.getValue().getRegressionSettings().getRidge()));
				ridgeTestGreedy.addValue(entry.getValue().getRootMeanSquaredError(), "RMSE",
						Double.toString(entry.getValue().getRegressionSettings().getRidge()));
				ridgeTestGreedy.addValue(entry.getValue().getRootRelativeSquaredError(), "RRSE",
						Double.toString(entry.getValue().getRegressionSettings().getRidge()));
				break;
			case "RIDGE_TEST_M":
				ridgeTestM5.addValue(entry.getValue().getMeanAbsoluteError(), "MAE",
						Double.toString(entry.getValue().getRegressionSettings().getRidge()));
				ridgeTestM5.addValue(entry.getValue().getRelativeAbsoluteError(), "RAE",
						Double.toString(entry.getValue().getRegressionSettings().getRidge()));
				ridgeTestM5.addValue(entry.getValue().getRootMeanSquaredError(), "RMSE",
						Double.toString(entry.getValue().getRegressionSettings().getRidge()));
				ridgeTestM5.addValue(entry.getValue().getRootRelativeSquaredError(), "RRSE",
						Double.toString(entry.getValue().getRegressionSettings().getRidge()));
				break;
			case "RIDGE_TEST_NO_ATTRIBUTE":
				ridgeTestNoAttribute.addValue(entry.getValue().getMeanAbsoluteError(), "MAE",
						Double.toString(entry.getValue().getRegressionSettings().getRidge()));
				ridgeTestNoAttribute.addValue(entry.getValue().getRelativeAbsoluteError(), "RAE",
						Double.toString(entry.getValue().getRegressionSettings().getRidge()));
				ridgeTestNoAttribute.addValue(entry.getValue().getRootMeanSquaredError(), "RMSE",
						Double.toString(entry.getValue().getRegressionSettings().getRidge()));
				ridgeTestNoAttribute.addValue(entry.getValue().getRootRelativeSquaredError(), "RRSE",
						Double.toString(entry.getValue().getRegressionSettings().getRidge()));
				break;

			default:
				throw new IllegalArgumentException("Invalid selection ");

			}
		}

		mapDatasets.put("Ridge Test M5", ridgeTestM5);
		mapDatasets.put("ridge Test Greedy", ridgeTestGreedy);
		mapDatasets.put("Ridge Test: No Attribute", ridgeTestNoAttribute);

		return mapDatasets;

	}

	private void outputGraph(DefaultCategoryDataset dataset, String title, String subTitle, String rowTitle,
			String columnTitle) {

		ChartView window = new ChartView(title, subTitle, rowTitle, columnTitle, dataset);
		ChartView.initialize(window);

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
		reader.close();
		in.close();
		is.close();
		data.setClassIndex(classIndex);
		return data;
	}

//	private void mapToFile(Map<String, Result> map) {
//		try {
//			FileOutputStream fileOut = new FileOutputStream("/tmp/mean.txt");
//
//			ObjectOutputStream out = new ObjectOutputStream(fileOut);
//			out.writeObject(map);
//			out.close();
//			fileOut.close();
//		} catch (FileNotFoundException e) {
//
//			e.printStackTrace();
//		} catch (IOException e) {
//
//			e.printStackTrace();
//		}
//
//	}

	private Result calculateSumResult(ArrayList<Result> results, String algorithmType) {

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
		if (algorithmType == "MLP") {
			AdvancedSettings settings = null;
			Result sumResult = new Result(settings, correlationCoefficient, meanAbsoluteError, rootMeanSquaredError,
					relativeAbsoluteError, rootRelativeSquaredError, instances);
			return sumResult;
		} else if (algorithmType == "REGRESSION") {
			RegressionSetting settings = null;
			Result sumResult = new Result(settings, correlationCoefficient, meanAbsoluteError, rootMeanSquaredError,
					relativeAbsoluteError, rootRelativeSquaredError, instances);
			return sumResult;
		}

		return null;

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

}
