package com.example.onesns;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.onesns.chatHome.ChatMainActivity;
import com.example.onesns.profileSetting.ProfileActivity;
import com.google.android.material.navigation.NavigationView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.bottom_navigation)
    com.google.android.material.bottomnavigation.BottomNavigationView BottomNavigationView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    // controller
    public NavController navController;

    @RequiresApi(api = Build.VERSION_CODES.P)
    @SuppressLint("ResourceAsColor")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    // 상단 툴바메뉴 클릭 이벤트
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.set_profile:
                Intent i = new Intent(this, ProfileActivity.class);
                startActivity(i);
                return true;
            case R.id.chat:
                Intent intent_cat = new Intent(this, ChatMainActivity.class);
                startActivity(intent_cat);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // 상태표시줄 색상 변경
        View view = getWindow().getDecorView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 23 버전 이상일 때 상태바 하얀 색상, 회색 아이콘
            view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(Color.parseColor("#f2f2f2"));
        } else if (Build.VERSION.SDK_INT >= 21) {
            // 21 버전 이상일 때 상태바 검은 색상, 흰색 아이콘
            getWindow().setStatusBarColor(Color.BLACK);
        }

        setupNavigation();
    }

    private void setupNavigation() {
        setSupportActionBar(toolbar); // 기본 액션바를 custom toolbar 로 사용한다.
        // controller를 사용하여 main fragment 랑 연결
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(BottomNavigationView, navController);
        // drawerlayout 안에 아이템을 클리 시 이벤트
        BottomNavigationView.setOnNavigationItemSelectedListener(this::onNavigationItemSelected);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        item.setChecked(true); // 아이템 클릭에 대해 허용

        int id = item.getItemId(); // 아이템별 id 값

        switch (id) {
            case R.id.action_main: // 메인화면
                navController.navigate(R.id.mainFragment);
                break;
            case R.id.action_calendar: // 캘린더
                navController.navigate(R.id.calendarFragment);
                break;
            case R.id.action_search: // 친구
                navController.navigate(R.id.searchFragment);
                break;
            case R.id.action_setting: // 설정
                navController.navigate(R.id.settingFragment);
                break;
        }




        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(Navigation.findNavController(this, R.id.nav_host_fragment), drawerLayout);
    }
}
