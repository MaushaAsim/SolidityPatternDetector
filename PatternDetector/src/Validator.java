import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

// read file line by line
public class Validator {
	
	public boolean checkDPInstances(String filepath,String SEARCH_TOKEN) throws IOException {
//		String SEARCH_TOKEN = "proxy";
		List<String> found = new ArrayList<>();


	        ProcessBuilder builder = new ProcessBuilder(
	                "sh", "-c", "grep -rnwl '"+filepath+"' -e '"+ SEARCH_TOKEN + "'");
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
