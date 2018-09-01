package plm.oop.com.plmac;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

public class InformationActivity extends AppCompatActivity {

    private Button adminLogin, adminLoginHidden;
    private RelativeLayout relativeLayout1;
    private ScrollView scrollView;
    private EditText adminPassword;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        progressDialog = new ProgressDialog(this);
        adminLogin = findViewById(R.id.btAdminLogin);
        adminLoginHidden = findViewById(R.id.btAdminLoginHidden);
        relativeLayout1 = findViewById(R.id.rellay1);
        scrollView = findViewById(R.id.svAbout);
        adminPassword = findViewById(R.id.etAdminPassword);


        adminLoginHidden.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                scrollView.setVisibility(View.GONE);
                relativeLayout1.setVisibility(View.VISIBLE);
                return false;
            }
        });
        adminLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setMessage("Loading...");
                progressDialog.show();
                String pass = adminPassword.getText().toString().trim();
                if (pass.isEmpty()) {
                    Toast.makeText(InformationActivity.this, "Input password.", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                } else {
                    if (pass.matches("wow")) {
                        startActivity(new Intent(InformationActivity.this, AdminActivity.class));
                        Toast.makeText(InformationActivity.this, "Hello Admin!", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    } else {
                        Toast.makeText(InformationActivity.this, "Wrong password.", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }
            }
        });

    }
}
