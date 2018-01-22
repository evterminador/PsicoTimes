package com.example.formandocodigo.psicotimes.data.disk;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.formandocodigo.psicotimes.entity.AppTop;
import com.example.formandocodigo.psicotimes.entity.HistoricState;
import com.example.formandocodigo.psicotimes.entity.StatisticsDetail;
import com.example.formandocodigo.psicotimes.utils.Continual;
import com.example.formandocodigo.psicotimes.utils.Converts;

import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * Created by FormandoCodigo on 13/12/2017.
 */

public class SQLiteManager extends SQLiteOpenHelper {

    private static Continual.SQLite.AppTop appTop = new Continual.SQLite.AppTop();
    private static Continual.SQLite.Statistics statistics = new Continual.SQLite.Statistics();
    private static Continual.SQLite.StatisticsDetail statisticsDetail = new Continual.SQLite.StatisticsDetail();

    private static final String CREATE_APP_TOP = "create table " +
            appTop.TABLE_NAME + " ( " +
            appTop.COLUMN_ID + " integer primary key AUTOINCREMENT, " +
            appTop.COLUMN_NAME_APP + " text not null, " +
            appTop.COLUMN_IMAGE + " text not null, " +
            appTop.COLUMN_QUANTITY + " integer not null, " +
            appTop.COLUMN_TIME_USE + " integer not null, " +
            appTop.COLUMN_LAST_POSITION + " integer, " +
            appTop.COLUMN_CREATED_AT + " text, " +
            appTop.COLUMN_UPDATED_AT + " text " +
            ")";

    private static final String CREATE_STATISTICS_TABLE = "create table " +
            statistics.TABLE_NAME + " ( " +
            statistics.COLUMN_ID + " integer primary key AUTOINCREMENT, " +
            statistics.COLUMN_NAME_APP_TOP + " text, " +
            statistics.COLUMN_QUANTITY + " integer, " +
            statistics.COLUMN_TIME_USE + " integer, " +
            statistics.COLUMN_NRO_UNLOCK + " integer, " +
            statistics.COLUMN_CREATED_AT + " text, " +
            statistics.COLUMN_UPDATED_AT + " text " +
            ")";

    private static final String CREATE_STATISTICS_DETAIL_TABLE = "create table " +
            statisticsDetail.TABLE_NAME + " (  " +
            statisticsDetail.COLUMN_NAME_APP + " text, " +
            statisticsDetail.COLUMN_IMAGE + " text, " +
            statisticsDetail.COLUMN_QUANTITY + " integer, " +
            statisticsDetail.COLUMN_TIME_USE + " integer, " +
            statisticsDetail.COLUMN_LAST_USE_TIME + " text, " +
            statisticsDetail.COLUMN_CREATED_AT + " text, " +
            statisticsDetail.COLUMN_UPDATED_AT + " text " +
            ")";

    private static SQLiteManager instance;

    public static SQLiteManager Instance() throws Exception {
        if (instance == null)
            throw new Exception("Implementar method initialize");

        return instance;
    }

    public static void Initialize(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        if (instance != null)
            instance.close();

        instance = new SQLiteManager(context, name, factory, version);
    }

    private SQLiteDatabase db;

