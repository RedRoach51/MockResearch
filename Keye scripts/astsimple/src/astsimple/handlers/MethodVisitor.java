package astsimple.handlers;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;


public class MethodVisitor extends ASTVisitor{

	List<MethodDeclaration> methods = new ArrayList<>();

	@Override
    public boolean visit(MethodDeclaration node) {
        methods.add(node);
        return super.visit(node);
    }

    public List<MethodDeclaration> getMethods() {
        return methods;
    }
    
    
    List<MethodInvocation> methodInvocation = new ArrayList<>();
    
    @Override
    public boolean visit(MethodInvocation node) {
    	
    	methodInvocation.add(node);
    	return super.visit(node);
    }
    
    public List<MethodInvocation> getMethodInvocations(){
    	return methodInvocation;
    }
    
    
}
