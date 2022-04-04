package com.avans.avanstv.Data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.avans.avanstv.Domain.Genre;
import com.avans.avanstv.Domain.Movie;

@TypeConverters({Converters.class})
@Database(entities = {Movie.class, Genre.class}, version = 5, exportSchema = false)
public abstract class MovieRoomDatabase extends RoomDatabase {
    public abstract MovieDao movieDao();
    public abstract GenreDao genreDao();
    private static MovieRoomDatabase instance;

    public static MovieRoomDatabase getDatabase(final Context context) {
        if (instance == null) {
            synchronized (MovieRoomDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                            MovieRoomDatabase.class, "movie_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return instance;
    }
}
