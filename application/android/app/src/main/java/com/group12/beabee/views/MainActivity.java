package com.group12.beabee.views;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.group12.beabee.R;
import com.group12.beabee.models.requests.SignUpRequest;
import com.group12.beabee.models.responses.SignUpResponse;
import com.group12.beabee.network.BeABeeService;
import com.group12.beabee.network.ServiceAPI;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private ServiceAPI serviceAPI;
    private TextView tvMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvMain = findViewById(R.id.tv_main);
        serviceAPI = BeABeeService.serviceAPI;


        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.username = "berk";
        signUpRequest.email = "eb@sad.com";
        signUpRequest.password = "1234";
        serviceAPI.signUpRequest(signUpRequest).enqueue(new Callback<SignUpResponse>() {
            @Override
            public void onResponse(Call<SignUpResponse> call, Response<SignUpResponse> response) {
                tvMain.setText(response.body().message);
            }

            @Override
            public void onFailure(Call<SignUpResponse> call, Throwable t) {
                tvMain.setText(t.getMessage());
            }
        });
    }
}