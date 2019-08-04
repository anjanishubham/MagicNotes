package com.lovelycoding.magicnote.asynctask;

import android.os.AsyncTask;
import android.util.Log;

import com.lovelycoding.magicnote.models.Note;
import com.lovelycoding.magicnote.persistant.NoteDao;

public class UpdateAsyncTask extends AsyncTask<Note, Void, Void> {
    NoteDao mNoteDao;
    private static final String TAG = "InsertAsyncTask";
    public UpdateAsyncTask(NoteDao mNoteDao) {
        this.mNoteDao = mNoteDao;
    }

    @Override
    protected Void doInBackground(Note... notes) {

        mNoteDao.update(notes);
        Log.d(TAG, "doInBackground: thread"+Thread.currentThread().getName());

        return null;
    }
}
