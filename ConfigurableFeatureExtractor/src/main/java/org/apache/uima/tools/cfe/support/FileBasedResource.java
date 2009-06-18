package org.apache.uima.tools.cfe.support;

import java.io.IOException;

public abstract class FileBasedResource
{
    protected String m_name;
    protected String m_path;
    
    public FileBasedResource (String name, String path)
    {
        m_path = path;
        m_name = name;
    }
    
    public String name ()
    {
        return m_name;
    }

    public String path ()
    {
        return m_path;
    }

    abstract public void load() throws IOException;
    abstract public void save() throws IOException;

}
