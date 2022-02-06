import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class DependecyMockedCalculation {

	static ArrayList<String> projects = new ArrayList<>();
	static ArrayList<String> files = new ArrayList<>();
	static ArrayList<String> num_of_dependencies = new ArrayList<>();
	static ArrayList<String> num_of_mocked = new ArrayList<>();
	
	// "File path to directory where output data from GetInfo is stored."
	private static String OutputDirectoryPath = "E:\\eclipse\\SummerResearch";
	
	
	public static void ReadAll1(final File folder, final File folder2) throws FileNotFoundException, IOException{//import classes
		
		for(final File entry: folder.listFiles()) {
			if(entry.getName().contains(".txt")) {
				String name = entry.getName();
				int pos = name.indexOf(" Import");
				name = name.substring(0,pos);
//				projects.add(name);
				
				ReadFile1(entry, name);
			}
		}
		
		ReadAll2(folder2);
	}
	
	public static void ReadFile1 (final File file, String projectName) throws FileNotFoundException, IOException{//import classes
		ArrayList<String> fileContent = new ArrayList<>();
		
		Scanner reader = new Scanner(file);
		
		while(reader.hasNextLine()) {
			fileContent.add(reader.nextLine());
		}
		
		int file_counter = 0;
		int class_counter = 0;
		
		
		for(int i = 0; i<fileContent.size();i++) {
			
			if(fileContent.get(i).contains(".java") && fileContent.get(i).contains("/")) {//start of a file
				files.add(fileContent.get(i));
				file_counter ++;
				class_counter = 0;
				
			}
			
			if(i == fileContent.size()-1) {
				class_counter++;
				num_of_dependencies.add(Integer.toString(class_counter));
				break;
			}
			
			else if((fileContent.get(i+1).contains(".java") && fileContent.get(i+1).contains("/"))) {//end of a file
				
				
				if(!fileContent.get(i).contains(".java") && !fileContent.get(i).contains("/")) {
					class_counter++;
				}
				
				num_of_dependencies.add(Integer.toString(class_counter));
			}
			
			else {
				if(!fileContent.get(i).contains(".java") && !fileContent.get(i).contains("/")) {
					class_counter++;
				}
			}
		}
		
		for(int j = 0; j <file_counter;j++) {
			projects.add(projectName);		
		}
		
		
		
	}
	
//	At this point, for this specific project, project, files, num_of_dependencies lists should be filled perfectly
	public static void ReadAll2(final File folder2) throws FileNotFoundException, IOException {//mocked classes
		
		for(final File entry: folder2.listFiles()) {
			if(entry.getName().contains(".txt")) {
				ReadFile2(entry);
			}
		}
		
		output();
	}
	
//	the order of text files should be the same in terms of projects, as well as the order of .java files in one project text file.
	public static void ReadFile2 (final File file) throws FileNotFoundException, IOException{//mocked classes
		ArrayList<String> fileContent = new ArrayList<>();
		ArrayList<String> classes_filtered = new ArrayList<>();
		
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
				classes_filtered.clear();
			}
			
			if(i == fileContent.size()-1) {
				if(!(fileContent.get(i).contains(".java") && fileContent.get(i).contains("/"))){
					if(!classes_filtered.contains(fileContent.get(i))) {
						classes_filtered.add(fileContent.get(i));
					}
				}
				
				num_of_mocked.add(Integer.toString(classes_filtered.size()));
				break;
			}
			
			if((fileContent.get(i+1).contains(".java") && fileContent.get(i+1).contains("/"))) {//end of a file
				
				if(!(fileContent.get(i).contains(".java") && fileContent.get(i).contains("/"))){
					if(!classes_filtered.contains(fileContent.get(i))) {
						classes_filtered.add(fileContent.get(i));
					}
				}
				
				num_of_mocked.add(Integer.toString(classes_filtered.size()));
			}
			
			else {
//				class_counter++;
				if(!(fileContent.get(i).contains(".java") && fileContent.get(i).contains("/"))){
					if(!classes_filtered.contains(fileContent.get(i))) {
						classes_filtered.add(fileContent.get(i));
					}
				}
			}
		}
		
	}
	
	public static void output() throws IOException {
		FileWriter newFile = new FileWriter(OutputDirectoryPath + "\\RQ2\\mocked dependencies calculation summary.csv");
		
		newFile.append("project name, file path, num of dependency classes, num of mocked classes");
		newFile.append("\n");
		
		System.out.println(num_of_dependencies);
		System.out.println(num_of_mocked);
		System.out.println(num_of_dependencies.size());
		System.out.println(num_of_mocked.size());
		
		for(int i = 0; i< projects.size(); i++) {
			newFile.append(projects.get(i) + ",");
			newFile.append(files.get(i) + ",");
			newFile.append(num_of_dependencies.get(i) + ",");
			newFile.append(num_of_mocked.get(i) + ",");
			newFile.append("\n");
		}
		
		newFile.flush();
		
		projects.clear();
		files.clear();
		num_of_dependencies.clear();
		num_of_mocked.clear();
		
		
		System.out.println("success");
	}
	
	public static void main (String[] args) throws IOException {
		
		
		final File folder1 = new File(OutputDirectoryPath + "\\RQ2\\single file import classes");
		final File folder2 = new File(OutputDirectoryPath + "\\RQ2\\single file mocked classes");
		ReadAll1(folder1, folder2);
				
		
	}
	
}
