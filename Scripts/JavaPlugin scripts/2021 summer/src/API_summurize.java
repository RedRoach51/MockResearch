

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class API_summurize {
	
	
	static ArrayList<String> fileContent = new ArrayList<String>();
	
	static ArrayList<String> frameworks = new ArrayList<String>();
	
	static ArrayList<String> methods = new ArrayList<String>();
	static ArrayList<String> origins = new ArrayList<String>();
	static ArrayList<Integer> counts = new ArrayList<Integer>();
	
//	static ArrayList<String> MockedObjectsInEachFile = new ArrayList<>();
	static ArrayList<String> MockedObjectsCategory = new ArrayList<>();
	static ArrayList<Integer> MockedObjectsCategory_counts = new ArrayList<>();
	
	static ArrayList<String> num_of_mocked = new ArrayList<>();
	
	
	static ArrayList<ArrayList<String>> ALL = new ArrayList<>();
	
	static ArrayList<String> to_be_excluded = new ArrayList<>(Arrays.asList("okhttp3","org","io","com","compile","google","api","net","jvnet","javacrumbs"));
	
	// "File path to directory where output data from GetInfo is stored."
	private static String OutputDirectoryPath = "E:\\eclipse\\SummerResearch";
	
	
	
	
	public static void ReadAll2(final File folder2) throws FileNotFoundException, IOException {//mocked classes
		
		for(final File entry: folder2.listFiles()) {
			if(entry.getName().contains(".txt")) {
				ReadFile2(entry);
			}
		}
		
		output2();
	}
	
	public static void ReadFile2 (final File file) throws FileNotFoundException, IOException{//mocked classes
		ArrayList<String> fileContent = new ArrayList<>();
		ArrayList<String> classes = new ArrayList<>();
		
		Scanner reader = new Scanner(file);
		
		while(reader.hasNextLine()) {
			fileContent.add(reader.nextLine());
		}
		
//		int class_counter = 0;
		
		for(int i = 0; i < fileContent.size();i++) {
			
			if(fileContent.get(i).contains(".java") && fileContent.get(i).contains("/")) {
//				int index = files.indexOf(fileContent.get(i));
//				if(index > (num_of_mocked.size()+1)){//means some files in the middle have no mocked classes
//					for(int k = 0; k < index - num_of_mocked.size(); k++) {
//						num_of_mocked.add("0");
//					}
//				}
				
//				class_counter = 0;
				classes.clear();
			}
			
			if(i == fileContent.size()-1) {
				if(!(fileContent.get(i).contains(".java") && fileContent.get(i).contains("/"))){
					
					classes.add(fileContent.get(i));
					
				}
				
				num_of_mocked.add(Integer.toString(classes.size()));
				break;
			}
			
			if((fileContent.get(i+1).contains(".java") && fileContent.get(i+1).contains("/"))) {//end of a file
				
				if(!(fileContent.get(i).contains(".java") && fileContent.get(i).contains("/"))){
					
					classes.add(fileContent.get(i));
					
				}
				
				num_of_mocked.add(Integer.toString(classes.size()));
			}
			
			else {
//				class_counter++;
				if(!(fileContent.get(i).contains(".java") && fileContent.get(i).contains("/"))){
					
					classes.add(fileContent.get(i));
					
				}
			}
		}
		
		
		
		
	}
	
	public static void output2() throws IOException {
		FileWriter newFile = new FileWriter(OutputDirectoryPath + "\\RQ2\\mocked objects in single file summary (not filtered).csv");
		
		newFile.append("# of Mocked objects in a single file, count");
		newFile.append("\n");
		
		
		System.out.println(num_of_mocked);
		System.out.println(num_of_mocked.size());
		
		
		
		for(String x : num_of_mocked) {
			if(Integer.parseInt(x)>10) {
				if(MockedObjectsCategory.contains("10 or greater")) {
					
					int temp = MockedObjectsCategory_counts.get(MockedObjectsCategory.indexOf("10 or greater")) + 1 ;
					MockedObjectsCategory_counts.set(MockedObjectsCategory.indexOf("10 or greater"), temp);
				}
				else {
					MockedObjectsCategory.add("10 or greater");
					MockedObjectsCategory_counts.add(1);
				}
			}
			
			else {
				
				if(MockedObjectsCategory.contains(x)) {
					int temp = MockedObjectsCategory_counts.get(MockedObjectsCategory.indexOf(x)) + 1 ;
					
					MockedObjectsCategory_counts.set(MockedObjectsCategory.indexOf(x), temp);
				}
				else {
					MockedObjectsCategory.add(x);
					MockedObjectsCategory_counts.add(1);
				}
				
			}
		}
		
		for(int i = 0; i< MockedObjectsCategory.size(); i++) {
			newFile.append(MockedObjectsCategory.get(i)+",");
			
			newFile.append(MockedObjectsCategory_counts.get(i).toString());
			
			newFile.append("\n");
		}
		
		newFile.flush();
		
		
		num_of_mocked.clear();
		
		
		System.out.println("success2");
	}
	public static void ReadALLFiles(final File folder) throws FileNotFoundException {
		for(final File entry : folder.listFiles()) {
			
			
			
			if(entry.getName().contains(".csv")) {
				ReadFile(entry);
			}
		}
		
		Analyze();
	}
	
	public static void ReadFile(final File file) throws FileNotFoundException {
		
//		System.out.println("1");
		
		Scanner reader = new Scanner(file);
		
		while(reader.hasNextLine()) {
			fileContent.add(reader.nextLine());
		}
		
		
		
		
	}
	
	public static void Analyze() {
		for(int i = 0; i< fileContent.size();i++) {
			
			
			
			
			int firstComma = fileContent.get(i).indexOf(",");
			
			String sentence = fileContent.get(i).substring(firstComma + 1);
			
			String temp = GetFrameworks(sentence);
			
//			if(fileContent.get(i).length()>0 && fileContent.get(i).charAt(0) == '#') {
//				while(sentence.contains("|")) {
//					
////					if(sentence.length()<=2) {						
////						MockedObjectsInEachFile.add(Character.toString(sentence.charAt(0)));
////						break;
////					}
//					int next_separator = sentence.indexOf("|");
//					MockedObjectsInEachFile.add(sentence.substring(0,next_separator));
//					if(sentence.length()<=2) {
//						break;
//					}
//					
//					sentence = sentence.substring(next_separator+1);					
//					
//					
//				}
//			}
			
			
			
			if(temp != "x") {
				if(frameworks.contains(temp)) {
					
					int position = frameworks.indexOf(temp);
					
					ALL.get(position).add(fileContent.get(i));
					
				}
				else {
					frameworks.add(temp);
					
					ArrayList<String> new_framework_methods = new ArrayList<>();
					
					new_framework_methods.add(fileContent.get(i));
					
					ALL.add(new_framework_methods);
				}
			}
			
			
		}
		
		output();
	}
	
	public static String GetFrameworks(String sentence) {
		
		sentence = sentence.toLowerCase();
		
		
		
		
//		System.out.println(sentence);
		
		if(sentence.contains(".")) {
			int nextPosition = sentence.indexOf(".");
			
//			System.out.println(sentence.substring(0,nextPosition));
			
			if(sentence.substring(0,4).equals("java") && sentence.contains("<") && sentence.contains("com.datatorrent")) {
				return "datatorrent";
			}
			
			else if(to_be_excluded.contains(sentence.substring(0,nextPosition))) {
				
				sentence = sentence.substring(nextPosition + 1);
//				System.out.println(sentence);
				return GetFrameworks(sentence);
			}
			
			else {
				String result = sentence.substring(0,nextPosition);
				
//				special handling for github
				if (result.equals("github")) {
					int nextPosition0 = sentence.indexOf(".");
					sentence = sentence.substring(nextPosition0 + 1); //this will start from the github username.
					int nextPosition2 = sentence.indexOf(".");
					sentence = sentence.substring(nextPosition2+1);
					int nextPosition3 = sentence.indexOf(".");
					result = sentence.substring(0, nextPosition3);
				}
				
//				System.out.println(result);
				
				return result;
			}
			
			
		}
		
		return "x";
	}
	
	public static void output() {
		
//		for(String num: MockedObjectsInEachFile) {//trying to separate the counts for 1 to 9 mocked objects for a single file, 10 or more will be counted together.
//			
//			if(Integer.parseInt(num)<10) {
//				if(MockedObjectsCategory.contains(num)) {
//					int temp = MockedObjectsCategory_counts.get(MockedObjectsCategory.indexOf(num)) + 1 ;
//					
//					MockedObjectsCategory_counts.set(MockedObjectsCategory.indexOf(num), temp);
//				}
//				else {
//					MockedObjectsCategory.add(num);
//					MockedObjectsCategory_counts.add(1);
//				}
//			}
//			else {
//				if(MockedObjectsCategory.contains("10 or greater")) {
//					int temp2 = MockedObjectsCategory_counts.get(MockedObjectsCategory.indexOf("10 or greater")) + 1;
//					MockedObjectsCategory_counts.set(MockedObjectsCategory.indexOf("10 or greater"), temp2);
//				}
//				else {
//					MockedObjectsCategory.add("10 or greater");
//					MockedObjectsCategory_counts.add(1);
//				}
//				
//			}
//			
//			
//			
//			
//		}
//		
//		try {
//			FileWriter newFile2 = new FileWriter("D:\\Stevens\\2021 summer general\\RQ2\\mocked objects in single file summary.csv");
//			
//			newFile2.append("# of Mocked objects in a single file, count");
//			newFile2.append("\n");
//			
//			for(int x = 0; x< MockedObjectsCategory.size();x++) {
//				newFile2.append(MockedObjectsCategory.get(x)+",");
//				
//				newFile2.append(MockedObjectsCategory_counts.get(x).toString());
//				newFile2.append("\n");
//			}
//			
//			newFile2.flush();
//			
//			System.out.println("succeed2");
//		}
//		catch(IOException e){
//			System.out.println("failed");
//		}
		
		
		
		
		
		for (int  i = 0; i < ALL.size();i++) {
			
			ArrayList<String> currentList = ALL.get(i);
			
			methods.clear();
			origins.clear();
			counts.clear();
			
			for(int j = 0; j < currentList.size(); j++) {
				
				String this_sentence = currentList.get(j);
				
				
				int nextComma = this_sentence.indexOf(",");
				
				String this_method = this_sentence.substring(0, nextComma);
				
//				System.out.println(this_method);
				
				this_sentence = this_sentence.substring(nextComma+1);
				
				
				
				nextComma = this_sentence.indexOf(",");
				
				String this_origin = this_sentence.substring(0,nextComma);
				
				this_sentence = this_sentence.substring(nextComma+1);
				
				
				int this_count = 0;
				
				
				if(this_sentence.contains(",")) {
					nextComma = this_sentence.indexOf(",");
					
					this_count = Integer.parseInt(this_sentence.substring(0, nextComma));
				}
				else {
					this_count = Integer.parseInt(this_sentence);
				}
				
				
				
				
				if(methods.contains(this_method)) {
					
					int new_count = counts.get(methods.indexOf(this_method)) + this_count;
					
					counts.set(methods.indexOf(this_method), new_count);
				}
				
				else {
					methods.add(this_method);
					
					origins.add(this_origin);
					
					counts.add(this_count);
				}

//				
//				ALL.get(i).get(j)
			}
			
			try {
				FileWriter newFile = new FileWriter(OutputDirectoryPath + "\\RQ2\\Mocking frameworks API usage summary\\" + frameworks.get(i) +".csv");
				
				newFile.append("method, origin, count");
				newFile.append("\n");
				
				for(int k = 0; k< methods.size();k++) {
					newFile.append(methods.get(k)+",");
					newFile.append(origins.get(k)+",");
					newFile.append(counts.get(k).toString());
					newFile.append("\n");
				}
				
				newFile.flush();
				
				System.out.println("succeed");
			}
			catch(IOException e){
				System.out.println("failed");
			}
		}
	}
	
	public static void main(String[] args) throws IOException {
		final File folder = new File(OutputDirectoryPath + "\\Mocking framework API calls data");
		
		ReadALLFiles(folder);
		
		System.out.println(fileContent.size());
		
		final File folder2 = new File(OutputDirectoryPath + "\\RQ2\\single file mocked classes");
		ReadAll2(folder2);
		
		
//		for (String item:MockedObjectsInEachFile) {
//			System.out.println(item);
//		}
		
//		System.out.println(fileContent.get(0).contains("."));
		
		System.out.println(frameworks);
		
		System.out.println(ALL.get(0).size());
		
//		for(String item : ALL.get(0)) {
//			System.out.println(item);
//		}
		
//		System.out.println(MockedObjectsInEachFile);
		System.out.println(MockedObjectsCategory_counts);
		
//		System.out.println(to_be_excluded.contains("mockito"));
		
		
	}
	

}
