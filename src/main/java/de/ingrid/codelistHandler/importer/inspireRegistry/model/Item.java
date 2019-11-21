package de.ingrid.codelistHandler.importer.inspireRegistry.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Item {
    public ItemValueWrapper value;
}
