package com.lovelycoding.magicnote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.lovelycoding.magicnote.adapter.NotesRecyclerAdapter;
import com.lovelycoding.magicnote.models.Note;
import com.lovelycoding.magicnote.persistant.NoteRepository;
import com.lovelycoding.magicnote.util.VerticalSpaceItemDecorator;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        NotesRecyclerAdapter.OnNoteListener, FloatingActionButton.OnClickListener
{

    private static final String TAG = "MainActivity";
    //Ui component
    private RecyclerView mRecyclerView;
    private MaterialToolbar toolbar;
    private FloatingActionButton fab;

    //vars
    private ArrayList<Note> mNotes = new ArrayList<>();
    private NotesRecyclerAdapter mNoteRecyclerAdapter;
    private NoteRepository mNoteRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView=findViewById(R.id.rv_notes);
        fab=findViewById(R.id.fab);
        fab.setOnClickListener(this);
        mNoteRepository=new NoteRepository(this);
        initToolbar();
        mNotes.add(new Note("First note ","aldklhf","Aug 2019"));

        Log.d(TAG, "onCreate: thread"+Thread.currentThread().getName());
        initRecyclerView();
       // insertFakeNotes();
        retrieveNotes();
    }


    private void retrieveNotes() {
        mNoteRepository.retrieveNotesTask().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                if(mNotes.size()>0)
                {
                    mNotes.clear();
                }
                if (notes != null) {
                    mNotes.addAll(notes);
                }
                mNoteRecyclerAdapter.notifyDataSetChanged();
            }
        });
    }

    private void initToolbar() {
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Notes");
        toolbar.setTitleTextColor(Color.WHITE);
    }

    private void insertFakeNotes()
    {
        for(int i=1;i<1000;i++)
        {
            Note note=new Note();
            note.setTitle("Title"+i);
            note.setContent("content #"+i);
            note.setTimestamp("Aug 2019");
            mNotes.add(note);

        }
        mNoteRecyclerAdapter.notifyDataSetChanged();
    }


    private void initRecyclerView()
    {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        VerticalSpaceItemDecorator itemDecorator=new VerticalSpaceItemDecorator(5);
        mRecyclerView.addItemDecoration(itemDecorator);
        mNoteRecyclerAdapter=new NotesRecyclerAdapter(mNotes,this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(mRecyclerView);
        mRecyclerView.setAdapter(mNoteRecyclerAdapter);
    }

    @Override
    public void onNoteClick(int position) {

        Toast.makeText(this, "item clicked"+position, Toast.LENGTH_SHORT).show();
        Intent intent=new Intent(this,NoteActivity.class);
        intent.putExtra("selected_note",mNotes.get(position));
        startActivity(intent);

    }

    @Override
    public void onClick(View view)
    {

        Intent intent=new Intent(this,NoteActivity.class);
        startActivity(intent);

    }


    private void  deleteNote(Note note)
    {
        mNotes.remove(note);
        mNoteRecyclerAdapter.notifyDataSetChanged();
        mNoteRepository.deleteNote(note);
    }
    private ItemTouchHelper.SimpleCallback itemTouchHelperCallback=new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            deleteNote(mNotes.get(viewHolder.getAdapterPosition()));
        }
    };


}
