package com.example.formandocodigo.psicotimes.login.view;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.example.formandocodigo.psicotimes.R;
import com.example.formandocodigo.psicotimes.login.repository.CreateProfileRepositoryImpl;
import com.example.formandocodigo.psicotimes.login.net.ApiService;
import com.example.formandocodigo.psicotimes.login.net.RetrofitBuilder;
import com.example.formandocodigo.psicotimes.login.net.entity.ApiError;
import com.example.formandocodigo.psicotimes.login.net.entity.RegisterResponse;
import com.example.formandocodigo.psicotimes.post.SplashScreenActivity;
import com.example.formandocodigo.psicotimes.utils.Converts;
import com.example.formandocodigo.psicotimes.main.view.MainActivity;
import com.example.formandocodigo.psicotimes.utils.net.NetworkStatus;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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

    @BindView(R.id.txt_name)
    TextInputLayout txtName;
    @BindView(R.id.txt_email)
    TextInputLayout txtEmail;
    @BindView(R.id.txt_password)
    TextInputLayout txtPassword;
    @BindView(R.id.txt_confirm_password)
    TextInputLayout txtConfirmPassword;
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
    @BindView(R.id.rbt_yes)
    RadioButton rbtYes;
    @BindView(R.id.rbt_no)
    RadioButton rbtNo;
    @BindView(R.id.btn_send_profile)
    Button btnSendProfile;
    @BindView(R.id.pgr_bar_profile)
    ProgressBar pgrBarProfile;

    @BindView(R.id.txt_birth_date_edit)
    TextInputEditText txtBirthDatePicker;

    String sex, occupation;
    Boolean isWorking = false;

    AwesomeValidation validator;

    ApiService service;
    Call<RegisterResponse> call;

    CreateProfileRepositoryImpl repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);
        showToolbar(getResources().getString(R.string.toolbar_tittle_create_profile), false);

        ButterKnife.bind(this);
        hideProgressBar();

        repository = new CreateProfileRepositoryImpl(this);

        if (repository.isExistsPreferences()) {
            Intent intent = new Intent(CreateProfileActivity.this, MainActivity.class);
            startActivity(intent);
        }

        fillSpinner();

        Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                calendar.set(Calendar.YEAR, i);
                calendar.set(Calendar.MONTH, i1);
                calendar.set(Calendar.DAY_OF_MONTH, i2);
                updateLabel(calendar);
            }
        };

        txtBirthDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(CreateProfileActivity.this, date,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                ).show();
            }
        });

        validator = new AwesomeValidation(ValidationStyle.TEXT_INPUT_LAYOUT);
        setupRules();
        service = RetrofitBuilder.createService(ApiService.class);
    }

    @OnClick(R.id.btn_send_profile)
    void onButtonSendCreateProfile(View v) {
        if (NetworkStatus.isOnline(this)) {
            showProgressBar();
            selectsSpinner();
            selectedWorking();
            DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            String name = txtName.getEditText().getText().toString();
            String email = txtEmail.getEditText().getText().toString();
            String password = txtPassword.getEditText().getText().toString();
            String confirmPassword = txtConfirmPassword.getEditText().getText().toString();
            String getDate = txtBirthDate.getEditText().getText().toString();
            String dni = txtDni.getEditText().getText().toString();

            txtBirthDate.setError(null);
            txtUseTime.setError(null);
            txtDni.setError(null);

            validator.clear();

            if (validator.validate()) {
                Integer timeUse = Integer.parseInt(txtUseTime.getEditText().getText().toString());
                Date birthDate = new Date();
                try {
                    birthDate = format.parse(getDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                call = service.profile(name, email, password, confirmPassword, birthDate, sex, dni, occupation, isWorking, timeUse);

                call.enqueue(new Callback<RegisterResponse>() {
                    @Override
                    public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                        if (response.isSuccessful()) {
                            //Log.w(TAG, "onResponse: " + response.body());
                            repository.createProfile(response.body());

                            goHome();
                        } else {
                            handleErrors(response.errorBody());
                        }
                    }

                    @Override
                    public void onFailure(Call<RegisterResponse> call, Throwable t) {
                        //Log.w(TAG, "onFailure: " + t.getMessage());
                    }
                });
            }
            hideProgressBar();
        } else {
            Toast.makeText(this, "Por favor conectese a la red", Toast.LENGTH_LONG).show();
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
        Intent intent = new Intent(this, SplashScreenActivity.class);
        startActivity(intent);
    }

    private void handleErrors(ResponseBody response) {
        ApiError apiError = Converts.convertErrors(response);

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
            if (error.getKey().equals("birth_date")) {
                txtBirthDate.setError(error.getValue().get(0));
            }
            if (error.getKey().equals("time_use")) {
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

    private void selectedWorking() {
        if (rbtYes.isSelected()) {
            isWorking = true;
        } else if (rbtNo.isSelected()) {
            isWorking = false;
        }
    }

    private void fillSpinner() {

        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<>(
                this, R.layout.spinner_item, getResources().getStringArray(R.array.sex)
        );

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                this, R.layout.spinner_item, getResources().getStringArray(R.array.occupations)
        );
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_item);
        spiOccupation.setAdapter(spinnerAdapter);

        stringArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spiSex.setAdapter(stringArrayAdapter);
    }

    private void updateLabel(Calendar calendar) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

        txtBirthDatePicker.setText(format.format(calendar.getTime()));
    }

    public void setupRules() {
        validator.addValidation(this, R.id.txt_email, Patterns.EMAIL_ADDRESS, R.string.error_invalid_email);
        validator.addValidation(this, R.id.txt_password, RegexTemplate.NOT_EMPTY, R.string.error_field_required);
        validator.addValidation(this, R.id.txt_confirm_password, RegexTemplate.NOT_EMPTY, R.string.error_field_required);
        validator.addValidation(this, R.id.txt_birth_date, RegexTemplate.NOT_EMPTY, R.string.error_field_required);
        validator.addValidation(this, R.id.txt_dni, RegexTemplate.NOT_EMPTY, R.string.error_field_required);
        validator.addValidation(this, R.id.txt_use_time, RegexTemplate.NOT_EMPTY, R.string.error_field_required);
    }

    public void showToolbar(String tittle, boolean upButton){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(tittle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(upButton);
    }

}
