package com.rsah.watermeter.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.rsah.watermeter.Activity.BackUp;
import com.rsah.watermeter.Activity.Syncronize;
import com.rsah.watermeter.Auth.LoginActivity;
import com.rsah.watermeter.R;
import com.rsah.watermeter.Session.SessionManager;

public class SettingFragment extends Fragment {

    private LinearLayout btnmore , btnmlogout , btnsync , btnbackup;
    private SessionManager session ;


    public SettingFragment() {
        // Required empty public constructor
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_setting, container, false);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Setting");


        btnmlogout = root.findViewById(R.id.btnlogout);
        btnsync= root.findViewById(R.id.btn_sync);
        btnbackup= root.findViewById(R.id.btn_backup);
        session = new SessionManager(getContext());



        btnmlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                logout();

            }
        });

        btnsync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getContext(), Syncronize.class));


            }
        });

        btnbackup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getContext(), BackUp.class));


            }
        });




        return root;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    private void logout(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(false);
        builder.setMessage("Logout ?");
        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //logOut
                session.logoutUser();
                Intent intent = new Intent(getContext(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
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


}