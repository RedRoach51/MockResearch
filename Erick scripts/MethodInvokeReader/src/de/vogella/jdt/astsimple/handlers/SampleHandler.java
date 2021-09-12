package de.vogella.jdt.astsimple.handlers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.jface.dialogs.MessageDialog;

public class SampleHandler extends AbstractHandler {

	private static File textLog;	
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);

		
    	textLog = new File("MethodsInvoked.txt");
        textLog.delete();
        textLog = new File("MethodsInvoked.txt");
		
		List<IJavaElement> temp = AutoRefactorHandler.getSelectedJavaElements(event);
		
		for (IJavaElement unit : temp) {
	        // now create the AST for the ICompilationUnits
	    	System.out.println("File: " + unit.getElementName());
	    	if (!unit.getElementName().toLowerCase().contains("test")) {
	    		System.out.println("Not Test File, Ignored");
	    		continue;
	    	}
	    	
	    	
	        CompilationUnit parse = GetInfo_Erick.parse((ICompilationUnit) unit);
	        
	        MethodVisitor visitor = new MethodVisitor();
	        parse.accept(visitor);

	        /*for (MethodInvocation method : visitor.getMethodsInvoked()) {
	        	ITypeBinding binding = method.resolveMethodBinding().getDeclaringClass();
	        	System.out.println("   Method name: " + method.getName());
	        	System.out.println("      Class origin: " + binding.getQualifiedName());

	        }*/
	        
	        try {
	        	FileWriter textWriter = new FileWriter(textLog.getName(), true);
	            
//	        	System.out.println("File: " + unit.getElementName());
	            textWriter.write("File: " + unit.getElementName() + "\n");
	            
	            System.out.println("Methods Invoked (" + visitor.getMethodsInvoked().size() + "):");
	            textWriter.write("Methods Invoked (" + visitor.getMethodsInvoked().size() + "):" + "\n");
	            
	            if (visitor.getMethodsInvoked().size() == 0) {
	            	continue;
	            }
	            
	        	Map <String,Integer> methods = visitor.getDetailedMethodsInvoked();
	            
	        	for (Map.Entry<String,Integer> methodEntry: methods.entrySet()) {
	            	System.out.println("   Invoked Method (" + methodEntry.getValue() + ")"
	            			+ " : " + methodEntry.getKey());
	            	textWriter.write("   Invoked Method (" + methodEntry.getValue() + ")"
	            			+ " : " + methodEntry.getKey() + "\n");
	            }
	            
	        	System.out.println("");
	            textWriter.write("\n");
	        	textWriter.close();
	        } catch (IOException e) {
	        	System.out.println("Error: IOException.");
	        	e.printStackTrace();
	        }
	        

	    }
		System.out.println("SampleHandler Clear!");
		return null;
	}
}
