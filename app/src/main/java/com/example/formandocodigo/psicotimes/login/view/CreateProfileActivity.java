package com.example.formandocodigo.psicotimes.login.view;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.example.formandocodigo.psicotimes.R;
import com.example.formandocodigo.psicotimes.login.repository.CreateProfileRepositoryImpl;
import com.example.formandocodigo.psicotimes.login.repository.LoginRepositoryImpl;
import com.example.formandocodigo.psicotimes.login.repository.net.ApiService;
import com.example.formandocodigo.psicotimes.login.repository.net.RetrofitBuilder;
import com.example.formandocodigo.psicotimes.login.repository.net.entity.ApiError;
import com.example.formandocodigo.psicotimes.login.repository.net.entity.RegisterResponse;
import com.example.formandocodigo.psicotimes.utils.Converts;
import com.example.formandocodigo.psicotimes.main.view.MainActivity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateProfileActivity extends AppCompatActivity implements CreateProfileView {

    private final static String TAG = "CreateProfileActivity";

    @BindView(R.id.txt_birth_date)
    TextInputLayout txtBirthDate;
    @BindView(R.id.txt_use_time)
    TextInputLayout txtUseTime;
    @BindView(R.id.txt_dni)
    TextInputLayout txtDni;
    @BindView(R.id.spinner_sex)
    Spinner spiSex;
    @BindView(R.id.spinner_occupation)
    Spinner spiOccupation;
    @BindView(R.id.btn_send_profile)
    Button btnSendProfile;
    @BindView(R.id.pgr_bar_profile)
    ProgressBar pgrBarProfile;

    String sex, occupation;
    Boolean isWorking;

    AwesomeValidation validator;

    ApiService service;
    Call<RegisterResponse> call;

    CreateProfileRepositoryImpl repository;
    LoginRepositoryImpl loginRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);
        showToolbar(getResources().getString(R.string.toolbar_tittle_create_profile), false);

        ButterKnife.bind(this);

        repository = new CreateProfileRepositoryImpl(this);
        loginRepository = new LoginRepositoryImpl(this);

        if (repository.isExistsPreferences()) {
            Intent intent = new Intent(CreateProfileActivity.this, MainActivity.class);
            startActivity(intent);
        }

        validator = new AwesomeValidation(ValidationStyle.TEXT_INPUT_LAYOUT);
        setupRules();
        service = RetrofitBuilder.createService(ApiService.class);

    }

    @OnClick(R.id.btn_send_profile)
    void onButtonSendCreateProfile(View v) {
        showProgressBar();
        selectsSpinner();
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        String getDate = txtBirthDate.getEditText().getText().toString();
        String dni = txtDni.getEditText().getText().toString();

        txtBirthDate.setError(null);
        txtUseTime.setError(null);
        txtDni.setError(null);

        String email = loginRepository.getEmailAndToken().get("email");

        validator.clear();

        if (validator.validate()) {
            Integer timeUse = Integer.parseInt(txtUseTime.getEditText().getText().toString());
            Date birthDate = new Date();
            try {
                birthDate = format.parse(getDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            call = service.profile(email, birthDate, sex, dni, occupation, false, timeUse);

            call.enqueue(new Callback<RegisterResponse>() {
                @Override
                public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                    if (response.isSuccessful()) {
                        Log.w(TAG, "onResponse: " + response.body());
                        repository.createProfile(response.body());

                        startActivity(new Intent(CreateProfileActivity.this, MainActivity.class));
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
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (call != null)  {
            call.cancel();
            call = null;
        }
    }

    @Override
    public void showProgressBar() {
        pgrBarProfile.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        pgrBarProfile.setVisibility(View.GONE);
    }

    @Override
    public void createProfileError(String error) {

    }

    @Override
    public void goHome() {

    }

    private void handleErrors(ResponseBody response) {
        ApiError apiError = Converts.convertErrors(response);

        for (Map.Entry<String, List<String>> error : apiError.getErrors().entrySet()) {
            if (error.getKey().equals("birthDate")) {
                txtBirthDate.setError(error.getValue().get(0));
            }
            if (error.getKey().equals("timeUse")) {
                txtUseTime.setError(error.getValue().get(0));
            }
            if (error.getKey().equals("dni")) {
                txtDni.setError(error.getValue().get(0));
            }
        }
    }

    private void selectsSpinner() {
        sex = spiSex.getSelectedItem().toString();
        occupation = spiOccupation.getSelectedItem().toString();
    }

    public void setupRules() {
        validator.addValidation(this, R.id.txt_birth_date, RegexTemplate.NOT_EMPTY, R.string.error_field_required);
        validator.addValidation(this, R.id.txt_dni, RegexTemplate.NOT_EMPTY, R.string.error_field_required);
        validator.addValidation(this, R.id.txt_use_time, RegexTemplate.NOT_EMPTY, R.string.error_field_required);
        validator.addValidation(this, R.id.txt_use_time, "[0-9]", R.string.error_field_number);
    }

    public void showToolbar(String tittle, boolean upButton){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(tittle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(upButton);

    }


}
