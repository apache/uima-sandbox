package org.apache.uima.caseditor;

import org.apache.uima.cas.Type;
import org.apache.uima.caseditor.core.model.DocumentElement;
import org.apache.uima.caseditor.core.model.INlpElement;
import org.apache.uima.caseditor.editor.AnnotationDocument;
import org.apache.uima.caseditor.editor.AnnotationStyle;
import org.apache.uima.caseditor.editor.EditorAnnotationStatus;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.part.FileEditorInput;

public class CasDocumentProvider 
		extends org.apache.uima.caseditor.editor.CasDocumentProvider {

	@Override
	protected IDocument createDocument(Object element) throws CoreException {
		if (element instanceof FileEditorInput) {
      FileEditorInput fileInput = (FileEditorInput) element;

			IFile file = fileInput.getFile();

			INlpElement nlpElement = CasEditorPlugin.getNlpModel().findMember(file);

			if (nlpElement instanceof DocumentElement) {

				try {
					org.apache.uima.caseditor.editor.IDocument workingCopy =
						((DocumentElement) nlpElement).getDocument(true);

					AnnotationDocument document = new AnnotationDocument();
					// TODO: fix it
					// document.setProject(nlpElement.getNlpProject());

					document.setDocument(workingCopy);
					return document;
				}
				catch (CoreException e) {
					mElementErrorStatus.put(element, new Status(IStatus.ERROR,
							CasEditorPlugin.ID, IStatus.OK,
							"There is a problem with the document: " + e.getMessage(), e));
				}
			}
			else {
				IStatus status;

				if (nlpElement == null) {
					status = new Status(IStatus.ERROR, CasEditorPlugin.ID, IStatus.OK,
							"Document not in a corpus folder!", null);
				}
				else {
					status = new Status(IStatus.ERROR, CasEditorPlugin.ID, IStatus.OK,
							"Not a cas document!", null);
				}

				mElementErrorStatus.put(element, status);
			}
		}

		return null;
	}

	@Override
	protected void doSaveDocument(IProgressMonitor monitor, Object element,
			IDocument document, boolean overwrite) throws CoreException {
	}
	
	private INlpElement getNlpElement(Object element) {
		if (element instanceof FileEditorInput) {
			FileEditorInput fileInput = (FileEditorInput) element;
			
			IFile file = fileInput.getFile();

			return CasEditorPlugin.getNlpModel().findMember(file);
		}
		
		return null;
	}
	
	protected AnnotationStyle getAnnotationStyle(Object element, Type type) {
		INlpElement nlpElement = getNlpElement(element);
		
		return nlpElement.getNlpProject().getDotCorpus().getAnnotation(type);
	}
	 
	protected EditorAnnotationStatus getEditorAnnotationStatus(Object element) {
		INlpElement nlpElement = getNlpElement(element);
		
		return nlpElement.getNlpProject().getEditorAnnotationStatus();
	}

	protected void setEditorAnnotationStatus(Object element,
			EditorAnnotationStatus editorAnnotationStatus) {
		INlpElement nlpElement = getNlpElement(element);
		
		nlpElement.getNlpProject().setEditorAnnotationStatus(editorAnnotationStatus);
	}
}