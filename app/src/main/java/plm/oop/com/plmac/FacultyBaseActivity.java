package plm.oop.com.plmac;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

public class FacultyBaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    public Dialog popupNoInternet;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        popupNoInternet = new Dialog(this);
        popupNoInternet.setContentView(R.layout.popup_no_internet);

        checkConnection();
        Button popupNoInternetButton = popupNoInternet.findViewById(R.id.retryConnectionButton);
        popupNoInternetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkConnection();
            }
        });
    }

    void displayDrawer(){
        SharedPreferences facultyPref = getSharedPreferences("Faculty", 0);
        final String userName = facultyPref.getString("userName", "");
        final String userNumber = facultyPref.getString("userNumber", "");


        Toolbar mToolbar = findViewById(R.id.nav_action_bar);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);


        DrawerLayout mDrawerLayout = findViewById(R.id.drawerLayoutFaculty);
        ActionBarDrawerToggle mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.navView);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.headerFacultyName);
        navUsername.setText(userName);
        TextView navUsernumber = (TextView) headerView.findViewById(R.id.headerFacultyNumber);
        navUsernumber.setText(userNumber);

    }
    public void checkConnection() {

        if (!checkInternetConnection()) {
            Toast.makeText(getApplicationContext(), "Network error. Check your network connection and try again.", Toast.LENGTH_SHORT).show();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                Objects.requireNonNull(popupNoInternet.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
            popupNoInternet.show();


        } else {
            popupNoInternet.dismiss();
        }
    }

    public boolean checkInternetConnection() {
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        } else {
            connected = false;
        }
        return connected;
    }

    public void logout() {
        new AlertDialog.Builder(this)
                .setTitle("Logout?")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        clearPref();
                        finish();
                    }
                }).create().show();
        Log.i("Back", "Back");
    }

    public void clearPref(){
        SharedPreferences facultyPref = getSharedPreferences("Faculty", 0);
        SharedPreferences.Editor editor = facultyPref.edit();
        editor.clear();
        editor.apply();
        startActivity(new Intent(this, IntroScreenActivity.class));
        finish();
    }
    public void viewHome() {
        startActivity(new Intent(this, FacultyViewProfile.class));
        finish();
    }

    public void viewNews() {
        startActivity(new Intent(this, NewsFacultyActivity.class));
        finish();
    }

    public void viewUpdatePassword() {
        startActivity(new Intent(this, FacultyUpdatePassword.class));
        finish();
    }

    public void viewSubjects() {
        startActivity(new Intent(this, FacultyViewSubject.class));
        finish();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int i = item.getItemId();

        if (i == R.id.nav_home) {
            viewNews();
        } else if (i == R.id.nav_profile) {
            viewHome();
        } else if (i == R.id.nav_update_password) {
            viewUpdatePassword();
        } else if (i == R.id.nav_subjects) {
            viewSubjects();
        } else if (i == R.id.nav_logout) {
            logout();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayoutFaculty);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayoutFaculty);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            viewHome();

        }
    }


}
