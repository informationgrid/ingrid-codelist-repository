package de.ingrid.codelistHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.mortbay.log.Log;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.json.JsonHierarchicalStreamDriver;
import com.thoughtworks.xstream.io.json.JsonWriter;

import de.ingrid.codelistHandler.model.CodeList;
import de.ingrid.codelistHandler.util.CodeListUtils;
import de.ingrid.codelistHandler.util.XmlCodeListUtils;

public class CodeListManager {
    
    private static String DEFAULT_CODELIST_PATH = "data/CodeListsData.xml";
    
    private List<CodeList> codeLists;
    
    private static CodeListManager manager;
    
    
    private CodeListManager() {
        // try to load code lists initally
        codeLists = readCodeListsFromFile();
    }
    
    public static CodeListManager getInstance() {
        if (manager == null) {
            manager = new CodeListManager();
        }
        return manager;
    }

    public CodeList getCodeList(String id) {
        for (CodeList cl : codeLists) {
            if (cl.getId().equals(id)) {
                return cl;
            }
        }
        return null;
    }
    
    public boolean updateCodeList(String id, String data) {
        
        CodeList cl = XmlCodeListUtils.getCodeListFromJsonGeneric(data);
        
        // add modification date
        cl.setLastModified(System.currentTimeMillis());
        
        // remove old codelist if it exists
        CodeList oldCl = getCodeList(cl.getId());
        if (oldCl != null)
            codeLists.remove(oldCl);
        
        cl.setId(id);
        codeLists.add(cl);
        
        // make persistent in file!
        writeCodeListsToFile();
        
        return true;
    }
    
    public boolean removeCodeList(String id) {
        CodeList cl = getCodeList(id);
        if (cl == null) {
            return false;
        } else {
            codeLists.remove(cl);
            writeCodeListsToFile();
        }
        return true;
    }
    
    public List<CodeList> getCodeLists() {
        return codeLists;
    }

    public List<CodeList> readCodeListsFromFile() {
        return readCodeListsFromFile(null);
    }
    
    @SuppressWarnings("unchecked")
    public List<CodeList> readCodeListsFromFile(String path) {
        XStream xStream = new XStream();
        String finalPath = path;
        try {
            if (finalPath == null)
                finalPath = DEFAULT_CODELIST_PATH;
            
            codeLists = (List<CodeList>) xStream.fromXML(new FileInputStream(finalPath));
        } catch (FileNotFoundException e) {
            Log.warn("CodeList-Repository does not exist! Creating new one: '" + finalPath + "'");
         // create a new empty list
            codeLists = new ArrayList<CodeList>();
        }
        
        return codeLists;
    }
    
    public boolean writeCodeListsToFile() {
        return writeCodeListsToFile(null);
    }
    
    public boolean writeCodeListsToFile(String path) {
        XStream xStream = new XStream();
        String finalPath = path;
        if (finalPath == null)
            finalPath = DEFAULT_CODELIST_PATH;
        
        try {
            createDataDir();
            FileOutputStream out = new FileOutputStream(finalPath);
            xStream.toXML(codeLists, out);
            
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
        
        return true;
    }
    
    private void createDataDir() throws IOException {
        File f = new File("data");
        if (!f.exists() || !f.isDirectory()) {
            f.mkdir();
        }
    }

    public Object getCodeListAsJson(String id) {
        return createJSON(getCodeList(id));
    }    
    
    public Object getCodeListsAsJson(String sortField, String sortMethod) {
        
        return createJSON(CodeListUtils.sortCodeList(getCodeLists(), sortField, sortMethod));
    }
    

    public Object getFilteredCodeListsAsJson(String name) {
        String search = name.substring(0, name.length()-1).toLowerCase();
        
        List<CodeList> filteredCLs = new ArrayList<CodeList>();
        
        for (CodeList cl : this.codeLists) {
            if (cl.getName().toLowerCase().startsWith(search))
                filteredCLs.add(cl);
        }
        return createJSON(filteredCLs);
    }
    
    private Object createJSON(Object obj) {
        XStream xstream = new XStream(new JsonHierarchicalStreamDriver() {
            @Override
            public HierarchicalStreamWriter createWriter(Writer writer) {
                return new JsonWriter(writer, JsonWriter.DROP_ROOT_MODE);
            }
        });
        
        //XStream xstream = new XStream(new JettisonMappedXmlDriver());
        //xstream.setMode(XStream.NO_REFERENCES);
        return xstream.toXML(obj);
    }
}
