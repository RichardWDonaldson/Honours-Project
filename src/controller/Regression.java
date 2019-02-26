package controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Random;

import model.Model;
import weka.classifiers.Classifier;
import weka.classifiers.functions.LinearRegression;
import weka.core.Instances;

public class Regression {

	Instances trainingInstances;
	Instances testInstances;

	Model model = new Model();


	public void simpleBuild(File arffFile, int classIndex, int iterations) {

		try {	

			for(int i = 1; i <= iterations; i++) {
				if(iterations > 1) {
					System.out.println("Iteration: " + i );
				}


				splitTrainingSet(arffFile, classIndex);

				System.out.println("Training " + trainingInstances.numInstances() + " Test " + testInstances.numInstances()
				+ "\r\n\r\nBuilding Classifier");

				Classifier classifier = new LinearRegression();

				classifier.buildClassifier(trainingInstances);

				model.saveEvaluation(testInstances, classifier);

			}

			model.outputResults();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}






	}





	public void advancedBuild() {




	}

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




}
