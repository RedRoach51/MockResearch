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
	static boolean method_added = false;
	
	static ArrayList<String> MockedObjectsCountForEachFile = new ArrayList<>();
	
	
	static ArrayList<String> exclude_names = new ArrayList<>(Arrays.asList("test.txt","limits (overall).txt"));
	
	static ArrayList<String> MockedObject_identifier = new ArrayList<>(Arrays.asList("mock","createMock"));
	
	
	
	
	
	
	
	public static String target = "apex-core";
	
	
	
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
		
		
		
		
		
//		File file = new File("D:\\Stevens\\2021 summer general\\Mocking framework API calls data\\" + target + ".txt");
		
		Scanner Reader = new Scanner(file);
		
		while(Reader.hasNextLine()) {
			String line = Reader.nextLine();
			text.add(line);
		}
		
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
		
		
		
		
		
//		System.out.println(text.get(2).substring(1,7).equals("Method"));
		System.out.println(origins);
		
		
		
	}

}
