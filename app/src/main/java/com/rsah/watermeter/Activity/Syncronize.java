package com.rsah.watermeter.Activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;



import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.developer.kalert.KAlertDialog;
import com.rsah.watermeter.Api.ApiService;
import com.rsah.watermeter.Api.Server;
import com.rsah.watermeter.Constant.Constant;
import com.rsah.watermeter.Model.json.Json;
import com.rsah.watermeter.Model.json.JsonCustomer;
import com.rsah.watermeter.Model.json.JsonPeriod;
import com.rsah.watermeter.Model.json.JsonSaveMeter;
import com.rsah.watermeter.Model.response.ResponseCustomer;
import com.rsah.watermeter.Model.response.ResponseData;
import com.rsah.watermeter.Model.response.ResponseDataDescription;
import com.rsah.watermeter.Model.response.ResponseDescription;
import com.rsah.watermeter.Model.response.ResponseRequest;
import com.rsah.watermeter.R;
import com.rsah.watermeter.Session.SessionManager;
import com.rsah.watermeter.Util.DbCustomerHelper;
import com.rsah.watermeter.Util.Helper;
import com.rsah.watermeter.Util.Utility;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.rsah.watermeter.Util.DbCustomerHelper.COLUMN_ADDRESS;
import static com.rsah.watermeter.Util.DbCustomerHelper.COLUMN_AREA;
import static com.rsah.watermeter.Util.DbCustomerHelper.COLUMN_COUNT_METER;
import static com.rsah.watermeter.Util.DbCustomerHelper.COLUMN_CST_ID;
import static com.rsah.watermeter.Util.DbCustomerHelper.COLUMN_CST_NAME;
import static com.rsah.watermeter.Util.DbCustomerHelper.COLUMN_DATE;
import static com.rsah.watermeter.Util.DbCustomerHelper.COLUMN_DESCRIPTION;
import static com.rsah.watermeter.Util.DbCustomerHelper.COLUMN_FINAL_METER;
import static com.rsah.watermeter.Util.DbCustomerHelper.COLUMN_ID;
import static com.rsah.watermeter.Util.DbCustomerHelper.COLUMN_IMAGE;
import static com.rsah.watermeter.Util.DbCustomerHelper.COLUMN_IMAGE_2;
import static com.rsah.watermeter.Util.DbCustomerHelper.COLUMN_PERIOD_DESC;
import static com.rsah.watermeter.Util.DbCustomerHelper.COLUMN_PERIOD_ID;
import static com.rsah.watermeter.Util.DbCustomerHelper.COLUMN_REFERENCE;
import static com.rsah.watermeter.Util.DbCustomerHelper.COLUMN_STATUS;
import static com.rsah.watermeter.Util.DbCustomerHelper.COLUMN_STATUS_SERVER;
import static com.rsah.watermeter.Util.DbCustomerHelper.COLUMN_SYNC;
import static com.rsah.watermeter.Util.DbCustomerHelper.COLUMN_USER_ID;

public class Syncronize extends AppCompatActivity {


    private ImageView ivsynch ;
    private TextView tvopen , tvwaiting , tvcomplete , tvpending , tvtotal , tvlastsync;

    private ApiService API;
    private SessionManager sesion;
    private DbCustomerHelper SQLite ;
    private RelativeLayout rlprogress , rlprogressLoading;

