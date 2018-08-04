package plm.oop.com.plmac;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class StudentUpdatePassword extends AppCompatActivity {

    private EditText sup_op;
    private EditText sup_np;
    private EditText sup_vnp;
    private Button sup_btsave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_updatepassword);

        sup_op=findViewById(R.id.etStudentUpdatePasswordOldPassword);
        sup_np=findViewById(R.id.etStudentUpdatePasswordNewPassword);
        sup_vnp=findViewById(R.id.etStudentUpdatePasswordVerifyPassword);
        sup_btsave=findViewById(R.id.btFacultyUpdatePasswordSave);

        sup_btsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
                finish();

            }
        });

    }
}
