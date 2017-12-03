package com.example.formandocodigo.psicotimes.login.view;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.example.formandocodigo.psicotimes.R;
import com.example.formandocodigo.psicotimes.login.net.entity.ApiError;
import com.example.formandocodigo.psicotimes.login.net.entity.RegisterResponse;
import com.example.formandocodigo.psicotimes.login.net.ApiService;
import com.example.formandocodigo.psicotimes.login.net.RetrofitBuilder;
import com.example.formandocodigo.psicotimes.login.repository.LoginActivityRepositoryImpl;
import com.example.formandocodigo.psicotimes.utils.Utils;
import com.example.formandocodigo.psicotimes.view.MainActivity;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements LoginActivityView {

    private static final String TAG = "LoginActivity";

    @BindView(R.id.txt_name)
    TextInputLayout txtName;
    @BindView(R.id.txt_email)
    TextInputLayout txtEmail;
    @BindView(R.id.txt_password)
    TextInputLayout txtPassword;

    AwesomeValidation validator;

    LoginActivityRepositoryImpl repository;

    ApiService service;
    Call<RegisterResponse> call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

        repository = new LoginActivityRepositoryImpl(this);

        if (repository.isExistsPreferences()) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }
        service = RetrofitBuilder.createService(ApiService.class);
        validator = new AwesomeValidation(ValidationStyle.TEXT_INPUT_LAYOUT);
        setupRules();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (call != null)  {
            call.cancel();
            call = null;
        }
    }

    @OnClick(R.id.btn_start)
    void onButtonStartClick(View e) {
        String name = txtName.getEditText().getText().toString();
        String email = txtEmail.getEditText().getText().toString();
        String password = txtPassword.getEditText().getText().toString();

        txtName.setError(null);
        txtEmail.setError(null);
        txtPassword.setError(null);

        validator.clear();

        if (validator.validate()) {
            call = service.register(name, email, password);
            call.enqueue(new Callback<RegisterResponse>() {

                @Override
                public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                    Log.w(TAG, "onResponse: " + response);

                    if (response.isSuccessful()) {
                        Log.w(TAG, "onResponse: " + response.body());
                        repository.signIn(response.body());

                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    } else {
                        handleErrors(response.errorBody());
                    }
                }
                @Override
                public void onFailure(Call<RegisterResponse> call, Throwable t) {
                    Log.w(TAG, "onFailure: " + t.getMessage());
                }
            });
        }
    }

    public void setupRules() {
        validator.addValidation(this, R.id.txt_name, RegexTemplate.NOT_EMPTY, R.string.error_field_required);
        validator.addValidation(this, R.id.txt_email, Patterns.EMAIL_ADDRESS, R.string.error_invalid_email);
        validator.addValidation(this, R.id.txt_password, RegexTemplate.NOT_EMPTY, R.string.error_field_required);
    }

    private void handleErrors(ResponseBody response) {
        ApiError apiError = Utils.convertErrors(response);

        for (Map.Entry<String, List<String>> error : apiError.getErrors().entrySet()) {
            if (error.getKey().equals("name")) {
                txtName.setError(error.getValue().get(0));
            }
            if (error.getKey().equals("email")) {
                txtEmail.setError(error.getValue().get(0));
            }
            if (error.getKey().equals("password")) {
                txtPassword.setError(error.getValue().get(0));
            }
        }
    }
}
