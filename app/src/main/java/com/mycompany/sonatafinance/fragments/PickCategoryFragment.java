package com.mycompany.sonatafinance.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.SearchView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.mycompany.sonatafinance.R;
import com.mycompany.sonatafinance.adapters.ListCategoriesAdapter;
import com.mycompany.sonatafinance.database.Category;
import com.mycompany.sonatafinance.database.DaoCategories;
import com.mycompany.sonatafinance.database.DaoNotes;
import com.mycompany.sonatafinance.impclass.PerformSearch;
import com.mycompany.sonatafinance.model.OnCategorySelectedListener;
import com.mycompany.sonatafinance.model.OnClickPopupMenuCategoryDeleteListener;
import com.mycompany.sonatafinance.popupmenu.CategoryPopupMenu;
import java.util.ArrayList;

public class PickCategoryFragment extends Fragment {

  ListView listView;
  ListCategoriesAdapter adapter;
  DaoCategories daoCategories;

  OnCategorySelectedListener onCategorySelectedListener;

  public PickCategoryFragment() {}

  @Nullable @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

    View root = inflater.inflate(R.layout.fragment_pick_category, container, false);
    listView = root.findViewById(R.id.list_view_categories);
    daoCategories = new DaoCategories(getContext());
    ArrayList<Category> categories = daoCategories.getCategories();
    adapter = new ListCategoriesAdapter(getContext(), categories);
    listView.setAdapter(adapter);

    listView.setOnItemClickListener((adapterView, view, id, i) -> {
      Category category = adapter.getItem(id);
      if (onCategorySelectedListener != null) {
        onCategorySelectedListener.onCategorySelected(category);
      }
      getActivity().onBackPressed();
    });

    listView.setOnItemLongClickListener((adapterView, view, i, l) -> {
      Category category = adapter.getItem(i);
      // delete category from database and update list
      // delete all Notes with this category
      showPopupMenu(view, ()->{
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Удалить категорию?");
        builder.setMessage("Вы уверены, что хотите удалить эту категорию? Удаление категории приведет к удалению всех заметок в ней.");
        builder.setPositiveButton("Удалить", (dialog, which) -> {
          new DaoNotes(getContext()).deleteAllByCategory(category);
          daoCategories.deleteCategory(category);
          categories.remove(category);
          adapter.notifyDataSetChanged();
        });
        builder.setNegativeButton("Отмена", (dialog, which) -> {

        });
        builder.show();
      });



      return true;
    });

    return root;
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
    if (actionBar != null) {
      actionBar.setDisplayHomeAsUpEnabled(true);
      actionBar.setHomeAsUpIndicator(androidx.appcompat.R.drawable.abc_ic_ab_back_material);
    }
    setHasOptionsMenu(true);
  }

  @Override public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
    inflater.inflate(R.menu.menu_pick_category, menu);
    super.onCreateOptionsMenu(menu, inflater);
  }

  @Override public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    if (item.getItemId() == android.R.id.home) {
        getActivity().onBackPressed();
    } else if (item.getItemId() == R.id.pick_category_item_create) {
      FragmentTransaction transaction = getFragmentManager().beginTransaction();
      transaction.replace(R.id.fragmentContainerView, new CreateCategoryFragment());
      transaction.addToBackStack(null);
      transaction.commit();

    }
    return super.onOptionsItemSelected(item);
  }

  public void setOnCategorySelectedListener(OnCategorySelectedListener onCategorySelectedListener) {
    this.onCategorySelectedListener = onCategorySelectedListener;
  }

  public void showPopupMenu(View view, OnClickPopupMenuCategoryDeleteListener listener) {
    CategoryPopupMenu categoryPopupMenu = new CategoryPopupMenu(getContext(), view, listener);
    categoryPopupMenu.show();
  }
}
