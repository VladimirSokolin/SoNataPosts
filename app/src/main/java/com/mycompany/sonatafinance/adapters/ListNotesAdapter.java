package com.mycompany.sonatafinance.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.mycompany.sonatafinance.R;
import com.mycompany.sonatafinance.database.Note;
import com.mycompany.sonatafinance.fragments.DayFragment;
import com.mycompany.sonatafinance.model.ListElementNote;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ListNotesAdapter extends ArrayAdapter {
  public ListNotesAdapter(@NonNull Context context, @NonNull List list) {
    super(context, R.layout.item_note, list);
  }

  DayFragment.OnNoteSelectedListener onNoteSelectedListener;
  SimpleDateFormat simpleDateFormat = new SimpleDateFormat(" dd.MM.yyyy   HH:mm", Locale.getDefault());

  @NonNull @Override
  public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

    Object object = getItem(position);

    convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_note, parent, false);
    TextView tvTitle = convertView.findViewById(R.id.tv_title);
    TextView editText = convertView.findViewById(R.id.et_content);
    TextView tvDate = convertView.findViewById(R.id.tv_date);
    ImageView ivIconCategory = convertView.findViewById(R.id.iv_icon_category);

    if(object instanceof Note){
      tvDate.setText(simpleDateFormat.format(((Note)object).getDate()));
      tvTitle.setText(""+((Note)object).getCategoryName());
      tvTitle.setBackgroundColor(((Note)object).getColor());
      editText.setText(((Note)object).getContent());
      if(((Note)object).getCategory() != null){
        ivIconCategory.setImageResource(getContext().getResources().getIdentifier(((Note)object).getCategory().getIcon(), "drawable", getContext().getPackageName()));

      }

      editText.setOnClickListener(v -> {
        Note note = (Note) object;
        onNoteSelectedListener.onNoteSelected(note);
      });

    }

    return convertView;
  }

  public void setOnNoteSelectedListener(DayFragment.OnNoteSelectedListener onNoteSelectedListener){
    this.onNoteSelectedListener = onNoteSelectedListener;
  }
}
