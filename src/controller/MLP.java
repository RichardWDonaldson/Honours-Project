package controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Random;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.core.Instances;
import weka.core.Utils;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;



public class MLP {
	
Instances trainingInstances;
Instances testInstances;
	
	
	
	
private void singleBuildExample() throws Exception {

/* if training set is supplied then run this
 * Instances trainingInstances = getInstances("ionosphereTrain.arff");
		Instances testInstances = getInstances("ionosphereTest.arff");
		 
		 	else
		 	
		 	run split Training set on File to be able to get both training and testing file
 */
	
	System.out.println("Training " + trainingInstances.numInstances() + " Test " + testInstances.numInstances() + "\r\n\r\nBuilding Classifier");
	
	//build classifier on training data
	Classifier classifier = new MultilayerPerceptron();
	
	// if you want to modify the default settings use the setOptions command as below
	
	// -L learning rate
	// -M momentum
	// -N training iterations
	// - S seed
	// -H  structure of network in form "h1,h2,..." where h1=neurons in hidden layer 1, h2=neurons in hidden layer 2 etc
	//  -V validation set size
	// -E validation threshold (ere dictates how many times in a row the validation set error can get worse before training is terminated.)
	
	//classifier.setOptions(Utils.splitOptions("-L 0.3 -M 0.2 -N 500 -V 0 -S 0 -E 20 -H "4,3"));

	classifier.buildClassifier(trainingInstances);
	
	//evaluate on test data
	System.out.println("Evaluating Model on Test Set");
	Evaluation eval = new Evaluation(testInstances); 

	eval.evaluateModel(classifier, testInstances);
	System.out.println(eval.toSummaryString("\nResults\n======\n", false));

	// if you want the confusion matrix too
	double[][] cmMatrix = eval.confusionMatrix();
	
     
    for(int row_i=0; row_i<cmMatrix.length; row_i++){
        for(int col_i=0; col_i<cmMatrix.length; col_i++){
            System.out.print(cmMatrix[row_i][col_i]);
            System.out.print("|");
        }
        System.out.println();
        System.out.println();
    }
	
    
	
	
	

	
}

private void splitTrainingSet() throws Exception {
	/* USE THIS CODE TO LOAD AN ARFF FILE THEN RANDOMLY SPLIT INTO
	 * TRAIN AND TEST SETS
	 
	// For arff files, you can use this method directly to load data
	// The method also sets the class attribute as the last attribute 		
	

	Instances data = getInstances("ionosphere.arff");
	
	//randomise the data. 
	// Set the seed to System.currentTimeMillis(); to give different seeds each time 
	long seed = 0;
	Random rand = new Random(seed);	
	data.randomize(rand);
	

	double trainingSetRatio = 0.66;	
	int dataSize = data.numInstances();
	int trainingSize = (int)Math.ceil(dataSize * trainingSetRatio);		
	int testSize = dataSize - trainingSize;

	//actual split
	Instances trainingInstances = new Instances(data, 0, trainingSize);
	Instances testInstances = new Instances(data, trainingSize, testSize);

	
	 END OF RANDOM SPLIT CODE 	*/
	
	
		
}

public static Instances getInstances(String filename) throws Exception{
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
	data.setClassIndex(data.numAttributes() - 1);
	return data;
}	




 





}
