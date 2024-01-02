package com.mycompany.sonatafinance.popupmenu;

import android.content.Context;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import com.mycompany.sonatafinance.model.OnClickPopupMenuCategoryDeleteListener;

public class CategoryPopupMenu extends PopupMenu {

  OnClickPopupMenuCategoryDeleteListener deleteListener;
  public CategoryPopupMenu(final Context context, View view, OnClickPopupMenuCategoryDeleteListener deleteListener) {
    super(context, view);
    this.deleteListener = deleteListener;
    getMenu().add("Delete");

    setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
      @Override
      public boolean onMenuItemClick(MenuItem item) {
        if (item.getTitle().equals("Delete")) {
          deleteListener.onCategoryDelete();
          return true;
        }
        return true;
      }
    });
  }
}
