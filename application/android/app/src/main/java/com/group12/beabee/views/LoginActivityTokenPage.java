package com.group12.beabee.views;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.group12.beabee.BeABeeApplication;
import com.group12.beabee.InputValidator;
import com.group12.beabee.R;
import com.group12.beabee.Utils;
import com.group12.beabee.models.requests.LoginRequest;
import com.group12.beabee.models.responses.BasicResponse;
import com.group12.beabee.models.responses.LoginResponse;
import com.group12.beabee.network.BeABeeService;
import com.group12.beabee.network.ServiceAPI;
import com.group12.beabee.views.MainStructure.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivityTokenPage extends AppCompatActivity {

    @BindView(R.id.et_name)
    EditText etUsername;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.et_email)
    EditText etEmail;
    @BindView(R.id.et_token)
    EditText ettoken;

    private ServiceAPI serviceAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_reset_password_2);
        ButterKnife.bind(this);
        serviceAPI = BeABeeService.serviceAPI;
    }

    @OnClick(R.id.btn_go_sign)
    public void OnClickLogin(){
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.username = etUsername.getText().toString();
        loginRequest.email = etEmail.getText().toString();
        loginRequest.password = etPassword.getText().toString();
        String token= ettoken.getText().toString();
        if (!InputValidator.IsTextNonEmpty(loginRequest.username) && !InputValidator.IsTextEmailFormat(loginRequest.email)) {
            Utils.ShowErrorToast(this, "Either username or a valid email should be provided!");
            return;
        }
        Utils.showLoading(getSupportFragmentManager());
        serviceAPI.postMailToken(token,loginRequest).enqueue(new Callback<BasicResponse>() {
            @Override
            public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {
                Utils.dismissLoading();
                if (response.isSuccessful() && response.body() != null && response.body().messageType.equals("SUCCESS")){
                    Intent intent = new Intent(LoginActivityTokenPage.this, LoginActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(LoginActivityTokenPage.this, "Something is wrong please try again later!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BasicResponse> call, Throwable t) {
                Utils.dismissLoading();
                Toast.makeText(LoginActivityTokenPage.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @OnClick(R.id.btn_go_back)
    public void GoToSignUpPage(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}