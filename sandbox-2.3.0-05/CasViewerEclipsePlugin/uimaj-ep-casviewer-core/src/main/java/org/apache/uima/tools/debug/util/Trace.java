package org.apache.uima.tools.debug.util;

import java.util.List;

public class Trace {
    
    public static boolean TRACING = true;
    
	public static int CONFIG = 0;
	public static int WARNING = 2;
	public static int SEVERE = 3;
	public static int FINER = 4;
	public static int FINEST = 5;
    
	private Trace() {
		super();
	}

  static public String getLastDotName (String name) 
  {
      int index = name.lastIndexOf(".");
      if (index != -1) {
          return name.substring(index+1);
      } else {
          return name;
      }
  }


  public static void list (String text, List list)
  {
    StackTraceElement frame = new Exception().getStackTrace()[1];
    System.out.println("[" + getShortClassName(frame.getClassName())
            + "." + frame.getMethodName() + "]"
            + text + " (size=" + list.size() + ")");
    
    for (int i=0; i<list.size(); ++i) {
      System.out.println("  obj#" + (i+1) + ": " 
          + getLastDotName(list.get(i).getClass().getName()));
    }    
  }
  
  
	public static void trace(int level, String s) {
		Trace.traceStack(level, s, null);
	}

	public static void trace(int level, String s, Throwable t) {
//		if (!getInstance().isDebugging())
//			return;

        System.out.println(s);
		if (t != null)
			t.printStackTrace();
	}
    
    public static void trace() {
        if (TRACING) {
            StackTraceElement frame = new Exception().getStackTrace()[1];
            System.out.println("[" + getShortClassName(frame.getClassName())
                    + "." + frame.getMethodName() + "]");
        }
    }
    
    
    private static String getClassAndMethodNames (StackTraceElement frame)
    {
        return getShortClassName(frame.getClassName()) + "." + frame.getMethodName();
    }
    
    public static void trace(int i) {
        Trace.traceStack(i, "", null);
//        if (TRACING) {
//            String extra = "";
//            StackTraceElement[] frames = new Exception().getStackTrace();
//            if (frames.length > (i+2) && i > 0) {
//                extra = "<-" + getClassAndMethodNames(frames[2]);
//            }
//            System.out.println("[" + getClassAndMethodNames(frames[1])
//                     + extra + "]");
//        }
    }

    public static void traceStack (int level, String postfix, Throwable t) 
    {
        String extra = "";
        StackTraceElement[] frames = new Exception().getStackTrace();
        if (frames.length > (level+3) && level > 0) {
            for (int i=1; i <=level; ++i) {
                extra += "<-" + getClassAndMethodNames(frames[i+2]);
                if (i%3 == 0) {
                  extra += "\n    ";
                }
            }
        }
        System.out.println("[" + getClassAndMethodNames(frames[2])
                + extra + "]" );
        System.out.println("    " + postfix);
    }
    public static void errStack (int level, String postfix, Throwable t) 
    {
      String extra = "";
      StackTraceElement[] frames = new Exception().getStackTrace();
      if (frames.length > (level+3) && level > 0) {
        for (int i=1; i <=level; ++i) {
          extra += "<-" + getClassAndMethodNames(frames[i+2]);
          if (i%3 == 0) {
            extra += "\n    ";
          }
        }
      }
      System.err.println("[" + getClassAndMethodNames(frames[2])
          + extra + "]"  + postfix);
    }
    
    
    public static void err() {
        if (TRACING) {
            StackTraceElement frame = new Exception().getStackTrace()[1];
            System.err.println("[" + getShortClassName(frame.getClassName())
                    + "." + frame.getMethodName() + "]");
        }
    }
    
    
    public static void tbd() {
        if (TRACING) {
            StackTraceElement frame = new Exception().getStackTrace()[1];
            System.err.println("[" + getShortClassName(frame.getClassName())
                    + "." + frame.getMethodName() + "] Not implemented");
        }
    }

    public static void tbd(String text) {
        if (TRACING) {
            StackTraceElement frame = new Exception().getStackTrace()[1];
            System.err.println("[" + getShortClassName(frame.getClassName())
                    + "." + frame.getMethodName() + "] Not implemented: " + text);
        }
    }
    
    public static void trace(String postfix) {
        if (TRACING) {
            StackTraceElement frame = new Exception().getStackTrace()[1];
            // System.out.println(prefix + frame);
            System.out.println("[" + getShortClassName(frame.getClassName()) 
                    + "." + frame.getMethodName() + "] " + postfix);
        }
    }
    
