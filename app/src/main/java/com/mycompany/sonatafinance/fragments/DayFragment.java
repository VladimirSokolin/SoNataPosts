package com.mycompany.sonatafinance.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.mycompany.sonatafinance.R;
import com.mycompany.sonatafinance.activities.MainActivity;
import com.mycompany.sonatafinance.adapters.ListCategoriesAdapter;
import com.mycompany.sonatafinance.adapters.ListNotesAdapter;
import com.mycompany.sonatafinance.database.Category;
import com.mycompany.sonatafinance.database.DaoCategories;
import com.mycompany.sonatafinance.database.DaoNotes;
import com.mycompany.sonatafinance.database.Note;
import com.mycompany.sonatafinance.database.SoNataDatabaseOpenHelper;
import com.mycompany.sonatafinance.impclass.PerformSearch;
import com.mycompany.sonatafinance.model.OnSearchListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class DayFragment extends Fragment implements OnSearchListener {

  Calendar calendar;
  private TextView tvScoreboard;
  private ListView listViewNotes;
  private ListNotesAdapter listNotesAdapter;
  DaoNotes daoNotes;
  private ArrayList<Object> listNotes;
  private SoNataDatabaseOpenHelper dataHelper;
  public DayFragment(){

  }

  @Override public void onSearch(ArrayList<Note> list) {
    listNotesAdapter = new ListNotesAdapter(getContext(), list);
    listViewNotes.setAdapter(listNotesAdapter);
  }

  public interface OnNoteSelectedListener{
    void onNoteSelected(Note note);
  }

  OnNoteSelectedListener onNoteSelectedListener;

  @Override public void onAttach(@NonNull Context context) {
    onNoteSelectedListener = (MainActivity) context;
    super.onAttach(context);
  }

  @Nullable @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_day, container, false);

    if(calendar == null){
      calendar = Calendar.getInstance();
    }

    dataHelper = new SoNataDatabaseOpenHelper(getContext());
    daoNotes = new DaoNotes(dataHelper);
    listNotes = daoNotes.getAllInDate(calendar);

    tvScoreboard = view.findViewById(R.id.fragment_day_tv_scoreboard);
    tvScoreboard.setText(setDateOnScoreboard());

    listViewNotes = view.findViewById(R.id.fragment_day_list_view_notes);

    listViewNotes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Object object = adapterView.getItemAtPosition(i);
        if(object instanceof Note){
          Note note = (Note) object;
          onNoteSelectedListener.onNoteSelected(note);
        }
      }
    });
    return view;
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    setHasOptionsMenu(true);
    // remove arrow home
    if(getActivity() != null){
      if(getActivity().getActionBar() != null){
        getActivity().getActionBar().setDisplayShowHomeEnabled(false);
      }
    }
    super.onCreate(savedInstanceState);
  }

  @Override public void onStart() {
    if(getContext() != null){
      if(listNotes != null){
        MainActivity.sortByDate((ArrayList) listNotes);
        listNotesAdapter = new ListNotesAdapter(getContext(), listNotes);
        listNotesAdapter.setOnNoteSelectedListener(onNoteSelectedListener);
        listViewNotes.setAdapter(listNotesAdapter);
      }
    }
    super.onStart();
  }
   @Override
   public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
       inflater.inflate(R.menu.menu_day_fragment, menu);
     MenuItem searchItem = menu.findItem(R.id.app_bar_search);
     SearchView searchView = (SearchView) searchItem.getActionView();
     searchView.setOnQueryTextListener(new PerformSearch(getContext(), this));
       super.onCreateOptionsMenu(menu, inflater);
   }

  @Override public boolean onOptionsItemSelected(@NonNull MenuItem item) {

    if(item.getItemId() == R.id.app_bar_add){
      //add new note into adapter, and open CreateNoteFragment
      onNoteSelectedListener.onNoteSelected(null); //null - need for insert instead of update
      return true;
    } else if (item.getItemId() == R.id.app_bar_sort) {
      tvScoreboard.setText(R.string.tv_scoreboard_search);
      ArrayList<Category> categories = new DaoCategories(getContext()).getCategories();
      ListCategoriesAdapter listCategoriesAdapter = new ListCategoriesAdapter(getContext(), categories);
      listViewNotes.setAdapter(listCategoriesAdapter);

      listViewNotes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
          tvScoreboard.setText("Все заметки выбранной категории");
          Category category = (Category) adapterView.getItemAtPosition(i);
          ArrayList<Note> notes = daoNotes.getByCategory(category);
          MainActivity.sortByDate(notes);
          listNotesAdapter = new ListNotesAdapter(getContext(), notes);
          listNotesAdapter.setOnNoteSelectedListener(onNoteSelectedListener);
          listViewNotes.setAdapter(listNotesAdapter);
          listViewNotes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
              Object object = adapterView.getItemAtPosition(i);
              if(object instanceof Note){
                Note note = (Note) object;
                onNoteSelectedListener.onNoteSelected(note);
              }
            }
          });
        }
      });
    }else if(item.getItemId() == R.id.app_bar_search){

    }
    return super.onOptionsItemSelected(item);
  }

  private String setDateOnScoreboard(){
    SimpleDateFormat sdfDate = new SimpleDateFormat("dd.MM.yyyy  EEEE", Locale.getDefault());
    return sdfDate.format(calendar.getTime());
  }

  public void setCalendar(Calendar calendar) {
    this.calendar = calendar;
  }



}
