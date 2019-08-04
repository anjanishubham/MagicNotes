package com.lovelycoding.magicnote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lovelycoding.magicnote.models.Note;
import com.lovelycoding.magicnote.persistant.NoteRepository;
import com.lovelycoding.magicnote.util.Utility;

public class NoteActivity extends AppCompatActivity implements
        View.OnTouchListener,
        GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener, View.OnClickListener {

    private static final String TAG = "NoteActivity";
    private static final int EDIT_MODE_ENABLE = 1;
    private static final int EDIT_MODE_DISABLE = 0;

    // Ui component
    private LinedEditText mNoteContentEditText;
    private EditText mToolbarNoteEditTitle;
    private TextView mToolbarNoteTitle;
    private RelativeLayout mCheckBoxContainer, mBackButtonContainer;
    private AppCompatImageButton mBackButton, mCheckButton;

    // var
    private boolean mIsNewNote;
    Note mInitialNote;
    Note mFinalNote;
    private GestureDetector mGestureDetector;
    private int mMode;
    private NoteRepository mNoteRepository;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        mNoteContentEditText = findViewById(R.id.et_note_content);
        mToolbarNoteEditTitle = findViewById(R.id.toolbar_note_edit_title);
        mToolbarNoteTitle = findViewById(R.id.toolbar_note_title);
        mCheckBoxContainer = findViewById(R.id.toolbar_check_container);
        mBackButtonContainer = findViewById(R.id.toolbar_back_arrow_container);
        mBackButton = findViewById(R.id.toolbar_back_arrow);
        mCheckButton = findViewById(R.id.toolbar_check);

        mNoteRepository=new NoteRepository(this);

        if (getIncomingIntent()) {
            setNewNoteProperties();
            enableEditMode();
        } else {
            setNoteProperties();
            disableEditMode();
            disableContentInteraction();// this for Note content disable while open into detail mode
        }


        setListener();

    }


    private void updateNote() {
        mNoteRepository.updateNote(mFinalNote);
    }

    private void saveChanges() {
        if (mIsNewNote) {
                saveNewNote();
        } else {
                    updateNote(); // if note is open in edit mode here call for update method
        }

    }

    private void saveNewNote() {

        mNoteRepository.insertNoteTask(mFinalNote);
    }
    private void hideSoftKeyboard() {
        InputMethodManager im=(InputMethodManager)this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view=this.getCurrentFocus();
        if (view == null) {
            view=new View(this);
        }
        im.hideSoftInputFromWindow(view.getWindowToken(),0);
    }

    //Enable edit mode
    private void enableEditMode() {
        mCheckBoxContainer.setVisibility(View.VISIBLE);
        mBackButtonContainer.setVisibility(View.GONE);
        mToolbarNoteEditTitle.setVisibility(View.VISIBLE);
        mToolbarNoteTitle.setVisibility(View.GONE);
        // mNoteContentEditText.setFocusable(true);
       // mToolbarNoteEditTitle.setText(mToolbarNoteTitle.getText());



        mMode = EDIT_MODE_ENABLE;
        enableContentInteraction();

    }

    // disable mode Note detail page
    private void disableEditMode() {
        mCheckBoxContainer.setVisibility(View.GONE);
        mBackButtonContainer.setVisibility(View.VISIBLE);
        mToolbarNoteEditTitle.setVisibility(View.GONE);
        mToolbarNoteTitle.setVisibility(View.VISIBLE);

        mToolbarNoteTitle.setText(mToolbarNoteEditTitle.getText());
        mMode = EDIT_MODE_DISABLE;
        disableContentInteraction();

        String temp=mNoteContentEditText.getText().toString();
        temp=temp.replace("\n","");
        temp = temp.replace(" ", "");
        if(temp.length()>0)
        {
            mFinalNote.setTitle(mToolbarNoteEditTitle.getText().toString());
            mFinalNote.setContent(mNoteContentEditText.getText().toString());
            mFinalNote.setTimestamp(Utility.getCurrentDateFormate());
            if(!mFinalNote.getContent().equals(mInitialNote.getContent())||
            !mFinalNote.getTitle().equals(mInitialNote.getTitle()))
            {
                saveChanges();
                Log.d(TAG, "disableEditMode:  save changing method ");

            }

        }


    }

    private void setListener() {
        mNoteContentEditText.setOnTouchListener(this);
        mGestureDetector = new GestureDetector(this, this);
        mCheckButton.setOnClickListener(this);
        mToolbarNoteTitle.setOnClickListener(this);
        mBackButton.setOnClickListener(this);

    }

    private void setNewNoteProperties()
    {

        mInitialNote=new Note();
        mFinalNote = new Note();
        mInitialNote.setTitle("");
        mFinalNote.setTitle("");

       // enableEditMode();


     /*   mToolbarNoteTitle.setText("");
        mToolbarNoteEditTitle.setText("");
        mNoteContentEditText.setText("");*/
    }

    // Setting note detail like title and description
    private void setNoteProperties() {
        mToolbarNoteTitle.setText(mInitialNote.getTitle());
        mToolbarNoteEditTitle.setText(mInitialNote.getTitle());
        mNoteContentEditText.setText(mInitialNote.getContent());
        // mNoteContentEditText.setFocusable(false);

    }

    private boolean getIncomingIntent() {
        if (getIntent().hasExtra("selected_note")) {
            mInitialNote = getIntent().getParcelableExtra("selected_note");

            mFinalNote=new Note();
            mFinalNote.setId(mInitialNote.getId());
            mFinalNote.setTitle(mInitialNote.getTitle());
            mFinalNote.setContent(mInitialNote.getContent());
            mFinalNote.setTimestamp(mInitialNote.getTimestamp());
            Log.d(TAG, "getIncomingIntent: "+mInitialNote.toString());
            mIsNewNote = false;
            return false;
        }
        mIsNewNote = true;
        return true;

    }

    private void enableContentInteraction() {
        mNoteContentEditText.setKeyListener(new EditText(this).getKeyListener());
        mNoteContentEditText.setFocusable(true);
        mNoteContentEditText.setFocusableInTouchMode(true);
        mNoteContentEditText.setCursorVisible(true);
        mNoteContentEditText.requestFocus();


    }

    private void disableContentInteraction() {
        mNoteContentEditText.setKeyListener(null);
        mNoteContentEditText.setFocusable(false);
        mNoteContentEditText.setFocusableInTouchMode(false);
        mNoteContentEditText.setCursorVisible(false);
        mNoteContentEditText.clearFocus();


    }




    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return mGestureDetector.onTouchEvent(motionEvent);
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent motionEvent) {
        enableEditMode();
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent motionEvent) {
        Log.d(TAG, "onDoubleTapEvent: ");
        return false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toolbar_check: {
                hideSoftKeyboard();
                disableEditMode();

                break;
            }
            case R.id.toolbar_note_title: {
                enableEditMode();
                break;
            }
            case R.id.toolbar_back_arrow: {
                finish();
            }
        }

    }

    @Override
    public void onBackPressed() {
        if (mMode == EDIT_MODE_ENABLE) {
            onClick(mCheckButton);
        } else
            super.onBackPressed();
    }


    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mMode=savedInstanceState.getInt("mode");
        if(mMode==EDIT_MODE_ENABLE)
        {
            enableEditMode();
        }

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("mode",mMode);
    }
}
