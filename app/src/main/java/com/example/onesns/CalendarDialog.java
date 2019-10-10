package com.example.onesns;


import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

import xyz.hasnat.sweettoast.SweetToast;

public class CalendarDialog {
    private Context context;

    public CalendarDialog(Context context) {
        this.context = context;
    }

    DatabaseReference storeDefaultDatabaseReference;
    DatabaseReference getUserDatabaseReference;
    FirebaseAuth mAuth;

    // 호출할 다이얼로그 함수를 정의한다.
    public void save() {

        // 커스텀 다이얼로그를 정의하기위해 Dialog클래스를 생성한다.
        final Dialog dlg = new Dialog(context);
        // 액티비티의 타이틀바를 숨긴다.
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 커스텀 다이얼로그의 레이아웃을 설정한다.
        dlg.setContentView(R.layout.calendar_dialog);

        // 커스텀 다이얼로그를 노출한다.
        dlg.show();

        // 커스텀 다이얼로그의 각 위젯들을 정의한다.
        EditText title = dlg.findViewById(R.id.title);
        EditText detail = dlg.findViewById(R.id.detail);
        final Button okButton = dlg.findViewById(R.id.okButton);
        final Button cancelButton = dlg.findViewById(R.id.cancelButton);

        okButton.setOnClickListener(view -> {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            String cal_days = prefs.getString("day", "");
            String post_day = prefs.getString("postTime", "");
            String cal_title = title.getText().toString();
            String cal_detail = detail.getText().toString();

            // firebase
            mAuth = FirebaseAuth.getInstance();
            String user_uid = mAuth.getCurrentUser().getUid();

            getUserDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(user_uid);
            getUserDatabaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                    String name = (String) dataSnapshot.child("user_name").getValue();
                    String user_image = (String) dataSnapshot.child("user_image").getValue();

                    HashMap<String, Object> calendar_body = new HashMap<>();
                    calendar_body.put("postTime", post_day);
                    calendar_body.put("title", cal_title);
                    calendar_body.put("detail", cal_detail);
                    calendar_body.put("days", cal_days);
                    calendar_body.put("user_uid", user_uid);
                    calendar_body.put("nickname", name);
                    calendar_body.put("user_image", user_image);
                    FirebaseDatabase.getInstance().getReference().child("calendar").push().setValue(calendar_body);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) { }
            });


            SweetToast.success(context, "일정을 추가 하였습니다..");

            // 커스텀 다이얼로그를 종료한다.
            dlg.dismiss();
        });
        cancelButton.setOnClickListener(view -> {
            SweetToast.error(context, "취소 했습니다.");
            dlg.dismiss();
        });
    }
}
