package plm.oop.com.plmac;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NewsStudentActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_student);
        SharedPreferences studentPref = getSharedPreferences("Student", 0);
        final String userName = studentPref.getString("userName", "");
        final String userNumber = studentPref.getString("userNumber", "");
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference newsReference = firebaseDatabase.getReference("Admin").child("News");
        newsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> title = new ArrayList<>();
                ArrayList<String> date = new ArrayList<>();
                ArrayList<String> content = new ArrayList<>();
                for (DataSnapshot zoneSnapshot : dataSnapshot.getChildren()) {
                    date.add(zoneSnapshot.getKey());
                    title.add(zoneSnapshot.child("Title").getValue(String.class));
                    content.add(zoneSnapshot.child("Content").getValue(String.class));
                }
                String[] titleArray = title.toArray(new String[0]);
                String[] dateArray = date.toArray(new String[0]);
                String[] contentArray = content.toArray(new String[0]);

                NewsAdapter place = new NewsAdapter(NewsStudentActivity.this, dateArray, titleArray, contentArray);
                listView = findViewById(R.id.lvNewsStudent);
                listView.setAdapter(place);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //MENU
        Toolbar mToolbar = findViewById(R.id.nav_action_bar);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);


        DrawerLayout mDrawerLayout = findViewById(R.id.drawerLayoutStudent);
        ActionBarDrawerToggle mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.navView);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.headerStudentName);
        navUsername.setText(userName);
        TextView navUsernumber = (TextView) headerView.findViewById(R.id.headerStudentNumber);
        navUsernumber.setText(userNumber);
        //END OF MENU
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayoutStudent);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            startActivity(new Intent(NewsStudentActivity.this, StudentMainActivity.class));
            finish();
        }
    }

    public void logout() {
        new AlertDialog.Builder(this)
                .setTitle("Logout?")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SharedPreferences studentPref = getSharedPreferences("Student", 0);
                        SharedPreferences.Editor editor = studentPref.edit();
                        editor.clear();
                        editor.apply();
                        startActivity(new Intent(NewsStudentActivity.this, IntroScreenActivity.class));
                        finish();
                    }
                }).create().show();
    }


    public void viewHome() {
        startActivity(new Intent(NewsStudentActivity.this, StudentMainActivity.class));
        finish();
    }

    public void viewNews() {
        startActivity(new Intent(NewsStudentActivity.this, NewsStudentActivity.class));
        finish();
    }

    public void viewUpdatePassword() {
        startActivity(new Intent(NewsStudentActivity.this, StudentUpdatePassword.class));
        finish();
    }

    public void viewSubjects() {
        startActivity(new Intent(NewsStudentActivity.this, StudentViewSubjects.class));
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
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayoutStudent);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    //END MENU
}
