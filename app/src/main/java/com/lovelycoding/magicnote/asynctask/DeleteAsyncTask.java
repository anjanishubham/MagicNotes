package com.lovelycoding.magicnote.asynctask;

import android.os.AsyncTask;
import android.util.Log;

import com.lovelycoding.magicnote.models.Note;
import com.lovelycoding.magicnote.persistant.NoteDao;

public class DeleteAsyncTask extends AsyncTask<Note, Void, Void> {
    NoteDao mNoteDao;
    private static final String TAG = "InsertAsyncTask";
    public DeleteAsyncTask(NoteDao mNoteDao) {
        this.mNoteDao = mNoteDao;
    }

    @Override
    protected Void doInBackground(Note... notes) {

        mNoteDao.delete(notes);
        Log.d(TAG, "doInBackground: thread"+Thread.currentThread().getName());

        return null;
    }
}
