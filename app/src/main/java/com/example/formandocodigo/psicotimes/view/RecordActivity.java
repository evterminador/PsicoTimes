package com.example.formandocodigo.psicotimes.view;

import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.example.formandocodigo.psicotimes.R;
import com.example.formandocodigo.psicotimes.adapter.HistoricStateAdapterRecyclerView;
import com.example.formandocodigo.psicotimes.domain.StateUseCase;
import com.example.formandocodigo.psicotimes.domain.StateUseCaseImpl;
import com.example.formandocodigo.psicotimes.entity.HistoricState;

import java.util.ArrayList;


public class RecordActivity extends AppCompatActivity {

    private ArrayList<HistoricState> historicStates;

    RecyclerView stateUsesRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        showToolbar("Historial", true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayout.VERTICAL);

        stateUsesRecycler = findViewById(R.id.record_recycler);
        stateUsesRecycler.setLayoutManager(linearLayoutManager);

        getStatisticsAll();

        fillBody();
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

    private void getStatisticsAll() {
        StateUseCase stateUseCase = new StateUseCaseImpl();
        historicStates = new ArrayList<>(stateUseCase.getHistoricStateAll());
    }

    private void fillBody() {
        if (historicStates.size() > 0) {
            HistoricStateAdapterRecyclerView recyclerView = new HistoricStateAdapterRecyclerView(historicStates,
                    R.layout.cardview_historic_state,
                    this);
            stateUsesRecycler.setAdapter(recyclerView);
        }
    }


    public void showToolbar(String tittle, boolean upButton){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(tittle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(upButton);
    }
}
