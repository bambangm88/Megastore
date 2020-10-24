package com.rsah.watermeter;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.rsah.watermeter.Auth.LoginActivity;
import com.rsah.watermeter.Fragment.HomeFragment;
import com.rsah.watermeter.Fragment.SettingFragment;
import com.rsah.watermeter.Fragment.TasklistFragment;
import com.rsah.watermeter.Fragment.MainScanFragment;
import com.rsah.watermeter.Model.response.ResponseLogin;
import com.rsah.watermeter.Session.SessionManager;
import com.rsah.watermeter.Util.AESUtil;
import com.rsah.watermeter.Util.Helper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {

    private AppBarConfiguration mAppBarConfiguration;
    private SessionManager session ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);




        session = new SessionManager(this);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);



        //default
        // displaySelectedScreenNavigationDrawer(R.id.nav_home);

        loadFragment(new MainScanFragment());

        //set user name and id in textview
        ResponseLogin user = Helper.DecodeFromJsonResponseLogin(session.getInstanceUser());
        String name = user.getUsername();
        String id = user.getUserid();
        TextView txttitle  = headerView.findViewById(R.id.txtname);
        TextView txtname = headerView.findViewById(R.id.txttitle);
        txtname.setText(name);
        txttitle.setText(id);





    }


    private void displaySelectedScreenNavigationDrawer(int itemId) {

        //creating fragment object
        Fragment fragment = null;

        //initializing the fragment object which is selected
        switch (itemId) {
            case R.id.scan:
                HomeFragment.ReferenceFromTaskList = "" ;
                fragment = new MainScanFragment();
                break;
            case R.id.tasklist:
                HomeFragment.ReferenceFromTaskList = "" ;
                fragment = new TasklistFragment();
                break;
            case R.id.synch:
                HomeFragment.ReferenceFromTaskList = "" ;
                fragment = new SettingFragment();
                break;
            case R.id.logout:
                HomeFragment.ReferenceFromTaskList = "" ;
               //startActivity(new Intent(this, LoginActivity.class));
                logout();
                break;
        }

        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.nav_host_fragment, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }




    //Bottom Navigation
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.Scan_bottom:
                    HomeFragment.ReferenceFromTaskList = "" ;
                    MainScanFragment home = new MainScanFragment();
                    loadFragment(home);
                    return true;

                case R.id.Tasklist:
                    HomeFragment.ReferenceFromTaskList = "" ;
                    TasklistFragment task = new TasklistFragment();
                    loadFragment(task);
                    return true;

                case R.id.Setting:
                    HomeFragment.ReferenceFromTaskList = "" ;
                    SettingFragment profile = new SettingFragment();
                    loadFragment(profile);
                    return true;

            }

            return false;
        }
    };

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.nav_host_fragment, fragment, fragment.getClass().getSimpleName());
        // transaction.addToBackStack(null);
        transaction.commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        displaySelectedScreenNavigationDrawer(item.getItemId());
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void logout(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("Logout ?");
        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //logOut
                session.logoutUser();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
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