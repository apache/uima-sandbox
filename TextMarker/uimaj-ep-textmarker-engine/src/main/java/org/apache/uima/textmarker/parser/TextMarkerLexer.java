// $ANTLR 3.2 Sep 23, 2009 12:02:23 D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g 2011-08-09 16:40:16

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
*/

	package org.apache.uima.textmarker.parser;


import org.antlr.runtime.BaseRecognizer;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.DFA;
import org.antlr.runtime.EarlyExitException;
import org.antlr.runtime.IntStream;
import org.antlr.runtime.Lexer;
import org.antlr.runtime.MismatchedSetException;
import org.antlr.runtime.NoViableAltException;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.RecognizerSharedState;

public class TextMarkerLexer extends Lexer {
    public static final int STAR=137;
    public static final int FloatTypeSuffix=112;
    public static final int OctalLiteral=110;
    public static final int FILTERMARKUP=48;
    public static final int LOG=38;
    public static final int CONTAINS=8;
    public static final int REMOVE=66;
    public static final int GREATEREQUAL=150;
    public static final int MARKFAST=42;
    public static final int MATCHEDTEXT=72;
    public static final int CONDITION=91;
    public static final int COUNT=14;
    public static final int LOGN=99;
    public static final int NOT=64;
    public static final int EOF=-1;
    public static final int Identifier=122;
    public static final int ACTION=92;
    public static final int NOTEQUAL=147;
    public static final int CLEAR=74;
    public static final int VBAR=139;
    public static final int DOUBLELIST=95;
    public static final int ENDSWITH=63;
    public static final int RPAREN=124;
    public static final int CREATE=32;
    public static final int GREATER=142;
    public static final int SIN=100;
    public static final int EXP=98;
    public static final int CURRENTCOUNT=16;
    public static final int COS=101;
    public static final int TAN=102;
    public static final int TYPELIST=97;
    public static final int LESS=141;
    public static final int REGEXP=26;
    public static final int GET=69;
    public static final int PARTOF=23;
    public static final int UNMARK=55;
    public static final int LAST=19;
    public static final int COMMENT=152;
    public static final int REMOVEDUPLICATE=67;
    public static final int UNMARKALL=56;
    public static final int RBRACK=126;
    public static final int NEAR=21;
    public static final int LINE_COMMENT=153;
    public static final int IntegerTypeSuffix=107;
    public static final int MARKSCORE=40;
    public static final int REMOVESTRING=73;
    public static final int TRANSFER=57;
    public static final int LCURLY=127;
    public static final int TRIE=7;
    public static final int FILTERTYPE=49;
    public static final int RETAINMARKUP=46;
    public static final int STRINGLIST=96;
    public static final int MARKONCE=41;
    public static final int ScriptString=80;
    public static final int EngineString=81;
    public static final int WS=151;
    public static final int WORDTABLE=11;
    public static final int WORDLIST=10;
    public static final int AutomataBlockString=83;
    public static final int FloatingPointLiteral=113;
    public static final int INTLIST=94;
    public static final int OR=22;
    public static final int JavaIDDigit=121;
    public static final int CALL=50;
    public static final int Annotation=5;
    public static final int FALSE=105;
    public static final int LESSEQUAL=149;
    public static final int RessourceLiteral=117;
    public static final int VOTE=28;
    public static final int Letter=120;
    public static final int EscapeSequence=114;
    public static final int SIZE=71;
    public static final int BasicAnnotationType=76;
    public static final int LBRACK=125;
    public static final int CharacterLiteral=115;
    public static final int DEL=37;
    public static final int ATTRIBUTE=35;
    public static final int TypeString=84;
    public static final int Exponent=111;
    public static final int ASSIGN_EQUAL=143;
    public static final int RETAINTYPE=47;
    public static final int AND=12;
    public static final int TypeSystemString=89;
    public static final int EXPAND=58;
    public static final int BlockString=82;
    public static final int IntString=85;
    public static final int HexDigit=106;
    public static final int COLOR=36;
    public static final int LPAREN=123;
    public static final int POSITION=25;
    public static final int IF=29;
    public static final int AT=130;
    public static final int LogLevel=77;
    public static final int SLASH=138;
    public static final int THEN=75;
    public static final int FILL=34;
    public static final int COMMA=133;
    public static final int IS=61;
    public static final int AMPER=140;
    public static final int REPLACE=45;
    public static final int GETLIST=70;
    public static final int EQUAL=146;
    public static final int GATHER=33;
    public static final int INLIST=17;
    public static final int PLUS=135;
    public static final int BooleanString=88;
    public static final int GETFEATURE=54;
    public static final int DOT=131;
    public static final int ListIdentifier=6;
    public static final int PARTOFNEQ=24;
    public static final int BOOLEANLIST=93;
    public static final int ADD=65;
    public static final int MARKTABLE=43;
    public static final int HexLiteral=108;
    public static final int XOR=103;
    public static final int PERCENT=144;
    public static final int MARK=39;
    public static final int PARSE=31;
    public static final int PackageString=79;
    public static final int OldColor=78;
    public static final int MARKLAST=44;
    public static final int MERGE=68;
    public static final int CONTEXTCOUNT=13;
    public static final int BEFORE=59;
    public static final int EXEC=51;
    public static final int MINUS=136;
    public static final int AFTER=60;
    public static final int DecimalLiteral=109;
    public static final int SEMI=134;
    public static final int TRUE=104;
    public static final int FEATURE=30;
    public static final int SymbolString=90;
    public static final int COLON=132;
    public static final int StringLiteral=116;
    public static final int StringString=87;
    public static final int SCORE=27;
    public static final int QUESTION=145;
    public static final int UnicodeEscape=118;
    public static final int RCURLY=128;
    public static final int STARTSWITH=62;
    public static final int ASSIGN=52;
    public static final int TOTALCOUNT=15;
    public static final int DECLARE=9;
    public static final int DocComment=4;
    public static final int ISINTAG=18;
    public static final int MOFN=20;
    public static final int OctalEscape=119;
    public static final int SETFEATURE=53;
    public static final int DoubleString=86;
    public static final int CIRCUMFLEX=129;
    public static final int ALT_NOTEQUAL=148;

    	public int implicitLineJoiningLevel = 0;
    	public int startPos=-1;
    	public void emitErrorMessage(String msg) {
    	}


    // delegates
    // delegators

