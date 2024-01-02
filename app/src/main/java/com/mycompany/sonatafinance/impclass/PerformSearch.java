package com.mycompany.sonatafinance.impclass;

import android.content.Context;
import android.widget.SearchView;
import com.mycompany.sonatafinance.database.DaoNotes;
import com.mycompany.sonatafinance.database.Note;
import com.mycompany.sonatafinance.model.OnSearchListener;
import java.util.ArrayList;

public class PerformSearch implements SearchView.OnQueryTextListener {

  Context context;
  DaoNotes dao;
  private ArrayList<Note> list;
  OnSearchListener onSearchListener;
  public PerformSearch(Context context, OnSearchListener onSearchListener) {
    this.context = context;
    this.onSearchListener = onSearchListener;
    dao = new DaoNotes(context);
  }

  @Override
  public boolean onQueryTextSubmit(String s) {
    list = dao.searchAllByString(s);
    list.sort((o1, o2) -> {
      if (o1.getDate() > o2.getDate()) {
        return -1;
      }
      if (o1.getDate() < o2.getDate()) {
        return 1;
      }
      return 0;
    });
    onSearchListener.onSearch(list);
    return true;
  }

  @Override public boolean onQueryTextChange(String s) {
    return false;
  }

  public ArrayList<Note> getList() {
    return list;
  }
}
