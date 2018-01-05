/*
 * **************************************************-
 * InGrid CodeList Repository
 * ==================================================
 * Copyright (C) 2014 - 2018 wemove digital solutions GmbH
 * ==================================================
 * Licensed under the EUPL, Version 1.1 or – as soon they will be
 * approved by the European Commission - subsequent versions of the
 * EUPL (the "Licence");
 * 
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 * 
 * http://ec.europa.eu/idabc/eupl5
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and
 * limitations under the Licence.
 * **************************************************#
 */
package de.ingrid.codelistHandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.json.JsonHierarchicalStreamDriver;
import com.thoughtworks.xstream.io.json.JsonWriter;

import de.ingrid.codelistHandler.model.CodeListEntryUpdate;
import de.ingrid.codelistHandler.model.CodeListUpdate;
import de.ingrid.codelists.CodeListService;
import de.ingrid.codelists.model.CodeList;
import de.ingrid.codelists.model.CodeListEntry;
import de.ingrid.codelists.persistency.XmlCodeListPersistency;
import de.ingrid.codelists.util.CodeListUtils;
import de.ingrid.codelists.util.VersionUtils;

@Component
public class CodeListManager {
    
    private static String PATH_CODELIST_UPDATES = "file:data/patches/*.xml";

    private static Logger log = Logger.getLogger( CodeListManager.class );
    
    @Autowired
    private CodeListService codeListService;
    
    private List<CodeList> initialCodelists;
    
    @Autowired
    public CodeListManager(CodeListService cls) {
        this.codeListService = cls;
        this.initialCodelists = this.codeListService.getInitialCodelists();
        try {
            checkForUpdates();
        } catch (IOException e) {
            log.error( "Error when checking for updated codelist information", e );
        }
        
        // set all codelists to the current timestamp to force an update of all clients
        if ("true".equals( System.getenv("forceUpdateCodelists") )) {
            List<CodeList> codeLists = getCodeLists();
            for (CodeList codeList : codeLists) {
                codeList.setLastModified( System.currentTimeMillis() );
            }
            writeCodeListsToFile();
        }
    }
    
    public CodeList getCodeList(String id) {
        return codeListService.getCodeList(id);
    }
    
