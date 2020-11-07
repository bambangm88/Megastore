package com.rsah.watermeter.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.rsah.watermeter.Activity.adapter.Adapter;
import com.rsah.watermeter.Constant.Constant;
import com.rsah.watermeter.Model.response.ResponseCustomer;
import com.rsah.watermeter.R;
import com.rsah.watermeter.Util.DbCustomerHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.R.layout.simple_spinner_item;
import static android.content.ContentValues.TAG;
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


public class TasklistFragment extends Fragment {

    public static boolean checkPeriod= true;
    private ListView listView;
    private AlertDialog.Builder dialog;
    private List<ResponseCustomer> itemList = new ArrayList<ResponseCustomer>();
    private Adapter adapter;
    private DbCustomerHelper SQLite ;
    private Context mContext ;
    private RelativeLayout card_notfound ;
    private ArrayList<String> arrayStatus = new ArrayList<String>();
    private Spinner spStatus ;
    private LayoutInflater inflater;
    private View dialogView;
    private EditText tvcstname , tvref , tvaddress , tvcluster;
    private Button btn__submit_filter ;
    private FloatingActionButton btn_filter ;
    private TextView jumlahunit ;


    public TasklistFragment() {
        // Required empty public constructor
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_tasklist, container, false);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Tasklist");


        mContext = getContext();
        SQLite = new DbCustomerHelper(mContext);
        listView = root.findViewById(R.id.list_view);
        adapter = new Adapter(getActivity(), itemList);
        listView.setAdapter(adapter);
        card_notfound = root.findViewById(R.id.card_notfound);
        btn_filter = root.findViewById(R.id.btnFilter);
        jumlahunit = root.findViewById(R.id.jumlahUnit);

        btn_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(getContext(), Filter.class));
                filter_dialog_popup();
            }
        });

        checkPeriod = true ;

        getAllCustomer();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                final String ref = itemList.get(position).getReference();
                final String status_server = itemList.get(position).getStatus_server();

                if (!status_server.equals("1")){ //if status server 1 cannot click
                    loadFragment(new MainScanFragment());
                    HomeFragment.ReferenceFromTaskList = ref ;
                    //Constant.PUBLIC_REFERENCE = ref ;
                }



            }


        });




        return root;
    }


    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.nav_host_fragment, fragment, fragment.getClass().getSimpleName());
        // transaction.addToBackStack(null);
        transaction.commit();
    }

    private void getAllCustomer() {

        ArrayList<HashMap<String, String>> row = SQLite.getAllTaskListCustomer();
        int Alldata = SQLite.getCustomerCountAll();
        jumlahunit.setText("Menampilkan "+row.size()+" / "+Alldata + " unit");

        show_card_notfound(false);
        itemList.clear();
        if (row.size() == 0){
            show_card_notfound(true);
            Toast.makeText(mContext,"Tasklist Tidak Ditemukan",Toast.LENGTH_SHORT).show();
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
            String status = row.get(i).get(COLUMN_STATUS);
            String status_server = row.get(i).get(COLUMN_STATUS_SERVER);
            String endmeter = row.get(i).get(COLUMN_FINAL_METER);
            String prev_initial_meter = row.get(i).get(COLUMN_PREV_INITIAL_METER);
            String initial_meter = row.get(i).get(COLUMN_INITIAL_METER);
            String prev_date_scan = row.get(i).get(COLUMN_PREV_DATE_SCAN);

            ResponseCustomer data = new ResponseCustomer();
            data.setReference(reference);
            data.setCustomer_name(cstname);
            data.setAddress(address_);
            data.setArea(area);
            data.setCustomer_id(cstid);
            data.setPrev_initial_meter(startmeter);
            data.setStatus_sync(status);
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


        if (bool){
            card_notfound.setVisibility(View.VISIBLE);
        }else{
            card_notfound.setVisibility(View.GONE);
        }

    }


    private void getAllCustomerByFilter(String ref , String address , String name, String status , String cluster) {
        ArrayList<HashMap<String, String>> row = SQLite.getAllCustomerByRefAddressName(ref,address,name,status,cluster);

        jumlahunit.setText(""+row.size()+" unit");

        Log.e(TAG, "getAllCustomerByFilter: "+row.size()+" unit" );

        show_card_notfound(false);
        itemList.clear();
        if (row.size() == 0){
            show_card_notfound(true);
            Toast.makeText(mContext,"Tasklist Tidak Ditemukan",Toast.LENGTH_SHORT).show();
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
            String prev_initial_meter = row.get(i).get(COLUMN_PREV_INITIAL_METER);
            String initial_meter = row.get(i).get(COLUMN_INITIAL_METER);
            String prev_date_scan= row.get(i).get(COLUMN_PREV_DATE_SCAN);

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


    private void clear_filter() {
        tvcstname.setText("");
        tvref.setText("");
        tvaddress.setText("");
    }


    private void filter_dialog_popup(){


        dialog = new AlertDialog.Builder(getContext());
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.activity_filter, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);

        tvcstname = dialogView.findViewById(R.id.tvcstname);
        tvref = dialogView.findViewById(R.id.tvreference);
        tvaddress = dialogView.findViewById(R.id.tvaddress);
        tvcluster = dialogView.findViewById(R.id.tvcluster);
        btn__submit_filter = dialogView.findViewById(R.id.btnFilter);
        spStatus= dialogView.findViewById(R.id.spStatus);

        arrayStatus.clear();
        arrayStatus.add("-- status --") ;
        arrayStatus.add(Constant.STATUS_OPEN) ;
        arrayStatus.add(Constant.STATUS_PENDING);
        arrayStatus.add(Constant.STATUS_COMPLETED) ;

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(mContext, simple_spinner_item,arrayStatus );
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spStatus.setAdapter(spinnerArrayAdapter);


        clear_filter();

        final AlertDialog ad = dialog.show();

        btn__submit_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String cstname = tvcstname.getText().toString();
                String ref = tvref.getText().toString();
                String address = tvaddress.getText().toString();
                String cluster = tvcluster.getText().toString();
                String status = spStatus.getSelectedItem().toString();

                //region handle empty like query
                if (cstname.equals("")){
                    cstname = "XXXX" ;
                }

                if (ref.equals("")){
                    ref = "XXXX" ;
                }

                if (address.equals("")){
                    address = "XXXX" ;
                }

                if (cluster.equals("")){
                    cluster = "XXXX" ;
                }
                //end region handle empty like query


                if (status.equals(Constant.STATUS_COMPLETED)){
                    status = Constant.STATUS_UPDATED ;
                }

                if (status.equals(Constant.STATUS_PENDING)){
                    status = Constant.STATUS_PENDING_CONFIRM ;
                }


                getAllCustomerByFilter(ref,address,cstname,status,cluster);

                ad.dismiss();

            }
        });


        dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });



    }



    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}