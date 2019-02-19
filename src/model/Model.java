package model;

import java.util.ArrayList;
import java.util.List;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Instances;
public class Model {

public ArrayList<Evaluation> evaluations = new ArrayList<Evaluation>();
public ArrayList<Result> results = new ArrayList<Result>();	
 
double correlationCoefficient;
double meanAbsoluteError;
double rootMeanSquredError;
double relativeAbsoluteError;
double rootRelativeSquredError;
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
	
	
	 
 }
 
public void outputResults() {

	int count = results.size();
	//System.out.println("Count: " + count);
	
for(Result result : results) {
//	System.out.println("correlation: ");

	
	correlationCoefficient += result.getCorrelationCoefficient();
	//System.out.println("112 " + result.getCorrelationCoefficient());
	meanAbsoluteError += result.getMeanAbsoluteError();
	rootMeanSquredError += result.getRootMeanSquredError();
	relativeAbsoluteError += result.getRelativeAbsoluteError();
	rootRelativeSquredError += result.getRootRelativeSquredError();
	instances += result.getInstances();
	
	
	
}
correlationCoefficient /= count;
meanAbsoluteError  /= count;
rootMeanSquredError  /= count;
relativeAbsoluteError  /= count;
rootRelativeSquredError  /= count;
instances /= count;

Result meanResult = new Result(correlationCoefficient, meanAbsoluteError, rootMeanSquredError, relativeAbsoluteError, rootRelativeSquredError, instances);



System.out.println("Mean Results");	
System.out.println(meanResult.toString());




	
	
}
 

	
}
