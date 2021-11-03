import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class MockFrameworkFrequencyCalculator {
	
	static ArrayList<String> fileContent = new ArrayList<>();
	
	static ArrayList<String>frameworks = new ArrayList<>();
	static ArrayList<Integer> frameworks_count = new ArrayList<>();
	
	static ArrayList<String> projects = new ArrayList<>();
	
	static boolean second_time = false;
	
	public static void main (String[] args) throws FileNotFoundException {
		
		int project_with_multiple_frameworks = 0;
		
		
		Scanner reader = new Scanner (new File("D:\\Stevens\\2021_summer_project\\UndProjects\\AllMockFrameworks.csv"));
		
		while(reader.hasNextLine()) {
			fileContent.add(reader.nextLine());
		}
		
		for(String line: fileContent) {
			int next_comma = line.indexOf(",");
			
			if(projects.contains(line.substring(0,next_comma)) && second_time) {
				project_with_multiple_frameworks ++;
				second_time = false;
			}
			else if(!projects.contains(line.substring(0,next_comma))) {
				projects.add(line.substring(0,next_comma));
				second_time = true;
			}
			
			line = line.substring(next_comma + 1);
			
			next_comma = line.indexOf(",");
			
			String framework = line.substring(0,next_comma);
			
			if(!framework.equals("Mocking Framework")){
				if(frameworks.contains(framework)) {
					int temp = frameworks_count.get(frameworks.indexOf(framework)) + 1;
					frameworks_count.set(frameworks.indexOf(framework), temp);
				}
				else {
					frameworks.add(framework);
					frameworks_count.add(1);
				}
			}
			
			
		}
		
		
		try {
			FileWriter newFile = new FileWriter("D:\\Stevens\\2021 summer general\\RQ2 needed data\\FrequencyByProject.csv");
			
			newFile.append("Framework, count");
			newFile.append("\n");
			
			for(int i = 0; i< frameworks.size();i++) {
				newFile.append(frameworks.get(i)+",");
				newFile.append(frameworks_count.get(i).toString());
				newFile.append("\n");
			}
			
			newFile.append("\n");
			newFile.append("project with multiple frameworks: "+","+ String.valueOf(project_with_multiple_frameworks));
			
			
			newFile.flush();
			
			System.out.println("succeed");
		}
		catch(IOException e){
			System.out.println("failed");
		}
		
		
		
		
		for(String item: fileContent) {
			System.out.println(item);
		}
	}

}
