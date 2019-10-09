package com.example.onesns;


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


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

    FirebaseAuth mAuth;
    DatabaseReference rootReference;
    List<FeedItem> feed_dataList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        // firebase
        rootReference = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        String user_id = mAuth.getCurrentUser().getUid();

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);

        RecyclerView feed_recyclerView = view.findViewById(R.id.feed_recycler_view);
        RecyclerView.LayoutManager feed_layoutManager = new LinearLayoutManager(getContext());
        feed_recyclerView.setLayoutManager(feed_layoutManager);
        feed_recyclerView.setLayoutManager(mLayoutManager);

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

                            feed_dataList.add(new FeedItem(title, detail, days, user_name, postTime));
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


        return view;
    }

}
