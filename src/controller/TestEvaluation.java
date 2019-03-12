package controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import weka.filters.supervised.attribute.TSLagMaker;

public class TestEvaluation {

	Instances trainingInstances;
	Instances testInstances;

	Model model = new Model();

	public void mlpEvaluation(File arffFile, String fieldsToForecast, int classIndex, int iterations, int numberOfPredictions) {
		try {
			Map<String, Result> mapMeanResults = new TreeMap<String, Result>();
			Map<Integer, List<List<NumericPrediction>>> mapPredictions = new HashMap<Integer, List<List<NumericPrediction>>>();
			Map<String, AdvancedSettings> map = model.populateMLPEvaluation();

			for(Map.Entry<String, AdvancedSettings> entry : map.entrySet()) {
				int testNumber = 0;
				String outputType = entry.getKey();
				ArrayList<Result> results = new ArrayList<Result>();
				System.out.println("==" + outputType + " TEST==");

				for(int i = 0; i <= iterations; i++) {
					System.out.println("Iteration " + i);

					WekaForecaster forecaster = new WekaForecaster();
					MultilayerPerceptron classifier = new MultilayerPerceptron();

					splitTrainingSet(arffFile, classIndex);

					classifier.setHiddenLayers(entry.getValue().getStructure());
					classifier.setTrainingTime((int) entry.getValue().getIterations());
					classifier.setLearningRate(entry.getValue().getLearningRate());
					classifier.setValidationSetSize((int) entry.getValue().getValidationSize());
					classifier.setValidationThreshold((int) entry.getValue().getValidationThreshold());
					classifier.setMomentum(entry.getValue().getMomentum());
					classifier.setSeed((int) entry.getValue().getSeed());

					classifier.buildClassifier(trainingInstances);

					forecaster.setFieldsToForecast(fieldsToForecast);
					forecaster.setBaseForecaster(classifier);

					forecaster.buildForecaster(trainingInstances, System.out);
					forecaster.primeForecaster(trainingInstances);

					List<List<NumericPrediction>> forecast = forecaster.forecast(numberOfPredictions,  System.out);
					mapPredictions.put(i, forecast);

					for(int j = 0; j < numberOfPredictions; j++ ) {
						List<NumericPrediction> predsAtStep = forecast.get(j);

						NumericPrediction predForTarget = predsAtStep.get(0);

						System.out.println("" + predForTarget.predicted() + " \n");
					}

					System.out.println("Evaluating Model on Test Set");
					Result result = evaluateMLPModel(testInstances, classifier, entry.getValue(), outputType);

					results.add(result);

				}
				System.out.println("==" +"END OF " + outputType + " TEST==");
				Result meanResult = calculateMeanResult(results, entry.getValue(), null);

				String test = outputType + testNumber;

				System.out.println("Adding " + test);

				mapMeanResults.put(test, meanResult);
				testNumber++;	
			}
			System.out.println("Finished Evaluations");	

			System.out.println("Creating Datasets...");
			//THIS IS WHERE THE GRAPHS WILL BE GENERATED

			ArrayList<DefaultCategoryDataset> testingDatasets = new ArrayList<DefaultCategoryDataset>();


			testingDatasets = createMLPDatasets(mapMeanResults);
			System.out.println("Outputting Graphs");

			for(DefaultCategoryDataset dataset: testingDatasets) {

				outputGraph(dataset, "Title", "Subtitle", "Bottom Row", "Side Row");
			}


		} catch(Exception e) {
			e.printStackTrace();
		}

	}

