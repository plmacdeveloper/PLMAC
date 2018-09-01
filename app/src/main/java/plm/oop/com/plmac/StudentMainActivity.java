package plm.oop.com.plmac;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class StudentMainActivity extends AppCompatActivity {

    private Button s_viewprofile;
    private Button s_viewattendance;
    private Button s_viewannouncements;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_main);
        Intent i= getIntent();
        final String userNumber= i.getStringExtra("userNumber");
        Log.i("SMA",userNumber);
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
        s_viewprofile=findViewById(R.id.btStudentMainViewProfile);
        s_viewattendance=findViewById(R.id.btStudentMainViewAttendance);
        s_viewannouncements=findViewById(R.id.btStudentMainViewAnnouncements);

        s_viewprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent a = new Intent(StudentMainActivity.this, StudentViewProfile.class);
                a.putExtra("userNumber",userNumber);
                startActivity(a);
            }
        });

        s_viewattendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StudentMainActivity.this,StudentViewAttendance.class));
            }
        });

        s_viewannouncements.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StudentMainActivity.this,NewsStudentActivity.class));
            }
        });

    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Logout?")
                .setNegativeButton("Cancel",null)
                .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SharedPreferences studentPref = getSharedPreferences("Student",0);
                        SharedPreferences.Editor editor = studentPref.edit();
                        editor.clear();
                        editor.apply();
                        startActivity(new Intent(StudentMainActivity.this,IntroScreenActivity.class));
                        finish();
                    }
                }).create().show();
        Log.i("Back","Back");
    }
}
