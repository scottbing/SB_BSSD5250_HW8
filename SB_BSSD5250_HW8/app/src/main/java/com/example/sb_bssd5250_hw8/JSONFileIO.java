package com.example.sb_bssd5250_hw8;

import android.content.Context;
import android.util.Log;
import java.util.ArrayList;
import android.view.View;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class JSONFileIO {

    private static String LOGID = "JSONFileIO";
    private Context mAppContext;    // set up context for singleton class

    public JSONFileIO() {
        //setContentView(R.layout.activity_main);
        this.mAppContext = mAppContext.getApplicationContext();
        
        makeData();
        //writeDataFile();
        NoteJSONSerializer noteJSONSerializer = new NoteJSONSerializer( this.mAppContext, "notes.json" );
        try {
            noteJSONSerializer.saveNotes (NotesData.getInstance(this.mAppContext).getNoteList());
        }  catch (Exception e) {
            Log.d(LOGID, e.toString());
        }
        readDataFile("notes json");
    }


    private void makeData() {
        ArrayList<Note> notes = NotesData.getInstance(this.mAppContext).getNoteList();
        for(int i=0;  i<10;  i++) {
            Note note = new Note();
            note.setName("Note	#" + i);
            note.setDesc(String.valueOf(View.generateViewId()));
            notes.add(note);
        }
    }

    private void writeDataFile() {
        ArrayList<Note> notes = NotesData.getInstance(this.mAppContext).getNoteList();
        String filename = "notes.txt";
        File file = new File(this.mAppContext.getFilesDir(), filename);
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

    private void readDataFile(String filename)  {

        File file = new File(this.mAppContext.getFilesDir(), filename);
        int length = (int)file.length();
        Log.d(LOGID,"File is bytes: " + String.valueOf(length));

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
            String s = new String(bytes,"UTF-8");
            Log.d(LOGID, s);
        } catch (Exception e) { //handle exception from string creation
            Log.d(LOGID, e.toString());
        }
    }

}