    public boolean updateCodeList(String id, String data) {
        
        codeListService.setCodelist(id, data);
        return writeCodeListsToFile();
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
    
    public CodeListEntry getCodeListEntry(String listId, String entryId) {
        CodeList cl = getCodeList(listId);
        if (cl == null) return null;
        
        for (CodeListEntry entry : cl.getEntries()) {
            if (entry.getId().equals( entryId )) return entry;
        }
        return null;
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
    
    public Object getCodeListAsShortJson(String sortField, String sortMethod) {
        String json = "[";
        for (CodeList codelist : CodeListUtils.sortCodeList(getCodeLists(), sortField, sortMethod)) {
            json += "{id:\""+codelist.getId()+"\",name:\""+codelist.getName()+"\"},";
        }
        json = json.substring(0, json.length()-1) + "]";
        return json;
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
    
    public Object getFilteredCodeListsAsShortJson(String name) {
        String search = name.substring(0, name.length()-1).toLowerCase();
        String json = "[";
        
        for (CodeList codelist : getCodeLists()) {
            if (codelist.getName() != null && codelist.getName().toLowerCase().startsWith(search))
                json += "{id:\""+codelist.getId()+"\",name:\""+codelist.getName()+"\"},";
        }
        json = json.substring(0, json.length()-1) + "]";
        return json;
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

    
    public Object findEntry(String name) {
        List<String[]> result = new ArrayList<String[]>();
        for (CodeList cl : getCodeLists()) {
            for (CodeListEntry entry : cl.getEntries()) {
                for (String lang : entry.getLocalisations().keySet()) {
                    if (entry.getLocalisations().get(lang).toLowerCase().contains(name)) {
                        String[] value = {cl.getId(), entry.getId(), lang};
                        result.add(value);
                    }
                }
            }
        }
        return createJSON(result);
    }

    public Object checkChangedInitialCodelist() {
        String json = "[{";
        String missing = "";
        List<CodeList> codelists = getCodeLists();
        
        for (CodeList codelist : this.initialCodelists) {
            if (!CodeListUtils.codelistExists(codelists, codelist.getId())) {
                missing += "{id:\""+codelist.getId()+"\",name:\""+codelist.getName()+"\"},";
            }
        }
        if (!missing.equals("")) json += "missing:[" + missing.substring(0, missing.length()-1) + "]";
        json += "}]";
        return json;
    }

    /**
     * @param id
     */
    public boolean addCodelistFromInitial(String id) {
        boolean success = false;
        for (CodeList codelist : this.initialCodelists) {
            if (codelist.getId().equals(id)) {
                codeListService.setCodelist(id, codelist);
                writeCodeListsToFile();
                success = true;
                break;
            }
        }
        return success;
    }
    
    public boolean updateCodelistsFromUpdateFile( String filePath ) {
        XmlCodeListPersistency<CodeListUpdate> xml = new XmlCodeListPersistency<CodeListUpdate>();
        xml.setPathToXml( filePath );
        List<CodeListUpdate> updateCodelists = xml.read();
        
        for (CodeListUpdate codeList : updateCodelists) {
            switch(codeList.getType()) {
            case ADD:
            case UPDATE:
                codeListService.setCodelist( codeList.getId(), codeList.getCodelist() );
                writeCodeListsToFile();
                log.info( "Added/Updated codelist: " + codeList.getId() );
                break;
            case REMOVE:
                removeCodeList( codeList.getId() );
                log.info( "Removed codelist: " + codeList.getId() );
                break;
            case ENTRYUPDATE:
                CodeList cl = getCodeList( codeList.getId() );
                if (cl == null) {
                	throw new RuntimeException("Codelist could not be found for update: " + codeList.getId());
                }
                for (CodeListEntryUpdate entry : codeList.getEntries()) {
                    switch(entry.getType()) {
                    case ADD:
                        cl.addEntry( entry.getEntry() );
                        log.info( "Added codelist entry: " + entry.getEntry().getId() + " from list: " + codeList.getId() );
                        break;
                    case REMOVE:
                        cl.removeEntry( entry.getEntry().getId() );
                        log.info( "Removed codelist entry: " + entry.getEntry().getId() + " from list: " + codeList.getId() );
                        break;
                    case UPDATE:
                        cl.removeEntry( entry.getEntry().getId() );
                        cl.addEntry( entry.getEntry() );
                        log.info( "Updated codelist entry: " + entry.getEntry().getId() + " from list: " + codeList.getId() );
                        break;
                    default:
                        break;
                    }
                }
                codeListService.setCodelist( cl.getId(), cl );
                writeCodeListsToFile();
                break;
            default:
                log.error( "Type not supported for updated codelist: " + codeList.getType() );
                break;
            }
        }
        
        return true;
    }

    public List<String> checkFilesForUpdate(String version) {
        List<String> resList = new ArrayList<String>();
        
        ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
        try {
            Resource[] resources = resourceResolver.getResources(PATH_CODELIST_UPDATES);
            for (Resource resource : resources) {
                if (resource.exists()) {
                    String filename = resource.getFilename();
                    String fileVersion = filename.substring( 0, filename.indexOf( '_' ) );
                    if (fileVersion.compareToIgnoreCase( version ) > 0 ) {
                        resList.add( resource.getFile().getPath() );
                    }
                }
            }
        } catch (FileNotFoundException e) {
            log.warn( "No changes dir found in classpath, where codelist updates are searched: " + PATH_CODELIST_UPDATES );
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resList;
    }
    
    public void checkForUpdates() throws IOException {
        List<String> filesForUpdate = checkFilesForUpdate( VersionUtils.getCurrentVersion() );
        Collections.sort( filesForUpdate );
        String newVersion = null;
        for (String file : filesForUpdate) {
            updateCodelistsFromUpdateFile( file );
            String fileName = new File( file ).getName();
            newVersion = fileName.substring( 0, fileName.indexOf( '_' ) );
            
            // rename update file so that it's not executed again
            Files.move( Paths.get( file ), Paths.get( file + ".bak" ), StandardCopyOption.REPLACE_EXISTING );
        }
        
        if (newVersion != null) {
            VersionUtils.writeVersionInfo( newVersion );
        }
    }
    
}
