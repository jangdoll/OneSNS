package com.example.onesns;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import xyz.hasnat.sweettoast.SweetToast;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.edt_id)
    TextInputEditText edtId;
    @BindView(R.id.edt_pw)
    TextInputEditText edtPw;
    @BindView(R.id.btn_sign_in)
    MaterialButton btnSignIn;
    @BindView(R.id.btn_login)
    MaterialButton btnLogin;
    @BindView(R.id.btn_google_sign_in)
    SignInButton btnGoogleSignIn;

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private DatabaseReference storeDefaultDatabaseReference;
    private DatabaseReference userDatabaseReference;

    // Google sign in
    private static final int RC_SIGN_IN = 100;
    private GoogleSignInClient mGoogleSignInClient;

    //progressDialog
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        // firebase
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        userDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users");

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

//        getSupportActionBar().setTitle("로그인"); // toolbar 타이틀 바꾸기

        btnGoogleSignIn.setSize(SignInButton.SIZE_WIDE); // 구글 로그인 버튼 테마

        // configure google sign in
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        //FirebaseAuth 객체의 공유 인스턴스를 가져오기
        mAuth = FirebaseAuth.getInstance();
    }

    @OnClick({R.id.btn_sign_in, R.id.btn_login, R.id.btn_google_sign_in})
    public void onViewClicked(View view) {
        // 이메일, 비밀번호 텍스트 값
        String email = edtId.getText().toString();
        String password = edtPw.getText().toString();

        switch (view.getId()) {
            case R.id.btn_sign_in: // 회원가입 버튼
                registerAccount(email,password);
                break;
            case R.id.btn_login: // 로그인 버튼
                loginAccount(email,password);
                break;
//            case R.id.btn_google_sign_in: // 구글 로그인 버튼
//                googleSignIn();
//                break;
        }
    }

    //구글 로그인 logic
//    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
//        Log.d("login", "firebaseAUthWithGoogle : " + acct.getId());
//        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
//        progressDialog = new ProgressDialog(LoginActivity.this);
//        progressDialog.setMessage("로그인 중입니다..");
//        progressDialog.show();
//        progressDialog.setCanceledOnTouchOutside(false);
//        //구글 로그인
//        mAuth.signInWithCredential(credential)
//                .addOnCompleteListener(this, task -> {
//                   if (task.isSuccessful()) {
//                       progressDialog.dismiss();
//                       Toast.makeText(getApplicationContext(), "구글 로그인 성공", Toast.LENGTH_SHORT).show();
//                       Intent i = new Intent(this, MainActivity.class);
//                       startActivity(i);
//                       finish();
//                   }else{
//                       progressDialog.dismiss();
//                       Toast.makeText(getApplicationContext(), "구글 로그인 실패", Toast.LENGTH_SHORT).show();
//                   }
//                });
//    }
    //구글 로그인
