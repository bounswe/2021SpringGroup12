package com.group12.beabee.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.group12.beabee.R;
import com.group12.beabee.models.requests.LoginRequest;
import com.group12.beabee.models.requests.SignUpRequest;
import com.group12.beabee.models.responses.LoginResponse;
import com.group12.beabee.models.responses.SignUpResponse;
import com.group12.beabee.network.BeABeeService;
import com.group12.beabee.network.ServiceAPI;
import com.group12.beabee.views.MainPage.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.et_name)
    EditText etUsername;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.et_email)
    EditText etEmail;

    private ServiceAPI serviceAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        serviceAPI = BeABeeService.serviceAPI;
    }

    @OnClick(R.id.btn_login)
    public void OnClickLogin(){
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.username = etUsername.getText().toString();
        loginRequest.email = etEmail.getText().toString();
        loginRequest.password = etPassword.getText().toString();

        serviceAPI.loginRequest(loginRequest).enqueue(new Callback<LoginResponse>() {

            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()){
                    //login to the main page
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(LoginActivity.this, "Something is wrong please try again later!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @OnClick(R.id.btn_go_sign)
    public void GoToSignUpPage(){
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }
}