package astsimple.handlers;

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
import org.eclipse.jdt.core.dom.MethodInvocation;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;

public class GetInfo extends AbstractHandler
{

  private static final File OUTPUT_DIRECTORY = new File("C:\\Users\\wchwe\\ResearchCode\\mock-results");

  // Project Repository Folder
  private static final String PROJECT_REPOSITORY = "C:\\Users\\wchwe\\ResearchCode\\mockrepo";

  private static final String JDT_NATURE = "org.eclipse.jdt.core.javanature";

  private boolean file_stated = false;
  private boolean method_found = false;

  private ArrayList<String> method_names = new ArrayList<>();
  private ArrayList<String> method_origin = new ArrayList<>();
  private ArrayList<Integer> method_count = new ArrayList<>();

  private ArrayList<String> invokedMethods = new ArrayList<>();
  private ArrayList<String> mock_methodnames = new ArrayList<>(Arrays.asList("mock", "createMock", "createNiceMock"));

  private ArrayList<String> mockedClasses = new ArrayList<>();

  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException
  {
    IWorkspace workspace = ResourcesPlugin.getWorkspace();
    IWorkspaceRoot root = workspace.getRoot();
    // Get all projects in the workspace
    IProject[] projects = root.getProjects();
    // Loop over all projects

    for (IProject project : projects) {
      try {
        if (project.isNatureEnabled(JDT_NATURE)) {
          analyseAndWriteMethods(project);
        }
      } catch (CoreException e) {
        e.printStackTrace();
      }
    }

    return null;
  }

  private void analyseAndWriteMethods(IProject project) throws JavaModelException
  {
    IPackageFragment[] packages = JavaCore.create(project).getPackageFragments();
    for (IPackageFragment mypackage : packages) {
      if (mypackage.getKind() == IPackageFragmentRoot.K_SOURCE) {
        createAST(mypackage);
      }
    }
    write(project, OUTPUT_DIRECTORY);
  }

  private void createAST(IPackageFragment mypackage) throws JavaModelException
  {
    for (ICompilationUnit unit : mypackage.getCompilationUnits()) {
      // now create the AST for the ICompilationUnits
      CompilationUnit parse = parse(unit);
      MethodVisitor visitor = new MethodVisitor();
      parse.accept(visitor);

      method_names.clear();
      method_origin.clear();
      method_count.clear();

      for (MethodInvocation method : visitor.getMethodInvocations()) {

        // System.out.println(" Method Name: "+method.getName());
        if (method.resolveMethodBinding() != null && method.resolveMethodBinding().getDeclaringClass() != null
            && (method.resolveMethodBinding().getDeclaringClass().getQualifiedName()
                .toLowerCase().contains("mock")
                && !method.resolveMethodBinding().getDeclaringClass().getQualifiedName()
                    .toLowerCase().contains("apache"))) {

          if (file_stated == false) {
            // System.out.println(unit.getPath().toString());
            // System.out.println("Methods Invoked: ");

            // invokedMethods.add(unit.getPath().toString());
            // invokedMethods.add("Methods Invoked: ");

            file_stated = true;
          }

          for (int i = 0; i < method_names.size(); i++) {
            if (method_names.get(i).equals(method.getName().toString()) &&
                method_origin.get(i)
                    .equals(method.resolveMethodBinding().getDeclaringClass().getQualifiedName().toString())) {
              method_count.set(i, method_count.get(i) + 1);
              method_found = true;
              break;
            }
          }

          if (!method_found) {
            method_names.add(method.getName().toString());
            method_origin.add(method.resolveMethodBinding().getDeclaringClass().getQualifiedName());
            method_count.add(1);
          }

          if (mock_methodnames.contains(method.getName().toString())) {
            mockedClasses.add(method.resolveTypeBinding().getQualifiedName());
          }

        }

      }

      for (int i = 0; i < method_names.size(); i++) {
        // System.out.println(" Method name: " + method_names.get(i));
        // System.out.println(" Class origin: " + method_origin.get(i));
        // System.out.println(" Count: " + method_count.get(i));
        // System.out.println(" ");

        invokedMethods.add("	Method name: " + method_names.get(i));
        invokedMethods.add("	Class origin: " + method_origin.get(i));
        invokedMethods.add("	Count: " + method_count.get(i));
        invokedMethods.add(" ");

      }

      file_stated = false;
      method_found = false;

    }
  }

  /**
   * Reads a ICompilationUnit and creates the AST DOM for manipulating the Java
   * source file
   *
   * @param unit
   * @return
   */

  private static CompilationUnit parse(ICompilationUnit unit)
  {
    ASTParser parser = ASTParser.newParser(AST.JLS14);
    parser.setKind(ASTParser.K_COMPILATION_UNIT);
    parser.setSource(unit);
    parser.setResolveBindings(true);
    return (CompilationUnit) parser.createAST(null); // parse
  }

  private void write(IProject project, File outputDir)
  {
    String projectLocation = project.getLocation().toString();
    String relativePath = projectLocation.substring(PROJECT_REPOSITORY.length());
    String projectName = relativePath.substring(0,
        relativePath.contains("/") ? relativePath.indexOf("/") : relativePath.length());

    try {
      new File(
          outputDir.getAbsolutePath() + File.separatorChar + "Mocking_framework_API_calls_data").deleteOnExit();
      new File(
          outputDir.getAbsolutePath() + File.separatorChar + "Mocking_framework_API_calls_data").mkdir();
      File methodInvocationFile = new File(
          outputDir.getAbsolutePath() + File.separatorChar + "Mocking_framework_API_calls_data", projectName + ".txt");
      methodInvocationFile.createNewFile();
      PrintWriter methodInvocationWriter = new PrintWriter(
          new FileOutputStream(methodInvocationFile, true));
      for (String invocation : invokedMethods) {
        methodInvocationWriter.append(invocation + "\n");
      }
      methodInvocationWriter.flush();
      methodInvocationWriter.close();

      new File(
          outputDir.getAbsolutePath() + File.separatorChar + "RQ3_data").deleteOnExit();
      new File(
          outputDir.getAbsolutePath() + File.separatorChar + "RQ3_data").mkdir();
      File mockedClassFile = new File(outputDir.getAbsolutePath() + File.separatorChar + "RQ3_data",
          projectName + "Mocked_classes.txt");
      mockedClassFile.createNewFile();

      PrintWriter mockClassWriter = new PrintWriter(new FileOutputStream(mockedClassFile,
          true));
      for (String item : mockedClasses) {
        mockClassWriter.append(item + "\n");
      }
      mockClassWriter.flush();
      mockClassWriter.close();

      invokedMethods.clear();
      mockedClasses.clear();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void outputTXT() throws IOException
  {

    String target = "credur-whisker";

    PrintWriter writer = new PrintWriter("C:\\Users\\wchwe\\ResearchCode\\mock-results\\Mocking framework API calls data\\"
        + target + ".txt", "UTF-8");

    for (String x : invokedMethods) {
      writer.println(x);
    }

    writer.close();

    PrintWriter writer2 = new PrintWriter("C:\\Users\\wchwe\\ResearchCode\\mock-results\\RQ3 data\\" + target +
        "Mocked classes.txt", "UTF-8");

    for (String item : mockedClasses) {
      writer2.println(item);
    }

    writer2.close();

    // System.out.println(invokedMethods.size());

    invokedMethods.clear();

    // System.out.println(mockedClasses);
    mockedClasses.clear();
  }

}
