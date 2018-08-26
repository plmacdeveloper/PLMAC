package plm.oop.com.plmac;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class FacultyCheckAttendance extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_check_attendance);
        Thread t = new Thread(){
            public void run(){
                try{
                    while(!isInterrupted()){
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Button bt = (Button) findViewById(R.id.button);
                                TextView tv = (TextView) findViewById(R.id.textaa);
                                TextView tv1 = (TextView) findViewById(R.id.textab);
                                TextView tv2 = (TextView) findViewById(R.id.textac);
                                Calendar c = Calendar.getInstance();
                                SimpleDateFormat sdf = new SimpleDateFormat("EEEE MM/dd/yyyy hh mm aa");
                                String lol = sdf.format(c.getTime());
                                String startTime = "7 08/26/2018 10 00 AM";
                                String endTime = "7 08/26/2018 6 30 PM";
                                tv.setText(lol);
                                Date l = null;
                                Date sT = null;
                                Date eT = null;
                                try {
                                    l = sdf.parse(lol);
                                    sT = sdf.parse(startTime);
                                    eT = sdf.parse(endTime);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                if(l.before(sT)){
                                    tv1.setText(startTime);
                                    bt.setEnabled(true);
                                }
                                if(l.after(eT)){
                                    tv2.setText(endTime);
                                    bt.setEnabled(false);
                                }
                                if(l.after(sT) && l.before(eT)) {
                                    tv1.setText("meow");
                                    tv2.setText("arf");
                                    bt.setEnabled(false);
                                }
                            }
                        });
                    }
                }catch(InterruptedException e){

                }
            }
        };
        t.start();
    }
//    @Override
//    public void onBackPressed() {
//        Intent ii= getIntent();
//        final String userNumber1= ii.getStringExtra("userNumber");
//        final String userName1= ii.getStringExtra("userName");
//        Intent i = new Intent(FacultyCheckAttendance.this,FacultyHomeActivity.class);
//        i.putExtra("userName",userName1);
//        i.putExtra("userNumber",userNumber1);
//        startActivity(i);
//    }
}