//    private void googleSignIn() {
//        Intent intent = mGoogleSignInClient.getSignInIntent();
//        startActivityForResult(intent, RC_SIGN_IN);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode == RC_SIGN_IN) {
//            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
//            try {
//                GoogleSignInAccount account = task.getResult(ApiException.class);
//                assert account != null;
//                firebaseAuthWithGoogle(account);
//            } catch (ApiException e) {
//                Log.w("login", "구글 로그인 실패", e);
//            }
//        }
//    }

    //  회원가입 logic
    private void registerAccount(String email, String password) {
        if(TextUtils.isEmpty(email)){ // 이메일 입력 후 확인 이벤트 처리
            Toast.makeText(this, "이메일을 입력해주세요", Toast.LENGTH_SHORT).show();
        }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "이메일 형식이 아닙니다.", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(password)){ // 패스워드 입력 후 확인 이벤트 처리
            Toast.makeText(this, "패스워드을 입력해주세요", Toast.LENGTH_SHORT).show();
        }else if(password.length() < 6){
            Toast.makeText(this, "패스워드을 6자리 이상 입력해주세요", Toast.LENGTH_SHORT).show();
        }else if(password.length() > 15){
            Toast.makeText(this, "패스워드을 15자리 이하로 입력해주세요", Toast.LENGTH_SHORT).show();
        }else{
            // create user
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            String deviceToken = FirebaseInstanceId.getInstance().getToken();
                            String name = "OneSNS user";

                            // get and link storage
                            String current_userID = mAuth.getCurrentUser().getUid();
                            storeDefaultDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(current_userID);
                            storeDefaultDatabaseReference.child("user_name").setValue(name);
                            storeDefaultDatabaseReference.child("verified").setValue("false");
                            storeDefaultDatabaseReference.child("search_name").setValue(name.toLowerCase());
                            storeDefaultDatabaseReference.child("user_email").setValue(email);
                            storeDefaultDatabaseReference.child("user_nickname").setValue("Welcome to OneSNS");
                            storeDefaultDatabaseReference.child("user_gender").setValue("");
                            storeDefaultDatabaseReference.child("user_country").setValue("KR");
                            storeDefaultDatabaseReference.child("created_at").setValue(ServerValue.TIMESTAMP);
                            storeDefaultDatabaseReference.child("user_status").setValue("Hi, OneSNS");
                            storeDefaultDatabaseReference.child("user_image").setValue("default_image"); // Original image
                            storeDefaultDatabaseReference.child("device_token").setValue(deviceToken);
                            storeDefaultDatabaseReference.child("user_thumb_image").setValue("default_image")
                                    .addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            // SENDING VERIFICATION EMAIL TO THE REGISTERED USER'S EMAIL
                                            user = mAuth.getCurrentUser();
                                            if (user != null) {
                                                user.sendEmailVerification()
                                                        .addOnCompleteListener(task11 -> {
                                                            if (task11.isSuccessful()) {

                                                                registerSuccessPopUp();

                                                                // LAUNCH activity after certain time period
                                                                new Timer().schedule(new TimerTask() {
                                                                    public void run() {
                                                                        LoginActivity.this.runOnUiThread(() -> {
                                                                            mAuth.signOut();

                                                                            Intent mainIntent = new Intent(getApplicationContext(), LoginActivity.class);
                                                                            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                                            startActivity(mainIntent);
                                                                            finish();
                                                                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                                                            SweetToast.info(getApplicationContext(), getString(R.string.authenticate_email));
                                                                        });
                                                                    }
                                                                }, 8000);
                                                            } else {
                                                                mAuth.signOut();
                                                            }
                                                        });
                                            }
                                        }
                                    });
                        } else {
                            String message = task.getException().getMessage();
                            SweetToast.error(getApplicationContext(), "Error : " + message);
                        }
                        progressDialog.dismiss();
                    });
            progressDialog = new ProgressDialog(LoginActivity.this);
            progressDialog.setMessage("회원가입 중입니다..");
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(false);

            // 이메일, 패스워드 회원가입
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
                if(task.isSuccessful()){
                    progressDialog.dismiss(); // 완료 되었을때 progressDialog 제거
                    Toast.makeText(this, "회원가입이 완료 되었습니다.", Toast.LENGTH_SHORT).show();
                }else{
                    progressDialog.dismiss();
                    Toast.makeText(this, "회원가입이 실패했습니다.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    /*
     *  회원가입 성공 후 팝업 띄움
     * */
    private void registerSuccessPopUp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        View view = LayoutInflater.from(LoginActivity.this).inflate(R.layout.register_success_popup, null);

        builder.setCancelable(false);

        builder.setView(view);
        builder.show();
    }

    /*
     *  이메일 인증 확인
     * */
    private void checkVerifiedEmail() {
        user = mAuth.getCurrentUser();
        boolean isVerified = false;
        if (user != null) {
            isVerified = user.isEmailVerified();
        }
        if (isVerified) {
            String UID = mAuth.getCurrentUser().getUid();
            userDatabaseReference.child(UID).child("verified").setValue("true");

            Intent intent_login = new Intent(LoginActivity.this, MainActivity.class);
            intent_login.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent_login);
            finish();
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

        } else {
            SweetToast.info(LoginActivity.this, getString(R.string.not_authenticated_email));
            mAuth.signOut();
        }
    }

    // 로그인 logic
    private void loginAccount(String email, String password) {
        if(TextUtils.isEmpty(email)) {
            Toast.makeText(this, "이메일을 입력해주세요.", Toast.LENGTH_SHORT).show();
        }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(this, "이메일 형식이 아닙니다.", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(password)){ // 패스워드 입력 후 확인 이벤트 처리
            Toast.makeText(this, "패스워드을 입력해주세요", Toast.LENGTH_SHORT).show();
        }else if(password.length() < 6){
            Toast.makeText(this, "패스워드을 6자리 이상 입력해주세요", Toast.LENGTH_SHORT).show();
        }else if(password.length() > 15){
            Toast.makeText(this, "패스워드을 15자리 이하로 입력해주세요", Toast.LENGTH_SHORT).show();
        }else{
            progressDialog = new ProgressDialog(LoginActivity.this);
            progressDialog.setMessage("로그인 중입니다..");
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(false);

            // 이메일, 패스워드 로그인
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    // these lines for taking DEVICE TOKEN for sending device to device notification
                    String userUID = mAuth.getCurrentUser().getUid();
                    String userDeviceToken = FirebaseInstanceId.getInstance().getToken();
                    userDatabaseReference.child(userUID).child("device_token").setValue(userDeviceToken)
                            .addOnSuccessListener(aVoid -> checkVerifiedEmail());

                } else {
                    SweetToast.error(LoginActivity.this, getString(R.string.wrong_emailPassword));
                }

                        progressDialog.dismiss();
            });
        }
    }

    //자동로그인
//    @Override
//    protected void onStart() {
//        super.onStart();
//        mUser = mAuth.getCurrentUser();
//        if (!(mUser == null)) { // 유저 장보가 있다면 바로 MainActivity로 이동
//            Intent i = new Intent(getApplicationContext(), MainActivity.class);
//            startActivity(i);
//            finish();
//        }
//    }

    // editText clearFocus [화면 클릭시 키보드 숨기기]
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    assert imm != null;
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }
}
