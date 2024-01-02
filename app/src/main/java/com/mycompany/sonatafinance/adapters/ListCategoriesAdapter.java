package com.mycompany.sonatafinance.adapters;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.mycompany.sonatafinance.R;
import com.mycompany.sonatafinance.database.Category;
import com.mycompany.sonatafinance.model.OnCategorySelectedListener;
import java.util.List;

public class ListCategoriesAdapter extends ArrayAdapter<Category> {

  public ListCategoriesAdapter(Context context, List<Category> categories) {
    super(context, R.layout.item_category, categories);
  }

  @NonNull @Override
  public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
    convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_category, parent, false);
    ConstraintLayout layout = convertView.findViewById(R.id.layout_fragment_category);
    TextView tv_title = convertView.findViewById(R.id.image_view_tv_title);
    ImageView imageView = convertView.findViewById(R.id.item_category_iv_icon);

    Category category = getItem(position);

    tv_title.setText(category.getName());
    GradientDrawable drawable = new GradientDrawable();
    drawable.setColor(category.getColor());
    drawable.setCornerRadius(20);

    layout.setBackground(drawable);
    imageView.setImageResource(getContext().getResources().getIdentifier(category.getIcon(), "drawable", getContext().getPackageName()));

    return convertView;
  }

}
