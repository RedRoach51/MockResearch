import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class API_Analyzer {
	
	
	static ArrayList<String> text = new ArrayList<>();
	
	static ArrayList<String> methods = new ArrayList<>();
	static ArrayList<String> origins = new ArrayList<>();
	static ArrayList<String> counts = new ArrayList<>();
	
	static ArrayList<String> RQ4_dataFromImport = new ArrayList<>();
	static ArrayList<String> RQ4_MockFileNotInTest = new ArrayList<>();
	static ArrayList<String> RQ4_ProjectName = new ArrayList<>();
	static ArrayList<String> RQ4_FrameworkUsed = new ArrayList<>();
	
	static boolean method_added = false;
	
	static ArrayList<String> MockedObjectsCountForEachFile = new ArrayList<>();
	
	
	static ArrayList<String> exclude_names = new ArrayList<>(Arrays.asList("test.txt","limits (overall).txt"));
	
	static ArrayList<String> MockedObject_identifier = new ArrayList<>(Arrays.asList("mock","createMock","createNiceMock"));
	
	static ArrayList<String> verified_MockFrameworks = new ArrayList<>(Arrays.asList(
			
			"Powermock",
			"Easymock",
			"Wiremock",
			"Mockwebserver",
			"Mockito",
			"Mockit",
			"mockejb",
			"mockserver",
			"mockwebserver",
			"mockftpserver",
			"Mockrunner"
			
			));
	
//	public static String target = "apex-core";
	
	
	
	public static void GoOverALLFiles(final File folder) throws FileNotFoundException {
		for(final File entry : folder.listFiles()) {
			
			text.clear();
			
			methods.clear();
			origins.clear();
			counts.clear();
			MockedObjectsCountForEachFile.clear();
			
			if(entry.getName().contains(".txt") && !exclude_names.contains(entry.getName())) {
				Analyze(entry);
			}
		}
		
		
	}
	
	
	
	
	
	public static void Analyze(final File file) throws FileNotFoundException {
		
		int positionOfDot = file.getName().indexOf(".");
		
		
		
//		File file = new File("D:\\Stevens\\2021 summer general\\Mocking framework API calls data\\" + target + ".txt");
		
		Scanner Reader = new Scanner(file);
		
		while(Reader.hasNextLine()) {
			String line = Reader.nextLine();
			text.add(line);
		}
		
		
//		for(String line : text) {
////			if(!line.contains("Class origin") && line.contains(".java") && !line.toLowerCase().contains("src/test")){
//			if(!line.contains("Class origin") && line.contains(".java") && !line.toLowerCase().contains("test")){
//				RQ4_MockNotInTest.add(line);
//				RQ4_ProjectName.add(file.getName().substring(0,positionOfDot));
//			}
//		}
		
		
		
		
		for(int i = 0; i<text.size();i++) {
			method_added = false;
			
			if(!text.get(i).equals(" ") && text.get(i).length()!=0) {
				if(text.get(i).substring(1,7).equals("Method")) {
					
					if(MockedObject_identifier.contains(text.get(i).substring(14))) {
						MockedObjectsCountForEachFile.add(text.get(i+2).substring(8));
					}
					
					
					
					for(int j = 0;j < methods.size(); j++) {
						if (methods.get(j).equals(text.get(i).substring(14))) {
							
							int temp = Integer.parseInt(counts.get(j)) + Integer.parseInt(text.get(i+2).substring(8));
							
							counts.set(j, String.valueOf(temp));
							
							
							method_added = true;
							break;
						}
					}
					
					
					if(method_added == false) {
						
						String temp = text.get(i+1).substring(15);
						
						if(temp.contains(",")) {
							temp = temp.replace(",", "|");
							System.out.println(temp);
						}
						
						methods.add(text.get(i).substring(14));
						origins.add(temp);
						counts.add(text.get(i+2).substring(8));
						
					}
				}
			}
		}
		
		
//		System.out.println(text.size());
//		System.out.println(methods.size());
		
		output(file);
//		output2(file);
	}
	
	
	public static void output(final File file) {
		
		int positionOfDot = file.getName().indexOf(".");
		
		int MockedObject_count = 0;
		
		
		try {
			FileWriter newFile = new FileWriter("D:\\Stevens\\2021 summer general\\Mocking framework API calls data\\" + file.getName().substring(0,positionOfDot) +"Analysis.csv");
			
			newFile.append("method, origin, count");
			newFile.append("\n");
			
			for(int i = 0; i< methods.size();i++) {
				newFile.append(methods.get(i)+",");
				newFile.append(origins.get(i)+",");
				newFile.append(counts.get(i).toString());
				newFile.append("\n");
				
				if(MockedObject_identifier.contains(methods.get(i))) {
					MockedObject_count += Integer.parseInt(counts.get(i));
				}
			}
			
			newFile.append("\n");
			newFile.append("MockedObjects: " + ","+ String.valueOf(MockedObject_count));
			newFile.append("\n");
			
			newFile.append("\n");
			StringBuilder SB = new StringBuilder();
			
			for(String num: MockedObjectsCountForEachFile) {
				SB.append(num);
				SB.append("|");
			}
			
			newFile.append("# of Mocked objects each file:" +"," +SB);
			newFile.append("\n");
			
			
			newFile.flush();
			
			System.out.println("succeed");
		}
		catch(IOException e){
			System.out.println("failed");
		}
		
	}
	
	
	
	
	
	public static void Analyze2(File file) throws FileNotFoundException {
		/*
		 * RQ4 analyze
		 */
		
		Scanner RQ4reader = new Scanner(file);
		
		while(RQ4reader.hasNext()) {
			RQ4_dataFromImport.add(RQ4reader.nextLine());
		}
		
		RQ4reader.close();
		
//		String [] test = RQ4_dataFromImport.get(1).split(",");
//		
//		System.out.println(test [0]);
//		System.out.println(test [1]);
//		System.out.println(test [2]);
//		System.out.println(test [3]);
		
		
		for (String line : RQ4_dataFromImport) {
			String [] splitted = line.split(",");
			
			if(!splitted[1].toLowerCase().contains("test") && verified_MockFrameworks.contains(splitted[2])) {
				RQ4_MockFileNotInTest.add(splitted[1]);
				RQ4_ProjectName.add(splitted[0]);
				RQ4_FrameworkUsed.add(splitted[2]);
			}
		}
		
		
		output2();
		
	}
	
	public static void output2() {
		
		
		ArrayList<String> uniqueName = new ArrayList<>();
		
		for(String name : RQ4_ProjectName) {
			if(!uniqueName.contains(name)) {
				uniqueName.add(name);
			}
		}
		
		
		
		try {
			FileWriter newFile = new FileWriter("D:\\Stevens\\2021 summer general\\RQ4\\" +"non-test files with mocking framework" +".csv");
			
			newFile.append("project name, file path");
			newFile.append("\n");
			
			for (int i = 0; i< RQ4_MockFileNotInTest.size();i++) {
				newFile.append(RQ4_ProjectName.get(i) + ",");
				newFile.append(RQ4_MockFileNotInTest.get(i)+",");
				newFile.append(RQ4_FrameworkUsed.get((i)));
				newFile.append("\n");
				
				
			}
			
			newFile.append("# of unique projects" + "," + uniqueName.size());
			
			newFile.flush();
			
			System.out.println("succeed");
			
			
		}
		catch(IOException e){
			System.out.println("failed");
		}
		
	}
	
	
//	public static void output2(final File file) {
//		
//		int positionOfDot = file.getName().indexOf(".");
//		
//		try {
//			FileWriter newFile = new FileWriter("D:\\Stevens\\2021 summer general\\Mocking framework API calls data\\" + file.getName().substring(0,positionOfDot) +" ");
//			
//		}
//		catch(IOException e) {
//			System.out.println("failed");
//		}
//	}
//	
	
	
	
	
	public static void main(String[] args) throws FileNotFoundException {
		
		API_Analyzer test = new API_Analyzer();
		
//		test.Analyze();
		
		final File folder = new File("D:\\Stevens\\2021 summer general\\Mocking framework API calls data");
		
//		final File folder = new File("D:\\Stevens\\2021 summer general\\test");
		
		GoOverALLFiles(folder);
		
		File RQ4_needed = new File("D:\\Stevens\\2021_summer_project\\project folders 58-78\\UndProjects\\AllMockImports.csv");
		
		Analyze2(RQ4_needed);
		
		
		
//		System.out.println(text.get(2).substring(1,7).equals("Method"));
		System.out.println(origins);
		
		
		
	}

}
