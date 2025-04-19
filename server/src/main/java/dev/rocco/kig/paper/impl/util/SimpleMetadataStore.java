package dev.rocco.kig.paper.impl.util;

import org.bukkit.metadata.MetadataStoreBase;

public class SimpleMetadataStore extends MetadataStoreBase<Object> {
    @Override
    protected String disambiguate(Object subject, String metadataKey) {
        return metadataKey;
    }
}