	public void regressionEvaluation(File arffFile, String fieldsToForecast, int classIndex, int iterations, int numberOfPredictions) {
		try {
			Map<String, Result> mapMeanResults = new TreeMap<String, Result>();
			Map<Integer,List<List<NumericPrediction>>> mapPredictions = new HashMap<Integer,List<List<NumericPrediction>>>();
			Map<String, RegressionSetting> map = model.populateRegressionEvaluation();
			
			for(Map.Entry<String, RegressionSetting> entry : map.entrySet()) {
				int testNumber = 0;
				String outputType = entry.getKey();
				ArrayList<Result> results = new ArrayList<Result>();
				System.out.println("===" + outputType + " TEST===");
				
				for(int i = 1; i <= iterations; i++) {
					String tempRidge = " -R " + Double.toString(entry.getValue().getRidge());
					String tempDecimalPlaces = " -num-decimal-places " + Integer.toString(entry.getValue().getDecimalPlaces());
					String tempAttributeSelection = " -S " + Integer.toString(entry.getValue().getAttributeSelectionMethod());

					System.out.println("Iteration " + i);
					
					WekaForecaster forecaster = new WekaForecaster();
					LinearRegression classifier = new LinearRegression();
					
					splitTrainingSet(arffFile, classIndex);
					
					((LinearRegression) classifier).setOptions(Utils.splitOptions(tempAttributeSelection + tempRidge + tempDecimalPlaces));
					
					classifier.buildClassifier(trainingInstances);
					
					forecaster.setFieldsToForecast(fieldsToForecast);
					forecaster.setBaseForecaster(classifier);
					
					forecaster.buildForecaster(trainingInstances, System.out);
					forecaster.primeForecaster(trainingInstances);
					
					List<List<NumericPrediction>> forecast = forecaster.forecast(numberOfPredictions,  System.out);
					mapPredictions.put(i, forecast);

					for(int j = 0; j < numberOfPredictions; j++ ) {
						List<NumericPrediction> predsAtStep = forecast.get(j);

						NumericPrediction predForTarget = predsAtStep.get(0);

						System.out.println("" + predForTarget.predicted() + " \n");
					}
					
					System.out.println("Evaluating Model on Test Set");
					Result result = evaluateRegressionModel(testInstances, classifier, entry.getValue(),outputType);
					
					results.add(result);
					
				}
				System.out.println("== END OF " + outputType + " TEST===");
				Result meanResult = calculateMeanResult(results,null,  entry.getValue());
				
				String test = outputType + testNumber;
				
				System.out.println("Adding " + test);
				
				mapMeanResults.put(test, meanResult);
				testNumber++;
				
				
				
			}
			System.out.println("Finished Evaluations");
			System.out.println("Creating Datasets...");
			
			ArrayList<DefaultCategoryDataset> testingDatasets = new ArrayList<DefaultCategoryDataset>();
			
			testingDatasets = createRegressionDatasets(mapMeanResults);
			System.out.println("Outputting Graphs");
			
			for(DefaultCategoryDataset dataset: testingDatasets) {
				//TODO Fix this
				outputGraph(dataset, "Title", "subTitle", "rowTitle", "columnTitle");
			}

		}catch(Exception e) {
			e.printStackTrace();
		}
	
	}


	public Result evaluateMLPModel(Instances testInstances, Classifier classifier, AdvancedSettings settings, String outputType) {
		System.out.println("Evaluating Model on Test Set");
		try {
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

			System.out.println(eval.toSummaryString("\nResults\n======\n", false));
			return result;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}




	}
	
	public Result evaluateRegressionModel(Instances testInstances, Classifier classifier, RegressionSetting settings, String outputType) {
		System.out.println("Evaluating Model on Test Set");
		
		try {
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
			
			System.out.println(eval.toSummaryString("\nResults\n======\n", false));
			return result;
			
			
			
		} catch (Exception e) {
		e.printStackTrace();
		return null;
		}
		
		
		
	}
	
	

	public Result calculateMeanResult(ArrayList<Result> results, AdvancedSettings settings, RegressionSetting regressionSettings) {
		double correlationCoefficient = 0;
		double meanAbsoluteError = 0;
		double rootMeanSquaredError = 0;
		double relativeAbsoluteError = 0;
		double rootRelativeSquaredError = 0;
		double instances = 0;
		int count = results.size();

		for(Result result : results) {

			correlationCoefficient += result.getCorrelationCoefficient();
			meanAbsoluteError += result.getMeanAbsoluteError();
			rootMeanSquaredError += result.getRootMeanSquaredError();
			relativeAbsoluteError += result.getRelativeAbsoluteError();
			rootRelativeSquaredError += result.getRootRelativeSquaredError();
			instances += result.getInstances();

		}

		correlationCoefficient /= count;
		meanAbsoluteError  /= count;
		rootMeanSquaredError  /= count;
		relativeAbsoluteError  /= count;
		rootRelativeSquaredError  /= count;
		instances /= count;

		
		if(regressionSettings == null) {
			Result meanResult = new Result(settings, correlationCoefficient, meanAbsoluteError, rootMeanSquaredError, relativeAbsoluteError, rootRelativeSquaredError, instances);
			return meanResult;
		} else if (settings == null) {
			Result meanResult = new Result(regressionSettings, correlationCoefficient, meanAbsoluteError, rootMeanSquaredError, relativeAbsoluteError, rootRelativeSquaredError, instances);
			return meanResult;
		}
		
		
		return null;
		


	}

