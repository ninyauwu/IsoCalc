package com.dl630.isocalc.core.storage;

public abstract class VersionedData {
    protected final String version;
    public VersionedData(String version) {
        this.version = version;
    }
}
