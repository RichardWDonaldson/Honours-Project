package view;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
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
import controller.Forecaster;
import controller.MLP;
import model.AdvancedSettings;
import weka.core.Instances;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.JComboBox;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.JCheckBox;

public class CSVView {

	
	
	
	private JFrame frame;
	private JTable tblOutput;
	private JTextField txtLearningRate;
	private JTextField txtMomentum;
	private JTextField txtTrainingIterations;
	private JTextField txtSeed;
	private JTextField txtStructure;
	private JTextField txtValidationThreshold;
	private JTextField txtValidationSize;
	
	Instances instance;
	
	File csvFile;
	File arffFile;
	
	int productChoice;
	int algorithmChoice;
	int advancedSettingsState;
	Object[] productsList;
	private JTextField txtIterations;

	
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
		frame.setBounds(100, 100, 815, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{215, 0};
		gridBagLayout.rowHeights = new int[]{335, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, 0.0, 1.0, Double.MIN_VALUE};
		frame.getContentPane().setLayout(gridBagLayout);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBorder(new CompoundBorder());
		GridBagConstraints gbc_tabbedPane = new GridBagConstraints();
		gbc_tabbedPane.insets = new Insets(0, 0, 5, 0);
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
		gbl_panel_2.columnWidths = new int[]{162, 0, 0, 423, 0};
		gbl_panel_2.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0};
		gbl_panel_2.columnWeights = new double[]{0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panel_2.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel_2.setLayout(gbl_panel_2);
		
		JLabel lblNewLabel = new JLabel("Algorithm");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 0;
		panel_2.add(lblNewLabel, gbc_lblNewLabel);
		
		JComboBox<?> cbAlgorithm = new JComboBox<Object>();
		cbAlgorithm.setModel(new DefaultComboBoxModel(new String[] {"Linear Regression", "Multi-Layer Perceptron"}));
		GridBagConstraints gbc_cbAlgorithm = new GridBagConstraints();
		gbc_cbAlgorithm.insets = new Insets(0, 0, 5, 5);
		gbc_cbAlgorithm.fill = GridBagConstraints.HORIZONTAL;
		gbc_cbAlgorithm.gridx = 1;
		gbc_cbAlgorithm.gridy = 0;
		panel_2.add(cbAlgorithm, gbc_cbAlgorithm);
		
		
		cbAlgorithm.addItemListener(new ItemListener() {
			
			public void itemStateChanged(ItemEvent e) {
				//Linear = 0, Multi = 1
				
			 algorithmChoice = cbAlgorithm.getSelectedIndex();
				
			}
			
		}
		);
		
		
		
		
		
		JLabel lblNewLabel_1 = new JLabel("Product");
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_1.gridx = 0;
		gbc_lblNewLabel_1.gridy = 1;
		panel_2.add(lblNewLabel_1, gbc_lblNewLabel_1);
				
				JComboBox<Object[]> cbProduct = new JComboBox<Object[]>();
				GridBagConstraints gbc_cbProduct = new GridBagConstraints();
				gbc_cbProduct.insets = new Insets(0, 0, 5, 5);
				gbc_cbProduct.fill = GridBagConstraints.HORIZONTAL;
				gbc_cbProduct.gridx = 1;
				gbc_cbProduct.gridy = 1;
				panel_2.add(cbProduct, gbc_cbProduct);
				
				JLabel lblNewLabel_9 = new JLabel("Iterations");
				GridBagConstraints gbc_lblNewLabel_9 = new GridBagConstraints();
				gbc_lblNewLabel_9.insets = new Insets(0, 0, 5, 5);
				gbc_lblNewLabel_9.anchor = GridBagConstraints.NORTHEAST;
				gbc_lblNewLabel_9.gridx = 0;
				gbc_lblNewLabel_9.gridy = 2;
				panel_2.add(lblNewLabel_9, gbc_lblNewLabel_9);
				
				txtIterations = new JTextField();
				GridBagConstraints gbc_txtIterations = new GridBagConstraints();
				gbc_txtIterations.insets = new Insets(0, 0, 5, 5);
				gbc_txtIterations.fill = GridBagConstraints.HORIZONTAL;
				gbc_txtIterations.gridx = 1;
				gbc_txtIterations.gridy = 2;
				panel_2.add(txtIterations, gbc_txtIterations);
				txtIterations.setColumns(10);
				
				JCheckBox chckbxAdvancedSettings = new JCheckBox("Advanced Settings");
				chckbxAdvancedSettings.setSelected(true);
				GridBagConstraints gbc_chckbxAdvancedSettings = new GridBagConstraints();
				gbc_chckbxAdvancedSettings.insets = new Insets(0, 0, 5, 5);
				gbc_chckbxAdvancedSettings.gridx = 0;
				gbc_chckbxAdvancedSettings.gridy = 3;
				panel_2.add(chckbxAdvancedSettings, gbc_chckbxAdvancedSettings);
				
				
				
				
				
				
				JPanel panel_1 = new JPanel();
				panel_1.setBorder(new TitledBorder(null, "Advanced Settings", TitledBorder.LEADING, TitledBorder.TOP, null, null));
				GridBagConstraints gbc_panel_1 = new GridBagConstraints();
				gbc_panel_1.fill = GridBagConstraints.BOTH;
				gbc_panel_1.gridwidth = 4;
				gbc_panel_1.insets = new Insets(0, 0, 5, 0);
				gbc_panel_1.gridx = 0;
				gbc_panel_1.gridy = 4;
				panel_2.add(panel_1, gbc_panel_1);
				GridBagLayout gbl_panel_1 = new GridBagLayout();
				gbl_panel_1.columnWidths = new int[]{0, 0, 0, 390, 0};
				gbl_panel_1.rowHeights = new int[]{0, 0, 0, 0, 0, 0};
				gbl_panel_1.columnWeights = new double[]{0.0, 1.0, 1.0, 1.0, Double.MIN_VALUE};
				gbl_panel_1.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
				panel_1.setLayout(gbl_panel_1);
				
				
				
				JLabel lblNewLabel_2 = new JLabel("Learning Rate");
				GridBagConstraints gbc_lblNewLabel_2 = new GridBagConstraints();
				gbc_lblNewLabel_2.anchor = GridBagConstraints.EAST;
				gbc_lblNewLabel_2.insets = new Insets(0, 0, 5, 5);
				gbc_lblNewLabel_2.gridx = 0;
				gbc_lblNewLabel_2.gridy = 1;
				panel_1.add(lblNewLabel_2, gbc_lblNewLabel_2);
				
				txtLearningRate = new JTextField();
				GridBagConstraints gbc_txtLearningRate = new GridBagConstraints();
				gbc_txtLearningRate.fill = GridBagConstraints.HORIZONTAL;
				gbc_txtLearningRate.insets = new Insets(0, 0, 5, 5);
				gbc_txtLearningRate.gridx = 1;
				gbc_txtLearningRate.gridy = 1;
				panel_1.add(txtLearningRate, gbc_txtLearningRate);
				txtLearningRate.setColumns(10);
				
				JLabel lblNewLabel_5 = new JLabel("Seed");
				GridBagConstraints gbc_lblNewLabel_5 = new GridBagConstraints();
				gbc_lblNewLabel_5.anchor = GridBagConstraints.EAST;
				gbc_lblNewLabel_5.insets = new Insets(0, 0, 5, 5);
				gbc_lblNewLabel_5.gridx = 2;
				gbc_lblNewLabel_5.gridy = 1;
				panel_1.add(lblNewLabel_5, gbc_lblNewLabel_5);
				
				txtSeed = new JTextField();
				GridBagConstraints gbc_txtSeed = new GridBagConstraints();
				gbc_txtSeed.fill = GridBagConstraints.HORIZONTAL;
				gbc_txtSeed.insets = new Insets(0, 0, 5, 0);
				gbc_txtSeed.gridx = 3;
				gbc_txtSeed.gridy = 1;
				panel_1.add(txtSeed, gbc_txtSeed);
				txtSeed.setColumns(10);
				
				JLabel lblNewLabel_3 = new JLabel("Momentum");
				GridBagConstraints gbc_lblNewLabel_3 = new GridBagConstraints();
				gbc_lblNewLabel_3.anchor = GridBagConstraints.EAST;
				gbc_lblNewLabel_3.insets = new Insets(0, 0, 5, 5);
				gbc_lblNewLabel_3.gridx = 0;
				gbc_lblNewLabel_3.gridy = 2;
				panel_1.add(lblNewLabel_3, gbc_lblNewLabel_3);
				
				txtMomentum = new JTextField();
				GridBagConstraints gbc_txtMomentum = new GridBagConstraints();
				gbc_txtMomentum.fill = GridBagConstraints.HORIZONTAL;
				gbc_txtMomentum.insets = new Insets(0, 0, 5, 5);
				gbc_txtMomentum.gridx = 1;
				gbc_txtMomentum.gridy = 2;
				panel_1.add(txtMomentum, gbc_txtMomentum);
				txtMomentum.setColumns(10);
				
				JLabel lblNewLabel_6 = new JLabel("Structure");
				GridBagConstraints gbc_lblNewLabel_6 = new GridBagConstraints();
				gbc_lblNewLabel_6.anchor = GridBagConstraints.EAST;
				gbc_lblNewLabel_6.insets = new Insets(0, 0, 5, 5);
				gbc_lblNewLabel_6.gridx = 2;
				gbc_lblNewLabel_6.gridy = 2;
				panel_1.add(lblNewLabel_6, gbc_lblNewLabel_6);
				
				txtStructure = new JTextField();
				GridBagConstraints gbc_txtStructure = new GridBagConstraints();
				gbc_txtStructure.fill = GridBagConstraints.HORIZONTAL;
				gbc_txtStructure.insets = new Insets(0, 0, 5, 0);
				gbc_txtStructure.gridx = 3;
				gbc_txtStructure.gridy = 2;
				panel_1.add(txtStructure, gbc_txtStructure);
				txtStructure.setColumns(10);
				
				JLabel lblNewLabel_4 = new JLabel("Training Iterations");
				GridBagConstraints gbc_lblNewLabel_4 = new GridBagConstraints();
				gbc_lblNewLabel_4.anchor = GridBagConstraints.EAST;
				gbc_lblNewLabel_4.insets = new Insets(0, 0, 5, 5);
				gbc_lblNewLabel_4.gridx = 0;
				gbc_lblNewLabel_4.gridy = 3;
				panel_1.add(lblNewLabel_4, gbc_lblNewLabel_4);
				
				txtTrainingIterations = new JTextField();
				GridBagConstraints gbc_txtTrainingIterations = new GridBagConstraints();
				gbc_txtTrainingIterations.fill = GridBagConstraints.HORIZONTAL;
				gbc_txtTrainingIterations.insets = new Insets(0, 0, 5, 5);
				gbc_txtTrainingIterations.gridx = 1;
				gbc_txtTrainingIterations.gridy = 3;
				panel_1.add(txtTrainingIterations, gbc_txtTrainingIterations);
				txtTrainingIterations.setColumns(10);
				
				JLabel lblNewLabel_7 = new JLabel("Validation Size");
				GridBagConstraints gbc_lblNewLabel_7 = new GridBagConstraints();
				gbc_lblNewLabel_7.anchor = GridBagConstraints.EAST;
				gbc_lblNewLabel_7.insets = new Insets(0, 0, 5, 5);
				gbc_lblNewLabel_7.gridx = 2;
				gbc_lblNewLabel_7.gridy = 3;
				panel_1.add(lblNewLabel_7, gbc_lblNewLabel_7);
				
				txtValidationSize = new JTextField();
				GridBagConstraints gbc_txtValidationSize = new GridBagConstraints();
				gbc_txtValidationSize.fill = GridBagConstraints.HORIZONTAL;
				gbc_txtValidationSize.insets = new Insets(0, 0, 5, 0);
				gbc_txtValidationSize.gridx = 3;
				gbc_txtValidationSize.gridy = 3;
				panel_1.add(txtValidationSize, gbc_txtValidationSize);
				txtValidationSize.setColumns(10);
				
				JLabel lblNewLabel_8 = new JLabel("Validation Threshold");
				GridBagConstraints gbc_lblNewLabel_8 = new GridBagConstraints();
				gbc_lblNewLabel_8.anchor = GridBagConstraints.EAST;
				gbc_lblNewLabel_8.insets = new Insets(0, 0, 0, 5);
				gbc_lblNewLabel_8.gridx = 2;
				gbc_lblNewLabel_8.gridy = 4;
				panel_1.add(lblNewLabel_8, gbc_lblNewLabel_8);
				
				txtValidationThreshold = new JTextField();
				GridBagConstraints gbc_txtValidationThreshold = new GridBagConstraints();
				gbc_txtValidationThreshold.fill = GridBagConstraints.HORIZONTAL;
				gbc_txtValidationThreshold.gridx = 3;
				gbc_txtValidationThreshold.gridy = 4;
				panel_1.add(txtValidationThreshold, gbc_txtValidationThreshold);
				txtValidationThreshold.setColumns(10);
				
				chckbxAdvancedSettings.addItemListener(new ItemListener() {
					public void itemStateChanged(ItemEvent e) {
						advancedSettingsState = e.getStateChange();
						if(e.getStateChange() == ItemEvent.SELECTED) {
							
							
						//	panel_1.setVisible(true);
							
							txtTrainingIterations.setEditable(true);
							txtLearningRate.setEditable(true);
							txtMomentum.setEditable(true);
							txtSeed.setEditable(true);
							txtStructure.setEditable(true);
							txtValidationThreshold.setEditable(true);
							txtValidationSize.setEditable(true);
							
							
							
						} else {
							txtTrainingIterations.setEditable(false);
							txtLearningRate.setEditable(false);
							txtMomentum.setEditable(false);
							txtSeed.setEditable(false);
							txtStructure.setEditable(false);
							txtValidationThreshold.setEditable(false);
							txtValidationSize.setEditable(false);
							
						}
					}
				});
				
				
				
				JButton btnRun = new JButton("Run");
				btnRun.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
				
						int iterations;
						String productName;
						
						
						if(arffFile == null) {
							
							JOptionPane.showMessageDialog(frame, "arff File is null", "Error", JOptionPane.ERROR_MESSAGE);
							
						} else {
							if(algorithmChoice == 0) {
								//LinearRegression
								
							} else if (algorithmChoice == 1) {
								//MLP
								
								MLP mlp = new MLP();
								Forecaster forecast = new Forecaster();
								
								try {
									
									productChoice = cbProduct.getSelectedIndex();
									productName = (String) productsList[productChoice];
									
									
									if(txtIterations.getText().isEmpty()) {
									 iterations = 1;
									} else {
										 iterations = Integer.parseInt(txtIterations.getText());
									}

								//	System.out.println("Chosen Product " + productChoice);
									if(productChoice == 0) {
										//Error - No product selected
										
									} else {
										
										if(advancedSettingsState == 2) {
											mlp.simpleBuild(arffFile, productChoice, iterations);	
											
											
										} else {
											
											advancedBuild(mlp, arffFile, productChoice, iterations);
											
					
										}
										
										
										
							//			weka.core.SerializationHelper.write("mlp.model", mlp);
										
										//Loading the model Classifier cls = (Classifier) weka.core.SerializationHelper.read("/some/where/j48.model"); 
										
							
									//	forecast.getForecast(arffFile, productName, productChoice);
										
										
										
									}
								
									

										} catch (Exception e) {
											
											e.printStackTrace();
											JOptionPane.showMessageDialog(frame, "MLP Stack Trace", "Error", JOptionPane.ERROR_MESSAGE);
											
										} 
										
								
								
							} else {
								//Error
							}

						}

					}
				});
				GridBagConstraints gbc_btnRun = new GridBagConstraints();
				gbc_btnRun.insets = new Insets(0, 0, 0, 5);
				gbc_btnRun.anchor = GridBagConstraints.SOUTH;
				gbc_btnRun.gridx = 2;
				gbc_btnRun.gridy = 5;
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
								productsList = (String[]) csvReader.getColumnNames();
								DefaultComboBoxModel cbModel = new DefaultComboBoxModel(productsList);
								
								
								
								
								tblOutput.setModel(model);
								cbProduct.setModel(cbModel);
							} catch (IOException e) {
								
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
		gbc_btnLoadCSV.insets = new Insets(0, 0, 5, 0);
		gbc_btnLoadCSV.gridx = 0;
		gbc_btnLoadCSV.gridy = 1;
		frame.getContentPane().add(btnLoadCSV, gbc_btnLoadCSV);
	}


	public void advancedBuild(MLP mlp, File arffFile, int classIndex, int iterations) {
		int trainingIterations = Integer.parseInt(txtTrainingIterations.getText());
		int learningRate = Integer.parseInt(txtLearningRate.getText());
		int momentum = Integer.parseInt(txtMomentum.getText());
		int seed = Integer.parseInt(txtSeed.getText());
		String structure = txtStructure.getText();
		int validationThreshold = Integer.parseInt(txtValidationThreshold.getText());
		int validationSize = Integer.parseInt(txtValidationSize.getText());
		
		
		
		
		
		//iterations, learningRate, momentum, seed, structure, validationThreshold, validationSize, file, classIndex
		mlp.advancedBuild(trainingIterations, learningRate, momentum, seed, structure, validationThreshold, validationSize, arffFile, classIndex, iterations);
		
		
		
	}


}
