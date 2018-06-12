package de.ingrid.codelistHandler.migrate;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thoughtworks.xstream.XStream;

import de.ingrid.codelists.CodeListService;
import de.ingrid.codelists.model.CodeList;

@Service
public class Migrator {
    
    private static Log log = LogFactory.getLog(Migrator.class);
    
    @Autowired
    private CodeListService codeListService;
    
    @SuppressWarnings("unchecked")
    public void run() {
        // since v4.4.0 we separated the codelists so that we need to read the codelist.xml file and store it
        // in separate files under a new directory
        File oldCodelistsFile = new File("data/codelists.xml");
        if (oldCodelistsFile.exists()) {
            FileReader codelistReader;
            try {
                codelistReader = new FileReader(oldCodelistsFile);
                XStream xStream = new XStream();
                List<CodeList> oldCodelists = (List<CodeList>) xStream.fromXML( codelistReader );
                codeListService.persistToAll( oldCodelists );
                codelistReader.close();

                log.info( "Successfully migrated old codelists");

                boolean deleteSuccess = oldCodelistsFile.delete();
                if (!deleteSuccess) {
                    log.warn("Couldn't delete file: " + oldCodelistsFile.getName());
                }
                
            } catch (FileNotFoundException e) {
                log.error( "Could not migrate old codelists", e );
            } catch (IOException e) {
                log.error( "Problem during migration of old codelists.", e );
            }
        }
    }
}