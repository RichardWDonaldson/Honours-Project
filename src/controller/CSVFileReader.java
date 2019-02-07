package controller;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.swing.table.DefaultTableModel;
import com.opencsv.CSVReader;

import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;



public class CSVFileReader {

	Object[] columnNames;
	transient CSVReader CSVFileReader;
	
	public Instances readCSV(File csvFileName, String arffFileName) throws IOException{
		
		File arffFile = new File(arffFileName);
		
		if(arffFile.exists()) {
			arffFile.delete();
		}
		
		// load CSV
	    CSVLoader loader = new CSVLoader();
	    loader.setSource(csvFileName);
	    Instances data = loader.getDataSet();
	    data.setClassIndex(data.numAttributes() - 1);

	    // save ARFF
	    ArffSaver saver = new ArffSaver();
	    saver.setInstances(data);
	    saver.setFile(new File(arffFileName));
	//    saver.setDestination(new File(arffFileName));
	    saver.writeBatch();
	   	   
	    return data;
		
	}
	
public DefaultTableModel getTableModel(File file) {
		
		try {
			FileInputStream fis = new FileInputStream(file.getAbsolutePath());
			
			InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
			
			CSVReader reader = new CSVReader(isr);
			
			List<?> myEntries = reader.readAll();
			
			columnNames = (String[]) myEntries.get(0);
			DefaultTableModel tableModel = new DefaultTableModel(columnNames, myEntries.size() - 1 );
			int rowCount = tableModel.getRowCount();
			
			for(int x = 0; x < rowCount + 1; x++) {
				int columnNumber = 0;
				if(x > 0) {
					for(String thisCellValue : (String[]) myEntries.get(x)) {
						tableModel.setValueAt(thisCellValue, x-1, columnNumber);
						columnNumber++;
					}
				}
				
			}
			
			reader.close();
			return tableModel;	
			
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		
		return null;
	}
	

	
	
	public String getFileExtension(File file) {
		String extension = "";
		
		try {
			if(file != null && file.exists()) {
				String name = file.getName();
				extension = name.substring(name.lastIndexOf("."));
			 
		}
		} catch (Exception ex) {
			
			extension = "";
		}
		
		return extension;
		
		
		
	}
	

	

	
	
}
