import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Scanner;

public class MockedClassesAnalysis {
	
	static ArrayList<String> fileNames = new ArrayList<>();
	static ArrayList<String> metricContent = new ArrayList<>();
	
	static ArrayList<String> proportion_name = new ArrayList<>();
	static ArrayList<String> proportion_value = new ArrayList<>();
	static ArrayList<String> proportion_value2 = new ArrayList<>();
	
	
	
	static ArrayList<String> result_projectName = new ArrayList<>();
	static ArrayList<String> result_NumOfInside = new ArrayList<>();
	static ArrayList<String> result_NumOfOutside = new ArrayList<>();
	
	
	static ArrayList<String> outsideContent = new ArrayList<>();
	static ArrayList<String> outsideClassName = new ArrayList<>();
	static ArrayList<Integer> outsideCount = new ArrayList<>();
	
	// "File path to directory where output data from GetInfo is stored."
	private static String OutputDirectoryPath = "E:\\eclipse\\SummerResearch";
	// "File path to directory where UnderstandProject info is stored."
	private static String UndProjectPath = "C:\\Users\\RedRo\\OneDrive\\Documents\\Academic Texts\\Summer Research\\UndProjects";
	
	

	
	
	public static void ReadAllFiles(final File folder) throws IOException {
		for(final File entry : folder.listFiles()) {
			
			
			
			if(entry.getName().contains(".txt")) {
				
				String name = entry.getName();
				int pos = name.indexOf("Mocked");
				name = name.substring(0,pos);
				
				fileNames.add(name);
				
				ReadFile(entry);
			}
		}
		
		output();
		
	}
	
	public static void ReadFile(final File file) throws FileNotFoundException, IOException {
		
		ArrayList<String> fileContent = new ArrayList<>();
		
		ArrayList<String> InsideClasses = new ArrayList<>();
		
		ArrayList<String> OutsideClasses = new ArrayList<>();
		
		ArrayList<String> InsideClasses_filtered = new ArrayList<>();
		ArrayList<String> OutsideClasses_filtered = new ArrayList<>();
		
		
		
		
		
		Scanner reader = new Scanner(file);
		
		while(reader.hasNextLine()) {
			fileContent.add(reader.nextLine());
		}
		
		
		String FileName = file.getName();
		int pos = FileName.indexOf("Mocked");
		String ProjectName = FileName.substring(0,pos);
		
		
		for(String sentence : fileContent) {
			if(sentence.contains(ProjectName)) {
				InsideClasses.add(sentence);
			}
			else {
				OutsideClasses.add(sentence);
			}
		}
		
		
		for(String sentence2: InsideClasses) {
			if (!InsideClasses_filtered.contains(sentence2)) {
				InsideClasses_filtered.add(sentence2);
			}
		}
		
		for (String sentence3: OutsideClasses) {
			
			outsideContent.add(sentence3);
			
			if(!OutsideClasses_filtered.contains(sentence3)) {
				OutsideClasses_filtered.add(sentence3);
			}
		}
		
		
		PrintWriter writer = new PrintWriter (OutputDirectoryPath + "\\RQ3\\RQ3 result\\" + ProjectName + " filtered mocked classes.txt", "UTF-8");
		
		writer.println("Inside classes: ");
		writer.println(" ");
		
		for (String sentence4 : InsideClasses_filtered) {
			writer.println(sentence4);
		}
		
		writer.println(" ");
		writer.println(" ");
		writer.println(" ");
		
		
		writer.println("Outside classes: ");
		writer.println(" ");
		
		for (String sentence5 :OutsideClasses_filtered ) {
			writer.println(sentence5);
			
		}
		
		writer.close();
		
		result_projectName.add(ProjectName);
		result_NumOfInside.add(String.valueOf(InsideClasses_filtered.size()));
		result_NumOfOutside.add(String.valueOf(OutsideClasses_filtered.size()));
		
		
		
		
		
		
		
	}
	
