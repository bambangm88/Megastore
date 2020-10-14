package com.rsah.watermeter.Fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.developer.kalert.KAlertDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.rsah.watermeter.Activity.ListFilter;
import com.rsah.watermeter.Activity.ScanBarcode;
import com.rsah.watermeter.Api.ApiService;
import com.rsah.watermeter.Api.Server;
import com.rsah.watermeter.Constant.Constant;
import com.rsah.watermeter.Model.response.ResponseLogin;
import com.rsah.watermeter.R;
import com.rsah.watermeter.Session.SessionManager;
import com.rsah.watermeter.Util.DbCustomerHelper;
import com.rsah.watermeter.Util.Helper;
import com.rsah.watermeter.Util.Utility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import static android.R.layout.simple_spinner_item;
import static com.rsah.watermeter.Fragment.UpdateScanFragment.isPending;
import static com.rsah.watermeter.Util.DbCustomerHelper.COLUMN_ADDRESS;
import static com.rsah.watermeter.Util.DbCustomerHelper.COLUMN_AREA;
import static com.rsah.watermeter.Util.DbCustomerHelper.COLUMN_COUNT_METER;
import static com.rsah.watermeter.Util.DbCustomerHelper.COLUMN_CST_ID;
import static com.rsah.watermeter.Util.DbCustomerHelper.COLUMN_CST_NAME;
import static com.rsah.watermeter.Util.DbCustomerHelper.COLUMN_DESCRIPTION;
import static com.rsah.watermeter.Util.DbCustomerHelper.COLUMN_ID;
import static com.rsah.watermeter.Util.DbCustomerHelper.COLUMN_ID_DESCRIPTION;
import static com.rsah.watermeter.Util.DbCustomerHelper.COLUMN_PREV_INITIAL_METER;
import static com.rsah.watermeter.Util.DbCustomerHelper.COLUMN_REFERENCE;

public class HomeFragment extends Fragment {


    private Button btnSaveMeter ;
    private FloatingActionButton btn_scan , btn_filter ;
    private ImageView ImgPrevMeter ,ImgPrevUnit;
    private EditText etid, etcstid, etcstname , etref , etarea, etaddress , etstartmeter , etendmeter , etdate, etdesc ;
    private int bitmap_size = 100; // range 1 - 100
    private Bitmap bitmap, decoded;
    private ProgressDialog pDialog;
    private int GALLERY = 4, CAMERA = 2;
    private RelativeLayout rlprogress;
    private ApiService API;
    private SessionManager session;
    private DbCustomerHelper SQLite ;
    private static String SETFOTO = "" ;
    private AlertDialog.Builder dialog;
    private LayoutInflater inflater;
    private View dialogView;
    private EditText tvcstname , tvref , tvaddress , tvlainlain , tvcluster , etprev_consum;
    private Button btn__submit_filter , btnLainLain ;
    private Spinner spStatus , spDesc ;
    private ArrayList<String> arrayStatus = new ArrayList<String>();
    private ArrayList<String> arrayDesc= new ArrayList<String>();
    private Switch swispending ;

    public static String ReferenceFromTaskList  = "" ;
    public static String publicstartmeter  = "" ;
    private RelativeLayout card_notfound ;
    private LinearLayout card_data ;






