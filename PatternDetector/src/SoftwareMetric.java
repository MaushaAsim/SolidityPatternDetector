import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class SoftwareMetric {
	static int WMC_threshold=8;
	static int DIT_threshold=3;
	static int NOC_threshold= 3;
	static int CBO_threshold=4;
	static int RFC_threshold=18;
	String SolidityFile, ETHAddress, ContractName, Type,contractFilePath;
	int NOM,DIT,NOC,RFC,CBO,WMC;
	boolean isFinal = false, isSubclass = false,controlledInit = false, controlledExcept = false,conglomeration = false,
			returns = false, recieves = false , createObj = false , delegates = false ,
			sameElem = false, typeOf = false , linkMethod = false , linkArtefact = false, ctorVisibility = false, 
			aggregation = false , adapterMethod = false, redirectinFamily = false, sameInterfaceInstance = false ,
			sameInterfaceContainer = false;
//int LCOM 0-1
//high if >= threshold/2
//low if < threshold/2
	
public SoftwareMetric(String[] arr, String contractfilePath) {
	this.contractFilePath = contractfilePath;
	this.SolidityFile = arr[0];
	this.ETHAddress= arr[1]; this.ContractName = arr[2]; this.Type = arr[3]; 
//	int SLOC= Integer.parseInt(arr[4]); int LLOC =Integer.parseInt(arr[5]);int CLOC= Integer.parseInt(arr[6]);
	this.WMC= Integer.parseInt(arr[8]);
//	int NL= Integer.parseInt(arr[9]);int NLE= Integer.parseInt(arr[10]); 
//	int NUMPAR= Integer.parseInt(arr[11]);int NOS= Integer.parseInt(arr[12]);
	int NOA= Integer.parseInt(arr[14]); 
	this.CBO= Integer.parseInt(arr[16]);
//	int NA= Integer.parseInt(arr[17]);int NOI= Integer.parseInt(arr[18]);
//	int avg_mcCC= Integer.parseInt(arr[19]);int avg_NL= Integer.parseInt(arr[20]); int avg_NLE= Integer.parseInt(arr[21]);
//	int avg_NUMPAR= Integer.parseInt(arr[22]);int avg_NOS= Integer.parseInt(arr[23]);int avg_NOI= Integer.parseInt(arr[24]);
//	
	this.NOM= Integer.parseInt(arr[7]);
	this.DIT= Integer.parseInt(arr[13]);
	this.NOC= Integer.parseInt(arr[15]);
	this.RFC= Integer.parseInt(arr[7])+ Integer.parseInt(arr[18]); //same as NOM due to solidity 
	
	if(this.NOC == 0)
		this.isFinal = true;
	if(NOA != 0)
		this.isSubclass = true;
	
}

 

 public boolean checkProxyRules() {
//	 >= DIT subject 2.0 < RFC subject 23.0 
	 if((2 <= this.DIT ) && (23<this.RFC))
		 return true;
	 else 
		 return false;
}
 
public boolean checkCompositeRules() {
	int val = 0;
	if( 5 <= this.DIT ) //avg between geml and prev metrics
		val++;
	if((this.RFC > 4) && (this.RFC <50))
		val++;
	if((this.WMC > 4) && (this.WMC <48))
		val++;
	if(this.CBO ==1)
		val++;

	if(val>=2)
		return true;
	return false;
	
}

public boolean checkObserverRules() {
	if((this.NOC < 4 )&&(this.RFC>27)&&(this.DIT<3)&&(this.WMC > 21))
		return true;
	return false;
}

public boolean checkFactoryRules() {
	int val=0;
	if(this.DIT>= DIT_threshold) //high
		val++;
	if(this.NOC >= NOC_threshold)	//high
		val++;
	if(this.CBO >= CBO_threshold)	//high
		val++;
	if(12 < this.RFC) //geml + threshold = 18 
		val++;
	if(this.WMC< WMC_threshold) //low
		val++;			
	
	if(val>2)
		return true;
	return false;
}

public boolean validateDPInstances(String SEARCH_TOKEN) throws IOException {
//	String SEARCH_TOKEN = "proxy";
	List<String> found = new ArrayList<>();

//		System.out.println("grep -rnwlEi '"+SEARCH_TOKEN +"' "+this.contractFilePath);
        ProcessBuilder builder = new ProcessBuilder(
                "sh", "-c", "grep -rnwlEi '"+SEARCH_TOKEN +"' "+this.contractFilePath);
        builder.redirectErrorStream(true);
        Process p = builder.start();
        BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;
        while (true) {
            line = r.readLine();
            if (line == null) {
                break;
            }
            found.add(line);
        }
        if(!found.isEmpty())
        	return true;
        return false;
        

}

}
