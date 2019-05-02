package view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.GridBagLayout;
import javax.swing.JTabbedPane;
import java.awt.GridBagConstraints;
import javax.swing.JPanel;
import javax.swing.JTable;
import java.awt.Insets;
import java.awt.CardLayout;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JRadioButton;
import javax.swing.JCheckBox;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;

import controller.CSVFileReader;
import controller.Forecaster;
import controller.TestEvaluation;
import model.AdvancedSettings;
import model.Model;
import model.RegressionSetting;
import weka.classifiers.Classifier;

import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.awt.event.ActionEvent;

public class HomeView {
	// TODO change console output to JTextArea
	// https://stackoverflow.com/questions/4443878/redirecting-system-out-to-jtextpane
	private JFrame frmHonoursProject;
	private JTextField txtTrainingIterations;
	private JTextField txtStructure;
	private JTextField txtDecimalPlaces;

	Object[] productsList;
	DefaultTableModel model;
	DefaultComboBoxModel cbModel;
	File csvFile;
	File arffFile;
	Model mainModel = new Model();
	int algorithmChoice = 0;
	int advancedSettingsState;
	int attributeSelection;

	int numberOfPredictions;

	private JTextField txtLearningRate;
	private JTextField txtMomentum;
	private JTextField txtValidationSize;
	private JTextField txtValidationThreshold;
	private JTextField txtSeed;
	private JTextField txtRidge;
	private JTable tblOutput;
	private JTextField txtIterations;

	private ArrayList<JTextField> mlpInput = new ArrayList<JTextField>();
	private ArrayList<JTextField> regressionInput = new ArrayList<JTextField>();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					HomeView window = new HomeView();
					window.frmHonoursProject.setVisible(true);

