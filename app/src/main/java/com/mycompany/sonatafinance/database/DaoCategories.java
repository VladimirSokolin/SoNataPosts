package com.mycompany.sonatafinance.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;

public class DaoCategories {
  SoNataDatabaseOpenHelper dataHelper;
  public DaoCategories(SoNataDatabaseOpenHelper dataHelper){
    this.dataHelper = dataHelper;
  }

  public DaoCategories(Context context){
    this.dataHelper = new SoNataDatabaseOpenHelper(context);
  }

  public void insertCategory(Category category){
    Thread thread = new Thread(() -> {
      SQLiteDatabase db = dataHelper.getWritableDatabase();
      ContentValues values = new ContentValues();
      values.put(Category.NAME, category.name);
      values.put(Category.COLOR, category.color);
      values.put(Category.ICON, category.icon);

      db.insert(Category.TABLE_NAME, null, values);
      db.close();
    });
    thread.start();
    try {
      thread.join();
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  public ArrayList<Category> getCategories(){
    ArrayList<Category> list = new ArrayList<>();
    Thread thread = new Thread(() -> {
      SQLiteDatabase db = dataHelper.getReadableDatabase();
      Cursor cursor = db.rawQuery("SELECT * FROM " + Category.TABLE_NAME, null);
      if(cursor.moveToFirst()){
        do{
          Category category = new Category();
          category.id = cursor.getLong(cursor.getColumnIndexOrThrow(Category.ID));
          category.name = cursor.getString(cursor.getColumnIndexOrThrow(Category.NAME));
          category.color = cursor.getInt(cursor.getColumnIndexOrThrow(Category.COLOR));
          category.icon = cursor.getString(cursor.getColumnIndexOrThrow(Category.ICON));
          list.add(category);
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

  public Category getCategoryById(long id){
    Category category = new Category();
    Thread thread = new Thread(() -> {
      SQLiteDatabase db = dataHelper.getReadableDatabase();
      Cursor cursor = db.rawQuery("SELECT * FROM " + Category.TABLE_NAME + " WHERE " + Category.ID + " = " + id, null);
      if(cursor.moveToFirst()){
        category.id = cursor.getLong(cursor.getColumnIndexOrThrow(Category.ID));
        category.name = cursor.getString(cursor.getColumnIndexOrThrow(Category.NAME));
        category.color = cursor.getInt(cursor.getColumnIndexOrThrow(Category.COLOR));
        category.icon = cursor.getString(cursor.getColumnIndexOrThrow(Category.ICON));
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
    return category;
  }

  public void deleteCategory(Category category){
    Thread thread = new Thread(() -> {
      SQLiteDatabase db = dataHelper.getWritableDatabase();
      db.delete(Category.TABLE_NAME, Category.ID + " = " + category.id, null);
      db.close();
    });
    thread.start();
    try {
      thread.join();
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }
}
