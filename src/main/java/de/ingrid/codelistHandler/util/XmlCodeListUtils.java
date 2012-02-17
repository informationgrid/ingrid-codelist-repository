package de.ingrid.codelistHandler.util;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import de.ingrid.codelistHandler.model.CodeList;
import de.ingrid.codelistHandler.model.CodeListEntry;

public class XmlCodeListUtils {

    public void writeToXml(String path, List<Object> data) {
        
    }
    
    public static CodeList getCodeListFromJsonGeneric(String data) {
        CodeList cl = new CodeList();
        
        try {
            JSONObject jo = new JSONObject(data);
            cl.setId(jo.getString("id"));
            cl.setName(jo.getString("name"));
            cl.setDescription(jo.getString("description"));
            cl.setDefaultEntry(jo.optString("defaultEntry", ""));
            cl.setLastModified(jo.getLong("lastModified"));
            
            List<CodeListEntry> entries = new ArrayList<CodeListEntry>();
            JSONArray jsonEntriesArray = jo.getJSONArray("entries");
            for (int i=0; i<jsonEntriesArray.length(); i++) {
                CodeListEntry cle = new CodeListEntry();
                JSONObject jsonEntryObject = jsonEntriesArray.getJSONObject(i);
                cle.setId(jsonEntryObject.getString("id"));
                JSONArray jsonLocalisationsArray = jsonEntryObject.getJSONArray("localisations"); 
                for (int j=0; j<jsonLocalisationsArray.length(); j++) {
                    cle.setLocalisedEntry(
                            jsonLocalisationsArray.getJSONArray(j).getString(0),
                            jsonLocalisationsArray.getJSONArray(j).getString(1)
                    );
                }
                entries.add(cle);
            }
            
            cl.setEntries(entries);
            
        } catch (JSONException e) {
            e.printStackTrace();
            cl = null;
        }
        
        return cl;
    }
}
