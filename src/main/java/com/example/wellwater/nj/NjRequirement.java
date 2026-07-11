package com.example.wellwater.nj;

public record NjRequirement(
        String name,
        String scopeNote,
        boolean countySpecific
) {
}
