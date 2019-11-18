package com.hesham.sawarstudent.ui.splash;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.hesham.sawarstudent.R;
import com.hesham.sawarstudent.data.response.CustomResponse;
import com.hesham.sawarstudent.networkmodule.Apiservice;
import com.hesham.sawarstudent.ui.home.HomeActivity;
import com.hesham.sawarstudent.ui.login.LoginActivity;
import com.hesham.sawarstudent.utils.PrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {

    PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                openActivity();
            }
        }, 1800);
        prefManager = new PrefManager(this);
// Get token
        // [START retrieve_current_token]
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("token", "getInstanceId failed", task.getException());
                            sendRegistrationToServer(prefManager.getNotificationToken());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                        prefManager.setNotificationToken(token);
                        sendRegistrationToServer(token);
                        // Log and toast R.string.msg_token_fmt
//                        String msg = getString(, token);
//                        Log.d(TAG, msg);
//                        Toast.makeText(NotificationActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void sendRegistrationToServer(String token) {
        Call<CustomResponse> call = Apiservice.getInstance().apiRequest.pushNotificationToken(token , prefManager.getStudentData() ==null ?0:prefManager.getStudentData().getId());
        call.enqueue(new Callback<CustomResponse>() {
            @Override
            public void onResponse(Call<CustomResponse> call, Response<CustomResponse> response) {
                if (response.body() != null) {
                    if (response.body().status) {
                    }
                }
            }

            @Override
            public void onFailure(Call<CustomResponse> call, Throwable t) {
                Log.d("tag", "articles total result:: " + t.getMessage());

            }
        });

    }
    public void openActivity() {
//        prefManager.getToken();
        if (prefManager.getToken().equals("")) {
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
            finish();
        } else {
            Intent i = new Intent(this, HomeActivity.class);
            startActivity(i);
            finish();
        }
    }
}