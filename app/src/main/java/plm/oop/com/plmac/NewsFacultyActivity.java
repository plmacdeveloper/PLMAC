package plm.oop.com.plmac;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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

import java.util.ArrayList;

public class NewsFacultyActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_faculty);
        SharedPreferences facultyPref = getSharedPreferences("Faculty", 0);
        final String userName = facultyPref.getString("userName", "");
        final String userNumber = facultyPref.getString("userNumber", "");
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

                NewsAdapter place = new NewsAdapter(NewsFacultyActivity.this, dateArray, titleArray, contentArray);
                listView = findViewById(R.id.lvNewsFaculty);
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
        //END OF MENU
    }

    public void logout() {
        new android.support.v7.app.AlertDialog.Builder(this)
                .setTitle("Logout?")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SharedPreferences facultyPref = getSharedPreferences("Faculty", 0);
                        SharedPreferences.Editor editor = facultyPref.edit();
                        editor.clear();
                        editor.apply();
                        startActivity(new Intent(NewsFacultyActivity.this, IntroScreenActivity.class));
                        finish();
                    }
                }).create().show();
        Log.i("Back", "Back");
    }


    public void viewHome() {
        startActivity(new Intent(NewsFacultyActivity.this, FacultyViewProfile.class));
        finish();
    }

    public void viewNews() {
//        startActivity(new Intent(NewsFacultyActivity.this, NewsFacultyActivity.class));
    }

    public void viewUpdatePassword() {
        startActivity(new Intent(NewsFacultyActivity.this, FacultyUpdatePassword.class));
        finish();
    }

    public void viewSubjects() {
        startActivity(new Intent(NewsFacultyActivity.this, FacultyViewSubject.class));
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
    //END MENU
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
