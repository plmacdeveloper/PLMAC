package plm.oop.com.plmac;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseArray;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;

public class FacultyViewProfile extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{


    private Button fvp_up;
    private TextView tvFacultyViewProfileName;
    private  TextView tvFacultyViewProfileCollege;
    private TextView tvFacultyViewProfileIDNumber;
    private SurfaceView cameraPreview;
    private TextView textResult;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private SurfaceHolder holder;
    final int RequestCameraPermissionID = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_view_profile);
        SharedPreferences facultyPref = getSharedPreferences("Faculty", 0);
        final String userName = facultyPref.getString("userName", "");
        final String userNumber = facultyPref.getString("userNumber", "");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mRef = database.getReference("Faculty");
        Query query =mRef.orderByChild("userNumber").equalTo(userNumber);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
                                                 @Override
                                                 public void onDataChange(DataSnapshot dataSnapshot) {

                                                         for (DataSnapshot faculty : dataSnapshot.getChildren()) {
                                                             tvFacultyViewProfileName.setText("Profile Name: "+faculty.child("userName").getValue(String.class));
                                                             tvFacultyViewProfileCollege.setText("Program: "+ faculty.child("userProgram").getValue(String.class));
                                                             tvFacultyViewProfileIDNumber.setText("User Number: "+faculty.child("userNumber").getValue(String.class));
                                                         }


                                                 }

                                                 @Override
                                                 public void onCancelled(DatabaseError databaseError) {

                                                 }
                                             });
        tvFacultyViewProfileCollege=findViewById(R.id.tvFacultyViewProfileCollege);
        tvFacultyViewProfileIDNumber=findViewById(R.id.tvFacultyViewProfileIDNumber);
        tvFacultyViewProfileName=findViewById(R.id.tvFacultyViewProfileName);

        cameraPreview = findViewById(R.id.svCameraPreview);
        textResult = findViewById(R.id.tvScanResult);
        cameraPreview.setZOrderMediaOverlay(true);
        holder = cameraPreview.getHolder();

        barcodeDetector = new BarcodeDetector.Builder(FacultyViewProfile.this)
                .setBarcodeFormats(Barcode.QR_CODE)
                .build();

        if(!barcodeDetector.isOperational()){
            Toast.makeText(FacultyViewProfile.this,"Sorry. Detector can't load.",Toast.LENGTH_SHORT).show();
            this.finish();
        }
        cameraSource = new CameraSource
                .Builder(FacultyViewProfile.this, barcodeDetector)
                .setFacing(CameraSource.CAMERA_FACING_FRONT)
                .setRequestedFps(24)
                .setAutoFocusEnabled(true)
                .setRequestedPreviewSize(1920, 1024)
                .build();

        cameraPreview.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {

                try {
                    if (ContextCompat.checkSelfPermission(FacultyViewProfile.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(cameraPreview.getHolder());
                        return;
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                cameraSource.stop();
            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> qrcodes = detections.getDetectedItems();
                if (qrcodes.size() > 0) {
                    textResult.post(new Runnable() {
                        @Override
                        public void run() {
                            Vibrator vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                            vibrator.vibrate(1000);
                            textResult.setText(qrcodes.valueAt(0).displayValue);
                        }
                    });
                }
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
        new AlertDialog.Builder(this)
                .setTitle("Logout?")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SharedPreferences facultyPref = getSharedPreferences("Faculty", 0);
                        SharedPreferences.Editor editor = facultyPref.edit();
                        editor.clear();
                        editor.apply();
                        startActivity(new Intent(FacultyViewProfile.this, IntroScreenActivity.class));
                        finish();
                    }
                }).create().show();
        Log.i("Back", "Back");
    }


    public void viewHome() {
//        startActivity(new Intent(FacultyViewProfile.this, FacultyViewProfile.class));
    }

    public void viewNews() {
        startActivity(new Intent(FacultyViewProfile.this, NewsFacultyActivity.class));
        finish();
    }

    public void viewUpdatePassword() {
        startActivity(new Intent(FacultyViewProfile.this, FacultyUpdatePassword.class));
        finish();
    }

    public void viewSubjects() {
        startActivity(new Intent(FacultyViewProfile.this, FacultyViewSubject.class));
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
        logout();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case RequestCameraPermissionID: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    try {
                        if (ActivityCompat.checkSelfPermission(FacultyViewProfile.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        cameraSource.start(cameraPreview.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            break;
        }
    }
}
