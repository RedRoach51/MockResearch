

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
	
	
	static ArrayList<ArrayList<String>> ALL = new ArrayList<>();
	
	static ArrayList<String> to_be_excluded = new ArrayList<>(Arrays.asList("okhttp3","org","io","com","compile","google","api","net","jvnet","javacrumbs"));
	
	
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
				
//				System.out.println(result);
				
				return result;
			}
			
			
		}
		
		return "x";
	}
	
	public static void output() {
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
				FileWriter newFile = new FileWriter("D:\\Stevens\\2021 summer general\\Mocking frameworks API usage summary\\" + frameworks.get(i) +".csv");
				
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
	
	public static void main(String[] args) throws FileNotFoundException {
		final File folder = new File("D:\\Stevens\\2021 summer general\\Mocking framework API calls data");
		
		ReadALLFiles(folder);
		
		System.out.println(fileContent.size());
		
		
		for (String item:fileContent) {
			System.out.println(item);
		}
		
//		System.out.println(fileContent.get(0).contains("."));
		
		System.out.println(frameworks);
		
		System.out.println(ALL.get(0).size());
		
		for(String item : ALL.get(0)) {
			System.out.println(item);
		}
		
//		System.out.println(to_be_excluded.contains("mockito"));
		
		
	}
	

}
