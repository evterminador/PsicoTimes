package com.example.formandocodigo.psicotimes.main.repository;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.formandocodigo.psicotimes.domain.StateUseCase;
import com.example.formandocodigo.psicotimes.domain.StateUseCaseImpl;
import com.example.formandocodigo.psicotimes.entity.HistoricState;
import com.example.formandocodigo.psicotimes.entity.StateUse;
import com.example.formandocodigo.psicotimes.entity.StatisticsDetail;
import com.example.formandocodigo.psicotimes.utils.Continual;
import com.example.formandocodigo.psicotimes.main.presenter.MainPresenter;
import com.example.formandocodigo.psicotimes.utils.Converts;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * Created by FormandoCodigo on 04/12/2017.
 */

public class MainRepositoryImpl implements MainRepository {

    private MainPresenter presenter;
    private StateUseCase useCase;

    public MainRepositoryImpl(MainPresenter presenter) {
        this.presenter = presenter;
        useCase = new StateUseCaseImpl();
    }

    @Override
    public List<StateUse> findByIdAll(Integer id) {
        List<StateUse> list = new ArrayList<>();
        return list; //useCase.findStateUseByIdAll(id);
    }

    @Override
    public List<StateUse> findAll() {
        List<StateUse> list = new ArrayList<>();
        return list; //useCase.getStateUseAll();
    }

    @Override
    public List<StatisticsDetail> getStatisticsDetailCurrentDate() {
        return useCase.getStatisticsDetailCurrentDate();
    }


    @Override
    public HashMap<String, String> getUserEmailAndPassword(Activity activity) {
        HashMap<String, String> data = new HashMap<>();

        SharedPreferences preferences = activity.getSharedPreferences(Continual.Shared.DEFAULT_FILE_NAME, Context.MODE_PRIVATE);

        String email = preferences.getString("email", null);
        String token = preferences.getString("token", null);

        data.put("email", email);
        data.put("token", token);

        return data;
    }

    @Override
    public int quantityUnlockScreen(Activity activity) {
        SharedPreferences preferences = activity.getSharedPreferences(Continual.Shared.LockScreen.FILE_NAME, Context.MODE_PRIVATE);
        return (preferences.getInt(Continual.Shared.LockScreen.KEY_SCREEN, 0));
    }

    @Override
    public int quantityNroApps(Activity activity) {
        List<HistoricState> historicStates = useCase.getHistoricStateAll();

        for (HistoricState h : historicStates) {
            if (h.getCreated_at().after(getCurrentDay()))
                return h.getQuantity();
        }

        return 0;
    }

    private Timestamp getCurrentDay() {
        Calendar c = Calendar.getInstance();

        Converts.setTimeToBeginningOfDay(c);

        return new Timestamp(c.getTimeInMillis());
    }

    private Timestamp getEndDay() {
        Calendar c = Calendar.getInstance();

        Converts.setTimeToEndOfDay(c);

        return new Timestamp(c.getTimeInMillis());
    }
}
