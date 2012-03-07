package de.ingrid.codelistHandler;

import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.json.JsonHierarchicalStreamDriver;
import com.thoughtworks.xstream.io.json.JsonWriter;

import de.ingrid.codelists.CodeListService;
import de.ingrid.codelists.model.CodeList;
import de.ingrid.codelists.util.CodeListUtils;

@Component
public class CodeListManager {
    
    @Autowired
    private CodeListService codeListService;
    
    public CodeListManager() {}
    
    public CodeList getCodeList(String id) {
        return codeListService.getCodeList(id);
    }
    
    public boolean updateCodeList(String id, String data) {
        
        codeListService.setCodelist(id, data);
        
        return true;
    }
    
    public boolean removeCodeList(String id) {
        CodeList cl = getCodeList(id);
        if (cl == null) {
            return false;
        } else {
            getCodeLists().remove(cl);
            writeCodeListsToFile();
        }
        return true;
    }
    
    public List<CodeList> getCodeLists() {
        return codeListService.getCodeLists();
    }

    public boolean writeCodeListsToFile() {
        return codeListService.persistToAll();
    }

    public Object getCodeListAsJson(String id) {
        return createJSON(getCodeList(id));
    }    
    
    public Object getCodeListsAsJson(String sortField, String lastModified, String sortMethod) {
        List<CodeList> cls = null;
        
        // only get those codelists that have changed after lastModified
        if (lastModified != null) {
            cls = new ArrayList<CodeList>();
            for (CodeList codeList : getCodeLists()) {
                if (codeList.getLastModified() > Long.valueOf(lastModified)) {
                    cls.add(codeList);
                }
            };
        } else {
            cls = getCodeLists();
        }
        return createJSON(CodeListUtils.sortCodeList(cls, sortField, sortMethod));
    }
    

    public Object getFilteredCodeListsAsJson(String name) {
        String search = name.substring(0, name.length()-1).toLowerCase();
        
        List<CodeList> filteredCLs = new ArrayList<CodeList>();
        
        for (CodeList cl : getCodeLists()) {
            if (cl.getName() != null && cl.getName().toLowerCase().startsWith(search))
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

    public void setCodeListService(CodeListService codeListService) {
        this.codeListService = codeListService;
    }

}
