package com.mycompany.sonatafinance.model;

import com.mycompany.sonatafinance.database.Note;
import java.util.ArrayList;

public interface OnSearchListener {
  public void onSearch(ArrayList<Note> list);
}
