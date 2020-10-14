package com.rsah.watermeter.Fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
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
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.developer.kalert.KAlertDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.rsah.watermeter.Activity.ListFilter;
import com.rsah.watermeter.Api.ApiService;
import com.rsah.watermeter.Api.Server;
import com.rsah.watermeter.BuildConfig;
import com.rsah.watermeter.Constant.Constant;
import com.rsah.watermeter.Model.response.ResponseLogin;
import com.rsah.watermeter.R;
import com.rsah.watermeter.Session.SessionManager;
import com.rsah.watermeter.Util.DbCustomerHelper;
import com.rsah.watermeter.Util.Helper;
import com.rsah.watermeter.Util.Utility;
import com.watermark.androidwm.WatermarkBuilder;
import com.watermark.androidwm.bean.WatermarkText;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import static android.R.layout.simple_spinner_item;
import static com.rsah.watermeter.Fragment.HomeFragment.ReferenceFromTaskList;

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

public class UpdateScanFragment extends Fragment {


    private Button btnSaveMeter ;
    private FloatingActionButton btn_scan , btn_filter ;
    private ImageView ImgPrevMeter ,ImgPrevUnit;
    private EditText etid, etcstid, etcstname , etref , etarea, etaddress , etstartmeter , etendmeter , etdate, etdesc ,etconsumption ;
    private int bitmap_size = 60; // range 1 - 100
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
    private EditText tvcstname , tvref , tvaddress , tvlainlain, etprev_consum;
    private Button btn__submit_filter , btnLainLain ;
    private Spinner spStatus , spDesc ;
    private ArrayList<String> arrayStatus = new ArrayList<String>();
    private ArrayList<String> arrayDesc= new ArrayList<String>();
    private Switch swispending ;
    public static String publicstartmeter  = "" ;
    public static String publicprevconsum  = "" ;
    private RelativeLayout card_notfound ;
    private LinearLayout card_data ;
    public static Boolean isPending = false ;
    static final int REQUEST_TAKE_PHOTO = 2;

    String mCurrentPhotoPath;

    String currentPhotoPath;



    public UpdateScanFragment() {
        // Required empty public constructor
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_update_scan, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Scan");

        API = Server.getAPIService();
        session = new SessionManager(getContext());


        btnSaveMeter = root.findViewById(R.id.btnSaveMeter);

        rlprogress = root.findViewById(R.id.rlprogress);
        SQLite = new DbCustomerHelper(getContext());


        ImgPrevMeter = root.findViewById(R.id.ivmeter);
        ImgPrevUnit = root.findViewById(R.id.ivunit);

        etid = root.findViewById(R.id.tvid);
        etconsumption = root.findViewById(R.id.etconsumption);
        etcstid = root.findViewById(R.id.tvcstid);
        etcstname = root.findViewById(R.id.tvcstname);
        etref = root.findViewById(R.id.tvref);
        etarea = root.findViewById(R.id.tvarea);
        etaddress = root.findViewById(R.id.tvaddress);
        etstartmeter = root.findViewById(R.id.etstartmeter);
        etprev_consum = root.findViewById(R.id.tvprev_consum);
        etendmeter = root.findViewById(R.id.etfinalmeter);
        etdate = root.findViewById(R.id.etdate);
        etdesc = root.findViewById(R.id.etdesc);
        spDesc = root.findViewById(R.id.spDesc);
        swispending = root.findViewById(R.id.swispending);
        card_notfound = root.findViewById(R.id.card_notfound);
        card_data = root.findViewById(R.id.card_data);

        Helper.requestMultiplePermissions(getActivity());


        display_desc();




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



        etendmeter.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void afterTextChanged(Editable mEdit)
            {

                String start = etstartmeter.getText().toString() ;
                String end = etendmeter.getText().toString() ;

                if (start.equals("")){
                    start = "0" ;
                }

                if (end.equals("")){
                    end = "0" ;
                }

                int lengend = end.length();

                if (lengend < 15){
                    Long a = Long.parseLong(start);
                    Long b = Long.parseLong(end);
                    Long consum = b-a ;
                    etconsumption.setText(""+consum);
                    if (end.equals("")){
                        etconsumption.setText("0");
                    }
                }


            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after){



            }

            public void onTextChanged(CharSequence s, int start, int before, int count){




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


                String tglcreate  	= Helper.getDateTimeNow();
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
                    String prevconsum_ = etprev_consum.getText().toString();
                    publicstartmeter = startmeter ;
                    publicprevconsum = prevconsum_ ;
                    etstartmeter.setText("0");
                    etconsumption.setText("0");
                    etprev_consum.setText("0");

                }else {
                    etendmeter.setError(Constant.FIELD_REQUIRED);
                    etendmeter.setEnabled(true);
                    etstartmeter.setText(publicstartmeter);
                    etprev_consum.setText(publicprevconsum);
                    publicstartmeter = "" ;
                    publicprevconsum = "" ;
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
       // Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //startActivityForResult(intent, CAMERA);

        dispatchTakePictureIntent();

    }


    private byte[] bitmapToByte(Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
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

            }
        }


        if (requestCode == CAMERA) {
                //Bitmap thumbnail = (Bitmap) data.getExtras().get("data");


            // Get the dimensions of the View
            int targetW = 1000;
            int targetH = 1000;

            // Get the dimensions of the bitmap
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;

            BitmapFactory.decodeFile(currentPhotoPath, bmOptions);

            int photoW = bmOptions.outWidth;
            int photoH = bmOptions.outHeight;

            // Determine how much to scale down the image
            int scaleFactor = Math.max(1, Math.min(photoW/targetW, photoH/targetH));

            // Decode the image file into a Bitmap sized to fill the View
            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = scaleFactor;
            bmOptions.inPurgeable = true;

            Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath, bmOptions);

            ExifInterface exif = null;
            try {
                exif = new ExifInterface(currentPhotoPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);

            Bitmap bmRotated = Helper.rotateBitmap(bitmap, orientation);

           // addStampToImage(bmRotated);
            bmRotated = Helper.waterMark(bmRotated, Helper.getDateTimeNow(),  Color.RED, 90, 90, true);


            //imageView.setImageBitmap(bitmap);
            if (SETFOTO.equals("1")){

                ImgPrevMeter.setImageBitmap(bmRotated);
                ImgPrevMeter.setTag("ISI");

//                WatermarkBuilder.create(getActivity(), ImgPrevMeter)
//                        .loadWatermarkText(watermarkText)
//
//                        .getWatermark()
//                        .setToImageView(ImgPrevMeter);


            }else if(SETFOTO.equals("2")){
                ImgPrevUnit.setImageBitmap(bmRotated);
                ImgPrevUnit.setTag("ISI");

//                WatermarkBuilder.create(getActivity(), ImgPrevUnit)
//                        .loadWatermarkText(watermarkText)
//                        .getWatermark()
//                        .setToImageView(ImgPrevUnit);
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




    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getContext(),
                        BuildConfig.APPLICATION_ID + ".provider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
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
        ReferenceFromTaskList  = "" ;

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
                etstartmeter.setText(startmeter);
                etprev_consum.setText(""+totalprevcconsum);
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





}