package com.example.formandocodigo.psicotimes.view;

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

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecordDetailActivity extends AppCompatActivity {

    @BindView(R.id.txt_total_time_use_detail)
    TextView txtTotalTimeUse;
    @BindView(R.id.txt_quantity_unlock_screen_detail)
    TextView txtQUS;
    @BindView(R.id.txt_quantity_nro_apps_detail)
    TextView txtNA;

    RecyclerView recyclerView;

    StateUseCase useCase;

    private ArrayList<StatisticsDetail> statisticsDetails;

    int id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_detail);
        useCase = new StateUseCaseImpl();

        Bundle recovery = getIntent().getExtras();

        ButterKnife.bind(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayout.VERTICAL);

        recyclerView = findViewById(R.id.statistics_recycler);
        recyclerView.setLayoutManager(linearLayoutManager);

        HistoricState state = null;

        if (recovery != null) {
            id = recovery.getInt("id");
        }

        if (id > 0) {
            state = useCase.findHistoricState(id);
            showToolbar("Detalle del " + Converts.convertTimestampToStringShort(state.getCreated_at()), true);
        } else {
            showToolbar("Detalle", true);
        }

        if (state != null) {
            txtTotalTimeUse.setText(Converts.convertLongToTimeSimple(state.getTimeUse()));
            txtNA.setText(String.valueOf(state.getQuantity()));
            txtQUS.setText(String.valueOf(state.getNroUnlock()));
            getStatisticsDetail(state.getCreated_at());
            setRecyclerView();
        }
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

    private void getStatisticsDetail(Timestamp date) {
        statisticsDetails = new ArrayList<>(useCase.getStatisticsDetailByDate(date));
    }

    private void setRecyclerView() {
        if (statisticsDetails.size() > 0) {
            StatisticsDetailAdapterRecyclerView recycler =
                    new StatisticsDetailAdapterRecyclerView(statisticsDetails, R.layout.cardview_state_use_main, this);

            recyclerView.setAdapter(recycler);
        }
    }

    public void showToolbar(String tittle, boolean upButton) {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(tittle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(upButton);
    }

}