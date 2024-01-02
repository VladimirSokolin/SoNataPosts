package com.mycompany.sonatafinance.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ArrayAdapter;
import java.util.ArrayList;
import java.util.Calendar;

public class DaoNotes {
  SoNataDatabaseOpenHelper dataHelper;
  DaoCategories daoCategories;
  public DaoNotes(SoNataDatabaseOpenHelper dataHelper){
    this.dataHelper = dataHelper;
    daoCategories = new DaoCategories(dataHelper);
    init();
  }

  public DaoNotes(Context context){
    this.dataHelper = new SoNataDatabaseOpenHelper(context);
    daoCategories = new DaoCategories(context);
    init();
  }
  private void init(){

  }

  public ArrayList<Note> searchAllByString(String sequence){
    ArrayList<Note> list = new ArrayList<>();
    Thread thread = new Thread(() -> {
      SQLiteDatabase db = dataHelper.getReadableDatabase();
      Cursor cursor = db.rawQuery("SELECT * FROM " + Note.TABLE_NAME + " WHERE " + Note.CONTENT + " LIKE '%" + sequence + "%'", null);
      if(cursor.moveToFirst()){
        do{
          Note note = new Note();
          note.id = cursor.getLong(cursor.getColumnIndexOrThrow(Note.ID));
          note.id_category = cursor.getLong(cursor.getColumnIndexOrThrow(Note.ID_CATEGORY));
          note.content = cursor.getString(cursor.getColumnIndexOrThrow(Note.CONTENT));
          note.date = cursor.getLong(cursor.getColumnIndexOrThrow(Note.DATE));

          note.setCategory(daoCategories.getCategoryById(note.id_category));

          list.add(note);
        }while(cursor.moveToNext());
        cursor.close();
        db.close();
      }
    });
    thread.start();
    try {
      thread.join();
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
    return list;

  }

  public void insert(Note note){
    Thread thread = new Thread(() -> {
      SQLiteDatabase db = dataHelper.getWritableDatabase();
      ContentValues values = new ContentValues();

      values.put(Note.ID_CATEGORY, note.id_category);
      values.put(Note.CONTENT, note.content);
      values.put(Note.DATE, note.date);

      db.insert(Note.TABLE_NAME, null, values);
      db.close();
    });
    thread.start();
    try {
      thread.join();
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  public void update(Note note){
    Thread thread = new Thread(() -> {
      SQLiteDatabase db = dataHelper.getWritableDatabase();
      ContentValues values = new ContentValues();
      values.put(Note.ID_CATEGORY, note.id_category);
      values.put(Note.CONTENT, note.content);
      values.put(Note.DATE, note.date);
      db.update(Note.TABLE_NAME, values, Note.ID + " = ?", new String[] {String.valueOf(note.id)});
    });
    thread.start();
  }

  public ArrayList getAll(){
    ArrayList list = new ArrayList<>();
    Thread thread = new Thread(() -> {
      SQLiteDatabase db = dataHelper.getReadableDatabase();
      Cursor cursor = db.rawQuery("SELECT * FROM " + Note.TABLE_NAME, null);
      if(cursor.moveToFirst()){
        do{
          Note note = new Note();
          note.id = cursor.getLong(cursor.getColumnIndexOrThrow(Note.ID));
          note.id_category = cursor.getLong(cursor.getColumnIndexOrThrow(Note.ID_CATEGORY));
          note.content = cursor.getString(cursor.getColumnIndexOrThrow(Note.CONTENT));
          note.date = cursor.getLong(cursor.getColumnIndexOrThrow(Note.DATE));

          note.setCategory(daoCategories.getCategoryById(note.id_category));

          list.add(note);
        }while(cursor.moveToNext());
        cursor.close();
        db.close();
      }
    });
    thread.start();
    try {
      thread.join();
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
    return list;
  }

  public ArrayList<Object> getAllInDate(Calendar calendar){
    ArrayList<Object> list = new ArrayList<>();
    Thread thread = new Thread(() -> {
      SQLiteDatabase db = dataHelper.getReadableDatabase();
      long start = timeImMillisStart(calendar);
      long end = timeImMillisEnd(calendar);
      Cursor cursor = db.rawQuery("SELECT * FROM " + Note.TABLE_NAME + " WHERE " + Note.DATE + " BETWEEN " + start + " AND " + end, null);
      if (cursor.moveToFirst()) {
        do {
          Note note = new Note();
          note.id = cursor.getLong(cursor.getColumnIndexOrThrow(Note.ID));
          note.id_category = cursor.getLong(cursor.getColumnIndexOrThrow(Note.ID_CATEGORY));
          note.content = cursor.getString(cursor.getColumnIndexOrThrow(Note.CONTENT));
          note.date = cursor.getLong(cursor.getColumnIndexOrThrow(Note.DATE));

          note.setCategory(daoCategories.getCategoryById(note.id_category));
          list.add(note);

        } while (cursor.moveToNext());
      }
    });
    thread.start();
    try {
      thread.join();
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
    return list;
  }

  private long timeImMillisStart(Calendar calendar){
    calendar.set(Calendar.HOUR_OF_DAY, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MILLISECOND, 0);
    return calendar.getTimeInMillis();
  }

  private long timeImMillisEnd(Calendar calendar){
    calendar.set(Calendar.HOUR_OF_DAY, 23);
    calendar.set(Calendar.MINUTE, 59);
    calendar.set(Calendar.SECOND, 59);
    calendar.set(Calendar.MILLISECOND, 999);
    return calendar.getTimeInMillis();
  }

  public ArrayList<Note> getByCategory(Category category) {
    ArrayList<Note> list = new ArrayList<>();
    Thread thread = new Thread(() -> {
      SQLiteDatabase db = dataHelper.getReadableDatabase();
      Cursor cursor = db.rawQuery(
          "SELECT * FROM " + Note.TABLE_NAME + " WHERE " + Note.ID_CATEGORY + " = " + category.getId(),
          null);
      if (cursor.moveToFirst()) {
        do {
          Note note = new Note();
          note.id = cursor.getLong(cursor.getColumnIndexOrThrow(Note.ID));
          note.id_category = cursor.getLong(cursor.getColumnIndexOrThrow(Note.ID_CATEGORY));
          note.content = cursor.getString(cursor.getColumnIndexOrThrow(Note.CONTENT));
          note.date = cursor.getLong(cursor.getColumnIndexOrThrow(Note.DATE));

          note.setCategory(category);

          list.add(note);
        } while (cursor.moveToNext());
      }
    });
    thread.start();
    try {
      thread.join();
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
    return list;
  }

  public void deleteAllByCategory(Category category){
    Thread thread = new Thread(() -> {
      SQLiteDatabase db = dataHelper.getReadableDatabase();
      db.delete(Note.TABLE_NAME, Note.ID_CATEGORY + " = " + category.getId(), null);
    });
    thread.start();
    try {
      thread.join();
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  public void delete(Note note){
    Thread thread = new Thread(() -> {
      SQLiteDatabase db = dataHelper.getReadableDatabase();
      db.delete(Note.TABLE_NAME, Note.ID + " = " + note.id, null);
    });
    thread.start();
    try {
      thread.join();
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

}
