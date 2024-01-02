package com.mycompany.sonatafinance.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.mycompany.sonatafinance.activities.MainActivity;
import com.mycompany.sonatafinance.R;
import java.util.Calendar;

public class CalendarFragment extends Fragment {

  private CalendarView calendarView;
  Calendar selectedCalendar = null;
  public CalendarFragment(){

  }

  @Nullable @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_calendar, container, false);

    MainActivity mainActivity = (MainActivity)getActivity();

    calendarView = view.findViewById(R.id.calendarView);
    calendarView.setOnDateChangeListener(mainActivity);
    return view;
  }
}
