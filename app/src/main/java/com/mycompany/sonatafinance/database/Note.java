package com.mycompany.sonatafinance.database;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class Note {

  public final static String ID = "id";
  public final static String ID_CATEGORY = "id_category";
  public final static String CONTENT = "content";
  public final static String DATE = "date";

  public final static String TABLE_NAME = "notes";
  public final static String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
      + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
      + ID_CATEGORY + " INTEGER, "
      + CONTENT + " TEXT, "
      + DATE + " INTEGER, "
      + "FOREIGN KEY(" + ID_CATEGORY + ") REFERENCES " + Category.TABLE_NAME + "(" + Category.ID + ") "
      + "ON UPDATE CASCADE ON DELETE CASCADE"
      + ")";

  public final static String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;


  long id;
  long id_category;
  String content;
  long date;
  int color;
  String icon;
  String nameCategory;

  private Category category;

  public Note(){}

  public Note(String content, Category category){
    this.content = content;
    this.category = category;
    id_category = category.id;
    date = Calendar.getInstance().getTimeInMillis();
    color = category.color;
    icon = category.icon;
    nameCategory = category.name;
  }

  public Calendar getCalendar(){
    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(date);
    return calendar;
  }

  public String getCategoryName(){
    return nameCategory;
  }

  public String getContent(){
    return content;
  }

  public String getIdResourceIcon(){
    return icon;
  }

  public int getColor(){
    return color;
  }

  public long getDate() {
    return date;
  }

  public String getNameCategory(){
    return nameCategory;
  }

  public Category getCategory(){
    return category;
  }

  public void setContent(String content){
    this.content = content;
  }
  public void setCategory(Category category){
    this.category = category;
    id_category = category.id;
    nameCategory = category.name;
    color = category.color;
    icon = category.icon;
  }

}
