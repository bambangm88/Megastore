package com.rsah.watermeter.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.rsah.watermeter.Activity.adapter.Adapter;
import com.rsah.watermeter.Model.response.ResponseCustomer;
import com.rsah.watermeter.R;
import com.rsah.watermeter.Util.DbCustomerHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.rsah.watermeter.Fragment.HomeFragment.ReferenceFromTaskList;
import static com.rsah.watermeter.Util.DbCustomerHelper.COLUMN_ADDRESS;
import static com.rsah.watermeter.Util.DbCustomerHelper.COLUMN_AREA;
import static com.rsah.watermeter.Util.DbCustomerHelper.COLUMN_COUNT_METER;
import static com.rsah.watermeter.Util.DbCustomerHelper.COLUMN_CST_ID;
import static com.rsah.watermeter.Util.DbCustomerHelper.COLUMN_CST_NAME;
import static com.rsah.watermeter.Util.DbCustomerHelper.COLUMN_FINAL_METER;
import static com.rsah.watermeter.Util.DbCustomerHelper.COLUMN_INITIAL_METER;
import static com.rsah.watermeter.Util.DbCustomerHelper.COLUMN_PREV_DATE_SCAN;
import static com.rsah.watermeter.Util.DbCustomerHelper.COLUMN_PREV_INITIAL_METER;
import static com.rsah.watermeter.Util.DbCustomerHelper.COLUMN_REFERENCE;
import static com.rsah.watermeter.Util.DbCustomerHelper.COLUMN_STATUS;
import static com.rsah.watermeter.Util.DbCustomerHelper.COLUMN_STATUS_SERVER;


public class ListFilter extends AppCompatActivity {

    private ListView listView;
    private AlertDialog.Builder dialog;
    private List<ResponseCustomer> itemList = new ArrayList<ResponseCustomer>();
    private Adapter adapter;
    private DbCustomerHelper SQLite ;
    private Context mContext ;
    private RelativeLayout card_notfound ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_filter);
        mContext = this ;
        SQLite = new DbCustomerHelper(mContext);
        listView = (ListView) findViewById(R.id.list_view);
        adapter = new Adapter(ListFilter.this, itemList);
        listView.setAdapter(adapter);



        Toolbar toolbar = findViewById(R.id.toolbarSync);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        String cstname = getIntent().getStringExtra("cstname");
        String ref = getIntent().getStringExtra("ref");
        String address = getIntent().getStringExtra("address");
        String cluster = getIntent().getStringExtra("cluster");
        String status = getIntent().getStringExtra("status");

        getAllCustomer(ref,address,cstname,status,cluster);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                final String id_ = itemList.get(position).getId();
                final String cst_id = itemList.get(position).getCustomer_id();
                final String cst_name = itemList.get(position).getCustomer_name();
                final String ref = itemList.get(position).getReference();
                final String area = itemList.get(position).getArea();
                final String address = itemList.get(position).getAddress();
                final String startmeter = itemList.get(position).getPrev_initial_meter();

                final String status_server = itemList.get(position).getStatus_server();

                if (!status_server.equals("1")){ //if status server 1 cannot click
                    Intent intent = new Intent();
                    intent.putExtra("id", id_);
                    intent.putExtra("customer_id", cst_id);
                    intent.putExtra("customer_name", cst_name);
                    intent.putExtra("reference", ref);
                    intent.putExtra("area", area);
                    intent.putExtra("address", address);
                    intent.putExtra("startmeter", startmeter);
                    setResult(RESULT_OK, intent);
                    ReferenceFromTaskList = ref ;
                    finish();
                }


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


    private void getAllCustomer(String ref , String address , String name, String status , String cluster) {
        ArrayList<HashMap<String, String>> row = SQLite.getAllCustomerByRefAddressName(ref,address,name,status,cluster);

        show_card_notfound(false);
        itemList.clear();
        if (row.size() == 0){
            show_card_notfound(true);
            Toast.makeText(mContext,"Customer Tidak Ditemukan",Toast.LENGTH_SHORT).show();
            adapter.notifyDataSetChanged();
            return;
        }


        for (int i = 0; i < row.size(); i++) {
            String reference = row.get(i).get(COLUMN_REFERENCE);
            String cstname = row.get(i).get(COLUMN_CST_NAME);
            String address_ = row.get(i).get(COLUMN_ADDRESS);
            String cstid = row.get(i).get(COLUMN_CST_ID);
            String area = row.get(i).get(COLUMN_AREA);
            String startmeter = row.get(i).get(COLUMN_COUNT_METER);
            String countmeter = row.get(i).get(COLUMN_COUNT_METER);
            String status_ = row.get(i).get(COLUMN_STATUS);
            String status_server = row.get(i).get(COLUMN_STATUS_SERVER);
            String endmeter = row.get(i).get(COLUMN_FINAL_METER);
            String prev_date_scan= row.get(i).get(COLUMN_PREV_DATE_SCAN);
            String prev_initial_meter = row.get(i).get(COLUMN_PREV_INITIAL_METER);
            String initial_meter = row.get(i).get(COLUMN_INITIAL_METER);

            ResponseCustomer data = new ResponseCustomer();
            data.setReference(reference);
            data.setCustomer_name(cstname);
            data.setAddress(address_);
            data.setArea(area);
            data.setCustomer_id(cstid);
            data.setPrev_initial_meter(startmeter);
            data.setStatus_sync(status_);
            data.setStatus_server(status_server);
            data.setFinal_meter(endmeter);
            data.setCount_meter(countmeter);
            data.setPrev_initial_meter(prev_initial_meter);
            data.setInitial_meter(initial_meter);
            data.setPrev_date_scan(prev_date_scan);



            itemList.add(data);
        }

        adapter.notifyDataSetChanged();
    }


    private void show_card_notfound(Boolean bool){

        card_notfound = findViewById(R.id.card_notfound);

        if (bool){
            card_notfound.setVisibility(View.VISIBLE);
        }else{
            card_notfound.setVisibility(View.GONE);
        }

    }




}