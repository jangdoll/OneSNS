package com.example.onesns;


import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFragment extends Fragment {
    //firebase
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    public SettingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        mAuth = FirebaseAuth.getInstance(); //firebase 인스턴스 가져오기

        //사용자 정보 가져오기
        currentUser = FirebaseAuth.getInstance().getCurrentUser(); // 현재 로그인 되어있는 사용자의 정보

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MaterialButton btn_logout_draw = view.findViewById(R.id.btn_logout);
        btn_logout_draw.setOnClickListener(view1 -> {
            final AlertDialog.Builder alt_logout = new AlertDialog.Builder(getActivity()); // alterDialog 생성
            alt_logout.setMessage("로그아웃 하시겠습니까?")
                    .setCancelable(false) // cancel 안되게
                    .setPositiveButton("네", ((dialogInterface, i) -> signOut()))
                    .setNegativeButton("아니오", ((dialogInterface, i) -> dialogInterface.cancel()));
            AlertDialog alertDialog = alt_logout.create();
            alertDialog.setTitle("로그아웃");
            alertDialog.show();
        });
    }

    // 로그아웃
    private void signOut() {
        mAuth.signOut();
        Intent intent_login = new Intent(getActivity(),LoginActivity.class);
        startActivity(intent_login);
    }
}