    private TextView tvprogresscount , tvprogresssize , tvperiod , tvnewperiod;
    private static String TAG_SYNC = "stop";
    private static int TAG_ITERATION_PROGRESS = 0;
    private static String TAG__STAUS_SYNC = "Uploading";
    private static int TAG_SYNC_SIZE = 100 ;
    private static String TAG_SYNC_FINISH = "" ;
    private ProgressDialog mProgressDialog;
    private List<ResponseData> CustomerList = new ArrayList<>();
    private List<ResponseDataDescription> DescriptionList = new ArrayList<>();
    private AsyncTaskProgress asyncprogress;
    private AsyncTaskProgressDownloading asyncdownloading;
    private AsyncTaskProgressDescDownloading asyncdescdownloading;
    private Switch swnewperiod , swwithdownload;
    private static Boolean newperiod = false;
    private Boolean withdownload = false;
    private static String TAG_PERIOD_ID = "";
    private static String TAG_PERIOD_ID_LOCAL = "";
    private static String TAG_PERIOD_ID_REQUEST = "";
    private static String period_desc_dump = "" ;
    private static String period_desc_dump_new = "" ;
    private LinearLayout cardnewperiod ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_syncronize);

        ivsynch = findViewById(R.id.ivsynch);
        API = Server.getAPIService();
        rlprogress = findViewById(R.id.rlprogress);
        tvprogresscount = findViewById(R.id.textprogress);
        sesion = new SessionManager(this);
        tvopen = findViewById(R.id.tvcopen);
        tvwaiting = findViewById(R.id.tvwaiting);
        tvcomplete = findViewById(R.id.tvcomplete);
        tvpending = findViewById(R.id.tvpending);
        tvtotal = findViewById(R.id.tvtotal);
        tvlastsync = findViewById(R.id.tvlastsync);
        swnewperiod = findViewById(R.id.swnewperiod);
        swwithdownload = findViewById(R.id.withdownload);
        tvperiod = findViewById(R.id.tvperiod);
        tvnewperiod = findViewById(R.id.tvnewperiod);
        cardnewperiod = findViewById(R.id.cardnewperiod);

        asyncprogress = new AsyncTaskProgress();
        asyncdownloading = new AsyncTaskProgressDownloading();
        asyncdescdownloading = new AsyncTaskProgressDescDownloading();
        newperiod = false;
        withdownload = false;

        Toolbar toolbar = findViewById(R.id.toolbarSync);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        cardnewperiod.setVisibility(View.GONE);

        SQLite = new DbCustomerHelper(this);

        ivsynch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirm_synch();
            }
        });

        getDataCustomerFromDBSetToTextview();

        String uid_ = Utility.UID;
        String secretkey = Utility.SECRETKEY;
        String method = Constant.TAG_METHOD;
        String time = ""+System.currentTimeMillis();
        String signature_ = Helper.Hash_SHA256(uid_+secretkey+time);
        requestPeriod(uid_,signature_,method,time);


        swnewperiod.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                newperiod = isChecked ;
                if (isChecked) {
                    swwithdownload.setVisibility(View.GONE);
                }else{
                    swwithdownload.setVisibility(View.VISIBLE);
                }

            }
        });

        swwithdownload.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                withdownload = isChecked ;

            }
        });




    }


    //homeback
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                //Write your logic here

                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }



    private void showProgress(Boolean bool){

        if (bool){
            rlprogress.setVisibility(View.VISIBLE);
        }else {
            rlprogress.setVisibility(View.GONE);
        }
    }




    private void confirm_synch(){
        AlertDialog.Builder builder = new AlertDialog.Builder(Syncronize.this);
        builder.setCancelable(false);
        builder.setMessage("synchronization data ?");
        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                if (Helper.isDeviceOnline(Syncronize.this)){

                    SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy HH:mm:ss", Locale.getDefault());
                    String currentDateandTime = sdf.format(new Date());

                    if (newperiod ){

                            Log.e("TAG", "confirm: "+"new periode" );
                            TAG_PERIOD_ID_REQUEST = TAG_PERIOD_ID_LOCAL ;
                            SQLite.updateStatusLastSyncDataDB(TAG_PERIOD_ID_LOCAL,period_desc_dump,currentDateandTime,Constant.TAG_NAME_APP);
                            SQLite.updateStatusLastSyncDataDB(TAG_PERIOD_ID,period_desc_dump_new,currentDateandTime,Constant.TAG_NAME_APP);
                            TAG_PERIOD_ID_REQUEST = TAG_PERIOD_ID ;

                    }else if(!withdownload){

                        Log.e("TAG", "confirm: "+"tidak withdownload" );
                        newperiod = false ;
                        TAG_SYNC = "start";
                        TAG_PERIOD_ID_REQUEST = TAG_PERIOD_ID_LOCAL ;
                        SQLite.updateStatusLastSyncDataDB(TAG_PERIOD_ID_LOCAL,period_desc_dump,currentDateandTime,Constant.TAG_NAME_APP);

                    }else{
                        Log.e("TAG", "confirm: "+"withdownload" );
                        TAG_PERIOD_ID_REQUEST = TAG_PERIOD_ID_LOCAL ;
                        SQLite.updateStatusLastSyncDataDB(TAG_PERIOD_ID_LOCAL,period_desc_dump,currentDateandTime,Constant.TAG_NAME_APP);
                    }
                    newperiod = false ;
                    TAG_SYNC = "start";
                    asyncprogress.execute("");

                }else{

                    new KAlertDialog(Syncronize.this, KAlertDialog.WARNING_TYPE)
                            .setTitleText("Notification")
                            .setContentText("Device Tidak Terhubung Internet")
                            .setConfirmText("OK")
                            .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                @Override
                                public void onClick(KAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                    getDataCustomerFromDBSetToTextview();
                                }
                            })
                            .show();



                }


            }
        });
        builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user select "No", just cancel this dialog and continue with app
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }


    private void requestPeriod(String uid , String signature ,String method ,  String time ) {

        checkInternet();
        showProgress(true);

        API.requestGetPeriod(new JsonPeriod(uid , signature , method , time)).enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {


                if (response.isSuccessful()){
                    showProgress(false);

                    if (response.body() != null){
                        showProgress(false);

                        ResponseData responseData = response.body();
                        if(responseData.getStatus().equals(Constant.ERR_0)){


                            String idPeriod = responseData.getData().get(0).getId();
                            String periodDesc = responseData.getData().get(0).getDescription() ;
                            String status = responseData.getData().get(0).getStatus() ;

                            String fontsize = responseData.getData().get(0).getFont_size() ;
                            String fontstyle = responseData.getData().get(0).getFont_style() ;
                            sesion.saveSetting(fontsize,fontstyle);

                            TAG_PERIOD_ID = idPeriod ;
                            period_desc_dump_new = periodDesc ;

                            //insert period dan last sync
                            ArrayList<HashMap<String, String>> datalastSync = SQLite.getAllSync(Constant.TAG_NAME_APP);
                            String lastsynctime = "" ; //if db is empty
                            String period_id_local = "" ;
                            String period_desc_local = "" ;


                            //if db kosong
                            if (datalastSync.size() == 0){

                                SQLite.deleteAllSyncData();
                                SQLite.insertLastSyncDataToDb(Constant.TAG_NAME_APP,idPeriod,periodDesc,lastsynctime );
                                TAG_PERIOD_ID_LOCAL = idPeriod ;
                                period_desc_dump = periodDesc ;

                            }else{


                                lastsynctime = datalastSync.get(0).get(COLUMN_SYNC) ;
                                period_id_local  = datalastSync.get(0).get(COLUMN_PERIOD_ID) ;
                                period_desc_local  = datalastSync.get(0).get(COLUMN_PERIOD_DESC) ;
                                TAG_PERIOD_ID_LOCAL = period_id_local ;
                                period_desc_dump = period_desc_local;


                                if (period_id_local.equals("")){

                                    SQLite.updateStatusLastSyncDataDB(period_desc_local,lastsynctime,Constant.TAG_NAME_APP);

                                }


                                //deteksi periode baru
                                if (!period_id_local.equals(idPeriod)){
                                    if (!period_id_local.equals("")) {
                                        swnewperiod.setVisibility(View.VISIBLE); //swithc active
                                        cardnewperiod.setVisibility(View.VISIBLE);
                                        tvnewperiod.setText(periodDesc);
                                        //period_desc_dump = periodDesc ;
                                    }
                                }

                            }

                            tvperiod.setText(period_desc_dump);

                        }else {
                            showProgress(false);
                            finish();
                            Toast.makeText(Syncronize.this, responseData.getMessage(), Toast.LENGTH_LONG).show();
                        }

                    }else{
                        showProgress(false);
                        finish();
                        Toast.makeText(Syncronize.this, Constant.TAG_SERVER_ERROR, Toast.LENGTH_LONG).show();
                    }

                }else {
                    showProgress(false);
                    finish();
                    Toast.makeText(Syncronize.this, Constant.TAG_SERVER_ERROR, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                showProgress(false);
                finish();
                Toast.makeText(Syncronize.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void requestSaveMeter(String uid , String signature , String customer_id , String reference , String customer_name , String area , String address , String initial_meter , String final_meter, String tglcreate , String userid , String imgfile,  String imgfile_2  , String desc ,String period_id,String id, String status) {


        API.requestSaveMeter(new JsonSaveMeter(uid , signature , customer_id , reference , customer_name , area , address , initial_meter , final_meter, tglcreate , userid , imgfile , imgfile_2 , desc , period_id,id,status)).enqueue(new Callback<ResponseRequest>() {
            @Override
            public void onResponse(Call<ResponseRequest> call, Response<ResponseRequest> response) {


                if (response.isSuccessful()){


                    if (response.body() != null){

                        ResponseRequest responseData = response.body();
                        if(responseData.getStatus().equals(Constant.ERR_0)){


                            SQLite.updateStatusCustomerByReference(Constant.STATUS_PENDING,reference); //change status failed /= pending

                            //Toast.makeText(Syncronize.this, responseData.getMessage(), Toast.LENGTH_LONG).show();

                        }else {

                            SQLite.updateStatusCustomerByReference(Constant.STATUS_FAILED,reference); //change status failed /= pending
                            //Toast.makeText(Syncronize.this, responseData.getMessage(), Toast.LENGTH_LONG).show();

                            if (responseData.getMessage().equals(Constant.STATUS_APPROVED)){
                                SQLite.updateStatusCustomerByReference(Constant.STATUS_COMPLETED,reference); //change status failed /= pending
                            }


                        }

                    }else{

                        SQLite.updateStatusCustomerByReference(Constant.STATUS_FAILED,reference); //change status failed /= pending
                        //Toast.makeText(Syncronize.this, Constant.TAG_SERVER_ERROR, Toast.LENGTH_LONG).show();
                    }

                }else {

                    SQLite.updateStatusCustomerByReference(Constant.STATUS_FAILED,reference); //change status failed /= pending
                    //Toast.makeText(Syncronize.this, Constant.TAG_SERVER_ERROR, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseRequest> call, Throwable t) {

                SQLite.updateStatusCustomerByReference(Constant.STATUS_FAILED,reference); //change status failed /= pending
                Toast.makeText(Syncronize.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }



    // Save data to SQLite database
    private void saveToLocalDB(ResponseCustomer rsp) {

            SQLite.insertCustomerToDb(rsp);

    }

    // Save data to SQLite database
    private void updateCustomerLocalDB(ResponseCustomer rsp) {

        SQLite.updateAllCustomerByref(rsp);

    }

    private void getDataCustomerFromDBSetToTextview(){

        ArrayList<HashMap<String, String>> dataopen = SQLite.getAllCustomerByStatus(Constant.STATUS_UPDATED);
        tvopen.setText(""+dataopen.size());

        ArrayList<HashMap<String, String>> datawaiting = SQLite.getAllCustomerByStatus(Constant.STATUS_WAITING);
        tvwaiting.setText(""+datawaiting.size());

        ArrayList<HashMap<String, String>> datacompleted = SQLite.getAllCustomerByStatus(Constant.STATUS_COMPLETED);
        tvcomplete.setText(""+datacompleted.size());

        ArrayList<HashMap<String, String>> datafailed = SQLite.getAllCustomerByStatus(Constant.STATUS_FAILED);
        ArrayList<HashMap<String, String>> datapending = SQLite.getAllCustomerByStatus(Constant.STATUS_PENDING);
        int Pending = datapending.size() + datafailed.size() ;
        tvpending.setText(""+Pending);

        ArrayList<HashMap<String, String>> datanew = SQLite.getAllCustomerByStatus("NEW");


        ArrayList<HashMap<String, String>> datalastSync = SQLite.getAllSync(Constant.TAG_NAME_APP);

        if (datalastSync.size() != 0){
            tvlastsync.setText(datalastSync.get(0).get(COLUMN_SYNC));
        }

        //set total
        String open = tvopen.getText().toString();
        String waiting = tvwaiting.getText().toString();
        String complete = tvcomplete.getText().toString();
        String pending = tvpending.getText().toString();
        int total = Integer.parseInt(open) + Integer.parseInt(waiting) + Integer.parseInt(complete) + Integer.parseInt(pending)  + datanew.size();
        tvtotal.setText(""+total);

    }



    private class AsyncTaskProgress extends AsyncTask<String,Integer,String>{


        @Override
        protected void onPreExecute() {


            showProgress(true);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            //start_synch();

            SQLite.exportDB(Syncronize.this);

            TAG_SYNC = "start";
            ArrayList<HashMap<String, String>> data = SQLite.getAllCustomer();
            String uid_ = Utility.UID;
            String secretkey = Utility.SECRETKEY;
            String reference = ""; //optional
            String time = ""+System.currentTimeMillis();
            String signature_ = Helper.Hash_SHA256(uid_+secretkey+time);

            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (data.size() == 0) {

                if (!withdownload) {

                    publishProgress(Constant.STATUS_FINISH_CODE_SYNC); //error code
                    //showProgress(false);
                    Log.e("TAG", "doInBackground: "+"kesini yah" );
                    asyncprogress.cancel(true);
                }else {
                    String period_id = TAG_PERIOD_ID_REQUEST;

                    if (newperiod) {
                        SQLite.deleteAllCustomer();
                    }

                    //add delay for wait database is uptodate
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    //insert into db from server if table is empty
                    TAG_SYNC = "start";
                    API.requestGetCustomer(new JsonCustomer(uid_, signature_, reference, time, period_id)).enqueue(new Callback<ResponseData>() {
                        @Override
                        public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {

                            if (response.isSuccessful()) {


                                if (response.body() != null) {

                                    ResponseData responseData = response.body();
                                    if (responseData.getStatus().equals(Constant.ERR_0)) {

                                        CustomerList.add(responseData);
                                        asyncdownloading.execute("");


                                    } else {
                                        publishProgress(Constant.STATUS_ERROR_CODE_SYNC); //error code
                                        asyncprogress.cancel(true);
                                        Toast.makeText(Syncronize.this, responseData.getMessage(), Toast.LENGTH_LONG).show();

                                    }

                                } else {
                                    publishProgress(Constant.STATUS_ERROR_CODE_SYNC); //error code
                                    asyncprogress.cancel(true);
                                    Toast.makeText(Syncronize.this, Constant.TAG_SERVER_ERROR, Toast.LENGTH_LONG).show();
                                }

                            } else {
                                publishProgress(Constant.STATUS_ERROR_CODE_SYNC); //error code
                                asyncprogress.cancel(true);
                                Toast.makeText(Syncronize.this, Constant.TAG_SERVER_ERROR, Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseData> call, Throwable t) {
                            publishProgress(Constant.STATUS_ERROR_CODE_SYNC); //error code
                            asyncprogress.cancel(true);
                            Toast.makeText(Syncronize.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }



            //save from local to db server
            for (int i = 0; i < data.size(); i++) {

                //TAG_ITERATION_PROGRESS = 1 ;
                publishProgress(i);
                TAG__STAUS_SYNC = "Uploading";
                TAG_SYNC_SIZE = data.size();

                try {
                    String uid		    = Utility.UID ;
                    String secret		= Utility.SECRETKEY ;
                    String customer_id = data.get(i).get(COLUMN_CST_ID);
                    String customer_name = data.get(i).get(COLUMN_CST_NAME);
                    String ref = data.get(i).get(COLUMN_REFERENCE);
                    String area = data.get(i).get(COLUMN_AREA);
                    String address = data.get(i).get(COLUMN_ADDRESS);
                    String initial_meter = data.get(i).get(COLUMN_COUNT_METER);
                    String imgfile= data.get(i).get(COLUMN_IMAGE);
                    String imgfile_2= data.get(i).get(COLUMN_IMAGE_2);
                    String tglcreate  	= data.get(i).get(COLUMN_DATE);
                    String final_meter 	= data.get(i).get(COLUMN_FINAL_METER);
                    String userid  		= data.get(i).get(COLUMN_USER_ID);
                    String desc  		= data.get(i).get(COLUMN_DESCRIPTION);
                    String status  		= data.get(i).get(COLUMN_STATUS);
                    String id  		= data.get(i).get(COLUMN_ID);
                    String signature  		= Helper.Hash_SHA256(uid+secret+id);
                    String period_id  		= data.get(i).get(COLUMN_PERIOD_ID);
                    String status_server 		= data.get(i).get(COLUMN_STATUS_SERVER);


                    //if status pending confirm
                    if (status.equals(Constant.STATUS_PENDING_CONFIRM)){
                        final_meter = "0" ;
                        initial_meter = "0" ;
                    }


                    if (tglcreate.equals("")){
                        tglcreate = Helper.getDateTimeNow();
                    }

                    requestSaveMeter(
                            uid,
                            signature,
                            customer_id,
                            ref,
                            customer_name,
                            area,
                            address,
                            initial_meter,
                            final_meter,
                            tglcreate,
                            userid,
                            imgfile,
                            imgfile_2,
                            desc,
                            period_id,
                            id,
                            status_server

                    );

                        if (i == data.size() - 1) {

                            if (!withdownload) {
                                publishProgress(Constant.STATUS_FINISH_CODE_SYNC); //error code
                                showProgress(false);
                                asyncprogress.cancel(true);
                            }else {
                                SQLite.deleteAllCustomer();
                                //add delay for wait database is uptodate
                                try {
                                    Thread.sleep(5000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                                Log.e("TAG", "doInBackground: "+"kesini dong" );

                                CustomerList.clear();
                                //insert into db from server if table is empty
                                API.requestGetCustomer(new JsonCustomer(uid_, signature_, reference, time, period_id)).enqueue(new Callback<ResponseData>() {
                                    @Override
                                    public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {

                                        if (response.isSuccessful()) {


                                            if (response.body() != null) {

                                                ResponseData responseData = response.body();
                                                if (responseData.getStatus().equals(Constant.ERR_0)) {

                                                    CustomerList.add(responseData);
                                                    asyncdownloading.execute("");

                                                } else {
                                                    publishProgress(Constant.STATUS_ERROR_CODE_SYNC); //error code
                                                    asyncprogress.cancel(true);
                                                    Toast.makeText(Syncronize.this, responseData.getMessage(), Toast.LENGTH_LONG).show();
                                                }

                                            } else {
                                                publishProgress(Constant.STATUS_ERROR_CODE_SYNC); //error code
                                                asyncprogress.cancel(true);
                                                Toast.makeText(Syncronize.this, Constant.TAG_SERVER_ERROR, Toast.LENGTH_LONG).show();
                                            }

                                        } else {
                                            publishProgress(Constant.STATUS_ERROR_CODE_SYNC); //error code
                                            asyncprogress.cancel(true);
                                            Toast.makeText(Syncronize.this, Constant.TAG_SERVER_ERROR, Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseData> call, Throwable t) {
                                        publishProgress(Constant.STATUS_ERROR_CODE_SYNC); //error code
                                        asyncprogress.cancel(true);
                                        Toast.makeText(Syncronize.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                        }



                }catch (Exception ex){
                    publishProgress(Constant.STATUS_ERROR_CODE_SYNC); //error code
                    asyncprogress.cancel(true);
                    Toast.makeText(Syncronize.this, "Error "+ex.getMessage(), Toast.LENGTH_SHORT).show();
                }


            }

            return null;
        }


        @Override
        protected void onProgressUpdate(Integer... values) {

            super.onProgressUpdate(values);

            tvprogresscount.setText(TAG__STAUS_SYNC+" "+values[0] + " / " + TAG_SYNC_SIZE);

            if(values[0] == Constant.STATUS_ERROR_CODE_SYNC){

                showProgress(false);
                asyncprogress.cancel(true);

                new KAlertDialog(Syncronize.this, KAlertDialog.WARNING_TYPE)
                        .setTitleText("Notification")
                        .setContentText("Syncronized Up Gagal, Terjadi Kesalahan")
                        .setConfirmText("OK")
                        .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                            @Override
                            public void onClick(KAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                                Intent intent = new Intent(Syncronize.this,Syncronize.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);;
                            }
                        })
                        .show();
            }

            if(values[0] == Constant.STATUS_FINISH_CODE_SYNC){

                showProgress(false);
                asyncprogress.cancel(true);

                new KAlertDialog(Syncronize.this, KAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Notification")
                        .setContentText("Syncronized Berhasil")
                        .setConfirmText("OK")
                        .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                            @Override
                            public void onClick(KAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                                Intent intent = new Intent(Syncronize.this,Syncronize.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);;
                            }
                        })
                        .show();
            }

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }



    private class AsyncTaskProgressDownloading extends AsyncTask<String,Integer,String>{


        @Override
        protected void onPreExecute() {
            showProgress(true);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            //start_synch();

            try {

                if (CustomerList != null){
                    for (int i = 0; i <CustomerList.get(0).getData().size(); i++) {

                        TAG_SYNC = "start";
                        try {
                            TAG__STAUS_SYNC = "Syncronized";
                            publishProgress(i);
                            TAG_SYNC_SIZE = CustomerList.get(0).getData().size();
                            //set entity customer
                            ResponseCustomer rsp = new ResponseCustomer();
                            rsp.setId(CustomerList.get(0).getData().get(i).getId());
                            rsp.setCustomer_id(CustomerList.get(0).getData().get(i).getCustomer_id());
                            rsp.setCustomer_name(CustomerList.get(0).getData().get(i).getCustomer_name());
                            rsp.setReference(CustomerList.get(0).getData().get(i).getReference());
                            rsp.setArea(CustomerList.get(0).getData().get(i).getArea());
                            rsp.setAddress(CustomerList.get(0).getData().get(i).getAddress());

                            rsp.setImage("");
                            rsp.setImage_2("");
                            rsp.setDate("");
                            rsp.setPrev_initial_meter(CustomerList.get(0).getData().get(i).getPrev_initial_meter());
                            rsp.setInitial_meter(CustomerList.get(0).getData().get(i).getInitial_meter());
                            rsp.setUser_id("");
                            rsp.setDescription("");
                            rsp.setFinal_meter(CustomerList.get(0).getData().get(i).getFinal_meter());
                            rsp.setPeriod_id(CustomerList.get(0).getData().get(i).getPeriod_id());
                            rsp.setStatus_server(CustomerList.get(0).getData().get(i).getStatus());
                            rsp.setPrev_final_meter(CustomerList.get(0).getData().get(i).getPrev_final_meter());
                            rsp.setPrev_date_scan(CustomerList.get(0).getData().get(i).getPrev_date_scan());
                            String ref = CustomerList.get(0).getData().get(i).getReference();
                            String status_server = CustomerList.get(0).getData().get(i).getStatus();

                            //cek if data is exist
                            ArrayList<HashMap<String, String>> data = SQLite.getCustomer(ref);
                            if (data.size() == 0) {

                                    rsp.setStatus(Constant.STATUS_OPEN);
                                    rsp.setStatus_sync(Constant.STATUS_COMPLETED);

                                    if (status_server.equals("1")){
                                        rsp.setStatus(Constant.STATUS_UPDATED);
                                    }

                                    if (status_server.equals("4")){
                                        rsp.setStatus(Constant.STATUS_UPDATED);
                                    }

                                    if (status_server.equals("2")){
                                        rsp.setStatus(Constant.STATUS_PENDING_CONFIRM);
                                    }

                                    //completed and new downloading from server
                                    saveToLocalDB(rsp);
                            } else {

                                rsp.setStatus_sync(Constant.STATUS_COMPLETED);


                                    if (status_server.equals("1")){
                                        rsp.setStatus(Constant.STATUS_UPDATED);
                                    }

                                    if (status_server.equals("4")){
                                        rsp.setStatus(Constant.STATUS_UPDATED);
                                    }

                                    if (status_server.equals("2")){
                                        rsp.setStatus(Constant.STATUS_PENDING_CONFIRM);
                                    }

                                updateCustomerLocalDB(rsp);


                            }

                        }catch (Exception ex ){

                            Log.e("TAG", "Terjadi Kesalahan "+ex.getMessage());
                            SQLite.updateStatusCustomerByReference(Constant.STATUS_FAILED,CustomerList.get(0).getData().get(i).getReference());

                        }


                        if (i == CustomerList.get(0).getData().size() - 1 ){


                            TAG_SYNC = "stop" ;
                            return "TRUE";



                        }





                    }
                }else{
                    TAG_SYNC = "stop" ;
                    return "TRUE" ;

                }


            }catch (Exception ex){
                TAG_SYNC = "stop" ;
                Log.e("TAG", "Terjadi Kesalahan "+ex.getMessage());
                Toast.makeText(Syncronize.this, "Terjadi Kesalahan "+ex.getMessage(), Toast.LENGTH_SHORT).show();
                return "FALSE" ;
            }



            return null ;
        }

        @Override
        protected void onPostExecute(String s) {

            if (s.equals("TRUE")){

                String uid		    = Utility.UID ;
                String secret		= Utility.SECRETKEY ;
                String time = ""+System.currentTimeMillis();
                String signature_ = Helper.Hash_SHA256(uid+secret+time);

                //insert into db from server if table is empty
                API.requestGetDescription(new Json(uid,signature_,time)).enqueue(new Callback<ResponseDataDescription>() {
                    @Override
                    public void onResponse(Call<ResponseDataDescription> call, Response<ResponseDataDescription> response) {

                        if (response.isSuccessful()){


                            if (response.body() != null){

                                ResponseDataDescription responseData = response.body();
                                if(responseData.getStatus().equals(Constant.ERR_0)){


                                    DescriptionList.add(responseData);
                                    asyncdescdownloading.execute("");


                                }else {
                                    publishProgress(Constant.STATUS_ERROR_CODE_SYNC); //error code
                                    asyncdownloading.cancel(true);
                                    Toast.makeText(Syncronize.this, responseData.getMessage(), Toast.LENGTH_LONG).show();
                                }

                            }else{
                                publishProgress(Constant.STATUS_ERROR_CODE_SYNC); //error code
                                asyncdownloading.cancel(true);
                                Toast.makeText(Syncronize.this, Constant.TAG_SERVER_ERROR, Toast.LENGTH_LONG).show();
                            }

                        }else {
                            publishProgress(Constant.STATUS_ERROR_CODE_SYNC); //error code
                            asyncdownloading.cancel(true);
                            Toast.makeText(Syncronize.this, Constant.TAG_SERVER_ERROR, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseDataDescription> call, Throwable t) {
                        publishProgress(Constant.STATUS_ERROR_CODE_SYNC); //error code
                        asyncdownloading.cancel(true);
                        Toast.makeText(Syncronize.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });



            }else{

            }



            if (TAG_SYNC.equals("stop")){
                //showProgress(false);
                tvprogresscount.setText(Constant.TAG_PLEASE_WAIT);
            }

            super.onPostExecute(s);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {

            super.onProgressUpdate(values);

            tvprogresscount.setText(TAG__STAUS_SYNC+" "+values[0] + " / " + TAG_SYNC_SIZE);
            Log.e("TAG", "onProgressUpdate: "+values[0] );

            if(values[0] == Constant.STATUS_ERROR_CODE_SYNC){

                showProgress(false);
                asyncdownloading.cancel(true);

                new KAlertDialog(Syncronize.this, KAlertDialog.WARNING_TYPE)
                        .setTitleText("Notification")
                        .setContentText("Syncronized Down Gagal, Terjadi Kesalahan")
                        .setConfirmText("OK")
                        .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                            @Override
                            public void onClick(KAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                                getDataCustomerFromDBSetToTextview();
                            }
                        })
                        .show();
            }

        }



    }



    private class AsyncTaskProgressDescDownloading extends AsyncTask<String,Integer,String>{


        @Override
        protected void onPreExecute() {
            showProgress(true);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            try {
                SQLite.deleteAllDescription();
                if (DescriptionList != null){
                    for (int i = 0; i <DescriptionList.get(0).getData().size(); i++) {

                        TAG_SYNC = "start";
                        try {
                            TAG__STAUS_SYNC = "Syncronized";
                            publishProgress(i);
                            TAG_SYNC_SIZE = DescriptionList.get(0).getData().size();
                            //set entity customer
                            ResponseDescription rsp = new ResponseDescription();
                            rsp.setId_description(DescriptionList.get(0).getData().get(i).getId_description());
                            rsp.setDescription(DescriptionList.get(0).getData().get(i).getDescription());
                            SQLite.insertDescriptionToDb(rsp);

                        }catch (Exception ex ){

                            Log.e("TAG", "Terjadi Kesalahan "+ex.getMessage());
                            //SQLite.updateStatusCustomerByReference(Constant.STATUS_FAILED,CustomerList.get(0).getData().get(i).getReference());

                        }


                        if (i == DescriptionList.get(0).getData().size() - 1 ){


                            TAG_SYNC = "stop" ;
                            return "TRUE";



                        }


                    }
                }else{
                    TAG_SYNC = "stop" ;
                    return "TRUE" ;

                }


            }catch (Exception ex){
                TAG_SYNC = "stop" ;
                Log.e("TAG", "Terjadi Kesalahan "+ex.getMessage());
                Toast.makeText(Syncronize.this, "Terjadi Kesalahan "+ex.getMessage(), Toast.LENGTH_SHORT).show();
                return "FALSE" ;
            }

            return null ;
        }

        @Override
        protected void onPostExecute(String s) {

            if (s.equals("TRUE")){
                TAG_SYNC = "stop";
                new KAlertDialog(Syncronize.this, KAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Notification")
                        .setContentText("Syncronized Berhasil")
                        .setConfirmText("OK")
                        .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                            @Override
                            public void onClick(KAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                                //getDataCustomerFromDBSetToTextview();


                                Intent intent = new Intent(Syncronize.this,Syncronize.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }
                        })
                        .show();


            }


            if (TAG_SYNC.equals("stop")){
                showProgress(false);
                tvprogresscount.setText(Constant.TAG_PLEASE_WAIT);
            }


            super.onPostExecute(s);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {

            super.onProgressUpdate(values);

            tvprogresscount.setText(TAG__STAUS_SYNC+" "+values[0] + " / " + TAG_SYNC_SIZE);

            if(values[0] == Constant.STATUS_ERROR_CODE_SYNC){

                showProgress(false);
                asyncdescdownloading.cancel(true);

                new KAlertDialog(Syncronize.this, KAlertDialog.WARNING_TYPE)
                        .setTitleText("Notification")
                        .setContentText("Syncronized Description Gagal, Terjadi Kesalahan")
                        .setConfirmText("OK")
                        .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                            @Override
                            public void onClick(KAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                                getDataCustomerFromDBSetToTextview();
                            }
                        })
                        .show();
            }


        }



    }


    private void checkInternet(){

        if (!Helper.isDeviceOnline(Syncronize.this)){
            new KAlertDialog(Syncronize.this, KAlertDialog.WARNING_TYPE)
                    .setTitleText("Notification")
                    .setContentText("Device Tidak Terhubung Internet")
                    .setConfirmText("OK")
                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                        @Override
                        public void onClick(KAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
                            finish();
                        }
                    })
                    .show();

            return;
        }


    }



    @Override
    public void onBackPressed() {

    }
}