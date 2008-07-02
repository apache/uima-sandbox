package org.apache.uima.casviewer.ui.internal.hover;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import org.apache.uima.tools.debug.util.Trace;

public class HoverInfoReader {
    
    protected String inputText;
    
    protected BufferedReader reader;

    public HoverInfoReader(String info) {
        reader = new BufferedReader(new StringReader(info));
        inputText = info;
    }
    
    /**
     * Each annotation is always started with [type_name].
     * 
     * @return
     * @throws IOException
     * @return String
     */
    private String getLine () throws IOException  {
        return reader.readLine();
    }
    
    public HoverInfo getAnnotationHover () {
        String line;
        try {
            line = getLine();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        if (line == null || (line.trim().length() == 0)) {
            return null;            
        }
        
        // Get type and span
//         String type = line.substring(1, line.indexOf(']'));
//         String span = line.substring(type.length()+2);
//         Trace.err("Type:" + type + " -> " + span);
//         Trace.err("|"+ line + "|");
        return new HoverInfo(line.substring(1, line.indexOf(']')), 
                    line.substring(line.indexOf(']')+1));
    }

}
