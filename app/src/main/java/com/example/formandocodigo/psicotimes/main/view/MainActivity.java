package com.example.formandocodigo.psicotimes.main.view;

import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.formandocodigo.psicotimes.R;
import com.example.formandocodigo.psicotimes.adapter.StatisticsDetailAdapterRecyclerView;
import com.example.formandocodigo.psicotimes.entity.StatisticsDetail;
import com.example.formandocodigo.psicotimes.main.presenter.MainPresenter;
import com.example.formandocodigo.psicotimes.main.presenter.MainPresenterImpl;
import com.example.formandocodigo.psicotimes.service.StateUseService;
import com.example.formandocodigo.psicotimes.utils.Converts;
import com.example.formandocodigo.psicotimes.utils.net.NetworkStatus;
import com.example.formandocodigo.psicotimes.utils.permission.UsageStatsPermission;
import com.example.formandocodigo.psicotimes.view.RecordActivity;
import com.example.formandocodigo.psicotimes.view.RecordDayActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, MainView {

    @BindView(R.id.txt_total_time_use_today)
    TextView txtTotalTimeUseToday;
    @BindView(R.id.txt_quantity_unlock_screen)
    TextView txtQuantityUnlockScreen;
    @BindView(R.id.txt_quantity_nro_apps)
    TextView txtQuantityNroApps;

    private MainPresenter presenter;

    ArrayList<StatisticsDetail> statisticsDetails;

    RecyclerView statisticsDetailCurrentDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayout.VERTICAL);

        statisticsDetailCurrentDate = findViewById(R.id.statistics_recycler);
        statisticsDetailCurrentDate.setLayoutManager(linearLayoutManager);

        ButterKnife.bind(this);

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
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        //startActivity(new Intent(MainActivity.this, SplashScreenActivity.class));
        //finish();
        initializeGraphics();
        initializeService();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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

        if (id == R.id.nav_record) {
            Intent intent = new Intent(MainActivity.this, RecordActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_record_date) {
            Intent intent = new Intent(MainActivity.this, RecordDayActivity.class);
            startActivity(intent);
        }  else if (id == R.id.nav_logout) {
            // Implement
        } else if (id == R.id.nav_exit) {
            finish();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private boolean isOnline() {
        return NetworkStatus.isOnline(this);
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
        getStatisticsDetailCurrentDate();
        setTotalTime();
        setRecyclerView();
        setQuantityUnLockChart();
        setQuantityNroApps();
    }

    private void setRecyclerView() {
        StatisticsDetailAdapterRecyclerView recyclerView =
                new StatisticsDetailAdapterRecyclerView(statisticsDetails, R.layout.cardview_state_use_main, this);

        statisticsDetailCurrentDate.setAdapter(recyclerView);
    }

    private void setQuantityUnLockChart() {
        int quantity = presenter.quantityUnlockScreen(this);
        txtQuantityUnlockScreen.setText(String.valueOf(quantity));
    }

    private void setQuantityNroApps() {
        int quantity = presenter.quantityNroApps(this);
        txtQuantityNroApps.setText(String.valueOf(quantity));
    }

    private void getStatisticsDetailCurrentDate() {
        statisticsDetails = new ArrayList<>(presenter.getStatisticsDetailCurrentDate());
    }

    private void setTotalTime() {
        if (statisticsDetails.size() > 0) {
            long timeTotal = getTotalUseTime(statisticsDetails);
            txtTotalTimeUseToday.setText(Converts.convertLongToTimeSimple(timeTotal));
        }
    }

    private long getTotalUseTime(ArrayList<StatisticsDetail> statisticsDetails) {
        long total = 0;

        for (StatisticsDetail s : statisticsDetails) {
            total += s.getTimeUse();
        }
        return total;
    }

}
