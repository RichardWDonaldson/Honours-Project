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

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import controller.CSVFileReader;
import controller.MLP;
import weka.core.Instances;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.JComboBox;
import javax.swing.JRadioButton;

public class CSVView {


	private JFrame frame;
	private JTable tblOutput;
	
	Instances instance;
	
	File csvFile;
	File arffFile;

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
		tabbedPane.setBorder(new CompoundBorder());
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
		scrollPane.setViewportBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panel.add(scrollPane);
		
		tblOutput = new JTable();
		scrollPane.setViewportView(tblOutput);
		tblOutput.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		JPanel panel_2 = new JPanel();
		tabbedPane.addTab("Settings", null, panel_2, null);
		GridBagLayout gbl_panel_2 = new GridBagLayout();
		gbl_panel_2.columnWidths = new int[]{0, 0, 0, 0, 0};
		gbl_panel_2.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_panel_2.columnWeights = new double[]{0.0, 1.0, 1.0, 0.0, Double.MIN_VALUE};
		gbl_panel_2.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel_2.setLayout(gbl_panel_2);
		
		JLabel lblNewLabel = new JLabel("Algorithm");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 0;
		panel_2.add(lblNewLabel, gbc_lblNewLabel);
		
		JComboBox comboBox = new JComboBox();
		GridBagConstraints gbc_comboBox = new GridBagConstraints();
		gbc_comboBox.insets = new Insets(0, 0, 5, 5);
		gbc_comboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox.gridx = 1;
		gbc_comboBox.gridy = 0;
		panel_2.add(comboBox, gbc_comboBox);
		
		JLabel lblNewLabel_1 = new JLabel("Product");
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_1.gridx = 0;
		gbc_lblNewLabel_1.gridy = 1;
		panel_2.add(lblNewLabel_1, gbc_lblNewLabel_1);
		
		JComboBox comboBox_1 = new JComboBox();
		GridBagConstraints gbc_comboBox_1 = new GridBagConstraints();
		gbc_comboBox_1.insets = new Insets(0, 0, 5, 5);
		gbc_comboBox_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox_1.gridx = 1;
		gbc_comboBox_1.gridy = 1;
		panel_2.add(comboBox_1, gbc_comboBox_1);
		
		JRadioButton rdbtnTesting = new JRadioButton("Testing");
		GridBagConstraints gbc_rdbtnTesting = new GridBagConstraints();
		gbc_rdbtnTesting.insets = new Insets(0, 0, 5, 5);
		gbc_rdbtnTesting.gridx = 1;
		gbc_rdbtnTesting.gridy = 3;
		panel_2.add(rdbtnTesting, gbc_rdbtnTesting);
		
	
		
		JRadioButton rdbtnTraining = new JRadioButton("Training");
		GridBagConstraints gbc_rdbtnTraining = new GridBagConstraints();
		gbc_rdbtnTraining.insets = new Insets(0, 0, 5, 5);
		gbc_rdbtnTraining.gridx = 1;
		gbc_rdbtnTraining.gridy = 4;
		panel_2.add(rdbtnTraining, gbc_rdbtnTraining);
		
		JRadioButton rdbtnForecasting = new JRadioButton("Forecasting");
		GridBagConstraints gbc_rdbtnForecasting = new GridBagConstraints();
		gbc_rdbtnForecasting.fill = GridBagConstraints.VERTICAL;
		gbc_rdbtnForecasting.insets = new Insets(0, 0, 5, 5);
		gbc_rdbtnForecasting.gridx = 1;
		gbc_rdbtnForecasting.gridy = 5;
		panel_2.add(rdbtnForecasting, gbc_rdbtnForecasting);
		
		ButtonGroup group = new ButtonGroup();
		
		group.add(rdbtnForecasting);
		group.add(rdbtnTraining);
		group.add(rdbtnTesting);
		
		
		JButton btnAdvancedSettings = new JButton("Advanced Settings");
		btnAdvancedSettings.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//Open new form for advanced MLP settings
				
				
			}
		});
		GridBagConstraints gbc_btnAdvancedSettings = new GridBagConstraints();
		gbc_btnAdvancedSettings.insets = new Insets(0, 0, 5, 5);
		gbc_btnAdvancedSettings.anchor = GridBagConstraints.ABOVE_BASELINE;
		gbc_btnAdvancedSettings.gridx = 1;
		gbc_btnAdvancedSettings.gridy = 7;
		panel_2.add(btnAdvancedSettings, gbc_btnAdvancedSettings);
		
		
		
		JButton btnRun = new JButton("Run");
		btnRun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			//TODO Add advanced Settings check
				if(arffFile == null) {
					
					JOptionPane.showMessageDialog(frame, "arff File is null", "Error", JOptionPane.ERROR_MESSAGE);
					
				} else {
					
					MLP mlp = new MLP();
					
					try {
						mlp.singleBuildExample(arffFile);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						JOptionPane.showMessageDialog(frame, "MLP Stack Trace", "Error", JOptionPane.ERROR_MESSAGE);
						
					} 
					
				}

			}
		});
		GridBagConstraints gbc_btnRun = new GridBagConstraints();
		gbc_btnRun.insets = new Insets(0, 0, 0, 5);
		gbc_btnRun.anchor = GridBagConstraints.SOUTH;
		gbc_btnRun.gridx = 1;
		gbc_btnRun.gridy = 10;
		panel_2.add(btnRun, gbc_btnRun);
		
		JButton btnLoadCSV = new JButton("Load File");
		btnLoadCSV.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				final JFileChooser fc = new JFileChooser();
				
				int returnVal = fc.showOpenDialog(frame);
				
				if(returnVal == JFileChooser.APPROVE_OPTION) {
					csvFile = fc.getSelectedFile();
					
					if(csvFile == null) {
						//error
						JOptionPane.showMessageDialog(frame, "Error", "Error", JOptionPane.ERROR_MESSAGE);
						
					} else {
						CSVFileReader csvReader = new CSVFileReader();
						String extension = csvReader.getFileExtension(csvFile);
				
						if(extension.equals(".csv")) {
						
							try {
							csvReader.readCSV(csvFile, "placeholder.arff");
						
							arffFile = new File("placeholder.arff");	
							
								DefaultTableModel model = csvReader.getTableModel(csvFile);	
								
								tblOutput.setModel(model);
								
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
				
							
						} else {
							//error
							JOptionPane.showMessageDialog(frame, "Error", "Error", JOptionPane.ERROR_MESSAGE);
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
