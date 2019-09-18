package com.example.sb_bssd5250_hw8;

import android.content.Context;

import com.example.sb_bssd5250_hw8.Note;
import com.example.sb_bssd5250_hw8.NotesData;

import org.json.JSONArray;
import org.json.JSONException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class NoteJSONSerializer {

    private Context mContext;
    private String mFilename;

    // NoteJSONSerializer Constructor
    public NoteJSONSerializer(Context c, String filename) {
        mContext = c;
        mFilename = filename;
    }

    public void saveNotes(ArrayList<Note> notes)
            throws JSONException, IOException {
        //Build an array in JSON
        JSONArray jsonArray = new JSONArray();
        for (Note n : notes) {
            //use  the toJSON function we wrote on each note
            jsonArray.put(n.toJSON());
        }
        //JSONArray of all notes built
        try {
            writeDataFile(jsonArray);
        } catch (Exception e) {
            //catch any errors that occur from try and throw them back to whoever called this
            throw e;
        }
    }

    private void writeDataFile(JSONArray jsonArray)
            throws JSONException, IOException {
        ArrayList<Note> notes = NotesData.getInstance(mContext).getNoteList();
        File file = new File(mContext.getFilesDir(), mFilename);
        FileOutputStream fileOutputStream;

        try {    //try to open the  file for writing
            fileOutputStream = new FileOutputStream(file);
            //Turn the first note's name to bytes   in utf8 format and put in file
            fileOutputStream.write(jsonArray.toString().getBytes("UTF-8"));
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (Exception e) {
            //catch any errors that occur from try and throw them back to whoever called this
            throw e;
        }
    }

}
