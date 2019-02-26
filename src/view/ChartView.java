package view;

import java.util.ArrayList;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import model.Result;



public class ChartView extends ApplicationFrame {
	
	public ChartView(String applicationTitle, String chartTitle, String categoryAxisLabel, String valueAxisLabel, List<Result> results) {
		super(applicationTitle);
		
		JFreeChart lineChart = ChartFactory.createLineChart(
				chartTitle,
				categoryAxisLabel, 
				valueAxisLabel, 
				createDataset(results),
				PlotOrientation.VERTICAL,
				true, true, false);
		
		ChartPanel chartPanel = new ChartPanel(lineChart);
		chartPanel.setPreferredSize(new java.awt.Dimension( 560 , 367 ));
		setContentPane(chartPanel);
		
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	public void initialize(List<Result> results, ChartView window) {

		
		window.pack( );
	      RefineryUtilities.centerFrameOnScreen( window );
	      window.setVisible( true );
		
		
		
	}


private DefaultCategoryDataset createDataset(List<Result> results) {
	
	
	
	
	
	int i = 1;
	DefaultCategoryDataset dataset = new DefaultCategoryDataset();
	for(Result result : results) {
		
		dataset.addValue(result.getMeanAbsoluteError(), "Mean", Integer.toString(i));
		dataset.addValue(result.getRelativeAbsoluteError(), "Relative", Integer.toString(i));
		dataset.addValue(result.getRootMeanSquredError(), "Root Mean", Integer.toString(i));
		dataset.addValue(result.getRootRelativeSquredError(), "Root Relative", Integer.toString(i));
		
	i++;
	}
	

	return dataset;
}


}












