package com.example.formandocodigo.psicotimes.view;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.formandocodigo.psicotimes.R;
import com.example.formandocodigo.psicotimes.adapter.StatisticsDetailAdapterRecyclerView;
import com.example.formandocodigo.psicotimes.domain.StateUseCase;
import com.example.formandocodigo.psicotimes.domain.StateUseCaseImpl;
import com.example.formandocodigo.psicotimes.entity.HistoricState;
import com.example.formandocodigo.psicotimes.entity.StatisticsDetail;
import com.example.formandocodigo.psicotimes.utils.Converts;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecordDayActivity extends AppCompatActivity {

    @BindView(R.id.txt_from_picker)
    TextInputEditText txtFromPicker;
    @BindView(R.id.txt_to_picker)
    TextInputEditText txtToPicker;
    @BindView(R.id.txt_total_time_use_day)
    TextView txtTotalTimeUseDay;
    @BindView(R.id.txt_quantity_nro_apps_day)
    TextView txtQuantityNroAppsDay;
    @BindView(R.id.txt_quantity_unlock_screen_day)
    TextView txtQuantityUnlockScreenDay;

    private ArrayList<StatisticsDetail> statisticsDetails = new ArrayList<>();

    StateUseCase useCase;

    RecyclerView statisticsDetailRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_day);
        showToolbar("Historial Especifíco", true);

        ButterKnife.bind(this);

        useCase = new StateUseCaseImpl();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayout.VERTICAL);

        statisticsDetailRecycler = findViewById(R.id.record_date_recycler);
        statisticsDetailRecycler.setLayoutManager(linearLayoutManager);

        Context context = getApplicationContext();

        long install;
        try {
            install = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).firstInstallTime;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            install = Calendar.getInstance().getTimeInMillis();
        }

        Timestamp t1 = new Timestamp(install);

        txtFromPicker.setText(Converts.convertTimestampToStringShort(t1));
        Timestamp current = new Timestamp(System.currentTimeMillis());
        txtToPicker.setText(Converts.convertTimestampToStringShort(current));

        getStatisticsDetailByDate(t1, current);

        fillBody();
        setTextViews();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.homeAsUp) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void getStatisticsDetailByDate(Timestamp t1, Timestamp t2) {
        statisticsDetails = new ArrayList<>(useCase.getStatisticsDetailByDate(t1, t2));
    }

    private void fillBody() {
        if (statisticsDetails.size() > 0) {
            StatisticsDetailAdapterRecyclerView recycler =
                    new StatisticsDetailAdapterRecyclerView(statisticsDetails, R.layout.cardview_state_use_main, this);

            statisticsDetailRecycler.setAdapter(recycler);
        }
    }

    private void setTextViews() {
        List<HistoricState> historicStates = useCase.getHistoricStateAll();
        long totalTime = 0;
        long totalUnlock = 0;
        if (historicStates.size() > 0 && statisticsDetails.size() > 0) {
            for (HistoricState h : historicStates) {
                totalUnlock += h.getNroUnlock();
            }

            for (StatisticsDetail s : statisticsDetails) {
                totalTime += s.getTimeUse();
            }

            txtTotalTimeUseDay.setText(Converts.convertLongToTimeSimple(totalTime));
            txtQuantityNroAppsDay.setText(String.valueOf(statisticsDetails.size()));
            txtQuantityUnlockScreenDay.setText(String.valueOf(totalUnlock));
        }
    }

    public void showToolbar(String tittle, boolean upButton){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(tittle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(upButton);
    }

}
