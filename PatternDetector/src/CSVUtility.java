
import java.io.BufferedReader;  
import java.io.FileReader;  
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.io.File;

public class CSVUtility  {  
	public void readMetrics(String csvfilepath, String contractFilePath,List<String[]> dataLines)   {  
	String line = "";  
	String splitBy = ";";  
	
	try {  
		//parsing a CSV file into BufferedReader class constructor  
//		BufferedReader br = new BufferedReader(new FileReader("/home/maarooshaa/dapps/LDelay/metrics/LDelayBase.csv"));
		BufferedReader br = new BufferedReader(new FileReader(csvfilepath));  
		String headerLine = br.readLine();
		ArrayList<SoftwareMetric> contractMetrics = new ArrayList<>();
		
		while ((line = br.readLine()) != null) {   //returns a Boolean value  
	  
				String[] metricsRow = line.split(splitBy);    // use comma as separator  
				SoftwareMetric metric = new SoftwareMetric(metricsRow, contractFilePath);
				contractMetrics.add(metric); //possibly remove this
				
				addToData(metric,dataLines);

			}  

	}   
	
	catch (IOException e)   {  
		e.printStackTrace();  
		}
	}
	
	private static void addToData(SoftwareMetric metric,List<String[]> dataLines) {
		 try {
			dataLines.add(new String[] 
			   { metric.SolidityFile , metric.ContractName, Integer.toString(metric.NOM) ,Integer.toString(metric.DIT), Integer.toString(metric.NOC),
					   Integer.toString(metric.RFC), Integer.toString(metric.WMC), Integer.toString(metric.CBO),Boolean.toString(metric.isFinal) , Boolean.toString(metric.isSubclass),
				Boolean.toString(metric.checkProxyRules()),Boolean.toString(metric.validateDPInstances("\\<proxy\\>")), Boolean.toString(metric.checkCompositeRules()),
				Boolean.toString(metric.validateDPInstances("\\<composite\\>")),Boolean.toString(metric.checkObserverRules()),
				Boolean.toString(metric.validateDPInstances("\\<observer\\>")),Boolean.toString(metric.checkFactoryRules()),
				Boolean.toString(metric.validateDPInstances("\\<factory\\>"))});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
	}
	
	public void saveAllMetrics(String CSV_FILE_NAME, List<String[]> dataLines) throws IOException {
	    File csvOutputFile = new File(CSV_FILE_NAME);
	    try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
	        dataLines.stream()
	          .map(this::convertToCSV)
	          .forEach(pw::println);
	    }
	}
	
	 public String convertToCSV(String[] data) {
	        return Stream.of(data)
	            .map(this::escapeSpecialCharacters)
	            .collect(Collectors.joining(","));
	    }
	public String escapeSpecialCharacters(String data) {
	    String escapedData = data.replaceAll("\\R", " ");
	    if (data.contains(",") || data.contains("\"") || data.contains("'")) {
	        data = data.replace("\"", "\"\"");
	        escapedData = "\"" + data + "\"";
	    }
	    return escapedData;
	}

	
}  
