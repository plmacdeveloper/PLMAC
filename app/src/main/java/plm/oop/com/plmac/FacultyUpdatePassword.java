package plm.oop.com.plmac;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class FacultyUpdatePassword extends AppCompatActivity {

    private EditText fup_op;
    private EditText fup_np;
    private EditText fup_vnp;
    private Button fup_btsave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_update_password);


        fup_op=findViewById(R.id.etFacultyUpdatePasswordOldPassword);
        fup_np=findViewById(R.id.etFacultyUpdatePasswordNewPassword);
        fup_vnp=findViewById(R.id.etFacultyUpdatePasswordVerifyPassword);
        fup_btsave=findViewById(R.id.btFacultyUpdatePasswordSave);
        fup_btsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
                finish();

            }
        });



    }
}
