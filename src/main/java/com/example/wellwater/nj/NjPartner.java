package com.example.wellwater.nj;

import java.util.List;

public record NjPartner(
        String slug,
        String name,
        String type,
        boolean active,
        String verificationStatus,
        String bookingUrl,
        List<String> supportedCounties,
        String disclosure
) {
    public NjPartner {
        supportedCounties = supportedCounties == null ? List.of() : List.copyOf(supportedCounties);
    }
}
