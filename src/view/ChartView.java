package view;

import java.awt.Color;
import java.util.List;

import javax.swing.WindowConstants;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import model.Result;

public class ChartView extends ApplicationFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ChartView(String applicationTitle, String chartTitle, String categoryAxisLabel, String valueAxisLabel,
			List<Result> results) {
		super(applicationTitle);

		JFreeChart lineChart = ChartFactory.createLineChart(chartTitle, categoryAxisLabel, valueAxisLabel,
				standardDataset(results), PlotOrientation.VERTICAL, true, true, false);
		ChartPanel chartPanel = new ChartPanel(lineChart);
		chartPanel.setPreferredSize(new java.awt.Dimension(560, 367));
		setContentPane(chartPanel);

	}

	// constructor for evaluation
	public ChartView(String applicationTitle, String chartTitle, String categoryAxisLabel, String valueAxisLabel,
			DefaultCategoryDataset dataset) {
		super(applicationTitle);

		JFreeChart lineChart = ChartFactory.createLineChart(chartTitle, categoryAxisLabel, valueAxisLabel, dataset,
				PlotOrientation.VERTICAL, true, true, false);

		ChartPanel chartPanel = new ChartPanel(lineChart);
		chartPanel.setPreferredSize(new java.awt.Dimension(560, 367));
		setContentPane(chartPanel);

	}

	/**
	 * Initialize the contents of the frame.
	 */
	public static void initialize(ChartView window) {

		window.pack();
		RefineryUtilities.centerFrameOnScreen(window);
		window.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		window.setVisible(true);

	}

	public DefaultCategoryDataset standardDataset(List<Result> results) {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		int i = 1;

		for (Result result : results) {

			dataset.addValue(result.getMeanAbsoluteError(), "Mean", Integer.toString(i));
			dataset.addValue(result.getRelativeAbsoluteError(), "Relative", Integer.toString(i));
			dataset.addValue(result.getRootMeanSquaredError(), "Root Mean", Integer.toString(i));
			dataset.addValue(result.getRootRelativeSquaredError(), "Root Relative", Integer.toString(i));

			i++;

		}

		return dataset;

	}

}
