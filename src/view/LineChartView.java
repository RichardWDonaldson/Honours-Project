package view;

import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;


public class LineChartView extends ApplicationFrame {

	public LineChartView(String applicationTitle , String chartTitle ) {
	      super(applicationTitle);
	      JFreeChart lineChart = ChartFactory.createLineChart(
	    		  chartTitle, 
	    		  "Week", 
	    		  "Value", 
	    		  createDataset(), 
	    		  PlotOrientation.VERTICAL, 
	    		  true,true,false);
	      
	      ChartPanel chartPanel = new ChartPanel(lineChart);
	      chartPanel.setPreferredSize( new java.awt.Dimension( 560 , 367 ) );
	      setContentPane( chartPanel );
		
		
	}

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LineChartView window = new LineChartView("Title", "other title");
					window.pack( );
				      RefineryUtilities.centerFrameOnScreen( window );
				      window.setVisible( true );
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */


	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		List<Integer> scores = new ArrayList<Integer>();
//		
//		
//		
//		frame = new JFrame();
//		frame.setBounds(100, 100, 450, 300);
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		
//		
//		
//		
		
	}
	
	   private DefaultCategoryDataset createDataset( ) {
		      DefaultCategoryDataset dataset = new DefaultCategoryDataset( );
		      
		      
		      
		      dataset.addValue( 15 , "schools" , "1970" );
		      dataset.addValue( 30 , "schools" , "1980" );
		      dataset.addValue( 60 , "schools" ,  "1990" );
		      dataset.addValue( 120 , "schools" , "2000" );
		      dataset.addValue( 240 , "schools" , "2010" );
		      dataset.addValue( 300 , "schools" , "2014" );
		      dataset.addValue( 300 , "not schools" , "2014" );
		      
		  
		      return dataset;
		   }
	
	
	
	

}
