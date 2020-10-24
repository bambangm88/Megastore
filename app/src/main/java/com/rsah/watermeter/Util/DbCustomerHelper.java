package com.rsah.watermeter.Util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import com.rsah.watermeter.Constant.Constant;
import com.rsah.watermeter.Model.response.ResponseCustomer;
import com.rsah.watermeter.Model.response.ResponseDescription;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;

import static androidx.constraintlayout.widget.Constraints.TAG;


public class DbCustomerHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 2;

    public static final String DATABASE_NAME = "newwatermeterreader.db";

    public static final String TABLE_CUSTOMER = "customer";
    public static final String TABLE_DESCRIPTION = "description";
    public static final String TABLE_SYNC= "sync";
    public static final String TABLE_PERIOD= "period_id";

    public static final String COLUMN_ID_LOCAL = "idLocal";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_CST_ID = "customer_id";
    public static final String COLUMN_CST_NAME = "customer_name";
    public static final String COLUMN_REFERENCE = "reference";
    public static final String COLUMN_AREA = "area";
    public static final String COLUMN_ADDRESS = "address";
    public static final String COLUMN_COUNT_METER = "count_meter";
    public static final String COLUMN_IMAGE = "image";
    public static final String COLUMN_IMAGE_2 = "image_2";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_FINAL_METER = "final_meter";
    public static final String COLUMN_INITIAL_METER = "initial_meter";
    public static final String COLUMN_PREV_INITIAL_METER = "prev_initial_meter";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_STATUS_SYNC = "status_sync";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_ID_DESCRIPTION = "id_description";
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_STATUS_SERVER = "status_server";
    public static final String COLUMN_PERIOD_ID = "period_id";
    public static final String COLUMN_PERIOD_DESC = "period_desc";
    public static final String COLUMN_PREV_DATE_SCAN = "prev_date_scan";

    public static final String COLUMN_SYNC = "sync";

    public static final String COLUMN_APP = "app";

    public DbCustomerHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /*
    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + TABLE_SQLite + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY autoincrement, " +
                COLUMN_NAME + " TEXT NOT NULL, " +
                COLUMN_ADDRESS + " TEXT NOT NULL" +
                " )";

        db.execSQL(SQL_CREATE_MOVIE_TABLE);
    }*/

    @Override
    public void onCreate(SQLiteDatabase db) {


        final String SQL_CREATE_TABLE_CUSTOMER = "CREATE TABLE " + TABLE_CUSTOMER + " (" +
                COLUMN_ID_LOCAL + " INTEGER PRIMARY KEY autoincrement, " +
                COLUMN_ID + " TEXT NOT NULL, " +
                COLUMN_CST_ID + " TEXT NOT NULL, " +
                COLUMN_CST_NAME + " TEXT NOT NULL," +
                COLUMN_REFERENCE + " TEXT NOT NULL, " +
                COLUMN_AREA + " TEXT , " +
                COLUMN_ADDRESS + " TEXT ," +
                COLUMN_COUNT_METER + " TEXT ," +
                COLUMN_IMAGE + " TEXT, " +
                COLUMN_IMAGE_2 + " TEXT, " +
                COLUMN_DATE + " TEXT ," +
                COLUMN_FINAL_METER + " TEXT, " +
                COLUMN_USER_ID + " TEXT, " +
                COLUMN_STATUS_SYNC + " TEXT ," +
                COLUMN_DESCRIPTION + " TEXT ," +
                COLUMN_STATUS + " TEXT ," +
                COLUMN_PERIOD_ID + " TEXT, " +
                COLUMN_STATUS_SERVER+ " TEXT, " +
                COLUMN_PREV_INITIAL_METER+ " TEXT, " +
                COLUMN_INITIAL_METER+ " TEXT, " +
                COLUMN_PREV_DATE_SCAN+ " TEXT " +

                " )";


        final String SQL_CREATE_TABLE_DESCRIPTION = "CREATE TABLE " + TABLE_DESCRIPTION + " (" +
                COLUMN_ID_LOCAL+ " INTEGER PRIMARY KEY autoincrement, " +
                COLUMN_ID_DESCRIPTION + " TEXT NOT NULL, " +
                COLUMN_DESCRIPTION + " TEXT " +
                " )";

        final String SQL_CREATE_TABLE_SYNC = "CREATE TABLE " + TABLE_SYNC + " (" +
                COLUMN_ID_LOCAL+ " INTEGER PRIMARY KEY autoincrement, " +
                COLUMN_APP + " TEXT, " +
                COLUMN_PERIOD_ID + " TEXT, " +
                COLUMN_PERIOD_DESC + " TEXT, " +
                COLUMN_SYNC + " TEXT " +
                " )";

        db.execSQL(SQL_CREATE_TABLE_DESCRIPTION);
        db.execSQL(SQL_CREATE_TABLE_CUSTOMER);
        db.execSQL(SQL_CREATE_TABLE_SYNC);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CUSTOMER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DESCRIPTION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SYNC);
        onCreate(db);
    }

    public ArrayList<HashMap<String, String>> getCustomer(String reference) {
        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT * FROM " + TABLE_CUSTOMER + " WHERE " + COLUMN_REFERENCE + "=" + "'" + reference + "'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put(COLUMN_ID, cursor.getString(1));
                map.put(COLUMN_CST_ID, cursor.getString(2));
                map.put(COLUMN_CST_NAME, cursor.getString(3));
                map.put(COLUMN_REFERENCE, cursor.getString(4));
                map.put(COLUMN_AREA, cursor.getString(5));
                map.put(COLUMN_ADDRESS, cursor.getString(6));
                map.put(COLUMN_COUNT_METER, cursor.getString(7));
                map.put(COLUMN_IMAGE, cursor.getString(8));
                map.put(COLUMN_IMAGE_2, cursor.getString(9));
                map.put(COLUMN_DATE, cursor.getString(10));
                map.put(COLUMN_FINAL_METER, cursor.getString(11));
                map.put(COLUMN_USER_ID, cursor.getString(12));
                map.put(COLUMN_STATUS_SYNC, cursor.getString(13));
                map.put(COLUMN_DESCRIPTION, cursor.getString(14));
                map.put(COLUMN_STATUS, cursor.getString(15));
                map.put(COLUMN_PERIOD_ID, cursor.getString(16));
                map.put(COLUMN_STATUS_SERVER, cursor.getString(17));
                map.put(COLUMN_PREV_INITIAL_METER, cursor.getString(18));
                map.put(COLUMN_INITIAL_METER, cursor.getString(19));
                map.put(COLUMN_PREV_DATE_SCAN, cursor.getString(20));

                wordList.add(map);
            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();
        return wordList;
    }


    public ArrayList<HashMap<String, String>> getAllCustomerByStatus(String status) {
        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT * FROM " + TABLE_CUSTOMER + " WHERE " + COLUMN_STATUS_SYNC + "=" + "'" + status+ "'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put(COLUMN_ID, cursor.getString(1));
                map.put(COLUMN_CST_ID, cursor.getString(2));
                map.put(COLUMN_CST_NAME, cursor.getString(3));
                map.put(COLUMN_REFERENCE, cursor.getString(4));
                map.put(COLUMN_AREA, cursor.getString(5));
                map.put(COLUMN_ADDRESS, cursor.getString(6));
                map.put(COLUMN_COUNT_METER, cursor.getString(7));
                map.put(COLUMN_IMAGE, cursor.getString(8));
                map.put(COLUMN_IMAGE_2, cursor.getString(9));
                map.put(COLUMN_DATE, cursor.getString(10));
                map.put(COLUMN_FINAL_METER, cursor.getString(11));
                map.put(COLUMN_USER_ID, cursor.getString(12));
                map.put(COLUMN_STATUS_SYNC, cursor.getString(13));
                map.put(COLUMN_DESCRIPTION, cursor.getString(14));
                map.put(COLUMN_STATUS, cursor.getString(15));
                map.put(COLUMN_PERIOD_ID, cursor.getString(16));
                map.put(COLUMN_STATUS_SERVER, cursor.getString(17));
                map.put(COLUMN_PREV_INITIAL_METER, cursor.getString(18));
                map.put(COLUMN_INITIAL_METER, cursor.getString(19));
                map.put(COLUMN_PREV_DATE_SCAN, cursor.getString(20));

                wordList.add(map);
            } while (cursor.moveToNext());
        }

        Log.e("select sqlite ", "" + wordList);

        cursor.close();
        database.close();
        return wordList;
    }


    public ArrayList<HashMap<String, String>> getAllCustomerByRefAddressName(String ref , String address , String name , String status , String cluster) {
        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<HashMap<String, String>>();

        String selectQuery = "select * from customer where reference like  '%"
                + ref
                + "%' or address like '%"
                + address
                + "%' or status like '%"
                + status
                + "%' or area like '%"
                + cluster
                + "%' or customer_name like '%"
                + name + "%'" ;

        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put(COLUMN_ID, cursor.getString(1));
                map.put(COLUMN_CST_ID, cursor.getString(2));
                map.put(COLUMN_CST_NAME, cursor.getString(3));
                map.put(COLUMN_REFERENCE, cursor.getString(4));
                map.put(COLUMN_AREA, cursor.getString(5));
                map.put(COLUMN_ADDRESS, cursor.getString(6));
                map.put(COLUMN_COUNT_METER, cursor.getString(7));
                map.put(COLUMN_IMAGE, cursor.getString(8));
                map.put(COLUMN_IMAGE_2, cursor.getString(9));
                map.put(COLUMN_DATE, cursor.getString(10));
                map.put(COLUMN_FINAL_METER, cursor.getString(11));
                map.put(COLUMN_USER_ID, cursor.getString(12));
                map.put(COLUMN_STATUS_SYNC, cursor.getString(13));
                map.put(COLUMN_DESCRIPTION, cursor.getString(14));
                map.put(COLUMN_STATUS, cursor.getString(15));
                map.put(COLUMN_PERIOD_ID, cursor.getString(16));
                map.put(COLUMN_STATUS_SERVER, cursor.getString(17));
                map.put(COLUMN_PREV_INITIAL_METER, cursor.getString(18));
                map.put(COLUMN_INITIAL_METER, cursor.getString(19));
                map.put(COLUMN_PREV_DATE_SCAN, cursor.getString(20));

                wordList.add(map);
            } while (cursor.moveToNext());
        }

        Log.e("select sqlite query ", "" + selectQuery);

        Log.e("select sqlite ", "" + wordList);
        cursor.close();
        database.close();
        return wordList;
    }

    public ArrayList<HashMap<String, String>> getAllDescription() {
        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<HashMap<String, String>>();

        String selectQuery = "SELECT * FROM " + TABLE_DESCRIPTION
                + " ORDER BY idLocal DESC" ;
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put(COLUMN_ID, cursor.getString(0));
                map.put(COLUMN_ID_DESCRIPTION, cursor.getString(1));
                map.put(COLUMN_DESCRIPTION, cursor.getString(2));

                wordList.add(map);
            } while (cursor.moveToNext());
        }

        Log.e("select sqlite query ", "" + selectQuery);

        Log.e("select sqlite ", "" + wordList);
        cursor.close();
        database.close();
        return wordList;
    }

    public ArrayList<HashMap<String, String>> getAllSync(String app) {
        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<HashMap<String, String>>();

        String selectQuery = "SELECT * FROM " + TABLE_SYNC
                + " WHERE " +
                COLUMN_APP+ "==" + "'" + app + "'"
                ;

        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put(COLUMN_ID_LOCAL, cursor.getString(0));
                map.put(COLUMN_APP, cursor.getString(1));
                map.put(COLUMN_PERIOD_ID, cursor.getString(2));
                map.put(COLUMN_PERIOD_DESC, cursor.getString(3));
                map.put(COLUMN_SYNC, cursor.getString(4));

                wordList.add(map);
            } while (cursor.moveToNext());
        }

        Log.e("select sqlite query ", "" + selectQuery);

        Log.e("select sqlite ", "" + wordList);
        cursor.close();
        database.close();
        return wordList;
    }

    public ArrayList<HashMap<String, String>> getAllTaskListCustomer() {
        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<HashMap<String, String>>();

        String selectQuery = "SELECT * FROM " + TABLE_CUSTOMER ;
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put(COLUMN_ID, cursor.getString(1));
                map.put(COLUMN_CST_ID, cursor.getString(2));
                map.put(COLUMN_CST_NAME, cursor.getString(3));
                map.put(COLUMN_REFERENCE, cursor.getString(4));
                map.put(COLUMN_AREA, cursor.getString(5));
                map.put(COLUMN_ADDRESS, cursor.getString(6));
                map.put(COLUMN_COUNT_METER, cursor.getString(7));
                map.put(COLUMN_IMAGE, cursor.getString(8));
                map.put(COLUMN_IMAGE_2, cursor.getString(9));
                map.put(COLUMN_DATE, cursor.getString(10));
                map.put(COLUMN_FINAL_METER, cursor.getString(11));
                map.put(COLUMN_USER_ID, cursor.getString(12));
                map.put(COLUMN_STATUS_SYNC, cursor.getString(13));
                map.put(COLUMN_DESCRIPTION, cursor.getString(14));
                map.put(COLUMN_STATUS, cursor.getString(15));
                map.put(COLUMN_PERIOD_ID, cursor.getString(16));
                map.put(COLUMN_STATUS_SERVER, cursor.getString(17));
                map.put(COLUMN_PREV_INITIAL_METER, cursor.getString(18));
                map.put(COLUMN_INITIAL_METER, cursor.getString(19));
                map.put(COLUMN_PREV_DATE_SCAN, cursor.getString(20));

                wordList.add(map);
            } while (cursor.moveToNext());
        }

        Log.e("select sqlite query ", "" + selectQuery);

        Log.e("select sqlite ", "" + wordList);
        cursor.close();
        database.close();
        return wordList;
    }


    public ArrayList<HashMap<String, String>> getAllCustomer() {
        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<HashMap<String, String>>();
        try {

            String selectQuery = "SELECT * FROM " + TABLE_CUSTOMER
                    + " WHERE " +
                    COLUMN_STATUS_SYNC+ "==" + "'" + Constant.STATUS_OPEN + "'"
                    + " or " +
                    COLUMN_STATUS_SYNC+ "==" + "'" + Constant.STATUS_UPDATED + "'"
                    + " or " +
                    COLUMN_STATUS_SYNC+ "==" + "'" + Constant.STATUS_WAITING + "'"
                    + " or " +
                    COLUMN_STATUS_SYNC+ "==" + "'" + Constant.STATUS_PENDING+ "'"
                    + " or " +
                    COLUMN_STATUS_SYNC+ "==" + "'" + Constant.STATUS_FAILED + "'"
                    ;
            SQLiteDatabase database = this.getWritableDatabase();
            Cursor cursor = database.rawQuery(selectQuery, null);

            if(cursor.getCount() != 0) {

                if (cursor.moveToFirst()) {
                    do {
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put(COLUMN_ID, cursor.getString(1));
                        map.put(COLUMN_CST_ID, cursor.getString(2));
                        map.put(COLUMN_CST_NAME, cursor.getString(3));
                        map.put(COLUMN_REFERENCE, cursor.getString(4));
                        map.put(COLUMN_AREA, cursor.getString(5));
                        map.put(COLUMN_ADDRESS, cursor.getString(6));
                        map.put(COLUMN_COUNT_METER, cursor.getString(7));
                        map.put(COLUMN_IMAGE, cursor.getString(8));
                        map.put(COLUMN_IMAGE_2, cursor.getString(9));
                        map.put(COLUMN_DATE, cursor.getString(10));
                        map.put(COLUMN_FINAL_METER, cursor.getString(11));
                        map.put(COLUMN_USER_ID, cursor.getString(12));
                        map.put(COLUMN_STATUS_SYNC, cursor.getString(13));
                        map.put(COLUMN_DESCRIPTION, cursor.getString(14));
                        map.put(COLUMN_STATUS, cursor.getString(15));
                        map.put(COLUMN_PERIOD_ID, cursor.getString(16));
                        map.put(COLUMN_STATUS_SERVER, cursor.getString(17));
                        map.put(COLUMN_PREV_INITIAL_METER, cursor.getString(18));
                        map.put(COLUMN_INITIAL_METER, cursor.getString(19));
                        map.put(COLUMN_PREV_DATE_SCAN, cursor.getString(20));

                        wordList.add(map);
                    } while (cursor.moveToNext());
                }

            }

            Log.e("select sqlite ", "" + wordList);
            cursor.close();
            database.close();


        }catch (Exception e){
            Log.e(TAG, "getAllCustomer: "+e.getMessage() );
        }

        return wordList;

    }

    public void insertCustomerToDb(ResponseCustomer rsp) {
        SQLiteDatabase database = this.getWritableDatabase();
        String queryValues = "INSERT INTO " + TABLE_CUSTOMER + " (id, customer_id , customer_name , reference , area , address , count_meter , image , image_2 , date ,final_meter, user_id , status_sync , description,status,period_id,status_server,prev_initial_meter,initial_meter,prev_date_scan) " +
                "VALUES (" +
                "'" + rsp.getId() + "', " +
                "'" + rsp.getCustomer_id() + "', " +
                "'" + rsp.getCustomer_name() + "', " +
                "'" + rsp.getReference() + "', " +
                "'" + rsp.getArea() + "', " +
                "'" + rsp.getAddress() + "', " +
                "'" + rsp.getPrev_final_meter() + "', " +
                "'" + rsp.getImage() + "', " +
                "'" + rsp.getImage_2() + "', " +
                "'" + rsp.getDate() + "', " +
                "'" + rsp.getFinal_meter()+ "', " +
                "'" + rsp.getUser_id()+ "', " +
                "'" + rsp.getStatus_sync()+ "', " +
                "'" + rsp.getDescription()+ "', " +
                "'" + rsp.getStatus()+ "', " +
                "'" + rsp.getPeriod_id()+ "', " +
                "'" + rsp.getStatus_server()+ "', " +
                "'" + rsp.getPrev_initial_meter()+ "', " +
                "'" + rsp.getInitial_meter()+ "', " +
                "'" + rsp.getPrev_date_scan()

                + "')";

        Log.e("select sqlite ", "" + queryValues);
        database.execSQL(queryValues);

        database.close();
    }

    public void insertDescriptionToDb(ResponseDescription rsp) {
        SQLiteDatabase database = this.getWritableDatabase();
        String queryValues = "INSERT INTO " + TABLE_DESCRIPTION + " (id_description, description ) " +
                "VALUES (" +
                "'" + rsp.getId_description() + "', " +
                "'" + rsp.getDescription()

                + "')";

        Log.e("select sqlite ", "" + queryValues);
        database.execSQL(queryValues);
        database.close();
    }



    public void insertLastSyncDataToDb(String app , String period_id , String period_desc , String sync) {
        SQLiteDatabase database = this.getWritableDatabase();
        String queryValues = "INSERT INTO " + TABLE_SYNC+ " (app,period_id,period_desc ,sync ) " +
                "VALUES (" +
                "'" + app + "', " +
                "'" + period_id + "', " +
                "'" + period_desc + "', " +
                "'" + sync

                + "')";

        Log.e("select sqlite ", "" + queryValues);
        database.execSQL(queryValues);
        database.close();
    }

    public void updateStatusLastSyncDataDB(String perioddessc , String date , String app) {

        SQLiteDatabase database = this.getWritableDatabase();
        String updateQuery = "UPDATE " + TABLE_SYNC + " SET "
                +COLUMN_PERIOD_DESC + "='" + perioddessc + "', "
                + COLUMN_SYNC + "='" + date + "'"
                + " WHERE " + COLUMN_APP+ "=" + "'" + app + "'";
        Log.e("update sqlite ", updateQuery);
        database.execSQL(updateQuery);
        database.close();
        Log.e("select sqlite ", "" + updateQuery);

    }

    public void updateStatusLastSyncDataDB(String idperiod,String perioddessc,String date , String app) {

        SQLiteDatabase database = this.getWritableDatabase();
        String updateQuery = "UPDATE " + TABLE_SYNC + " SET "
                + COLUMN_PERIOD_ID + "='" + idperiod + "', "
                +COLUMN_PERIOD_DESC + "='" + perioddessc + "', "
                + COLUMN_SYNC + "='" + date + "'"
                + " WHERE " + COLUMN_APP+ "=" + "'" + app + "'";
        Log.e("update sqlite ", updateQuery);
        database.execSQL(updateQuery);
        database.close();
        Log.e("select sqlite ", "" + updateQuery);

    }

    public void updateAllCustomerByref(ResponseCustomer rsp) {
        SQLiteDatabase database = this.getWritableDatabase();
         String updateQuery = "UPDATE " + TABLE_CUSTOMER + " SET "
                 + COLUMN_ID + "='" + rsp.getId() + "', "
                 + COLUMN_CST_ID + "='" + rsp.getCustomer_id() + "', "
                 + COLUMN_CST_NAME + "='" + rsp.getCustomer_name() + "', "
                 + COLUMN_REFERENCE + "='" + rsp.getReference() + "',"
                 + COLUMN_AREA + "='" + rsp.getArea() + "',"
                 + COLUMN_ADDRESS + "='" + rsp.getAddress() + "', "
                 + COLUMN_IMAGE + "='" + rsp.getImage() + "', "
                 + COLUMN_IMAGE_2 + "='" + rsp.getImage_2() + "', "
                 + COLUMN_DATE + "='" + rsp.getDate() + "',"
                 + COLUMN_COUNT_METER + "='" + rsp.getPrev_final_meter()  + "',"
                 + COLUMN_FINAL_METER + "='" + rsp.getFinal_meter() + "',"
                 + COLUMN_PREV_INITIAL_METER + "='" + rsp.getPrev_initial_meter()  + "',"
                 + COLUMN_INITIAL_METER + "='" + rsp.getInitial_meter()+ "',"
                 + COLUMN_USER_ID + "='" + rsp.getUser_id() + "', "
                 + COLUMN_STATUS_SYNC + "='" + rsp.getStatus_sync() + "', "
                 + COLUMN_DESCRIPTION + "='" + rsp.getStatus() + "', "
                 + COLUMN_PERIOD_ID + "='" + rsp.getPeriod_id() + "', "
                 + COLUMN_STATUS_SERVER + "='" + rsp.getStatus_server() + "', "
                 + COLUMN_PREV_DATE_SCAN + "='" + rsp.getPrev_date_scan() + "'"
                 + " WHERE " + COLUMN_REFERENCE+ "=" + "'" + rsp.getReference() + "'"
                 + " AND " + COLUMN_STATUS_SYNC+ "!=" + "'" + Constant.STATUS_FAILED + "'" ;

        database.execSQL(updateQuery);
        Log.e("select sqlite ", "" + updateQuery);
        database.close();
    }


    public void deleteAllDescription() {
        SQLiteDatabase database = this.getWritableDatabase();
        String updateQuery = "DELETE FROM " + TABLE_DESCRIPTION ;
        Log.e("update sqlite ", updateQuery);
        database.execSQL(updateQuery);
        database.close();
    }

    public void deleteAllCustomer() {
        SQLiteDatabase database = this.getWritableDatabase();
        String updateQuery = "DELETE FROM " + TABLE_CUSTOMER ;
        Log.e("update sqlite ", updateQuery);
        database.execSQL(updateQuery);
        database.close();
    }

    public void deleteAllSyncData() {
        SQLiteDatabase database = this.getWritableDatabase();
        String updateQuery = "DELETE FROM " + TABLE_SYNC ;
        Log.e("update sqlite ", updateQuery);
        database.execSQL(updateQuery);
        database.close();
    }


    public void updateCustomerByReference(String image ,String image_2, String startmeter , String finalmeter, String date, String userid , String reference , String description , String status , String status_sync , String status_server) {

        SQLiteDatabase database = this.getWritableDatabase();
        String updateQuery = "UPDATE " + TABLE_CUSTOMER + " SET "
                + COLUMN_IMAGE + "='" + image + "', "
                + COLUMN_IMAGE_2 + "='" + image_2 + "', "
                + COLUMN_COUNT_METER + "='" + startmeter + "', "
                + COLUMN_FINAL_METER + "='" + finalmeter + "', "
                + COLUMN_DATE + "='" + date + "', "
                + COLUMN_USER_ID + "='" + userid + "', "
                + COLUMN_STATUS + "='" + status + "', "
                + COLUMN_STATUS_SYNC + "='" + status_sync + "', "
                + COLUMN_STATUS_SERVER + "='" + status_server + "', "
                + COLUMN_DESCRIPTION + "='" + description + "'"
                + " WHERE " + COLUMN_REFERENCE+ "=" + "'" + reference + "'";
        Log.e("update sqlite ", updateQuery);
        database.execSQL(updateQuery);
        database.close();

    }

    public void updateStatusCustomerByReference(String status , String reference) {

        SQLiteDatabase database = this.getWritableDatabase();
        String updateQuery = "UPDATE " + TABLE_CUSTOMER + " SET "
                + COLUMN_STATUS_SYNC + "='" + status + "'"
                + " WHERE " + COLUMN_REFERENCE+ "=" + "'" + reference + "'";
        Log.e("update sqlite ", updateQuery);
        database.execSQL(updateQuery);
        database.close();
        Log.e("select sqlite ", "" + updateQuery);

    }

    public void updateStatusLokalCustomerByReference(String status , String reference) {

        SQLiteDatabase database = this.getWritableDatabase();
        String updateQuery = "UPDATE " + TABLE_CUSTOMER + " SET "
                + COLUMN_STATUS + "='" + status + "'"
                + " WHERE " + COLUMN_REFERENCE+ "=" + "'" + reference + "'";
        Log.e("update sqlite ", updateQuery);
        database.execSQL(updateQuery);
        database.close();
        Log.e("select sqlite ", "" + updateQuery);

    }

    public void updateStatusSyncAllCustomer(String status) {

        SQLiteDatabase database = this.getWritableDatabase();
        String updateQuery = "UPDATE " + TABLE_CUSTOMER + " SET "
                + COLUMN_STATUS + "='" + status + "'"
                + " WHERE " + COLUMN_STATUS+ "!=" + "'" + Constant.STATUS_COMPLETED + "'";
        Log.e("update sqlite ", updateQuery);
        database.execSQL(updateQuery);
        database.close();
    }



    public Boolean exportDB(Context context){
        File sd = Environment.getExternalStorageDirectory();
        File data = Environment.getDataDirectory();
        FileChannel source=null;
        FileChannel destination=null;
        String currentDBPath = "/data/"+ "com.rsah.watermeterreader" +"/databases/"+DATABASE_NAME;

        String backupDBPath = DATABASE_NAME;
        File currentDB = new File(data, currentDBPath);
        File backupDB = new File(sd, backupDBPath);
        try {
            source = new FileInputStream(currentDB).getChannel();
            destination = new FileOutputStream(backupDB).getChannel();
            destination.transferFrom(source, 0, source.size());
            source.close();
            destination.close();
            return true ;
        } catch(IOException e) {
            Log.e("", "exportDB: "+e.getMessage() );
            return false ;
        }
    }



}

