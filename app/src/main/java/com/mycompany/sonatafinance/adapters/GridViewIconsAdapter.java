package com.mycompany.sonatafinance.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.mycompany.sonatafinance.R;
import java.util.List;

public class GridViewIconsAdapter extends ArrayAdapter<String> {
  public GridViewIconsAdapter(@NonNull Context context, String[] list) {
    super(context, R.layout.item_icon, list);
  }

  @NonNull @Override
  public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

    convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_icon, parent, false);
    ImageView ivIcon = convertView.findViewById(R.id.iv_pick_icon);
    String icon = getItem(position);
    ivIcon.setImageResource(getContext().getResources().getIdentifier(icon, "drawable", getContext().getPackageName()));

    return convertView;
  }
}
