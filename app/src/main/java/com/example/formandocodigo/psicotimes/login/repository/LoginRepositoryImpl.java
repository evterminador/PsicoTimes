package com.example.formandocodigo.psicotimes.login.repository;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.formandocodigo.psicotimes.login.repository.net.entity.RegisterResponse;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Singleton;

/**
 * Created by FormandoCodigo on 01/12/2017.
 */

@Singleton
public class LoginRepositoryImpl implements LoginRepository {

    private static final String DEFAULT_FILE_NAME = "user_";

    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    private Context context;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public LoginRepositoryImpl(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(DEFAULT_FILE_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    @Override
    public void signIn(RegisterResponse response) {
        String created = format.format(new Timestamp(System.currentTimeMillis()));

        for (Map.Entry<String, String> res : response.getSuccess().entrySet()) {
            if (res.getKey().equals("name")) {
                editor.putString("name", res.getValue());
            }
            if (res.getKey().equals("email")) {
                editor.putString("email", res.getValue());
            }
            if (res.getKey().equals("token")) {
                editor.putString("token", res.getValue());
            }
        }
        editor.putString("created_at", created);

        editor.commit();
    }

    public boolean isExistsPreferences() {
        String exists = sharedPreferences.getString("token", null);

        if (exists != null)
            return true;

        return false;
    }

    public HashMap<String, String> getEmailandToken() {
        HashMap<String, String> data = new HashMap<>();

        String email = sharedPreferences.getString("email", null);
        String token = sharedPreferences.getString("token", null);

        data.put("email", email);
        data.put("token", token);

        return data;
    }
}