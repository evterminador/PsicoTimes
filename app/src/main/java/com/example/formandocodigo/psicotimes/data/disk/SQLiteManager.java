package com.example.formandocodigo.psicotimes.data.disk;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.formandocodigo.psicotimes.entity.ApplicationEntity;
import com.example.formandocodigo.psicotimes.entity.StateUser;
import com.example.formandocodigo.psicotimes.utils.Continual;
import com.example.formandocodigo.psicotimes.utils.Converts;

import java.util.ArrayList;

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
            app.COLUMN_RELEVANCE_ + " INTEGER NOT NULL," +
            app.COLUMN_IMAGE + " TEXT NOT NULL," +
            app.COLUMN_DESCRIPTION + " TEXT NOT NULL," +
            app.COLUMN_CREATED_AT + " text," +
            app.COLUMN_UPDATED_AT + " text " +
            ")";

    private static final String CREATE_STATE_USES_TABLE = "create table " +
            stateUse.TABLE_NAME + " ( " +
            stateUse.COLUMN_ID_USERS + " integer, " +
            stateUse.COLUMN_ID_APP + " integer, " +
            stateUse.COLUMN_TIME_USE + " REAL not null, " +
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

    public void insertAppAll(ArrayList<ApplicationEntity> list) {
        db = this.getWritableDatabase();
        ContentValues values;

        for (ApplicationEntity s : list) {
            values = new ContentValues();
            values.put(app.COLUMN_ID, s.getId());
            values.put(app.COLUMN_NAME, s.getName());
            values.put(app.COLUMN_RELEVANCE_, s.getRelevance());
            values.put(app.COLUMN_IMAGE, s.getImage());
            values.put(app.COLUMN_DESCRIPTION, s.getDescription());
            values.put(app.COLUMN_CREATED_AT, Converts.convertTimestampToString(s.getCreated_at()));
            values.put(app.COLUMN_UPDATED_AT, Converts.convertTimestampToString(s.getUpdated_at()));

            db.insert(app.TABLE_NAME, null, values);
        }
    }

    public ArrayList<StateUser> getAllStateUser() {
        ArrayList<StateUser> all = new ArrayList<>();
        Cursor c = null;

        try {
            c = this.getReadableDatabase().rawQuery("select * from " + stateUse.TABLE_NAME, null);

            StateUser userEntity;
            while (c.moveToNext()) {
                userEntity = new StateUser();
                userEntity.setId_users(c.getInt(0));
                userEntity.setId_app(c.getInt(1));
                userEntity.setTimeUse(c.getLong(2));
                userEntity.setQuantity(c.getInt(3));
                userEntity.setLastUseTime(Converts.convertStringToTimestamp(c.getString(4)));
                userEntity.setCreated_at(Converts.convertStringToTimestamp(c.getString(5)));
                userEntity.setUpdated_at(Converts.convertStringToTimestamp(c.getString(6)));

                all.add(userEntity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            c.close();
        }
        return all;
    }

    public void insertStateUser(StateUser userEntity) {
        db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(stateUse.COLUMN_ID_USERS, userEntity.getId_users());
        values.put(stateUse.COLUMN_ID_APP, userEntity.getId_app());
        values.put(stateUse.COLUMN_TIME_USE, userEntity.getTimeUse());
        values.put(stateUse.COLUMN_QUANTITY, userEntity.getQuantity());
        values.put(stateUse.COLUMN_LAST_USE_TIME, Converts.convertTimestampToString(userEntity.getLastUseTime()));
        values.put(stateUse.COLUMN_CREATED_AT, Converts.convertTimestampToString(userEntity.getCreated_at()));
        values.put(stateUse.COLUMN_UPDATED_AT, Converts.convertTimestampToString(userEntity.getUpdated_at()));

        db.insert(stateUse.TABLE_NAME, null, values);
    }

    public void updateStateUser(StateUser userEntity) {
        ContentValues values = new ContentValues();
        values.put(stateUse.COLUMN_TIME_USE, userEntity.getTimeUse());
        values.put(stateUse.COLUMN_QUANTITY, userEntity.getQuantity());
        values.put(stateUse.COLUMN_LAST_USE_TIME, Converts.convertTimestampToString(userEntity.getLastUseTime()));
        values.put(stateUse.COLUMN_UPDATED_AT, Converts.convertTimestampToString(userEntity.getUpdated_at()));

        db.update(stateUse.TABLE_NAME, values,
                stateUse.COLUMN_ID_APP + "=" + userEntity.getId_app() + " and " +
                        stateUse.COLUMN_TIME_USE + " = " + userEntity.getTimeUse() + " and " +
                        stateUse.COLUMN_CREATED_AT + " = " +  Converts.convertTimestampToString(userEntity.getCreated_at())
                ,null);
    }

    public void deleteStateUSer(StateUser userEntity) {
        db = this.getWritableDatabase();

        db.delete(stateUse.TABLE_NAME,
                stateUse.COLUMN_ID_APP + "=" + userEntity.getId_app() + " and " +
                        stateUse.COLUMN_TIME_USE + " = " + userEntity.getTimeUse() + " and " +
                        stateUse.COLUMN_CREATED_AT + " = " + Converts.convertTimestampToString(userEntity.getCreated_at()),
                null);
    }

}
