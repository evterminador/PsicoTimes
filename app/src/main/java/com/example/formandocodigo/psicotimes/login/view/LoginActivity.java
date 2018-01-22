package com.example.formandocodigo.psicotimes.login.view;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.example.formandocodigo.psicotimes.R;
import com.example.formandocodigo.psicotimes.login.net.entity.ApiError;
import com.example.formandocodigo.psicotimes.login.net.entity.RegisterResponse;
import com.example.formandocodigo.psicotimes.login.net.ApiService;
import com.example.formandocodigo.psicotimes.login.net.RetrofitBuilder;
import com.example.formandocodigo.psicotimes.login.repository.LoginRepositoryImpl;
import com.example.formandocodigo.psicotimes.utils.Continual;
import com.example.formandocodigo.psicotimes.utils.Converts;
import com.example.formandocodigo.psicotimes.post.SplashScreenActivity;
import com.example.formandocodigo.psicotimes.utils.net.NetworkStatus;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements LoginView {

    private static final String TAG = "LoginActivity";

    @BindView(R.id.txt_email)
    TextInputLayout txtEmail;
    @BindView(R.id.txt_password)
    TextInputLayout txtPassword;
    @BindView(R.id.login_progress)
    ProgressBar loginProgress;

    AwesomeValidation validator;

    LoginRepositoryImpl repository;

    ApiService service;
    Call<RegisterResponse> call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);
        hideProgressBar();

        repository = new LoginRepositoryImpl(this);

        if (repository.isExistsPreferences()) {
            Intent intent = new Intent(LoginActivity.this, SplashScreenActivity.class);
            startActivity(intent);
            finish();
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
        if (NetworkStatus.isOnline(this)) {
            showProgressBar();
            String email = txtEmail.getEditText().getText().toString();
            String password = txtPassword.getEditText().getText().toString();

            txtEmail.setError(null);
            txtPassword.setError(null);

            validator.clear();

            if (validator.validate()) {

                call = service.login(email, password);
                call.enqueue(new Callback<RegisterResponse>() {

                    @Override
                    public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                        Log.w(TAG, "onResponse: " + response);

                        if (response.isSuccessful()) {
                            Log.w(TAG, "onResponse: " + response.body());
                            repository.signIn(response.body());

                            startActivity(new Intent(LoginActivity.this, SplashScreenActivity.class));
                        } else {
                            handleErrors(response.errorBody());
                        }

                        hideProgressBar();
                    }
                    @Override
                    public void onFailure(Call<RegisterResponse> call, Throwable t) {
                        Log.w(TAG, "onFailure: " + t.getMessage());

                        hideProgressBar();
                    }
                });
            }
            hideProgressBar();
        } else {
            Toast.makeText(this, "Por favor conectese a la red", Toast.LENGTH_LONG).show();
        }
    }

    public void setupRules() {
        validator.addValidation(this, R.id.txt_email, Patterns.EMAIL_ADDRESS, R.string.error_invalid_email);
        validator.addValidation(this, R.id.txt_password, RegexTemplate.NOT_EMPTY, R.string.error_field_required);
    }

    private void handleErrors(ResponseBody response) {
        txtPassword.getEditText().setText("");
        ApiError apiError = Converts.convertErrors(response);

        for (Map.Entry<String, List<String>> error : apiError.getErrors().entrySet()) {
            if (error.getKey().equals("email")) {
                txtEmail.setError(error.getValue().get(0));
            }
        }
    }

    @Override
    public void goCreateProfile(View view) {
        Intent intent = new Intent(this, CreateProfileActivity.class);
        startActivity(intent);
    }

    @Override
    public void showProgressBar() {
        loginProgress.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        loginProgress.setVisibility(View.GONE);
    }

    public void goForgotPassword(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://addictphones.com/password/reset"));
        startActivity(intent);
    }

}
