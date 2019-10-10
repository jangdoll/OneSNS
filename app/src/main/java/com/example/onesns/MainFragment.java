package com.example.onesns;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

    FirebaseAuth mAuth;
    DatabaseReference rootReference, userReferce;
    List<FeedItem> feed_dataList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences("change_theme", MODE_PRIVATE);
        if (sharedPreferences.getBoolean("dark_theme", false)) {
            getActivity().setTheme(R.style.darktheme);
        } else {
            getActivity().setTheme(R.style.AppTheme);
        }
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        // firebase
        rootReference = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        String user_uid = mAuth.getCurrentUser().getUid();

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);

        RecyclerView feed_recyclerView = view.findViewById(R.id.feed_recycler_view);
        RecyclerView.LayoutManager feed_layoutManager = new LinearLayoutManager(getContext());
        feed_recyclerView.setLayoutManager(feed_layoutManager);
        feed_recyclerView.setLayoutManager(mLayoutManager);


//        FirebaseDatabase.getInstance().getReference().child("friends").orderByChild(user_uid).equalTo(user_uid)
//                .addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
//
//                DatabaseReference uid = dataSnapshot.getRef();
//

                FirebaseDatabase.getInstance().getReference().child("calendar")
                        .addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                if (dataSnapshot.exists()) {
                                    String title = (String) dataSnapshot.child("title").getValue();
                                    String detail = (String) dataSnapshot.child("detail").getValue();
                                    String days = (String) dataSnapshot.child("days").getValue();
                                    String user_name = (String) dataSnapshot.child("nickname").getValue();
                                    String postTime = (String) dataSnapshot.child("postTime").getValue();
                                    String user_image = (String) dataSnapshot.child("user_image").getValue();

                                    feed_dataList.add(new FeedItem(title, detail, days, user_name, postTime, user_image));
                                    FeedAdapter feed_adapter = new FeedAdapter(feed_dataList);
                                    feed_recyclerView.setAdapter(feed_adapter);
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
//            }

//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

        return view;
    }

}
