package de.ingrid.codelistHandler.importer.priorityDataset.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemValueWrapper {
    public String id;

    public LangTextItem label;
}
