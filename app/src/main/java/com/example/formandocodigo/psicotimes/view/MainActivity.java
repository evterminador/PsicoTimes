package com.example.formandocodigo.psicotimes.view;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Process;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.formandocodigo.psicotimes.R;
import com.example.formandocodigo.psicotimes.data.cache.FileManager;
import com.example.formandocodigo.psicotimes.data.cache.StateUseCacheImpl;
import com.example.formandocodigo.psicotimes.data.cache.serializer.Serializer;
import com.example.formandocodigo.psicotimes.data.entity.mapper.StateUseEntityDataMapper;
import com.example.formandocodigo.psicotimes.view.net.OrderService;
import com.example.formandocodigo.psicotimes.view.net.RetrofitBuilder;
import com.example.formandocodigo.psicotimes.view.net.entity.AppOrder;
import com.example.formandocodigo.psicotimes.view.net.entity.AppOrderResponse;
import com.example.formandocodigo.psicotimes.model.StateUse;
import com.example.formandocodigo.psicotimes.service.StateUseService;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";

    private PieChart pieChart;
    ArrayList<StateUse> stateUses = new ArrayList<>();

    OrderService service;
    Call<AppOrderResponse> call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        pieChart = findViewById(R.id.pie_chart_statistics);
        pieChart.setUsePercentValues(true);

        service = RetrofitBuilder.createService(OrderService.class);

        initializeService();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                syncUp();
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        setData();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopService(new Intent(MainActivity.this, StateUseService.class));
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        initializeService();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (call != null)  {
            call.cancel();
            call = null;
        }
        stopService(new Intent(MainActivity.this, StateUseService.class));
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
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void initializeService() {
        if (checkForPermission(this)) {
            startService(new Intent(MainActivity.this, StateUseService.class));
        } else {
            requestPermission();
        }
    }

    private boolean checkForPermission(Context context) {
        AppOpsManager appOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, Process.myUid(), context.getPackageName());
        return mode == AppOpsManager.MODE_ALLOWED;
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

    void setData() {
        getAppAll();

        ArrayList<PieEntry> yVals = new ArrayList<>();
        ArrayList<String> xVals = new ArrayList<>();

        PieDataSet dataSet;

        if (stateUses.size() > 0) {
            for (int i = 0; i < stateUses.size(); i++) {
                yVals.add(new PieEntry(stateUses.get(i).getQuantity(), i));
                xVals.add(stateUses.get(i).getNameApplication());
            }
            dataSet = new PieDataSet(yVals, "Cantidad de uso");
            dataSet.setSelectionShift(stateUses.size());
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
        ArrayList<Integer> colors = new ArrayList<>();

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


    void getAppAll() {
        StateUseCacheImpl read = new StateUseCacheImpl(this, new Serializer(), new FileManager());
        StateUseEntityDataMapper stateUseMapper = new StateUseEntityDataMapper();
        if (read.getAll() != null) {
            stateUses = stateUseMapper.transformArrayList(read.getAll());
        }
    }

    void syncUp() {

        if (stateUses != null) {
            AppOrder order = new AppOrder();

            order.setToken("5WnKwTopjztqMPIZpKWwxVl40jlGdQVLTtSsueoQwAjWFzgSWcli1TjPmRws");
            order.setEmail("k_npc2009@hotmail.com");

            Toast.makeText(this, "" + stateUses.size(), Toast.LENGTH_LONG).show();

            order.setStateUses(stateUses);

            call = service.appOrder(order);

            call.enqueue(new Callback<AppOrderResponse>() {

                @Override
                public void onResponse(Call<AppOrderResponse> call, Response<AppOrderResponse> response) {
                    if (response.isSuccessful()) {
                        Log.w(TAG, "onResponse: " + response.body());
                        //repository.signIn(response.body());
                    } else {
                        Log.w(TAG, "onResponse: " + response.errorBody());
                        //handleErrors(response.errorBody());
                    }
                }

                @Override
                public void onFailure(Call<AppOrderResponse> call, Throwable t) {
                    Log.w(TAG, "onFailure: " + t.getMessage());
                }
            });
        }

    }

}
