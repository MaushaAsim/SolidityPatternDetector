import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.io.FilenameFilter;

public class Main {
    public static void main(String[] args) throws Exception{
    
     String path = "/home/maarooshaa/dapps_shortlisted";
     File directoryPath = new File(path);
     File[] contents = directoryPath.listFiles();
     for(File file : contents) {
    	 System.out.println("Project name: "+file.getName());
    	 detectPatterns(file.getAbsolutePath());
    	 System.out.println(" ");
     }
     System.out.println("Processing Completed");
     
    }
    
    public static void detectPatterns(String path) throws Exception{
//    	path = "/home/maarooshaa/dapps/smart-contracts";
    	File dappPath = new File(path);
    	File smartContractDirPath = new File(path+"/contracts");
        FilenameFilter textFilefilter = new FilenameFilter(){
           public boolean accept(File dir, String name) {
              String lowercaseName = name.toLowerCase();
              if (lowercaseName.endsWith(".sol")) {
                 return true;
              } else {
                 return false;
              }
           }
        };
        //List of all the solidity files
        File filesList[] = smartContractDirPath.listFiles(textFilefilter);
        if((filesList!=null)&&(filesList.length != 0)) {
	        System.out.println("List of the solidity files in the specified directory:");
	        TerminalProcess cmd = new TerminalProcess();
	        CSVUtility csvUtil = new CSVUtility(); 
	        List<String[]> dataLines = new ArrayList<>();
	      	 dataLines.add(new String[] 
	      		   { "File Name", "Contract Name", "NOM", "DIT","NOC","RFC","WMC","CBO","isFinal","isSubClass","Proxy","Proxy-V","Composite","Composite-V","Observer","Observer-V","Factory","Factory-V" });


	        try {
				cmd.runCommand(dappPath, "mkdir flattened_contracts");
				cmd.runCommand(dappPath, "mkdir metrics");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	           
	        String inputfilePath;
	        for(File file : filesList) {
	           String fileName[] = file.getName().split("[.]",0);
	           System.out.println("File name: "+file.getName());   
	           try {
	        	   //flatten contract
		           cmd.runCommand(smartContractDirPath, "truffle-flattener "+ file.getName()+" > " + path+"/flattened_contracts/"+fileName[0]+"flattened.sol");
		           inputfilePath = "/flattened_contracts/"+fileName[0]+"flattened.sol";

	           }
	        	   catch (InterruptedException | IOException x) {
			            x.printStackTrace();
				           inputfilePath = "/contracts/"+fileName[0]+"flattened.sol";

			        }
	           
	           
	           try {
	        	   
		           //run contracts through SolMet
		           cmd.runCommand(dappPath, "> "+"./metrics/"+fileName[0]+".csv");
		           cmd.runCommand(dappPath, "java -jar /home/maarooshaa/tools/solMet/target/solmet-1.0.jar "+ "-inputFile ."+inputfilePath+ " -outFile "+ "./metrics/"+fileName[0]+".csv");
		          
		           csvUtil.readMetrics(path+"/metrics/"+fileName[0]+".csv",path+inputfilePath,dataLines);
		           //validate file for DP instances
		           	           
	           }
		        catch (InterruptedException | IOException x) {
		            x.printStackTrace();
		        }
	           finally {
		           csvUtil.saveAllMetrics( path+"/metrics/output.csv",dataLines);     

	           }
	        }
	        System.out.println("Successful Metric Extraction Completed"); 
	       
	    //no smart contracts found
        }else {
        	 System.out.println("ERROR - The provided directory does not support solidity files.");
        }
    }
    

}
