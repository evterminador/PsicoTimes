package com.example.formandocodigo.psicotimes.login.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.formandocodigo.psicotimes.R;
import com.example.formandocodigo.psicotimes.login.entity.ApiError;
import com.example.formandocodigo.psicotimes.login.entity.RegisterResponse;
import com.example.formandocodigo.psicotimes.login.network.ApiService;
import com.example.formandocodigo.psicotimes.login.network.RetrofitBuilder;
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
    EditText txtName;
    @BindView(R.id.txt_email)
    EditText txtEmail;
    @BindView(R.id.txt_password)
    EditText txtPassword;

    ApiService service;
    Call<RegisterResponse> call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

        service = RetrofitBuilder.createService(ApiService.class);
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
        String name = txtName.getText().toString();
        String email = txtEmail.getText().toString();
        String password = txtPassword.getText().toString();

        txtName.setError(null);
        txtEmail.setError(null);
        txtPassword.setError(null);

        call = service.register(name, email, password);
        call.enqueue(new Callback<RegisterResponse>() {

            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                Log.w(TAG, "onResponse: " + response);

                if (response.isSuccessful()) {

                } else {
                    handleErrors(response.errorBody());
                }
            }
            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                Log.w(TAG, "Onfailuere: " + t.getMessage());
            }
        });
        //Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        //startActivity(intent);
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
