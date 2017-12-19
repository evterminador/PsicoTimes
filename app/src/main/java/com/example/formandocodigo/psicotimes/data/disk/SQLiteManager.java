package com.example.formandocodigo.psicotimes.data.disk;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.formandocodigo.psicotimes.entity.App;
import com.example.formandocodigo.psicotimes.entity.StateUse;
import com.example.formandocodigo.psicotimes.entity.StateUser;
import com.example.formandocodigo.psicotimes.utils.Continual;
import com.example.formandocodigo.psicotimes.utils.Converts;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by FormandoCodigo on 13/12/2017.
 */

public class SQLiteManager extends SQLiteOpenHelper {

    private static Continual.SQLite.App app = new Continual.SQLite.App();
    private static Continual.SQLite.StateUse stateUse = new Continual.SQLite.StateUse();

    private static final String CREATE_APP_TABLE = "create table " +
            app.TABLE_NAME + " ( " +
            app.COLUMN_ID + " INTEGER PRIMARY KEY," +
            app.COLUMN_NAME +  " TEXT NOT NULL," +
            app.COLUMN_RELEVANCE + " INTEGER NOT NULL," +
            app.COLUMN_IMAGE + " TEXT NOT NULL," +
            app.COLUMN_DESCRIPTION + " TEXT NOT NULL," +
            app.COLUMN_CREATED_AT + " text," +
            app.COLUMN_UPDATED_AT + " text " +
            ")";

