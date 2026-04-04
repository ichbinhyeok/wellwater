package com.example.wellwater.pseo;

public enum PseoSearchRole {
    CORE(true, 0),
    SUPPORT(true, 1),
    HOLD(false, 2),
    CONVERSION(false, 3);

    private final boolean indexable;
    private final int sortRank;

    PseoSearchRole(boolean indexable, int sortRank) {
        this.indexable = indexable;
        this.sortRank = sortRank;
    }

    public boolean isIndexable() {
        return indexable;
    }

    public int sortRank() {
        return sortRank;
    }
}
