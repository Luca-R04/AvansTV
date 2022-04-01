package com.avans.avanstv.Domain;

import java.util.Date;

public class Video {
    private String iso_639_1;
    private String iso_3166_1;
    private String name;
    private String key;
    private String site;
    private int size;
    private String type;
    private boolean official;
    private Date published_at;
    private String id;

    public String getKey() {
        return key;
    }
}
