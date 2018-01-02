package com.example.formandocodigo.psicotimes.view;

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
import com.example.formandocodigo.psicotimes.adapter.HistoricStateAdapterRecyclerView;
import com.example.formandocodigo.psicotimes.adapter.StateUseByDateAdapterRecyclerView;
import com.example.formandocodigo.psicotimes.data.entity.StateUseEntity;
import com.example.formandocodigo.psicotimes.data.entity.mapper.StateUseEntityDataMapper;
import com.example.formandocodigo.psicotimes.domain.StateUseCaseImpl;
import com.example.formandocodigo.psicotimes.entity.HistoricState;
import com.example.formandocodigo.psicotimes.entity.StateUse;
import com.example.formandocodigo.psicotimes.sort.SortHistoricStateByDate;
import com.example.formandocodigo.psicotimes.sort.SortStateUseByDate;
import com.example.formandocodigo.psicotimes.utils.Converts;
import com.example.formandocodigo.psicotimes.utils.StateUseAll;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecordDayActivity extends AppCompatActivity {

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd 00:00:00");

    private ArrayList<StateUse> stateUses = new ArrayList<>();

    @BindView(R.id.ctl_day)
    ConstraintLayout ctlDay;
    @BindView(R.id.ctl_month)
    ConstraintLayout ctlMonth;
    @BindView(R.id.txt_total_use)
    TextView txtTotalUse;

    RecyclerView stateUsesRecycler;

    private int mode = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_day);
        showToolbar("", true);

        ButterKnife.bind(this);

        getStateUsesAll();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayout.VERTICAL);

        stateUsesRecycler = findViewById(R.id.record_date_recycler);
        stateUsesRecycler.setLayoutManager(linearLayoutManager);

        ctlDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mode = 0;
                fillBody(mode);
            }
        });

        ctlMonth.setOnClickListener(new View.OnClickListener() {
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

        if (mode == 1) {
            HistoricStateAdapterRecyclerView recyclerView = new HistoricStateAdapterRecyclerView(historicStateListByMonth(),
                    R.layout.cardview_historic_state,
                    this);

            stateUsesRecycler.setAdapter(recyclerView);
        } else {
            StateUseByDateAdapterRecyclerView recyclerView = new StateUseByDateAdapterRecyclerView(stateUses,
                    R.layout.cardview_state_use_historic,
                    this);
            stateUsesRecycler.setAdapter(recyclerView);

            Timestamp fecha = new Timestamp(System.currentTimeMillis());

            txtTotalUse.setText(getTotalApp(stateUses));

            String date = dateFormat.format(fecha.getTime());
            try {
                fecha = Converts.convertStringToTimestamp(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    private ArrayList<StateUse> stateUseListByDay() {
        ArrayList<StateUse> list = stateUses;
        ArrayList<StateUse> newList = new ArrayList<>();
        Collections.sort(list, new SortStateUseByDate());

        Timestamp fecha = new Timestamp(System.currentTimeMillis());

        txtTotalUse.setText(getTotalApp(list));

        String date = dateFormat.format(fecha.getTime());
        try {
            fecha = Converts.convertStringToTimestamp(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (stateUses.size() > 0) {
            for (StateUse s : list) {
                if (s.getCreated_at().after(fecha)) {
                    newList.add(s);
                }
            }
        }

        return newList;
    }

    public ArrayList<HistoricState> historicStateListByMonth() {
        StateUseCaseImpl useCase = new StateUseCaseImpl();

        ArrayList<HistoricState> list = new ArrayList<>(useCase.getHistoricStateAll());

        Collections.sort(list, new SortHistoricStateByDate());
        Collections.reverse(list);

        return list;
    }

    private void getStateUsesAll() {
        StateUseAll stateUseAll = new StateUseAll(this);

        stateUses = transformStateUseEntityToStateUse(stateUseAll.getStateUseEntityAll());
    }

    private String getTotalApp(ArrayList<StateUse> list) {
        String date = "2h12m16s";
        if (stateUses.size() > 0) {
            long count = 0;
            for (StateUse s : list) {
                count += s.getUseTime();
            }
            date = Converts.convertLongToTimeChar(count);
        }
        return date;
    }

    private void changedMode() {
        if (mode == 1) {
            ctlDay.setBackground(null);
            ctlDay.setElevation(0);

            ctlMonth.setBackground(getResources().getDrawable(R.drawable.border_select_historic));
            ctlMonth.setElevation(4F);
        } else {
            ctlMonth.setBackground(null);
            ctlMonth.setElevation(0);

            ctlDay.setBackground(getResources().getDrawable(R.drawable.border_select_historic));
            ctlDay.setElevation(4F);
        }
    }

    public void showToolbar(String tittle, boolean upButton){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(tittle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(upButton);
    }

    private ArrayList<StateUse> transformStateUseEntityToStateUse(List<StateUseEntity> stateUseEntities) {
        ArrayList<StateUse> list;

        StateUseEntityDataMapper mapper = new StateUseEntityDataMapper();

        list = new ArrayList<>(mapper.transformArrayList((ArrayList<StateUseEntity>) stateUseEntities));

        return list;
    }
}
