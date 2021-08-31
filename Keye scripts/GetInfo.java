package astsimple.handlers;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;

public class GetInfo extends AbstractHandler{
	private static final String JDT_NATURE = "org.eclipse.jdt.core.javanature";
	
	private boolean file_stated = false;
	private boolean method_found = false;
	
	private ArrayList<String> method_names = new ArrayList<>();
	private ArrayList<String> method_origin = new ArrayList<>();
	private ArrayList<Integer> method_count = new ArrayList<>();
	
	private ArrayList<String> ALL = new ArrayList<>();
	

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        IWorkspace workspace = ResourcesPlugin.getWorkspace();
        IWorkspaceRoot root = workspace.getRoot();
        // Get all projects in the workspace
        IProject[] projects = root.getProjects();
        // Loop over all projects
        for (IProject project : projects) {
            try {
                if (project.isNatureEnabled(JDT_NATURE)) {
                    analyseMethods(project);
                }
            } catch (CoreException e) {
                e.printStackTrace();
            }
        }
        
        try {
			outputTXT(projects[0]);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
        return null;
    }

    private void analyseMethods(IProject project) throws JavaModelException {
        IPackageFragment[] packages = JavaCore.create(project).getPackageFragments();
        // parse(JavaCore.create(project));
        for (IPackageFragment mypackage : packages) {
            if (mypackage.getKind() == IPackageFragmentRoot.K_SOURCE) {
            	
                createAST(mypackage);
            }

        }
    }

    private void createAST(IPackageFragment mypackage) throws JavaModelException {
        for (ICompilationUnit unit : mypackage.getCompilationUnits()) {
            // now create the AST for the ICompilationUnits
            CompilationUnit parse = parse(unit);
            MethodVisitor visitor = new MethodVisitor();
            parse.accept(visitor);
            
            method_names.clear();
        	method_origin.clear();
        	method_count.clear();
           
//            System.out.println(unit.getPath().toString());
//            System.out.println("Methods declared: ");
//            for (MethodDeclaration method : visitor.getMethods()) {
//            	
//                System.out.println("	Method name: " + method.getName()
//                        + " 	Return type: " + method.getReturnType2());
//            }
//            
//            System.out.println("Methods Invoked: ");
            for(MethodInvocation method: visitor.getMethodInvocations()) {
            	
            	
            	
            	
//            	System.out.println("	Method Name: "+method.getName());
            	if(method.resolveMethodBinding() != null && method.resolveMethodBinding().getDeclaringClass()!= null
            			&& (method.resolveMethodBinding().getDeclaringClass().getQualifiedName()
            			.toLowerCase().contains("mock")&&!method.resolveMethodBinding().getDeclaringClass().getQualifiedName()
                    			.toLowerCase().contains("apache"))) {
            		
            		if(file_stated == false) {
            			System.out.println(unit.getPath().toString());
            			System.out.println("Methods Invoked: ");
            			
            			ALL.add(unit.getPath().toString());
            			ALL.add("Methods Invoked: ");
            			
//            			try {
//                  	      FileWriter myWriter = new FileWriter("D:\\Stevens\\2021 summer general\\Mocking framework API calls data\\camel.txt");
//                  	      
//                  	      
//                  	      myWriter.write(unit.getPath().toString());
//                  	      myWriter.write("Methods Invoked: ");
//                  	      
//                  	      myWriter.close();
//                  	      
//                  	    } catch (IOException e) {
//                  	      
//                  	      e.printStackTrace();
//                  	    }
              	      
            			file_stated = true;
            		}
            		
            		for(int i = 0;i < method_names.size(); i++) {
            			if(method_names.get(i).equals(method.getName().toString()) && 
            					method_origin.get(i).equals(method.resolveMethodBinding().getDeclaringClass().getQualifiedName().toString())) {
            				method_count.set(i, method_count.get(i) + 1);
            				method_found = true;
            				break;
            			}
            		}
            		
            		if(!method_found) {
            			method_names.add(method.getName().toString());
                		method_origin.add(method.resolveMethodBinding().getDeclaringClass().getQualifiedName());
                		method_count.add(1);
            		}
            		
            		
            		
//            		System.out.println("	Method Name: " + method.getName());
//            		
//                	ITypeBinding binding = method.resolveMethodBinding().getDeclaringClass();
//                	
////                	ITypeBinding binding2 = method.resolveMethodBinding().getDeclaringClass();
//                	
//                	if(binding!=null) {
//                		System.out.println("	Class origin: " + binding.getQualifiedName());
//                	}
            		
            		
            		
            		
            		
//            		this part is what I use to test to get the dependency classes, which are the classes that are mocked, you can ignore this
//            		so far.
            		
//            		System.out.println("333333333" + method.getName().toString());
//                	System.out.println("333333333" + method.resolveMethodBinding().getDeclaringClass().getQualifiedName());
//                	System.out.println("333333333" + method.resolveTypeBinding().getQualifiedName());
//                	System.out.println("333333333" + method.getParent().toString());
            		
            		
                	
                	
            	}
            	
            	
            	
            	
            }
            
            for(int i = 0; i< method_names.size();i++) {
            	System.out.println("	Method name: " + method_names.get(i));
            	System.out.println("	Class origin: " + method_origin.get(i));
            	System.out.println("	Count: " + method_count.get(i));
            	System.out.println(" ");
            	
            	
            	ALL.add("	Method name: " + method_names.get(i));
            	ALL.add("	Class origin: " + method_origin.get(i));
            	ALL.add("	Count: " + method_count.get(i));
            	ALL.add(" ");
            	
//            	try {
//            	      FileWriter myWriter = new FileWriter("D:\\Stevens\\2021 summer general\\Mocking framework API calls data\\camel.txt");
//            	      
//            	      myWriter.write("		Method name: " + method_names.get(i));
//            	      myWriter.write("		Class origin: " + method_origin.get(i));
//            	      myWriter.write("		Count: " + method_count.get(i));
//            	      myWriter.write(" ");
//            	      
//            	      myWriter.close();
//            	      System.out.println("Successfully wrote to the file.");
//            	    } catch (IOException e) {
//            	      System.out.println("An error occurred.");
//            	      e.printStackTrace();
//            	    }
            }
            
            file_stated = false;
            method_found = false;

        }
    }

    /**
     * Reads a ICompilationUnit and creates the AST DOM for manipulating the
     * Java source file
     *
     * @param unit
     * @return
     */

    private static CompilationUnit parse(ICompilationUnit unit) {
        ASTParser parser = ASTParser.newParser(AST.JLS16);
        parser.setKind(ASTParser.K_COMPILATION_UNIT);
        parser.setSource(unit);
        parser.setResolveBindings(true);
        return (CompilationUnit) parser.createAST(null); // parse
    }
    
    
    private void outputTXT(IProject project) throws IOException{
    	
    	
//    	PrintWriter writer = new PrintWriter("D:\\Stevens\\2021 summer general\\Mocking framework API calls data"
//    				+ "\\test.txt", "UTF-8");
//    		
//    	writer.println("line 3");
//    	writer.println("line 4");
//    	
//    	writer.close();
    	
    	PrintWriter writer = new PrintWriter("D:\\Stevens\\2021 summer general\\Mocking framework API calls data\\"
				+ "any23" + ".txt", "UTF-8");
    	
    	
    	
    	for(String x: ALL) {
    		writer.println(x);
    	}
    	
    	
    	
    	writer.close();
    	
    	System.out.println(ALL.size());
    	
    	ALL.clear();
    }
    
}  
    

