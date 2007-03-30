package org.apache.uima.caseditor.editor;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.uima.cas.text.AnnotationFS;
import org.apache.uima.caseditor.CasEditorPlugin;
import org.apache.uima.caseditor.FileEditorInput;
import org.apache.uima.caseditor.core.model.DocumentElement;
import org.apache.uima.caseditor.core.model.INlpElement;
import org.apache.uima.caseditor.editor.annotation.EclipseAnnotationPeer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.operation.IRunnableContext;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.jface.text.source.IAnnotationModelListener;
import org.eclipse.ui.texteditor.AbstractDocumentProvider;

/**
 * Provides the {@link org.apache.uima.caseditor.core.IDocument} for 
 * the {@link AnnotationEditor}.
 */
public class CasDocumentProvider extends AbstractDocumentProvider {

	/**
	 * The method {@link #createDocument(Object)} put error status 
	 * objects for the given element in this map, if something with document creation
	 * goes wrong. 
	 * 
	 * The method {@link #getStatus(Object)} can than retrive and return the status.
	 */
	private Map<Object, IStatus> mElementErrorStatus = new HashMap<Object, IStatus>();

	@Override
	protected IAnnotationModel createAnnotationModel(Object element) throws CoreException {
		return new IAnnotationModel() {
			
			private org.apache.uima.caseditor.core.IDocument mDocument;
			
			public void addAnnotation(Annotation annotation, Position position) {
			}

			public void addAnnotationModelListener(IAnnotationModelListener listener) {
			}

			public void connect(IDocument document) {
				mDocument = (org.apache.uima.caseditor.core.IDocument) document;
			}

			public void disconnect(IDocument document) {
				mDocument = null;
			}

			public Iterator getAnnotationIterator() {
				return new Iterator() {
					private Iterator mAnnotations = 
						mDocument.getCAS().getAnnotationIndex().iterator();
					
					public boolean hasNext() {
						return mAnnotations.hasNext();
					}

					public Object next() {
						AnnotationFS annotation = (AnnotationFS) mAnnotations.next();
						
						EclipseAnnotationPeer peer = new EclipseAnnotationPeer(annotation.getType().getName(), false, "");
						peer.setAnnotation(annotation);
						return peer;
					}

					public void remove() {
					}};
			}

			public Position getPosition(Annotation annotation) {
				EclipseAnnotationPeer peer = (EclipseAnnotationPeer) annotation;
				AnnotationFS annotationFS = peer.getAnnotationFS(); 
				return new Position(annotationFS.getBegin(), 
						annotationFS.getEnd() - annotationFS.getBegin());
			}

			public void removeAnnotation(Annotation annotation) {
			}

			public void removeAnnotationModelListener(IAnnotationModelListener listener) {
			}
		};
	}
	
	/**
	 * Creates the a new {@link AnnotationDocument} from the given {@link FileEditorInputOLD} 
	 * element. For all other elemetns null is returned.
	 */
	@Override
	protected IDocument createDocument(Object element) throws CoreException {
		if (element instanceof FileEditorInput) {
      FileEditorInput fileInput = (FileEditorInput) element;
			
			IFile file = fileInput.getFile();
			
			INlpElement nlpElement = CasEditorPlugin.getNlpModel().findMember(file);
			
			if (nlpElement instanceof DocumentElement) {

				try {
					org.apache.uima.caseditor.core.IDocument workingCopy = 
						((DocumentElement) nlpElement).getDocument();
					
					AnnotationDocument document = new AnnotationDocument();
					document.setProject(nlpElement.getNlpProject());
					
					document.setDocument(workingCopy);
					return document;
				}
				catch (CoreException e) {
					mElementErrorStatus.put(element, new Status(IStatus.INFO, 
							CasEditorPlugin.ID, IStatus.ERROR,
							"There is a problem with the document: " + e.getMessage(), e));
				}
			}
			else {
				IStatus status;
				
				if (nlpElement == null) {
					status = new Status(IStatus.INFO, CasEditorPlugin.ID, IStatus.ERROR,
							"Document not in a corpus folder!", null);
				}
				else {
					status = new Status(IStatus.INFO, CasEditorPlugin.ID, IStatus.ERROR,
							"Not a cas document!", null);
				}
			      
				mElementErrorStatus.put(element, status);
			}
		}
		
		return null;
	}

	@Override
	protected void doSaveDocument(IProgressMonitor monitor, Object element, IDocument document, boolean overwrite) throws CoreException {
		fireElementStateChanging(element);
		
		org.apache.uima.caseditor.core.IDocument casDocument = 
			(org.apache.uima.caseditor.core.IDocument) document;
		
		try {
			casDocument.save();
		}
		catch (CoreException e) {
			fireElementStateChangeFailed(element);
			throw e;
		}
		
		fireElementDirtyStateChanged(element, false);
	}

	@Override
	protected IRunnableContext getOperationRunner(IProgressMonitor monitor) {
		return null;
	}
	
	@Override
	public IStatus getStatus(Object element) {
	    IStatus status = mElementErrorStatus.get(element);

	    if (status == null) {
	      status = super.getStatus(element);
	    }

	    return status;	
	}
}
