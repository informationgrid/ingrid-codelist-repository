package de.ingrid.codelistHandler.importer.priorityDataset.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MetadataCodelist {

    public List<Item> containeditems;
}
