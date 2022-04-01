package com.avans.avanstv.Domain;

import java.util.List;

public class VideoResponse {
    private final List<Video> results;

    public VideoResponse(List<Video> results) {
        this.results = results;
    }

    public List<Video> getVideos() {
        return results;
    }
}
