package com.mycompany.sonatafinance.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

public class SoNataDatabaseOpenHelper extends SQLiteOpenHelper {

  private static final int VERSION = 1;
  private static final String NAME_DATABASE = "SonataFinanceDatabase";
  public SoNataDatabaseOpenHelper(@Nullable Context context) {
    super(context, NAME_DATABASE, null, VERSION);
  }

  @Override public void onCreate(SQLiteDatabase db) {
    db.execSQL("PRAGMA foreign_keys = ON");

    db.execSQL(Note.CREATE_TABLE);
    db.execSQL(Category.CREATE_TABLE);

    Category defaultCategory = Category.getDefaultCategory();

    db.execSQL("INSERT OR IGNORE INTO " + Category.TABLE_NAME + " VALUES (" + defaultCategory.id + ", '" + defaultCategory.name
        + "', "+defaultCategory.color+", '"+defaultCategory.icon+"')");
    db.execSQL("INSERT OR IGNORE INTO " + Category.TABLE_NAME + " VALUES (2, 'Заметки о здоровье', 0x8090f0ff, 'health');");
    db.execSQL("INSERT OR IGNORE INTO " + Note.TABLE_NAME + " VALUES (1, 1, 'День проходит замечательно', 1704005491468)");
    db.execSQL("INSERT OR IGNORE INTO " + Note.TABLE_NAME + " VALUES (2, 2, 'Замечание о здоровье', 1704005494000)");
  }

  @Override public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    // add new column
    db.execSQL(Category.DROP_TABLE);
    db.execSQL(Note.DROP_TABLE);
    onCreate(db);
  }
}
