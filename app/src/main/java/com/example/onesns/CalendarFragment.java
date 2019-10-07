package com.example.onesns;


import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.applikeysolutions.cosmocalendar.view.CalendarView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import xyz.hasnat.sweettoast.SweetToast;


/**
 * A simple {@link Fragment} subclass.
 */
public class CalendarFragment extends Fragment {
    CalendarView calendarView;

    // firebase
    DatabaseReference getcalendarDatabaseReference;
    FirebaseAuth mAuth;
    StorageReference mCalendar;
    FloatingActionButton button_add;
    DatabaseReference rootReference;
    List<CalendarItem> dataList = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        // firebase
        rootReference = FirebaseDatabase.getInstance().getReference();

        mAuth = FirebaseAuth.getInstance();
        String user_id = mAuth.getCurrentUser().getUid();
        getcalendarDatabaseReference = FirebaseDatabase.getInstance().getReference().child("calendar").child(user_id);
        String myRef = getcalendarDatabaseReference.getKey();
        RecyclerView recyclerView = view.findViewById(R.id.recyclerview);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        FirebaseDatabase.getInstance().getReference().child("calendar").child(user_id)
                .addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists()) {
                    for(int i =0;  i < dataSnapshot.getChildrenCount()-2; i++) {
                        String title = (String) dataSnapshot.child("title").getValue();
                        String detail = (String) dataSnapshot.child("detail").getValue();
                        String days = (String) dataSnapshot.child("days").getValue();
//                        Toast.makeText(getContext(), title + "\n" + detail + "\n" + days, Toast.LENGTH_SHORT).show();

//                    HashMap<String, Object> calendar_body = new HashMap<>();
//                    calendar_body.put("title", dataSnapshot.child("cal_title").getValue());
//                    calendar_body.put("detail", dataSnapshot.child("cal_detail").getValue());
//                    calendar_body.put("days", dataSnapshot.child("days").getValue());


                        dataList.add(new CalendarItem(title, detail, days));
                        CalendarAdapter adapter = new CalendarAdapter(dataList);
                        recyclerView.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        calendarView = view.findViewById(R.id.calendarView);
        button_add = view.findViewById(R.id.button_add);
        calendarView.dispatchSetSelected(false);
        button_add.setOnClickListener(view1 -> {
            List<java.util.Calendar> days = calendarView.getSelectedDates();

            String result = "";
            for (int i = 0; i < days.size(); i++) {
                java.util.Calendar calendar = days.get(i);
                @SuppressLint("WrongConstant") final int day = calendar.get(Calendar.DAY_OF_MONTH);
                @SuppressLint("WrongConstant") final int month = calendar.get(Calendar.MONTH);
                @SuppressLint("WrongConstant") final int year = calendar.get(Calendar.YEAR);
                String week = new SimpleDateFormat("EE").format(calendar.getTime());
                String day_full = year + "년 " + (month + 1) + "월 " + day + "일 " + week + "요일";
                result += (day_full);
            }
            if(result.equals("")) {
                SweetToast.error(getContext(), "날짜를 선택해 주세요.");
            }else {
                CalendarDialog calendarDialog = new CalendarDialog(getActivity());
                calendarDialog.save();

                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(Objects.requireNonNull(getActivity()));
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("day", result);
                editor.apply();
            }
        });

        return view;

    }


}
