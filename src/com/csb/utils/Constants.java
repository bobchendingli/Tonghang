package com.csb.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author bobo
 *
 */
public class Constants {
	public static final String SD_FILE_MEETING = "/tonghang/meeting";
	public static final String SD_FILE_MYINFO = "/tonghang/myinfo";
    public static final List<String> PIC_LIST = new ArrayList<String>();
    static {
    	PIC_LIST.add(".jpg");
    	PIC_LIST.add(".jpeg");
    	PIC_LIST.add(".png");
    }
    
    public static final List<String> DOC_LIST = new ArrayList<String>();
    static {
    	DOC_LIST.add(".doc");
    	DOC_LIST.add(".docx");
    }
    
    public static final List<String> XlS_LIST = new ArrayList<String>();
    static {
    	XlS_LIST.add(".xls");
    	XlS_LIST.add(".xlsx");
    }
    
    public static final List<String> PPT_LIST = new ArrayList<String>();
    static {
    	PPT_LIST.add(".ppt");
    	PPT_LIST.add(".pptx");
    }
    
    public static final String PDF = ".pdf";
    public static final String TXT = ".txt";
}
