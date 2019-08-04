package com.lovelycoding.magicnote.persistant;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.lovelycoding.magicnote.asynctask.DeleteAsyncTask;
import com.lovelycoding.magicnote.asynctask.InsertAsyncTask;
import com.lovelycoding.magicnote.asynctask.UpdateAsyncTask;
import com.lovelycoding.magicnote.models.Note;

import java.util.List;

public class NoteRepository {
    private NoteDatabase mNoteDatabase;

    public NoteRepository(Context context) {
        mNoteDatabase = NoteDatabase.getInstance(context);

    }
    public void insertNoteTask(Note note) {

        new InsertAsyncTask(mNoteDatabase.getNoteDao()).execute(note);
    }

    public void updateNote(Note note) {

        new UpdateAsyncTask(mNoteDatabase.getNoteDao()).execute(note);
    }

    public void deleteNote(Note note)
    {
        new DeleteAsyncTask(mNoteDatabase.getNoteDao()).execute(note);

    }

    public LiveData<List<Note>> retrieveNotesTask() {

        return mNoteDatabase.getNoteDao().getNotes();
    }
}