    private static final String CREATE_STATE_USES_TABLE = "create table " +
            stateUse.TABLE_NAME + " ( " +
            stateUse.COLUMN_ID_USERS + " integer, " +
            stateUse.COLUMN_ID_APP + " integer, " +
            stateUse.COLUMN_TIME_USE + " integer not null, " +
            stateUse.COLUMN_QUANTITY + " integer not null, " +
            stateUse.COLUMN_LAST_USE_TIME + " text not null, " +
            stateUse.COLUMN_CREATED_AT + " text, " +
            stateUse.COLUMN_UPDATED_AT + " text, " +
            "foreign key (id_app) references app (id) " +
            ") ";

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
        sqLiteDatabase.execSQL(CREATE_APP_TABLE);
        sqLiteDatabase.execSQL(CREATE_STATE_USES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void insertApp(App a) {
        db = this.getWritableDatabase();

        ContentValues values  = new ContentValues();

        values.put(app.COLUMN_ID, a.getId());
        values.put(app.COLUMN_NAME, a.getName());
        values.put(app.COLUMN_RELEVANCE, a.getRelevance());
        values.put(app.COLUMN_IMAGE, a.getImage());
        values.put(app.COLUMN_DESCRIPTION, a.getDescription());
        values.put(app.COLUMN_CREATED_AT, Converts.convertTimestampToString(a.getCreated_at()));
        values.put(app.COLUMN_UPDATED_AT, Converts.convertTimestampToString(a.getUpdated_at()));

        db.insert(app.TABLE_NAME, null, values);

    }

    public ArrayList<App> getAllApp () {
        ArrayList<App> all = new ArrayList<>();
        Cursor c = null;

        try {
            c = this.getReadableDatabase().rawQuery("select * from " + app.TABLE_NAME, null);

            App app;
            while (c.moveToNext()) {
                app = new App();
                app.setId(c.getInt(0));
                app.setName(c.getString(1));
                app.setRelevance(c.getInt(2));
                app.setImage(c.getString(3));
                app.setDescription(c.getString(4));
                app.setCreated_at(Converts.convertStringToTimestamp(c.getString(5)));
                app.setUpdated_at(Converts.convertStringToTimestamp(c.getString(6)));

                all.add(app);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            c.close();
        }
        return all;
    }

    public ArrayList<StateUser> getAllStateUser() {
        ArrayList<StateUser> all = new ArrayList<>();
        Cursor c = null;

        try {
            c = this.getReadableDatabase().rawQuery("select * from " + stateUse.TABLE_NAME, null);

            StateUser stateUser;
            while (c.moveToNext()) {
                stateUser = new StateUser();
                stateUser.setId_users(c.getInt(0));
                stateUser.setId_app(c.getInt(1));
                stateUser.setTimeUse(c.getLong(2));
                stateUser.setQuantity(c.getInt(3));
                stateUser.setLastUseTime(Converts.convertStringToTimestamp(c.getString(4)));
                stateUser.setCreated_at(Converts.convertStringToTimestamp(c.getString(5)));
                stateUser.setUpdated_at(Converts.convertStringToTimestamp(c.getString(6)));

                all.add(stateUser);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            c.close();
        }
        return all;
    }

    public List<StateUse> getStateUses() {
        List<StateUse> list = new ArrayList<>();
        Cursor c = null;

        try {
            final String sql = "select " + " a." + app.COLUMN_ID +
                    ", a." + app.COLUMN_NAME +
                    ", a." + app.COLUMN_IMAGE +
                    ", s." + stateUse.COLUMN_TIME_USE +
                    ", sum(s." + stateUse.COLUMN_QUANTITY + ") " +
                    ", s." + stateUse.COLUMN_LAST_USE_TIME +
                    ", s." + stateUse.COLUMN_CREATED_AT +
                    ", s." + stateUse.COLUMN_UPDATED_AT +
                    " from " + stateUse.TABLE_NAME + " as s " +
                    "inner join " + app.TABLE_NAME + " as a " +
                    "on a." + app.COLUMN_ID + " = s." + stateUse.COLUMN_ID_APP + " " +
                    "group by a." + app.COLUMN_NAME;

            c = this.getReadableDatabase().rawQuery(sql, null);

            StateUse stateUse;
            while (c.moveToNext()) {
                stateUse = new StateUse();
                stateUse.setId(c.getInt(0));
                stateUse.setNameApplication(c.getString(1));
                stateUse.setImageApp(c.getString(2));
                stateUse.setUseTime(c.getLong(3));
                stateUse.setQuantity(c.getInt(4));
                stateUse.setLastUseTime(Converts.convertStringToTimestamp(c.getString(5)));
                stateUse.setCreated_at(Converts.convertStringToTimestamp(c.getString(6)));
                stateUse.setUpdated_at(Converts.convertStringToTimestamp(c.getString(7)));

                list.add(stateUse);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            c.close();
        }
        return list;
    }

    public ArrayList<StateUse> findStateUseById(Integer id) {
        ArrayList<StateUse> list = new ArrayList<>();
        Cursor c = null;
        try {
            final String sql = "select " + " a." + app.COLUMN_ID +
                    ", a." + app.COLUMN_NAME +
                    ", a." + app.COLUMN_IMAGE +
                    ", s." + stateUse.COLUMN_TIME_USE +
                    ", s." + stateUse.COLUMN_QUANTITY +
                    ", s." + stateUse.COLUMN_LAST_USE_TIME +
                    ", s." + stateUse.COLUMN_CREATED_AT +
                    ", s." + stateUse.COLUMN_UPDATED_AT +
                    " from " + stateUse.TABLE_NAME + " as s " +
                    "inner join " + app.TABLE_NAME + " as a " +
                    "on a." + app.COLUMN_ID + " = s." + stateUse.COLUMN_ID_APP + " " +
                    "where a." + app.COLUMN_ID + " = " + id;

            c = this.getReadableDatabase().rawQuery(sql, null);

            StateUse use;
            while (c.moveToNext()) {
                use = new StateUse();
                use.setId(c.getInt(0));
                use.setNameApplication(c.getString(1));
                use.setImageApp(c.getString(2));
                use.setUseTime(c.getLong(3));
                use.setQuantity(c.getInt(4));
                use.setLastUseTime(Converts.convertStringToTimestamp(c.getString(5)));
                use.setCreated_at(Converts.convertStringToTimestamp(c.getString(6)));
                use.setUpdated_at(Converts.convertStringToTimestamp(c.getString(7)));

                list.add(use);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<StateUse> getStateUsesByDate () {
        List<StateUse> list = new ArrayList<>();
        Cursor c = null;

        try {
            final String sql = "select a." + app.COLUMN_NAME +
                    ", a." + app.COLUMN_IMAGE +
                    ", s." + stateUse.COLUMN_TIME_USE +
                    ", s." + stateUse.COLUMN_QUANTITY +
                    ", s." + stateUse.COLUMN_LAST_USE_TIME +
                    ", s." + stateUse.COLUMN_CREATED_AT +
                    ", s." + stateUse.COLUMN_UPDATED_AT +
                    " from " + stateUse.TABLE_NAME + " as s " +
                    "inner join " + app.TABLE_NAME + " as a " +
                    "on a." + app.COLUMN_ID + " = s." + stateUse.COLUMN_ID_APP;

            c = this.getReadableDatabase().rawQuery(sql, null);

            StateUse stateUse;
            while (c.moveToNext()) {
                stateUse = new StateUse();
                stateUse.setId(c.getInt(0));
                stateUse.setNameApplication(c.getString(1));
                stateUse.setImageApp(c.getString(2));
                stateUse.setUseTime(c.getLong(3));
                stateUse.setQuantity(c.getInt(4));
                stateUse.setLastUseTime(Converts.convertStringToTimestamp(c.getString(5)));
                stateUse.setCreated_at(Converts.convertStringToTimestamp(c.getString(6)));
                stateUse.setUpdated_at(Converts.convertStringToTimestamp(c.getString(7)));

                list.add(stateUse);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            c.close();
        }
        return list;
    }

    public void insertStateUser(StateUser stateUser) {
        db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(stateUse.COLUMN_ID_USERS, stateUser.getId_users());
        values.put(stateUse.COLUMN_ID_APP, stateUser.getId_app());
        values.put(stateUse.COLUMN_TIME_USE, stateUser.getTimeUse());
        values.put(stateUse.COLUMN_QUANTITY, stateUser.getQuantity());
        values.put(stateUse.COLUMN_LAST_USE_TIME, Converts.convertTimestampToString(stateUser.getLastUseTime()));
        values.put(stateUse.COLUMN_CREATED_AT, Converts.convertTimestampToString(stateUser.getCreated_at()));
        values.put(stateUse.COLUMN_UPDATED_AT, Converts.convertTimestampToString(stateUser.getUpdated_at()));

        db.insert(stateUse.TABLE_NAME, null, values);
    }

    public void updateStateUser(StateUser stateUser) {
        db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(stateUse.COLUMN_TIME_USE, stateUser.getTimeUse());
        values.put(stateUse.COLUMN_QUANTITY, stateUser.getQuantity());
        values.put(stateUse.COLUMN_LAST_USE_TIME, Converts.convertTimestampToString(stateUser.getLastUseTime()));
        values.put(stateUse.COLUMN_UPDATED_AT, Converts.convertTimestampToString(stateUser.getUpdated_at()));

        db.update(stateUse.TABLE_NAME, values,
                stateUse.COLUMN_ID_APP + "= ? " + " AND " +
                        stateUse.COLUMN_CREATED_AT + " = ? "
                , new String[] { String.valueOf(stateUser.getId_app()), Converts.convertTimestampToString(stateUser.getCreated_at()) });
    }

    public void updateApp(App a) {
        db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(app.COLUMN_NAME, a.getName());
        values.put(app.COLUMN_RELEVANCE, a.getRelevance());
        values.put(app.COLUMN_IMAGE, a.getImage());
        values.put(app.COLUMN_DESCRIPTION, a.getDescription());
        values.put(app.COLUMN_UPDATED_AT, Converts.convertTimestampToString(a.getUpdated_at()));

        db.update(app.TABLE_NAME, values, app.COLUMN_ID + " = " + a.getId(), null);
    }

    public void deleteStateUSer(StateUser stateUser) {
        db = this.getWritableDatabase();

        db.delete(stateUse.TABLE_NAME,
                stateUse.COLUMN_ID_APP + "= ? " + " and " +
                        stateUse.COLUMN_CREATED_AT + " = ? ",
                new String[] { String.valueOf(stateUser.getId_app()),
                        Converts.convertTimestampToString(stateUser.getCreated_at()) });
    }

}
