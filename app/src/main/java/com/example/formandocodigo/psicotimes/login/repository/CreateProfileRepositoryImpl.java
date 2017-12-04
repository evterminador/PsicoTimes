package com.example.formandocodigo.psicotimes.login.repository;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.formandocodigo.psicotimes.login.repository.net.entity.RegisterResponse;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Map;

/**
 * Created by FormandoCodigo on 04/12/2017.
 */

public class CreateProfileRepositoryImpl implements CreateProfileRepository {

    private static final String DEFAULT_FILE_NAME = "profile_";

    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    private Context context;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public CreateProfileRepositoryImpl(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(DEFAULT_FILE_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    @Override
    public void createProfile(RegisterResponse response) {
        String created = format.format(new Timestamp(System.currentTimeMillis()));

        for (Map.Entry<String, String> res : response.getSuccess().entrySet()) {
            if (res.getKey().equals("update")) {
                if (res.getValue().equals("true")) {
                    editor.putBoolean("isExistsProfile", true);
                }

            }
        }
        editor.putString("updated_at", created);

        editor.commit();
    }

    public boolean isExistsPreferences() {
        Boolean exists = sharedPreferences.getBoolean("isExistsProfile", false);

        if (exists)
            return true;

        return exists;
    }
}
