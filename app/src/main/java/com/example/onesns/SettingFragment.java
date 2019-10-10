package com.example.onesns;


import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFragment extends Fragment {
    //firebase
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    TextView tv_email;
    Switch myswitch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        SharedPreferences sharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences("change_theme", MODE_PRIVATE);
        if (sharedPreferences.getBoolean("dark_theme", false)) {
            getActivity().setTheme(R.style.darktheme);
        } else {
            getActivity().setTheme(R.style.AppTheme);
        }

        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        mAuth = FirebaseAuth.getInstance(); //firebase 인스턴스 가져오기
        tv_email = view.findViewById(R.id.tv_email);


        //사용자 정보 가져오기
        currentUser = mAuth.getCurrentUser(); // 현재 로그인 되어있는 사용자의 정보

        tv_email.setText(currentUser.getEmail());

        myswitch = view.findViewById(R.id.myswitch);
        // 테마
        if (String.valueOf(sharedPreferences.getBoolean("dark_theme", false)).equals("true")) {
            myswitch.setChecked(true);
        } else {
            myswitch.setChecked(false);
        }
        myswitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("dark_theme", isChecked);
            editor.apply();
            handleDarkMode(sharedPreferences.getBoolean("dark_theme", false));
        });
        return view;
    }

    private void handleDarkMode(boolean active) {
        // 새로고침
        Intent i = getActivity().getIntent();
        if (active) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            // 새로고침
            getActivity().finish();
            startActivity(i);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            // 새로고침
            getActivity().finish();
            startActivity(i);
        }
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
