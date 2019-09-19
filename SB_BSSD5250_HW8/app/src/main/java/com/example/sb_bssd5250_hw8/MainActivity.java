package com.example.sb_bssd5250_hw8;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity  implements NotesData.NotesDataUpdatedListener {

    private RecyclerView notesRV;
    private NotesAdapter notesAdapter;
    private int i = 1;
    private static String LOGID = "MainActivity";

    @Override
    public void updateNotesDependents() {
        NotesAdapter notesAdapter = new NotesAdapter();
        notesAdapter.setmContext(this);
        notesRV.swapAdapter(notesAdapter, true);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState)	{
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        // setup an array of notes
        ArrayList<Note> notesArray = NotesData.getInstance(this).getNoteList();

        NotesData.getInstance(this).setListener(this);

        notesRV	= new RecyclerView(  this);
        notesRV.setBackgroundColor(Color.RED);

        notesAdapter = new NotesAdapter();
        notesAdapter.setmContext(this);
        notesRV.setAdapter(notesAdapter);
        notesRV.setLayoutManager(new LinearLayoutManager( this));

        Button addNoteButton = new Button( this);
        addNoteButton.setText("+");
        addNoteButton.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,  ViewGroup.LayoutParams.WRAP_CONTENT));
        //make this button add a new note when clicked
        addNoteButton.setOnClickListener(addClickedListener);

        Button saveNoteButton = new Button( this);
        saveNoteButton.setText("Save");
        saveNoteButton.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,  ViewGroup.LayoutParams.WRAP_CONTENT));
        //make this button save a new note when clicked
        saveNoteButton.setOnClickListener(saveClickedListener);

        // got this from the instructor
        LinearLayout linearLayout = new LinearLayout( this);
        LinearLayout buttonLL =  new  LinearLayout(this);
        buttonLL.setOrientation(LinearLayout.HORIZONTAL);
        buttonLL.addView(addNoteButton);
        buttonLL.addView(saveNoteButton);

        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,  ViewGroup.LayoutParams.MATCH_PARENT));
        linearLayout.addView(buttonLL);
        linearLayout.addView(notesRV);

        setContentView(linearLayout);

    }

    private View.OnClickListener addClickedListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.d("addClick Listener", "add clicked");
            Note note = new Note();
            note.setName("Name");
            note.setDate("Date");
            note.setDesc("Description");
            NotesData.getInstance(MainActivity.this).getNoteList().add(note);
            NotesData.getInstance(null).refreshNotes();


        }
    };
    private View.OnClickListener saveClickedListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.d("saveClick Listener", "add clicked");
            makeData();
            //writeDataFile();
            NoteJSONSerializer noteJSONSerializer = new NoteJSONSerializer( MainActivity.this, "notes.json" );
            try {
                noteJSONSerializer.saveNotes (NotesData.getInstance(MainActivity.this).getNoteList());
            }  catch (Exception e) {
                Log.d(LOGID, e.toString());
            }
            readDataFile("notes json");


        }
    };

    private void makeData() {
        ArrayList<Note> notes = NotesData.getInstance(this).getNoteList();
        for(int i=0;  i<10;  i++) {
            Note note = new Note();
            note.setName("Note	#" + i);
            note.setDesc(String.valueOf(View.generateViewId()));
            notes.add(note);
        }
    }


    private void writeDataFile() {
        ArrayList<Note> notes = NotesData.getInstance(this).getNoteList();
        String filename = "notes.txt";
        File file = new File(getApplicationContext().getFilesDir(), filename);
        FileOutputStream fileOutputStream;
        try {    //try to open the  file for writing
            fileOutputStream = new FileOutputStream(file);
            //Turn the first note's name to bytes   in utf8 format and put in file
            fileOutputStream.write(notes.get(0).toString().getBytes("UTF-8"));
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (Exception e) {
            //catch any errors that occur from try and throw them back to whoever called this
            Log.d(LOGID, e.toString());
        }
    }

    private void readDataFile(String filename) {

        File file = new File(getApplicationContext().getFilesDir(), filename);
        int length = (int) file.length();
        Log.d(LOGID, "File is bytes: " + String.valueOf(length));

        byte[] bytes = new byte[length]; //byte array to hold all read bytes

        FileInputStream fileInputStream;
        try { //try to open the file for reading
            fileInputStream = new FileInputStream(file);
            fileInputStream.read(bytes);
            fileInputStream.close();
        } catch (Exception e) {//handle exception arising from above
            Log.d(LOGID, e.toString());
        }

        //Now try something different that also requires eception handling
        try {
            String s = new String(bytes, "UTF-8");
            Log.d(LOGID, s);
        } catch (Exception e) { //handle exception from string creation
            Log.d(LOGID, e.toString());
        }
    }

}
