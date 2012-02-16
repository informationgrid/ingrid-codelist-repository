package de.ingrid.codelistHandler.util;

import java.util.List;

public class XmlCodeListUtils {

    public static String addClassInformation(String input) {
        // add container class
        String modString = "{\"codelist\":" + input + "}";
        
        // add classes that represents the entries
        modString = modString.replaceAll("\"entries\":",       "\"entries\":[{\"clEntry\":");
        modString = modString.replaceAll("\"localisations\":", "\"localisations\":[{\"entry\":");
        
        // handle arrays
        modString = modString.replaceAll("\\[\\[",            "[{\"string\":[");
        modString = modString.replaceAll("\\],\\[",           "]},{\"string\":[");
        modString = modString.replaceAll("\\]\\]}",           "]}]}]}");
        //modString = modString.replaceAll("\\]}\\]}\\]}\\]}}", "]}]}]}]}]}}");
        modString = modString.replaceAll("\\]}\\]}\\]}\\]",   "]}]}]}]}]");
        
        return modString;
    }
    
    public void writeToXml(String path, List<Object> data) {
        
    }
}