    public static void trace(String prefix, String postfix) {
        if (TRACING) {
            StackTraceElement frame = new Exception().getStackTrace()[1];

            System.out.println("[" + getShortClassName(frame.getClassName()) 
                    + "." + frame.getMethodName() + "] " 
                    + getLastDotName(prefix) + " " + postfix);
        }
    }    
    
    public static void dot(String postfix) {
      if (TRACING) {
          StackTraceElement frame = new Exception().getStackTrace()[1];

          System.out.println("[" + getShortClassName(frame.getClassName()) 
                  + "." + frame.getMethodName() + "] " 
                  + getLastDotName(postfix));
      }
    }    
    public static void dotFirst(String prefix, String postfix) {
      if (TRACING) {
          StackTraceElement frame = new Exception().getStackTrace()[1];

          System.out.println("[" + getShortClassName(frame.getClassName()) 
                  + "." + frame.getMethodName() + "] " 
                  + getLastDotName(prefix) + " " + postfix);
      }
    }    
        
    public static void dotLast(String prefix, String postfix) {
      if (TRACING) {
          StackTraceElement frame = new Exception().getStackTrace()[1];

          System.out.println("[" + getShortClassName(frame.getClassName()) 
                  + "." + frame.getMethodName() + "] " 
                  + prefix + " " + getLastDotName(postfix));
      }
    }    
    public static void outDotLast(int level, String prefix, String postfix) {
      if (TRACING) {
        for (int i=0; i<level; ++i) System.out.print("    ");
        System.out.println(prefix + " " + getLastDotName(postfix));
      }
    }   
    
    public static void outDotLast(String prefix, String postfix) {
      if (TRACING) {
          System.out.println(prefix + " " + getLastDotName(postfix));
      }
    }    

    public static void trace(int level, String prefix, String s) {
        Trace.traceStack(level, getLastDotName(prefix) + " " + s, null);
    }
    
    public static void out () {
        if (TRACING) {
            System.out.println ();
        }
    }    
    
    public static void out (String text) {
        if (TRACING) {
            System.out.println (text);
        }
    }    

    public static void out (int level, String text) {
      if (TRACING) {
          for (int i=0; i<level; ++i) System.out.print("    ");
          System.out.println (text);
      }
  }    
    
    public static void err(String postfix) {
        if (TRACING) {
            StackTraceElement frame = new Exception().getStackTrace()[1];
            System.err.println("[" + getShortClassName(frame.getClassName()) 
                    + "." + frame.getMethodName() + "] " + postfix);
        }
    }
//    public static void err(String prefix, String postfix) {
//        if (TRACING) {
//            StackTraceElement frame = new Exception().getStackTrace()[1];
//
//            System.err.println("[" + getShortClassName(frame.getClassName()) 
//                    + "." + frame.getMethodName() + "] " 
//                    + getLastDotName(prefix) + " " + postfix);
//        }
//    }
    
    public static void errDotFirst (String prefix, String postfix) {
      if (TRACING) {
          StackTraceElement frame = new Exception().getStackTrace()[1];

          System.err.println("[" + getShortClassName(frame.getClassName()) 
                  + "." + frame.getMethodName() + "] " 
                  + getLastDotName(prefix) + " " + postfix);
      }
  }
    
    public static void errDotLast(String prefix, String postfix) {
      if (TRACING) {
          StackTraceElement frame = new Exception().getStackTrace()[1];

          System.err.println("[" + getShortClassName(frame.getClassName()) 
                  + "." + frame.getMethodName() + "] " 
                  + prefix + " " + getLastDotName(postfix));
      }
    }    
    
    public static void err(int level, String prefix, String s) {
        Trace.errStack(level, getLastDotName(prefix) + " " + s, null);
    }
    public static void err(int level, String s) {
        Trace.errStack(level, s, null);
    }

    public static void bug(String postfix) {
        if (TRACING) {
            StackTraceElement frame = new Exception().getStackTrace()[1];
            // System.out.println(prefix + frame);
            System.err.println("??? BUG [" + getShortClassName(frame.getClassName()) 
                    + "." + frame.getMethodName() + "] " + postfix);
        }
    }    
    
    public static void logPerformanceTime (String message, long startTime) {
        long endTime = System.currentTimeMillis();
        StackTraceElement frame = new Exception().getStackTrace()[1];
        System.out.println("[" + getShortClassName(frame.getClassName()) 
                + "." + frame.getMethodName() + "] " 
                + "Performance - " + message + (endTime - startTime) + " ms");
    }    
    
    private static String getShortClassName (String name)
    {
        return name.substring(name.lastIndexOf('.')+1, name.length());
    }
    
}
