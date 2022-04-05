package com.avans.avanstv.Domain;

import java.util.List;

public class CastResponse {
    private final List<Cast> cast;

    public CastResponse(List<Cast> cast) {
        this.cast = cast;
    }

    public List<Cast> getCast() {
        return cast;
    }
}
