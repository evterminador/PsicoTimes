package com.example.formandocodigo.psicotimes.view;

import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.formandocodigo.psicotimes.R;
import com.example.formandocodigo.psicotimes.adapter.StateUseAdapterRecyclerView;
import com.example.formandocodigo.psicotimes.domain.StateUseCase;
import com.example.formandocodigo.psicotimes.domain.StateUseCaseImpl;
import com.example.formandocodigo.psicotimes.entity.StateUse;
import com.example.formandocodigo.psicotimes.sort.SortStateUseByQuantity;
import com.example.formandocodigo.psicotimes.sort.SortStateUseByUseTime;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecordActivity extends AppCompatActivity {

    private ArrayList<StateUse> stateUses = new ArrayList<>();

    @BindView(R.id.ctl_percent_use_time)
    ConstraintLayout ctlUseTime;
    @BindView(R.id.ctl_percent_quantity)
    ConstraintLayout ctlQuantity;

    @BindView(R.id.use_time_percent_value) TextView txtUTPValue;
    @BindView(R.id.use_time_percent_increment) TextView txtUTPIncrement;
    @BindView(R.id.quantity_percent_value) TextView txtQValue;
    @BindView(R.id.quantity_percent_increment) TextView txtQIncrement;

    RecyclerView stateUsesRecycler;

    private int mode = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        showToolbar("Historial", true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayout.VERTICAL);

        stateUsesRecycler = findViewById(R.id.record_recycler);
        stateUsesRecycler.setLayoutManager(linearLayoutManager);

        ButterKnife.bind(this);

        getAppAll();

        ctlUseTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mode = 0;
                fillBody(mode);
            }
        });

        ctlQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mode = 1;
                fillBody(mode);
            }
        });
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

    private void fillBody(int mode) {
        changedMode();
        StateUseAdapterRecyclerView recyclerView;
        if (mode == 1) {
            recyclerView = new StateUseAdapterRecyclerView(stateUseListByUseTime(),
                    R.layout.cardview_state_use_historic,
                    this,
                    mode);
        } else {
            recyclerView = new StateUseAdapterRecyclerView(stateUseListByQuantity(),
                    R.layout.cardview_state_use_historic,
                    this,
                    mode);
        }
        stateUsesRecycler.setAdapter(recyclerView);
    }

    private ArrayList<StateUse> stateUseListByUseTime() {
        ArrayList<StateUse> list = stateUses;
        Collections.sort(list, new SortStateUseByUseTime());
        Collections.reverse(list);

        return list;
    }

    private ArrayList<StateUse> stateUseListByQuantity() {
        ArrayList<StateUse> list = stateUses;
        Collections.sort(list, new SortStateUseByQuantity());
        Collections.reverse(list);

        return list;
    }

    private void getAppAll() {
        StateUseCase stateUseCase = new StateUseCaseImpl();
        stateUses = stateUseCase.getStateUseAll();
    }

    private void changedMode() {
        if (mode == 1) {
            ctlUseTime.setBackground(null);
            ctlUseTime.setElevation(0);
            txtUTPValue.setText("67");
            txtUTPValue.setTextColor(Color.parseColor("#3D3D3D"));

            ctlQuantity.setBackground(getResources().getDrawable(R.drawable.border_select_historic));
            ctlQuantity.setElevation(4F);
            txtQValue.setText("47%");
            txtQValue.setTextColor(Color.parseColor("#1EFF8A"));
        } else {
            ctlQuantity.setBackground(null);
            ctlQuantity.setElevation(0);
            txtQValue.setText("47");
            txtQValue.setTextColor(Color.parseColor("#3D3D3D"));

            ctlUseTime.setBackground(getResources().getDrawable(R.drawable.border_select_historic));
            ctlUseTime.setElevation(4F);
            txtUTPValue.setText("67%");
            txtUTPValue.setTextColor(Color.parseColor("#1EFF8A"));
        }
    }

    public void showToolbar(String tittle, boolean upButton){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(tittle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(upButton);
    }
}