	public ArrayList<DefaultCategoryDataset> createMLPDatasets(Map<String, Result> meanResults) {

		
		
		
		ArrayList<DefaultCategoryDataset> testingDatasets = new ArrayList<DefaultCategoryDataset>();
		DefaultCategoryDataset standard = new DefaultCategoryDataset();
		DefaultCategoryDataset structure = new DefaultCategoryDataset();
		DefaultCategoryDataset trainingIterations = new DefaultCategoryDataset();
		DefaultCategoryDataset learningRate = new DefaultCategoryDataset();
		DefaultCategoryDataset momentum = new DefaultCategoryDataset();
		DefaultCategoryDataset validationThreshold = new DefaultCategoryDataset();
		DefaultCategoryDataset validationSize = new DefaultCategoryDataset();

		for(Map.Entry<String, Result> entry: meanResults.entrySet()) {

			String currentKey = entry.getKey().toUpperCase();
			currentKey = currentKey.replaceAll("\\d","");
			System.out.println("Current Key " + currentKey);

			switch(currentKey) {

			case "STRUCTURE":
				structure.addValue(entry.getValue().getMeanAbsoluteError(), "MAE", entry.getValue().getSettings().getStructure());
				structure.addValue(entry.getValue().getRelativeAbsoluteError(), "RAE", entry.getValue().getSettings().getStructure());
				structure.addValue(entry.getValue().getRootMeanSquaredError(), "RMSE", entry.getValue().getSettings().getStructure());
				structure.addValue(entry.getValue().getRootRelativeSquaredError(), "RRSE", entry.getValue().getSettings().getStructure());
				break;
			case "TRAINING_ITERATIONS":
				trainingIterations.addValue(entry.getValue().getMeanAbsoluteError(), "MAE", Double.toString(entry.getValue().getSettings().getIterations()));
				trainingIterations.addValue(entry.getValue().getRelativeAbsoluteError(), "RAE", Double.toString(entry.getValue().getSettings().getIterations()));
				trainingIterations.addValue(entry.getValue().getRootMeanSquaredError(), "RMSE", Double.toString(entry.getValue().getSettings().getIterations()));
				trainingIterations.addValue(entry.getValue().getRootRelativeSquaredError(), "RRSE", Double.toString(entry.getValue().getSettings().getIterations()));
				break;
			case "LEARNING_RATE":
				learningRate.addValue(entry.getValue().getMeanAbsoluteError(), "MAE", Double.toString(entry.getValue().getSettings().getLearningRate()));
				learningRate.addValue(entry.getValue().getRelativeAbsoluteError(), "RAE", Double.toString(entry.getValue().getSettings().getLearningRate()));
				learningRate.addValue(entry.getValue().getRootMeanSquaredError(), "RMSE", Double.toString(entry.getValue().getSettings().getLearningRate()));
				learningRate.addValue(entry.getValue().getRootRelativeSquaredError(), "RRSE", Double.toString(entry.getValue().getSettings().getLearningRate()));
				break;
			case "MOMENTUM":
				momentum.addValue(entry.getValue().getMeanAbsoluteError(), "MAE", Double.toString(entry.getValue().getSettings().getMomentum()));
				momentum.addValue(entry.getValue().getRelativeAbsoluteError(), "RAE", Double.toString(entry.getValue().getSettings().getMomentum()));
				momentum.addValue(entry.getValue().getRootMeanSquaredError(), "RMSE", Double.toString(entry.getValue().getSettings().getMomentum()));
				momentum.addValue(entry.getValue().getRootRelativeSquaredError(), "RRSE", Double.toString(entry.getValue().getSettings().getMomentum()));
				break;
			case "VALIDATION_THRESHOLD":
				validationThreshold.addValue(entry.getValue().getMeanAbsoluteError(), "MAE", Double.toString(entry.getValue().getSettings().getValidationThreshold()));
				validationThreshold.addValue(entry.getValue().getRelativeAbsoluteError(), "RAE", Double.toString(entry.getValue().getSettings().getValidationThreshold()));
				validationThreshold.addValue(entry.getValue().getRootMeanSquaredError(), "RMSE", Double.toString(entry.getValue().getSettings().getValidationThreshold()));
				validationThreshold.addValue(entry.getValue().getRootRelativeSquaredError(), "RRSE", Double.toString(entry.getValue().getSettings().getValidationThreshold()));
				break;
			case "VALIDATION_SIZE":
				validationSize.addValue(entry.getValue().getMeanAbsoluteError(), "MAE", Double.toString(entry.getValue().getSettings().getValidationSize()));
				validationSize.addValue(entry.getValue().getRelativeAbsoluteError(), "RAE", Double.toString(entry.getValue().getSettings().getValidationSize()));
				validationSize.addValue(entry.getValue().getRootMeanSquaredError(), "RMSE", Double.toString(entry.getValue().getSettings().getValidationSize()));
				validationSize.addValue(entry.getValue().getRootRelativeSquaredError(), "RRSE", Double.toString(entry.getValue().getSettings().getValidationSize()));
				break;
			default:
				throw new IllegalArgumentException("Invalid selection " );
			}
		}

		testingDatasets.add(learningRate);
		testingDatasets.add(structure);
		testingDatasets.add(trainingIterations);
		testingDatasets.add(momentum);
		testingDatasets.add(validationThreshold);
		testingDatasets.add(validationSize);

		return testingDatasets;

	}
	