    private SQLiteManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_APP_TOP);
        sqLiteDatabase.execSQL(CREATE_STATISTICS_TABLE);
        sqLiteDatabase.execSQL(CREATE_STATISTICS_DETAIL_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    /**
     * Contents for application tops
     *
     */
    public void insertAppTop(AppTop a) {
        db = this.getWritableDatabase();

        ContentValues values  = new ContentValues();

        values.put(appTop.COLUMN_ID, a.getId());
        values.put(appTop.COLUMN_NAME_APP, a.getName());
        values.put(appTop.COLUMN_IMAGE, a.getImage());
        values.put(appTop.COLUMN_QUANTITY, a.getQuantity());
        values.put(appTop.COLUMN_TIME_USE, a.getTimeUse());
        values.put(appTop.COLUMN_LAST_POSITION, a.getLastPosition());
        values.put(appTop.COLUMN_CREATED_AT, Converts.convertTimestampToString(a.getCreated_at()));
        values.put(appTop.COLUMN_UPDATED_AT, Converts.convertTimestampToString(a.getUpdated_at()));

        db.insert(appTop.TABLE_NAME, null, values);
    }

    public ArrayList<AppTop> getAppTopAll () {
        ArrayList<AppTop> all = new ArrayList<>();
        Cursor c = null;

        try {
            c = this.getReadableDatabase().rawQuery("select * from " + appTop.TABLE_NAME, null);

            AppTop appTop;
            while (c.moveToNext()) {
                appTop = new AppTop();
                appTop.setId(c.getInt(0));
                appTop.setName(c.getString(1));
                appTop.setImage(c.getString(2));
                appTop.setQuantity(c.getInt(3));
                appTop.setTimeUse(c.getLong(4));
                appTop.setLastPosition(c.getInt(5));
                appTop.setCreated_at(Converts.convertStringToTimestamp(c.getString(6)));
                appTop.setUpdated_at(Converts.convertStringToTimestamp(c.getString(7)));

                all.add(appTop);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (c != null) c.close();
        }
        return all;
    }

    public void updateAppTop(AppTop a) {
        db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(appTop.COLUMN_NAME_APP, a.getName());
        values.put(appTop.COLUMN_IMAGE, a.getImage());
        values.put(appTop.COLUMN_QUANTITY, a.getQuantity());
        values.put(appTop.COLUMN_TIME_USE, a.getTimeUse());
        values.put(appTop.COLUMN_LAST_POSITION, a.getLastPosition());
        values.put(appTop.COLUMN_UPDATED_AT, Converts.convertTimestampToString(a.getUpdated_at()));

        db.update(appTop.TABLE_NAME, values, appTop.COLUMN_CREATED_AT + " = ?"
                , new String[] { Converts.convertTimestampToString(a.getCreated_at()) });
    }

    /**
     * Contents for statistics
     */
    public void insertHistoricState(HistoricState historicState) {
        db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(statistics.COLUMN_NAME_APP_TOP, historicState.getNameAppTop());
        values.put(statistics.COLUMN_QUANTITY, historicState.getQuantity());
        values.put(statistics.COLUMN_TIME_USE, historicState.getTimeUse());
        values.put(statistics.COLUMN_NRO_UNLOCK, historicState.getNroUnlock());
        values.put(statistics.COLUMN_CREATED_AT, Converts.convertTimestampToString(historicState.getCreated_at()));
        values.put(statistics.COLUMN_UPDATED_AT, Converts.convertTimestampToString(historicState.getUpdated_at()));

        db.insert(statistics.TABLE_NAME, null, values);
    }

    public ArrayList<HistoricState> getHistoricStateAll() {
        ArrayList<HistoricState> list = new ArrayList<>();
        Cursor c = null;

        try {
            String sql = "select * from " + statistics.TABLE_NAME;

            c = this.getReadableDatabase().rawQuery(sql, null);

            HistoricState historicState;
            while (c.moveToNext()) {
                historicState = new HistoricState();

                historicState.setId(c.getInt(0));
                historicState.setNameAppTop(c.getString(1));
                historicState.setQuantity(c.getInt(2));
                historicState.setTimeUse(c.getLong(3));
                historicState.setNroUnlock(c.getInt(4));
                historicState.setCreated_at(Converts.convertStringToTimestamp(c.getString(5)));
                historicState.setUpdated_at(Converts.convertStringToTimestamp(c.getString(6)));

                list.add(historicState);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (c != null) c.close();
        }
        return list;
    }

    public HistoricState findHistoricState(int id) {
        HistoricState historicState = null;
        Cursor c = null;

        try {
            String sql = "select * from " + statistics.TABLE_NAME + " where " +
                    statistics.COLUMN_ID + " = " + id;

            c = this.getReadableDatabase().rawQuery(sql, null);

            while (c.moveToNext()) {
                historicState = new HistoricState();
                historicState.setId(c.getInt(0));
                historicState.setNameAppTop(c.getString(1));
                historicState.setQuantity(c.getInt(2));
                historicState.setTimeUse(c.getLong(3));
                historicState.setNroUnlock(c.getInt(4));
                historicState.setCreated_at(Converts.convertStringToTimestamp(c.getString(5)));
                historicState.setUpdated_at(Converts.convertStringToTimestamp(c.getString(6)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (c != null) c.close();
        }
        return historicState;
    }

    public void updateHistoricState(HistoricState historicState) {
        db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(statistics.COLUMN_NAME_APP_TOP, historicState.getNameAppTop());
        values.put(statistics.COLUMN_QUANTITY, historicState.getQuantity());
        values.put(statistics.COLUMN_TIME_USE, historicState.getTimeUse());
        values.put(statistics.COLUMN_NRO_UNLOCK, historicState.getNroUnlock());
        values.put(statistics.COLUMN_UPDATED_AT, Converts.convertTimestampToString(historicState.getUpdated_at()));

        db.update(statistics.TABLE_NAME, values,
                statistics.COLUMN_CREATED_AT + " = ?",
                new String[] { Converts.convertTimestampToString(historicState.getCreated_at()) });
    }

    /**
     * Contents for statistics detail
     */
    public void insertStatisticsDetail(StatisticsDetail sDetail) {
        db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(statisticsDetail.COLUMN_NAME_APP, sDetail.getNameApp());
        values.put(statisticsDetail.COLUMN_IMAGE, sDetail.getImage());
        values.put(statisticsDetail.COLUMN_QUANTITY, sDetail.getQuantity());
        values.put(statisticsDetail.COLUMN_TIME_USE, sDetail.getTimeUse());
        values.put(statisticsDetail.COLUMN_LAST_USE_TIME, Converts.convertTimestampToString(sDetail.getLastUseTime()));
        values.put(statisticsDetail.COLUMN_CREATED_AT, Converts.convertTimestampToString(sDetail.getCreated_at()));
        values.put(statisticsDetail.COLUMN_UPDATED_AT, Converts.convertTimestampToString(sDetail.getUpdated_at()));

        db.insert(statisticsDetail.TABLE_NAME, null, values);
    }

    public ArrayList<StatisticsDetail> getStatisticsDetailAll() {
        ArrayList<StatisticsDetail> list = new ArrayList<>();
        Cursor c = null;

        try {
            String sql = "select * from " + statisticsDetail.TABLE_NAME;

            c = this.getReadableDatabase().rawQuery(sql, null);

            StatisticsDetail detail;
            while (c.moveToNext()) {
                detail = new StatisticsDetail();
                detail.setNameApp(c.getString(0));
                detail.setImage(c.getString(1));
                detail.setQuantity(c.getInt(2));
                detail.setTimeUse(c.getLong(3));
                detail.setLastUseTime(Converts.convertStringToTimestamp(c.getString(4)));
                detail.setCreated_at(Converts.convertStringToTimestamp(c.getString(5)));
                detail.setUpdated_at(Converts.convertStringToTimestamp(c.getString(6)));

                list.add(detail);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (c != null) c.close();
        }
        return list;
    }

    public ArrayList<StatisticsDetail> findStatisticsDetailByDate(Timestamp date, Timestamp date1) {
        ArrayList<StatisticsDetail> list = new ArrayList<>();
        Cursor c = null;

        try {
            String sql = "select * from " + statisticsDetail.TABLE_NAME +
                    " where " +  statisticsDetail.COLUMN_CREATED_AT + " between '" +
                    Converts.convertTimestampToString(date) + "' and '" + Converts.convertTimestampToString(date1) + "'";

            c = this.getReadableDatabase().rawQuery(sql, null);

            StatisticsDetail sDetail;
            while (c.moveToNext()) {
                sDetail = new StatisticsDetail();
                sDetail.setNameApp(c.getString(0));
                sDetail.setImage(c.getString(1));
                sDetail.setQuantity(c.getInt(2));
                sDetail.setTimeUse(c.getLong(3));
                sDetail.setLastUseTime(Converts.convertStringToTimestamp(c.getString(4)));
                sDetail.setCreated_at(Converts.convertStringToTimestamp(c.getString(5)));
                sDetail.setUpdated_at(Converts.convertStringToTimestamp(c.getString(6)));

                list.add(sDetail);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (c != null) c.close();
        }
        return list;
    }

    public ArrayList<StatisticsDetail> findStatisticsDetailByDateSort(Timestamp date, Timestamp date1) {
        ArrayList<StatisticsDetail> list = new ArrayList<>();
        Cursor c = null;

        try {
            String sql = "select " + statisticsDetail.COLUMN_NAME_APP + ", " +
                    statisticsDetail.COLUMN_IMAGE + ", " +
                    "sum(" + statisticsDetail.COLUMN_QUANTITY + "), " +
                    "sum(" + statisticsDetail.COLUMN_TIME_USE + ") " +
                    " from " + statisticsDetail.TABLE_NAME +
                    " where " +  statisticsDetail.COLUMN_CREATED_AT + " between '" +
                    Converts.convertTimestampToString(date) + "' and '" + Converts.convertTimestampToString(date1) + "' " +
                    "group by " + statisticsDetail.COLUMN_NAME_APP + " order by " + statisticsDetail.COLUMN_TIME_USE + " desc";

            c = this.getReadableDatabase().rawQuery(sql, null);

            StatisticsDetail sDetail;
            while (c.moveToNext()) {
                sDetail = new StatisticsDetail();
                sDetail.setNameApp(c.getString(0));
                sDetail.setImage(c.getString(1));
                sDetail.setQuantity(c.getInt(2));
                sDetail.setTimeUse(c.getLong(3));

                list.add(sDetail);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (c != null) c.close();
        }
        return list;
    }

    public void updateStatisticsDetail(StatisticsDetail s, StatisticsDetail s1) {
        db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(statisticsDetail.COLUMN_NAME_APP, s.getNameApp());
        values.put(statisticsDetail.COLUMN_IMAGE, s.getImage());
        values.put(statisticsDetail.COLUMN_QUANTITY, s.getQuantity());
        values.put(statisticsDetail.COLUMN_TIME_USE, s.getTimeUse());
        values.put(statisticsDetail.COLUMN_LAST_USE_TIME, Converts.convertTimestampToString(s.getLastUseTime()));
        values.put(statisticsDetail.COLUMN_UPDATED_AT, Converts.convertTimestampToString(s.getUpdated_at()));

        db.update(statisticsDetail.TABLE_NAME, values, statisticsDetail.COLUMN_NAME_APP + " = ? and " +
                statisticsDetail.COLUMN_CREATED_AT + " = ?"
                , new String[] { s.getNameApp(), Converts.convertTimestampToString(s1.getCreated_at()) });
    }



}
