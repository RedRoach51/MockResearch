package de.vogella.jdt.astsimple.handlers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 * This is the Eclipse handler for launching the automated cleanups. This is
 * invoked from the Eclipse UI.
 *
 * @see <a href=
 *      "http://www.vogella.com/articles/EclipsePlugIn/article.html#contribute"
 *      >Extending Eclipse - Plug-in Development Tutorial</a>
 */
public class AutoRefactorHandler {

	static List<IJavaElement> getSelectedJavaElements(ExecutionEvent event) {
		final Shell shell = HandlerUtil.getActiveShell(event);
		final String activePartId = HandlerUtil.getActivePartId(event);
		if ("org.eclipse.jdt.ui.CompilationUnitEditor".equals(activePartId) //$NON-NLS-1$
				|| "com.google.gwt.eclipse.core.editors.gwtJavaEditor".equals(activePartId) //$NON-NLS-1$
				|| "com.google.gwt.eclipse.core.editors.java.GWTJavaEditor".equals(activePartId)) { //$NON-NLS-1$
			return getSelectedJavaElements(shell, HandlerUtil.getActiveEditor(event));
		} else if ("org.eclipse.jdt.ui.PackageExplorer".equals(activePartId) //$NON-NLS-1$
				|| "org.eclipse.ui.navigator.ProjectExplorer".equals(activePartId)) { //$NON-NLS-1$
					return getSelectedJavaElements(shell,
							(IStructuredSelection) HandlerUtil.getCurrentSelection(event));
				} else {
					return Collections.emptyList();
				}
	}

	private static List<IJavaElement> getSelectedJavaElements(Shell shell, IStructuredSelection selection) {
		boolean goodSelection = true;
		final List<IJavaElement> results = new ArrayList<>();

		for (Object el : selection.toArray()) {
			if (el instanceof ICompilationUnit || el instanceof IPackageFragment || el instanceof IPackageFragmentRoot
					|| el instanceof IJavaProject) {
				results.add((IJavaElement) el);
			} else if (el instanceof IProject) {
				final IProject project = (IProject) el;
				if (project.isOpen() && hasNature(project, JavaCore.NATURE_ID)) {
					results.add(JavaCore.create(project));
				}
			} else {
				goodSelection = false;
			}
		}

		if (!goodSelection) {
			showMessage(shell, "Please select a Java source file, Java package or Java project"); //$NON-NLS-1$
		}

		return results;
	}

	private static boolean hasNature(final IProject project, String natureId) {
		try {
			return project.hasNature(natureId);
		} catch (CoreException e) {
			throw new UnsupportedOperationException(null, e);
		}
	}

	private static List<IJavaElement> getSelectedJavaElements(Shell shell, IEditorPart activeEditor) {
		final IEditorInput editorInput = activeEditor.getEditorInput();
		final IJavaElement javaElement = JavaUI.getEditorInputJavaElement(editorInput);
		if (javaElement instanceof ICompilationUnit) {
			return Collections.singletonList(javaElement);
		}
		showMessage(shell, "This action only works on Java source files"); //$NON-NLS-1$
		return Collections.emptyList();
	}

	private static void showMessage(final Shell shell, final String message) {
		Display.getDefault().asyncExec(new Runnable() {
			/**
			 * Run.
			 */
			@Override
			public void run() {
				MessageDialog.openInformation(shell, "Info", message); //$NON-NLS-1$
			}
		});
	}

}