	public ArrayList<DefaultCategoryDataset> createRegressionDatasets(Map<String, Result> meanResults) {
		
		ArrayList<DefaultCategoryDataset> testingDatasets = new ArrayList<DefaultCategoryDataset>();
		DefaultCategoryDataset ridgeTestGreedy = new DefaultCategoryDataset();
		DefaultCategoryDataset ridgeTestM5 = new DefaultCategoryDataset();
		DefaultCategoryDataset ridgeTestNoAttribute = new DefaultCategoryDataset();
		DefaultCategoryDataset standardNoAttribute = new DefaultCategoryDataset();
		DefaultCategoryDataset standardM5 = new DefaultCategoryDataset(); 
		DefaultCategoryDataset standardGreedy = new DefaultCategoryDataset();
		
		
		
		for(Map.Entry<String, Result> entry: meanResults.entrySet()) { 
			String currentKey = entry.getKey().toUpperCase();
			currentKey = currentKey.replaceAll("\\d","");
			System.out.println("Current Key " + currentKey);
			
			switch(currentKey) {
			case "RIDGE_TEST_GREEDY":
				ridgeTestGreedy.addValue(entry.getValue().getMeanAbsoluteError(), "MAE", Double.toString(entry.getValue().getRegressionSettings().getRidge()));
				ridgeTestGreedy.addValue(entry.getValue().getRelativeAbsoluteError(), "RAE", Double.toString(entry.getValue().getRegressionSettings().getRidge()));
				ridgeTestGreedy.addValue(entry.getValue().getRootMeanSquaredError(), "RMSE", Double.toString(entry.getValue().getRegressionSettings().getRidge()));
				ridgeTestGreedy.addValue(entry.getValue().getRootRelativeSquaredError(), "RRSE", Double.toString(entry.getValue().getRegressionSettings().getRidge()));
				break;
			case "RIDGE_TEST_M":
				ridgeTestM5.addValue(entry.getValue().getMeanAbsoluteError(), "MAE", Double.toString(entry.getValue().getRegressionSettings().getRidge()));
				ridgeTestM5.addValue(entry.getValue().getRelativeAbsoluteError(), "RAE", Double.toString(entry.getValue().getRegressionSettings().getRidge()));
				ridgeTestM5.addValue(entry.getValue().getRootMeanSquaredError(), "RMSE", Double.toString(entry.getValue().getRegressionSettings().getRidge()));
				ridgeTestM5.addValue(entry.getValue().getRootRelativeSquaredError(), "RRSE", Double.toString(entry.getValue().getRegressionSettings().getRidge()));
				break;
			case "RIDGE_TEST_NO_ATTRIBUTE":
				ridgeTestNoAttribute.addValue(entry.getValue().getMeanAbsoluteError(), "MAE", Double.toString(entry.getValue().getRegressionSettings().getRidge()));
				ridgeTestNoAttribute.addValue(entry.getValue().getRelativeAbsoluteError(), "RAE", Double.toString(entry.getValue().getRegressionSettings().getRidge()));
				ridgeTestNoAttribute.addValue(entry.getValue().getRootMeanSquaredError(), "RMSE", Double.toString(entry.getValue().getRegressionSettings().getRidge()));
				ridgeTestNoAttribute.addValue(entry.getValue().getRootRelativeSquaredError(), "RRSE", Double.toString(entry.getValue().getRegressionSettings().getRidge()));
				break;

			default: throw new IllegalArgumentException("Invalid selection " );
				
			}
		}

		testingDatasets.add(ridgeTestM5);
		testingDatasets.add(ridgeTestGreedy);
		testingDatasets.add(ridgeTestNoAttribute);
		
		
		
	return testingDatasets;
		
		
	}
	
	



	public void outputGraph(DefaultCategoryDataset dataset, String title, String subTitle, String rowTitle, String columnTitle) {

		ChartView window = new ChartView(title, subTitle, rowTitle, columnTitle, dataset);
		window.initialize(window);

	}

	public void splitTrainingSet(File arffFile, int classIndex) throws Exception {

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

	public static Instances getInstances(String filename, int classIndex) throws Exception {
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


}







