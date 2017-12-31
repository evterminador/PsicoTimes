package com.example.formandocodigo.psicotimes.main.view;

import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.formandocodigo.psicotimes.R;
import com.example.formandocodigo.psicotimes.main.net.AppOrderResponse;
import com.example.formandocodigo.psicotimes.sort.SortStateUseByQuantity;
import com.example.formandocodigo.psicotimes.sort.SortStateUseByUseTime;
import com.example.formandocodigo.psicotimes.utils.Converts;
import com.example.formandocodigo.psicotimes.main.presenter.MainPresenter;
import com.example.formandocodigo.psicotimes.main.presenter.MainPresenterImpl;
import com.example.formandocodigo.psicotimes.utils.net.OrderService;
import com.example.formandocodigo.psicotimes.utils.net.RetrofitBuilder;
import com.example.formandocodigo.psicotimes.main.net.StateUserOrderResponse;
import com.example.formandocodigo.psicotimes.entity.StateUse;
import com.example.formandocodigo.psicotimes.service.StateUseService;
import com.example.formandocodigo.psicotimes.utils.net.NetworkStatus;
import com.example.formandocodigo.psicotimes.utils.permission.UsageStatsPermission;
import com.example.formandocodigo.psicotimes.view.RecordActivity;
import com.example.formandocodigo.psicotimes.view.RecordDayActivity;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, MainView {

    @BindView(R.id.txt_quantity_unlock_screen)
    TextView txtQuantityUnlockScreen;

    private MainPresenter presenter;

    private PieChart pieChart;
    private BarChart barChart;
    List<StateUse> stateUses = new ArrayList<>();

    OrderService service;
    Call<StateUserOrderResponse> stateUserCall;
    Call<AppOrderResponse> appCall;

    private int id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        pieChart = findViewById(R.id.pie_chart_statistics);
        barChart = findViewById(R.id.bar_chart_quantity_use);
        pieChart.setUsePercentValues(true);

        pieChart.getLegend().setEnabled(false);
        barChart.getLegend().setEnabled(false);

        //Sets charts
        pieChart.getDescription().setEnabled(false);
        barChart.getDescription().setEnabled(false);

        service = RetrofitBuilder.createService(OrderService.class);

        presenter = new MainPresenterImpl(this);

        initializeService();

        /*fabSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                syncUp();
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        initializeGraphics();

        if (isOnline()) {
            //initializeApp();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        initializeGraphics();
        initializeService();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (stateUserCall != null)  {
            stateUserCall.cancel();
            stateUserCall = null;
        }
        if (appCall != null) {
            appCall.cancel();
            appCall = null;
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem statusNetwork = menu.findItem(R.id.status_network);

        if (isOnline()) {
               statusNetwork.setVisible(false);
        } else {
            statusNetwork.setVisible(true);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.status_network) {
            /*Intent intent = new Intent(Settings.INTENT_CATEGORY_USAGE_ACCESS_CONFIG);
            startActivity(intent);*/
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            // Implement user profile
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_record_date) {
            Intent intent = new Intent(MainActivity.this, RecordDayActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_record) {
            Intent intent = new Intent(MainActivity.this, RecordActivity.class);
            startActivity(intent);
        }  else if (id == R.id.nav_send) {
            finish();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void updateAppSuccess(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void updateAppError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void syncError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
    }

    private boolean isOnline() {
        return NetworkStatus.isOnline(this);
    }

    private void initializeApp() {
        Thread tr = new Thread(new Runnable() {
            @Override
            public void run() {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateApp();
                    }
                });
            }
        });
        tr.start();
    }

    private void initializeService() {
        if (checkForPermission(this)) {

            boolean isService = isMyServiceRunning(StateUseService.class);

            if (!isService) {
                startService(new Intent(MainActivity.this, StateUseService.class));
            }
        } else {
            requestPermission();
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private boolean checkForPermission(Context context) {
        return UsageStatsPermission.isExistsPermission(context);
    }

    private void requestPermission() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        View view = getLayoutInflater().inflate(R.layout.dialog_request_permission, null);

        builder.setView(view);

        builder.setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        final AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void initializeGraphics() {
        getAppAll();
        setPieChart();
        setQuantityUnLockChart();
        setBarChart();
    }

    private void setPieChart() {
        ArrayList<PieEntry> yVals = new ArrayList<>();
        ArrayList<String> xVals = new ArrayList<>();

        PieDataSet dataSet;

        if (stateUses.size() > 0) {
            List<StateUse> stateUsesByUseTime = stateUseListByUseTime();

            StateUse maxUseTime = null;
            for (StateUse s : stateUsesByUseTime) {
                if (maxUseTime == null)
                    maxUseTime = s;
                yVals.add(new PieEntry(s.getUseTime(), s.getNameApplication()));
                xVals.add(s.getNameApplication());
            }

            dataSet = new PieDataSet(yVals, "Cantidad de uso");
            dataSet.setSelectionShift(stateUsesByUseTime.size());

            pieChart.setCenterText(maxUseTime.getNameApplication() +
                    "\n Tiempo de uso: " +
                    Converts.convertLongToTimeChar(maxUseTime.getUseTime()));
        } else {
            // Default valor
            float[] yData = { 5, 10, 15, 30, 40 };
            String[] xData = { "Sony", "Huawei", "LG", "Apple", "Samsung" };

            for (int i = 0; i < yData.length; i++)
                yVals.add(new PieEntry(yData[i], i));

            for (int i = 0; i < xData.length; i++)
                xVals.add(xData[i]);

            dataSet = new PieDataSet(yVals, "Dafault");
            dataSet.setSelectionShift(5);
        }

        // create pie data set
        dataSet.setSliceSpace(2);
        //dataSet.setSelectionShift(5);

        // add many colors
        ArrayList<Integer> colors = getChartsColors();

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);

        // instantiate pie data object now
        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.GRAY);

        pieChart.setData(data);

        // undo all highlights
        pieChart.highlightValues(null);

        // update pie chart
        pieChart.invalidate();
    }

    private void setQuantityUnLockChart() {
        Integer quantity = presenter.quantityUnlockScreen(this);
        txtQuantityUnlockScreen.setText(String.valueOf(quantity));

        /*ArrayList<Entry> componet1 = new ArrayList<>();

        LineDataSet setComp1;

        if (stateUses.size() > 0) {
            List<StateUse> stateUseByQuantity = presenter.findAllById(id);

            for (StateUse s : stateUseByQuantity) {
                componet1.add(new Entry(Converts.getDayTimesTamp(s.getCreated_at()), s.getQuantity()));
            }

            setComp1 = new LineDataSet(componet1, stateUseByQuantity.get(0).getNameApplication());
        } else {
            float[] yData = { 5, 10, 15, 30, 40 };

            for (int i = 0; i < yData.length; i++) {
                componet1.add(new Entry(i, yData[i]));
            }

            setComp1 = new LineDataSet(componet1, "Default value");
        }

        setComp1.addColor(ColorTemplate.VORDIPLOM_COLORS[0]);

        setComp1.setAxisDependency(YAxis.AxisDependency.RIGHT);

        List<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(setComp1);

        LineData data = new LineData(dataSets);

        lineChart.setData(data);
        lineChart.getXAxis().setEnabled(false);
        lineChart.getAxisLeft().setEnabled(false);
        lineChart.getAxisRight().setEnabled(false);
        lineChart.invalidate();*/
    }

    private void setBarChart() {
        ArrayList<BarEntry> entries = new ArrayList<>();
        ArrayList<String> xValues = new ArrayList<>();

        BarDataSet dataSet;

        if (stateUses.size() > 0) {
            List<StateUse> stateUseByQuantity = stateUseListByQuantity();

            for (int i = 0; i < stateUseByQuantity.size(); i++) {
                entries.add(new BarEntry(i, stateUseByQuantity.get(i).getQuantity()));
                xValues.add(stateUseByQuantity.get(i).getNameApplication());
            }

            dataSet = new BarDataSet(entries, "Cantidad de entradas");
        } else {
            float[] yData = { 5, 10, 15, 30, 40 };
            String[] xData = { "Sony", "Huawei", "LG", "Apple", "Samsung" };

            for (int i = 0; i < yData.length; i++) {
                entries.add(new BarEntry(i, yData[i]));
                xValues.add(xData[i]);
            }

            dataSet = new BarDataSet(entries, "Default values");
        }

        ArrayList<Integer> colors = getChartsColors();

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);

        dataSet.setAxisDependency(YAxis.AxisDependency.RIGHT);

        BarData data = new BarData(dataSet);

        barChart.setData(data);
        barChart.setFitBars(true);
        barChart.getXAxis().setEnabled(false);
        barChart.getAxisLeft().setEnabled(false);
        barChart.getAxisRight().setEnabled(false);
        barChart.invalidate();
    }

    private void getAppAll() {
        stateUses = presenter.findAll();
    }

    private void updateApp() {
        presenter.updateApp(this, service, appCall);
    }

    private void syncUp() {
        presenter.syncUp(this, service, stateUserCall);
    }

    private List<StateUse> stateUseListByUseTime() {
        List<StateUse> list = stateUses;
        Collections.sort(list, new SortStateUseByUseTime());
        Collections.reverse(list);

        List<StateUse> top10 = limitStateUses(list, 10);

        return top10;
    }

    private List<StateUse> stateUseListByQuantity() {
        List<StateUse> list = stateUses;
        Collections.sort(list, new SortStateUseByQuantity());
        Collections.reverse(list);

        return limitStateUses(list, 5);
    }

    private List<StateUse> limitStateUses(List<StateUse> list, int limit) {
        List<StateUse> newList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (i >= limit) {
                return newList;
            }
            newList.add(list.get(i));
        }
        return newList;
    }

    protected ArrayList<Integer> getChartsColors() {
        ArrayList<Integer> colors = new ArrayList<>();

        for (int c : ColorTemplate.MATERIAL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        return colors;
    }
}
