package controller;

import java.io.File;
import java.util.List;
import weka.core.Instances;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.evaluation.NumericPrediction;
import weka.classifiers.timeseries.WekaForecaster;

public class Forecaster {
		
public void getForecast(File arffFile, String fieldsToForecast, int classIndex) {
	
	
try {
	
	Instances instance = MLP.getInstances(arffFile.getName(), classIndex);	
	
	
	WekaForecaster forecaster = new WekaForecaster();	
		
	forecaster.setFieldsToForecast(fieldsToForecast);
	
	forecaster.setBaseForecaster(new MultilayerPerceptron());
	
	//forecaster.getTSLagMaker().setTimeStampField("Date");
	//forecaster.getTSLagMaker().setMinLag(1);
	//forecaster.getTSLagMaker().setMaxLag(52);
	
	
	//forecaster.getTSLagMaker().setAddDayOfWeek(true);
	
	
	forecaster.buildForecaster(instance, System.out);
	
	forecaster.primeForecaster(instance);
	
	List<List<NumericPrediction>> forecast = forecaster.forecast(12, System.out);
	
	for(int i = 0; i < 12; i++) {
		List<NumericPrediction> predsAtStep = forecast.get(i);
		
		
			NumericPrediction predForTarget = predsAtStep.get(0);
			
			System.out.print("" + predForTarget.predicted() + " ");
		
		
		System.out.println();
	}

	
} catch (Exception e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}	
	
	
	
}
	
	
	

}
