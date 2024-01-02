package com.mycompany.sonatafinance;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;

public class DialogConfirmation extends AlertDialog {
  public DialogConfirmation(Context context) {
    super(context);
    Builder builder = new Builder(context);
    builder.setTitle("Удалить категорию?");
    builder.setMessage("Вы уверены, что хотите удалить категорию? Удаление категории приведёт к удалению всех заметок связанных с ней.");
    builder.setPositiveButton("Да", null);

  }
}
