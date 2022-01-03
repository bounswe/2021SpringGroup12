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

public class LoginActivityForgotPage extends AppCompatActivity {

    @BindView(R.id.et_email)
    EditText etEmail;

    private ServiceAPI serviceAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_reset_password);
        ButterKnife.bind(this);
        serviceAPI = BeABeeService.serviceAPI;
    }

    @OnClick(R.id.btn_go_sign)
    public void OnClickLogin(){
        String email = etEmail.getText().toString();

        if (!InputValidator.IsTextNonEmpty(email) || !InputValidator.IsTextEmailFormat(email)) {
            Utils.ShowErrorToast(this, "Either username or a valid email should be provided!");
            return;
        }
        Utils.showLoading(getSupportFragmentManager());
        serviceAPI.getForgotpassword(email).enqueue(new Callback<BasicResponse>() {

            @Override
            public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {
                Utils.dismissLoading();
                if (response.isSuccessful() && response.body() != null && response.body().messageType.equals("INFO")){
                    Intent intent = new Intent(LoginActivityForgotPage.this, LoginActivityTokenPage.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(LoginActivityForgotPage.this, response.body().message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BasicResponse> call, Throwable t) {
                Utils.dismissLoading();
                Toast.makeText(LoginActivityForgotPage.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @OnClick(R.id.btn_go_back)
    public void GoToSignUpPage(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}