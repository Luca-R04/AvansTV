package com.avans.avanstv.Data;

import androidx.room.TypeConverter;

import com.avans.avanstv.Domain.Genre;
import com.avans.avanstv.Domain.Video;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Converters {
    //Global converters for ArrayList<String>
    @TypeConverter
    public static ArrayList<String> fromString(String value) {
        Type listType = new TypeToken<ArrayList<String>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromArrayList(ArrayList<String> list) {
        Gson gson = new Gson();
        return gson.toJson(list);
    }

    //Converters for video attribute in the Movie class
    @TypeConverter
    public static Video fromVideoString(String video) {
        return new Video(video);
    }

    @TypeConverter
    public static String fromVideo(Video video) {
        return video.getKey();
    }

    //Converters for List<Integer> for the genre_ids attribute in the Movie class
    @TypeConverter
    public static List<Integer> fromInteger(String values) {
        Type listType = new TypeToken<List<Integer>>() {}.getType();
        return new Gson().fromJson(values, listType);
    }

    @TypeConverter
    public static String fromIntegerArray(List<Integer> list) {
        Gson gson = new Gson();
        return gson.toJson(list);
    }

    //Converters for the Date type
    @TypeConverter
    public static Date fromDateString(String date) {
        return new Date(date);
    }

    @TypeConverter
    public static String fromDate(Date date) {
        return date.toString();
    }

    //Converters for the Genre class
    @TypeConverter
    public static Genre fromGenreString(String genre) {
        return new Genre(genre);
    }

    @TypeConverter
    public static String fromGenre(Genre genre) {
        return genre.getName();
    }
}
