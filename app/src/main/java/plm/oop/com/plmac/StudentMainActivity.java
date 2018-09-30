package plm.oop.com.plmac;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class StudentMainActivity extends StudentBaseActivity {


    private TextView tvStudentViewProfileName;
    private TextView tvStudentViewProfileProgram;
    private TextView tvStudentViewProfileStudentNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_main);

        displayDrawer();
        SharedPreferences studentPref = getSharedPreferences("Student", 0);
        String userNumber = studentPref.getString("userNumber", "");
        String userName = studentPref.getString("userName", "");
        //START OF QR CODE
        QRCodeWriter writer = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = writer.encode(userNumber, BarcodeFormat.QR_CODE, 512, 512);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            ((ImageView) findViewById(R.id.img_result_qr)).setImageBitmap(bmp);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        //END OF QR CODE

        //SETTING VALUE FOR VIEWS
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mRef = database.getReference("Student");
        Query query = mRef.orderByChild("userNumber").equalTo(userNumber);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot student : dataSnapshot.getChildren()) {
                    tvStudentViewProfileName.setText("Profile Name: " + student.child("userName").getValue(String.class));
                    tvStudentViewProfileProgram.setText("Program: " + student.child("userProgram").getValue(String.class));
                    tvStudentViewProfileStudentNumber.setText("Student Number: " + student.child("userNumber").getValue(String.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        tvStudentViewProfileProgram = findViewById(R.id.tvStudentViewProfileProgram);
        tvStudentViewProfileStudentNumber = findViewById(R.id.tvStudentViewProfileStudentNumber);
        tvStudentViewProfileName = findViewById(R.id.tvStudentViewProfileName);
        //END OF SETTING VALUE FOR VIEWS

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        logout();
    }
}
