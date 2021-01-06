package com.example.eventssqliteexample;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class MyDBHelper extends SQLiteOpenHelper {

    public static final int DB_VERSION=1;
    public static final String DB_NAME="EventsSQLiteDB";
    public static final String TABLE_NAME="Events", COL_ID="ID", COL_NAME="Name", COL_DESC = "Description", COL_LOC = "Location", COL_DATE = "EDate";


    Context context;

    MyDBHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    public MyEvent getEvent(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, new String[] { COL_ID,
                        COL_NAME, COL_DESC, COL_LOC, COL_DATE }, COL_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        return new MyEvent(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4)
        );
    }

    public int editEvent(MyEvent event){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_NAME, event.getName());
        values.put(COL_DESC, event.getDescription());
        values.put(COL_LOC, event.getLocation());
        values.put(COL_DATE, event.getDate());

        // updating row
        return db.update(TABLE_NAME, values, COL_ID + " = ?",
                new String[] { String.valueOf(event.getId()) });
    }

    public void removeEvent(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COL_ID + " = ?",
                new String[] { String.valueOf(id) });
        db.close();
    }

    public void addEvent(MyEvent event){
        SQLiteDatabase database=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(COL_NAME, event.getName());
        contentValues.put(COL_DESC, event.getDescription());
        contentValues.put(COL_LOC, event.getLocation());
        contentValues.put(COL_DATE, event.getDate());

        database.insert(TABLE_NAME, null, contentValues);
        database.close();
    }

    public List<MyEvent> fetchEvents(){
        List<MyEvent> myEvents=new ArrayList<>();
        String selectQuery="SELECT * FROM "+TABLE_NAME;
        SQLiteDatabase database = getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery,null);
        if(cursor.moveToFirst()){
            do{
                MyEvent event=new MyEvent(Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4)
                        );
                myEvents.add(event);
            }while (cursor.moveToNext());
        }
        return myEvents;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+TABLE_NAME+" ("
                +COL_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                +COL_NAME+" TEXT NOT NULL, "
                +COL_DESC+" TEXT NOT NULL, "
                +COL_LOC+" TEXT NOT NULL, "
                +COL_DATE+" TEXT NOT NULL"
                +")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }
}
