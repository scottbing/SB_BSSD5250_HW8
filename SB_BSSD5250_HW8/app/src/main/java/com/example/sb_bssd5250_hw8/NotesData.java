package com.example.sb_bssd5250_hw8;

import android.content.Context;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class NotesData {  //Singleton class of all not data

    public interface NotesDataUpdatedListener {
        public void updateNotesDependents();
    }
    public NotesDataUpdatedListener listener;

    private ArrayList<Note> mNotes;

    private static NotesData sNotesData;
    private Context mAppContext;

    private NotesData(Context context) {
        mAppContext = context;
        mNotes = new ArrayList<Note>();
        listener = null;
    }

    public ArrayList<Note> getNoteList() {
        return mNotes;
    }
    public void setListener(NotesDataUpdatedListener notesDataUpdatedListener ) {
        listener = notesDataUpdatedListener;
    }

    public static NotesData getInstance(@Nullable Context c) {
        if (sNotesData == null) {
            sNotesData = new NotesData(c.getApplicationContext());
        }
        return sNotesData;
    }

    public void refreshNotes() {
        if (listener != null)
            listener.updateNotesDependents();
    }
}
