package com.mycompany.sonatafinance.activities;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.os.Build;
import android.widget.CalendarView;
import android.widget.TextView;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mycompany.sonatafinance.R;
import com.mycompany.sonatafinance.adapters.ListCategoriesAdapter;
import com.mycompany.sonatafinance.database.Category;
import com.mycompany.sonatafinance.database.Note;
import com.mycompany.sonatafinance.fragments.CalendarFragment;
import com.mycompany.sonatafinance.fragments.CreateNoteFragment;
import com.mycompany.sonatafinance.fragments.DayFragment;
import com.mycompany.sonatafinance.fragments.PickCategoryFragment;
import com.mycompany.sonatafinance.fragments.PrintFragment;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements CalendarView.OnDateChangeListener, DayFragment.OnNoteSelectedListener,
   CreateNoteFragment.OnReadyButtonListener {

  private BottomNavigationView bottomNavigationView;
  private TextView tvScoreboard;

  @RequiresApi(api = Build.VERSION_CODES.O) @SuppressLint("CommitTransaction") @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);


    //set color status bar
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      getWindow().setStatusBarColor(getResources().getColor(R.color.status_bar, this.getTheme()));
    }

    bottomNavigationView = findViewById(R.id.bottomNavigationView);
    bottomNavigationView.inflateMenu(R.menu.menu_bottom_navigation);
    bottomNavigationView.setOnItemSelectedListener(item -> {
      Fragment selectedFragment = null;

      if(item.getItemId() == R.id.menu_bottom_navigation_bt_day){
        selectedFragment = new DayFragment();
      } else if(item.getItemId() == R.id.menu_bottom_navigation_bt_calendar){
        selectedFragment = new CalendarFragment();
      } else if (item.getItemId() == R.id.menu_bottom_navigation_bt_print) {
        selectedFragment = new PrintFragment();
      }

      if(selectedFragment != null){
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, selectedFragment).commit();
      }
      return true;
    });

    getSupportFragmentManager().beginTransaction().add(R.id.fragmentContainerView, new DayFragment());
  }

  @Override
  public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
    Calendar calendar = Calendar.getInstance();
    calendar.set(Calendar.YEAR, year);
    calendar.set(Calendar.MONTH, month);
    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
    DayFragment dayFragment = new DayFragment();
    dayFragment.setCalendar(calendar);
    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, dayFragment).commit();
  }

  @Override
  public void onNoteSelected(Note note) {
    if(note == null){
      getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new CreateNoteFragment()).commit();
    } else {
      CreateNoteFragment createNoteFragment = new CreateNoteFragment();
      createNoteFragment.setNote(note);
      getSupportFragmentManager().beginTransaction()
          .replace(R.id.fragmentContainerView, createNoteFragment)
          .addToBackStack(null)
          .commit();
    }
  }

  @Override public void onReady() {
    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new DayFragment()).commit();
  }

  public static void sortByDate(ArrayList<Note> list){
    list.sort((o1, o2) -> {
      if (o1.getDate() > o2.getDate()) {
        return -1;
      }
      if (o1.getDate() < o2.getDate()) {
        return 1;
      }
      return 0;
    });
  }
}