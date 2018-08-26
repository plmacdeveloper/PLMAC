package plm.oop.com.plmac;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
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
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class FacultyCheckAttendance extends AppCompatActivity {

    private SurfaceView cameraPreview;
    private TextView textResult;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private SurfaceHolder holder;
    final int RequestCameraPermissionID = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_check_attendance);
        final ArrayList<String> studNum1 = new ArrayList<>();
        final ArrayList<String> studName1 = new ArrayList<>();
        Intent i = getIntent();
        final String subjectCode = i.getStringExtra("subjectCode");
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dayDate = new SimpleDateFormat("MMMM-dd-yyyy");
        final String currentDate = dayDate.format(c.getTime());
        if(!subjectCode.isEmpty()) {
            if(studNum1.size() == 0){
            Log.i("check", subjectCode);

            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            final DatabaseReference checkDate = firebaseDatabase.getReference("Subject").child(subjectCode).child("Attendance");
            DatabaseReference addStudentstoAttendance = firebaseDatabase.getReference("Subject").child(subjectCode).child("Students");
            addStudentstoAttendance.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        String studNum = ds.getKey().toString();
                        studNum1.add(studNum);
                        String studName = ds.getValue().toString();
                        studName1.add(studName);
                        checkDate.child(currentDate).child(studName).setValue("absent");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            checkDate.child(currentDate).child(" ").removeValue();
        }
        }else{
            Toast.makeText(FacultyCheckAttendance.this,"No subject is active.",Toast.LENGTH_SHORT).show();
            onBackPressed();
            finish();
        }
        cameraPreview = findViewById(R.id.svFacultyCameraPreview);
        textResult = findViewById(R.id.tvFacultyScanResult);
        cameraPreview.setZOrderMediaOverlay(true);
        holder = cameraPreview.getHolder();

        barcodeDetector = new BarcodeDetector.Builder(FacultyCheckAttendance.this)
                .setBarcodeFormats(Barcode.QR_CODE)
                .build();

        if (!barcodeDetector.isOperational()) {
            Toast.makeText(FacultyCheckAttendance.this, "Sorry. Detector can't load.", Toast.LENGTH_SHORT).show();
            this.finish();
        }
        cameraSource = new CameraSource
                .Builder(FacultyCheckAttendance.this, barcodeDetector)
                .setFacing(CameraSource.CAMERA_FACING_FRONT)
                .setRequestedFps(24)
                .setAutoFocusEnabled(true)
                .setRequestedPreviewSize(1920, 1024)
                .build();

        cameraPreview.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {

                try {
                    if (ContextCompat.checkSelfPermission(FacultyCheckAttendance.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
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
                            String register = qrcodes.valueAt(0).displayValue;
                            textResult.setText(register);
                            FirebaseDatabase fd = FirebaseDatabase.getInstance();
                            DatabaseReference checkPresent = fd.getReference("Subject").child(subjectCode).child("Attendance").child(currentDate);
                            for(int a = 0;a<studName1.size();a++) {
                                if (register.compareTo(studNum1.get(a)) == 0){
                                    checkPresent.child(studName1.get(a)).setValue("present");
                                    Toast.makeText(FacultyCheckAttendance.this,studName1.get(a)+" is present.",Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
                }
            }
        });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case RequestCameraPermissionID: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    try {
                        if (ActivityCompat.checkSelfPermission(FacultyCheckAttendance.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
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
