package com.example.ppo2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;

public class DatabaseAdapter {

    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;

    public DatabaseAdapter(Context context)
    {
        dbHelper = new DatabaseHelper(context.getApplicationContext());
    }

    public DatabaseAdapter open()
    {
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close()
    {
        dbHelper.close();
    }

    private Cursor getAllEntries()
    {
        String[] columns = new String[] {DatabaseHelper.COLUMN_ID, DatabaseHelper.COLUMN_COLOR,
                DatabaseHelper.COLUMN_TITLE, DatabaseHelper.COLUMN_PREPARE, DatabaseHelper.COLUMN_WORK,
                DatabaseHelper.COLUMN_calm, DatabaseHelper.COLUMN_CYCLES, DatabaseHelper.COLUMN_SETS,
                DatabaseHelper.COLUMN_StsCalm};
        return  database.query(DatabaseHelper.TABLE, columns, null, null,
                null, null, null);
    }

    public List<Sequence> getSequences()
    {
        ArrayList<Sequence> sequences = new ArrayList<>();
        Cursor cursor = getAllEntries();
        if(cursor.moveToFirst())
        {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID));
                int color = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_COLOR));
                String title = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_TITLE));
                int prepare = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_PREPARE));
                int work = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_WORK));
                int calm = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_calm));
                int cycles = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_CYCLES));
                int sets = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_SETS));
                int stsCalm = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_StsCalm));
                sequences.add(new Sequence(id, color, title, prepare, work, calm, cycles, sets, stsCalm));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return sequences;
    }

    public long getCount()
    {
        return DatabaseUtils.queryNumEntries(database, DatabaseHelper.TABLE);
    }

    public Sequence getSequence(int id)
    {
        Sequence sequence = null;
        String query = String.format("SELECT * FROM %s WHERE %s=?",DatabaseHelper.TABLE, DatabaseHelper.COLUMN_ID);
        Cursor cursor = database.rawQuery(query, new String[]{ String.valueOf(id)});
        if(cursor.moveToFirst())
        {
            int color = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_COLOR));
            String title = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_TITLE));
            int prepare = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_PREPARE));
            int work = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_WORK));
            int calm = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_calm));
            int cycles = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_CYCLES));
            int sets = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_SETS));
            int stsCalm = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_StsCalm));
            sequence = new Sequence(id, color, title, prepare, work, calm, cycles, sets, stsCalm);
        }
        cursor.close();
        return sequence;
    }

    public long insert(Sequence sequence)
    {

        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.COLUMN_COLOR, sequence.color);
        cv.put(DatabaseHelper.COLUMN_TITLE, sequence.title);
        cv.put(DatabaseHelper.COLUMN_PREPARE, sequence.prepare);
        cv.put(DatabaseHelper.COLUMN_WORK, sequence.work);
        cv.put(DatabaseHelper.COLUMN_calm, sequence.calm);
        cv.put(DatabaseHelper.COLUMN_CYCLES, sequence.cycles);
        cv.put(DatabaseHelper.COLUMN_SETS, sequence.sets);
        cv.put(DatabaseHelper.COLUMN_StsCalm, sequence.stsCalm);
        return  database.insert(DatabaseHelper.TABLE, null, cv);
    }

    public long delete(int id)
    {

        String whereClause = "_id = ?";
        String[] whereArgs = new String[]{String.valueOf(id)};
        return database.delete(DatabaseHelper.TABLE, whereClause, whereArgs);
    }

    public long update(Sequence sequence)
    {
        String whereClause = DatabaseHelper.COLUMN_ID + "=" + String.valueOf(sequence.id);
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.COLUMN_COLOR, sequence.color);
        cv.put(DatabaseHelper.COLUMN_TITLE, sequence.title);
        cv.put(DatabaseHelper.COLUMN_PREPARE, sequence.prepare);
        cv.put(DatabaseHelper.COLUMN_WORK, sequence.work);
        cv.put(DatabaseHelper.COLUMN_calm, sequence.calm);
        cv.put(DatabaseHelper.COLUMN_CYCLES, sequence.cycles);
        cv.put(DatabaseHelper.COLUMN_SETS, sequence.sets);
        cv.put(DatabaseHelper.COLUMN_StsCalm, sequence.stsCalm);
        return database.update(DatabaseHelper.TABLE, cv, whereClause, null);
    }

    public void clear()
    {
        dbHelper.onUpgrade(database, 0, 0);
    }
}