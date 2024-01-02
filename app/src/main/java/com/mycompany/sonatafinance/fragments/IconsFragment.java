package com.mycompany.sonatafinance.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.mycompany.sonatafinance.R;
import com.mycompany.sonatafinance.adapters.GridViewIconsAdapter;
import com.mycompany.sonatafinance.model.OnIconSelectedListener;

public class IconsFragment extends Fragment {
  public IconsFragment() {

  }

  OnIconSelectedListener onIconSelectedListener;
  GridViewIconsAdapter gridViewAdapter;

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    //remove arrow home from action bar
    if(getActivity() != null){
      if(((AppCompatActivity)getActivity()).getSupportActionBar() != null) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
      }
    }

  }

  @Nullable @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View root = inflater.inflate(R.layout.fragment_icons, container, false);
    GridView gridView = root.findViewById(R.id.fragment_icons_grid_view);
    String[] icons = new String[]{
      "calendar", "day", "health", "img", "icon_1", "icon_2", "icon_3", "icon_4",
      "icon_5", "icon_6", "icon_8", "icon_9", "icon_10", "icon_11", "icon_12",
      "icon_13", "icon_14", "icon_15", "icon_16", "icon_17", "icon_18", "icon_19", "icon_20",
      "icon_21", "icon_22", "icon_23", "icon_24", "icon_25", "icon_26", "icon_27", "icon_28",
      "icon_29", "icon_30", "icon_31", "icon_32", "icon_33", "icon_34", "icon_35", "icon_36",
      "icon_37", "icon_38", "icon_39", "icon_40", "icon_41"
    };
    gridViewAdapter = new GridViewIconsAdapter(getContext(), icons);
    gridView.setAdapter(gridViewAdapter);

    gridView.setOnItemClickListener((adapterView, view, i, l) -> {
      String nameIcon = (String) adapterView.getItemAtPosition(i);
      onIconSelectedListener.onIconSelected(nameIcon);
      getActivity().onBackPressed();
    });

    return root;
  }

  public void setOnIconSelectedListener(OnIconSelectedListener onIconSelectedListener) {
    this.onIconSelectedListener = onIconSelectedListener;
  }
}
