package com.example.wellwater.nj;

public record NjLocation(
        double longitude,
        double latitude,
        String municipality,
        String county,
        boolean inNewJersey
) {
}
