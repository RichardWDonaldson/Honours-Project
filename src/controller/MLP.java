package controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Random;

import model.Model;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.core.Instances;
import weka.core.Utils;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;



//TODO Add model saving https://stackoverflow.com/questions/33556543/how-to-save-model-and-apply-it-on-a-test-dataset-on-java 
//TODO change console output to JTextArea https://stackoverflow.com/questions/4443878/redirecting-system-out-to-jtextpane 
public class MLP {

	Instances trainingInstances;
	Instances testInstances;

	Model model = new Model();

	public void simpleBuild(File arffFile, int classIndex, int iterations) throws Exception {


		for(int i = 1; i <= iterations; i++) {

			System.out.println("Iteration: " + i);


			splitTrainingSet(arffFile, classIndex);

			System.out.println("Training " + trainingInstances.numInstances() + " Test " + testInstances.numInstances()
			+ "\r\n\r\nBuilding Classifier");


			// build classifier on training data
			Classifier classifier = new MultilayerPerceptron();

			classifier.buildClassifier(trainingInstances);


			model.saveEvaluation(testInstances, classifier);

		}

		model.outputResults();

	}
	//iterations, learningRate, momentum, seed, structure, validationThreshold, validationSize, file, classIndex
	public void advancedBuild(double trainingIterations, double learningRate, double momentum, double seed, String structure,
			double validationThreshold, double validationSize, File arffFile, int classIndex, int iterations) {
		// -L learning rate
		// -M momentum
		// -N training iterations
		// - S seed
		// -H structure of network in form "h1,h2,..." where h1=neurons in hidden layer
		// 1, h2=neurons in hidden layer 2 etc
		// -V validation set size
		// -E validation threshold (ere dictates how many times in a row the validation
		// set error can get worse before training is terminated.)

		try {
			//TODO Optimise this

			int tempIterations = (int) trainingIterations;
			int tempSeeds = (int) seed;
			int validationSizeToInt = (int) validationSize;
			int validationThresholdToInt = (int) validationThreshold;

			String tempLearningRate = "-L " + Double.toString(learningRate);
			String tempMomentum = "-M " + Double.toString(momentum);
			String tempTrainingIterations = "-N " + Integer.toString(tempIterations);
			String tempSeed = "-S " + Integer.toString(tempSeeds);
			String tempStructure = "-H " + structure;
			String tempValidationSize = "-V " + Integer.toString(validationSizeToInt);
			String tempValidationThreshold = "-E " + Integer.toString(validationThresholdToInt);
			String tempSettings = tempLearningRate
					+ " " 
					+ tempMomentum
					+ " "
					+ tempTrainingIterations
					+ " "
					+ tempSeed
					+ " "
					+ tempStructure
					+ " "
					+ tempValidationSize
					+ " "
					+ tempValidationThreshold;



			for(int i = 1; i <= iterations; i++ ) {
				System.out.println("Iteration: " + i);
				splitTrainingSet(arffFile, classIndex);

				System.out.println("Training " + trainingInstances.numInstances() + " Test " + testInstances.numInstances()
				+ "\r\n\r\nBuilding Classifier");

				Classifier classifier = new MultilayerPerceptron();

				//	((MultilayerPerceptron) classifier).setOptions(Utils.splitOptions("-L 0.3 -M 0.2 -N 500 -V 0 -S 0 -E 20 -H 4,3"));

				System.out.println(tempSettings);
				((MultilayerPerceptron) classifier).setOptions(Utils.splitOptions(tempSettings));

				((MultilayerPerceptron) classifier).setNumDecimalPlaces(2);
				classifier.buildClassifier(trainingInstances);

				// evaluate on test data
				System.out.println("Evaluating Model on Test Set");
				Evaluation eval = new Evaluation(testInstances);

				eval.evaluateModel(classifier, testInstances);
				System.out.println(eval.toSummaryString("\nResults\n======\n", false));

				model.saveEvaluation(testInstances, classifier);

			}
			model.outputResults();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	//Move this to a shared controller
	private void splitTrainingSet(File arffFile, int classIndex) throws Exception {

		Instances data = getInstances(arffFile.getName(), classIndex);

		// randomise the data.
		// Set the seed to System.currentTimeMillis(); to give different seeds each time
		long seed = System.currentTimeMillis();
		Random rand = new Random(seed);
		data.randomize(rand);

		double trainingSetRatio = 0.66;
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
		// classIndex = data.numAttributes() - 1;
		System.out.println(classIndex);
		reader.close();
		in.close();
		is.close();
		data.setClassIndex(classIndex);
		return data;
	}


	// TODO Fix this if you want to display a confusion matrix, null pointer
	// exception
	// if you want the confusion matrix too
	//double[][] cmMatrix = eval.confusionMatrix();
	//
	// 
	//for(int row_i=0; row_i<cmMatrix.length; row_i++){
	//    for(int col_i=0; col_i<cmMatrix.length; col_i++){
	//        System.out.print(cmMatrix[row_i][col_i]);
	//        System.out.print("|");
	//    }
	//    System.out.println();
	//    System.out.println();
	//}



}
