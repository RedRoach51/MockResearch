package de.vogella.jdt.astsimple.handlers;

import java.util.Map;
import java.util.HashMap;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

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
import org.eclipse.jdt.core.dom.MethodDeclaration;

import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.MethodInvocation;

public class GetInfo_Erick extends AbstractHandler {

	private static File textLog;
    private static final String JDT_NATURE = "org.eclipse.jdt.core.javanature";

// Certain classes are failing for unexplained reasons.
    private static final String[] blockedClasses = {"ServerContextTest.java"};
    

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        
    	textLog = new File("MethodsInvoked.txt");
        textLog.delete();
        textLog = new File("MethodsInvoked.txt");
        
    	System.out.println("~~ Clear! ~~");
    	IWorkspace workspace = ResourcesPlugin.getWorkspace();
        IWorkspaceRoot root = workspace.getRoot();
        System.out.println("Root: " + root);
        // Get all projects in the workspace
        IProject[] projects = root.getProjects();
        // Loop over all projects
        System.out.println("Number of Projects: " + projects.length);
        for (IProject project : projects) {
            try {
            	
            	System.out.println("Project: " + project.getName());
//                if (project.isNatureEnabled(JDT_NATURE)) {
                	System.out.println("Analyzing...");
                	analyseMethods(project);
//                }
                
            } catch (CoreException e) {
                e.printStackTrace();
            }
        }

        
        return null;
    }

    private void analyseMethods(IProject project) throws JavaModelException {
    	System.out.println(project.getName());
    	IPackageFragment[] packages = JavaCore.create(project).getPackageFragments();
        // parse(JavaCore.create(project));
    	System.out.println("Number of Packages: " + packages.length);
        for (IPackageFragment mypackage : packages) {
            if (mypackage.getKind() == IPackageFragmentRoot.K_SOURCE) {
                createAST(mypackage);
            }
        }
    }

    private void createAST(IPackageFragment mypackage) throws JavaModelException {
        for (ICompilationUnit unit : mypackage.getCompilationUnits()) {
            // now create the AST for the ICompilationUnits
        	System.out.println("File: " + unit.getElementName());
        	if (!unit.getElementName().toLowerCase().contains("test")) {
        		System.out.println("Not Test File, Ignored");
        		continue;
        	}
        	
        	
            CompilationUnit parse = parse(unit);
            
            MethodVisitor visitor = new MethodVisitor();
            parse.accept(visitor);

            /*for (MethodInvocation method : visitor.getMethodsInvoked()) {
            	ITypeBinding binding = method.resolveMethodBinding().getDeclaringClass();
            	System.out.println("   Method name: " + method.getName());
            	System.out.println("      Class origin: " + binding.getQualifiedName());

            }*/
            
            try {
            	FileWriter textWriter = new FileWriter(textLog.getName(), true);
                
//            	System.out.println("File: " + unit.getElementName());
                textWriter.write("File: " + unit.getElementName() + "\n");
                
                System.out.println("Methods Invoked (" + visitor.getMethodsInvoked().size() + "):");
                textWriter.write("Methods Invoked (" + visitor.getMethodsInvoked().size() + "):" + "\n");
                
                if (visitor.getMethodsInvoked().size() == 0) {
                	continue;
                }
                
            	Map <String,Integer> methods = visitor.getDetailedMethodsInvoked();
                
            	for (Map.Entry<String,Integer> methodEntry: methods.entrySet()) {
                	System.out.println("   Invoked Method: " + methodEntry.getKey());
                	textWriter.write("   Invoked Method: " + methodEntry.getKey() + "\n");
                	
                	System.out.println("   Count: " + methodEntry.getValue());
                	textWriter.write("   Count: " + methodEntry.getValue() + "\n");
                }
                
            	System.out.println("");
                textWriter.write("\n");
            	textWriter.close();
            } catch (IOException e) {
            	System.out.println("Error: IOException.");
            	e.printStackTrace();
            }
            

        }
    }

    /**
     * Reads a ICompilationUnit and creates the AST DOM for manipulating the
     * Java source file
     *
     * @param unit
     * @return
     */

    static CompilationUnit parse(ICompilationUnit unit) {
        ASTParser parser = ASTParser.newParser(AST.JLS3);
        parser.setKind(ASTParser.K_COMPILATION_UNIT);
        parser.setSource(unit);
        parser.setResolveBindings(true);
        return (CompilationUnit) parser.createAST(null); // parse
    }
}