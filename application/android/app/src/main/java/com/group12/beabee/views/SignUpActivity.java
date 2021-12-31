package com.group12.beabee.views;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.group12.beabee.InputValidator;
import com.group12.beabee.R;
import com.group12.beabee.Utils;
import com.group12.beabee.models.requests.SignUpRequest;
import com.group12.beabee.models.responses.SignUpResponse;
import com.group12.beabee.network.BeABeeService;
import com.group12.beabee.network.ServiceAPI;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {

    @BindView(R.id.et_name)
    EditText etUsername;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.et_password_repeat)
    EditText etPasswordRepeat;
    @BindView(R.id.et_email)
    EditText etEmail;

    private ServiceAPI serviceAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);
        serviceAPI = BeABeeService.serviceAPI;
    }

    @OnClick(R.id.btn_signup)
    public void OnClickSignUp(){
        String password = etPassword.getText().toString();
        String password2 = etPasswordRepeat.getText().toString();
        if (!password2.equals(password)){
            Toast.makeText(this, "Make sure your passwords match!!", Toast.LENGTH_LONG).show();
            return;
        }

        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.username = etUsername.getText().toString();
        signUpRequest.email = etEmail.getText().toString();
        signUpRequest.password = password;

        if (!InputValidator.IsTextNonEmpty(signUpRequest.username)){
            Utils.ShowErrorToast(this, "Username must not be empty!!");
            return;
        }

        if (!InputValidator.IsTextNonEmpty(signUpRequest.password)){
            Utils.ShowErrorToast(this, "Password must not be empty!!");
            return;
        }

        if (!InputValidator.IsTextEmailFormat(signUpRequest.email)){
            Utils.ShowErrorToast(this, "Email should be in correct email format!!");
            return;
        }

        Utils.showLoading(getSupportFragmentManager());
        serviceAPI.signUpRequest(signUpRequest).enqueue(new Callback<SignUpResponse>() {

            @Override
            public void onResponse(Call<SignUpResponse> call, Response<SignUpResponse> response) {
                Utils.dismissLoading();
                Toast.makeText(SignUpActivity.this, response.body().message, Toast.LENGTH_LONG).show();
                finish();
            }

            @Override
            public void onFailure(Call<SignUpResponse> call, Throwable t) {
                Utils.dismissLoading();
                Toast.makeText(SignUpActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }
}