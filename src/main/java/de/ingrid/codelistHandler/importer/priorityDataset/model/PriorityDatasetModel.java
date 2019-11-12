package de.ingrid.codelistHandler.importer.priorityDataset.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PriorityDatasetModel {

    @JsonProperty("metadata-codelist")
    public MetadataCodelist metadataCodelist;

    public List<Item> getItems() {
        return this.metadataCodelist.containeditems;
    }
}