    public TextMarkerLexer() {;} 
    public TextMarkerLexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }
    public TextMarkerLexer(CharStream input, RecognizerSharedState state) {
        super(input,state);

    }
    public String getGrammarFileName() { return "D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g"; }

    // $ANTLR start "TRIE"
    public final void mTRIE() throws RecognitionException {
        try {
            int _type = TRIE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:45:2: ( 'TRIE' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:45:4: 'TRIE'
            {
            match("TRIE"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "TRIE"

    // $ANTLR start "CONTAINS"
    public final void mCONTAINS() throws RecognitionException {
        try {
            int _type = CONTAINS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:49:2: ( 'CONTAINS' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:49:4: 'CONTAINS'
            {
            match("CONTAINS"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "CONTAINS"

    // $ANTLR start "DECLARE"
    public final void mDECLARE() throws RecognitionException {
        try {
            int _type = DECLARE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:53:2: ( 'DECLARE' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:53:4: 'DECLARE'
            {
            match("DECLARE"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "DECLARE"

    // $ANTLR start "WORDLIST"
    public final void mWORDLIST() throws RecognitionException {
        try {
            int _type = WORDLIST;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:57:2: ( 'WORDLIST' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:57:4: 'WORDLIST'
            {
            match("WORDLIST"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "WORDLIST"

    // $ANTLR start "WORDTABLE"
    public final void mWORDTABLE() throws RecognitionException {
        try {
            int _type = WORDTABLE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:61:2: ( 'WORDTABLE' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:61:4: 'WORDTABLE'
            {
            match("WORDTABLE"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "WORDTABLE"

    // $ANTLR start "AND"
    public final void mAND() throws RecognitionException {
        try {
            int _type = AND;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:65:2: ( 'AND' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:65:4: 'AND'
            {
            match("AND"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "AND"

    // $ANTLR start "CONTEXTCOUNT"
    public final void mCONTEXTCOUNT() throws RecognitionException {
        try {
            int _type = CONTEXTCOUNT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:69:2: ( 'CONTEXTCOUNT' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:69:4: 'CONTEXTCOUNT'
            {
            match("CONTEXTCOUNT"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "CONTEXTCOUNT"

    // $ANTLR start "COUNT"
    public final void mCOUNT() throws RecognitionException {
        try {
            int _type = COUNT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:73:2: ( 'COUNT' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:73:4: 'COUNT'
            {
            match("COUNT"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "COUNT"

    // $ANTLR start "TOTALCOUNT"
    public final void mTOTALCOUNT() throws RecognitionException {
        try {
            int _type = TOTALCOUNT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:77:2: ( 'TOTALCOUNT' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:77:4: 'TOTALCOUNT'
            {
            match("TOTALCOUNT"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "TOTALCOUNT"

    // $ANTLR start "CURRENTCOUNT"
    public final void mCURRENTCOUNT() throws RecognitionException {
        try {
            int _type = CURRENTCOUNT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:81:2: ( 'CURRENTCOUNT' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:81:4: 'CURRENTCOUNT'
            {
            match("CURRENTCOUNT"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "CURRENTCOUNT"

    // $ANTLR start "INLIST"
    public final void mINLIST() throws RecognitionException {
        try {
            int _type = INLIST;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:85:2: ( 'INLIST' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:85:4: 'INLIST'
            {
            match("INLIST"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INLIST"

    // $ANTLR start "ISINTAG"
    public final void mISINTAG() throws RecognitionException {
        try {
            int _type = ISINTAG;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:89:2: ( 'ISINTAG' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:89:4: 'ISINTAG'
            {
            match("ISINTAG"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "ISINTAG"

    // $ANTLR start "LAST"
    public final void mLAST() throws RecognitionException {
        try {
            int _type = LAST;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:93:2: ( 'LAST' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:93:4: 'LAST'
            {
            match("LAST"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "LAST"

    // $ANTLR start "MOFN"
    public final void mMOFN() throws RecognitionException {
        try {
            int _type = MOFN;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:97:2: ( 'MOFN' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:97:4: 'MOFN'
            {
            match("MOFN"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "MOFN"

    // $ANTLR start "NEAR"
    public final void mNEAR() throws RecognitionException {
        try {
            int _type = NEAR;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:101:2: ( 'NEAR' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:101:4: 'NEAR'
            {
            match("NEAR"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "NEAR"

    // $ANTLR start "OR"
    public final void mOR() throws RecognitionException {
        try {
            int _type = OR;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:105:2: ( 'OR' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:105:4: 'OR'
            {
            match("OR"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "OR"

    // $ANTLR start "PARTOF"
    public final void mPARTOF() throws RecognitionException {
        try {
            int _type = PARTOF;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:109:2: ( 'PARTOF' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:109:4: 'PARTOF'
            {
            match("PARTOF"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "PARTOF"

    // $ANTLR start "PARTOFNEQ"
    public final void mPARTOFNEQ() throws RecognitionException {
        try {
            int _type = PARTOFNEQ;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:113:2: ( 'PARTOFNEQ' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:113:4: 'PARTOFNEQ'
            {
            match("PARTOFNEQ"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "PARTOFNEQ"

    // $ANTLR start "POSITION"
    public final void mPOSITION() throws RecognitionException {
        try {
            int _type = POSITION;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:117:2: ( 'POSITION' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:117:4: 'POSITION'
            {
            match("POSITION"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "POSITION"

    // $ANTLR start "REGEXP"
    public final void mREGEXP() throws RecognitionException {
        try {
            int _type = REGEXP;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:121:2: ( 'REGEXP' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:121:4: 'REGEXP'
            {
            match("REGEXP"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "REGEXP"

    // $ANTLR start "SCORE"
    public final void mSCORE() throws RecognitionException {
        try {
            int _type = SCORE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:125:2: ( 'SCORE' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:125:4: 'SCORE'
            {
            match("SCORE"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "SCORE"

    // $ANTLR start "VOTE"
    public final void mVOTE() throws RecognitionException {
        try {
            int _type = VOTE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:129:2: ( 'VOTE' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:129:4: 'VOTE'
            {
            match("VOTE"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "VOTE"

    // $ANTLR start "IF"
    public final void mIF() throws RecognitionException {
        try {
            int _type = IF;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:133:2: ( 'IF' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:133:4: 'IF'
            {
            match("IF"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "IF"

    // $ANTLR start "FEATURE"
    public final void mFEATURE() throws RecognitionException {
        try {
            int _type = FEATURE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:137:2: ( 'FEATURE' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:137:4: 'FEATURE'
            {
            match("FEATURE"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "FEATURE"

    // $ANTLR start "PARSE"
    public final void mPARSE() throws RecognitionException {
        try {
            int _type = PARSE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:141:2: ( 'PARSE' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:141:4: 'PARSE'
            {
            match("PARSE"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "PARSE"

    // $ANTLR start "CREATE"
    public final void mCREATE() throws RecognitionException {
        try {
            int _type = CREATE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:145:2: ( 'CREATE' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:145:4: 'CREATE'
            {
            match("CREATE"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "CREATE"

    // $ANTLR start "GATHER"
    public final void mGATHER() throws RecognitionException {
        try {
            int _type = GATHER;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:149:2: ( 'GATHER' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:149:4: 'GATHER'
            {
            match("GATHER"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "GATHER"

    // $ANTLR start "FILL"
    public final void mFILL() throws RecognitionException {
        try {
            int _type = FILL;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:153:2: ( 'FILL' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:153:4: 'FILL'
            {
            match("FILL"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "FILL"

    // $ANTLR start "ATTRIBUTE"
    public final void mATTRIBUTE() throws RecognitionException {
        try {
            int _type = ATTRIBUTE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:157:2: ( 'ATTRIBUTE' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:157:4: 'ATTRIBUTE'
            {
            match("ATTRIBUTE"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "ATTRIBUTE"

    // $ANTLR start "COLOR"
    public final void mCOLOR() throws RecognitionException {
        try {
            int _type = COLOR;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:161:2: ( 'COLOR' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:161:4: 'COLOR'
            {
            match("COLOR"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "COLOR"

    // $ANTLR start "DEL"
    public final void mDEL() throws RecognitionException {
        try {
            int _type = DEL;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:165:2: ( 'DEL' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:165:4: 'DEL'
            {
            match("DEL"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "DEL"

    // $ANTLR start "LOG"
    public final void mLOG() throws RecognitionException {
        try {
            int _type = LOG;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:169:2: ( 'LOG' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:169:4: 'LOG'
            {
            match("LOG"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "LOG"

    // $ANTLR start "MARK"
    public final void mMARK() throws RecognitionException {
        try {
            int _type = MARK;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:173:2: ( 'MARK' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:173:4: 'MARK'
            {
            match("MARK"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "MARK"

    // $ANTLR start "MARKSCORE"
    public final void mMARKSCORE() throws RecognitionException {
        try {
            int _type = MARKSCORE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:177:2: ( 'MARKSCORE' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:177:4: 'MARKSCORE'
            {
            match("MARKSCORE"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "MARKSCORE"

    // $ANTLR start "MARKONCE"
    public final void mMARKONCE() throws RecognitionException {
        try {
            int _type = MARKONCE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:181:2: ( 'MARKONCE' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:181:4: 'MARKONCE'
            {
            match("MARKONCE"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "MARKONCE"

    // $ANTLR start "MARKFAST"
    public final void mMARKFAST() throws RecognitionException {
        try {
            int _type = MARKFAST;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:185:2: ( 'MARKFAST' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:185:4: 'MARKFAST'
            {
            match("MARKFAST"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "MARKFAST"

    // $ANTLR start "MARKTABLE"
    public final void mMARKTABLE() throws RecognitionException {
        try {
            int _type = MARKTABLE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:189:2: ( 'MARKTABLE' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:189:4: 'MARKTABLE'
            {
            match("MARKTABLE"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "MARKTABLE"

    // $ANTLR start "MARKLAST"
    public final void mMARKLAST() throws RecognitionException {
        try {
            int _type = MARKLAST;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:193:2: ( 'MARKLAST' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:193:4: 'MARKLAST'
            {
            match("MARKLAST"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "MARKLAST"

    // $ANTLR start "REPLACE"
    public final void mREPLACE() throws RecognitionException {
        try {
            int _type = REPLACE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:197:2: ( 'REPLACE' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:197:4: 'REPLACE'
            {
            match("REPLACE"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "REPLACE"

    // $ANTLR start "RETAINMARKUP"
    public final void mRETAINMARKUP() throws RecognitionException {
        try {
            int _type = RETAINMARKUP;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:201:2: ( 'RETAINMARKUP' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:201:4: 'RETAINMARKUP'
            {
            match("RETAINMARKUP"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RETAINMARKUP"

    // $ANTLR start "RETAINTYPE"
    public final void mRETAINTYPE() throws RecognitionException {
        try {
            int _type = RETAINTYPE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:205:2: ( 'RETAINTYPE' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:205:4: 'RETAINTYPE'
            {
            match("RETAINTYPE"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RETAINTYPE"

    // $ANTLR start "FILTERMARKUP"
    public final void mFILTERMARKUP() throws RecognitionException {
        try {
            int _type = FILTERMARKUP;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:209:2: ( 'FILTERMARKUP' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:209:4: 'FILTERMARKUP'
            {
            match("FILTERMARKUP"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "FILTERMARKUP"

    // $ANTLR start "FILTERTYPE"
    public final void mFILTERTYPE() throws RecognitionException {
        try {
            int _type = FILTERTYPE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:213:2: ( 'FILTERTYPE' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:213:4: 'FILTERTYPE'
            {
            match("FILTERTYPE"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "FILTERTYPE"

    // $ANTLR start "CALL"
    public final void mCALL() throws RecognitionException {
        try {
            int _type = CALL;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:217:2: ( 'CALL' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:217:4: 'CALL'
            {
            match("CALL"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "CALL"

    // $ANTLR start "EXEC"
    public final void mEXEC() throws RecognitionException {
        try {
            int _type = EXEC;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:222:2: ( 'EXEC' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:222:4: 'EXEC'
            {
            match("EXEC"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "EXEC"

    // $ANTLR start "ASSIGN"
    public final void mASSIGN() throws RecognitionException {
        try {
            int _type = ASSIGN;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:226:2: ( 'ASSIGN' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:226:4: 'ASSIGN'
            {
            match("ASSIGN"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "ASSIGN"

    // $ANTLR start "SETFEATURE"
    public final void mSETFEATURE() throws RecognitionException {
        try {
            int _type = SETFEATURE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:230:2: ( 'SETFEATURE' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:230:4: 'SETFEATURE'
            {
            match("SETFEATURE"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "SETFEATURE"

    // $ANTLR start "GETFEATURE"
    public final void mGETFEATURE() throws RecognitionException {
        try {
            int _type = GETFEATURE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:234:2: ( 'GETFEATURE' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:234:4: 'GETFEATURE'
            {
            match("GETFEATURE"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "GETFEATURE"

    // $ANTLR start "UNMARK"
    public final void mUNMARK() throws RecognitionException {
        try {
            int _type = UNMARK;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:238:2: ( 'UNMARK' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:238:4: 'UNMARK'
            {
            match("UNMARK"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "UNMARK"

    // $ANTLR start "UNMARKALL"
    public final void mUNMARKALL() throws RecognitionException {
        try {
            int _type = UNMARKALL;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:242:2: ( 'UNMARKALL' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:242:4: 'UNMARKALL'
            {
            match("UNMARKALL"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "UNMARKALL"

    // $ANTLR start "TRANSFER"
    public final void mTRANSFER() throws RecognitionException {
        try {
            int _type = TRANSFER;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:246:2: ( 'TRANSFER' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:246:4: 'TRANSFER'
            {
            match("TRANSFER"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "TRANSFER"

    // $ANTLR start "EXPAND"
    public final void mEXPAND() throws RecognitionException {
        try {
            int _type = EXPAND;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:251:2: ( 'EXPAND' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:251:4: 'EXPAND'
            {
            match("EXPAND"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "EXPAND"

    // $ANTLR start "BEFORE"
    public final void mBEFORE() throws RecognitionException {
        try {
            int _type = BEFORE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:255:2: ( 'BEFORE' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:255:4: 'BEFORE'
            {
            match("BEFORE"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "BEFORE"

    // $ANTLR start "AFTER"
    public final void mAFTER() throws RecognitionException {
        try {
            int _type = AFTER;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:259:2: ( 'AFTER' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:259:4: 'AFTER'
            {
            match("AFTER"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "AFTER"

    // $ANTLR start "IS"
    public final void mIS() throws RecognitionException {
        try {
            int _type = IS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:263:2: ( 'IS' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:263:4: 'IS'
            {
            match("IS"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "IS"

    // $ANTLR start "STARTSWITH"
    public final void mSTARTSWITH() throws RecognitionException {
        try {
            int _type = STARTSWITH;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:268:2: ( 'STARTSWITH' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:268:4: 'STARTSWITH'
            {
            match("STARTSWITH"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "STARTSWITH"

    // $ANTLR start "ENDSWITH"
    public final void mENDSWITH() throws RecognitionException {
        try {
            int _type = ENDSWITH;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:272:2: ( 'ENDSWITH' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:272:4: 'ENDSWITH'
            {
            match("ENDSWITH"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "ENDSWITH"

    // $ANTLR start "NOT"
    public final void mNOT() throws RecognitionException {
        try {
            int _type = NOT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:278:2: ( 'NOT' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:278:4: 'NOT'
            {
            match("NOT"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "NOT"

    // $ANTLR start "ADD"
    public final void mADD() throws RecognitionException {
        try {
            int _type = ADD;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:281:5: ( 'ADD' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:281:7: 'ADD'
            {
            match("ADD"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "ADD"

    // $ANTLR start "REMOVE"
    public final void mREMOVE() throws RecognitionException {
        try {
            int _type = REMOVE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:282:8: ( 'REMOVE' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:282:10: 'REMOVE'
            {
            match("REMOVE"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "REMOVE"

    // $ANTLR start "REMOVEDUPLICATE"
    public final void mREMOVEDUPLICATE() throws RecognitionException {
        try {
            int _type = REMOVEDUPLICATE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:283:17: ( 'REMOVEDUPLICATE' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:283:19: 'REMOVEDUPLICATE'
            {
            match("REMOVEDUPLICATE"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "REMOVEDUPLICATE"

    // $ANTLR start "MERGE"
    public final void mMERGE() throws RecognitionException {
        try {
            int _type = MERGE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:284:8: ( 'MERGE' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:284:10: 'MERGE'
            {
            match("MERGE"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "MERGE"

    // $ANTLR start "GET"
    public final void mGET() throws RecognitionException {
        try {
            int _type = GET;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:285:5: ( 'GET' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:285:7: 'GET'
            {
            match("GET"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "GET"

    // $ANTLR start "GETLIST"
    public final void mGETLIST() throws RecognitionException {
        try {
            int _type = GETLIST;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:286:9: ( 'GETLIST' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:286:11: 'GETLIST'
            {
            match("GETLIST"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "GETLIST"

    // $ANTLR start "SIZE"
    public final void mSIZE() throws RecognitionException {
        try {
            int _type = SIZE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:287:6: ( 'SIZE' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:287:8: 'SIZE'
            {
            match("SIZE"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "SIZE"

    // $ANTLR start "MATCHEDTEXT"
    public final void mMATCHEDTEXT() throws RecognitionException {
        try {
            int _type = MATCHEDTEXT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:288:13: ( 'MATCHEDTEXT' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:288:15: 'MATCHEDTEXT'
            {
            match("MATCHEDTEXT"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "MATCHEDTEXT"

    // $ANTLR start "REMOVESTRING"
    public final void mREMOVESTRING() throws RecognitionException {
        try {
            int _type = REMOVESTRING;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:289:14: ( 'REMOVESTRING' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:289:16: 'REMOVESTRING'
            {
            match("REMOVESTRING"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "REMOVESTRING"

    // $ANTLR start "CLEAR"
    public final void mCLEAR() throws RecognitionException {
        try {
            int _type = CLEAR;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:290:8: ( 'CLEAR' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:290:11: 'CLEAR'
            {
            match("CLEAR"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "CLEAR"

    // $ANTLR start "THEN"
    public final void mTHEN() throws RecognitionException {
        try {
            int _type = THEN;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:293:2: ( '->' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:293:5: '->'
            {
            match("->"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "THEN"

    // $ANTLR start "BasicAnnotationType"
    public final void mBasicAnnotationType() throws RecognitionException {
        try {
            int _type = BasicAnnotationType;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:297:2: ( 'COLON' | 'SW' | 'MARKUP' | 'PERIOD' | 'CW' | 'NUM' | 'QUESTION' | 'SPECIAL' | 'CAP' | 'COMMA' | 'EXCLAMATION' | 'SEMICOLON' | 'NBSP' | 'AMP' | '_' | 'SENTENCEEND' | 'W' | 'PM' | 'ANY' | 'ALL' | 'SPACE' | 'BREAK' )
            int alt1=22;
            alt1 = dfa1.predict(input);
            switch (alt1) {
                case 1 :
                    // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:297:4: 'COLON'
                    {
                    match("COLON"); 


                    }
                    break;
                case 2 :
                    // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:297:13: 'SW'
                    {
                    match("SW"); 


                    }
                    break;
                case 3 :
                    // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:297:20: 'MARKUP'
                    {
                    match("MARKUP"); 


                    }
                    break;
                case 4 :
                    // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:297:31: 'PERIOD'
                    {
                    match("PERIOD"); 


                    }
                    break;
                case 5 :
                    // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:297:42: 'CW'
                    {
                    match("CW"); 


                    }
                    break;
                case 6 :
                    // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:297:48: 'NUM'
                    {
                    match("NUM"); 


                    }
                    break;
                case 7 :
                    // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:297:56: 'QUESTION'
                    {
                    match("QUESTION"); 


                    }
                    break;
                case 8 :
                    // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:297:69: 'SPECIAL'
                    {
                    match("SPECIAL"); 


                    }
                    break;
                case 9 :
                    // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:297:81: 'CAP'
                    {
                    match("CAP"); 


                    }
                    break;
                case 10 :
                    // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:297:89: 'COMMA'
                    {
                    match("COMMA"); 


                    }
                    break;
                case 11 :
                    // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:297:99: 'EXCLAMATION'
                    {
                    match("EXCLAMATION"); 


                    }
                    break;
                case 12 :
                    // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:297:115: 'SEMICOLON'
                    {
                    match("SEMICOLON"); 


                    }
                    break;
                case 13 :
                    // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:297:129: 'NBSP'
                    {
                    match("NBSP"); 


                    }
                    break;
                case 14 :
                    // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:297:137: 'AMP'
                    {
                    match("AMP"); 


                    }
                    break;
                case 15 :
                    // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:298:2: '_'
                    {
                    match('_'); 

                    }
                    break;
                case 16 :
                    // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:298:8: 'SENTENCEEND'
                    {
                    match("SENTENCEEND"); 


                    }
                    break;
                case 17 :
                    // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:298:24: 'W'
                    {
                    match('W'); 

                    }
                    break;
                case 18 :
                    // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:298:30: 'PM'
                    {
                    match("PM"); 


                    }
                    break;
                case 19 :
                    // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:298:37: 'ANY'
                    {
                    match("ANY"); 


                    }
                    break;
                case 20 :
                    // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:298:45: 'ALL'
                    {
                    match("ALL"); 


                    }
                    break;
                case 21 :
                    // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:298:53: 'SPACE'
                    {
                    match("SPACE"); 


                    }
                    break;
                case 22 :
                    // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:298:63: 'BREAK'
                    {
                    match("BREAK"); 


                    }
                    break;

            }
            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "BasicAnnotationType"

    // $ANTLR start "LogLevel"
    public final void mLogLevel() throws RecognitionException {
        try {
            int _type = LogLevel;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:301:9: ( 'finest' | 'finer' | 'fine' | 'config' | 'info' | 'warning' | 'severe' )
            int alt2=7;
            alt2 = dfa2.predict(input);
            switch (alt2) {
                case 1 :
                    // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:302:2: 'finest'
                    {
                    match("finest"); 


                    }
                    break;
                case 2 :
                    // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:302:13: 'finer'
                    {
                    match("finer"); 


                    }
                    break;
                case 3 :
                    // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:302:23: 'fine'
                    {
                    match("fine"); 


                    }
                    break;
                case 4 :
                    // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:302:32: 'config'
                    {
                    match("config"); 


                    }
                    break;
                case 5 :
                    // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:302:43: 'info'
                    {
                    match("info"); 


                    }
                    break;
                case 6 :
                    // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:302:52: 'warning'
                    {
                    match("warning"); 


                    }
                    break;
                case 7 :
                    // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:302:64: 'severe'
                    {
                    match("severe"); 


                    }
                    break;

            }
            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "LogLevel"

    // $ANTLR start "OldColor"
    public final void mOldColor() throws RecognitionException {
        try {
            int _type = OldColor;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:306:2: ( 'black' | 'maroon' | 'green' | 'olive' | 'navy' | 'purple' | 'teal' | 'gray' | 'silver' | 'red' | 'lime' | 'yellow' | 'blue' | 'fuchsia' | 'aqua' )
            int alt3=15;
            alt3 = dfa3.predict(input);
            switch (alt3) {
                case 1 :
                    // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:306:4: 'black'
                    {
                    match("black"); 


                    }
                    break;
                case 2 :
                    // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:306:14: 'maroon'
                    {
                    match("maroon"); 


                    }
                    break;
                case 3 :
                    // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:306:25: 'green'
                    {
                    match("green"); 


                    }
                    break;
                case 4 :
                    // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:306:35: 'olive'
                    {
                    match("olive"); 


                    }
                    break;
                case 5 :
                    // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:306:45: 'navy'
                    {
                    match("navy"); 


                    }
                    break;
                case 6 :
                    // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:306:54: 'purple'
                    {
                    match("purple"); 


                    }
                    break;
                case 7 :
                    // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:306:65: 'teal'
                    {
                    match("teal"); 


                    }
                    break;
                case 8 :
                    // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:306:74: 'gray'
                    {
                    match("gray"); 


                    }
                    break;
                case 9 :
                    // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:306:83: 'silver'
                    {
                    match("silver"); 


                    }
                    break;
                case 10 :
                    // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:306:94: 'red'
                    {
                    match("red"); 


                    }
                    break;
                case 11 :
                    // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:306:102: 'lime'
                    {
                    match("lime"); 


                    }
                    break;
                case 12 :
                    // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:306:111: 'yellow'
                    {
                    match("yellow"); 


                    }
                    break;
                case 13 :
                    // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:306:122: 'blue'
                    {
                    match("blue"); 


                    }
                    break;
                case 14 :
                    // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:306:131: 'fuchsia'
                    {
                    match("fuchsia"); 


                    }
                    break;
                case 15 :
                    // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:306:143: 'aqua'
                    {
                    match("aqua"); 


                    }
                    break;

            }
            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "OldColor"

    // $ANTLR start "PackageString"
    public final void mPackageString() throws RecognitionException {
        try {
            int _type = PackageString;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:309:17: ( 'PACKAGE' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:309:19: 'PACKAGE'
            {
            match("PACKAGE"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "PackageString"

    // $ANTLR start "ScriptString"
    public final void mScriptString() throws RecognitionException {
        try {
            int _type = ScriptString;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:310:14: ( 'SCRIPT' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:310:16: 'SCRIPT'
            {
            match("SCRIPT"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "ScriptString"

    // $ANTLR start "EngineString"
    public final void mEngineString() throws RecognitionException {
        try {
            int _type = EngineString;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:311:14: ( 'ENGINE' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:311:16: 'ENGINE'
            {
            match("ENGINE"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "EngineString"

    // $ANTLR start "BlockString"
    public final void mBlockString() throws RecognitionException {
        try {
            int _type = BlockString;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:312:14: ( 'BLOCK' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:312:16: 'BLOCK'
            {
            match("BLOCK"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "BlockString"

    // $ANTLR start "AutomataBlockString"
    public final void mAutomataBlockString() throws RecognitionException {
        try {
            int _type = AutomataBlockString;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:313:22: ( 'RULES' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:313:24: 'RULES'
            {
            match("RULES"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "AutomataBlockString"

    // $ANTLR start "TypeString"
    public final void mTypeString() throws RecognitionException {
        try {
            int _type = TypeString;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:314:13: ( 'TYPE' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:314:15: 'TYPE'
            {
            match("TYPE"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "TypeString"

    // $ANTLR start "IntString"
    public final void mIntString() throws RecognitionException {
        try {
            int _type = IntString;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:315:11: ( 'INT' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:315:13: 'INT'
            {
            match("INT"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "IntString"

    // $ANTLR start "DoubleString"
    public final void mDoubleString() throws RecognitionException {
        try {
            int _type = DoubleString;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:316:14: ( 'DOUBLE' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:316:16: 'DOUBLE'
            {
            match("DOUBLE"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "DoubleString"

    // $ANTLR start "StringString"
    public final void mStringString() throws RecognitionException {
        try {
            int _type = StringString;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:317:14: ( 'STRING' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:317:16: 'STRING'
            {
            match("STRING"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "StringString"

    // $ANTLR start "BooleanString"
    public final void mBooleanString() throws RecognitionException {
        try {
            int _type = BooleanString;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:318:15: ( 'BOOLEAN' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:318:17: 'BOOLEAN'
            {
            match("BOOLEAN"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "BooleanString"

    // $ANTLR start "TypeSystemString"
    public final void mTypeSystemString() throws RecognitionException {
        try {
            int _type = TypeSystemString;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:319:17: ( 'TYPESYSTEM' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:319:19: 'TYPESYSTEM'
            {
            match("TYPESYSTEM"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "TypeSystemString"

    // $ANTLR start "SymbolString"
    public final void mSymbolString() throws RecognitionException {
        try {
            int _type = SymbolString;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:320:14: ( 'SYMBOL' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:320:16: 'SYMBOL'
            {
            match("SYMBOL"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "SymbolString"

    // $ANTLR start "CONDITION"
    public final void mCONDITION() throws RecognitionException {
        try {
            int _type = CONDITION;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:321:11: ( 'CONDITION' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:321:13: 'CONDITION'
            {
            match("CONDITION"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "CONDITION"

    // $ANTLR start "ACTION"
    public final void mACTION() throws RecognitionException {
        try {
            int _type = ACTION;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:322:9: ( 'ACTION' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:322:11: 'ACTION'
            {
            match("ACTION"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "ACTION"

    // $ANTLR start "BOOLEANLIST"
    public final void mBOOLEANLIST() throws RecognitionException {
        try {
            int _type = BOOLEANLIST;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:324:2: ( 'BOOLEANLIST' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:324:5: 'BOOLEANLIST'
            {
            match("BOOLEANLIST"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "BOOLEANLIST"

    // $ANTLR start "INTLIST"
    public final void mINTLIST() throws RecognitionException {
        try {
            int _type = INTLIST;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:325:9: ( 'INTLIST' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:325:11: 'INTLIST'
            {
            match("INTLIST"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INTLIST"

    // $ANTLR start "DOUBLELIST"
    public final void mDOUBLELIST() throws RecognitionException {
        try {
            int _type = DOUBLELIST;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:327:2: ( 'DOUBLELIST' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:327:5: 'DOUBLELIST'
            {
            match("DOUBLELIST"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "DOUBLELIST"

    // $ANTLR start "STRINGLIST"
    public final void mSTRINGLIST() throws RecognitionException {
        try {
            int _type = STRINGLIST;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:329:2: ( 'STRINGLIST' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:329:4: 'STRINGLIST'
            {
            match("STRINGLIST"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "STRINGLIST"

    // $ANTLR start "TYPELIST"
    public final void mTYPELIST() throws RecognitionException {
        try {
            int _type = TYPELIST;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:330:9: ( 'TYPELIST' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:330:11: 'TYPELIST'
            {
            match("TYPELIST"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "TYPELIST"

    // $ANTLR start "EXP"
    public final void mEXP() throws RecognitionException {
        try {
            int _type = EXP;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:334:6: ( 'EXP' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:334:8: 'EXP'
            {
            match("EXP"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "EXP"

    // $ANTLR start "LOGN"
    public final void mLOGN() throws RecognitionException {
        try {
            int _type = LOGN;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:335:6: ( 'LOGN' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:335:8: 'LOGN'
            {
            match("LOGN"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "LOGN"

    // $ANTLR start "SIN"
    public final void mSIN() throws RecognitionException {
        try {
            int _type = SIN;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:336:5: ( 'SIN' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:336:7: 'SIN'
            {
            match("SIN"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "SIN"

    // $ANTLR start "COS"
    public final void mCOS() throws RecognitionException {
        try {
            int _type = COS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:337:5: ( 'COS' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:337:7: 'COS'
            {
            match("COS"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "COS"

    // $ANTLR start "TAN"
    public final void mTAN() throws RecognitionException {
        try {
            int _type = TAN;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:338:5: ( 'TAN' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:338:7: 'TAN'
            {
            match("TAN"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "TAN"

    // $ANTLR start "XOR"
    public final void mXOR() throws RecognitionException {
        try {
            int _type = XOR;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:339:5: ( 'XOR' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:339:8: 'XOR'
            {
            match("XOR"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "XOR"

    // $ANTLR start "TRUE"
    public final void mTRUE() throws RecognitionException {
        try {
            int _type = TRUE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:340:7: ( 'true' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:340:9: 'true'
            {
            match("true"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "TRUE"

    // $ANTLR start "FALSE"
    public final void mFALSE() throws RecognitionException {
        try {
            int _type = FALSE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:341:8: ( 'false' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:341:10: 'false'
            {
            match("false"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "FALSE"

    // $ANTLR start "HexLiteral"
    public final void mHexLiteral() throws RecognitionException {
        try {
            int _type = HexLiteral;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:343:12: ( '0' ( 'x' | 'X' ) ( HexDigit )+ ( IntegerTypeSuffix )? )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:343:14: '0' ( 'x' | 'X' ) ( HexDigit )+ ( IntegerTypeSuffix )?
            {
            match('0'); 
            if ( input.LA(1)=='X'||input.LA(1)=='x' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:343:28: ( HexDigit )+
            int cnt4=0;
            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);

                if ( ((LA4_0>='0' && LA4_0<='9')||(LA4_0>='A' && LA4_0<='F')||(LA4_0>='a' && LA4_0<='f')) ) {
                    alt4=1;
                }


                switch (alt4) {
            	case 1 :
            	    // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:343:28: HexDigit
            	    {
            	    mHexDigit(); 

            	    }
            	    break;

            	default :
            	    if ( cnt4 >= 1 ) break loop4;
                        EarlyExitException eee =
                            new EarlyExitException(4, input);
                        throw eee;
                }
                cnt4++;
            } while (true);

            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:343:38: ( IntegerTypeSuffix )?
            int alt5=2;
            int LA5_0 = input.LA(1);

            if ( (LA5_0=='L'||LA5_0=='l') ) {
                alt5=1;
            }
            switch (alt5) {
                case 1 :
                    // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:343:38: IntegerTypeSuffix
                    {
                    mIntegerTypeSuffix(); 

                    }
                    break;

            }


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "HexLiteral"

    // $ANTLR start "DecimalLiteral"
    public final void mDecimalLiteral() throws RecognitionException {
        try {
            int _type = DecimalLiteral;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:345:16: ( ( '0' | '1' .. '9' ( '0' .. '9' )* ) ( IntegerTypeSuffix )? )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:345:18: ( '0' | '1' .. '9' ( '0' .. '9' )* ) ( IntegerTypeSuffix )?
            {
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:345:18: ( '0' | '1' .. '9' ( '0' .. '9' )* )
            int alt7=2;
            int LA7_0 = input.LA(1);

            if ( (LA7_0=='0') ) {
                alt7=1;
            }
            else if ( ((LA7_0>='1' && LA7_0<='9')) ) {
                alt7=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 7, 0, input);

                throw nvae;
            }
            switch (alt7) {
                case 1 :
                    // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:345:19: '0'
                    {
                    match('0'); 

                    }
                    break;
                case 2 :
                    // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:345:25: '1' .. '9' ( '0' .. '9' )*
                    {
                    matchRange('1','9'); 
                    // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:345:34: ( '0' .. '9' )*
                    loop6:
                    do {
                        int alt6=2;
                        int LA6_0 = input.LA(1);

                        if ( ((LA6_0>='0' && LA6_0<='9')) ) {
                            alt6=1;
                        }


                        switch (alt6) {
                    	case 1 :
                    	    // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:345:34: '0' .. '9'
                    	    {
                    	    matchRange('0','9'); 

                    	    }
                    	    break;

                    	default :
                    	    break loop6;
                        }
                    } while (true);


                    }
                    break;

            }

            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:345:45: ( IntegerTypeSuffix )?
            int alt8=2;
            int LA8_0 = input.LA(1);

            if ( (LA8_0=='L'||LA8_0=='l') ) {
                alt8=1;
            }
            switch (alt8) {
                case 1 :
                    // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:345:45: IntegerTypeSuffix
                    {
                    mIntegerTypeSuffix(); 

                    }
                    break;

            }


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "DecimalLiteral"

    // $ANTLR start "OctalLiteral"
    public final void mOctalLiteral() throws RecognitionException {
        try {
            int _type = OctalLiteral;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:347:14: ( '0' ( '0' .. '7' )+ ( IntegerTypeSuffix )? )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:347:16: '0' ( '0' .. '7' )+ ( IntegerTypeSuffix )?
            {
            match('0'); 
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:347:20: ( '0' .. '7' )+
            int cnt9=0;
            loop9:
            do {
                int alt9=2;
                int LA9_0 = input.LA(1);

                if ( ((LA9_0>='0' && LA9_0<='7')) ) {
                    alt9=1;
                }


                switch (alt9) {
            	case 1 :
            	    // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:347:21: '0' .. '7'
            	    {
            	    matchRange('0','7'); 

            	    }
            	    break;

            	default :
            	    if ( cnt9 >= 1 ) break loop9;
                        EarlyExitException eee =
                            new EarlyExitException(9, input);
                        throw eee;
                }
                cnt9++;
            } while (true);

            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:347:32: ( IntegerTypeSuffix )?
            int alt10=2;
            int LA10_0 = input.LA(1);

            if ( (LA10_0=='L'||LA10_0=='l') ) {
                alt10=1;
            }
            switch (alt10) {
                case 1 :
                    // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:347:32: IntegerTypeSuffix
                    {
                    mIntegerTypeSuffix(); 

                    }
                    break;

            }


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "OctalLiteral"

    // $ANTLR start "HexDigit"
    public final void mHexDigit() throws RecognitionException {
        try {
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:350:10: ( ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' ) )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:350:12: ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' )
            {
            if ( (input.LA(1)>='0' && input.LA(1)<='9')||(input.LA(1)>='A' && input.LA(1)<='F')||(input.LA(1)>='a' && input.LA(1)<='f') ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

        }
        finally {
        }
    }
    // $ANTLR end "HexDigit"

    // $ANTLR start "IntegerTypeSuffix"
    public final void mIntegerTypeSuffix() throws RecognitionException {
        try {
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:353:19: ( ( 'l' | 'L' ) )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:353:21: ( 'l' | 'L' )
            {
            if ( input.LA(1)=='L'||input.LA(1)=='l' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

        }
        finally {
        }
    }
    // $ANTLR end "IntegerTypeSuffix"

    // $ANTLR start "FloatingPointLiteral"
    public final void mFloatingPointLiteral() throws RecognitionException {
        try {
            int _type = FloatingPointLiteral;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:356:5: ( ( '0' .. '9' )+ '.' ( '0' .. '9' )* ( Exponent )? ( FloatTypeSuffix )? | '.' ( '0' .. '9' )+ ( Exponent )? ( FloatTypeSuffix )? )
            int alt18=2;
            int LA18_0 = input.LA(1);

            if ( ((LA18_0>='0' && LA18_0<='9')) ) {
                alt18=1;
            }
            else if ( (LA18_0=='.') ) {
                alt18=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 18, 0, input);

                throw nvae;
            }
            switch (alt18) {
                case 1 :
                    // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:356:9: ( '0' .. '9' )+ '.' ( '0' .. '9' )* ( Exponent )? ( FloatTypeSuffix )?
                    {
                    // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:356:9: ( '0' .. '9' )+
                    int cnt11=0;
                    loop11:
                    do {
                        int alt11=2;
                        int LA11_0 = input.LA(1);

                        if ( ((LA11_0>='0' && LA11_0<='9')) ) {
                            alt11=1;
                        }


                        switch (alt11) {
                    	case 1 :
                    	    // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:356:10: '0' .. '9'
                    	    {
                    	    matchRange('0','9'); 

                    	    }
                    	    break;

                    	default :
                    	    if ( cnt11 >= 1 ) break loop11;
                                EarlyExitException eee =
                                    new EarlyExitException(11, input);
                                throw eee;
                        }
                        cnt11++;
                    } while (true);

                    match('.'); 
                    // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:356:25: ( '0' .. '9' )*
                    loop12:
                    do {
                        int alt12=2;
                        int LA12_0 = input.LA(1);

                        if ( ((LA12_0>='0' && LA12_0<='9')) ) {
                            alt12=1;
                        }


                        switch (alt12) {
                    	case 1 :
                    	    // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:356:26: '0' .. '9'
                    	    {
                    	    matchRange('0','9'); 

                    	    }
                    	    break;

                    	default :
                    	    break loop12;
                        }
                    } while (true);

                    // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:356:37: ( Exponent )?
                    int alt13=2;
                    int LA13_0 = input.LA(1);

                    if ( (LA13_0=='E'||LA13_0=='e') ) {
                        alt13=1;
                    }
                    switch (alt13) {
                        case 1 :
                            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:356:37: Exponent
                            {
                            mExponent(); 

                            }
                            break;

                    }

                    // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:356:47: ( FloatTypeSuffix )?
                    int alt14=2;
                    int LA14_0 = input.LA(1);

                    if ( (LA14_0=='D'||LA14_0=='F'||LA14_0=='d'||LA14_0=='f') ) {
                        alt14=1;
                    }
                    switch (alt14) {
                        case 1 :
                            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:356:47: FloatTypeSuffix
                            {
                            mFloatTypeSuffix(); 

                            }
                            break;

                    }


                    }
                    break;
                case 2 :
                    // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:357:9: '.' ( '0' .. '9' )+ ( Exponent )? ( FloatTypeSuffix )?
                    {
                    match('.'); 
                    // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:357:13: ( '0' .. '9' )+
                    int cnt15=0;
                    loop15:
                    do {
                        int alt15=2;
                        int LA15_0 = input.LA(1);

                        if ( ((LA15_0>='0' && LA15_0<='9')) ) {
                            alt15=1;
                        }


                        switch (alt15) {
                    	case 1 :
                    	    // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:357:14: '0' .. '9'
                    	    {
                    	    matchRange('0','9'); 

                    	    }
                    	    break;

                    	default :
                    	    if ( cnt15 >= 1 ) break loop15;
                                EarlyExitException eee =
                                    new EarlyExitException(15, input);
                                throw eee;
                        }
                        cnt15++;
                    } while (true);

                    // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:357:25: ( Exponent )?
                    int alt16=2;
                    int LA16_0 = input.LA(1);

                    if ( (LA16_0=='E'||LA16_0=='e') ) {
                        alt16=1;
                    }
                    switch (alt16) {
                        case 1 :
                            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:357:25: Exponent
                            {
                            mExponent(); 

                            }
                            break;

                    }

                    // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:357:35: ( FloatTypeSuffix )?
                    int alt17=2;
                    int LA17_0 = input.LA(1);

                    if ( (LA17_0=='D'||LA17_0=='F'||LA17_0=='d'||LA17_0=='f') ) {
                        alt17=1;
                    }
                    switch (alt17) {
                        case 1 :
                            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:357:35: FloatTypeSuffix
                            {
                            mFloatTypeSuffix(); 

                            }
                            break;

                    }


                    }
                    break;

            }
            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "FloatingPointLiteral"

    // $ANTLR start "Exponent"
    public final void mExponent() throws RecognitionException {
        try {
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:363:10: ( ( 'e' | 'E' ) ( '+' | '-' )? ( '0' .. '9' )+ )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:363:12: ( 'e' | 'E' ) ( '+' | '-' )? ( '0' .. '9' )+
            {
            if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:363:22: ( '+' | '-' )?
            int alt19=2;
            int LA19_0 = input.LA(1);

            if ( (LA19_0=='+'||LA19_0=='-') ) {
                alt19=1;
            }
            switch (alt19) {
                case 1 :
                    // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:
                    {
                    if ( input.LA(1)=='+'||input.LA(1)=='-' ) {
                        input.consume();

                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        recover(mse);
                        throw mse;}


                    }
                    break;

            }

            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:363:33: ( '0' .. '9' )+
            int cnt20=0;
            loop20:
            do {
                int alt20=2;
                int LA20_0 = input.LA(1);

                if ( ((LA20_0>='0' && LA20_0<='9')) ) {
                    alt20=1;
                }


                switch (alt20) {
            	case 1 :
            	    // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:363:34: '0' .. '9'
            	    {
            	    matchRange('0','9'); 

            	    }
            	    break;

            	default :
            	    if ( cnt20 >= 1 ) break loop20;
                        EarlyExitException eee =
                            new EarlyExitException(20, input);
                        throw eee;
                }
                cnt20++;
            } while (true);


            }

        }
        finally {
        }
    }
    // $ANTLR end "Exponent"

    // $ANTLR start "FloatTypeSuffix"
    public final void mFloatTypeSuffix() throws RecognitionException {
        try {
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:366:17: ( ( 'f' | 'F' | 'd' | 'D' ) )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:366:19: ( 'f' | 'F' | 'd' | 'D' )
            {
            if ( input.LA(1)=='D'||input.LA(1)=='F'||input.LA(1)=='d'||input.LA(1)=='f' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

        }
        finally {
        }
    }
    // $ANTLR end "FloatTypeSuffix"

    // $ANTLR start "CharacterLiteral"
    public final void mCharacterLiteral() throws RecognitionException {
        try {
            int _type = CharacterLiteral;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:369:5: ( '\\'' ( EscapeSequence | ~ ( '\\'' | '\\\\' ) ) '\\'' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:369:9: '\\'' ( EscapeSequence | ~ ( '\\'' | '\\\\' ) ) '\\''
            {
            match('\''); 
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:369:14: ( EscapeSequence | ~ ( '\\'' | '\\\\' ) )
            int alt21=2;
            int LA21_0 = input.LA(1);

            if ( (LA21_0=='\\') ) {
                alt21=1;
            }
            else if ( ((LA21_0>='\u0000' && LA21_0<='&')||(LA21_0>='(' && LA21_0<='[')||(LA21_0>=']' && LA21_0<='\uFFFF')) ) {
                alt21=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 21, 0, input);

                throw nvae;
            }
            switch (alt21) {
                case 1 :
                    // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:369:16: EscapeSequence
                    {
                    mEscapeSequence(); 

                    }
                    break;
                case 2 :
                    // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:369:33: ~ ( '\\'' | '\\\\' )
                    {
                    if ( (input.LA(1)>='\u0000' && input.LA(1)<='&')||(input.LA(1)>='(' && input.LA(1)<='[')||(input.LA(1)>=']' && input.LA(1)<='\uFFFF') ) {
                        input.consume();

                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        recover(mse);
                        throw mse;}


                    }
                    break;

            }

            match('\''); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "CharacterLiteral"

    // $ANTLR start "StringLiteral"
    public final void mStringLiteral() throws RecognitionException {
        try {
            int _type = StringLiteral;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:373:5: ( '\"' ( EscapeSequence | ~ ( '\\\\' | '\"' ) )* '\"' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:373:8: '\"' ( EscapeSequence | ~ ( '\\\\' | '\"' ) )* '\"'
            {
            match('\"'); 
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:373:12: ( EscapeSequence | ~ ( '\\\\' | '\"' ) )*
            loop22:
            do {
                int alt22=3;
                int LA22_0 = input.LA(1);

                if ( (LA22_0=='\\') ) {
                    alt22=1;
                }
                else if ( ((LA22_0>='\u0000' && LA22_0<='!')||(LA22_0>='#' && LA22_0<='[')||(LA22_0>=']' && LA22_0<='\uFFFF')) ) {
                    alt22=2;
                }


                switch (alt22) {
            	case 1 :
            	    // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:373:14: EscapeSequence
            	    {
            	    mEscapeSequence(); 

            	    }
            	    break;
            	case 2 :
            	    // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:373:31: ~ ( '\\\\' | '\"' )
            	    {
            	    if ( (input.LA(1)>='\u0000' && input.LA(1)<='!')||(input.LA(1)>='#' && input.LA(1)<='[')||(input.LA(1)>=']' && input.LA(1)<='\uFFFF') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    break loop22;
                }
            } while (true);

            match('\"'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "StringLiteral"

    // $ANTLR start "RessourceLiteral"
    public final void mRessourceLiteral() throws RecognitionException {
        try {
            int _type = RessourceLiteral;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:377:5: ( '\\'' ( EscapeSequence | ~ ( '\\\\' | '\\'' ) )* '\\'' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:377:8: '\\'' ( EscapeSequence | ~ ( '\\\\' | '\\'' ) )* '\\''
            {
            match('\''); 
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:377:13: ( EscapeSequence | ~ ( '\\\\' | '\\'' ) )*
            loop23:
            do {
                int alt23=3;
                int LA23_0 = input.LA(1);

                if ( (LA23_0=='\\') ) {
                    alt23=1;
                }
                else if ( ((LA23_0>='\u0000' && LA23_0<='&')||(LA23_0>='(' && LA23_0<='[')||(LA23_0>=']' && LA23_0<='\uFFFF')) ) {
                    alt23=2;
                }


                switch (alt23) {
            	case 1 :
            	    // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:377:15: EscapeSequence
            	    {
            	    mEscapeSequence(); 

            	    }
            	    break;
            	case 2 :
            	    // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:377:32: ~ ( '\\\\' | '\\'' )
            	    {
            	    if ( (input.LA(1)>='\u0000' && input.LA(1)<='&')||(input.LA(1)>='(' && input.LA(1)<='[')||(input.LA(1)>=']' && input.LA(1)<='\uFFFF') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    break loop23;
                }
            } while (true);

            match('\''); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RessourceLiteral"

    // $ANTLR start "EscapeSequence"
    public final void mEscapeSequence() throws RecognitionException {
        try {
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:382:5: ( '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | '\\\"' | '\\'' | '\\\\' ) | UnicodeEscape | OctalEscape )
            int alt24=3;
            int LA24_0 = input.LA(1);

            if ( (LA24_0=='\\') ) {
                switch ( input.LA(2) ) {
                case '\"':
                case '\'':
                case '\\':
                case 'b':
                case 'f':
                case 'n':
                case 'r':
                case 't':
                    {
                    alt24=1;
                    }
                    break;
                case 'u':
                    {
                    alt24=2;
                    }
                    break;
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                    {
                    alt24=3;
                    }
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("", 24, 1, input);

                    throw nvae;
                }

            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 24, 0, input);

                throw nvae;
            }
            switch (alt24) {
                case 1 :
                    // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:382:9: '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | '\\\"' | '\\'' | '\\\\' )
                    {
                    match('\\'); 
                    if ( input.LA(1)=='\"'||input.LA(1)=='\''||input.LA(1)=='\\'||input.LA(1)=='b'||input.LA(1)=='f'||input.LA(1)=='n'||input.LA(1)=='r'||input.LA(1)=='t' ) {
                        input.consume();

                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        recover(mse);
                        throw mse;}


                    }
                    break;
                case 2 :
                    // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:383:9: UnicodeEscape
                    {
                    mUnicodeEscape(); 

                    }
                    break;
                case 3 :
                    // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:384:9: OctalEscape
                    {
                    mOctalEscape(); 

                    }
                    break;

            }
        }
        finally {
        }
    }
    // $ANTLR end "EscapeSequence"

    // $ANTLR start "OctalEscape"
    public final void mOctalEscape() throws RecognitionException {
        try {
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:389:5: ( '\\\\' ( '0' .. '3' ) ( '0' .. '7' ) ( '0' .. '7' ) | '\\\\' ( '0' .. '7' ) ( '0' .. '7' ) | '\\\\' ( '0' .. '7' ) )
            int alt25=3;
            int LA25_0 = input.LA(1);

            if ( (LA25_0=='\\') ) {
                int LA25_1 = input.LA(2);

                if ( ((LA25_1>='0' && LA25_1<='3')) ) {
                    int LA25_2 = input.LA(3);

                    if ( ((LA25_2>='0' && LA25_2<='7')) ) {
                        int LA25_5 = input.LA(4);

                        if ( ((LA25_5>='0' && LA25_5<='7')) ) {
                            alt25=1;
                        }
                        else {
                            alt25=2;}
                    }
                    else {
                        alt25=3;}
                }
                else if ( ((LA25_1>='4' && LA25_1<='7')) ) {
                    int LA25_3 = input.LA(3);

                    if ( ((LA25_3>='0' && LA25_3<='7')) ) {
                        alt25=2;
                    }
                    else {
                        alt25=3;}
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 25, 1, input);

                    throw nvae;
                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 25, 0, input);

                throw nvae;
            }
            switch (alt25) {
                case 1 :
                    // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:389:9: '\\\\' ( '0' .. '3' ) ( '0' .. '7' ) ( '0' .. '7' )
                    {
                    match('\\'); 
                    // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:389:14: ( '0' .. '3' )
                    // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:389:15: '0' .. '3'
                    {
                    matchRange('0','3'); 

                    }

                    // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:389:25: ( '0' .. '7' )
                    // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:389:26: '0' .. '7'
                    {
                    matchRange('0','7'); 

                    }

                    // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:389:36: ( '0' .. '7' )
                    // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:389:37: '0' .. '7'
                    {
                    matchRange('0','7'); 

                    }


                    }
                    break;
                case 2 :
                    // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:390:9: '\\\\' ( '0' .. '7' ) ( '0' .. '7' )
                    {
                    match('\\'); 
                    // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:390:14: ( '0' .. '7' )
                    // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:390:15: '0' .. '7'
                    {
                    matchRange('0','7'); 

                    }

                    // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:390:25: ( '0' .. '7' )
                    // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:390:26: '0' .. '7'
                    {
                    matchRange('0','7'); 

                    }


                    }
                    break;
                case 3 :
                    // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:391:9: '\\\\' ( '0' .. '7' )
                    {
                    match('\\'); 
                    // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:391:14: ( '0' .. '7' )
                    // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:391:15: '0' .. '7'
                    {
                    matchRange('0','7'); 

                    }


                    }
                    break;

            }
        }
        finally {
        }
    }
    // $ANTLR end "OctalEscape"

    // $ANTLR start "UnicodeEscape"
    public final void mUnicodeEscape() throws RecognitionException {
        try {
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:396:5: ( '\\\\' 'u' HexDigit HexDigit HexDigit HexDigit )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:396:9: '\\\\' 'u' HexDigit HexDigit HexDigit HexDigit
            {
            match('\\'); 
            match('u'); 
            mHexDigit(); 
            mHexDigit(); 
            mHexDigit(); 
            mHexDigit(); 

            }

        }
        finally {
        }
    }
    // $ANTLR end "UnicodeEscape"

    // $ANTLR start "Identifier"
    public final void mIdentifier() throws RecognitionException {
        try {
            int _type = Identifier;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:401:5: ( Letter ( Letter | JavaIDDigit )* )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:401:9: Letter ( Letter | JavaIDDigit )*
            {
            mLetter(); 
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:401:16: ( Letter | JavaIDDigit )*
            loop26:
            do {
                int alt26=2;
                int LA26_0 = input.LA(1);

                if ( (LA26_0=='$'||(LA26_0>='0' && LA26_0<='9')||(LA26_0>='A' && LA26_0<='Z')||LA26_0=='_'||(LA26_0>='a' && LA26_0<='z')||(LA26_0>='\u00C0' && LA26_0<='\u00D6')||(LA26_0>='\u00D8' && LA26_0<='\u00F6')||(LA26_0>='\u00F8' && LA26_0<='\u1FFF')||(LA26_0>='\u3040' && LA26_0<='\u318F')||(LA26_0>='\u3300' && LA26_0<='\u337F')||(LA26_0>='\u3400' && LA26_0<='\u3D2D')||(LA26_0>='\u4E00' && LA26_0<='\u9FFF')||(LA26_0>='\uF900' && LA26_0<='\uFAFF')) ) {
                    alt26=1;
                }


                switch (alt26) {
            	case 1 :
            	    // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:
            	    {
            	    if ( input.LA(1)=='$'||(input.LA(1)>='0' && input.LA(1)<='9')||(input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z')||(input.LA(1)>='\u00C0' && input.LA(1)<='\u00D6')||(input.LA(1)>='\u00D8' && input.LA(1)<='\u00F6')||(input.LA(1)>='\u00F8' && input.LA(1)<='\u1FFF')||(input.LA(1)>='\u3040' && input.LA(1)<='\u318F')||(input.LA(1)>='\u3300' && input.LA(1)<='\u337F')||(input.LA(1)>='\u3400' && input.LA(1)<='\u3D2D')||(input.LA(1)>='\u4E00' && input.LA(1)<='\u9FFF')||(input.LA(1)>='\uF900' && input.LA(1)<='\uFAFF') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    break loop26;
                }
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "Identifier"

    // $ANTLR start "Letter"
    public final void mLetter() throws RecognitionException {
        try {
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:407:5: ( '\\u0024' | '\\u0041' .. '\\u005a' | '\\u005f' | '\\u0061' .. '\\u007a' | '\\u00c0' .. '\\u00d6' | '\\u00d8' .. '\\u00f6' | '\\u00f8' .. '\\u00ff' | '\\u0100' .. '\\u1fff' | '\\u3040' .. '\\u318f' | '\\u3300' .. '\\u337f' | '\\u3400' .. '\\u3d2d' | '\\u4e00' .. '\\u9fff' | '\\uf900' .. '\\ufaff' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:
            {
            if ( input.LA(1)=='$'||(input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z')||(input.LA(1)>='\u00C0' && input.LA(1)<='\u00D6')||(input.LA(1)>='\u00D8' && input.LA(1)<='\u00F6')||(input.LA(1)>='\u00F8' && input.LA(1)<='\u1FFF')||(input.LA(1)>='\u3040' && input.LA(1)<='\u318F')||(input.LA(1)>='\u3300' && input.LA(1)<='\u337F')||(input.LA(1)>='\u3400' && input.LA(1)<='\u3D2D')||(input.LA(1)>='\u4E00' && input.LA(1)<='\u9FFF')||(input.LA(1)>='\uF900' && input.LA(1)<='\uFAFF') ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

        }
        finally {
        }
    }
    // $ANTLR end "Letter"

    // $ANTLR start "JavaIDDigit"
    public final void mJavaIDDigit() throws RecognitionException {
        try {
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:424:5: ( '\\u0030' .. '\\u0039' | '\\u0660' .. '\\u0669' | '\\u06f0' .. '\\u06f9' | '\\u0966' .. '\\u096f' | '\\u09e6' .. '\\u09ef' | '\\u0a66' .. '\\u0a6f' | '\\u0ae6' .. '\\u0aef' | '\\u0b66' .. '\\u0b6f' | '\\u0be7' .. '\\u0bef' | '\\u0c66' .. '\\u0c6f' | '\\u0ce6' .. '\\u0cef' | '\\u0d66' .. '\\u0d6f' | '\\u0e50' .. '\\u0e59' | '\\u0ed0' .. '\\u0ed9' | '\\u1040' .. '\\u1049' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:
            {
            if ( (input.LA(1)>='0' && input.LA(1)<='9')||(input.LA(1)>='\u0660' && input.LA(1)<='\u0669')||(input.LA(1)>='\u06F0' && input.LA(1)<='\u06F9')||(input.LA(1)>='\u0966' && input.LA(1)<='\u096F')||(input.LA(1)>='\u09E6' && input.LA(1)<='\u09EF')||(input.LA(1)>='\u0A66' && input.LA(1)<='\u0A6F')||(input.LA(1)>='\u0AE6' && input.LA(1)<='\u0AEF')||(input.LA(1)>='\u0B66' && input.LA(1)<='\u0B6F')||(input.LA(1)>='\u0BE7' && input.LA(1)<='\u0BEF')||(input.LA(1)>='\u0C66' && input.LA(1)<='\u0C6F')||(input.LA(1)>='\u0CE6' && input.LA(1)<='\u0CEF')||(input.LA(1)>='\u0D66' && input.LA(1)<='\u0D6F')||(input.LA(1)>='\u0E50' && input.LA(1)<='\u0E59')||(input.LA(1)>='\u0ED0' && input.LA(1)<='\u0ED9')||(input.LA(1)>='\u1040' && input.LA(1)<='\u1049') ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

        }
        finally {
        }
    }
    // $ANTLR end "JavaIDDigit"

    // $ANTLR start "LPAREN"
    public final void mLPAREN() throws RecognitionException {
        try {
            int _type = LPAREN;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:443:8: ( '(' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:443:10: '('
            {
            match('('); 
            implicitLineJoiningLevel++;

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "LPAREN"

    // $ANTLR start "RPAREN"
    public final void mRPAREN() throws RecognitionException {
        try {
            int _type = RPAREN;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:445:8: ( ')' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:445:10: ')'
            {
            match(')'); 
            implicitLineJoiningLevel--;

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RPAREN"

    // $ANTLR start "LBRACK"
    public final void mLBRACK() throws RecognitionException {
        try {
            int _type = LBRACK;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:447:8: ( '[' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:447:10: '['
            {
            match('['); 
            implicitLineJoiningLevel++;

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "LBRACK"

    // $ANTLR start "RBRACK"
    public final void mRBRACK() throws RecognitionException {
        try {
            int _type = RBRACK;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:449:8: ( ']' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:449:10: ']'
            {
            match(']'); 
            implicitLineJoiningLevel--;

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RBRACK"

    // $ANTLR start "LCURLY"
    public final void mLCURLY() throws RecognitionException {
        try {
            int _type = LCURLY;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:451:8: ( '{' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:451:10: '{'
            {
            match('{'); 
            implicitLineJoiningLevel++;

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "LCURLY"

    // $ANTLR start "RCURLY"
    public final void mRCURLY() throws RecognitionException {
        try {
            int _type = RCURLY;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:453:8: ( '}' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:453:10: '}'
            {
            match('}'); 
            implicitLineJoiningLevel--;

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RCURLY"

    // $ANTLR start "CIRCUMFLEX"
    public final void mCIRCUMFLEX() throws RecognitionException {
        try {
            int _type = CIRCUMFLEX;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:455:12: ( '^' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:455:14: '^'
            {
            match('^'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "CIRCUMFLEX"

    // $ANTLR start "AT"
    public final void mAT() throws RecognitionException {
        try {
            int _type = AT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:457:4: ( '@' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:457:6: '@'
            {
            match('@'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "AT"

    // $ANTLR start "DOT"
    public final void mDOT() throws RecognitionException {
        try {
            int _type = DOT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:459:5: ( '.' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:459:7: '.'
            {
            match('.'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "DOT"

    // $ANTLR start "COLON"
    public final void mCOLON() throws RecognitionException {
        try {
            int _type = COLON;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:461:8: ( ':' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:461:10: ':'
            {
            match(':'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "COLON"

    // $ANTLR start "COMMA"
    public final void mCOMMA() throws RecognitionException {
        try {
            int _type = COMMA;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:463:7: ( ',' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:463:9: ','
            {
            match(','); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "COMMA"

    // $ANTLR start "SEMI"
    public final void mSEMI() throws RecognitionException {
        try {
            int _type = SEMI;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:465:6: ( ';' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:465:8: ';'
            {
            match(';'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "SEMI"

    // $ANTLR start "PLUS"
    public final void mPLUS() throws RecognitionException {
        try {
            int _type = PLUS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:467:6: ( '+' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:467:8: '+'
            {
            match('+'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "PLUS"

    // $ANTLR start "MINUS"
    public final void mMINUS() throws RecognitionException {
        try {
            int _type = MINUS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:469:7: ( '-' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:469:9: '-'
            {
            match('-'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "MINUS"

    // $ANTLR start "STAR"
    public final void mSTAR() throws RecognitionException {
        try {
            int _type = STAR;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:471:6: ( '*' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:471:8: '*'
            {
            match('*'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "STAR"

    // $ANTLR start "SLASH"
    public final void mSLASH() throws RecognitionException {
        try {
            int _type = SLASH;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:473:7: ( '/' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:473:9: '/'
            {
            match('/'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "SLASH"

    // $ANTLR start "VBAR"
    public final void mVBAR() throws RecognitionException {
        try {
            int _type = VBAR;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:475:6: ( '|' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:475:8: '|'
            {
            match('|'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "VBAR"

    // $ANTLR start "AMPER"
    public final void mAMPER() throws RecognitionException {
        try {
            int _type = AMPER;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:477:7: ( '&' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:477:9: '&'
            {
            match('&'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "AMPER"

    // $ANTLR start "LESS"
    public final void mLESS() throws RecognitionException {
        try {
            int _type = LESS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:479:6: ( '<' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:479:8: '<'
            {
            match('<'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "LESS"

    // $ANTLR start "GREATER"
    public final void mGREATER() throws RecognitionException {
        try {
            int _type = GREATER;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:481:9: ( '>' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:481:11: '>'
            {
            match('>'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "GREATER"

    // $ANTLR start "ASSIGN_EQUAL"
    public final void mASSIGN_EQUAL() throws RecognitionException {
        try {
            int _type = ASSIGN_EQUAL;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:483:14: ( '=' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:483:16: '='
            {
            match('='); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "ASSIGN_EQUAL"

    // $ANTLR start "PERCENT"
    public final void mPERCENT() throws RecognitionException {
        try {
            int _type = PERCENT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:485:9: ( '%' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:485:11: '%'
            {
            match('%'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "PERCENT"

    // $ANTLR start "QUESTION"
    public final void mQUESTION() throws RecognitionException {
        try {
            int _type = QUESTION;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:487:10: ( '?' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:487:12: '?'
            {
            match('?'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "QUESTION"

    // $ANTLR start "EQUAL"
    public final void mEQUAL() throws RecognitionException {
        try {
            int _type = EQUAL;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:489:7: ( '==' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:489:9: '=='
            {
            match("=="); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "EQUAL"

    // $ANTLR start "NOTEQUAL"
    public final void mNOTEQUAL() throws RecognitionException {
        try {
            int _type = NOTEQUAL;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:491:10: ( '!=' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:491:12: '!='
            {
            match("!="); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "NOTEQUAL"

    // $ANTLR start "ALT_NOTEQUAL"
    public final void mALT_NOTEQUAL() throws RecognitionException {
        try {
            int _type = ALT_NOTEQUAL;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:493:13: ( '<>' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:493:15: '<>'
            {
            match("<>"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "ALT_NOTEQUAL"

    // $ANTLR start "LESSEQUAL"
    public final void mLESSEQUAL() throws RecognitionException {
        try {
            int _type = LESSEQUAL;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:495:11: ( '<=' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:495:13: '<='
            {
            match("<="); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "LESSEQUAL"

    // $ANTLR start "GREATEREQUAL"
    public final void mGREATEREQUAL() throws RecognitionException {
        try {
            int _type = GREATEREQUAL;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:498:14: ( '>=' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:498:16: '>='
            {
            match(">="); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "GREATEREQUAL"

    // $ANTLR start "WS"
    public final void mWS() throws RecognitionException {
        try {
            int _type = WS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:500:5: ( ( ' ' | '\\r' | '\\t' | '\\u000C' | '\\n' ) )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:500:8: ( ' ' | '\\r' | '\\t' | '\\u000C' | '\\n' )
            {
            if ( (input.LA(1)>='\t' && input.LA(1)<='\n')||(input.LA(1)>='\f' && input.LA(1)<='\r')||input.LA(1)==' ' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            _channel=HIDDEN;

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "WS"

    // $ANTLR start "COMMENT"
    public final void mCOMMENT() throws RecognitionException {
        try {
            int _type = COMMENT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:510:5: ( '/*' ( options {greedy=false; } : . )* '*/' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:510:9: '/*' ( options {greedy=false; } : . )* '*/'
            {
            match("/*"); 

            if (input.LA(1)=='*') _type=DocComment; else _channel=HIDDEN;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:510:77: ( options {greedy=false; } : . )*
            loop27:
            do {
                int alt27=2;
                int LA27_0 = input.LA(1);

                if ( (LA27_0=='*') ) {
                    int LA27_1 = input.LA(2);

                    if ( (LA27_1=='/') ) {
                        alt27=2;
                    }
                    else if ( ((LA27_1>='\u0000' && LA27_1<='.')||(LA27_1>='0' && LA27_1<='\uFFFF')) ) {
                        alt27=1;
                    }


                }
                else if ( ((LA27_0>='\u0000' && LA27_0<=')')||(LA27_0>='+' && LA27_0<='\uFFFF')) ) {
                    alt27=1;
                }


                switch (alt27) {
            	case 1 :
            	    // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:510:105: .
            	    {
            	    matchAny(); 

            	    }
            	    break;

            	default :
            	    break loop27;
                }
            } while (true);

            match("*/"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "COMMENT"

    // $ANTLR start "LINE_COMMENT"
    public final void mLINE_COMMENT() throws RecognitionException {
        try {
            int _type = LINE_COMMENT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:514:5: ( '//' (~ ( '\\n' | '\\r' ) )* ( '\\r' )? '\\n' )
            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:514:7: '//' (~ ( '\\n' | '\\r' ) )* ( '\\r' )? '\\n'
            {
            match("//"); 

            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:514:12: (~ ( '\\n' | '\\r' ) )*
            loop28:
            do {
                int alt28=2;
                int LA28_0 = input.LA(1);

                if ( ((LA28_0>='\u0000' && LA28_0<='\t')||(LA28_0>='\u000B' && LA28_0<='\f')||(LA28_0>='\u000E' && LA28_0<='\uFFFF')) ) {
                    alt28=1;
                }


                switch (alt28) {
            	case 1 :
            	    // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:514:12: ~ ( '\\n' | '\\r' )
            	    {
            	    if ( (input.LA(1)>='\u0000' && input.LA(1)<='\t')||(input.LA(1)>='\u000B' && input.LA(1)<='\f')||(input.LA(1)>='\u000E' && input.LA(1)<='\uFFFF') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    break loop28;
                }
            } while (true);

            // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:514:26: ( '\\r' )?
            int alt29=2;
            int LA29_0 = input.LA(1);

            if ( (LA29_0=='\r') ) {
                alt29=1;
            }
            switch (alt29) {
                case 1 :
                    // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:514:26: '\\r'
                    {
                    match('\r'); 

                    }
                    break;

            }

            match('\n'); 
            _channel=HIDDEN;

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "LINE_COMMENT"

    public void mTokens() throws RecognitionException {
        // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:8: ( TRIE | CONTAINS | DECLARE | WORDLIST | WORDTABLE | AND | CONTEXTCOUNT | COUNT | TOTALCOUNT | CURRENTCOUNT | INLIST | ISINTAG | LAST | MOFN | NEAR | OR | PARTOF | PARTOFNEQ | POSITION | REGEXP | SCORE | VOTE | IF | FEATURE | PARSE | CREATE | GATHER | FILL | ATTRIBUTE | COLOR | DEL | LOG | MARK | MARKSCORE | MARKONCE | MARKFAST | MARKTABLE | MARKLAST | REPLACE | RETAINMARKUP | RETAINTYPE | FILTERMARKUP | FILTERTYPE | CALL | EXEC | ASSIGN | SETFEATURE | GETFEATURE | UNMARK | UNMARKALL | TRANSFER | EXPAND | BEFORE | AFTER | IS | STARTSWITH | ENDSWITH | NOT | ADD | REMOVE | REMOVEDUPLICATE | MERGE | GET | GETLIST | SIZE | MATCHEDTEXT | REMOVESTRING | CLEAR | THEN | BasicAnnotationType | LogLevel | OldColor | PackageString | ScriptString | EngineString | BlockString | AutomataBlockString | TypeString | IntString | DoubleString | StringString | BooleanString | TypeSystemString | SymbolString | CONDITION | ACTION | BOOLEANLIST | INTLIST | DOUBLELIST | STRINGLIST | TYPELIST | EXP | LOGN | SIN | COS | TAN | XOR | TRUE | FALSE | HexLiteral | DecimalLiteral | OctalLiteral | FloatingPointLiteral | CharacterLiteral | StringLiteral | RessourceLiteral | Identifier | LPAREN | RPAREN | LBRACK | RBRACK | LCURLY | RCURLY | CIRCUMFLEX | AT | DOT | COLON | COMMA | SEMI | PLUS | MINUS | STAR | SLASH | VBAR | AMPER | LESS | GREATER | ASSIGN_EQUAL | PERCENT | QUESTION | EQUAL | NOTEQUAL | ALT_NOTEQUAL | LESSEQUAL | GREATEREQUAL | WS | COMMENT | LINE_COMMENT )
        int alt30=138;
        alt30 = dfa30.predict(input);
        switch (alt30) {
            case 1 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:10: TRIE
                {
                mTRIE(); 

                }
                break;
            case 2 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:15: CONTAINS
                {
                mCONTAINS(); 

                }
                break;
            case 3 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:24: DECLARE
                {
                mDECLARE(); 

                }
                break;
            case 4 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:32: WORDLIST
                {
                mWORDLIST(); 

                }
                break;
            case 5 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:41: WORDTABLE
                {
                mWORDTABLE(); 

                }
                break;
            case 6 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:51: AND
                {
                mAND(); 

                }
                break;
            case 7 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:55: CONTEXTCOUNT
                {
                mCONTEXTCOUNT(); 

                }
                break;
            case 8 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:68: COUNT
                {
                mCOUNT(); 

                }
                break;
            case 9 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:74: TOTALCOUNT
                {
                mTOTALCOUNT(); 

                }
                break;
            case 10 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:85: CURRENTCOUNT
                {
                mCURRENTCOUNT(); 

                }
                break;
            case 11 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:98: INLIST
                {
                mINLIST(); 

                }
                break;
            case 12 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:105: ISINTAG
                {
                mISINTAG(); 

                }
                break;
            case 13 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:113: LAST
                {
                mLAST(); 

                }
                break;
            case 14 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:118: MOFN
                {
                mMOFN(); 

                }
                break;
            case 15 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:123: NEAR
                {
                mNEAR(); 

                }
                break;
            case 16 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:128: OR
                {
                mOR(); 

                }
                break;
            case 17 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:131: PARTOF
                {
                mPARTOF(); 

                }
                break;
            case 18 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:138: PARTOFNEQ
                {
                mPARTOFNEQ(); 

                }
                break;
            case 19 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:148: POSITION
                {
                mPOSITION(); 

                }
                break;
            case 20 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:157: REGEXP
                {
                mREGEXP(); 

                }
                break;
            case 21 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:164: SCORE
                {
                mSCORE(); 

                }
                break;
            case 22 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:170: VOTE
                {
                mVOTE(); 

                }
                break;
            case 23 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:175: IF
                {
                mIF(); 

                }
                break;
            case 24 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:178: FEATURE
                {
                mFEATURE(); 

                }
                break;
            case 25 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:186: PARSE
                {
                mPARSE(); 

                }
                break;
            case 26 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:192: CREATE
                {
                mCREATE(); 

                }
                break;
            case 27 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:199: GATHER
                {
                mGATHER(); 

                }
                break;
            case 28 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:206: FILL
                {
                mFILL(); 

                }
                break;
            case 29 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:211: ATTRIBUTE
                {
                mATTRIBUTE(); 

                }
                break;
            case 30 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:221: COLOR
                {
                mCOLOR(); 

                }
                break;
            case 31 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:227: DEL
                {
                mDEL(); 

                }
                break;
            case 32 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:231: LOG
                {
                mLOG(); 

                }
                break;
            case 33 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:235: MARK
                {
                mMARK(); 

                }
                break;
            case 34 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:240: MARKSCORE
                {
                mMARKSCORE(); 

                }
                break;
            case 35 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:250: MARKONCE
                {
                mMARKONCE(); 

                }
                break;
            case 36 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:259: MARKFAST
                {
                mMARKFAST(); 

                }
                break;
            case 37 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:268: MARKTABLE
                {
                mMARKTABLE(); 

                }
                break;
            case 38 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:278: MARKLAST
                {
                mMARKLAST(); 

                }
                break;
            case 39 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:287: REPLACE
                {
                mREPLACE(); 

                }
                break;
            case 40 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:295: RETAINMARKUP
                {
                mRETAINMARKUP(); 

                }
                break;
            case 41 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:308: RETAINTYPE
                {
                mRETAINTYPE(); 

                }
                break;
            case 42 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:319: FILTERMARKUP
                {
                mFILTERMARKUP(); 

                }
                break;
            case 43 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:332: FILTERTYPE
                {
                mFILTERTYPE(); 

                }
                break;
            case 44 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:343: CALL
                {
                mCALL(); 

                }
                break;
            case 45 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:348: EXEC
                {
                mEXEC(); 

                }
                break;
            case 46 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:353: ASSIGN
                {
                mASSIGN(); 

                }
                break;
            case 47 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:360: SETFEATURE
                {
                mSETFEATURE(); 

                }
                break;
            case 48 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:371: GETFEATURE
                {
                mGETFEATURE(); 

                }
                break;
            case 49 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:382: UNMARK
                {
                mUNMARK(); 

                }
                break;
            case 50 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:389: UNMARKALL
                {
                mUNMARKALL(); 

                }
                break;
            case 51 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:399: TRANSFER
                {
                mTRANSFER(); 

                }
                break;
            case 52 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:408: EXPAND
                {
                mEXPAND(); 

                }
                break;
            case 53 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:415: BEFORE
                {
                mBEFORE(); 

                }
                break;
            case 54 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:422: AFTER
                {
                mAFTER(); 

                }
                break;
            case 55 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:428: IS
                {
                mIS(); 

                }
                break;
            case 56 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:431: STARTSWITH
                {
                mSTARTSWITH(); 

                }
                break;
            case 57 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:442: ENDSWITH
                {
                mENDSWITH(); 

                }
                break;
            case 58 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:451: NOT
                {
                mNOT(); 

                }
                break;
            case 59 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:455: ADD
                {
                mADD(); 

                }
                break;
            case 60 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:459: REMOVE
                {
                mREMOVE(); 

                }
                break;
            case 61 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:466: REMOVEDUPLICATE
                {
                mREMOVEDUPLICATE(); 

                }
                break;
            case 62 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:482: MERGE
                {
                mMERGE(); 

                }
                break;
            case 63 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:488: GET
                {
                mGET(); 

                }
                break;
            case 64 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:492: GETLIST
                {
                mGETLIST(); 

                }
                break;
            case 65 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:500: SIZE
                {
                mSIZE(); 

                }
                break;
            case 66 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:505: MATCHEDTEXT
                {
                mMATCHEDTEXT(); 

                }
                break;
            case 67 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:517: REMOVESTRING
                {
                mREMOVESTRING(); 

                }
                break;
            case 68 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:530: CLEAR
                {
                mCLEAR(); 

                }
                break;
            case 69 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:536: THEN
                {
                mTHEN(); 

                }
                break;
            case 70 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:541: BasicAnnotationType
                {
                mBasicAnnotationType(); 

                }
                break;
            case 71 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:561: LogLevel
                {
                mLogLevel(); 

                }
                break;
            case 72 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:570: OldColor
                {
                mOldColor(); 

                }
                break;
            case 73 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:579: PackageString
                {
                mPackageString(); 

                }
                break;
            case 74 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:593: ScriptString
                {
                mScriptString(); 

                }
                break;
            case 75 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:606: EngineString
                {
                mEngineString(); 

                }
                break;
            case 76 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:619: BlockString
                {
                mBlockString(); 

                }
                break;
            case 77 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:631: AutomataBlockString
                {
                mAutomataBlockString(); 

                }
                break;
            case 78 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:651: TypeString
                {
                mTypeString(); 

                }
                break;
            case 79 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:662: IntString
                {
                mIntString(); 

                }
                break;
            case 80 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:672: DoubleString
                {
                mDoubleString(); 

                }
                break;
            case 81 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:685: StringString
                {
                mStringString(); 

                }
                break;
            case 82 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:698: BooleanString
                {
                mBooleanString(); 

                }
                break;
            case 83 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:712: TypeSystemString
                {
                mTypeSystemString(); 

                }
                break;
            case 84 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:729: SymbolString
                {
                mSymbolString(); 

                }
                break;
            case 85 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:742: CONDITION
                {
                mCONDITION(); 

                }
                break;
            case 86 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:752: ACTION
                {
                mACTION(); 

                }
                break;
            case 87 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:759: BOOLEANLIST
                {
                mBOOLEANLIST(); 

                }
                break;
            case 88 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:771: INTLIST
                {
                mINTLIST(); 

                }
                break;
            case 89 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:779: DOUBLELIST
                {
                mDOUBLELIST(); 

                }
                break;
            case 90 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:790: STRINGLIST
                {
                mSTRINGLIST(); 

                }
                break;
            case 91 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:801: TYPELIST
                {
                mTYPELIST(); 

                }
                break;
            case 92 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:810: EXP
                {
                mEXP(); 

                }
                break;
            case 93 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:814: LOGN
                {
                mLOGN(); 

                }
                break;
            case 94 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:819: SIN
                {
                mSIN(); 

                }
                break;
            case 95 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:823: COS
                {
                mCOS(); 

                }
                break;
            case 96 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:827: TAN
                {
                mTAN(); 

                }
                break;
            case 97 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:831: XOR
                {
                mXOR(); 

                }
                break;
            case 98 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:835: TRUE
                {
                mTRUE(); 

                }
                break;
            case 99 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:840: FALSE
                {
                mFALSE(); 

                }
                break;
            case 100 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:846: HexLiteral
                {
                mHexLiteral(); 

                }
                break;
            case 101 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:857: DecimalLiteral
                {
                mDecimalLiteral(); 

                }
                break;
            case 102 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:872: OctalLiteral
                {
                mOctalLiteral(); 

                }
                break;
            case 103 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:885: FloatingPointLiteral
                {
                mFloatingPointLiteral(); 

                }
                break;
            case 104 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:906: CharacterLiteral
                {
                mCharacterLiteral(); 

                }
                break;
            case 105 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:923: StringLiteral
                {
                mStringLiteral(); 

                }
                break;
            case 106 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:937: RessourceLiteral
                {
                mRessourceLiteral(); 

                }
                break;
            case 107 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:954: Identifier
                {
                mIdentifier(); 

                }
                break;
            case 108 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:965: LPAREN
                {
                mLPAREN(); 

                }
                break;
            case 109 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:972: RPAREN
                {
                mRPAREN(); 

                }
                break;
            case 110 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:979: LBRACK
                {
                mLBRACK(); 

                }
                break;
            case 111 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:986: RBRACK
                {
                mRBRACK(); 

                }
                break;
            case 112 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:993: LCURLY
                {
                mLCURLY(); 

                }
                break;
            case 113 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:1000: RCURLY
                {
                mRCURLY(); 

                }
                break;
            case 114 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:1007: CIRCUMFLEX
                {
                mCIRCUMFLEX(); 

                }
                break;
            case 115 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:1018: AT
                {
                mAT(); 

                }
                break;
            case 116 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:1021: DOT
                {
                mDOT(); 

                }
                break;
            case 117 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:1025: COLON
                {
                mCOLON(); 

                }
                break;
            case 118 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:1031: COMMA
                {
                mCOMMA(); 

                }
                break;
            case 119 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:1037: SEMI
                {
                mSEMI(); 

                }
                break;
            case 120 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:1042: PLUS
                {
                mPLUS(); 

                }
                break;
            case 121 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:1047: MINUS
                {
                mMINUS(); 

                }
                break;
            case 122 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:1053: STAR
                {
                mSTAR(); 

                }
                break;
            case 123 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:1058: SLASH
                {
                mSLASH(); 

                }
                break;
            case 124 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:1064: VBAR
                {
                mVBAR(); 

                }
                break;
            case 125 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:1069: AMPER
                {
                mAMPER(); 

                }
                break;
            case 126 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:1075: LESS
                {
                mLESS(); 

                }
                break;
            case 127 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:1080: GREATER
                {
                mGREATER(); 

                }
                break;
            case 128 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:1088: ASSIGN_EQUAL
                {
                mASSIGN_EQUAL(); 

                }
                break;
            case 129 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:1101: PERCENT
                {
                mPERCENT(); 

                }
                break;
            case 130 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:1109: QUESTION
                {
                mQUESTION(); 

                }
                break;
            case 131 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:1118: EQUAL
                {
                mEQUAL(); 

                }
                break;
            case 132 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:1124: NOTEQUAL
                {
                mNOTEQUAL(); 

                }
                break;
            case 133 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:1133: ALT_NOTEQUAL
                {
                mALT_NOTEQUAL(); 

                }
                break;
            case 134 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:1146: LESSEQUAL
                {
                mLESSEQUAL(); 

                }
                break;
            case 135 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:1156: GREATEREQUAL
                {
                mGREATEREQUAL(); 

                }
                break;
            case 136 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:1169: WS
                {
                mWS(); 

                }
                break;
            case 137 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:1172: COMMENT
                {
                mCOMMENT(); 

                }
                break;
            case 138 :
                // D:\\work\\workspace-uima2\\uimaj-ep-textmarker-engine\\src\\main\\java\\org\\apache\\uima\\textmarker\\parser\\TextMarkerLexer.g:1:1180: LINE_COMMENT
                {
                mLINE_COMMENT(); 

                }
                break;

        }

    }


    protected DFA1 dfa1 = new DFA1(this);
    protected DFA2 dfa2 = new DFA2(this);
    protected DFA3 dfa3 = new DFA3(this);
    protected DFA30 dfa30 = new DFA30(this);
    static final String DFA1_eotS =
        "\37\uffff";
    static final String DFA1_eofS =
        "\37\uffff";
    static final String DFA1_minS =
        "\2\101\1\105\1\uffff\1\105\1\102\2\uffff\1\114\3\uffff\1\114\3"+
        "\uffff\1\101\1\115\15\uffff";
    static final String DFA1_maxS =
        "\1\137\2\127\1\uffff\1\115\1\125\2\uffff\1\116\3\uffff\1\115\3"+
        "\uffff\1\105\1\116\15\uffff";
    static final String DFA1_acceptS =
        "\3\uffff\1\3\2\uffff\1\7\1\13\1\uffff\1\17\1\21\1\26\1\uffff\1"+
        "\5\1\11\1\2\2\uffff\1\4\1\22\1\6\1\15\1\16\1\23\1\24\1\1\1\12\1"+
        "\10\1\25\1\14\1\20";
    static final String DFA1_specialS =
        "\37\uffff}>";
    static final String[] DFA1_transitionS = {
            "\1\10\1\13\1\1\1\uffff\1\7\7\uffff\1\3\1\5\1\uffff\1\4\1\6"+
            "\1\uffff\1\2\3\uffff\1\12\7\uffff\1\11",
            "\1\16\15\uffff\1\14\7\uffff\1\15",
            "\1\21\12\uffff\1\20\6\uffff\1\17",
            "",
            "\1\22\7\uffff\1\23",
            "\1\25\22\uffff\1\24",
            "",
            "",
            "\1\30\1\26\1\27",
            "",
            "",
            "",
            "\1\31\1\32",
            "",
            "",
            "",
            "\1\34\3\uffff\1\33",
            "\1\35\1\36",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA1_eot = DFA.unpackEncodedString(DFA1_eotS);
    static final short[] DFA1_eof = DFA.unpackEncodedString(DFA1_eofS);
    static final char[] DFA1_min = DFA.unpackEncodedStringToUnsignedChars(DFA1_minS);
    static final char[] DFA1_max = DFA.unpackEncodedStringToUnsignedChars(DFA1_maxS);
    static final short[] DFA1_accept = DFA.unpackEncodedString(DFA1_acceptS);
    static final short[] DFA1_special = DFA.unpackEncodedString(DFA1_specialS);
    static final short[][] DFA1_transition;

    static {
        int numStates = DFA1_transitionS.length;
        DFA1_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA1_transition[i] = DFA.unpackEncodedString(DFA1_transitionS[i]);
        }
    }

    class DFA1 extends DFA {

        public DFA1(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 1;
            this.eot = DFA1_eot;
            this.eof = DFA1_eof;
            this.min = DFA1_min;
            this.max = DFA1_max;
            this.accept = DFA1_accept;
            this.special = DFA1_special;
            this.transition = DFA1_transition;
        }
        public String getDescription() {
            return "296:1: BasicAnnotationType : ( 'COLON' | 'SW' | 'MARKUP' | 'PERIOD' | 'CW' | 'NUM' | 'QUESTION' | 'SPECIAL' | 'CAP' | 'COMMA' | 'EXCLAMATION' | 'SEMICOLON' | 'NBSP' | 'AMP' | '_' | 'SENTENCEEND' | 'W' | 'PM' | 'ANY' | 'ALL' | 'SPACE' | 'BREAK' );";
        }
    }
    static final String DFA2_eotS =
        "\10\uffff\1\13\3\uffff";
    static final String DFA2_eofS =
        "\14\uffff";
    static final String DFA2_minS =
        "\1\143\1\151\4\uffff\1\156\1\145\1\162\3\uffff";
    static final String DFA2_maxS =
        "\1\167\1\151\4\uffff\1\156\1\145\1\163\3\uffff";
    static final String DFA2_acceptS =
        "\2\uffff\1\4\1\5\1\6\1\7\3\uffff\1\1\1\2\1\3";
    static final String DFA2_specialS =
        "\14\uffff}>";
    static final String[] DFA2_transitionS = {
            "\1\2\2\uffff\1\1\2\uffff\1\3\11\uffff\1\5\3\uffff\1\4",
            "\1\6",
            "",
            "",
            "",
            "",
            "\1\7",
            "\1\10",
            "\1\12\1\11",
            "",
            "",
            ""
    };

    static final short[] DFA2_eot = DFA.unpackEncodedString(DFA2_eotS);
    static final short[] DFA2_eof = DFA.unpackEncodedString(DFA2_eofS);
    static final char[] DFA2_min = DFA.unpackEncodedStringToUnsignedChars(DFA2_minS);
    static final char[] DFA2_max = DFA.unpackEncodedStringToUnsignedChars(DFA2_maxS);
    static final short[] DFA2_accept = DFA.unpackEncodedString(DFA2_acceptS);
    static final short[] DFA2_special = DFA.unpackEncodedString(DFA2_specialS);
    static final short[][] DFA2_transition;

    static {
        int numStates = DFA2_transitionS.length;
        DFA2_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA2_transition[i] = DFA.unpackEncodedString(DFA2_transitionS[i]);
        }
    }

    class DFA2 extends DFA {

        public DFA2(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 2;
            this.eot = DFA2_eot;
            this.eof = DFA2_eof;
            this.min = DFA2_min;
            this.max = DFA2_max;
            this.accept = DFA2_accept;
            this.special = DFA2_special;
            this.transition = DFA2_transition;
        }
        public String getDescription() {
            return "301:1: LogLevel : ( 'finest' | 'finer' | 'fine' | 'config' | 'info' | 'warning' | 'severe' );";
        }
    }
    static final String DFA3_eotS =
        "\24\uffff";
    static final String DFA3_eofS =
        "\24\uffff";
    static final String DFA3_minS =
        "\1\141\1\154\1\uffff\1\162\12\uffff\2\141\4\uffff";
    static final String DFA3_maxS =
        "\1\171\1\154\1\uffff\1\162\12\uffff\1\165\1\145\4\uffff";
    static final String DFA3_acceptS =
        "\2\uffff\1\2\1\uffff\1\4\1\5\1\6\1\7\1\11\1\12\1\13\1\14\1\16\1"+
        "\17\2\uffff\1\1\1\15\1\3\1\10";
    static final String DFA3_specialS =
        "\24\uffff}>";
    static final String[] DFA3_transitionS = {
            "\1\15\1\1\3\uffff\1\14\1\3\4\uffff\1\12\1\2\1\5\1\4\1\6\1\uffff"+
            "\1\11\1\10\1\7\4\uffff\1\13",
            "\1\16",
            "",
            "\1\17",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\20\23\uffff\1\21",
            "\1\23\3\uffff\1\22",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA3_eot = DFA.unpackEncodedString(DFA3_eotS);
    static final short[] DFA3_eof = DFA.unpackEncodedString(DFA3_eofS);
    static final char[] DFA3_min = DFA.unpackEncodedStringToUnsignedChars(DFA3_minS);
    static final char[] DFA3_max = DFA.unpackEncodedStringToUnsignedChars(DFA3_maxS);
    static final short[] DFA3_accept = DFA.unpackEncodedString(DFA3_acceptS);
    static final short[] DFA3_special = DFA.unpackEncodedString(DFA3_specialS);
    static final short[][] DFA3_transition;

    static {
        int numStates = DFA3_transitionS.length;
        DFA3_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA3_transition[i] = DFA.unpackEncodedString(DFA3_transitionS[i]);
        }
    }

    class DFA3 extends DFA {

        public DFA3(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 3;
            this.eot = DFA3_eot;
            this.eof = DFA3_eof;
            this.min = DFA3_min;
            this.max = DFA3_max;
            this.accept = DFA3_accept;
            this.special = DFA3_special;
            this.transition = DFA3_transition;
        }
        public String getDescription() {
            return "305:1: OldColor : ( 'black' | 'maroon' | 'green' | 'olive' | 'navy' | 'purple' | 'teal' | 'gray' | 'silver' | 'red' | 'lime' | 'yellow' | 'blue' | 'fuchsia' | 'aqua' );";
        }
    }
    static final String DFA30_eotS =
        "\1\uffff\3\55\1\122\17\55\1\u0082\1\55\1\122\21\55\2\u009c\1\u009e"+
        "\20\uffff\1\u00a4\2\uffff\1\u00a7\1\u00a9\1\u00ab\4\uffff\11\55"+
        "\1\122\3\55\1\uffff\11\55\1\u00cb\1\u00cc\11\55\1\u00d7\3\55\1\122"+
        "\6\55\1\122\16\55\2\uffff\26\55\1\uffff\1\u0114\2\uffff\1\u009c"+
        "\16\uffff\4\55\1\u011e\4\55\1\u0124\3\55\1\122\2\55\1\u012a\2\55"+
        "\1\u012d\1\122\3\55\1\u0131\2\122\2\55\1\u0135\1\55\2\uffff\1\55"+
        "\1\u0139\5\55\1\u013f\1\122\1\55\1\uffff\21\55\1\u0153\7\55\1\u015e"+
        "\1\55\1\u0161\33\55\1\u017d\3\55\1\u0181\6\uffff\1\u0186\2\55\1"+
        "\u018b\1\uffff\5\55\1\uffff\2\55\1\u0195\2\55\1\uffff\2\55\1\uffff"+
        "\3\55\1\uffff\3\55\1\uffff\1\55\1\u01a2\1\u01a3\1\uffff\1\u01a4"+
        "\1\u01ab\2\55\1\u01ae\1\uffff\1\122\21\55\1\u01c0\1\uffff\3\55\1"+
        "\u01c4\1\55\1\u01c6\4\55\1\uffff\1\u01cb\1\55\1\uffff\11\55\1\u01d8"+
        "\3\55\1\u01d8\4\55\1\u017d\2\55\1\u017d\1\55\1\u017d\1\55\1\u017d"+
        "\1\u01e4\1\uffff\1\u017d\1\55\1\u017d\6\uffff\4\55\1\uffff\3\55"+
        "\1\u01ef\1\u01f0\2\122\2\55\1\uffff\1\u01f3\6\55\1\u01fa\4\55\3"+
        "\uffff\6\55\1\uffff\1\55\1\u0206\1\uffff\1\55\1\u0208\7\55\1\u0210"+
        "\1\u0211\6\55\1\uffff\1\55\1\122\1\55\1\uffff\1\55\1\uffff\4\55"+
        "\1\uffff\6\55\1\122\1\u0225\3\55\1\u01d8\1\uffff\1\55\1\u022a\4"+
        "\55\1\u017d\1\55\2\u017d\1\55\1\uffff\1\55\2\uffff\7\55\2\uffff"+
        "\1\55\1\u023b\1\uffff\1\55\1\u023e\3\55\1\u0242\1\uffff\1\u0243"+
        "\1\u0244\7\55\1\122\1\55\1\uffff\1\u024e\1\uffff\2\55\1\122\1\u0251"+
        "\2\55\1\u0257\2\uffff\1\u0258\4\55\1\u025e\1\55\1\u0260\2\55\1\u0264"+
        "\2\55\1\u0267\2\55\1\u026a\1\u026c\1\u026d\1\uffff\2\55\1\u01d8"+
        "\1\55\1\uffff\1\u01d8\1\55\1\u01d8\4\u017d\1\uffff\10\55\1\uffff"+
        "\1\u027b\1\55\1\uffff\3\55\3\uffff\1\u0280\1\u0281\7\55\1\uffff"+
        "\1\u0289\1\55\1\uffff\1\u028b\4\55\2\uffff\5\55\1\uffff\1\122\1"+
        "\uffff\1\u0295\2\55\1\uffff\1\55\1\u0299\1\uffff\2\55\1\uffff\1"+
        "\55\2\uffff\1\u029e\1\55\1\u017d\1\u01d8\1\uffff\1\u02a0\2\55\1"+
        "\u02a3\1\u02a4\3\55\1\uffff\1\55\1\u02a9\2\55\2\uffff\1\55\1\u02ad"+
        "\1\u02ae\1\55\1\u02b0\2\55\1\uffff\1\u02b3\1\uffff\11\55\1\uffff"+
        "\3\55\1\uffff\1\55\1\u02c1\2\55\1\uffff\1\122\1\uffff\2\55\2\uffff"+
        "\1\55\1\u02c7\2\55\1\uffff\1\u02ca\1\u02cb\1\u02cc\2\uffff\1\u02cd"+
        "\1\uffff\1\55\1\u02cf\1\uffff\5\55\1\122\7\55\1\uffff\1\u02dc\1"+
        "\55\1\u02de\1\u02df\1\55\1\uffff\1\55\1\u02e2\4\uffff\1\55\1\uffff"+
        "\1\55\1\u02e5\2\55\1\u02e8\1\55\1\u02ea\1\u02eb\1\55\1\u02ed\1\u02ee"+
        "\1\55\1\uffff\1\55\2\uffff\2\55\1\uffff\1\u02f3\1\55\1\uffff\2\55"+
        "\1\uffff\1\122\2\uffff\1\55\2\uffff\1\122\1\u02f8\1\u02f9\1\u02fa"+
        "\1\uffff\1\u02fb\1\55\1\u02fd\1\u02fe\4\uffff\1\55\2\uffff\1\55"+
        "\1\u0301\1\uffff";
    static final String DFA30_eofS =
        "\u0302\uffff";
    static final String DFA30_minS =
        "\1\11\2\101\1\105\1\44\1\103\1\106\2\101\1\102\1\122\1\101\1\105"+
        "\1\103\1\117\1\105\1\101\2\116\1\105\1\76\1\125\1\44\1\141\1\157"+
        "\1\156\1\141\1\145\1\154\1\141\1\162\1\154\1\141\1\165\2\145\1\151"+
        "\1\145\1\161\1\117\2\56\1\60\1\0\17\uffff\1\52\2\uffff\3\75\4\uffff"+
        "\1\101\1\124\1\120\1\116\1\114\1\122\1\105\1\114\1\105\1\44\1\103"+
        "\1\125\1\122\1\uffff\1\104\1\124\1\123\1\124\1\104\1\120\1\114\1"+
        "\124\1\114\2\44\1\123\1\107\1\106\2\122\1\101\1\124\1\115\1\123"+
        "\1\44\1\103\1\123\1\122\1\44\1\107\1\114\1\117\1\115\1\101\1\116"+
        "\1\44\1\101\1\115\1\124\1\101\1\114\2\124\1\103\1\104\1\115\1\106"+
        "\1\105\2\117\2\uffff\1\105\1\156\1\143\1\154\1\156\1\146\1\162\1"+
        "\166\1\154\1\141\1\162\1\141\1\151\1\166\1\162\1\141\1\165\1\144"+
        "\1\155\1\154\1\165\1\122\1\uffff\1\56\2\uffff\1\56\1\uffff\1\42"+
        "\1\0\13\uffff\1\105\1\116\1\101\1\105\1\44\1\104\1\116\1\117\1\115"+
        "\1\44\1\122\1\101\1\114\1\44\1\101\1\114\1\44\1\102\1\104\2\44\1"+
        "\122\1\111\1\105\3\44\2\111\1\44\1\116\2\uffff\1\124\1\44\1\116"+
        "\1\113\1\103\1\107\1\122\2\44\1\120\1\uffff\1\123\1\113\2\111\1"+
        "\105\1\114\1\101\1\117\1\105\1\122\1\111\1\106\1\111\1\124\1\122"+
        "\1\111\1\105\1\44\2\103\1\102\1\105\1\124\1\114\1\110\1\44\1\103"+
        "\1\44\1\114\1\123\1\111\1\101\1\117\1\101\1\103\1\114\1\123\1\145"+
        "\1\150\1\163\1\146\1\157\1\156\1\145\1\166\1\143\1\145\1\157\1\145"+
        "\1\171\1\166\1\171\1\160\1\154\1\145\1\44\1\145\1\154\1\141\1\44"+
        "\1\uffff\1\0\1\60\2\0\1\uffff\1\44\1\123\1\114\1\44\1\uffff\1\101"+
        "\1\111\1\124\1\116\1\101\1\uffff\1\105\1\124\1\44\1\122\1\101\1"+
        "\uffff\2\114\1\uffff\1\111\1\107\1\122\1\uffff\1\117\1\123\1\111"+
        "\1\uffff\1\124\2\44\1\uffff\2\44\1\110\1\105\1\44\1\uffff\1\44\1"+
        "\117\1\105\1\101\1\124\1\117\1\130\1\101\1\111\1\126\1\123\1\105"+
        "\1\120\1\105\1\103\1\105\1\124\1\116\1\44\1\uffff\1\111\1\105\1"+
        "\117\1\44\1\125\1\44\3\105\1\111\1\uffff\1\44\1\116\1\uffff\1\101"+
        "\1\127\1\116\2\122\2\113\1\105\1\124\1\44\1\163\1\145\1\151\1\44"+
        "\1\151\1\162\1\145\1\153\1\44\1\157\1\156\1\44\1\145\1\44\1\154"+
        "\2\44\1\uffff\1\44\1\157\1\44\1\uffff\1\60\2\0\2\uffff\1\106\1\103"+
        "\1\131\1\111\1\uffff\1\111\1\130\1\124\4\44\1\116\1\105\1\uffff"+
        "\1\44\1\122\1\105\1\111\1\101\1\102\1\116\1\44\1\116\1\124\1\123"+
        "\1\101\3\uffff\1\103\1\116\3\101\1\120\1\uffff\1\105\1\44\1\uffff"+
        "\1\106\1\44\1\107\1\111\1\104\1\120\1\103\1\116\1\105\2\44\1\124"+
        "\1\101\1\117\1\116\1\123\1\107\1\uffff\1\101\1\44\1\114\1\uffff"+
        "\1\122\1\uffff\2\122\1\101\1\123\1\uffff\1\104\1\115\1\111\1\105"+
        "\1\113\1\105\2\44\1\101\1\111\1\164\1\44\1\uffff\1\151\1\44\1\147"+
        "\1\156\1\145\1\162\1\44\1\156\2\44\1\145\1\uffff\1\167\1\60\1\0"+
        "\1\105\1\117\2\123\1\116\1\124\1\111\2\uffff\1\124\1\44\1\uffff"+
        "\1\105\1\44\1\123\1\102\1\125\1\44\1\uffff\2\44\1\124\1\107\1\117"+
        "\1\103\1\123\1\102\1\123\1\44\1\104\1\uffff\1\44\1\uffff\1\105\1"+
        "\117\2\44\1\105\1\115\1\44\2\uffff\1\44\1\124\1\114\1\103\1\127"+
        "\1\44\1\114\1\44\1\105\1\115\1\44\2\124\1\44\1\101\1\124\3\44\1"+
        "\uffff\1\116\1\117\1\44\1\141\1\uffff\1\44\1\147\5\44\1\60\1\122"+
        "\1\125\2\124\1\123\1\103\1\117\1\103\1\uffff\1\44\1\111\1\uffff"+
        "\1\124\1\114\1\124\3\uffff\2\44\1\122\1\105\1\124\1\114\2\124\1"+
        "\105\1\uffff\1\44\1\116\1\uffff\1\44\1\101\1\131\1\125\1\124\2\uffff"+
        "\1\125\1\117\1\105\2\111\1\uffff\1\44\1\uffff\1\44\1\101\1\131\1"+
        "\uffff\1\125\1\44\1\uffff\1\124\1\110\1\uffff\1\114\2\uffff\1\44"+
        "\1\116\2\44\1\0\1\44\1\116\1\105\2\44\1\117\1\116\1\117\1\uffff"+
        "\1\123\1\44\2\105\2\uffff\1\105\2\44\1\105\1\44\1\105\1\121\1\uffff"+
        "\1\44\1\uffff\1\122\2\120\2\122\1\116\1\105\1\124\1\123\1\uffff"+
        "\1\122\1\120\1\122\1\uffff\1\111\1\44\1\114\1\111\1\uffff\1\44\1"+
        "\uffff\1\124\1\115\2\uffff\1\125\1\44\1\125\1\124\1\uffff\3\44\2"+
        "\uffff\1\44\1\uffff\1\130\1\44\1\uffff\1\113\1\105\1\114\1\111\1"+
        "\105\1\44\1\116\1\110\1\124\1\113\2\105\1\117\1\uffff\1\44\1\123"+
        "\2\44\1\116\1\uffff\1\116\1\44\4\uffff\1\124\1\uffff\1\125\1\44"+
        "\1\111\1\116\1\44\1\104\2\44\1\125\2\44\1\116\1\uffff\1\124\2\uffff"+
        "\2\124\1\uffff\1\44\1\120\1\uffff\1\103\1\107\1\uffff\1\44\2\uffff"+
        "\1\120\2\uffff\4\44\1\uffff\1\44\1\101\2\44\4\uffff\1\124\2\uffff"+
        "\1\105\1\44\1\uffff";
    static final String DFA30_maxS =
        "\1\ufaff\1\131\1\127\1\117\1\ufaff\1\124\1\123\2\117\1\125\1\122"+
        "\1\117\1\125\1\131\1\117\1\111\1\105\1\130\1\116\1\122\1\76\1\125"+
        "\1\ufaff\1\165\1\157\1\156\1\141\1\151\1\154\1\141\1\162\1\154\1"+
        "\141\1\165\1\162\1\145\1\151\1\145\1\161\1\117\1\170\2\71\1\uffff"+
        "\17\uffff\1\57\2\uffff\1\76\2\75\4\uffff\1\111\1\124\1\120\1\116"+
        "\1\125\1\122\1\105\1\120\1\105\1\ufaff\1\114\1\125\1\122\1\uffff"+
        "\1\131\1\124\1\123\1\124\1\104\1\120\1\114\2\124\2\ufaff\1\123\1"+
        "\107\1\106\1\124\1\122\1\101\1\124\1\115\1\123\1\ufaff\1\122\1\123"+
        "\1\122\1\ufaff\1\124\1\114\1\122\1\124\1\122\1\132\1\ufaff\1\105"+
        "\1\115\1\124\1\101\1\114\2\124\1\120\1\107\1\115\1\106\1\105\2\117"+
        "\2\uffff\1\105\1\156\1\143\1\154\1\156\1\146\1\162\1\166\1\154\1"+
        "\165\1\162\1\145\1\151\1\166\1\162\1\141\1\165\1\144\1\155\1\154"+
        "\1\165\1\122\1\uffff\1\71\2\uffff\1\71\1\uffff\1\165\1\uffff\13"+
        "\uffff\1\105\1\116\1\101\1\105\1\ufaff\1\124\1\116\1\117\1\115\1"+
        "\ufaff\1\122\1\101\1\114\1\ufaff\1\101\1\114\1\ufaff\1\102\1\104"+
        "\2\ufaff\1\122\1\111\1\105\3\ufaff\2\111\1\ufaff\1\116\2\uffff\1"+
        "\124\1\ufaff\1\116\1\113\1\103\1\107\1\122\2\ufaff\1\120\1\uffff"+
        "\1\124\1\113\2\111\1\105\1\114\1\101\1\117\1\105\1\122\1\111\1\106"+
        "\1\111\1\124\1\122\1\111\1\105\1\ufaff\2\103\1\102\1\105\2\124\1"+
        "\110\1\ufaff\1\103\1\ufaff\1\114\1\123\1\111\1\101\1\117\1\101\1"+
        "\103\1\114\1\123\1\145\1\150\1\163\1\146\1\157\1\156\1\145\1\166"+
        "\1\143\1\145\1\157\1\145\1\171\1\166\1\171\1\160\1\154\1\145\1\ufaff"+
        "\1\145\1\154\1\141\1\ufaff\1\uffff\1\uffff\1\146\2\uffff\1\uffff"+
        "\1\ufaff\1\123\1\114\1\ufaff\1\uffff\1\105\1\111\1\124\1\122\1\101"+
        "\1\uffff\1\105\1\124\1\ufaff\1\122\1\101\1\uffff\1\114\1\124\1\uffff"+
        "\1\111\1\107\1\122\1\uffff\1\117\1\123\1\111\1\uffff\1\124\2\ufaff"+
        "\1\uffff\2\ufaff\1\110\1\105\1\ufaff\1\uffff\1\ufaff\1\117\1\105"+
        "\1\101\1\124\1\117\1\130\1\101\1\111\1\126\1\123\1\105\1\120\1\105"+
        "\1\103\1\105\1\124\1\116\1\ufaff\1\uffff\1\111\1\105\1\117\1\ufaff"+
        "\1\125\1\ufaff\3\105\1\111\1\uffff\1\ufaff\1\116\1\uffff\1\101\1"+
        "\127\1\116\2\122\2\113\1\105\1\124\1\ufaff\1\163\1\145\1\151\1\ufaff"+
        "\1\151\1\162\1\145\1\153\1\ufaff\1\157\1\156\1\ufaff\1\145\1\ufaff"+
        "\1\154\2\ufaff\1\uffff\1\ufaff\1\157\1\ufaff\1\uffff\1\146\2\uffff"+
        "\2\uffff\1\106\1\103\1\131\1\111\1\uffff\1\111\1\130\1\124\4\ufaff"+
        "\1\116\1\105\1\uffff\1\ufaff\1\122\1\105\1\111\1\101\1\102\1\116"+
        "\1\ufaff\1\116\1\124\1\123\1\101\3\uffff\1\103\1\116\3\101\1\120"+
        "\1\uffff\1\105\1\ufaff\1\uffff\1\106\1\ufaff\1\107\1\111\1\104\1"+
        "\120\1\103\1\116\1\105\2\ufaff\1\124\1\101\1\117\1\116\1\123\1\107"+
        "\1\uffff\1\101\1\ufaff\1\114\1\uffff\1\122\1\uffff\2\122\1\101\1"+
        "\123\1\uffff\1\104\1\115\1\111\1\105\1\113\1\105\2\ufaff\1\101\1"+
        "\111\1\164\1\ufaff\1\uffff\1\151\1\ufaff\1\147\1\156\1\145\1\162"+
        "\1\ufaff\1\156\2\ufaff\1\145\1\uffff\1\167\1\146\1\uffff\1\105\1"+
        "\117\2\123\1\116\1\124\1\111\2\uffff\1\124\1\ufaff\1\uffff\1\105"+
        "\1\ufaff\1\123\1\102\1\125\1\ufaff\1\uffff\2\ufaff\1\124\1\107\1"+
        "\117\1\103\1\123\1\102\1\123\1\ufaff\1\104\1\uffff\1\ufaff\1\uffff"+
        "\1\105\1\117\2\ufaff\1\105\1\124\1\ufaff\2\uffff\1\ufaff\1\124\1"+
        "\114\1\103\1\127\1\ufaff\1\114\1\ufaff\1\105\1\124\1\ufaff\2\124"+
        "\1\ufaff\1\101\1\124\3\ufaff\1\uffff\1\116\1\117\1\ufaff\1\141\1"+
        "\uffff\1\ufaff\1\147\5\ufaff\1\146\1\122\1\125\2\124\1\123\1\103"+
        "\1\117\1\103\1\uffff\1\ufaff\1\111\1\uffff\1\124\1\114\1\124\3\uffff"+
        "\2\ufaff\1\122\1\105\1\124\1\114\2\124\1\105\1\uffff\1\ufaff\1\116"+
        "\1\uffff\1\ufaff\1\101\1\131\1\125\1\124\2\uffff\1\125\1\117\1\105"+
        "\2\111\1\uffff\1\ufaff\1\uffff\1\ufaff\1\101\1\131\1\uffff\1\125"+
        "\1\ufaff\1\uffff\1\124\1\110\1\uffff\1\114\2\uffff\1\ufaff\1\116"+
        "\2\ufaff\1\uffff\1\ufaff\1\116\1\105\2\ufaff\1\117\1\116\1\117\1"+
        "\uffff\1\123\1\ufaff\2\105\2\uffff\1\105\2\ufaff\1\105\1\ufaff\1"+
        "\105\1\121\1\uffff\1\ufaff\1\uffff\1\122\2\120\2\122\1\116\1\105"+
        "\1\124\1\123\1\uffff\1\122\1\120\1\122\1\uffff\1\111\1\ufaff\1\114"+
        "\1\111\1\uffff\1\ufaff\1\uffff\1\124\1\115\2\uffff\1\125\1\ufaff"+
        "\1\125\1\124\1\uffff\3\ufaff\2\uffff\1\ufaff\1\uffff\1\130\1\ufaff"+
        "\1\uffff\1\113\1\105\1\114\1\111\1\105\1\ufaff\1\116\1\110\1\124"+
        "\1\113\2\105\1\117\1\uffff\1\ufaff\1\123\2\ufaff\1\116\1\uffff\1"+
        "\116\1\ufaff\4\uffff\1\124\1\uffff\1\125\1\ufaff\1\111\1\116\1\ufaff"+
        "\1\104\2\ufaff\1\125\2\ufaff\1\116\1\uffff\1\124\2\uffff\2\124\1"+
        "\uffff\1\ufaff\1\120\1\uffff\1\103\1\107\1\uffff\1\ufaff\2\uffff"+
        "\1\120\2\uffff\4\ufaff\1\uffff\1\ufaff\1\101\2\ufaff\4\uffff\1\124"+
        "\2\uffff\1\105\1\ufaff\1\uffff";
    static final String DFA30_acceptS =
        "\54\uffff\1\151\1\153\1\154\1\155\1\156\1\157\1\160\1\161\1\162"+
        "\1\163\1\165\1\166\1\167\1\170\1\172\1\uffff\1\174\1\175\3\uffff"+
        "\1\u0081\1\u0082\1\u0084\1\u0088\15\uffff\1\106\56\uffff\1\105\1"+
        "\171\26\uffff\1\144\1\uffff\1\147\1\145\1\uffff\1\164\2\uffff\1"+
        "\152\1\u0089\1\u008a\1\173\1\u0085\1\u0086\1\176\1\u0087\1\177\1"+
        "\u0083\1\u0080\37\uffff\1\67\1\27\12\uffff\1\20\74\uffff\1\146\4"+
        "\uffff\1\150\4\uffff\1\140\5\uffff\1\137\5\uffff\1\37\2\uffff\1"+
        "\6\3\uffff\1\73\3\uffff\1\117\3\uffff\1\40\5\uffff\1\72\23\uffff"+
        "\1\136\12\uffff\1\77\2\uffff\1\134\33\uffff\1\110\3\uffff\1\141"+
        "\3\uffff\1\150\1\1\4\uffff\1\116\11\uffff\1\54\14\uffff\1\15\1\135"+
        "\1\16\6\uffff\1\41\2\uffff\1\17\21\uffff\1\101\3\uffff\1\26\1\uffff"+
        "\1\34\4\uffff\1\55\14\uffff\1\107\13\uffff\1\142\12\uffff\1\10\1"+
        "\36\2\uffff\1\104\6\uffff\1\66\13\uffff\1\76\1\uffff\1\31\7\uffff"+
        "\1\115\1\25\23\uffff\1\114\4\uffff\1\143\20\uffff\1\32\2\uffff\1"+
        "\120\3\uffff\1\56\1\126\1\13\11\uffff\1\21\2\uffff\1\24\5\uffff"+
        "\1\74\1\112\5\uffff\1\121\1\uffff\1\124\3\uffff\1\33\2\uffff\1\64"+
        "\2\uffff\1\113\1\uffff\1\61\1\65\15\uffff\1\3\4\uffff\1\130\1\14"+
        "\7\uffff\1\111\1\uffff\1\47\11\uffff\1\30\3\uffff\1\100\4\uffff"+
        "\1\122\1\uffff\1\63\2\uffff\1\133\1\2\4\uffff\1\4\3\uffff\1\43\1"+
        "\44\1\uffff\1\46\2\uffff\1\23\15\uffff\1\71\5\uffff\1\125\2\uffff"+
        "\1\5\1\35\1\42\1\45\1\uffff\1\22\14\uffff\1\62\1\uffff\1\11\1\123"+
        "\2\uffff\1\131\2\uffff\1\51\2\uffff\1\57\1\uffff\1\70\1\132\1\uffff"+
        "\1\53\1\60\4\uffff\1\102\4\uffff\1\127\1\7\1\12\1\50\1\uffff\1\103"+
        "\1\52\2\uffff\1\75";
    static final String DFA30_specialS =
        "\53\uffff\1\7\164\uffff\1\5\164\uffff\1\4\1\uffff\1\0\1\10\152"+
        "\uffff\1\1\1\2\142\uffff\1\3\u008a\uffff\1\6\u008f\uffff}>";
    static final String[] DFA30_transitionS = {
            "\2\104\1\uffff\2\104\22\uffff\1\104\1\103\1\54\1\uffff\1\55"+
            "\1\101\1\75\1\53\1\56\1\57\1\72\1\71\1\67\1\24\1\52\1\73\1\50"+
            "\11\51\1\66\1\70\1\76\1\100\1\77\1\102\1\65\1\5\1\23\1\2\1\3"+
            "\1\21\1\17\1\20\1\55\1\6\2\55\1\7\1\10\1\11\1\12\1\13\1\25\1"+
            "\14\1\15\1\1\1\22\1\16\1\4\1\47\2\55\1\60\1\uffff\1\61\1\64"+
            "\1\26\1\uffff\1\46\1\34\1\30\2\55\1\27\1\36\1\55\1\31\2\55\1"+
            "\44\1\35\1\40\1\37\1\41\1\55\1\43\1\33\1\42\2\55\1\32\1\55\1"+
            "\45\1\55\1\62\1\74\1\63\102\uffff\27\55\1\uffff\37\55\1\uffff"+
            "\u1f08\55\u1040\uffff\u0150\55\u0170\uffff\u0080\55\u0080\uffff"+
            "\u092e\55\u10d2\uffff\u5200\55\u5900\uffff\u0200\55",
            "\1\110\15\uffff\1\106\2\uffff\1\105\6\uffff\1\107",
            "\1\114\12\uffff\1\115\2\uffff\1\111\2\uffff\1\113\2\uffff"+
            "\1\112\1\uffff\1\116",
            "\1\117\11\uffff\1\120",
            "\1\55\13\uffff\12\55\7\uffff\16\55\1\121\13\55\4\uffff\1\55"+
            "\1\uffff\32\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55"+
            "\u1040\uffff\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e"+
            "\55\u10d2\uffff\u5200\55\u5900\uffff\u0200\55",
            "\1\132\1\127\1\uffff\1\126\5\uffff\1\131\1\130\1\123\4\uffff"+
            "\1\125\1\124",
            "\1\135\7\uffff\1\133\4\uffff\1\134",
            "\1\136\15\uffff\1\137",
            "\1\141\3\uffff\1\142\11\uffff\1\140",
            "\1\146\2\uffff\1\143\11\uffff\1\144\5\uffff\1\145",
            "\1\147",
            "\1\150\3\uffff\1\152\7\uffff\1\153\1\uffff\1\151",
            "\1\154\17\uffff\1\155",
            "\1\156\1\uffff\1\157\3\uffff\1\161\6\uffff\1\163\3\uffff\1"+
            "\160\2\uffff\1\162\1\uffff\1\164",
            "\1\165",
            "\1\166\3\uffff\1\167",
            "\1\170\3\uffff\1\171",
            "\1\173\11\uffff\1\172",
            "\1\174",
            "\1\175\6\uffff\1\177\2\uffff\1\u0080\2\uffff\1\176",
            "\1\u0081",
            "\1\u0083",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32"+
            "\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\u0086\7\uffff\1\u0084\13\uffff\1\u0085",
            "\1\u0087",
            "\1\u0088",
            "\1\u0089",
            "\1\u008a\3\uffff\1\u008b",
            "\1\u008c",
            "\1\u008d",
            "\1\u008e",
            "\1\u008f",
            "\1\u0090",
            "\1\u0091",
            "\1\u0092\14\uffff\1\u0093",
            "\1\u0094",
            "\1\u0095",
            "\1\u0096",
            "\1\u0097",
            "\1\u0098",
            "\1\u009b\1\uffff\10\u009a\2\u009b\36\uffff\1\u0099\37\uffff"+
            "\1\u0099",
            "\1\u009b\1\uffff\12\u009d",
            "\12\u009b",
            "\47\u00a0\1\u00a1\64\u00a0\1\u009f\uffa3\u00a0",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\u00a2\4\uffff\1\u00a3",
            "",
            "",
            "\1\u00a6\1\u00a5",
            "\1\u00a8",
            "\1\u00aa",
            "",
            "",
            "",
            "",
            "\1\u00ad\7\uffff\1\u00ac",
            "\1\u00ae",
            "\1\u00af",
            "\1\u00b0",
            "\1\u00b3\1\u00b4\1\u00b1\4\uffff\1\u00b5\1\uffff\1\u00b2",
            "\1\u00b6",
            "\1\u00b7",
            "\1\u00b8\3\uffff\1\u00b9",
            "\1\u00ba",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32"+
            "\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\u00bb\10\uffff\1\u00bc",
            "\1\u00bd",
            "\1\u00be",
            "",
            "\1\u00bf\24\uffff\1\u00c0",
            "\1\u00c1",
            "\1\u00c2",
            "\1\u00c3",
            "\1\u00c4",
            "\1\u00c5",
            "\1\u00c6",
            "\1\u00c7",
            "\1\u00c8\7\uffff\1\u00c9",
            "\1\55\13\uffff\12\55\7\uffff\10\55\1\u00ca\21\55\4\uffff\1"+
            "\55\1\uffff\32\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08"+
            "\55\u1040\uffff\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e"+
            "\55\u10d2\uffff\u5200\55\u5900\uffff\u0200\55",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32"+
            "\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\u00cd",
            "\1\u00ce",
            "\1\u00cf",
            "\1\u00d0\1\uffff\1\u00d1",
            "\1\u00d2",
            "\1\u00d3",
            "\1\u00d4",
            "\1\u00d5",
            "\1\u00d6",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32"+
            "\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\u00d9\16\uffff\1\u00d8",
            "\1\u00da",
            "\1\u00db",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32"+
            "\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\u00dc\5\uffff\1\u00df\2\uffff\1\u00dd\3\uffff\1\u00de",
            "\1\u00e0",
            "\1\u00e1\2\uffff\1\u00e2",
            "\1\u00e4\1\u00e5\5\uffff\1\u00e3",
            "\1\u00e6\20\uffff\1\u00e7",
            "\1\u00e9\13\uffff\1\u00e8",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32"+
            "\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\u00eb\3\uffff\1\u00ea",
            "\1\u00ec",
            "\1\u00ed",
            "\1\u00ee",
            "\1\u00ef",
            "\1\u00f0",
            "\1\u00f1",
            "\1\u00f4\1\uffff\1\u00f2\12\uffff\1\u00f3",
            "\1\u00f5\2\uffff\1\u00f6",
            "\1\u00f7",
            "\1\u00f8",
            "\1\u00f9",
            "\1\u00fa",
            "\1\u00fb",
            "",
            "",
            "\1\u00fc",
            "\1\u00fd",
            "\1\u00fe",
            "\1\u00ff",
            "\1\u0100",
            "\1\u0101",
            "\1\u0102",
            "\1\u0103",
            "\1\u0104",
            "\1\u0105\23\uffff\1\u0106",
            "\1\u0107",
            "\1\u0109\3\uffff\1\u0108",
            "\1\u010a",
            "\1\u010b",
            "\1\u010c",
            "\1\u010d",
            "\1\u010e",
            "\1\u010f",
            "\1\u0110",
            "\1\u0111",
            "\1\u0112",
            "\1\u0113",
            "",
            "\1\u009b\1\uffff\10\u009a\2\u009b",
            "",
            "",
            "\1\u009b\1\uffff\12\u009d",
            "",
            "\1\u0115\4\uffff\1\u0115\10\uffff\4\u0117\4\u0118\44\uffff"+
            "\1\u0115\5\uffff\1\u0115\3\uffff\1\u0115\7\uffff\1\u0115\3\uffff"+
            "\1\u0115\1\uffff\1\u0115\1\u0116",
            "\47\u00a1\1\u0119\uffd8\u00a1",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\u011a",
            "\1\u011b",
            "\1\u011c",
            "\1\u011d",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32"+
            "\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\u0120\17\uffff\1\u011f",
            "\1\u0121",
            "\1\u0122",
            "\1\u0123",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32"+
            "\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\u0125",
            "\1\u0126",
            "\1\u0127",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32"+
            "\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\u0128",
            "\1\u0129",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32"+
            "\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\u012b",
            "\1\u012c",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32"+
            "\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32"+
            "\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\u012e",
            "\1\u012f",
            "\1\u0130",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32"+
            "\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32"+
            "\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32"+
            "\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\u0132",
            "\1\u0133",
            "\1\55\13\uffff\12\55\7\uffff\13\55\1\u0134\16\55\4\uffff\1"+
            "\55\1\uffff\32\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08"+
            "\55\u1040\uffff\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e"+
            "\55\u10d2\uffff\u5200\55\u5900\uffff\u0200\55",
            "\1\u0136",
            "",
            "",
            "\1\u0137",
            "\1\55\13\uffff\12\55\7\uffff\15\55\1\u0138\14\55\4\uffff\1"+
            "\55\1\uffff\32\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08"+
            "\55\u1040\uffff\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e"+
            "\55\u10d2\uffff\u5200\55\u5900\uffff\u0200\55",
            "\1\u013a",
            "\1\u013b",
            "\1\u013c",
            "\1\u013d",
            "\1\u013e",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32"+
            "\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32"+
            "\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\u0140",
            "",
            "\1\u0142\1\u0141",
            "\1\u0143",
            "\1\u0144",
            "\1\u0145",
            "\1\u0146",
            "\1\u0147",
            "\1\u0148",
            "\1\u0149",
            "\1\u014a",
            "\1\u014b",
            "\1\u014c",
            "\1\u014d",
            "\1\u014e",
            "\1\u014f",
            "\1\u0150",
            "\1\u0151",
            "\1\u0152",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32"+
            "\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\u0154",
            "\1\u0155",
            "\1\u0156",
            "\1\u0157",
            "\1\u0158",
            "\1\u0159\7\uffff\1\u015a",
            "\1\u015b",
            "\1\55\13\uffff\12\55\7\uffff\5\55\1\u015c\5\55\1\u015d\16"+
            "\55\4\uffff\1\55\1\uffff\32\55\105\uffff\27\55\1\uffff\37\55"+
            "\1\uffff\u1f08\55\u1040\uffff\u0150\55\u0170\uffff\u0080\55"+
            "\u0080\uffff\u092e\55\u10d2\uffff\u5200\55\u5900\uffff\u0200"+
            "\55",
            "\1\u015f",
            "\1\55\13\uffff\12\55\7\uffff\1\u0160\31\55\4\uffff\1\55\1"+
            "\uffff\32\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55"+
            "\u1040\uffff\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e"+
            "\55\u10d2\uffff\u5200\55\u5900\uffff\u0200\55",
            "\1\u0162",
            "\1\u0163",
            "\1\u0164",
            "\1\u0165",
            "\1\u0166",
            "\1\u0167",
            "\1\u0168",
            "\1\u0169",
            "\1\u016a",
            "\1\u016b",
            "\1\u016c",
            "\1\u016d",
            "\1\u016e",
            "\1\u016f",
            "\1\u0170",
            "\1\u0171",
            "\1\u0172",
            "\1\u0173",
            "\1\u0174",
            "\1\u0175",
            "\1\u0176",
            "\1\u0177",
            "\1\u0178",
            "\1\u0179",
            "\1\u017a",
            "\1\u017b",
            "\1\u017c",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32"+
            "\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\u017e",
            "\1\u017f",
            "\1\u0180",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32"+
            "\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "",
            "\47\u00a1\1\u0119\uffd8\u00a1",
            "\12\u0182\7\uffff\6\u0182\32\uffff\6\u0182",
            "\47\u00a1\1\u0119\10\u00a1\10\u0183\uffc8\u00a1",
            "\47\u00a1\1\u0119\10\u00a1\10\u0184\uffc8\u00a1",
            "",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32"+
            "\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\u0187",
            "\1\u0188",
            "\1\55\13\uffff\12\55\7\uffff\13\55\1\u018a\6\55\1\u0189\7"+
            "\55\4\uffff\1\55\1\uffff\32\55\105\uffff\27\55\1\uffff\37\55"+
            "\1\uffff\u1f08\55\u1040\uffff\u0150\55\u0170\uffff\u0080\55"+
            "\u0080\uffff\u092e\55\u10d2\uffff\u5200\55\u5900\uffff\u0200"+
            "\55",
            "",
            "\1\u018c\3\uffff\1\u018d",
            "\1\u018e",
            "\1\u018f",
            "\1\u0191\3\uffff\1\u0190",
            "\1\u0192",
            "",
            "\1\u0193",
            "\1\u0194",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32"+
            "\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\u0196",
            "\1\u0197",
            "",
            "\1\u0198",
            "\1\u0199\7\uffff\1\u019a",
            "",
            "\1\u019b",
            "\1\u019c",
            "\1\u019d",
            "",
            "\1\u019e",
            "\1\u019f",
            "\1\u01a0",
            "",
            "\1\u01a1",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32"+
            "\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32"+
            "\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32"+
            "\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\55\13\uffff\12\55\7\uffff\5\55\1\u01a7\5\55\1\u01a9\2\55"+
            "\1\u01a6\3\55\1\u01a5\1\u01a8\1\u01aa\5\55\4\uffff\1\55\1\uffff"+
            "\32\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040"+
            "\uffff\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2"+
            "\uffff\u5200\55\u5900\uffff\u0200\55",
            "\1\u01ac",
            "\1\u01ad",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32"+
            "\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32"+
            "\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\u01af",
            "\1\u01b0",
            "\1\u01b1",
            "\1\u01b2",
            "\1\u01b3",
            "\1\u01b4",
            "\1\u01b5",
            "\1\u01b6",
            "\1\u01b7",
            "\1\u01b8",
            "\1\u01b9",
            "\1\u01ba",
            "\1\u01bb",
            "\1\u01bc",
            "\1\u01bd",
            "\1\u01be",
            "\1\u01bf",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32"+
            "\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "",
            "\1\u01c1",
            "\1\u01c2",
            "\1\u01c3",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32"+
            "\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\u01c5",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32"+
            "\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\u01c7",
            "\1\u01c8",
            "\1\u01c9",
            "\1\u01ca",
            "",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32"+
            "\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\u01cc",
            "",
            "\1\u01cd",
            "\1\u01ce",
            "\1\u01cf",
            "\1\u01d0",
            "\1\u01d1",
            "\1\u01d2",
            "\1\u01d3",
            "\1\u01d4",
            "\1\u01d5",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\21"+
            "\55\1\u01d7\1\u01d6\7\55\105\uffff\27\55\1\uffff\37\55\1\uffff"+
            "\u1f08\55\u1040\uffff\u0150\55\u0170\uffff\u0080\55\u0080\uffff"+
            "\u092e\55\u10d2\uffff\u5200\55\u5900\uffff\u0200\55",
            "\1\u01d9",
            "\1\u01da",
            "\1\u01db",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32"+
            "\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\u01dc",
            "\1\u01dd",
            "\1\u01de",
            "\1\u01df",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32"+
            "\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\u01e0",
            "\1\u01e1",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32"+
            "\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\u01e2",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32"+
            "\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\u01e3",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32"+
            "\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32"+
            "\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32"+
            "\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\u01e5",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32"+
            "\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "",
            "\12\u01e6\7\uffff\6\u01e6\32\uffff\6\u01e6",
            "\47\u00a1\1\u0119\10\u00a1\10\u01e7\uffc8\u00a1",
            "\47\u00a1\1\u0119\uffd8\u00a1",
            "",
            "",
            "\1\u01e8",
            "\1\u01e9",
            "\1\u01ea",
            "\1\u01eb",
            "",
            "\1\u01ec",
            "\1\u01ed",
            "\1\u01ee",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32"+
            "\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32"+
            "\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32"+
            "\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32"+
            "\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\u01f1",
            "\1\u01f2",
            "",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32"+
            "\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\u01f4",
            "\1\u01f5",
            "\1\u01f6",
            "\1\u01f7",
            "\1\u01f8",
            "\1\u01f9",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32"+
            "\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\u01fb",
            "\1\u01fc",
            "\1\u01fd",
            "\1\u01fe",
            "",
            "",
            "",
            "\1\u01ff",
            "\1\u0200",
            "\1\u0201",
            "\1\u0202",
            "\1\u0203",
            "\1\u0204",
            "",
            "\1\u0205",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32"+
            "\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "",
            "\1\u0207",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32"+
            "\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\u0209",
            "\1\u020a",
            "\1\u020b",
            "\1\u020c",
            "\1\u020d",
            "\1\u020e",
            "\1\u020f",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32"+
            "\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32"+
            "\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\u0212",
            "\1\u0213",
            "\1\u0214",
            "\1\u0215",
            "\1\u0216",
            "\1\u0217",
            "",
            "\1\u0218",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32"+
            "\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\u0219",
            "",
            "\1\u021a",
            "",
            "\1\u021b",
            "\1\u021c",
            "\1\u021d",
            "\1\u021e",
            "",
            "\1\u021f",
            "\1\u0220",
            "\1\u0221",
            "\1\u0222",
            "\1\u0223",
            "\1\u0224",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32"+
            "\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32"+
            "\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\u0226",
            "\1\u0227",
            "\1\u0228",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32"+
            "\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "",
            "\1\u0229",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32"+
            "\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\u022b",
            "\1\u022c",
            "\1\u022d",
            "\1\u022e",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32"+
            "\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\u022f",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32"+
            "\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32"+
            "\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\u0230",
            "",
            "\1\u0231",
            "\12\u0232\7\uffff\6\u0232\32\uffff\6\u0232",
            "\47\u00a1\1\u0119\uffd8\u00a1",
            "\1\u0233",
            "\1\u0234",
            "\1\u0235",
            "\1\u0236",
            "\1\u0237",
            "\1\u0238",
            "\1\u0239",
            "",
            "",
            "\1\u023a",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32"+
            "\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "",
            "\1\u023c",
            "\1\55\13\uffff\12\55\7\uffff\13\55\1\u023d\16\55\4\uffff\1"+
            "\55\1\uffff\32\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08"+
            "\55\u1040\uffff\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e"+
            "\55\u10d2\uffff\u5200\55\u5900\uffff\u0200\55",
            "\1\u023f",
            "\1\u0240",
            "\1\u0241",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32"+
            "\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32"+
            "\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32"+
            "\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\u0245",
            "\1\u0246",
            "\1\u0247",
            "\1\u0248",
            "\1\u0249",
            "\1\u024a",
            "\1\u024b",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32"+
            "\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\u024c",
            "",
            "\1\55\13\uffff\12\55\7\uffff\15\55\1\u024d\14\55\4\uffff\1"+
            "\55\1\uffff\32\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08"+
            "\55\u1040\uffff\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e"+
            "\55\u10d2\uffff\u5200\55\u5900\uffff\u0200\55",
            "",
            "\1\u024f",
            "\1\u0250",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32"+
            "\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32"+
            "\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\u0252",
            "\1\u0253\6\uffff\1\u0254",
            "\1\55\13\uffff\12\55\7\uffff\3\55\1\u0255\16\55\1\u0256\7"+
            "\55\4\uffff\1\55\1\uffff\32\55\105\uffff\27\55\1\uffff\37\55"+
            "\1\uffff\u1f08\55\u1040\uffff\u0150\55\u0170\uffff\u0080\55"+
            "\u0080\uffff\u092e\55\u10d2\uffff\u5200\55\u5900\uffff\u0200"+
            "\55",
            "",
            "",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32"+
            "\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\u0259",
            "\1\u025a",
            "\1\u025b",
            "\1\u025c",
            "\1\55\13\uffff\12\55\7\uffff\13\55\1\u025d\16\55\4\uffff\1"+
            "\55\1\uffff\32\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08"+
            "\55\u1040\uffff\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e"+
            "\55\u10d2\uffff\u5200\55\u5900\uffff\u0200\55",
            "\1\u025f",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32"+
            "\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\u0261",
            "\1\u0262\6\uffff\1\u0263",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32"+
            "\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\u0265",
            "\1\u0266",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32"+
            "\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\u0268",
            "\1\u0269",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32"+
            "\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\55\13\uffff\12\55\7\uffff\1\u026b\31\55\4\uffff\1\55\1"+
            "\uffff\32\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55"+
            "\u1040\uffff\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e"+
            "\55\u10d2\uffff\u5200\55\u5900\uffff\u0200\55",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32"+
            "\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "",
            "\1\u026e",
            "\1\u026f",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32"+
            "\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\u0270",
            "",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32"+
            "\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\u0271",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32"+
            "\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32"+
            "\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32"+
            "\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32"+
            "\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32"+
            "\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\12\u0272\7\uffff\6\u0272\32\uffff\6\u0272",
            "\1\u0273",
            "\1\u0274",
            "\1\u0275",
            "\1\u0276",
            "\1\u0277",
            "\1\u0278",
            "\1\u0279",
            "\1\u027a",
            "",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32"+
            "\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\u027c",
            "",
            "\1\u027d",
            "\1\u027e",
            "\1\u027f",
            "",
            "",
            "",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32"+
            "\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32"+
            "\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\u0282",
            "\1\u0283",
            "\1\u0284",
            "\1\u0285",
            "\1\u0286",
            "\1\u0287",
            "\1\u0288",
            "",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32"+
            "\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\u028a",
            "",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32"+
            "\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\u028c",
            "\1\u028d",
            "\1\u028e",
            "\1\u028f",
            "",
            "",
            "\1\u0290",
            "\1\u0291",
            "\1\u0292",
            "\1\u0293",
            "\1\u0294",
            "",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32"+
            "\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32"+
            "\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\u0296",
            "\1\u0297",
            "",
            "\1\u0298",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32"+
            "\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "",
            "\1\u029a",
            "\1\u029b",
            "",
            "\1\u029c",
            "",
            "",
            "\1\55\13\uffff\12\55\7\uffff\13\55\1\u029d\16\55\4\uffff\1"+
            "\55\1\uffff\32\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08"+
            "\55\u1040\uffff\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e"+
            "\55\u10d2\uffff\u5200\55\u5900\uffff\u0200\55",
            "\1\u029f",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32"+
            "\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32"+
            "\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\47\u00a1\1\u0119\uffd8\u00a1",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32"+
            "\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\u02a1",
            "\1\u02a2",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32"+
            "\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32"+
            "\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\u02a5",
            "\1\u02a6",
            "\1\u02a7",
            "",
            "\1\u02a8",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32"+
            "\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\u02aa",
            "\1\u02ab",
            "",
            "",
            "\1\u02ac",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32"+
            "\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32"+
            "\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\u02af",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32"+
            "\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\u02b1",
            "\1\u02b2",
            "",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32"+
            "\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "",
            "\1\u02b4",
            "\1\u02b5",
            "\1\u02b6",
            "\1\u02b7",
            "\1\u02b8",
            "\1\u02b9",
            "\1\u02ba",
            "\1\u02bb",
            "\1\u02bc",
            "",
            "\1\u02bd",
            "\1\u02be",
            "\1\u02bf",
            "",
            "\1\u02c0",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32"+
            "\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\u02c2",
            "\1\u02c3",
            "",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32"+
            "\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "",
            "\1\u02c4",
            "\1\u02c5",
            "",
            "",
            "\1\u02c6",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32"+
            "\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\u02c8",
            "\1\u02c9",
            "",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32"+
            "\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32"+
            "\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32"+
            "\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "",
            "",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32"+
            "\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "",
            "\1\u02ce",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32"+
            "\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "",
            "\1\u02d0",
            "\1\u02d1",
            "\1\u02d2",
            "\1\u02d3",
            "\1\u02d4",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32"+
            "\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\u02d5",
            "\1\u02d6",
            "\1\u02d7",
            "\1\u02d8",
            "\1\u02d9",
            "\1\u02da",
            "\1\u02db",
            "",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32"+
            "\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\u02dd",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32"+
            "\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32"+
            "\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\u02e0",
            "",
            "\1\u02e1",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32"+
            "\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "",
            "",
            "",
            "",
            "\1\u02e3",
            "",
            "\1\u02e4",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32"+
            "\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\u02e6",
            "\1\u02e7",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32"+
            "\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\u02e9",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32"+
            "\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32"+
            "\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\u02ec",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32"+
            "\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32"+
            "\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\u02ef",
            "",
            "\1\u02f0",
            "",
            "",
            "\1\u02f1",
            "\1\u02f2",
            "",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32"+
            "\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\u02f4",
            "",
            "\1\u02f5",
            "\1\u02f6",
            "",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32"+
            "\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "",
            "",
            "\1\u02f7",
            "",
            "",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32"+
            "\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32"+
            "\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32"+
            "\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32"+
            "\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32"+
            "\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\u02fc",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32"+
            "\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32"+
            "\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            "",
            "",
            "",
            "",
            "\1\u02ff",
            "",
            "",
            "\1\u0300",
            "\1\55\13\uffff\12\55\7\uffff\32\55\4\uffff\1\55\1\uffff\32"+
            "\55\105\uffff\27\55\1\uffff\37\55\1\uffff\u1f08\55\u1040\uffff"+
            "\u0150\55\u0170\uffff\u0080\55\u0080\uffff\u092e\55\u10d2\uffff"+
            "\u5200\55\u5900\uffff\u0200\55",
            ""
    };

    static final short[] DFA30_eot = DFA.unpackEncodedString(DFA30_eotS);
    static final short[] DFA30_eof = DFA.unpackEncodedString(DFA30_eofS);
    static final char[] DFA30_min = DFA.unpackEncodedStringToUnsignedChars(DFA30_minS);
    static final char[] DFA30_max = DFA.unpackEncodedStringToUnsignedChars(DFA30_maxS);
    static final short[] DFA30_accept = DFA.unpackEncodedString(DFA30_acceptS);
    static final short[] DFA30_special = DFA.unpackEncodedString(DFA30_specialS);
    static final short[][] DFA30_transition;

    static {
        int numStates = DFA30_transitionS.length;
        DFA30_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA30_transition[i] = DFA.unpackEncodedString(DFA30_transitionS[i]);
        }
    }

    class DFA30 extends DFA {

        public DFA30(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 30;
            this.eot = DFA30_eot;
            this.eof = DFA30_eof;
            this.min = DFA30_min;
            this.max = DFA30_max;
            this.accept = DFA30_accept;
            this.special = DFA30_special;
            this.transition = DFA30_transition;
        }
        public String getDescription() {
            return "1:1: Tokens : ( TRIE | CONTAINS | DECLARE | WORDLIST | WORDTABLE | AND | CONTEXTCOUNT | COUNT | TOTALCOUNT | CURRENTCOUNT | INLIST | ISINTAG | LAST | MOFN | NEAR | OR | PARTOF | PARTOFNEQ | POSITION | REGEXP | SCORE | VOTE | IF | FEATURE | PARSE | CREATE | GATHER | FILL | ATTRIBUTE | COLOR | DEL | LOG | MARK | MARKSCORE | MARKONCE | MARKFAST | MARKTABLE | MARKLAST | REPLACE | RETAINMARKUP | RETAINTYPE | FILTERMARKUP | FILTERTYPE | CALL | EXEC | ASSIGN | SETFEATURE | GETFEATURE | UNMARK | UNMARKALL | TRANSFER | EXPAND | BEFORE | AFTER | IS | STARTSWITH | ENDSWITH | NOT | ADD | REMOVE | REMOVEDUPLICATE | MERGE | GET | GETLIST | SIZE | MATCHEDTEXT | REMOVESTRING | CLEAR | THEN | BasicAnnotationType | LogLevel | OldColor | PackageString | ScriptString | EngineString | BlockString | AutomataBlockString | TypeString | IntString | DoubleString | StringString | BooleanString | TypeSystemString | SymbolString | CONDITION | ACTION | BOOLEANLIST | INTLIST | DOUBLELIST | STRINGLIST | TYPELIST | EXP | LOGN | SIN | COS | TAN | XOR | TRUE | FALSE | HexLiteral | DecimalLiteral | OctalLiteral | FloatingPointLiteral | CharacterLiteral | StringLiteral | RessourceLiteral | Identifier | LPAREN | RPAREN | LBRACK | RBRACK | LCURLY | RCURLY | CIRCUMFLEX | AT | DOT | COLON | COMMA | SEMI | PLUS | MINUS | STAR | SLASH | VBAR | AMPER | LESS | GREATER | ASSIGN_EQUAL | PERCENT | QUESTION | EQUAL | NOTEQUAL | ALT_NOTEQUAL | LESSEQUAL | GREATEREQUAL | WS | COMMENT | LINE_COMMENT );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            IntStream input = _input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA30_279 = input.LA(1);

                        s = -1;
                        if ( (LA30_279=='\'') ) {s = 281;}

                        else if ( ((LA30_279>='0' && LA30_279<='7')) ) {s = 387;}

                        else if ( ((LA30_279>='\u0000' && LA30_279<='&')||(LA30_279>='(' && LA30_279<='/')||(LA30_279>='8' && LA30_279<='\uFFFF')) ) {s = 161;}

                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA30_387 = input.LA(1);

                        s = -1;
                        if ( (LA30_387=='\'') ) {s = 281;}

                        else if ( ((LA30_387>='\u0000' && LA30_387<='&')||(LA30_387>='(' && LA30_387<='/')||(LA30_387>='8' && LA30_387<='\uFFFF')) ) {s = 161;}

                        else if ( ((LA30_387>='0' && LA30_387<='7')) ) {s = 487;}

                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA30_388 = input.LA(1);

                        s = -1;
                        if ( (LA30_388=='\'') ) {s = 281;}

                        else if ( ((LA30_388>='\u0000' && LA30_388<='&')||(LA30_388>='(' && LA30_388<='\uFFFF')) ) {s = 161;}

                        if ( s>=0 ) return s;
                        break;
                    case 3 : 
                        int LA30_487 = input.LA(1);

                        s = -1;
                        if ( (LA30_487=='\'') ) {s = 281;}

                        else if ( ((LA30_487>='\u0000' && LA30_487<='&')||(LA30_487>='(' && LA30_487<='\uFFFF')) ) {s = 161;}

                        if ( s>=0 ) return s;
                        break;
                    case 4 : 
                        int LA30_277 = input.LA(1);

                        s = -1;
                        if ( (LA30_277=='\'') ) {s = 281;}

                        else if ( ((LA30_277>='\u0000' && LA30_277<='&')||(LA30_277>='(' && LA30_277<='\uFFFF')) ) {s = 161;}

                        if ( s>=0 ) return s;
                        break;
                    case 5 : 
                        int LA30_160 = input.LA(1);

                        s = -1;
                        if ( (LA30_160=='\'') ) {s = 281;}

                        else if ( ((LA30_160>='\u0000' && LA30_160<='&')||(LA30_160>='(' && LA30_160<='\uFFFF')) ) {s = 161;}

                        if ( s>=0 ) return s;
                        break;
                    case 6 : 
                        int LA30_626 = input.LA(1);

                        s = -1;
                        if ( (LA30_626=='\'') ) {s = 281;}

                        else if ( ((LA30_626>='\u0000' && LA30_626<='&')||(LA30_626>='(' && LA30_626<='\uFFFF')) ) {s = 161;}

                        if ( s>=0 ) return s;
                        break;
                    case 7 : 
                        int LA30_43 = input.LA(1);

                        s = -1;
                        if ( (LA30_43=='\\') ) {s = 159;}

                        else if ( ((LA30_43>='\u0000' && LA30_43<='&')||(LA30_43>='(' && LA30_43<='[')||(LA30_43>=']' && LA30_43<='\uFFFF')) ) {s = 160;}

                        else if ( (LA30_43=='\'') ) {s = 161;}

                        if ( s>=0 ) return s;
                        break;
                    case 8 : 
                        int LA30_280 = input.LA(1);

                        s = -1;
                        if ( (LA30_280=='\'') ) {s = 281;}

                        else if ( ((LA30_280>='0' && LA30_280<='7')) ) {s = 388;}

                        else if ( ((LA30_280>='\u0000' && LA30_280<='&')||(LA30_280>='(' && LA30_280<='/')||(LA30_280>='8' && LA30_280<='\uFFFF')) ) {s = 161;}

                        if ( s>=0 ) return s;
                        break;
            }
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 30, _s, input);
            error(nvae);
            throw nvae;
        }
    }
 

}