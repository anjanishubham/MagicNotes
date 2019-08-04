package com.lovelycoding.magicnote.persistant;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.lovelycoding.magicnote.models.Note;

@Database(entities = {Note.class}, version = 1)

public abstract class NoteDatabase extends RoomDatabase {
    public static final String DATABASE_NAME = "note_db";
    public static NoteDatabase instance;
    static NoteDatabase getInstance(final Context context) {
        if (instance == null) {
            instance= Room.databaseBuilder(context.getApplicationContext(),
                    NoteDatabase.class,
                    DATABASE_NAME).build();
        }
        return instance;
    }

    public abstract NoteDao getNoteDao();

}