	public static void output() throws IOException {
		
		
		
		FileWriter newFile = new FileWriter(OutputDirectoryPath + "RQ3\\RQ3 result\\summary.csv");
		
		newFile.append("project name, variety of inside mocked classes, variety of outside mocked classes, proportion of mocked classes");
		newFile.append("\n");
		
		int moreInside = 0;
		int moreOutside = 0;
		
		for (int i = 0; i < result_projectName.size(); i++) {
			newFile.append(result_projectName.get(i) + ",");
			newFile.append(result_NumOfInside.get(i)+ ",");
			newFile.append(result_NumOfOutside.get(i) + ",");
			
			newFile.append("\n");
			
			
			if(Integer.parseInt(result_NumOfInside.get(i)) > Integer.parseInt(result_NumOfOutside.get(i))){
				moreInside ++;
			}
			else if(Integer.parseInt(result_NumOfOutside.get(i)) > Integer.parseInt(result_NumOfInside.get(i))) {
				moreOutside++;
			}
			
			
		}
		
		newFile.append(" " + "\n");
		
		newFile.append("more inside classes mocked: " + "," + "more outside classes mocked: " + "\n");
		
		newFile.append(String.valueOf(moreInside)+"," + String.valueOf(moreOutside)+"\n");
		
		
		newFile.flush();
		
		System.out.println("success");
		
		Analysis2();
		
		Analysis3();
		
	}
	
	public static void Analysis2() throws IOException {
		
		int total = 0;
		int test = 0;
		int rest = 0;
		
		Scanner reader = new Scanner (new File(UndProjectPath + "\\AllMetrics.csv"));
		
		while(reader.hasNextLine()) {
			metricContent.add(reader.nextLine());
		}
		
		
		
		for(String line: metricContent) {
			for(String name: fileNames) {
				if(line.contains(name)) {
					
//					System.out.println(line);
					
//					calculate total number of classes created for each project;
					for(int i = 0; i<4;i++) {
						int pos = line.indexOf(",");
						line = line.substring(pos + 1);
					}
					
					int pos2 = line.indexOf(",");
					
					total = Integer.parseInt(line.substring(0,pos2));

					line = line.substring(pos2 + 1);
					
					int pos3 = line.indexOf(",");
					
					line = line.substring(pos3 + 1);
					
					int pos4 = line.indexOf(",");
					
					test = Integer.parseInt(line.substring(0,pos4));
					
					rest = total - test;
					
					System.out.println(rest);
					
					
					int idx = result_projectName.indexOf(name);
					
					String inside = result_NumOfInside.get(idx);
					
					
					proportion_name.add(name);
					
					proportion_value.add(String.valueOf(rest));
					
					proportion_value2.add(inside);
					
					
		
					
					
				}
			}
		}
		
		
		output2();
		
	}
	
	
	public static void output2() throws IOException {
		FileWriter newFile = new FileWriter(OutputDirectoryPath + "\\RQ3\\RQ3 result\\proportion of mocked classes.csv");
		
		newFile.append("project name, # of total classes created, # of inside classes mocked");
		newFile.append("\n");
		
		for(int i = 0; i < proportion_name.size(); i++) {
			newFile.append(proportion_name.get(i)+",");
			newFile.append(proportion_value.get(i) + ",");
			newFile.append(proportion_value2.get(i) + ',');
			newFile.append("\n");
		}
		
		newFile.flush();
		
		System.out.println("success");
	}
	
	
	
	public static void Analysis3() throws IOException{
		for (String x : outsideContent) {
			if(!outsideClassName.contains(x)) {
				outsideClassName.add(x);
				outsideCount.add(1);
			}
			else {
				int idx = outsideClassName.indexOf(x);
				
				int current = outsideCount.get(idx);
				
				outsideCount.set(idx, current + 1);
				
				
			}
		}
		
		
		output3();
	}
	
	public static void output3() throws IOException {
		FileWriter newFile = new FileWriter(OutputDirectoryPath + "\\RQ3\\RQ3 result\\outside mocked classes summary.csv");
		
		newFile.append("class name, frequency");
		newFile.append("\n");
		
		for(int i = 0; i< outsideClassName.size();i++) {
			newFile.append(outsideClassName.get(i) + ",");
			newFile.append(outsideCount.get(i) + ",");
			newFile.append("\n");
			
		}
		
		newFile.flush();
		
		System.out.println("success");
	}
	
	
	
	public static void main (String[] args) throws IOException {
//		String a = "any23Mocked Classes";
//		
//		int b = a.indexOf("Mocked");
//		
//		System.out.println(a.substring(0,b));
		
		final File folder = new File(OutputDirectoryPath + "\\RQ3\\RQ3 data");
		ReadAllFiles(folder);
				
		
	}
	
}
