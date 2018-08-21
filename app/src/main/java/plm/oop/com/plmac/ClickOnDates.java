package plm.oop.com.plmac;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ClickOnDates extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_click_on_dates);

        Button codbt1 = (Button) findViewById(R.id.codbt1);
        Button codbt2 = (Button) findViewById(R.id.codbt2);


            codbt1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(ClickOnDates.this,ViewStudents.class);
                    startActivity(i);
                }
            });

            codbt2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent j = new Intent(ClickOnDates.this,QRScanner.class);
                    startActivity(j);
                }
            });

    }
}
