package org.apache.uima.tm.textmarker.scanner;
import java.util.*;
import java.util.regex.*;

import org.apache.uima.jcas.JCas;

import org.apache.uima.tm.textmarker.kernel.type.TextMarkerBasic;
import org.apache.uima.tm.type.AMP;
import org.apache.uima.tm.type.BREAK;
import org.apache.uima.tm.type.CAP;
import org.apache.uima.tm.type.COLON;
import org.apache.uima.tm.type.COMMA;
import org.apache.uima.tm.type.CW;
import org.apache.uima.tm.type.EXCLAMATION;
import org.apache.uima.tm.type.MARKUP;
import org.apache.uima.tm.type.NBSP;
import org.apache.uima.tm.type.NUM;
import org.apache.uima.tm.type.PERIOD;
import org.apache.uima.tm.type.QUESTION;
import org.apache.uima.tm.type.SEMICOLON;
import org.apache.uima.tm.type.SPACE;
import org.apache.uima.tm.type.SPECIAL;
import org.apache.uima.tm.type.SW;

%%

%{
    private int number = 0;
    private int comment_count = 0;
    private Map<String,String> tags = new HashMap<String,String>();
    private JCas cas;
    private final static Pattern tagPattern =
        Pattern.compile("</?(\\w+)([^>]*)>");
    private String splitAndPutInMap(String tag){
        Matcher m = tagPattern.matcher(tag);
        if(m.find()){
            String name = m.group(1).toLowerCase();
            tags.put(name,m.group(2));
            return name;
        } else {
            return null;
        }
    }   
    private void removeTag(String closingTag){
        String cTag = closingTag.replace("</","");
        cTag = cTag.replace(">","").toLowerCase();
        tags.remove(cTag.trim());
    }
    public void setJCas(JCas cas) {
        this.cas = cas;
    }
%}

%unicode
%line
%char
%state COMMENT
%type TextMarkerBasic
%class SeedLexer

ALPHA=[A-Za-z]
DIGIT=[0-9]
WHITE_SPACE_CHAR=[\n\r\ \t\b\012]
BREAK=[\n\r\b\012]
SPACE=[ \t]
COMMENT_TEXT=([^*/]|[^*]"/"[^*]|[^/]"*"[^/]|"*"[^/]|"/"[^*])*
%%


<YYINITIAL> {
    "/*" {
                yybegin(COMMENT);
                comment_count++;
    }
    
    \<[/][^>]+\> {
                removeTag(yytext());
                MARKUP t = new MARKUP(cas);
                t.setBegin(yychar);
                t.setEnd(yychar + yytext().length());
                t.setTags(tags);
                return t;
    }
                    
    \<[^>]+\> {
                String tag = splitAndPutInMap(yytext());
                MARKUP t = new MARKUP(cas);
                t.setBegin(yychar);
                t.setEnd(yychar + yytext().length());
                t.setTags(tags);
                return t;
    }
                
    \xA0|&nbsp;|&NBSP; {
                NBSP t = new NBSP(cas);
                t.setBegin(yychar);
                t.setEnd(yychar + yytext().length());
                t.setTags(tags);
                return t;
    }

    &{ALPHA}+; {
                AMP t = new AMP(cas);
                t.setBegin(yychar);
                t.setEnd(yychar + yytext().length());
                t.setTags(tags);
                return t;
    }

    {BREAK} {
                BREAK t = new BREAK(cas);
                t.setBegin(yychar);
                t.setEnd(yychar + yytext().length());
                t.setTags(tags);
                return t;
    }

    {SPACE} {
                SPACE t = new SPACE(cas);
                t.setBegin(yychar);
                t.setEnd(yychar + yytext().length());
                t.setTags(tags);
                return t;
    }

    ":" {
                COLON t = new COLON(cas);
                t.setBegin(yychar);
                t.setEnd(yychar + yytext().length());
                t.setTags(tags);
                return t;
    }

    "," {
                COMMA t = new COMMA(cas);
                t.setBegin(yychar);
                t.setEnd(yychar + yytext().length());
                t.setTags(tags);
                return t;
    }

    "." {
                PERIOD t = new PERIOD(cas);
                t.setBegin(yychar);
                t.setEnd(yychar + yytext().length());
                t.setTags(tags);
                return t;
    }

    
    "!" {
                EXCLAMATION t = new EXCLAMATION(cas);
                t.setBegin(yychar);
                t.setEnd(yychar + yytext().length());
                t.setTags(tags);
                return t;    
    }

    ";" {
                SEMICOLON t = new SEMICOLON(cas);
                t.setBegin(yychar);
                t.setEnd(yychar + yytext().length());
                t.setTags(tags);
                return t;
    }

    "?" {
                QUESTION t = new QUESTION(cas);
                t.setBegin(yychar);
                t.setEnd(yychar + yytext().length());
                t.setTags(tags);
                return t;
    }

    [:lowercase:]+ {
                SW t = new SW(cas);
                t.setBegin(yychar);
                t.setEnd(yychar + yytext().length());
                t.setTags(tags);
                return t;
    }

    [:uppercase:][:lowercase:]* {
                CW t = new CW(cas);
                t.setBegin(yychar);
                t.setEnd(yychar + yytext().length());
                t.setTags(tags);
                return t;
    }

    [:uppercase:]+ {
                CAP t = new CAP(cas);
                t.setBegin(yychar);
                t.setEnd(yychar + yytext().length());
                t.setTags(tags);
                return t;
    }

    {DIGIT}+ {
                NUM t = new NUM(cas);
                t.setBegin(yychar);
                t.setEnd(yychar + yytext().length());
                t.setTags(tags);
                return t;
    }

    . {
                SPECIAL t = new SPECIAL(cas);
                t.setBegin(yychar);
                t.setEnd(yychar + yytext().length());
                t.setTags(tags);
                return t;
    }

    <<EOF>> {
                SPECIAL t = new SPECIAL(cas);
                t.setBegin(yychar);
                t.setEnd(yychar + yytext().length());
                t.setTags(tags);
                return t;
    }

}

<COMMENT> {
    "/*" { comment_count++; }
    "*/" { if (--comment_count == 0) yybegin(YYINITIAL); }
    {COMMENT_TEXT} { }
    <<EOF>> {
                SPECIAL t = new SPECIAL(cas);
                t.setBegin(yychar);
                t.setEnd(yychar + yytext().length());
                t.setTags(tags);
                return t;
    }
}