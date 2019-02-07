package view;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import controller.CSVFileReader;
import weka.core.Instances;

public class CSVView {


	private JFrame frame;
	private JTable tblOutput;
	
	File csvFile;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CSVView window = new CSVView();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public CSVView() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 600, 440);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{510, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{335, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 0.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		frame.getContentPane().setLayout(gridBagLayout);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		GridBagConstraints gbc_tabbedPane = new GridBagConstraints();
		gbc_tabbedPane.insets = new Insets(0, 0, 5, 5);
		gbc_tabbedPane.fill = GridBagConstraints.BOTH;
		gbc_tabbedPane.gridx = 0;
		gbc_tabbedPane.gridy = 0;
		frame.getContentPane().add(tabbedPane, gbc_tabbedPane);
		
		JPanel panel = new JPanel();
		tabbedPane.addTab("Preprocess", null, panel, null);
		panel.setLayout(new GridLayout(1, 0, 0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		panel.add(scrollPane);
		
		tblOutput = new JTable();
		scrollPane.setViewportView(tblOutput);
		
		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("Regression", null, panel_1, null);
		GridBagLayout gbl_panel_1 = new GridBagLayout();
		gbl_panel_1.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
		gbl_panel_1.rowHeights = new int[]{0, 0, 0};
		gbl_panel_1.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panel_1.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		panel_1.setLayout(gbl_panel_1);
		
		JLabel lblNewLabel = new JLabel("Display Settings/Variables for Regression");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.gridx = 6;
		gbc_lblNewLabel.gridy = 1;
		panel_1.add(lblNewLabel, gbc_lblNewLabel);
		
		JPanel panel_2 = new JPanel();
		tabbedPane.addTab("Neural Network", null, panel_2, null);
		GridBagLayout gbl_panel_2 = new GridBagLayout();
		gbl_panel_2.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
		gbl_panel_2.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_panel_2.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panel_2.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel_2.setLayout(gbl_panel_2);
		
		JLabel lblDisplaySettingsvariablesFor = new JLabel("Display Settings/variables for NN");
		GridBagConstraints gbc_lblDisplaySettingsvariablesFor = new GridBagConstraints();
		gbc_lblDisplaySettingsvariablesFor.insets = new Insets(0, 0, 5, 0);
		gbc_lblDisplaySettingsvariablesFor.gridx = 6;
		gbc_lblDisplaySettingsvariablesFor.gridy = 1;
		panel_2.add(lblDisplaySettingsvariablesFor, gbc_lblDisplaySettingsvariablesFor);
		
		JButton btnAdvancedSettings = new JButton("Advanced Settings");
		btnAdvancedSettings.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//Open new form for advanced MLP settings
				
				
			}
		});
		GridBagConstraints gbc_btnAdvancedSettings = new GridBagConstraints();
		gbc_btnAdvancedSettings.anchor = GridBagConstraints.ABOVE_BASELINE;
		gbc_btnAdvancedSettings.gridx = 6;
		gbc_btnAdvancedSettings.gridy = 10;
		panel_2.add(btnAdvancedSettings, gbc_btnAdvancedSettings);
		
		JButton btnLoadCSV = new JButton("Load File");
		btnLoadCSV.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				final JFileChooser fc = new JFileChooser();
				
				int returnVal = fc.showOpenDialog(frame);
				
				if(returnVal == JFileChooser.APPROVE_OPTION) {
					csvFile = fc.getSelectedFile();
					
					if(csvFile == null) {
						//error
					} else {
						CSVFileReader csvReader = new CSVFileReader();
						String extension = csvReader.getFileExtension(csvFile);
					//	System.out.println(extension);
						if(extension.equals(".csv")) {
						
							try {
								Instances instance = csvReader.readCSV(csvFile, "placeholder.arff");
								
								DefaultTableModel model = csvReader.getTableModel(csvFile);	
								
								tblOutput.setModel(model);
								
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						//	tblOutput.setModel(tableModel);
							
							
						} else {
							//error
						}
					}
				}				
			}
		});
		GridBagConstraints gbc_btnLoadCSV = new GridBagConstraints();
		gbc_btnLoadCSV.insets = new Insets(0, 0, 0, 5);
		gbc_btnLoadCSV.gridx = 0;
		gbc_btnLoadCSV.gridy = 1;
		frame.getContentPane().add(btnLoadCSV, gbc_btnLoadCSV);
	}


}