					// ConsoleView console = new ConsoleView();
					// console.main();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public HomeView() {
		initialize();
		generateInputArrays();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmHonoursProject = new JFrame();
		frmHonoursProject.setTitle("Honours Project");
		frmHonoursProject.setBounds(100, 100, 784, 554);
		frmHonoursProject.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0 };
		gridBagLayout.rowHeights = new int[] { 434, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 1.0, 1.0, Double.MIN_VALUE };
		frmHonoursProject.getContentPane().setLayout(gridBagLayout);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		GridBagConstraints gbc_tabbedPane = new GridBagConstraints();
		gbc_tabbedPane.insets = new Insets(0, 0, 5, 0);
		gbc_tabbedPane.fill = GridBagConstraints.BOTH;
		gbc_tabbedPane.gridx = 0;
		gbc_tabbedPane.gridy = 0;
		frmHonoursProject.getContentPane().add(tabbedPane, gbc_tabbedPane);

		JPanel tablePanel = new JPanel();
		tabbedPane.addTab("Pre-Process", null, tablePanel, null);
		GridBagLayout gbl_tablePanel = new GridBagLayout();
		gbl_tablePanel.columnWidths = new int[] { 0, 0 };
		gbl_tablePanel.rowHeights = new int[] { 0, 0 };
		gbl_tablePanel.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gbl_tablePanel.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
		tablePanel.setLayout(gbl_tablePanel);

		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 0;
		tablePanel.add(scrollPane, gbc_scrollPane);

		tblOutput = new JTable();
		scrollPane.setViewportView(tblOutput);

		JPanel settingsPanel = new JPanel();
		tabbedPane.addTab("Settings", null, settingsPanel, null);
		GridBagLayout gbl_settingsPanel = new GridBagLayout();
		gbl_settingsPanel.columnWidths = new int[] { 0, 0 };
		gbl_settingsPanel.rowHeights = new int[] { 201, 0, 0 };
		gbl_settingsPanel.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gbl_settingsPanel.rowWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
		settingsPanel.setLayout(gbl_settingsPanel);

		JPanel mainSettingsPanel = new JPanel();
		GridBagConstraints gbc_mainSettingsPanel = new GridBagConstraints();
		gbc_mainSettingsPanel.insets = new Insets(0, 0, 5, 0);
		gbc_mainSettingsPanel.fill = GridBagConstraints.BOTH;
		gbc_mainSettingsPanel.gridx = 0;
		gbc_mainSettingsPanel.gridy = 0;
		settingsPanel.add(mainSettingsPanel, gbc_mainSettingsPanel);
		GridBagLayout gbl_mainSettingsPanel = new GridBagLayout();
		gbl_mainSettingsPanel.columnWidths = new int[] { 0, 0, 0, 0, 0 };
		gbl_mainSettingsPanel.rowHeights = new int[] { 0, 0, 0, 0, 0, 0 };
		gbl_mainSettingsPanel.columnWeights = new double[] { 0.0, 1.0, 0.0, 1.0, Double.MIN_VALUE };
		gbl_mainSettingsPanel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		mainSettingsPanel.setLayout(gbl_mainSettingsPanel);

		JLabel lblNewLabel_4 = new JLabel("Algorithm");
		GridBagConstraints gbc_lblNewLabel_4 = new GridBagConstraints();
		gbc_lblNewLabel_4.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_4.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_4.gridx = 0;
		gbc_lblNewLabel_4.gridy = 0;
		mainSettingsPanel.add(lblNewLabel_4, gbc_lblNewLabel_4);

		JComboBox cbAlgorithm = new JComboBox();
		cbAlgorithm.setModel(new DefaultComboBoxModel(new String[] { "Linear Regression", "MLP" }));
		GridBagConstraints gbc_cbAlgorithm = new GridBagConstraints();
		gbc_cbAlgorithm.insets = new Insets(0, 0, 5, 5);
		gbc_cbAlgorithm.fill = GridBagConstraints.HORIZONTAL;
		gbc_cbAlgorithm.gridx = 1;
		gbc_cbAlgorithm.gridy = 0;
		mainSettingsPanel.add(cbAlgorithm, gbc_cbAlgorithm);

		JPanel panel_4 = new JPanel();
		GridBagConstraints gbc_panel_4 = new GridBagConstraints();
		gbc_panel_4.fill = GridBagConstraints.BOTH;
		gbc_panel_4.gridheight = 2;
		gbc_panel_4.insets = new Insets(0, 0, 5, 0);
		gbc_panel_4.gridx = 3;
		gbc_panel_4.gridy = 0;
		mainSettingsPanel.add(panel_4, gbc_panel_4);
		GridBagLayout gbl_panel_4 = new GridBagLayout();
		gbl_panel_4.columnWidths = new int[] { 0, 0 };
		gbl_panel_4.rowHeights = new int[] { 0, 0, 0, 0 };
		gbl_panel_4.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gbl_panel_4.rowWeights = new double[] { 0.0, 0.0, 0.0, Double.MIN_VALUE };
		panel_4.setLayout(gbl_panel_4);

		JRadioButton rdbtnForecasting = new JRadioButton("Forecasting");
		GridBagConstraints gbc_rdbtnForecasting = new GridBagConstraints();
		gbc_rdbtnForecasting.insets = new Insets(0, 0, 5, 0);
		gbc_rdbtnForecasting.gridx = 0;
		gbc_rdbtnForecasting.gridy = 1;
		panel_4.add(rdbtnForecasting, gbc_rdbtnForecasting);

		JRadioButton rdbtnEvaluation = new JRadioButton("Evaluation");
		GridBagConstraints gbc_rdbtnEvaluation = new GridBagConstraints();
		gbc_rdbtnEvaluation.gridx = 0;
		gbc_rdbtnEvaluation.gridy = 2;
		panel_4.add(rdbtnEvaluation, gbc_rdbtnEvaluation);

		JLabel lblNewLabel_5 = new JLabel("Product");
		GridBagConstraints gbc_lblNewLabel_5 = new GridBagConstraints();
		gbc_lblNewLabel_5.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_5.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_5.gridx = 0;
		gbc_lblNewLabel_5.gridy = 1;
		mainSettingsPanel.add(lblNewLabel_5, gbc_lblNewLabel_5);

		JComboBox cbProduct = new JComboBox();

		GridBagConstraints gbc_cbProduct = new GridBagConstraints();
		gbc_cbProduct.insets = new Insets(0, 0, 5, 5);
		gbc_cbProduct.fill = GridBagConstraints.HORIZONTAL;
		gbc_cbProduct.gridx = 1;
		gbc_cbProduct.gridy = 1;
		mainSettingsPanel.add(cbProduct, gbc_cbProduct);

		JLabel Iterations = new JLabel("Iterations");
		GridBagConstraints gbc_Iterations = new GridBagConstraints();
		gbc_Iterations.insets = new Insets(0, 0, 5, 5);
		gbc_Iterations.anchor = GridBagConstraints.EAST;
		gbc_Iterations.gridx = 0;
		gbc_Iterations.gridy = 2;
		mainSettingsPanel.add(Iterations, gbc_Iterations);

		txtIterations = new JTextField();
		GridBagConstraints gbc_txtIterations = new GridBagConstraints();
		gbc_txtIterations.insets = new Insets(0, 0, 5, 5);
		gbc_txtIterations.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtIterations.gridx = 1;
		gbc_txtIterations.gridy = 2;
		mainSettingsPanel.add(txtIterations, gbc_txtIterations);
		txtIterations.setColumns(10);

		JCheckBox chckbxAdvancedSettings = new JCheckBox("Advanced Settings");
		chckbxAdvancedSettings.setSelected(true);
		GridBagConstraints gbc_chckbxAdvancedSettings = new GridBagConstraints();
		gbc_chckbxAdvancedSettings.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxAdvancedSettings.gridx = 0;
		gbc_chckbxAdvancedSettings.gridy = 3;
		mainSettingsPanel.add(chckbxAdvancedSettings, gbc_chckbxAdvancedSettings);

		ButtonGroup group = new ButtonGroup();
		rdbtnForecasting.setActionCommand("2");
		rdbtnEvaluation.setActionCommand("3");
		group.add(rdbtnForecasting);
		group.add(rdbtnEvaluation);

		JButton btnRun = new JButton("Run");
		btnRun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				String selectedOption = group.getSelection().getActionCommand();
				int iterations;
				String productName;

				if (txtIterations.getText().isEmpty()) {
					iterations = 1;
				} else {
					iterations = Integer.parseInt(txtIterations.getText());
				}

				if (arffFile != null) {
					int productChoice = cbProduct.getSelectedIndex();
					productName = (String) productsList[productChoice];

					if (productChoice != 0) {

						try {
							run(selectedOption, productName, iterations, productChoice, productsList);
						} catch (Exception e) {

							e.printStackTrace();
						}
					} else {
						// No product selected
						JOptionPane.showMessageDialog(frmHonoursProject, "please select a product", "Error",
								JOptionPane.ERROR_MESSAGE);
					}

				} else {
					// arffFile is null
					JOptionPane.showMessageDialog(frmHonoursProject, "arff File is null", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		GridBagConstraints gbc_btnRun = new GridBagConstraints();
		gbc_btnRun.insets = new Insets(0, 0, 0, 5);
		gbc_btnRun.gridx = 2;
		gbc_btnRun.gridy = 4;
		mainSettingsPanel.add(btnRun, gbc_btnRun);

		JPanel cardPanel = new JPanel(new CardLayout());
		GridBagConstraints gbc_cardPanel = new GridBagConstraints();
		gbc_cardPanel.fill = GridBagConstraints.BOTH;
		gbc_cardPanel.gridx = 0;
		gbc_cardPanel.gridy = 1;
		settingsPanel.add(cardPanel, gbc_cardPanel);
		cardPanel.setLayout(new CardLayout(0, 0));

		JPanel mlpCard = new JPanel();
		// cardPanel.add(mlpCard, "name_696316107003312");
		GridBagLayout gbl_mlpCard = new GridBagLayout();
		gbl_mlpCard.columnWidths = new int[] { 0, 0, 0, 0, 0 };
		gbl_mlpCard.rowHeights = new int[] { 0, 0, 0, 0, 0, 0 };
		gbl_mlpCard.columnWeights = new double[] { 0.0, 1.0, 0.0, 1.0, Double.MIN_VALUE };
		gbl_mlpCard.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		mlpCard.setLayout(gbl_mlpCard);

		JLabel lblNewLabel = new JLabel("Training Iterations");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 1;
		mlpCard.add(lblNewLabel, gbc_lblNewLabel);

		txtTrainingIterations = new JTextField();
		GridBagConstraints gbc_txtTrainingIterations = new GridBagConstraints();
		gbc_txtTrainingIterations.insets = new Insets(0, 0, 5, 5);
		gbc_txtTrainingIterations.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtTrainingIterations.gridx = 1;
		gbc_txtTrainingIterations.gridy = 1;
		mlpCard.add(txtTrainingIterations, gbc_txtTrainingIterations);
		txtTrainingIterations.setColumns(10);

		JLabel lblNewLabel_1 = new JLabel("Structure");
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_1.gridx = 2;
		gbc_lblNewLabel_1.gridy = 1;
		mlpCard.add(lblNewLabel_1, gbc_lblNewLabel_1);

		txtStructure = new JTextField();
		GridBagConstraints gbc_txtStructure = new GridBagConstraints();
		gbc_txtStructure.insets = new Insets(0, 0, 5, 0);
		gbc_txtStructure.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtStructure.gridx = 3;
		gbc_txtStructure.gridy = 1;
		mlpCard.add(txtStructure, gbc_txtStructure);
		txtStructure.setColumns(10);

		JLabel lblNewLabel_6 = new JLabel("Learning Rate");
		GridBagConstraints gbc_lblNewLabel_6 = new GridBagConstraints();
		gbc_lblNewLabel_6.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_6.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_6.gridx = 0;
		gbc_lblNewLabel_6.gridy = 2;
		mlpCard.add(lblNewLabel_6, gbc_lblNewLabel_6);

		txtLearningRate = new JTextField();
		GridBagConstraints gbc_txtLearningRate = new GridBagConstraints();
		gbc_txtLearningRate.insets = new Insets(0, 0, 5, 5);
		gbc_txtLearningRate.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtLearningRate.gridx = 1;
		gbc_txtLearningRate.gridy = 2;
		mlpCard.add(txtLearningRate, gbc_txtLearningRate);
		txtLearningRate.setColumns(10);

		JLabel lblValidationSize = new JLabel("Validation Size");
		GridBagConstraints gbc_lblValidationSize = new GridBagConstraints();
		gbc_lblValidationSize.insets = new Insets(0, 0, 5, 5);
		gbc_lblValidationSize.anchor = GridBagConstraints.EAST;
		gbc_lblValidationSize.gridx = 2;
		gbc_lblValidationSize.gridy = 2;
		mlpCard.add(lblValidationSize, gbc_lblValidationSize);

		txtValidationSize = new JTextField();
		GridBagConstraints gbc_txtValidationSize = new GridBagConstraints();
		gbc_txtValidationSize.anchor = GridBagConstraints.NORTH;
		gbc_txtValidationSize.insets = new Insets(0, 0, 5, 0);
		gbc_txtValidationSize.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtValidationSize.gridx = 3;
		gbc_txtValidationSize.gridy = 2;
		mlpCard.add(txtValidationSize, gbc_txtValidationSize);
		txtValidationSize.setColumns(10);

		JLabel lblNewLabel_7 = new JLabel("Momentum");
		GridBagConstraints gbc_lblNewLabel_7 = new GridBagConstraints();
		gbc_lblNewLabel_7.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_7.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_7.gridx = 0;
		gbc_lblNewLabel_7.gridy = 3;
		mlpCard.add(lblNewLabel_7, gbc_lblNewLabel_7);

		txtMomentum = new JTextField();
		GridBagConstraints gbc_txtMomentum = new GridBagConstraints();
		gbc_txtMomentum.insets = new Insets(0, 0, 5, 5);
		gbc_txtMomentum.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtMomentum.gridx = 1;
		gbc_txtMomentum.gridy = 3;
		mlpCard.add(txtMomentum, gbc_txtMomentum);
		txtMomentum.setColumns(10);

		JLabel lblNewLabel_8 = new JLabel("Validation Threshold");
		GridBagConstraints gbc_lblNewLabel_8 = new GridBagConstraints();
		gbc_lblNewLabel_8.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_8.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_8.gridx = 2;
		gbc_lblNewLabel_8.gridy = 3;
		mlpCard.add(lblNewLabel_8, gbc_lblNewLabel_8);

		txtValidationThreshold = new JTextField();
		GridBagConstraints gbc_txtValidationThreshold = new GridBagConstraints();
		gbc_txtValidationThreshold.insets = new Insets(0, 0, 5, 0);
		gbc_txtValidationThreshold.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtValidationThreshold.gridx = 3;
		gbc_txtValidationThreshold.gridy = 3;
		mlpCard.add(txtValidationThreshold, gbc_txtValidationThreshold);
		txtValidationThreshold.setColumns(10);

		JLabel lblSeed = new JLabel("Seed");
		GridBagConstraints gbc_lblSeed = new GridBagConstraints();
		gbc_lblSeed.insets = new Insets(0, 0, 0, 5);
		gbc_lblSeed.anchor = GridBagConstraints.EAST;
		gbc_lblSeed.gridx = 0;
		gbc_lblSeed.gridy = 4;
		mlpCard.add(lblSeed, gbc_lblSeed);

		txtSeed = new JTextField();
		GridBagConstraints gbc_txtSeed = new GridBagConstraints();
		gbc_txtSeed.insets = new Insets(0, 0, 0, 5);
		gbc_txtSeed.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtSeed.gridx = 1;
		gbc_txtSeed.gridy = 4;
		mlpCard.add(txtSeed, gbc_txtSeed);
		txtSeed.setColumns(10);

		JButton btnLoadFile = new JButton("Load File");
		btnLoadFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				loadFile();

				tblOutput.setModel(model);
				cbProduct.setModel(cbModel);

			}
		});

		JPanel linearRegressionCard = new JPanel();

		GridBagLayout gbl_linearRegressionCard = new GridBagLayout();
		gbl_linearRegressionCard.columnWidths = new int[] { 0, 0, 0, 0, 0 };
		gbl_linearRegressionCard.rowHeights = new int[] { 0, 0, 0, 0 };
		gbl_linearRegressionCard.columnWeights = new double[] { 0.0, 1.0, 0.0, 1.0, Double.MIN_VALUE };
		gbl_linearRegressionCard.rowWeights = new double[] { 0.0, 0.0, 0.0, Double.MIN_VALUE };
		linearRegressionCard.setLayout(gbl_linearRegressionCard);

		JLabel lblNewLabel_2 = new JLabel("Attribute Selection");
		GridBagConstraints gbc_lblNewLabel_2 = new GridBagConstraints();
		gbc_lblNewLabel_2.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_2.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_2.gridx = 0;
		gbc_lblNewLabel_2.gridy = 1;
		linearRegressionCard.add(lblNewLabel_2, gbc_lblNewLabel_2);

		JComboBox cbAttributeSelection = new JComboBox();
		cbAttributeSelection.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {

				attributeSelection = cbAttributeSelection.getSelectedIndex();

			}
		});
		cbAttributeSelection.setModel(new DefaultComboBoxModel(new String[] { "M5", "No Attribute", "Greedy" }));
		GridBagConstraints gbc_cbAttributeSelection = new GridBagConstraints();
		gbc_cbAttributeSelection.insets = new Insets(0, 0, 5, 5);
		gbc_cbAttributeSelection.fill = GridBagConstraints.HORIZONTAL;
		gbc_cbAttributeSelection.gridx = 1;
		gbc_cbAttributeSelection.gridy = 1;
		linearRegressionCard.add(cbAttributeSelection, gbc_cbAttributeSelection);

		JLabel lblNewLabel_3 = new JLabel("Decimal Places");
		GridBagConstraints gbc_lblNewLabel_3 = new GridBagConstraints();
		gbc_lblNewLabel_3.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_3.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_3.gridx = 2;
		gbc_lblNewLabel_3.gridy = 1;
		linearRegressionCard.add(lblNewLabel_3, gbc_lblNewLabel_3);

		txtDecimalPlaces = new JTextField();
		GridBagConstraints gbc_txtDecimalPlaces = new GridBagConstraints();
		gbc_txtDecimalPlaces.insets = new Insets(0, 0, 5, 0);
		gbc_txtDecimalPlaces.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtDecimalPlaces.gridx = 3;
		gbc_txtDecimalPlaces.gridy = 1;
		linearRegressionCard.add(txtDecimalPlaces, gbc_txtDecimalPlaces);
		txtDecimalPlaces.setColumns(10);

		JLabel lblNewLabel_9 = new JLabel("Ridge");
		GridBagConstraints gbc_lblNewLabel_9 = new GridBagConstraints();
		gbc_lblNewLabel_9.insets = new Insets(0, 0, 0, 5);
		gbc_lblNewLabel_9.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_9.gridx = 0;
		gbc_lblNewLabel_9.gridy = 2;
		linearRegressionCard.add(lblNewLabel_9, gbc_lblNewLabel_9);

		txtRidge = new JTextField();
		GridBagConstraints gbc_txtRidge = new GridBagConstraints();
		gbc_txtRidge.insets = new Insets(0, 0, 0, 5);
		gbc_txtRidge.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtRidge.gridx = 1;
		gbc_txtRidge.gridy = 2;
		linearRegressionCard.add(txtRidge, gbc_txtRidge);
		txtRidge.setColumns(10);

		cardPanel.add(linearRegressionCard, "Linear Regression");
		cardPanel.add(mlpCard, "MLP");

		GridBagConstraints gbc_btnLoadFile = new GridBagConstraints();
		gbc_btnLoadFile.gridx = 0;
		gbc_btnLoadFile.gridy = 1;
		frmHonoursProject.getContentPane().add(btnLoadFile, gbc_btnLoadFile);

		cbAlgorithm.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				algorithmChoice = cbAlgorithm.getSelectedIndex();
				CardLayout cl = (CardLayout) (cardPanel.getLayout());
				System.out.println(e.getItem());

				cl.show(cardPanel, (String) e.getItem());
			}
		});

		chckbxAdvancedSettings.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {

				advancedSettingsState = e.getStateChange();
				if (e.getStateChange() == ItemEvent.SELECTED) {
					// TODO Fill this out with Linear fields
					// Advanced Settings enabled
					
					for(JTextField textbox : mlpInput) {
					textbox.setEditable(true);	
					}
					for(JTextField textbox : regressionInput) {
						textbox.setEditable(true);	
						}
//					
//					txtTrainingIterations.setEditable(true);
//					txtLearningRate.setEditable(true);
//					txtMomentum.setEditable(true);
//					txtSeed.setEditable(true);
//					txtStructure.setEditable(true);
//					txtValidationThreshold.setEditable(true);
//					txtValidationSize.setEditable(true);
//					txtDecimalPlaces.setEditable(true);
//					txtRidge.setEditable(true);
//					cbAlgorithm.setEditable(true);

				} else {
					// Advanced Settings disabled
					for(JTextField textbox : mlpInput) {
						textbox.setEditable(false);	
						}
					for(JTextField textbox : regressionInput) {
						textbox.setEditable(false);	
						}
					
//					txtTrainingIterations.setEditable(false);
//					txtLearningRate.setEditable(false);
//					txtMomentum.setEditable(false);
//					txtSeed.setEditable(false);
//					txtStructure.setEditable(false);
//					txtValidationThreshold.setEditable(false);
//					txtValidationSize.setEditable(false);
//					txtDecimalPlaces.setEditable(false);
//					txtRidge.setEditable(false);
//					cbAlgorithm.setEditable(false);

				}

			}
		});
	}

	private void loadFile() {

		final JFileChooser fc = new JFileChooser();
		int returnValue = fc.showOpenDialog(frmHonoursProject);

		if (returnValue == JFileChooser.APPROVE_OPTION) {
			csvFile = fc.getSelectedFile();
			if (csvFile != null) {
				CSVFileReader csvReader = new CSVFileReader();
				String extension = csvReader.getFileExtension(csvFile);

				if (extension.equals(".csv")) {
					try {
						String arffFileName = csvFile.getName();
						arffFileName.replace(".csv", ".arff");
						boolean Success = csvReader.readCSV(csvFile, arffFileName);
						if (Success) {
							arffFile = new File(arffFileName);
							// TODO fix type safety

							model = csvReader.getTableModel(csvFile);
							productsList = (String[]) csvReader.getColumnNames();
							cbModel = new DefaultComboBoxModel(productsList);
						} else {
							// Corrupt File, please try again
							JOptionPane.showMessageDialog(frmHonoursProject, "arff File is null", "Error",
									JOptionPane.ERROR_MESSAGE);
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else {
					// File isn't correct format
					JOptionPane.showMessageDialog(frmHonoursProject, "arff File is null", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			} else {
				// File is null
				JOptionPane.showMessageDialog(frmHonoursProject, "arff File is null", "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		} else {
			// error in file choosing
			JOptionPane.showMessageDialog(frmHonoursProject, "arff File is null", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void run(String selectedOption, String productName, int iterations, int productChoice,
			Object[] fieldsToForecast) throws Exception {

		switch (selectedOption) {
		case "2": // Forecasting
			System.out.println("Forecasting is selected");
			Forecaster forecaster = new Forecaster();
			int numberOfPredictions = 5;
			if (algorithmChoice == 0) {
				if (advancedSettingsState == 2) {
					forecaster.regressionSimpleForecast(arffFile, productName, productChoice, iterations,
							numberOfPredictions);
				} else {
					double ridge = Double.parseDouble(txtRidge.getText());
					int decimalPlaces = Integer.parseInt(txtDecimalPlaces.getText());

					RegressionSetting settings = new RegressionSetting(ridge, decimalPlaces, attributeSelection);

					forecaster.regressionAdvancedForecast(arffFile, productName, productChoice, iterations, settings,
							numberOfPredictions);
				}

			} else if (algorithmChoice == 1) {
				// MLP Forecasting
				if (advancedSettingsState == 2) {
					// simple
					forecaster.mlpSimpleForecast(arffFile, productName, productChoice, iterations, numberOfPredictions);
				} else {
					// advanced
					advancedBuild(forecaster, arffFile, productChoice, productName, iterations, numberOfPredictions);
				}
			} else {
				// Error
				JOptionPane.showMessageDialog(frmHonoursProject, "arff File is null", "Error",
						JOptionPane.ERROR_MESSAGE);
			}
			break;
		case "3": // Evaluation
			System.out.println("Evaluation is selected");
			TestEvaluation evaluations = new TestEvaluation();
			if (algorithmChoice == 0) {
				// Linear Regression
				numberOfPredictions = 5;
				evaluations.regressionEvaluation(arffFile, productName, productChoice, iterations, numberOfPredictions);
				// evaluations.allProductsRegressionEvaluation(arffFile, fieldsToForecast,
				// iterations, numberOfPredictions);
			} else if (algorithmChoice == 1) {
				// MLP
				numberOfPredictions = 5;
				// evaluations.allProductsMLPEvaluation(arffFile, fieldsToForecast, iterations,
				// numberOfPredictions);

				evaluations.mlpEvaluation(arffFile, productName, productChoice, iterations, numberOfPredictions);
				JOptionPane.showMessageDialog(frmHonoursProject, "Finished my dude", "Thank God",
						JOptionPane.PLAIN_MESSAGE);
			} else {
				// error
				JOptionPane.showMessageDialog(frmHonoursProject, "arff File is null", "Error",
						JOptionPane.ERROR_MESSAGE);
			}
			break;
		default: // No radio button selected
			// Error
			JOptionPane.showMessageDialog(frmHonoursProject, "arff File is null", "Error", JOptionPane.ERROR_MESSAGE);
			break;

		}
	}

	private void advancedBuild(Forecaster forecaster, File arffFile, int classIndex, String productName, int iterations,
			int numberOfPreductions) {
		double trainingTime = Double.parseDouble(txtTrainingIterations.getText());
		double learningRate = Double.parseDouble(txtLearningRate.getText());
		double momentum = Double.parseDouble(txtMomentum.getText());
		double seed = Double.parseDouble(txtSeed.getText());
		String structure = txtStructure.getText();
		double validationThreshold = Double.parseDouble(txtValidationThreshold.getText());
		double validationSize = Double.parseDouble(txtValidationSize.getText());

		AdvancedSettings settings = new AdvancedSettings(structure, trainingTime, learningRate, momentum, seed,
				validationThreshold, validationSize);

		forecaster.mlpAdvancedForecast(arffFile, productName, classIndex, iterations, settings, numberOfPredictions);

		// iterations, learningRate, momentum, seed, structure, validationThreshold,
		// validationSize, file, classIndex
		// mlp.advancedBuild(trainingIterations, learningRate, momentum, seed,
		// structure, validationThreshold, validationSize, arffFile, classIndex,
		// iterations);

	}

	private void generateInputArrays() {

		mlpInput.add(txtTrainingIterations);
		mlpInput.add(txtMomentum);
		mlpInput.add(txtLearningRate);
		mlpInput.add(txtValidationThreshold);
		mlpInput.add(txtValidationSize);
		mlpInput.add(txtSeed);
		mlpInput.add(txtStructure);

		regressionInput.add(txtRidge);
		regressionInput.add(txtDecimalPlaces);

	}

	private boolean validateInput(int algorithmChoice) {

		switch (algorithmChoice) {
		case 0:
			for (JTextField textbox : regressionInput) {
				if (textbox.getText().isEmpty()) {
					return false;
				}
			}
			break;
		case 1:
			for (JTextField textbox : mlpInput) {
				if (textbox.getText().isEmpty()) {
					return false;
				}
			}
			break;
		default:
			return false;
		}
		return true;

	}

}
