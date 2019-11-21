package de.ingrid.codelistHandler.importer.inspireRegistry.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MetadataCodelist {

    public List<Item> containeditems;
}
