package com.example.wellwater.nj;

import java.util.Optional;

public interface NjGeocodingService {

    Optional<NjLocation> locate(String address);
}
