package com.mycompany.sonatafinance.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.mycompany.sonatafinance.R;
import com.mycompany.sonatafinance.activities.MainActivity;
import com.mycompany.sonatafinance.database.Category;
import com.mycompany.sonatafinance.database.DaoCategories;
import yuku.ambilwarna.AmbilWarnaDialog;

public class CreateCategoryFragment extends Fragment {

  String icon = "day";
  int MainColor = Color.WHITE;
  ImageView ivIconCategory;
  String nameCategory;

  boolean isIconSelected = false;
  public CreateCategoryFragment(){}

  @Nullable @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View root = inflater.inflate(R.layout.fragment_create_category, container, false);

    TextView pickColor = root.findViewById(R.id.tv_create_category_color);
    EditText etNameCategory = root.findViewById(R.id.et_create_category);
    ivIconCategory = root.findViewById(R.id.iv_create_category);
    Button btnCreateCategory = root.findViewById(R.id.bt_create_category);

    if(isIconSelected){
      ivIconCategory.setImageResource(getResources().getIdentifier(icon, "drawable", getContext().getPackageName()));
    }

    pickColor.setOnClickListener(v -> {
      AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(getContext(), 0xff000000, new AmbilWarnaDialog.OnAmbilWarnaListener() {
        @Override
        public void onCancel(AmbilWarnaDialog dialog) {}

        @Override
        public void onOk(AmbilWarnaDialog dialog, int color) {
          pickColor.setBackgroundColor(color);
          MainColor = color;
        }
      });
      colorPicker.show();
    });
    ivIconCategory.setOnClickListener(v -> {
      FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
      IconsFragment iconsFragment = new IconsFragment();
      iconsFragment.setOnIconSelectedListener((icon) -> {
        isIconSelected = true;
        this.icon = icon;
      });
      transaction.replace(R.id.fragmentContainerView, iconsFragment);
      transaction.addToBackStack(null);
      transaction.commit();
    });


    btnCreateCategory.setOnClickListener(v -> {
      Category category = new Category();
      category.setName(etNameCategory.getText().toString());
      category.setIcon(icon);
      category.setColor(MainColor);
      new DaoCategories(getContext()).insertCategory(category);
      getActivity().onBackPressed();
    });

    return root;
  }
}
