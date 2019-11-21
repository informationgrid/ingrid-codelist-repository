package de.ingrid.codelistHandler.importer.inspireRegistry.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemValueWrapper {
    public String id;

    public LangTextItem label;

    @JsonProperty("metadata-codelist")
    public ItemValueWrapper metadataCodelist;
}