    public HomeFragment () {
        // Required empty public constructor
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Scan");

        API = Server.getAPIService();
        session = new SessionManager(getContext());

        btn_scan = root.findViewById(R.id.btnScan);
        btn_filter = root.findViewById(R.id.btnFilter);
        btnSaveMeter = root.findViewById(R.id.btnSaveMeter);

        rlprogress = root.findViewById(R.id.rlprogress);
        SQLite = new DbCustomerHelper(getContext());


        ImgPrevMeter = root.findViewById(R.id.ivmeter);
        ImgPrevUnit = root.findViewById(R.id.ivunit);

        etid = root.findViewById(R.id.tvid);
        etcstid = root.findViewById(R.id.tvcstid);
        etcstname = root.findViewById(R.id.tvcstname);
        etref = root.findViewById(R.id.tvref);
        etarea = root.findViewById(R.id.tvarea);
        etaddress = root.findViewById(R.id.tvaddress);
        etprev_consum = root.findViewById(R.id.tvprev_consum);
        etstartmeter = root.findViewById(R.id.etstartmeter);
        etendmeter = root.findViewById(R.id.etfinalmeter);
        etdate = root.findViewById(R.id.etdate);
        etdesc = root.findViewById(R.id.etdesc);
        spDesc = root.findViewById(R.id.spDesc);
        swispending = root.findViewById(R.id.swispending);
        card_notfound = root.findViewById(R.id.card_notfound);
        card_data = root.findViewById(R.id.card_data);

        Helper.requestMultiplePermissions(getActivity());


        display_desc();


        btn_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getContext(), ScanBarcode.class),1);
            }
        });

        btn_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(getContext(), Filter.class));
                filter_dialog_popup();
            }
        });


        ImgPrevMeter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SETFOTO = "1" ;
                //showPictureDialog();
                takePhotoFromCamera();
            }
        });

        ImgPrevUnit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SETFOTO = "2" ;
                //showPictureDialog();
                takePhotoFromCamera();
            }
        });

        etdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //takePhotoFromCamera();
                Helper.showDateDialog(getContext(),etdate,"yyyy-MM-dd HH:mm:ss");
            }
        });


        spDesc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String desc = spDesc.getSelectedItem().toString();

                if (desc.equals("Lain lain") || desc.equals("Lain-lain") || desc.equals("lain lain") || desc.equals("Lain Lain")  || desc.equals("Other") || desc.equals("other") || desc.equals("LAIN-LAIN") || desc.equals("LAIN LAIN")|| desc.equals("Lainnya") || desc.equals("lainnya") || desc.equals("LAINNYA")){
                    spDesc.setSelection(0); // keterangan)
                    desc_lain_lain();
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        btnSaveMeter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ResponseLogin data = Helper.DecodeFromJsonResponseLogin(session.getInstanceUser());

                String uid		    = Utility.UID ;
                String secret		= Utility.SECRETKEY ;

                String reference	= etref.getText().toString() ;
                String customer_id	= etcstid.getText().toString() ;
                String customer_name	= etcstname.getText().toString() ;
                String area			= etarea.getText().toString() ;
                String address		= etaddress.getText().toString() ;
                String initial_meter	= etstartmeter.getText().toString();
                String final_meter 	= etendmeter.getText().toString();
                String tglcreate  	= etdate.getText().toString();
                String userid  		= data.getUserid();
                String desc  		= spDesc.getSelectedItem().toString();
                //String signature  		= Helper.Hash_SHA256(uid+secret+reference);

                String imgfile = "" ;
                String imgfile_2 = "" ;
                //check image is include
                if (ImgPrevMeter.getTag().toString().equals("ISI")) {
                    imgfile = Helper.getStringImage(Helper.imageView2Bitmap(ImgPrevMeter));
                }

                if (ImgPrevUnit.getTag().toString().equals("ISI")) {
                    imgfile_2 = Helper.getStringImage(Helper.imageView2Bitmap(ImgPrevUnit));
                }

                if (final_meter.equals("")) {
                    final_meter = "" ;
                }

                if (desc.equals("-- keterangan --")){
                    desc = "" ;
                }



                //cek

                if (customer_name.equals("")){
                    etcstname.setError(Constant.FIELD_REQUIRED);
                }
                else if (reference.equals("")){
                    etref.setError(Constant.FIELD_REQUIRED);
                }
                else if (area.equals("")){
                    etarea.setError(Constant.FIELD_REQUIRED);
                }
                else if (address.equals("")){
                    etaddress.setError(Constant.FIELD_REQUIRED);
                }
                else if (initial_meter.equals("")){
                    etstartmeter.setError(Constant.FIELD_REQUIRED);
                }
                else if (final_meter.equals("")){
                    etendmeter.setError(Constant.FIELD_REQUIRED);
                }
                else if (tglcreate.equals("")){
                    etdate.setError(Constant.FIELD_REQUIRED);
                }
                else if (!isPending){
                    if(final_meter.equals("0")){
                        etendmeter.setError(Constant.FIELD_REQUIRED);
                    }
                    else if (Integer.parseInt(final_meter) < Integer.parseInt(initial_meter ) ){
                        etendmeter.setError("tidak boleh kurang dari start meter");
                    }else {
                        confirm(imgfile,imgfile_2,initial_meter,final_meter,tglcreate,userid,reference,desc,Constant.STATUS_UPDATED , Constant.STATUS_UPDATED , Constant.STATUS_SERVER_SCAN);
                    }
                }
                else {

                    if (desc.equals("")){
                        Helper.message_error(getContext(),Constant.MESSAGE_ERROR_2);
                    }
                    else if (imgfile.equals("")){
                        Helper.message_error(getContext(),Constant.MESSAGE_ERROR_1);
                    }
                    else if (imgfile_2.equals("")){
                        Helper.message_error(getContext(),Constant.MESSAGE_ERROR_1);
                    }

                    else{
                        confirm(imgfile,imgfile_2,"0","0",tglcreate,userid,reference,desc,Constant.STATUS_PENDING_CONFIRM , Constant.STATUS_UPDATED , Constant.STATUS_SERVER_PENDING);
                    }



                }



            }
        });



        swispending.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked){
                    isPending = true ;
                    etendmeter.setText("0");
                    etendmeter.setEnabled(false);
                    String startmeter = etstartmeter.getText().toString();
                    publicstartmeter = startmeter ;
                    etstartmeter.setText("0");

                }else {
                    etendmeter.setError(Constant.FIELD_REQUIRED);
                    etendmeter.setEnabled(true);
                    etstartmeter.setText(publicstartmeter);
                    publicstartmeter = "" ;
                    isPending = false ;
                }


            }
        });


        if (!ReferenceFromTaskList.equals("")){
            getCustomer(ReferenceFromTaskList);
            //ReferenceFromTaskList = "" ;
        }





        return root;
    }



    private void showPictureDialog(){
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(getContext());
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select berkas from gallery",
                "Capture berkas from camera" };
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }



    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }


    private void takePhotoFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == 0) {
            return;
        }

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK) {

                show_data(true);

                String id = data.getStringExtra("id");
                String customer_id = data.getStringExtra("customer_id");
                String customer_name = data.getStringExtra("customer_name");
                String reference = data.getStringExtra("reference");
                String area = data.getStringExtra("area");
                String address = data.getStringExtra("address");
                String startmeter = data.getStringExtra("startmeter");

                etid.setText(id);
                etcstid.setText(customer_id);
                etcstname.setText(customer_name);
                etref.setText(reference);
                etarea.setText(area);
                etaddress.setText(address);
                etstartmeter.setText(startmeter);
                publicstartmeter = startmeter ;
                loadFragment(new MainScanFragment());


            }
        }


        if (requestCode == CAMERA) {
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");

                if (SETFOTO.equals("1")){
                    ImgPrevMeter.setImageBitmap(thumbnail);
                    ImgPrevMeter.setTag("ISI");
                }else if(SETFOTO.equals("2")){
                    ImgPrevUnit.setImageBitmap(thumbnail);
                    ImgPrevUnit.setTag("ISI");
                }



        }

        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {

                Bitmap thumbnail  = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), contentURI);


                if (SETFOTO.equals("1")){
                    ImgPrevMeter.setImageBitmap(thumbnail);
                    ImgPrevMeter.setTag("ISI");
                }else if(SETFOTO.equals("2")){
                    ImgPrevUnit.setImageBitmap(thumbnail);
                    ImgPrevUnit.setTag("ISI");
                }


                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        }





    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }



    private void showProgress(Boolean bool){

        if (bool){
            rlprogress.setVisibility(View.VISIBLE);
        }else {
            rlprogress.setVisibility(View.GONE);
        }
    }

    private void refresh (){
        publicstartmeter = "" ;
        ImgPrevMeter.setImageResource(R.drawable.emptyimage);
        ImgPrevMeter.setTag("");
        ImgPrevUnit.setImageResource(R.drawable.emptyimage);
        ImgPrevUnit.setTag("");
        etid.setText("");
        etcstid.setText("");
        etcstname.setText("");
        etref.setText("");
        etarea.setText("");
        etaddress.setText("");
        etstartmeter.setText("");
        etendmeter.setText("");
        etdate.setText("");
        etdesc.setText("");
        isPending = false ;
        swispending.setChecked(false);
        spDesc.setSelection(0);
        etendmeter.setEnabled(true);

    }

    private void confirm(String image,String image_2, String startmeter , String finalmeter, String date, String userid , String reference , String description , String status , String status_sync , String status_server){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(false);
        builder.setMessage("simpan data ?");
        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                    //save meter customer
                SQLite.updateCustomerByReference(image,image_2,startmeter,finalmeter,date,userid,reference,description,status,status_sync,status_server);

                refresh();
                new KAlertDialog(getContext(), KAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Notification")
                        .setContentText("Data Customer Berhasil Disimpan")
                        .setConfirmText("OK")
                        .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                            @Override
                            public void onClick(KAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();

                            }
                        })
                        .show();

                show_data(false);
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


    private void clear_filter() {
        tvcstname.setText("");
        tvref.setText("");
        tvaddress.setText("");
    }

    private void display_desc(){


        arrayDesc.clear();
        arrayDesc.add("-- keterangan --") ;

        ArrayList<HashMap<String, String>> data = SQLite.getAllDescription();

        for (int i = 0; i < data.size(); i++) {

            try {

                String id = data.get(i).get(COLUMN_ID_DESCRIPTION);
                String description = data.get(i).get(COLUMN_DESCRIPTION);
                arrayDesc.add(description);

            }catch (Exception ex){
                Toast.makeText(getContext(), "Error "+ex.getMessage(), Toast.LENGTH_SHORT).show();

            }


        }



        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getContext(), simple_spinner_item,arrayDesc);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spDesc.setAdapter(spinnerArrayAdapter);









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

            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getContext(), simple_spinner_item,arrayStatus );
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

                    Intent intent = new Intent(getContext(),ListFilter.class);
                    intent.putExtra("cstname", cstname);
                    intent.putExtra("ref", ref);
                    intent.putExtra("address", address);
                    intent.putExtra("cluster", cluster);
                    intent.putExtra("status", status);

                    startActivityForResult(intent,1);

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




    private void desc_lain_lain(){


        dialog = new AlertDialog.Builder(getContext());
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.activity_desc_lain_lain, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);

        tvlainlain = dialogView.findViewById(R.id.tvlainlain);
        btnLainLain = dialogView.findViewById(R.id.btnLain_lain);
        tvlainlain.setText("");

        final AlertDialog ad = dialog.show();

        btnLainLain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String lain2 = tvlainlain.getText().toString();

                arrayDesc.add(lain2) ;
                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getContext(), simple_spinner_item,arrayDesc);
                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
                spDesc.setAdapter(spinnerArrayAdapter);

                spDesc.setSelection(arrayDesc.indexOf(lain2));

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





    private void getCustomer(String reference) {
        ArrayList<HashMap<String, String>> data = SQLite.getCustomer(reference);

        if (data.size() == 0){
            Toast.makeText(getContext(), "Customer Tidak Ada", Toast.LENGTH_SHORT).show();
            return;
        }

        show_data(true);

        for (int i = 0; i < data.size(); i++) {

            try {

                //Toast.makeText(getContext(), "Scan Berhasil", Toast.LENGTH_SHORT).show();
                String id = data.get(i).get(COLUMN_ID);
                String customer_id = data.get(i).get(COLUMN_CST_ID);
                String customer_name = data.get(i).get(COLUMN_CST_NAME);
                String ref = data.get(i).get(COLUMN_REFERENCE);
                String area = data.get(i).get(COLUMN_AREA);
                String address = data.get(i).get(COLUMN_ADDRESS);
                String startmeter= data.get(i).get(COLUMN_COUNT_METER);
                String preinitialmeter= data.get(i).get(COLUMN_PREV_INITIAL_METER);
                String prefinalmeter= data.get(i).get(COLUMN_COUNT_METER);


                if (prefinalmeter.equals("")){
                    prefinalmeter = "0" ;
                }
                if (preinitialmeter.equals("")){
                    preinitialmeter  = "0" ;
                }

                int prevfnlmtr = Integer.parseInt(prefinalmeter);
                int previnitmtr = Integer.parseInt(preinitialmeter);
                int totalprevcconsum = prevfnlmtr - previnitmtr ;


                etid.setText(id);
                etcstid.setText(customer_id);
                etcstname.setText(customer_name);
                etref.setText(ref);
                etarea.setText(area);
                etaddress.setText(address);
                etprev_consum.setText(""+totalprevcconsum);

                if(isPending){
                    etstartmeter.setText("0");
                }else {
                    etstartmeter.setText(startmeter);
                }


                publicstartmeter = startmeter ;

            }catch (Exception ex){
                Toast.makeText(getContext(), "Error "+ex.getMessage(), Toast.LENGTH_SHORT).show();

            }


        }

    }


    private void show_data(Boolean bool){
        if (bool){
            card_data.setVisibility(View.VISIBLE);
            card_notfound.setVisibility(View.GONE);
        }else{
            card_notfound.setVisibility(View.VISIBLE);
            card_data.setVisibility(View.GONE);
        }

    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.nav_host_fragment, fragment, fragment.getClass().getSimpleName());
        // transaction.addToBackStack(null);
        transaction.commit();
    }



}