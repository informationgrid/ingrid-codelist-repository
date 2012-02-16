package de.ingrid.codelistHandler.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import de.ingrid.codelistHandler.model.CodeList;

public class CodeListUtils {

    public static String SORT_INCREMENT = "inc";
    
    public static String SORT_DECREMENT = "dec";
    
    public static List<CodeList> sortCodeList(List<CodeList> list, final String what, final String how) {
        Collections.sort(list, new Comparator<CodeList>() {

            @Override
            public int compare(CodeList o1, CodeList o2) {
                int res = 0;
                
                if ("name".equals(what)) {
                    res = o1.getName().compareTo(o2.getName());
                } else if ("id".equals(what)) {
                    res = o1.getId().compareTo(o2.getId());
                }
                return (how.equals(SORT_INCREMENT) ? res : res*-1);
            }
        });
        return list;
    }
}
