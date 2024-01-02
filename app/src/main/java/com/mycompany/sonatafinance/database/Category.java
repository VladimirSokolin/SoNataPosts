package com.mycompany.sonatafinance.database;

import android.content.Context;
import com.mycompany.sonatafinance.R;

public class Category {

  public final static String ID = "id";
  public final static String NAME = "name";
  public final static String COLOR = "color";
  public final static String ICON = "icon";
  public final static String TABLE_NAME = "categories";
  public final static String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
      + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
      + NAME + " TEXT, "
      + COLOR + " INTEGER, "
      + ICON + " TEXT"
      + ")";
  public final static String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
  // указать какие значения ввести в таблицу

  long id;
  String name;
  int color;
  String icon;

  public String getName() {
    return name;
  }
  public int getColor() {
    return color;
  }
  public String getIcon() {
    return icon;
  }
  public long getId() {
    return id;
  }

  public void setIcon(String icon) {
    this.icon = icon;
  }
  public void setColor(int color) {
    this.color = color;
  }
  public void setName(String name) {
    this.name = name;
  }

  public static Category getDefaultCategory(){
    Category category = new Category();
    category.id = 1;
    category.name = "Обычная заметка";
    category.color = 0xFF808090;
    category.icon = "img";
    return category;
  }
}
