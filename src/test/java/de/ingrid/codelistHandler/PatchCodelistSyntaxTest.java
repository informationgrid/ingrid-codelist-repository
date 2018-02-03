package de.ingrid.codelistHandler;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import de.ingrid.codelistHandler.model.CodeListEntryUpdate;
import de.ingrid.codelistHandler.model.CodeListUpdate;
import de.ingrid.codelists.model.CodeListEntry;
import de.ingrid.codelists.persistency.XmlCodeListPersistency;

public class PatchCodelistSyntaxTest {

    @Test
    public void test() {

        try {

            List<String> filesForUpdate = getPatchFiles();

            for (String file : filesForUpdate) {
                XmlCodeListPersistency<CodeListUpdate> xml = new XmlCodeListPersistency<CodeListUpdate>();
                xml.setPathToXml( file );
                List<CodeListUpdate> updateCodelists = xml.read();

                for (CodeListUpdate codeList : updateCodelists) {
                    switch (codeList.getType()) {
                    case ADD:
                    case UPDATE:
                        for (CodeListEntry cle : codeList.getCodelist().getEntries()) {
                            String data = cle.getData();
                            if (data != null && data.startsWith( "{" ) && data.endsWith( "}" )) {
                                try {
                                    new JSONObject( data );
                                } catch (JSONException e) {
                                    Assert.fail( "Error in '" + file + "'JSON data field: " + data );
                                }
                            }
                        }
                        break;
                    case ENTRYUPDATE:

                        String data;
                        for (CodeListEntryUpdate entry : codeList.getEntries()) {
                            switch (entry.getType()) {
                            case ADD:
                            case UPDATE:
                                data = entry.getEntry().getData();
                                if (data != null && data.startsWith( "{" ) && data.endsWith( "}" )) {
                                    try {
                                        new JSONObject( data );
                                    } catch (JSONException e) {
                                        Assert.fail( "Error in '" + file + "'JSON data field: " + data );
                                    }
                                }
                                break;
                            default:
                                break;
                            }
                        }
                    default:
                        break;
                    }
                }
            }
        } catch (Exception e) {
            Assert.fail( "Error validating Codelist patches: " + e );
        }

    }

    private List<String> getPatchFiles() throws Exception {
        List<String> resList = new ArrayList<String>();

        ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resourceResolver.getResources( "file:src/main/release/patches/*.xml" );
        for (Resource resource : resources) {
            if (resource.exists()) {
                resList.add( resource.getFile().getPath() );
            }
        }
        return resList;
    }

}
