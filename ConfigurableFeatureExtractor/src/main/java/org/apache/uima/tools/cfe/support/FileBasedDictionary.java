/**
 * 
 */
package org.apache.uima.tools.cfe.support;


/**
 * @author Igor Sominsky
 *
 */
abstract public class FileBasedDictionary<T> extends FileBasedResource 
{
    public FileBasedDictionary (String name, String path)
    {
        super(name, path);
    }
    
    abstract protected void addEntry(String key, T value, int linenum);
    abstract protected void addLine(String[] line_columns, int linenum);
    
    abstract public T getEntry(String key);
    abstract public int size();
    
}
