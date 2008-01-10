/**
 * 
 */
package org.apache.uima.aae.error;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.uima.aae.error.ErrorResultTDs.TD;
import org.apache.uima.aae.error.ErrorResultTDsImpl.TDImpl;

public class ErrorResultBaseImpl implements ErrorResult
{
	private static final long serialVersionUID = -964940732231472225L;

	private ErrorResultComponentPath resultPath = 
		new ErrorResultComponentPathImpl();
	private ErrorResultTDs resultTDs = 
		new ErrorResultTDsImpl();
	
	private boolean wasTerminated = false;
	private boolean wasDisabled = false;
	private Throwable rootCause;

	/* (non-Javadoc)
	 * @see org.apache.uima.aae.error.ErrorResult#addComponentKeyPath(java.lang.String)
	 */
	public void addComponentKeyPath( String aComponentKeyPath )
	{
		addComponentKeyPath( aComponentKeyPath, false, false);
	}

	/* (non-Javadoc)
	 * @see org.apache.uima.aae.error.ErrorResult#addComponentKeyPath(java.lang.String)
	 */
	public void addComponentKeyPath(String aComponentKeyPath, boolean terminated, boolean disabled)
	{
		resultPath.add(aComponentKeyPath, 0);
		wasTerminated = terminated;
		wasDisabled = disabled;
		if ( wasTerminated || wasDisabled )
		{
			TD td = ((ErrorResultTDsImpl)resultTDs).new TDImpl(aComponentKeyPath, wasTerminated, wasDisabled );
			resultTDs.add(td);
		}
	}

	/* (non-Javadoc)
	 * @see org.apache.uima.aae.error.ErrorResult#getComponentKeyPath()
	 */
	public ErrorResultComponentPath getComponentKeyPath()
	{
		return resultPath;
	}

	/* (non-Javadoc)
	 * @see org.apache.uima.aae.error.ErrorResult#getRootCause()
	 */
	public Throwable getRootCause()
	{
		return rootCause;
	}

	/* (non-Javadoc)
	 * @see org.apache.uima.aae.error.ErrorResult#getTDs()
	 */
	public ErrorResultTDs getTDs()
	{
		return resultTDs;
	}

	/* (non-Javadoc)
	 * @see org.apache.uima.aae.error.ErrorResult#setDisabled()
	 */
	public void setDisabled()
	{
		wasDisabled = true;
	}

	/* (non-Javadoc)
	 * @see org.apache.uima.aae.error.ErrorResult#setRootCause(java.lang.Throwable)
	 */
	public void setRootCause(Throwable aThrowable)
	{
		Throwable t = aThrowable, prev = null;
		while( (t = t.getCause()) != null )
		{
			prev = t;
		}
		rootCause = ( prev == null )? aThrowable : prev; 
	}

	/* (non-Javadoc)
	 * @see org.apache.uima.aae.error.ErrorResult#setTerminated()
	 */
	public void setTerminated()
	{
		wasTerminated = true;
	}

	/* (non-Javadoc)
	 * @see org.apache.uima.aae.error.ErrorResult#wasDisabled()
	 */
	public boolean wasDisabled()
	{
		return wasDisabled;
	}

	/* (non-Javadoc)
	 * @see org.apache.uima.aae.error.ErrorResult#wasTerminated()
	 */
	public boolean wasTerminated()
	{
		return wasTerminated;
	}

	public static void main( String[] args )
	{
		ErrorResultBaseImpl errorResult = new ErrorResultBaseImpl();

		errorResult.setRootCause(new AsynchAEException(new NullPointerException()));
		errorResult.addComponentKeyPath("InnerMostLevel");
		errorResult.addComponentKeyPath("InnerLevel", true, false);
		errorResult.addComponentKeyPath("TopLevel");
		
		System.out.println("Root Cause:"+errorResult.getRootCause());
		ErrorResultComponentPath ercp = errorResult.getComponentKeyPath();
		Iterator it = ercp.iterator();
		int inx=1;
		while( it.hasNext())
		{
			System.out.print((String)it.next());
			System.out.println("");
			
			for( int i=0;i<inx; i++ )
			{
				System.out.print("\t");
			}
			inx++;
		}
		System.out.println("");
		
		ErrorResultTDs tds = errorResult.getTDs();
		Iterator it2 = tds.iterator();
		while( it2.hasNext())
		{
			System.out.println( ((TD)it2.next()).gatPath());
		}
	}
}
