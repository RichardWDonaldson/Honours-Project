package model;

public class Result {
	
	
	
	
	
double correlationCoefficient;
double meanAbsoluteError;
double rootMeanSquredError;
double relativeAbsoluteError;
double rootRelativeSquredError;
double instances;
public Result(double correlationCoefficient, double meanAbsoluteError, double rootMeanSquredError,
		double relativeAbsoluteError, double rootRelativeSquredError, double instances) {
	super();
	this.correlationCoefficient = correlationCoefficient;
	this.meanAbsoluteError = meanAbsoluteError;
	this.rootMeanSquredError = rootMeanSquredError;
	this.relativeAbsoluteError = relativeAbsoluteError;
	this.rootRelativeSquredError = rootRelativeSquredError;
	this.instances = instances;
}
public double getCorrelationCoefficient() {
	return correlationCoefficient;
}
public void setCorrelationCoefficient(double correlationCoefficient) {
	this.correlationCoefficient = correlationCoefficient;
}
public double getMeanAbsoluteError() {
	return meanAbsoluteError;
}
public void setMeanAbsoluteError(double meanAbsoluteError) {
	this.meanAbsoluteError = meanAbsoluteError;
}
public double getRootMeanSquredError() {
	return rootMeanSquredError;
}
public void setRootMeanSquredError(double rootMeanSquredError) {
	this.rootMeanSquredError = rootMeanSquredError;
}
public double getRelativeAbsoluteError() {
	return relativeAbsoluteError;
}
public void setRelativeAbsoluteError(double relativeAbsoluteError) {
	this.relativeAbsoluteError = relativeAbsoluteError;
}
public double getRootRelativeSquredError() {
	return rootRelativeSquredError;
}
public void setRootRelativeSquredError(double rootRelativeSquredError) {
	this.rootRelativeSquredError = rootRelativeSquredError;
}
public double getInstances() {
	return instances;
}
public void setInstances(double instances) {
	this.instances = instances;
}



@Override
public String toString() {
	

//TODO Fix toString so it outputs in the format I want	
	//TODO output appropriate d.p.
	return "CorrelationCoefficient=\t\t" 
		+ correlationCoefficient 
		+ ", \nmeanAbsoluteError=\t\t" 
		+ meanAbsoluteError
		+ ", \nrootMeanSquredError=\t\t" 
		+ rootMeanSquredError 
		+ ", \nrelativeAbsoluteError=\t\t" 
			+ relativeAbsoluteError
			+ ", \nrootRelativeSquredError=\t\t" 
			+ rootRelativeSquredError 
			+ ", \ninstances=\t\t" 
			+ instances; 
			

	}


	
	

}
