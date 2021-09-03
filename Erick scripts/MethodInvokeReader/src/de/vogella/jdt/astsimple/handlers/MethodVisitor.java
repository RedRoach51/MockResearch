package de.vogella.jdt.astsimple.handlers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;

public class MethodVisitor extends ASTVisitor {
	
    List<MethodInvocation> methodsInvo = new ArrayList<>();
    Map<String, Integer> detailedMethods = new HashMap<String, Integer>();
    String currentClass = null;
    String ImportFilter = "mock";
    
    @Override
    public boolean visit(TypeDeclaration node) {

    	// Skip this node if Node is not a 'test' file.

/*
    	if (!node.getName().toString().toLowerCase().contains("test")){	
			System.out.println(" - Not a Test File, Ignored");
    		return false;
    	}
*/
    	currentClass = node.getName().toString();
    	return super.visit(node);
    }
    
    
    @Override
    public boolean visit(MethodInvocation node) {
    	if (node.resolveMethodBinding() == null){
    		return super.visit(node);
    	}
    	if (node.resolveMethodBinding().getDeclaringClass() == null) {
    		return super.visit(node);
    	}
    	methodsInvo.add(node);

    	
    	
    	String name = node.getName().toString();
    	String source = node.resolveMethodBinding().getDeclaringClass().getQualifiedName();

    	if (source.contains("java.") || source.contains("apache.")) {
    		return super.visit(node);
    	}
    	
    	if (source.contains("<")){
    		int bracket1 = source.indexOf("<");
    		int bracket2 = source.lastIndexOf(">");
    		source= source.substring(0,bracket1) + source.substring(bracket2 + 1);
    	}
    	
    	if (source.toLowerCase().contains(ImportFilter)){
    		String fullPath = source + "." + name;
    		if (!detailedMethods.containsKey(fullPath)){
    			detailedMethods.put(fullPath, 0);
    		}
    		detailedMethods.put(fullPath, detailedMethods.get(fullPath) + 1);
    	}

    	
    	


    	
/*    	String name = node.getName().toString();
    	String source = node.resolveMethodBinding().getDeclaringClass().getQualifiedName();
    	if (source.toLowerCase().contains(ImportFilter)){
    		String fullPath = source + "." + name;
    		if (!currentMethods.containsKey(fullPath)){
    			currentMethods.put(fullPath, 0);
    		}
    		currentMethods.put(fullPath, currentMethods.get(fullPath) + 1);
    	}
*/    	
    	return super.visit(node);
    }

    public List<MethodInvocation> getMethodsInvoked(){
    	return methodsInvo;
    }
    public Map<String,Integer> getDetailedMethodsInvoked(){
    	return detailedMethods;
    }
    

    	
/*    	System.out.println("   Method name: " + method.getName());
    	ITypeBinding binding = method.resolveMethodBinding().getDeclaringClass();
    	System.out.println("      Class origin: " + binding.getQualifiedName());	
*/
    
    public void setImportFilter(String keyword) {
    	
    }
}