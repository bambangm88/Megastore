package com.rsah.watermeter.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.google.zxing.Result;
import com.rsah.watermeter.Api.ApiService;
import com.rsah.watermeter.Api.Server;
import com.rsah.watermeter.R;
import com.rsah.watermeter.Session.SessionManager;
import com.rsah.watermeter.Util.DbCustomerHelper;
import com.rsah.watermeter.Util.Helper;
import com.rsah.watermeter.Util.Utility;

import java.util.ArrayList;
import java.util.HashMap;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static com.rsah.watermeter.Fragment.HomeFragment.ReferenceFromTaskList;
import static com.rsah.watermeter.Util.DbCustomerHelper.COLUMN_ADDRESS;
import static com.rsah.watermeter.Util.DbCustomerHelper.COLUMN_AREA;
import static com.rsah.watermeter.Util.DbCustomerHelper.COLUMN_COUNT_METER;
import static com.rsah.watermeter.Util.DbCustomerHelper.COLUMN_CST_ID;
import static com.rsah.watermeter.Util.DbCustomerHelper.COLUMN_CST_NAME;
import static com.rsah.watermeter.Util.DbCustomerHelper.COLUMN_ID;
import static com.rsah.watermeter.Util.DbCustomerHelper.COLUMN_REFERENCE;


public class ScanBarcode extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;

    public static final int REQUEST_CODE = 100;
    public static final int PERMISSION_REQUEST = 200;
    private RelativeLayout rlprogress;
    public static Boolean isGetResultData = false ;


    private ApiService API;
    private SessionManager sesion;
    private DbCustomerHelper SQLite ;
    private Switch ToogleFlashLight ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_barcode);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST);

        }




        SQLite = new DbCustomerHelper(this);
        API = Server.getAPIService();
        rlprogress = findViewById(R.id.rlprogress);
        isGetResultData = false ;
        ToogleFlashLight = findViewById(R.id.toggleFlashLight);

        mScannerView = new ZXingScannerView(this);
        RelativeLayout rl = findViewById(R.id.relative_scan);
        rl.addView(mScannerView);
        mScannerView.setResultHandler(this);





        mScannerView.startCamera();


        ToogleFlashLight.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    mScannerView.setFlash(isChecked);

            }
        });

        Toolbar toolbar = findViewById(R.id.toolbarSync);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
;

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


    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result rawResult) {

        String uid = Utility.UID;
        String secretkey = Utility.SECRETKEY;
        String reference = rawResult.getText().toString();
        String time = ""+System.currentTimeMillis();
        String signature = Helper.Hash_SHA256(uid+secretkey+time);

        if (!isGetResultData){
          //  requestGetCustomer(uid,signature,reference,time);
            getCustomer(reference);

        }

        mScannerView.resumeCameraPreview(this);
    }


    private void getCustomer(String reference) {
        ArrayList<HashMap<String, String>> data = SQLite.getCustomer(reference);

        if (data.size() == 0){
            Toast.makeText(ScanBarcode.this, "Customer Tidak Ada", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }





        for (int i = 0; i < data.size(); i++) {

            try {

                Toast.makeText(ScanBarcode.this, "Scan Berhasil", Toast.LENGTH_SHORT).show();
                String id = data.get(i).get(COLUMN_ID);
                String customer_id = data.get(i).get(COLUMN_CST_ID);
                String customer_name = data.get(i).get(COLUMN_CST_NAME);
                String ref = data.get(i).get(COLUMN_REFERENCE);
                String area = data.get(i).get(COLUMN_AREA);
                String address = data.get(i).get(COLUMN_ADDRESS);
                String startmeter= data.get(i).get(COLUMN_COUNT_METER);
                String status = data.get(i).get(COLUMN_COUNT_METER);
                ReferenceFromTaskList = ref ;

                Intent intent = new Intent();
                intent.putExtra("id", id);
                intent.putExtra("customer_id", customer_id);
                intent.putExtra("customer_name", customer_name);
                intent.putExtra("reference", ref);
                intent.putExtra("area", area);
                intent.putExtra("address", address);
                intent.putExtra("startmeter", startmeter);
                setResult(RESULT_OK, intent);
                finish();

            }catch (Exception ex){
                Toast.makeText(ScanBarcode.this, "Error "+ex.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }


        }

    }


    private void showProgress(Boolean bool){

        if (bool){
            rlprogress.setVisibility(View.VISIBLE);
        }else {
            rlprogress.setVisibility(View.GONE);
        }
    }




}